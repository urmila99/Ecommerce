package com.urmila.ecommerce.service;

import com.urmila.ecommerce.Model.Cart;
import com.urmila.ecommerce.Model.CartItem;
import com.urmila.ecommerce.Model.Product;
import com.urmila.ecommerce.Repository.CartItemRepository;
import com.urmila.ecommerce.Repository.CartRepository;
import com.urmila.ecommerce.Repository.ProductRepository;
import com.urmila.ecommerce.exceptions.APIException;
import com.urmila.ecommerce.exceptions.ResourceNotFoundException;
import com.urmila.ecommerce.payload.CartDTO;
import com.urmila.ecommerce.payload.ProductDTO;
import com.urmila.ecommerce.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProduct_price(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItemList();
        System.out.println("size of cartItem" + cartItems.size());

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        //System.out.println("productStream "+ productStream.toList());
        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    public Cart createCart() {
        Cart usercart = cartRepository.findCartByEmail((authUtil.loggedInEmail()));
        if (usercart != null) {
            return usercart;
        }
        Cart crt = new Cart();
        crt.setTotalPrice(0.0);
        crt.setUser(authUtil.loggedInUser());
        return cartRepository.save(crt);
    }

    public List<CartDTO> getAllCarts() {
        List<Cart> cartList = cartRepository.findAll();
        if (cartList.isEmpty())
            throw new APIException("No carts found");
        List<CartDTO> cartDTOList = cartList.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItemList().stream().map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
            cartDTO.setProducts(productDTOS);
            return cartDTO;
        }).toList();
        return cartDTOList;
    }

    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null)
            throw new ResourceNotFoundException("cart", "cartId", cartId);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItemList().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> stream = cart.getCartItemList().stream().map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
        cartDTO.setProducts(stream);
        return cartDTO;
    }

    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        Long cartId = userCart.getCartId();
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart", "cartId", cartId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + "is not available");
        }
        if(product.getQuantity()<quantity)
            throw new APIException("Please make an order of the "+product.getProductId()+"less than or equal to quantity"+product.getQuantity());
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(productId,cartId);
        if(cartItem==null)
            throw new APIException("Product "+product.getProductName()+"is not available in cart");
        cartItem.setProduct_price(product.getSpecialPrice());
        cartItem.setQuantity(cartItem.getQuantity()+quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice()+(cartItem.getProduct_price()*cartItem.getQuantity()));
        cartRepository.save(cart);
        CartItem updatedItem=cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity()==0)
             cartItemRepository.deleteById(updatedItem.getCartItemId());
        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems=cart.getCartItemList();
        Stream<ProductDTO> productDTOStream=cartItems.stream().map(item->{
            ProductDTO prd=modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }
}

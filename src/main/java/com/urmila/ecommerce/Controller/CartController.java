package com.urmila.ecommerce.Controller;

import com.urmila.ecommerce.Model.Cart;
import com.urmila.ecommerce.Repository.CartRepository;
import com.urmila.ecommerce.payload.CartDTO;
import com.urmila.ecommerce.payload.ProductDTO;
import com.urmila.ecommerce.service.CartService;
import com.urmila.ecommerce.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    AuthUtil authUtil;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable long productId, @PathVariable Integer quantity)
    {
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts()
    {
        List<CartDTO> cartDTOList=cartService.getAllCarts();
        return ResponseEntity.ok(cartDTOList);
    }

    @GetMapping("/carts/users/carts")
    public ResponseEntity<CartDTO> getCartByUser()
    {
        String emailId=authUtil.loggedInEmail();
        Cart cart=cartRepository.findCartByEmail(emailId);
        Long cartId=cart.getCartId();
        CartDTO cartDTO=cartService.getCart(emailId,cartId);
        List<ProductDTO> product=cart.getCartItemList().stream().map(p->modelMapper.map(p.getProduct(),ProductDTO.class)).toList();
        cartDTO.setProducts(product);
        return ResponseEntity.ok(cartDTO);
    }
    //user click delete decreases by one and user click add add 1
    @PutMapping("/cart/product/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,@PathVariable String operation)
    {
        CartDTO cartDTO=cartService.updateProductQuantityInCart(productId,operation.equalsIgnoreCase("delete")?-1:1);
        return ResponseEntity.ok(cartDTO);
    }
}

package com.urmila.ecommerce.payload;

public class CartItemDTO {
    private Long cartItemId;
    private Integer quantity;
    private double discount;
    private double product_price;
    private CartDTO cart;
    private ProductDTO product;

    public CartItemDTO() {
    }

    public CartItemDTO(Long cartItemId, Integer quantity, double discount, double product_price, CartDTO cart, ProductDTO product) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.discount = discount;
        this.product_price = product_price;
        this.cart = cart;
        this.product = product;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public CartDTO getCart() {
        return cart;
    }

    public void setCart(CartDTO cart) {
        this.cart = cart;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}

package com.urmila.ecommerce.payload;

import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    private Long cartId;
    private double totalPrice;
    private List<ProductDTO> products=new ArrayList<ProductDTO>();

    public CartDTO() {
    }

    public CartDTO(Long cartId, double totalPrice, List<ProductDTO> products) {
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}

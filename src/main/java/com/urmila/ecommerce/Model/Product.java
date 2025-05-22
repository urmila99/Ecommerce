package com.urmila.ecommerce.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank
    @Size(min=3,message = "Product name must contain atleast 3 characters")
    private String productName;
    @NotBlank
    @Size(min=3,message = "Product description must contain atleast 3 characters")
    private String description;
    private String image;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private User user;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<CartItem> products=new ArrayList<>();

    public Product() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(Double specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getDiscount() {
        return discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}

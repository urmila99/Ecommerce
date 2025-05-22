package com.urmila.ecommerce.payload;

public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
    private String image;
    public ProductDTO() {
    }

    public ProductDTO(Long productId, String productName, String description, Integer quantity, Double price, Double discount, Double specialPrice, String image) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.specialPrice = specialPrice;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(Double specialPrice) {
        this.specialPrice = specialPrice;
    }
}

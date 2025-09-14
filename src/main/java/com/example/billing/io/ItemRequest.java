package com.example.billing.io;

import java.math.BigDecimal;

public class ItemRequest {
    private String name;
    private String description;
    private String categoryId;
    private BigDecimal price;

    public ItemRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ItemRequest(String name, String description, String categoryId, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
    }
}

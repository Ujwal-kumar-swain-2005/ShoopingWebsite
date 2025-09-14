package com.example.billing.io;

import java.util.List;

public class OrderRequest {

    private String customerName;
    private String phoneNumber;
    private List<OrderItemRequest> cartItems;
    private Double subtotal;
    private Double tax;
    private Double grandTotal;
    private String paymentMethod;

    public OrderRequest() {
    }

    public OrderRequest(String customerName, String phoneNumber, List<OrderItemRequest> cartItems, Double subtotal, Double tax, Double grandTotal, String paymentMethod) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.cartItems = cartItems;
        this.subtotal = subtotal;
        this.tax = tax;
        this.grandTotal = grandTotal;
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<OrderItemRequest> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<OrderItemRequest> cartItems) {
        this.cartItems = cartItems;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static class OrderItemRequest {
        private String itemId;
        private String name;
        private Double price;
        private Integer quantity;

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public OrderItemRequest(String itemId, String name, Double price, Integer quantity) {
            this.itemId = itemId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public OrderItemRequest() {
        }
    }
}
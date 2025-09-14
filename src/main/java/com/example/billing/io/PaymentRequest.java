package com.example.billing.io;

public class PaymentRequest {
    private Double amount;
    private String currency;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentRequest(Double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public PaymentRequest() {
    }
}

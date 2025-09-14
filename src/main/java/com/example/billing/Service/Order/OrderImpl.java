package com.example.billing.Service.Order;

import com.example.billing.io.OrderRequest;
import com.example.billing.io.OrderResponse;
import com.example.billing.io.PaymentVerificationRequest;

import java.time.LocalDate;
import java.util.List;

public interface OrderImpl {
    OrderResponse createOrder(OrderRequest request);
    void deleteOrder(String orderId);
    OrderResponse verifyPayment(PaymentVerificationRequest paymentVerificationRequest);
    List<OrderResponse> getLatestOrder();

    Double sumSalesByDate(LocalDate date);

    Long countByOrderDate(LocalDate date);

    List<OrderResponse> findRecentOrders();
}

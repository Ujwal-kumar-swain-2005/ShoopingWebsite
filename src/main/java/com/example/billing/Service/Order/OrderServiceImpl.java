package com.example.billing.Service.Order;

import com.example.billing.Entity.OrderEntity;
import com.example.billing.Entity.OrderItemEntity;
import com.example.billing.Repository.OrderEntityRepository;
import com.example.billing.io.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderServiceImpl implements OrderImpl
{
    @Autowired
    private OrderEntityRepository orderEntityRepository;
    @Override
    public OrderResponse createOrder(OrderRequest request) {
        OrderEntity orderEntity = convertToOrderEntity(request);
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setStatus(orderEntity.getPaymentMethod() ==
                PaymentMethod.CASH? PaymentDetails.PaymentStatus.COMPLETED: PaymentDetails.PaymentStatus.PENDING);
        orderEntity.setPaymentDetails(paymentDetails);
       List<OrderItemEntity> orderItemEntities =  request.getCartItems().stream().map(this::convertToOrderItemEntity).collect(Collectors.toList());
        orderEntity.setItems(orderItemEntities);
       return convertToResponse(orderEntityRepository.save(orderEntity));

    }

    private OrderItemEntity convertToOrderItemEntity(OrderRequest.OrderItemRequest orderItemRequest) {
    OrderItemEntity orderItemEntity = new OrderItemEntity();
    orderItemEntity.setItemId(orderItemEntity.getItemId());
    orderItemEntity.setName(orderItemRequest.getName());
    orderItemEntity.setPrice(orderItemRequest.getPrice());
    orderItemEntity.setQuantity(orderItemRequest.getQuantity());
    return orderItemEntity;
    }

    private OrderResponse convertToResponse(OrderEntity save) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(save.getOrderId());
        orderResponse.setCustomerName(save.getCustomerName());
        orderResponse.setPhoneNumber(save.getPhoneNumber());
        orderResponse.setTax(save.getTax());
        orderResponse.setSubtotal(save.getSubtotal());
        orderResponse.setGrandTotal(save.getGrandTotal());
        orderResponse.setPaymentDetails(save.getPaymentDetails());
        orderResponse.setItems(save.getItems().stream().map(this::convertToItemResponse).collect(Collectors.toList()));
        orderResponse.setPaymentMethod(save.getPaymentMethod());
        orderResponse.setCreatedAt(save.getCreatedAt());
        return orderResponse;
    }

    private OrderResponse.OrderItemResponse convertToItemResponse(OrderItemEntity orderItemEntity) {
    OrderResponse.OrderItemResponse orderItemResponse = new OrderResponse.OrderItemResponse();
    orderItemResponse.setItemId(orderItemEntity.getItemId());
    orderItemResponse.setName(orderItemEntity.getName());
    orderItemResponse.setPrice(orderItemEntity.getPrice());
    orderItemResponse.setQuantity(orderItemEntity.getQuantity());
    return orderItemResponse;
    }

    private OrderEntity convertToOrderEntity(OrderRequest orderItemRequest) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerName(orderItemRequest.getCustomerName());
        orderEntity.setPhoneNumber(orderItemRequest.getPhoneNumber());
        orderEntity.setSubtotal(orderItemRequest.getSubtotal());
        orderEntity.setTax(orderItemRequest.getTax());
        orderEntity.setGrandTotal(orderEntity.getGrandTotal());
        orderEntity.setPaymentMethod(PaymentMethod.valueOf(orderItemRequest.getPaymentMethod()));
        return orderEntity;
    }


    @Override
    public void deleteOrder(String orderId) {
    OrderEntity orderEntity = orderEntityRepository.findByOrderId(orderId).orElseThrow(()->new RuntimeException("Order not found"));
    orderEntityRepository.delete(orderEntity);
    }
    @Override
    public OrderResponse verifyPayment(PaymentVerificationRequest request) {
        OrderEntity existingOrder = orderEntityRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!verifyRazorpaySignature(request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature())) {
            throw new RuntimeException("Payment verification failed");
        }

        PaymentDetails paymentDetails = existingOrder.getPaymentDetails();
        paymentDetails.setRazorpayOrderId(request.getRazorpayOrderId());
        paymentDetails.setRazorpayPaymentId(request.getRazorpayPaymentId());
        paymentDetails.setRazorpaySignature(request.getRazorpaySignature());
        paymentDetails.setStatus(PaymentDetails.PaymentStatus.COMPLETED);

        existingOrder = orderEntityRepository.save(existingOrder);
        return convertToResponse(existingOrder);

    }

    @Override
    public List<OrderResponse> getLatestOrder() {
       return  orderEntityRepository
                .findAllByOrderByCreatedAtDesc()
                .stream().map(this :: convertToResponse).toList();
    }
    @Override
    public Double sumSalesByDate(LocalDate date) {
        return orderEntityRepository.sumSalesByDate(date);
    }

    @Override
    public Long countByOrderDate(LocalDate date) {
        return orderEntityRepository.countByOrderDate(date);
    }

    @Override
    public List<OrderResponse> findRecentOrders() {
        return orderEntityRepository.findRecentOrders(PageRequest.of(0, 5))
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private boolean verifyRazorpaySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        return true;
    }
}

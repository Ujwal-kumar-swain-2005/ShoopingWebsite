package com.example.billing.Service.Razorpay;

import com.example.billing.io.RazorpayOrderResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayServiceImpl implements RazorpayService{
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    @Value("${razorpay.key.secret}")
    private String RazorpayKeySecret;

    @Override
    public RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId,RazorpayKeySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",amount*100);
        orderRequest.put("currency",currency);
        orderRequest.put("reciept","order_recipt"+System.currentTimeMillis());
        orderRequest.put("payment_capture",1);
        Order order = razorpayClient.orders.create(orderRequest);
        return convertToResponse(order);
    }
    private RazorpayOrderResponse convertToResponse(Order order){
        RazorpayOrderResponse razorpayOrderResponse = new RazorpayOrderResponse();
        razorpayOrderResponse.setId(order.get("id"));
        razorpayOrderResponse.setEntity(order.get("entity"));
        razorpayOrderResponse.setCurrency(order.get("currency"));
        razorpayOrderResponse.setAmount(order.get("amount"));
        razorpayOrderResponse.setStatus(order.get("status"));
        razorpayOrderResponse.setCreated_at(order.get("created_id"));
        razorpayOrderResponse.setReceipt(order.get("receipt"));
        return razorpayOrderResponse;
    }
}

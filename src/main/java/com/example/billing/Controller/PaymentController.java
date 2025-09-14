package com.example.billing.Controller;

import com.example.billing.Service.Order.OrderServiceImpl;
import com.example.billing.Service.Razorpay.RazorpayService;
import com.example.billing.io.OrderResponse;
import com.example.billing.io.PaymentRequest;
import com.example.billing.io.PaymentVerificationRequest;
import com.example.billing.io.RazorpayOrderResponse;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymnets")
public class PaymentController {
    @Autowired
    private RazorpayService razorpayService;
    @Autowired
    private OrderServiceImpl orderService;
    @PostMapping("/create_order")
    @ResponseStatus(HttpStatus.CREATED)
    public RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) throws RazorpayException{
        return razorpayService.createOrder(request.getAmount(),request.getCurrency());
    }
    @PostMapping("/verify")
    public OrderResponse verifyPayment(@RequestBody PaymentVerificationRequest request) {
        return orderService.verifyPayment(request);
    }
}

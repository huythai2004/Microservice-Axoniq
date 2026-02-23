package com.project.paymentservice.controller;

import com.project.paymentservice.entity.Payment;
import com.project.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsByOrderId(@PathVariable ("oderId") String orderId) {
        List<Payment> payments = paymentService.getPayments(orderId);
        return ResponseEntity.ok(payments);
    }

    @RequestMapping()
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
}

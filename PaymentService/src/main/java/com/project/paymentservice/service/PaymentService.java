package com.project.paymentservice.service;

import com.project.paymentservice.entity.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> getPayments(String userId);
    List<Payment> getAllPayments();
    Payment getPaymentsById(String paymentId);
}

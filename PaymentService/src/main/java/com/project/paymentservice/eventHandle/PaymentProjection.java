package com.project.paymentservice.eventHandle;

import com.project.paymentservice.entity.Payment;
import com.project.paymentservice.event.PaymentCancelledEvent;
import com.project.paymentservice.event.PaymentCompletedEvent;
import com.project.paymentservice.event.PaymentProcessedEvent;
import com.project.paymentservice.repository.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentProjection {
    @Autowired
    private PaymentRepository paymentRepository;

    @EventHandler
    public void on (PaymentProcessedEvent event) {
        System.out.println("PaymentProcessedEvent received: " + event);
        //Update read model
        Payment payment = new Payment();
        payment.setPaymentId(event.getPaymentId());
        payment.setOrderId(event.getOrderId());
        payment.setUserId(event.getUserId());
        payment.setAmount(event.getAmount());
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on (PaymentCancelledEvent event){
        System.out.println("PaymentCancelledEvent received: " + event);
        // Update read model
        Payment payment = paymentRepository.getReferenceById(event.getPaymentId());
        if (payment != null) {
            payment.setStatus(event.getStatus());
        }
    }

    @EventHandler
    public void on (PaymentCompletedEvent event) {
        System.out.println("PaymentCompletedEvent received: " + event);
        // Update read model
        Payment payment = paymentRepository.getReferenceById(event.getPaymentId());
        if (payment != null) {
            payment.setStatus(event.getStatus());
            paymentRepository.save(payment);
        } else {
            System.out.println("Payment not found for ID: " + event.getPaymentId());
        }
    }
}

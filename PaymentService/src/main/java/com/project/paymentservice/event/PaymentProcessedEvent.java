package com.project.paymentservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PaymentProcessedEvent {
    private String paymentId;
    private String userId;
    private String orderId;
    private double amount;
    private String status;
}

package com.project.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PaymentProcessEvent {
    private String paymentId;
    private String orderId;
    private String userId;
    private double amount;
    private String status;
}

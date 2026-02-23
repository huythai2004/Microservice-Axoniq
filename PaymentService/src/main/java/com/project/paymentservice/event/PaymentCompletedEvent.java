package com.project.paymentservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PaymentCompletedEvent {
    private String paymentId;
    private String orderId;
    private String userId;
    private double price;
    private String status = "SUCCESS";
}
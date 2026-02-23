package com.project.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ShippingProcessedEvent {
    private String shippingId;
    private String paymentId;
    private String orderId;
    private String userId;
    private String status;
    private double amount;
}

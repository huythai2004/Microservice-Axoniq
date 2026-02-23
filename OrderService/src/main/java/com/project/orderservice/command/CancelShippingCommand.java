package com.project.orderservice.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelShippingCommand {
    private String shippingId;
    private String orderId;
    private String paymentId;
    private String status;
}

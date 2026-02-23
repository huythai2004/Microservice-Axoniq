package com.project.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ShippingCompleteEvent {
    private String shippingId;
    private String orderId;
    private String userId;
    private String status = "SUCCESS";
}

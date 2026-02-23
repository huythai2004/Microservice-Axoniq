package com.project.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)

public class OrderRequest {
    private String orderId;
    private String userId;
    private double price;
    private int quantity;
    private String productId;
}

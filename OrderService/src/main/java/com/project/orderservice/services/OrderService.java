package com.project.orderservice.services;

import com.project.orderservice.dto.OrderRequest;

public interface OrderService {
    OrderRequest createOrder(OrderRequest orderRequest);
}

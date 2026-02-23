package com.project.orderservice.services.impl;

import com.project.orderservice.command.CreateOrderCommand;
import com.project.orderservice.common.OrderStatus;
import com.project.orderservice.dto.OrderRequest;
import com.project.orderservice.entity.Order;
import com.project.orderservice.repository.OrderRepository;
import com.project.orderservice.services.OrderService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderRequest createOrder (OrderRequest orderRequest) {
        String orderId = UUID.randomUUID().toString();
        orderRequest.setOrderId(orderId);
        CreateOrderCommand command = new CreateOrderCommand(
                orderId,
                orderRequest.getUserId(),
                orderRequest.getProductId(),
                orderRequest.getQuantity(),
                orderRequest.getPrice()
        );

        // Save the order directly using
        Order order = new Order();
        order.setOrderId(orderRequest.getOrderId());
        order.setUserId(orderRequest.getUserId());
        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());
        order.setPrice(orderRequest.getPrice());
        order.setStatus(OrderStatus.COMPLETED.name());
        orderRepository.save(order);

        //Store the order in DB (if needed)
        commandGateway.send(command);
        System.out.println("Order created!!" + orderRequest);
        return orderRequest;
    }
}

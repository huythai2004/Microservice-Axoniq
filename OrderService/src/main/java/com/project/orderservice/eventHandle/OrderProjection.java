package com.project.orderservice.eventHandle;

import com.project.orderservice.entity.Order;
import com.project.orderservice.event.OrderCancelledEvent;
import com.project.orderservice.event.OrderCompletedEvent;
import com.project.orderservice.event.OrderCreatedEvent;
import com.project.orderservice.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProjection {
    @Autowired
    private OrderRepository orderRepository;
    @EventHandler
    public void on (OrderCreatedEvent event) {
        // Handle the event and update the read model
        Order order = new Order();

        order.setOrderId(event.getOrderId());
        order.setUserId(event.getUserId());
        order.setProductId(event.getProductId());
        order.setQuantity(event.getQuantity());
        order.setPrice(event.getPrice());
        order.setStatus(event.getStatus());
        orderRepository.save(order);
        System.out.println("Event received, status: "  + event);
    }

    @EventHandler
    public void on (OrderCompletedEvent event) {
        Order order = orderRepository.getReferenceById(event.getOrderId());
        if (order != null) {
            order.setStatus(event.getStatus());
            orderRepository.save(order);
            System.out.println("Order status updated: " + event);
        } else {
            System.out.println("Order not found for OrderCompletedEvent: " +event);
        }
    }

    @EventHandler
    public void on (OrderCancelledEvent event) {
        Order order = orderRepository.getReferenceById(event.getOrderId());
        if(order != null) {
            order.setStatus(event.getStatus());
            orderRepository.save(order);
            System.out.println("Order status updated: " + event);
        } else  {
            System.out.println("Order not found for OrderCancelledEvent: " + event);
        }
    }
}

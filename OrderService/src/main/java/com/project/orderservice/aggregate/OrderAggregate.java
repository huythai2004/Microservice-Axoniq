package com.project.orderservice.aggregate;

import com.project.orderservice.command.CancelOrderCommand;
import com.project.orderservice.command.CompleteOrderCommand;
import com.project.orderservice.command.CreateOrderCommand;
import com.project.orderservice.common.OrderStatus;
import com.project.orderservice.event.OrderCancelledEvent;
import com.project.orderservice.event.OrderCompletedEvent;
import com.project.orderservice.event.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double price;
    private String orderStatus;
    private OrderAggregate(){}

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);

        //Here you would typically apply an event to persist the state change
        orderCreatedEvent.setStatus(OrderStatus.ORDERED.name());
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on (OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.getOrderId();
        this.userId = orderCreatedEvent.getUserId();
        this.productId = orderCreatedEvent.getProductId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.price = orderCreatedEvent.getPrice();
        this.orderStatus = orderCreatedEvent.getStatus();
        System.out.println("Order " + orderCreatedEvent.getOrderId() + " created successfully!");
    }

    @CommandHandler
    public void handle (CancelOrderCommand cancelOrderCommand) {
        OrderCancelledEvent event = new OrderCancelledEvent();
        event.setOrderId(cancelOrderCommand.getOrderId());
        event.setUserId(cancelOrderCommand.getUserId());
        event.setStatus(OrderStatus.CANCELLED.name());
        AggregateLifecycle.apply(event);
        System.out.println("Order" + cancelOrderCommand.getOrderId() + " cancelled successfully!");
    }

    @EventSourcingHandler
    public void on (OrderCancelledEvent event) {
        this.orderStatus = event.getStatus();
    }

    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        //Implementation for completing the order would go here
        OrderCompletedEvent event = new OrderCompletedEvent();
        event.setOrderId(completeOrderCommand.getOrderId());
        event.setUserId(userId);
        event.setStatus(OrderStatus.COMPLETED.name());
        AggregateLifecycle.apply(event);
        System.out.println("Order " + completeOrderCommand.getOrderId() + " completed successfully!");
    }

    @EventSourcingHandler
    public void on (OrderCompletedEvent event) {
        this.orderStatus = event.getStatus();
        System.out.println("OrderStatus: " + event.getStatus() + "for OrderID: " + event.getOrderId());
    }
}

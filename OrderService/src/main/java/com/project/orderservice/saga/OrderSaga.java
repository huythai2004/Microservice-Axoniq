package com.project.orderservice.saga;

import com.project.orderservice.command.*;
import com.project.orderservice.common.OrderStatus;
import com.project.orderservice.common.PaymentStatus;
import com.project.orderservice.common.ShippingStatus;
import com.project.orderservice.event.OrderCreatedEvent;
import com.project.orderservice.event.PaymentProcessEvent;
import com.project.orderservice.event.ShippingCancelledEvent;
import com.project.orderservice.event.ShippingProcessedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    private String paymentId;
    private String shippingId;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        paymentId = UUID.randomUUID().toString();
        SagaLifecycle.associateWith("paymentId", paymentId);
        ProcessPaymentCommand paymentCommand = new ProcessPaymentCommand(paymentId,
                event.getOrderId(),
                event.getUserId(),
                event.getPrice() * event.getQuantity());
        System.out.println("OrderSaga - Processing payment for orderId: " + event.getOrderId());
        commandGateway.send(paymentCommand);
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle (PaymentProcessEvent event) {
        paymentId = event.getPaymentId();
        //Handle payment process event
        if(PaymentStatus.SUCCESS.name().equals(event.getStatus())) {
            //Proceed with shipping or other steps
            shippingId = UUID.randomUUID().toString();
            SagaLifecycle.associateWith("shippingId", shippingId);
            ProcessShippingCommand processShippingCommand = new ProcessShippingCommand(
                    shippingId,
                    event.getOrderId(),
                    event.getUserId(),
                    event.getPaymentId(),
                    event.getAmount()
            );
            commandGateway.send(processShippingCommand);
        } else {
            // Handle payment failure, possibly compensating actions
            compensateOrder(event.getOrderId());
        }
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handle (ShippingProcessedEvent event) {
        if(ShippingStatus.DELIVERED.name().equals(event.getStatus())) {
            //Shipping succeeded, you might want to complete the order here
            //handle shipping completed event
            // You can complete the saga here if needed
            CompleteOrderCommand orderCommand = new CompleteOrderCommand();
            orderCommand.setOrderId(event.getOrderId());
            orderCommand.getStatus(OrderStatus.COMPLETED.name());
            commandGateway.send(orderCommand);
            SagaLifecycle.end();
        } else {
            // Handle shipping failure, possibly compensating actions
            compensatePayment(event.getOrderId(), event.getPaymentId());
            compensateOrder (event.getOrderId());
        }
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handleShippingCancelled (ShippingCancelledEvent event) {
        //Shipping cancelled, you might want to cancel the order, payment here
        // Handle shipping cancelled event
        // You can end the saga here if needed
        // Handle shipping failure, possibly compensating actions
        compensatePayment(event.getOrderId(), event.getPaymentId());
        compensateOrder(event.getOrderId());
    }

    private void compensateOrder(String orderId) {
        CancelOrderCommand command = new CancelOrderCommand();
        command.setOrderId(orderId);
        commandGateway.send(command);
        SagaLifecycle.end();
    }

    private void compensatePayment(String orderId, String paymentId) {
        System.out.println("Compensating payment for orderId: " + orderId + ", paymentId: " + paymentId);
        CancelPaymentCommand cancelPaymentCommand = new CancelPaymentCommand();
        cancelPaymentCommand.setPaymentId(paymentId);
        cancelPaymentCommand.setOrderId(orderId);
        commandGateway.send(cancelPaymentCommand);
    }
}

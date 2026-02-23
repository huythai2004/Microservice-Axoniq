package com.project.shippingservice.aggregate;

import com.project.shippingservice.command.CancelShippingCommand;
import com.project.shippingservice.command.ProcessShippingCommand;
import com.project.shippingservice.common.ShippingStatus;
import com.project.shippingservice.event.ShippingCancelledEvent;
import com.project.shippingservice.event.ShippingProcessedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor

public class ShippingAggregate {
    private String shippingId;
    private String orderId;
    private String userId;
    private double amount;
    private String status;

    @CommandHandler
    public ShippingAggregate(ProcessShippingCommand command) {
        String status = simulateShipping(command);
        ShippingProcessedEvent event = new ShippingProcessedEvent(command.getShippingId(), command.getUserId(), command.getOrderId(), command.getAmount(), status);
        org.axonframework.modelling.command.AggregateLifecycle.apply(event);
        System.out.println("ShippingAggregate - Shipping processed for shippingId: " + command.getShippingId() + ", status: " + status);
    }


    @CommandHandler
    public void handle(CancelShippingCommand command) {
        ShippingCancelledEvent event = new ShippingCancelledEvent(command.getShippingId(), command.getOrderId(), command.getPaymentId(), ShippingStatus.CANCELLED.name());

        //Logic cancel order
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ShippingCancelledEvent event) {
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(ShippingProcessedEvent event) {
        this.shippingId = event.getShippingId();
        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.amount = event.getAmount();
        this.status = event.getStatus();
        // Save to DB
    }

    private String simulateShipping(ProcessShippingCommand command) {
        // Can put data to shipping service DB here
        //return "FAILED" if user out of area
        System.out.println("Simulating shipping for orderId: " + command.getOrderId() + ", userId: " + command.getUserId());
        return "out-of-area".equals(command.getUserId()) ? ShippingStatus.FAILED.name() : ShippingStatus.DELIVERED.name();
    }
}

package com.project.paymentservice.aggregate;

import com.project.paymentservice.command.ProcessPaymentCommand;
import com.project.paymentservice.event.PaymentProcessedEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor

public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String userId;
    private double amount;
    private String status;
    @CommandHandler
    public PaymentAggregate (ProcessPaymentCommand command) {
        String status = simulatePayment(command);
        PaymentProcessedEvent event = new PaymentProcessedEvent(
                command.getPaymentId(),
                command.getUserId(),
                command.getOrderId(),
                command.getAmount(),status
        );
        AggregateLifecycle.apply(event);
        System.out.println("PaymentAggregate - Payment processed for paymentId: " + command.getPaymentId() + ", status: " + status);
    }

    private String simulatePayment(ProcessPaymentCommand command) {
        return "SUCCESS";
    }
}

package com.project.shippingservice.eventHandle;

import com.project.shippingservice.entity.Shipping;
import com.project.shippingservice.event.ShippingCancelledEvent;
import com.project.shippingservice.event.ShippingCompleteEvent;
import com.project.shippingservice.event.ShippingProcessedEvent;
import com.project.shippingservice.repository.ShippingRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShippingProjection {
    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private CommandGateway commandGateway;

    @EventHandler
    public void on (ShippingProcessedEvent event) {
        System.out.println("ShippingProcessEvent received:" + event);
        Shipping shipping = new Shipping();
        shipping.setShippingId(event.getShippingId());
        shipping.setOrderId(event.getOrderId());
        shipping.setUserId(event.getUserId());
        shipping.setAmount(event.getAmount());
        shipping.setStatus(event.getStatus());
        shippingRepository.save(shipping);

        //Update read model
    }

    @EventHandler
    public void on (ShippingCancelledEvent event) {
        System.out.println("ShippingCancelEvent received:" + event);
        Shipping shipping = shippingRepository.getReferenceById(event.getShippingId());
        if (shipping != null) {
            shipping.setStatus(event.getStatus());
            shippingRepository.save(shipping);
        } else {
            System.out.println("Shipping with id " + event.getShippingId() + " not found for cancellation.");
        }
    }

    @EventHandler
    public void on (ShippingCompleteEvent event) {
        System.out.println("ShippingCompleteEvent received:" + event);
        Shipping shipping = shippingRepository.getReferenceById(event.getShippingId());
        if (shipping != null) {
            shipping.setStatus(event.getStatus());
            shippingRepository.save(shipping);
        } else {
            System.out.println("Shipping with id " + event.getShippingId() + " not found for completion.");
        }
    }
}

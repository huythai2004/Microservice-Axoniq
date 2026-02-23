package com.project.shippingservice.service.impl;

import com.project.shippingservice.command.CancelShippingCommand;
import com.project.shippingservice.command.CompleteShippingCommand;
import com.project.shippingservice.common.ShippingStatus;
import com.project.shippingservice.entity.Shipping;
import com.project.shippingservice.repository.ShippingRepository;
import com.project.shippingservice.service.ShippingService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public List<Shipping> getShippings(String userId) {
        return shippingRepository.findByUserId(userId);
    }

    @Override
        public List<Shipping> getAllShippings() {
        return shippingRepository.findAll();
    }

    @Override
    public Shipping completeShipping(Shipping shipping, String shippingId) {
        CompleteShippingCommand command = new CompleteShippingCommand(
                shippingId,
                shipping.getUserId(),
                shipping.getStatus()
        );
        Shipping updatedShipping = shippingRepository.getReferenceById(shippingId);
        updatedShipping.setStatus(ShippingStatus.DELIVERED.name());
        commandGateway.sendAndWait(command);
        return updatedShipping;
    }

    @Override
    public Shipping cancelShipping(Shipping shipping, String shippingId) {
        //Call service to get payment by order id
        String paymentId = "shipping.getPaymentId()"; // Get from Payment Service
        CancelShippingCommand command = new CancelShippingCommand(
                shippingId,
                shipping.getUserId(),
                paymentId,
                shipping.getStatus()
        );
        Shipping updatedShipping = shippingRepository.getReferenceById(shippingId);
        updatedShipping.setStatus(ShippingStatus.CANCELLED.name());
        commandGateway.sendAndWait(command);
        return updatedShipping;
    }
}



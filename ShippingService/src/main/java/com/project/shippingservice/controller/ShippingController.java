package com.project.shippingservice.controller;

import com.project.shippingservice.common.ShippingStatus;
import com.project.shippingservice.entity.Shipping;
import com.project.shippingservice.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shippings")

public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @GetMapping
    public ResponseEntity<List<Shipping>> getAllShippings() {
        List<Shipping> shippings = shippingService.getAllShippings();
        return ResponseEntity.ok(shippings);
    }

    @PutMapping("/{shippingId}")
    public ResponseEntity<Shipping> completeShipping(@PathVariable("shippingId") String shippingId, @RequestBody Shipping shipping) {
        //implemention for complete shipping
        shipping.setStatus(ShippingStatus.DELIVERED.name());
        Shipping updatedShipping =  shippingService.completeShipping(shipping, shippingId);
        return ResponseEntity.ok(updatedShipping);
    }

    @PutMapping("/cancel/{shipping}")
    public ResponseEntity<Shipping> cancelShipping(@PathVariable("shipping") @RequestBody Shipping shipping, String shippingId) {
        //implemention for cancel shipping
        shipping.setStatus(ShippingStatus.CANCELLED.name());
        Shipping updatedShipping =  shippingService.cancelShipping(shipping, shippingId);
        return ResponseEntity.ok(updatedShipping);
    }
}

package com.project.shippingservice.service;

import com.project.shippingservice.entity.Shipping;

import java.util.List;

public interface ShippingService {
    List<Shipping> getShippings(String userId);
    List<Shipping> getAllShippings();
    Shipping completeShipping(Shipping shipping, String shippingId);
    Shipping cancelShipping(Shipping shipping, String shippingId);
}

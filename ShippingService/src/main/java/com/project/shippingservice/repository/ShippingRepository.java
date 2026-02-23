package com.project.shippingservice.repository;

import com.project.shippingservice.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, String> {
    List<Shipping> findByUserId(String userId);
}

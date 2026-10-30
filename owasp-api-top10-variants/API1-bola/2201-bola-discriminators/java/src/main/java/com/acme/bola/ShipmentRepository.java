package com.acme.bola;

import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<ShipmentRequest, String> {
}
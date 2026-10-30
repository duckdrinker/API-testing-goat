package com.acme.bola;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {
}
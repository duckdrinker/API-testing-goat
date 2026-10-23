package com.acme.bola;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    /** Case I: explicit field-by-field mapping of a DTO. */
    public Order create(OrderCreateRequest req) {
        Order order = new Order();
        order.setProductName(req.getProductName());
        order.setQuantity(req.getQuantity());
        order.setUserId(req.getUserId());
        order.setOwnerId(req.getOwnerId());
        order.setStatus(req.getStatus());
        order.setRole(req.getRole());
        return order;
    }

    /** Case H (negative): the DTO has nothing to bind. */
    public Order createSafe(OrderCreateSafeRequest req) {
        Order order = new Order();
        order.setProductName(req.getProductName());
        order.setQuantity(req.getQuantity());
        return order;
    }
}
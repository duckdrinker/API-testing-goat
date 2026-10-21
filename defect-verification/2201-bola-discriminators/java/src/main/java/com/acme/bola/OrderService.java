package com.acme.bola;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public Order save(OrderCreateRequest req) {
        return create(req);
    }

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

    public Order createSafe(OrderCreateSafeRequest req) {
        Order order = new Order();
        order.setProductName(req.getProductName());
        order.setQuantity(req.getQuantity());
        return order;
    }
}
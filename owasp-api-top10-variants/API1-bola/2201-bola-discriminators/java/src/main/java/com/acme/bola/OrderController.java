package com.acme.bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService service;
    private final OrderRepository orders;
    private final ShipmentRepository shipments;

    public OrderController(OrderService service, OrderRepository orders, ShipmentRepository shipments) {
        this.service = service;
        this.orders = orders;
        this.shipments = shipments;
    }

    /** Case G: the request type is the stored entity, so the client sets userId/ownerId/status/role. */
    @PostMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public Order createOrder(@RequestBody Order order) {
        return orders.save(order);
    }

    /** Case H (negative): request type with no owner-reference or protected field. */
    @PostMapping("/orders/safe")
    @PreAuthorize("isAuthenticated()")
    public Order createSafe(@RequestBody OrderCreateSafeRequest req) {
        return service.createSafe(req);
    }

    /** Case I: plain DTO mapped explicitly by the service: no bind evidence, no finding. */
    @PostMapping("/orders/new")
    @PreAuthorize("isAuthenticated()")
    public Order create(@RequestBody OrderCreateRequest req) {
        return service.create(req);
    }

    /** Case J: the whole request body reaches a persistence call. */
    @PostMapping("/shipments")
    @PreAuthorize("isAuthenticated()")
    public ShipmentRequest createShipment(@RequestBody ShipmentRequest req) {
        return shipments.save(req);
    }
}
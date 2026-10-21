package com.acme.bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    /** Case G: POST binds userId/ownerId/status/role from the request (the body goes to save(...)). */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Order save(@RequestBody OrderCreateRequest req) {
        return service.save(req);
    }

    /**
     * Case I: same binding as case G, but the body goes to create(...). The mass_assignment detector
     * only recognises update/save/insert/persist as the write, so no finding is produced here.
     */
    @PostMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public Order create(@RequestBody OrderCreateRequest req) {
        return service.create(req);
    }

    /** Case H (negative). */
    @PostMapping("/safe")
    @PreAuthorize("isAuthenticated()")
    public Order createSafe(@RequestBody OrderCreateSafeRequest req) {
        return service.createSafe(req);
    }
}
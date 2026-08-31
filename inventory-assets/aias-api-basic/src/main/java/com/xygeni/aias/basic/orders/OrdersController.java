package com.xygeni.aias.basic.orders;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// No auth annotations on purpose, and the response leaks a raw PII field
// (creditCardNumber) — gives the apisec detectors at least one real finding
// on this module, to exercise AIAS-010/011/012 (issuesAssetMapping).
// Full paths are given directly on each mapping (no class-level
// @RequestMapping) so that GET /health below is a bare top-level path, not
// prefixed by a module base path — see InvoicesController for why.
@RestController
public class OrdersController {

    @GetMapping("/api/orders")
    public List<OrderResponse> listOrders() {
        return List.of(new OrderResponse("ORD-1001", "Jane Doe", "4111-1111-1111-1111"));
    }

    @PostMapping("/api/orders")
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        return new OrderResponse("ORD-1002", request.getCustomerName(), request.getCreditCardNumber());
    }

    // Same METHOD + path as InvoicesController's /health, in a different
    // module — exercises AIAS-014 (endpoint identity scoped to module, not
    // global).
    @GetMapping("/health")
    public String health() {
        return "orders-ok";
    }
}

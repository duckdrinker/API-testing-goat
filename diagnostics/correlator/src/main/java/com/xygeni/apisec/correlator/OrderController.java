package com.xygeni.apisec.correlator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FlawCorrelator rule 3: excessive_exposure_on_unauthenticated_endpoint.
 * No auth annotation -> unauthenticated_endpoint. Response over-exposes a
 * sensitivity-tagged, unreferenced field ("creditCardNumber") -> excessive_data_exposure.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable String id) {
        return new OrderResponse(id, "SHIPPED", "4111111111111111");
    }
}

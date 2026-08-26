package com.xygeni.apisec.api9inventory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API9:2023 - Improper Inventory Management fixture.
 *
 * - GET /api/users is implemented in code AND declared in openapi.yaml
 *   (clean reference endpoint, matched on both sides - no finding).
 * - GET /api/legacy/report is implemented in code but intentionally
 *   omitted from openapi.yaml -> zombie_endpoint.
 * - GET /api/orders/{id} is declared in openapi.yaml as /api/orders/{orderId}
 *   (path-parameter name differs, structure doesn't) -> zombie/orphan
 *   negative: the normalized join should succeed, no finding.
 *
 * The companion openapi.yaml also declares GET /api/orphaned-endpoint,
 * which has no implementation anywhere in this project -> orphan_spec.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/users")
    @PreAuthorize("isAuthenticated()")
    public String listUsers() {
        return "[...users...]";
    }

    @GetMapping("/legacy/report")
    @PreAuthorize("isAuthenticated()")
    public String legacyReport() {
        return "{...legacy report, undocumented...}";
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("isAuthenticated()")
    public String getOrder(@PathVariable String id) {
        return "{...order " + id + "...}";
    }
}

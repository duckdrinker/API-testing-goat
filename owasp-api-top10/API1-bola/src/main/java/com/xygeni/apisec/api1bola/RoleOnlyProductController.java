package com.xygeni.apisec.api1bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BOLA fixture for xygeni-product-backlog#1551 (bare role check accepted as
 * proof of ownership).
 *
 * Guarded only by {@code hasRole('USER')} - a plain, non-privileged role
 * check, not an ownership check. Any authenticated USER can read any other
 * user's product by guessing/enumerating {id}; Phase B should still confirm
 * BOLA here instead of clearing it the way a genuine ownership check would.
 */
@RestController
@RequestMapping("/api/user-role-products")
public class RoleOnlyProductController {

    private final ProductRepository repository;

    public RoleOnlyProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Product getProduct(@PathVariable String id) {
        return repository.findById(id);
    }
}

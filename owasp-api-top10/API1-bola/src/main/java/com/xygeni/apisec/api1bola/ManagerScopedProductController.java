package com.xygeni.apisec.api1bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BOLA fixture for the trimmed privilegedRoles default (xygeni-product-backlog#1400).
 *
 * Same shape as ProductController.getProduct() ((GET) /resource/{id}, no ownership
 * check), but gated by hasRole('MANAGER') instead of isAuthenticated(). "manager" was
 * removed from the shipped privilegedRoles default on broken_object_level_authorization.yml,
 * so Phase B should now confirm this candidate instead of clearing it as an admin-gated
 * endpoint.
 */
@RestController
@RequestMapping("/api/manager-products")
public class ManagerScopedProductController {

    private final ProductRepository repository;

    public ManagerScopedProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Product getProduct(@PathVariable String id) {
        return repository.findById(id);
    }
}

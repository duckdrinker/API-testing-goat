package com.xygeni.apisec.api1bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BOLA fixture for a custom repo_scoping pattern (xygeni-product-backlog#1386).
 *
 * Same shape as ProductController.getProduct() ((GET) /resource/{id}, authenticated,
 * no ownership check via the built-in name check), but the handler calls
 * selectForCurrentTenant(id) - a repository method whose name only counts as an
 * ownership-check pattern once "selectForCurrentTenant" is added to the
 * broken_object_level_authorization detector's repo_scoping configuration.
 * Phase B should clear this candidate only when that custom config is present.
 */
@RestController
@RequestMapping("/api/tenant-products")
public class RepoScopingPositiveController {

    private final ProductRepository repository;

    public RepoScopingPositiveController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Product getTenantProduct(@PathVariable String id) {
        return repository.selectForCurrentTenant(id);
    }
}

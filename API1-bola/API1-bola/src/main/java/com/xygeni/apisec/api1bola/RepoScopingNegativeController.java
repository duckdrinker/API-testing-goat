package com.xygeni.apisec.api1bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BOLA regression guard for the repo_scoping fix (xygeni-product-backlog#1386).
 *
 * Same shape as ProductController.getProduct() ((GET) /resource/{id}, authenticated,
 * no real ownership check), but the handler calls findByUserAgent(id) - a name that
 * textually matches the built-in By...User... shape, but the walker's token-boundary
 * check must still reject it as a false ownership signal (a User-Agent header lookup
 * scopes nothing), even with a custom repo_scoping pattern configured elsewhere.
 * Phase B must still confirm this candidate - a custom pattern must not weaken the
 * built-in strict check.
 */
@RestController
@RequestMapping("/api/agent-products")
public class RepoScopingNegativeController {

    private final ProductRepository repository;

    public RepoScopingNegativeController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Product getAgentProduct(@PathVariable String id) {
        return repository.findByUserAgent(id);
    }
}

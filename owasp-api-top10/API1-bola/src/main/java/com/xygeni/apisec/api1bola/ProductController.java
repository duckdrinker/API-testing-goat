package com.xygeni.apisec.api1bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * API1:2023 - Broken Object Level Authorization fixture.
 *
 * Every method below only checks that the caller is authenticated
 * (isAuthenticated()) - none of them verify that the authenticated
 * caller actually owns the {id} being read, updated or deleted.
 * Any logged-in user can access, modify or delete any other user's
 * product by guessing/enumerating {id}.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Product getProduct(@PathVariable String id) {
        return repository.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Product updateProduct(@PathVariable String id, @RequestBody Product update) {
        return repository.save(id, update);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteProduct(@PathVariable String id) {
        repository.delete(id);
    }
}

package com.xygeni.apisec.api1bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BOLA negative fixture (Phase B "check present" branch).
 *
 * Same shape as ProductController.getProduct() ((GET) /resource/{id},
 * authenticated), but the handler declares an explicit ownership check
 * via SpEL ("#id == authentication.principal.id") - Phase B should
 * recognise this pattern and clear the candidate instead of confirming it.
 */
@RestController
@RequestMapping("/api/safe-products")
public class SafeProductController {

    private final ProductRepository repository;

    public SafeProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public Product getOwnProduct(@PathVariable String id) {
        return repository.findById(id);
    }
}

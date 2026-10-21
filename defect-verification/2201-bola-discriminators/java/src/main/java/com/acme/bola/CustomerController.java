package com.acme.bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Case A: BOLA read whose response type has sensitive fields (email, ssn, cardNumber). */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final Repo repo;

    public CustomerController(Repo repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Customer getCustomer(@PathVariable String id) {
        return repo.findCustomer(id);
    }
}
package com.acme;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * #2214 - owner bound through a local variable.
 * Cases marked NOT REPORTED must stay silent; cases marked REPORTED must fire
 * java.user_controlled_primary_key.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    // CTRL-1 REPORTED: unbounded lookup, baseline positive.
    @GetMapping("/ctrl1/{id}")
    public Order ctrl1(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // CTRL-2 NOT REPORTED: caller written inline in the bound.
    @GetMapping("/ctrl2/{id}")
    public Order ctrl2(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        return repo.findByIdAndOwner(id, user.getUsername()).orElse(null);
    }

    // A1 NOT REPORTED: bound in the lookup, caller held in a local.
    @GetMapping("/a1/{id}")
    public Order a1(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        String owner = user.getUsername();
        return repo.findByIdAndOwner(id, owner).orElse(null);
    }

    // A2 REPORTED: twin of A1, the local holds a request value.
    @GetMapping("/a2/{id}")
    public Order a2(@PathVariable Long id, HttpServletRequest request) {
        String owner = request.getParameter("owner");
        return repo.findByIdAndOwner(id, owner).orElse(null);
    }

    // B1 NOT REPORTED: comparison after the lookup, caller held in a local.
    @GetMapping("/b1/{id}")
    public Order b1(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        String owner = user.getUsername();
        Order order = repo.findById(id).orElse(null);
        if (order == null || !order.getOwner().equals(owner)) {
            throw new IllegalStateException("forbidden");
        }
        return order;
    }

    // B2 REPORTED: twin of B1, the local holds a request value.
    @GetMapping("/b2/{id}")
    public Order b2(@PathVariable Long id, HttpServletRequest request) {
        String owner = request.getParameter("owner");
        Order order = repo.findById(id).orElse(null);
        if (order == null || !order.getOwner().equals(owner)) {
            throw new IllegalStateException("forbidden");
        }
        return order;
    }

    // C1 REPORTED: local first takes the caller, then is rebound to a request value.
    @GetMapping("/c1/{id}")
    public Order c1(@PathVariable Long id, @AuthenticationPrincipal AppUser user, HttpServletRequest request) {
        String owner = user.getUsername();
        owner = request.getParameter("owner");
        return repo.findByIdAndOwner(id, owner).orElse(null);
    }

    // C2 REPORTED: rebound local, comparison after the lookup.
    @GetMapping("/c2/{id}")
    public Order c2(@PathVariable Long id, @AuthenticationPrincipal AppUser user, HttpServletRequest request) {
        String owner = user.getUsername();
        owner = request.getParameter("owner");
        Order order = repo.findById(id).orElse(null);
        if (order == null || !order.getOwner().equals(owner)) {
            throw new IllegalStateException("forbidden");
        }
        return order;
    }

    // D1 REPORTED: loop variable, not assigned once from the caller.
    @GetMapping("/d1/{id}")
    public Order d1(@PathVariable Long id, HttpServletRequest request) {
        Order found = null;
        for (String owner : request.getParameterValues("owners")) {
            found = repo.findByIdAndOwner(id, owner).orElse(null);
        }
        return found;
    }
}

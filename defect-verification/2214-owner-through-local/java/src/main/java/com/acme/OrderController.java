package com.acme;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * #2214 - owner checked through a local variable.
 * Cases marked NOT REPORTED must stay silent; cases marked REPORTED must fire
 * java.user_controlled_primary_key.
 * The Java lookup takes only the key (findById): a derived query such as
 * findByIdAndOwner(id, x) is not a sink at all, so the owner check is the
 * comparison after the fetch (place B of the ticket).
 * Scanner 6.21.0: CTRL-2, B1 and the other NOT REPORTED cases are still
 * REPORTED, with the caller inline or in a local.
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

    // CTRL-2 NOT REPORTED: comparison after the lookup, caller written inline.
    @GetMapping("/ctrl2/{id}")
    public Order ctrl2(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        Order order = repo.findById(id).orElse(null);
        if (order == null || !order.getOwner().equals(user.getUsername())) {
            throw new IllegalStateException("forbidden");
        }
        return order;
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

    // C2 REPORTED: local first takes the caller, then is rebound to a request value.
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
            Order order = repo.findById(id).orElse(null);
            if (order != null && order.getOwner().equals(owner)) {
                found = order;
            }
        }
        return found;
    }
}

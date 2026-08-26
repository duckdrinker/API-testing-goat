package com.example.apibasic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping
    public List<Order> listOrders() {
        return List.of(new Order(1L, "Widget", 3));
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return new Order(id, "Widget", 3);
    }

    public record Order(Long id, String item, int quantity) {}
}

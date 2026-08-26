package com.example.apibasicv2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @GetMapping
    public List<Invoice> listInvoices() {
        return List.of(new Invoice(1L, 199.99));
    }

    @GetMapping("/{id}")
    public Invoice getInvoice(@PathVariable Long id) {
        return new Invoice(id, 199.99);
    }

    @PostMapping
    public Invoice createInvoice(@RequestBody Invoice invoice) {
        return invoice;
    }

    public record Invoice(Long id, double amount) {}
}

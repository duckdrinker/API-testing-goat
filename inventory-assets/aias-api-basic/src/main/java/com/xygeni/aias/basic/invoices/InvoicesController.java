package com.xygeni.aias.basic.invoices;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Second module, no PII, no findings expected here — keeps the one
// deliberate finding in the "orders" module unambiguous for AIAS-010/011/012.
// Full paths are given directly on each mapping (no class-level
// @RequestMapping) so that GET /health below collides exactly with
// OrdersController's /health — see there for why.
@RestController
public class InvoicesController {

    @GetMapping("/api/invoices")
    public List<String> listInvoices() {
        return List.of("INV-2001", "INV-2002");
    }

    @GetMapping("/health")
    public String health() {
        return "invoices-ok";
    }
}

package com.xygeni.apisec.api2auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ASEC-045 known-limitation fixture.
 *
 * No per-method or per-class auth annotation anywhere on this controller -
 * it is protected ONLY by SecurityConfig's centralized
 * anyRequest().authenticated() rule. Xygeni's unauthenticated_endpoint
 * detector does not parse that centralized declaration, so this endpoint
 * may still surface as a false-positive candidate today. That is the
 * documented, pre-existing limitation this fixture exists to demonstrate -
 * not a new defect.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @GetMapping
    public String listTransactions() {
        return "[...transactions...]";
    }
}

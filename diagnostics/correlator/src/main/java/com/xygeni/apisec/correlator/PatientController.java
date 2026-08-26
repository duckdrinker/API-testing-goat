package com.xygeni.apisec.correlator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FlawCorrelator rule 1: pii_leak_in_unauthenticated_endpoint.
 * No auth annotation anywhere -> unauthenticated_endpoint. Response leaks
 * a PHI-tagged, unreferenced field ("diagnosis") -> pii_leak_in_response.
 * Both on the same endpoint -> the composite rule fires (severity critical).
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @GetMapping("/{id}")
    public PatientResponse getPatient(@PathVariable String id) {
        return new PatientResponse(id, "Jane Doe", "Type 2 Diabetes");
    }
}

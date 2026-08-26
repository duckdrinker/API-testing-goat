package com.xygeni.apisec.correlator;

/**
 * "diagnosis" is never referenced by the request and is a sensitivity-tagged
 * (PHI) field -> pii_leak_in_response, combined with no auth requirement on
 * the endpoint that returns it -> the FlawCorrelator's
 * "pii_leak_in_unauthenticated_endpoint" composite rule (critical).
 */
public class PatientResponse {
    private String id;
    private String name;
    private String diagnosis;

    public PatientResponse() {}

    public PatientResponse(String id, String name, String diagnosis) {
        this.id = id;
        this.name = name;
        this.diagnosis = diagnosis;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
}

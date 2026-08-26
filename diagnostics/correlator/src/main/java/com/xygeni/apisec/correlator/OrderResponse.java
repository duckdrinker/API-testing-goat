package com.xygeni.apisec.correlator;

/**
 * "creditCardNumber" is never referenced by the request and is a
 * sensitivity-tagged (PCI) field -> excessive_data_exposure (high
 * confidence), combined with no auth requirement on the endpoint that
 * returns it -> the FlawCorrelator's "excessive_exposure_on_unauthenticated_endpoint"
 * rule bumps the existing excessive_data_exposure finding to severity high
 * (no new finding row is added for this rule).
 */
public class OrderResponse {
    private String id;
    private String status;
    private String creditCardNumber;

    public OrderResponse() {}

    public OrderResponse(String id, String status, String creditCardNumber) {
        this.id = id;
        this.status = status;
        this.creditCardNumber = creditCardNumber;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentCardNumber() { return creditCardNumber; }
    public void setPaymentCardNumber(String creditCardNumber) { this.creditCardNumber = creditCardNumber; }
}

package com.xygeni.apisec.authconfidence;

/** Response DTO with a PII field — the "response sensitive" signal is ON. */
public class WidgetSecret {
    private String id;
    private String ssn;

    public WidgetSecret() {}

    public WidgetSecret(String id, String ssn) {
        this.id = id;
        this.ssn = ssn;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
}

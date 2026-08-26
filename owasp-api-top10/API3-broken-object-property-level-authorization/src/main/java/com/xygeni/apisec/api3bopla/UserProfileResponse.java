package com.xygeni.apisec.api3bopla;

/**
 * Response DTO returned by GET /api/users/profile.
 *
 * "ssn" is never part of the request (see UserProfileUpdateRequest) and is a
 * sensitivity-tagged (PII) field, so it is:
 *  - unreferenced + sensitivity-tagged  -> excessive_data_exposure (high confidence)
 *  - a PII field reaching the client    -> pii_leak_in_response
 */
public class UserProfileResponse {
    private String id;
    private String name;
    private String email;
    private String ssn;

    public UserProfileResponse() {}

    public UserProfileResponse(String id, String name, String email, String ssn) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.ssn = ssn;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
}

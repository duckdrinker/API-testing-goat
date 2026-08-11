package com.xygeni.apisec.api3bopla;

/**
 * excessive_data_exposure / pii_leak_in_response negative fixture.
 *
 * Only exposes fields relevant to the operation - no unreferenced,
 * sensitivity-tagged fields like UserProfileResponse.ssn.
 */
public class UserSummaryResponse {
    private String id;
    private String name;

    public UserSummaryResponse() {}

    public UserSummaryResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

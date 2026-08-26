package com.xygeni.apisec.api3bopla;

/**
 * mass_assignment negative fixture.
 *
 * Deliberately has no "role" (or other protected) field to bind -
 * only the fields a client should legitimately be able to set.
 */
public class UserProfileSafeUpdateRequest {
    private String name;
    private String email;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

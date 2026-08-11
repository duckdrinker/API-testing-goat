package com.xygeni.apisec.api3bopla;

/**
 * Request DTO for PUT /api/users/profile.
 *
 * "role" is not meant to be client-settable, but it is accepted here and
 * binds straight through to the persistent User entity -> mass_assignment.
 */
public class UserProfileUpdateRequest {
    private String name;
    private String email;
    private String role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

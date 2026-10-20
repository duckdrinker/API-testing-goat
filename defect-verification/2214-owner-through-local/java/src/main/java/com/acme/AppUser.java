package com.acme;

/** The application's own type for the authenticated caller. */
public class AppUser {
    private final String username;

    public AppUser(String username) { this.username = username; }

    public String getUsername() { return username; }
}

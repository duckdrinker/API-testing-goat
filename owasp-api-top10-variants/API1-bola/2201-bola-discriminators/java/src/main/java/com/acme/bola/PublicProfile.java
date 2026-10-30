package com.acme.bola;

/** Response type WITHOUT sensitive fields: response_sensitive_fields must be absent or empty. */
public class PublicProfile {
    private String id;
    private String displayName;
    private String title;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
package com.xygeni.apisec.authconfidence;

/** Boring response DTO — no sensitive fields (the "response sensitive" signal is OFF). */
public class WidgetSummary {
    private String id;
    private String name;

    public WidgetSummary() {}

    public WidgetSummary(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

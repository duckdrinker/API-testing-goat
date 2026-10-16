package com.acme;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Order {
    @Id
    private Long id;
    private String owner;
    private String status;

    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public String getStatus() { return status; }
}

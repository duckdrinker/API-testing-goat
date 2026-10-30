package com.acme.bola;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Stored entity used as the request type of POST /api/orders (case G). The mass_assignment
 * detector reports on bind evidence, and an @Entity request type is that evidence: the client
 * can set userId/ownerId (owner references) and status/role (protected fields).
 */
@Entity
public class Order {
    @Id
    private String id;
    private String userId;
    private String ownerId;
    private String productName;
    private int quantity;
    private String status;
    private String role;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
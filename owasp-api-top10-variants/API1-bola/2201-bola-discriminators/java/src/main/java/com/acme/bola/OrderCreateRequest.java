package com.acme.bola;

/**
 * Case I: a plain DTO (not an entity) that the service maps field by field. The client can still
 * set userId/ownerId, but there is no bind evidence for mass_assignment: the type is not stored
 * and the whole body does not reach a persistence call. Covering it needs a data-flow check
 * (agreed with the dev as a future ticket), so no finding is the accurate result today.
 */
public class OrderCreateRequest {
    private String productName;
    private int quantity;
    private String userId;
    private String ownerId;
    private String status;
    private String role;

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
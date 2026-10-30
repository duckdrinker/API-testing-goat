package com.acme.bola;

/** Case J request type: not an entity, but the controller hands the whole body to repository.save(...). */
public class ShipmentRequest {
    private String carrier;
    private String userId;
    private String ownerId;
    private String status;

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
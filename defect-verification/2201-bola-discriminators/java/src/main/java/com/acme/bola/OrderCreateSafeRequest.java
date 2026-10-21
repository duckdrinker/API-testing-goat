package com.acme.bola;

/** Case H (negative): no owner-reference field and no protected field can be bound. */
public class OrderCreateSafeRequest {
    private String productName;
    private int quantity;

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
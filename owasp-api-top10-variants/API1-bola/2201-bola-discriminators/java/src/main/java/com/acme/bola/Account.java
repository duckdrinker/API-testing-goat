package com.acme.bola;

public class Account {
    private String id;
    private String ownerId;
    private double balance;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
package com.xygeni.apisec.api1bola;

public class Product {
    private String id;
    private String ownerId;
    private String name;
    private double price;

    public Product() {}

    public Product(String id, String ownerId, String name, double price) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

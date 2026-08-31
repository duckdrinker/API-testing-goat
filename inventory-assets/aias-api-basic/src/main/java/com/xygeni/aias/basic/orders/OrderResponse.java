package com.xygeni.aias.basic.orders;

public class OrderResponse {

    private String orderId;
    private String customerName;
    private String creditCardNumber;

    public OrderResponse() {
    }

    public OrderResponse(String orderId, String customerName, String creditCardNumber) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.creditCardNumber = creditCardNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}

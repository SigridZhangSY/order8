package com.thoughtworks.ketsu.domain.order;

public class OrderItem {
    private long productId;
    private int quantity;
    private float amount;


    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getAmount() {
        return amount;
    }
}

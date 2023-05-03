package com.example.dynodash.ui.customer.list;

public class OrderItem {
    private String itemID;
    private Integer quantity;
    private Double singlePrice;
    private String itemName;

    public OrderItem(String id, Integer quantity, Double singlePrice, String itemName) {
        this.itemID = id;
        this.quantity = quantity;
        this.singlePrice = singlePrice;
        this.itemName = itemName;
    }

    public Double getSinglePrice() {
        return singlePrice;
    }

    public String getItemID() {
        return itemID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setSinglePrice(Double singlePrice) {
        this.singlePrice = singlePrice;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

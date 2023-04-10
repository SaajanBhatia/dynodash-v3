package com.example.dynodash.ui.restaurant.orders;

import java.util.ArrayList;

public class OrdersListItem {

    private String orderID;
    private String customerID;
    private String customerName;
    private Boolean completed;
    private ArrayList<OrdersSingleFoodItem> orderedFood;
    private Integer tableNumber;
    private Double orderTotal;

    public OrdersListItem(
            String orderID,
            String customerID,
            String customerName,
            Boolean completed,
            Integer tableNumber,
            Double orderTotal,
            ArrayList<OrdersSingleFoodItem> itemList
    ) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.completed = completed;
        this.tableNumber = tableNumber;
        this.orderTotal = orderTotal;
        this.orderedFood = itemList;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getOrderID() {
        return orderID;
    }

    public ArrayList<OrdersSingleFoodItem> getOrderedFood() {
        return orderedFood;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOrderedFood(ArrayList<OrdersSingleFoodItem> orderedFood) {
        this.orderedFood = orderedFood;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }


}

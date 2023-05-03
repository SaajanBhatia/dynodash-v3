package com.example.dynodash.ui.customer.list;

public class RestaurantListItem {

    public RestaurantListItem(
            String name,
            String desc,
            String addr,
            String restaurantID
    ) {
        this.restaurantName = name;
        this.restaurantDesc = desc;
        this.restaurantAddr = addr;
        this.restaurantID = restaurantID;
    }

    private String restaurantID;
    private String restaurantName;
    private String restaurantDesc;
    private String restaurantAddr;

    public String getRestaurantAddr() {
        return restaurantAddr;
    }

    public String getRestaurantDesc() {
        return restaurantDesc;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantAddr(String restaurantAddr) {
        this.restaurantAddr = restaurantAddr;
    }

    public void setRestaurantDesc(String restaurantDesc) {
        this.restaurantDesc = restaurantDesc;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}

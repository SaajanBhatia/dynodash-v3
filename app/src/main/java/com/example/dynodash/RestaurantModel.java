package com.example.dynodash;

public class RestaurantModel {
    private String ID;
    private String name;
    private String description;
    private String address;

    public RestaurantModel(
            String ID,
            String name,
            String description,
            String address
    ) {
        this.ID = ID;
        this.description = description;
        this.address = address;
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

package com.example.dynodash.ui.restaurant.orders;

public class OrdersSingleFoodItem {
    public String itemName;
    public Integer itemQuantity;
    public Double itemPrice;

    public OrdersSingleFoodItem(
            String itemName,
            Integer itemQuantity,
            Double itemPrice
    ) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
    }
}

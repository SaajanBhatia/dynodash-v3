package com.example.dynodash.ui.customer.list;

public class MenuListItem {
    private String itemID;
    private String itemName;
    private String itemDesc;
    private Double itemPrice;
    private Integer quantity;

    public MenuListItem(
            String id,
            String name,
            String desc,
            Double price
    ) {
        this.itemName = name;
        this.itemID = id;
        this.itemDesc = desc;
        this.itemPrice = price;

        // Initial Quantity -> 0
        this.quantity = 0;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }
}

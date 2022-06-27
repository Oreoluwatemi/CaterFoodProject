package com.example.caterfoodproject.model;

public class Cart {
    public String name;
    public String price;
    public String quantity;
    public String restaurantName;

    public Cart(String name, String price, String quantity, String restaurantName){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.restaurantName = restaurantName;
    }
}

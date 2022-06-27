package com.example.caterfoodproject.model;

public class Order {
    public String name;
    public String price;
    public String date;
    public String email;


    public Order(String name, String price, String date, String email){
        this.name = name;
        this.price = price;
        this.date = date;
        this.email = email;
    }
}

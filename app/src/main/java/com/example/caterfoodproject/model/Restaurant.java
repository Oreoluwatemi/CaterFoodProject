package com.example.caterfoodproject.model;

public class Restaurant {
    public String name;
    public String style;
    public String image;
    public String description;
    public String location;
    public String tags;

    public Restaurant(String name, String style, String image, String desc, String location, String tags){
        this.name = name;
        this.style = style;
        this.image = image;
        this.description = desc;
        this.location = location;
        this.tags = tags;
    }
}

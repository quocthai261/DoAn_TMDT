package com.example.tryagain;

import java.io.Serializable;

public class Product implements Serializable {
    private String Id, Name, Description, Image, Location;
    private int Price;
    private int typeDisplay;
    public static final int TYPE_GRID = 1, TYPE_LIST = 2;

    public Product() {
    }

    public Product(String id, String name, String description, String image, String location, int price) {
        Id = id;
        Name = name;
        Description = description;
        Image = image;
        Location = location;
        Price = price;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }
}

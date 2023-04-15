package com.example.tryagain;

import java.io.Serializable;

public class Best_Seller_Product implements Serializable {
    private String Id, Image, Name;
    private int Price;

    public Best_Seller_Product() {

    }

    public Best_Seller_Product(String id, String image, String name, int price) {
        Id = id;
        Image = image;
        Name = name;
        Price = price;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}

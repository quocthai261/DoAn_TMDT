package com.example.tryagain;

import java.io.Serializable;

public class Category implements Serializable {
    private String Name, Image, Category;

    public Category() {
    }

    public Category(String name, String image, String category) {
        Name = name;
        Image = image;
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}

package com.example.tryagain;

public class Cart {
    private String Name, Size, Image, Price, Id, Quantity, Total_Price;

    public Cart() {
    }

    public Cart(String name, String size, String image, String price, String id, String quantity, String total_Price) {
        Name = name;
        Size = size;
        Image = image;
        Price = price;
        Id = id;
        Quantity = quantity;
        Total_Price = total_Price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(String total_Price) {
        Total_Price = total_Price;
    }
}

package com.example.tryagain;

public class User {
    private String Name, Phone, Sex, Address, Email, Profile_image;

    public User() {
    }

    public User(String name, String phone, String sex, String address, String email, String profile_image) {
        Name = name;
        Phone = phone;
        Sex = sex;
        Address = address;
        Email = email;
        Profile_image = profile_image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfile_image() {
        return Profile_image;
    }

    public void setProfile_image(String profile_image) {
        Profile_image = profile_image;
    }
}

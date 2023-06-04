package com.example.tryagain;

import java.io.Serializable;

public class Bank_Payment extends Bank  implements Serializable {
    private String Number, User_Name, Date;

    public Bank_Payment() {
    }

    public Bank_Payment(String number, String user_Name, String date) {
        Number = number;
        User_Name = user_Name;
        Date = date;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

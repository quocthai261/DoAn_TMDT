package com.example.tryagain;

import java.io.Serializable;

public class Bank implements Serializable {
    private String Full_Name, Short_Name, Logo;

    public Bank() {
    }

    public Bank(String full_Name, String short_Name, String logo) {
        Full_Name = full_Name;
        Short_Name = short_Name;
        Logo = logo;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getShort_Name() {
        return Short_Name;
    }

    public void setShort_Name(String short_Name) {
        Short_Name = short_Name;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }
}

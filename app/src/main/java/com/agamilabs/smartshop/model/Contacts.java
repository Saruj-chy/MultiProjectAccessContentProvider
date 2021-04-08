package com.agamilabs.smartshop.model;

public class Contacts {
    public String name;
    public String phoneNumber;
    public String image;
    public boolean isSelected;

    public Contacts(String name, String phoneNumber,String image,boolean isSelected) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

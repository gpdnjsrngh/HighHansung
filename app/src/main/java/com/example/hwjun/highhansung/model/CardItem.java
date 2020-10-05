package com.example.hwjun.highhansung.model;

public class CardItem {
    private String num;
    private String name;
    private String major;
    private String gender;

    public CardItem(String num, String name, String major, String gender) {
        this.num=num;
        this.name=name;
        this.major=major;
        this.gender=gender;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

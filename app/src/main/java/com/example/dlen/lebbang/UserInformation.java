package com.example.dlen.lebbang;

/**
 * Created by ASUS on 2018/1/1.
 */

public class UserInformation {

    private int userId;
    private String username;
    private String phoneNum;
    private int credit;
    private String headImg;

    public UserInformation(int userId, String username, String phoneNum, int credit, String headImg) {
        this.userId = userId;
        this.username = username;
        this.phoneNum = phoneNum;
        this.credit = credit;
        this.headImg = headImg;
    }

    public int getUserId() {
        return userId;
    }

    public int getCredit() {
        return credit;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getHeadImg() {
        return headImg;
    }

}

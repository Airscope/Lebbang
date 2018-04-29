package com.example.dlen.lebbang;

import java.io.Serializable;

/**
 * Created by Dlen on 2017/12/27.
 */

public class RenWu  implements Serializable{
    private int taskId;
    private String name;
    private String details;
    private String imageId;
    private String phoneNum;
    private String price;
    private String tag;
    private int userId;
    public RenWu(int taskId, String name, String details, String imageSrc, String price, String tag, int userId) {
        this.details = details;
        this.name = name;
        //this.imageId = imageSrc;
        this.imageId = imageSrc;
        this.phoneNum = "12312345678";
        this.taskId = taskId;
        this.price = price;
        this.tag = tag;
        this.userId = userId;
    }

    public RenWu(String details) {
        this.details = details;
        this.name = "no";
        this.imageId = null;
        this.phoneNum = "12312345678";
        this.taskId = 3;
        this.price = "no";
        this.tag = "no";
    }

    public String getName() {
        return name;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public String getDetails() {
        return details;
    }
    public String getImageId() {
        return imageId;
    }
    public int getTaskId() {
        return taskId;
    }

    public String getPrice() {
        return price;
    }

    public String getTag() {
        return tag;
    }

    public void setTaskId(int id) {
        taskId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setImageId(String url) {
        this.imageId = url;
    }

    public void setPhoneNum(String num) {
        this.phoneNum = num;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        userId = id;
    }
}

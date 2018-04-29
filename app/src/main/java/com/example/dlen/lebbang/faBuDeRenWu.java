package com.example.dlen.lebbang;

/**
 * Created by Dlen on 2017/12/30.
 */

public class faBuDeRenWu {
    private int people;
    private String details;
    private int state;
    private int taskId;
    private int userId;
    private String reward;

    public faBuDeRenWu(int a, String b, int c, int taskId, int userId, String price) {
        this.people = a;
        this.details = b;
        this.state = c;
        this.taskId =taskId;
        this.userId = userId;
        this.reward = price;
    }

    public faBuDeRenWu(int a, String b, int c) {
        this.people = a;
        this.details = b;
        this.state = c;
    }
    public String getDetails() {
        return details;
    }
    public int getPeople() {
        return people;
    }
    public int getState() {
        return state;
    }
    public int getTaskId() {return taskId;}
    public int getUserId() {return userId;}
    public String getReward() {return reward;}
}

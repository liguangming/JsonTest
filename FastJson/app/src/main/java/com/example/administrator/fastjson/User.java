package com.example.administrator.fastjson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/8.
 */

class User {
    private String name;
    private  int age;
    private ArrayList<Friend> friends;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

}

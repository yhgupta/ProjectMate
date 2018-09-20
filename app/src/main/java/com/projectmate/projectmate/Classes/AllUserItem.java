package com.projectmate.projectmate.Classes;

import java.util.ArrayList;

public class AllUserItem {
    private int userID;
    private String userName;
    private ArrayList<Integer> listx ;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Integer> getListx() {
        return listx;
    }

    public void setListx(ArrayList<Integer> listx) {
        this.listx = listx;
    }
}

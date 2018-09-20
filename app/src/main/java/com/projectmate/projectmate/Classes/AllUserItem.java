package com.projectmate.projectmate.Classes;

import java.util.ArrayList;

public class AllUserItem {
    private int userID;
    private String userName;
    private ArrayList<Integer> listx ;

    public AllUserItem( int userID, String userName , ArrayList<Integer> listx ){
        this.userID = userID;
        this.userName = userName;
        this.listx = listx;
    }

    public int getUserID() { return userID; }

    public String getUserName() {
        return userName;
    }


    public ArrayList<Integer> getListx() {
        return listx;
    }


}



//variables initialization and its getters and setters with constructors


package com.projectmate.projectmate.Classes;

import java.util.ArrayList;

public class AllUserItem {
    private int id;
    private String username;
    private ArrayList<Integer> skills;

    public AllUserItem(int id, String username, ArrayList<Integer> skills){
        this.id = id;
        this.username = username;
        this.skills = skills;
    }

    public int getId() { return id; }

    public String getUsername() {
        return username;
    }


    public ArrayList<Integer> getSkills() {
        return skills;
    }


}

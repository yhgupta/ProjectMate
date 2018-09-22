
//variables initialization and its getters and setters with constructors

package com.projectmate.projectmate.Classes;

public class BasicUser {
    private int id;
    private String username;

    public BasicUser(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

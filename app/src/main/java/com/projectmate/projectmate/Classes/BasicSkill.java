//variables initialization and its getters and setters with constructors

package com.projectmate.projectmate.Classes;

public class BasicSkill {
    private int id;
    private String name;

    public BasicSkill(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

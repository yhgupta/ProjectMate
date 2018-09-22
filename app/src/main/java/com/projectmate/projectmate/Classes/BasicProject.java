//variables initialization and its getters and setters with constructors

package com.projectmate.projectmate.Classes;

public class BasicProject {
    private int id;
    private String project_name;

    public BasicProject(int id, String project_name) {
        this.id = id;
        this.project_name = project_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}

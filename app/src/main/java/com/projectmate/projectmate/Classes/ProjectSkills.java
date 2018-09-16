package com.projectmate.projectmate.Classes;

import java.util.ArrayList;

public class ProjectSkills {
    private Project project;
    private ArrayList<Skill> skills;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public ProjectSkills(Project project, ArrayList<Skill> skills) {

        this.project = project;
        this.skills = skills;
    }
}

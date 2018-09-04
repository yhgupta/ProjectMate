package com.projectmate.projectmate.Classes;

import java.util.ArrayList;

public class Project {
    private String projectName;
    private String projectShortDesc;
    private String projectCompleteDesc;

    private ArrayList<Skill> skills;

    public Project(String projectName, String projectShortDesc, String projectCompleteDesc, ArrayList<Skill> skills) {
        this.projectName = projectName;
        this.projectShortDesc = projectShortDesc;
        this.projectCompleteDesc = projectCompleteDesc;
        this.skills = skills;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectShortDesc() {
        return projectShortDesc;
    }

    public void setProjectShortDesc(String projectShortDesc) {
        this.projectShortDesc = projectShortDesc;
    }

    public String getProjectCompleteDesc() {
        return projectCompleteDesc;
    }

    public void setProjectCompleteDesc(String projectCompleteDesc) {
        this.projectCompleteDesc = projectCompleteDesc;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }
}

package com.projectmate.projectmate.Classes;

import java.util.ArrayList;

public class Project {
    private String projectName;
    private String projectShortDesc;
    private String projectCompleteDesc;

    private ArrayList<Integer> skillIDs;

    public Project(String projectName, String projectShortDesc, String projectCompleteDesc, ArrayList<Integer> skillIDs) {
        this.projectName = projectName;
        this.projectShortDesc = projectShortDesc;
        this.projectCompleteDesc = projectCompleteDesc;
        this.skillIDs = skillIDs;
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

    public ArrayList<Integer> getSkillIDs() {
        return skillIDs;
    }

    public void setSkillIDs(ArrayList<Integer> skillIDs) {
        this.skillIDs = skillIDs;
    }
}

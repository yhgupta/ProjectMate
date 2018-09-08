package com.projectmate.projectmate.Classes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Project {
    @SerializedName("project_name")
    private String projectName;

    @SerializedName("project_short_desc")
    private String projectShortDesc;

    @SerializedName("project_complete_desc")
    private String projectCompleteDesc;

    private ArrayList<Integer> skills;

    public Project(String projectName, String projectShortDesc, String projectCompleteDesc, ArrayList<Integer> skills) {
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

    public ArrayList<Integer> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Integer> skills) {
        this.skills = skills;
    }
}

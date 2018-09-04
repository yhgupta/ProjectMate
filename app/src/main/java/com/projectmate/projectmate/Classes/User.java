package com.projectmate.projectmate.Classes;

import java.util.ArrayList;

public class User {
    private String name;
    private String organization;
    private String city;
    private String country;

    private String codeChefUsername;
    private int codeChefRanking;

    private ArrayList<Skill> skills;
    private ArrayList<Project> projects;

    public User(String name, String organization, String city, String country, String codeChefUsername, int codeChefRanking, ArrayList<Skill> skills, ArrayList<Project> projects) {
        this.name = name;
        this.organization = organization;
        this.city = city;
        this.country = country;
        this.codeChefUsername = codeChefUsername;
        this.codeChefRanking = codeChefRanking;
        this.skills = skills;
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCodeChefUsername() {
        return codeChefUsername;
    }

    public void setCodeChefUsername(String codeChefUsername) {
        this.codeChefUsername = codeChefUsername;
    }

    public int getCodeChefRanking() {
        return codeChefRanking;
    }

    public void setCodeChefRanking(int codeChefRanking) {
        this.codeChefRanking = codeChefRanking;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}


//variables initialization and its getters and setters with constructors

package com.projectmate.projectmate.Classes;

import java.util.ArrayList;

public class User {
    private int id; // Primary Key

    private String name;
    private String organization;
    private String city;
    private String location;

    private String username;
    private int ranking;

    private ArrayList<Skill> skills;
    private ArrayList<Project> projects;

    public User(int id, String name, String organization, String city, String location, String username, int ranking, ArrayList<Skill> skills, ArrayList<Project> projects) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.city = city;
        this.location = location;
        this.username = username;
        this.ranking = ranking;
        this.skills = new ArrayList<>(skills);
        this.projects = new ArrayList<>(projects);
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
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

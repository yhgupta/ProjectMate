package com.projectmate.projectmate.Classes;

import android.content.Intent;

public class Skill {
    private Integer skillID;
    private String skillName;
    private int skillRating;
    private String shortDescription;
    private String coursesTaken;

    public Skill(Integer skillID, String skillName, int skillRating, String shortDescription, String coursesTaken) {
        this.skillID = skillID;
        this.skillName = skillName;
        this.skillRating = skillRating;
        this.shortDescription = shortDescription;
        this.coursesTaken = coursesTaken;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getSkillRating() {
        return skillRating;
    }

    public void setSkillRating(int skillRating) {
        this.skillRating = skillRating;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getCoursesTaken() {
        return coursesTaken;
    }

    public void setCoursesTaken(String coursesTaken) {
        this.coursesTaken = coursesTaken;
    }
}

package com.projectmate.projectmate.Classes;

public class Skill {
    private String skillName;
    private int skillRating;
    private String shortDescription;
    private String coursesTaken;

    public Skill(String skillName, int skillRating, String shortDescription, String coursesTaken) {
        this.skillName = skillName;
        this.skillRating = skillRating;
        this.shortDescription = shortDescription;
        this.coursesTaken = coursesTaken;
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

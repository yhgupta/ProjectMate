package com.projectmate.projectmate.Classes;

import com.google.gson.annotations.SerializedName;

/**
 * Simple class to hold skill
 */
public class Skill {

    @SerializedName("skill_id")
    private Integer skillID;

    @SerializedName("skill_name")
    private String skillName;

    @SerializedName("skill_rating")
    private float skillRating = 0;

    @SerializedName("skill_short_desc")
    private String shortDescription = "";

    @SerializedName("skill_courses_taken")
    private String coursesTaken = "";

    public Skill(Integer skillID, String skillName, float skillRating, String shortDescription, String coursesTaken) {
        this.skillID = skillID;
        this.skillName = skillName;
        this.skillRating = skillRating;
        this.shortDescription = shortDescription;
        this.coursesTaken = coursesTaken;
    }

    public Skill(Integer skillID, String skillName) {
        this.skillID = skillID;
        this.skillName = skillName;
    }


    public void setSkillID(Integer skillID) {
        this.skillID = skillID;
    }

    public void setSkillRating(float skillRating) {
        this.skillRating = skillRating;
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

    public float getSkillRating() {
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

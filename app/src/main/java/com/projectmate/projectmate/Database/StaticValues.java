package com.projectmate.projectmate.Database;

import com.projectmate.projectmate.Classes.User;

import java.util.ArrayList;


/**
 * Acts like a session class, stores data for other classes to access
 */
public class StaticValues {
    private StaticValues() {
    }

    private static String CODECHEF_AUTH_KEY;

    private static User CURRENT_USER;

    private static ArrayList<String> ALL_SKILLS;

    public static void setCodeChefAuthKey(String authKey) {
        CODECHEF_AUTH_KEY = authKey;
    }

    public static String getCodeChefAuthKey() {
        return CODECHEF_AUTH_KEY;
    }

    public static void setCurrentUser(User user) {
        CURRENT_USER = user;
    }

    public static User getCurrentUser() {
        return CURRENT_USER;
    }

    public static ArrayList<String> getAllSkills() {
        return ALL_SKILLS;
    }

    public static void setAllSkills(ArrayList<String> allSkills) {
        ALL_SKILLS = allSkills;
    }
}

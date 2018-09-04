package com.projectmate.projectmate.Database;

import com.projectmate.projectmate.Classes.User;

public class StaticValues {
    private StaticValues(){}

    private static String CODECHEF_AUTH_KEY;

    private static User CURRENT_USER;

    public static void setCodeChefAuthKey(String authKey){
        CODECHEF_AUTH_KEY = authKey;
    }

    public static String getCodeChefAuthKey(){
        return CODECHEF_AUTH_KEY;
    }

    public static void setCurrentUser(User user){
        CURRENT_USER = user;
    }
    public static User getCurrentUser(){
        return CURRENT_USER;
    }

}

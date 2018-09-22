package com.projectmate.projectmate.AlibabaCloud;

import android.net.Uri;

/*
 * This Class generate all the url's required for ProjectMate Api
 * */

public class ProjectMateUris {

    //The default url for getting current user details users/me/
    public static String getAuthUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.USERS_PATH)
                .appendPath(ProjectMateAPIContract.CURRENT_USER_PATH)
                .appendPath("");

        return builder.build().toString();
    }

    //Projects having skills in me projects/<int:offset>/
    public static String getProjectsForMe(int offset) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.PROJECTS_PATH)
                .appendPath(String.valueOf(offset))
                .appendPath("");

        return builder.build().toString();
    }

    //All projects projects/all/<int:offset>/
    public static String getAllProjects(int offset) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.PROJECTS_PATH)
                .appendPath(ProjectMateAPIContract.All_PROJECTS_PATH)
                .appendPath(String.valueOf(offset))
                .appendPath("");

        return builder.build().toString();
    }

    //Get details of a specific project
    public static String getProject(int project_id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.PROJECTS_PATH)
                .appendPath(ProjectMateAPIContract.GET_PROJECTS_PATH)
                .appendPath(String.valueOf(project_id))
                .appendPath("");

        return builder.build().toString();
    }

    //Get users having the skills in my project users/all/<int:project_id>/<int:offset>/
    public static String getUsersForProject(int project_id, int offset) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.USERS_PATH)
                .appendPath(ProjectMateAPIContract.ALL_USERS_PATH)
                .appendPath(String.valueOf(project_id))
                .appendPath(String.valueOf(offset))
                .appendPath("");

        return builder.build().toString();
    }

    //Get details of a specific user users/get/<int:pk>/
    public static String getUser(int user_id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.USERS_PATH)
                .appendPath(ProjectMateAPIContract.GET_USER_PATH)
                .appendPath(String.valueOf(user_id))
                .appendPath("");

        return builder.build().toString();
    }

    //Invite a user to join a project projects/get/<int:pk>/invite/<int:user_id>/
    public static String InviteUserToProject(int project_id, int user_id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.PROJECTS_PATH)
                .appendPath(ProjectMateAPIContract.GET_PROJECTS_PATH)
                .appendPath(String.valueOf(project_id))
                .appendPath(ProjectMateAPIContract.INVITE_PROJECTS_PATH)
                .appendPath(String.valueOf(user_id))
                .appendPath("");

        return builder.build().toString();
    }

    //Send join request to a project projects/get/<int:pk>/join/
    public static String JoinProject(int project_id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.PROJECTS_PATH)
                .appendPath(ProjectMateAPIContract.GET_PROJECTS_PATH)
                .appendPath(String.valueOf(project_id))
                .appendPath(ProjectMateAPIContract.JOIN_PROJECTS_PATH)
                .appendPath("");

        return builder.build().toString();
    }

    //Get Activities current user has done activities/get/<int:offset>/
    public static String GetActivities(int offset) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.ACTIVITIES_PATH)
                .appendPath(ProjectMateAPIContract.GET_ACTIVITIES_PATH)
                .appendPath(String.valueOf(offset))
                .appendPath("");

        return builder.build().toString();
    }

    //Reply to a received activity/notification activities/reply/<int:pk>/<int:reply>
    public static String ReplyToActivity(int activity_id, int reply) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.ACTIVITIES_PATH)
                .appendPath(ProjectMateAPIContract.REPLY_ACTIVITIES_PATH)
                .appendPath(String.valueOf(activity_id))
                .appendPath(String.valueOf(reply))
                .appendPath("");

        return builder.build().toString();
    }

    //Get all the chats current user have done with different users (Only last message)
    //chat/get/all/
    public static String GetUserChats() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.CHATS_PATH)
                .appendPath(ProjectMateAPIContract.GET_CHATS_PATH)
                .appendPath(ProjectMateAPIContract.All_CHATS_PATH)
                .appendPath("");

        return builder.build().toString();
    }

    //Get all the messages for a specific user chat/get/<int:user_id>/<int:offset>
    public static String GetChat(int user_id, int offset) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ProjectMateAPIContract.SERVER_SCHEME)
                .encodedAuthority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
                .appendPath(ProjectMateAPIContract.CHATS_PATH)
                .appendPath(ProjectMateAPIContract.GET_CHATS_PATH)
                .appendPath(String.valueOf(user_id))
                .appendPath(String.valueOf(offset))
                .appendPath("");

        return builder.build().toString();
    }


}

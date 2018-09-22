package com.projectmate.projectmate.AlibabaCloud;

public class ProjectMateAPIContract {

    //All the paths for interacting with the ProjectMate API on Alibaba Cloud
    public static final String SERVER_SCHEME = "http";

    public static final String SERVER_URL = "149.129.133.253";
    public static final String API_PATH = "api";

    public static final String USERS_PATH = "users";
    public static final String CURRENT_USER_PATH = "me";
    public static final String ALL_USERS_PATH = "all";
    public static final String GET_USER_PATH = "get";

    public static final String PROJECTS_PATH = "projects";
    public static final String GET_PROJECTS_PATH = "get";
    public static final String All_PROJECTS_PATH = "all";

    public static final String INVITE_PROJECTS_PATH = "invite";
    public static final String JOIN_PROJECTS_PATH = "join";

    public static final String ACTIVITIES_PATH = "activities";
    public static final String REPLY_ACTIVITIES_PATH = "reply";
    public static final String GET_ACTIVITIES_PATH = "get";

    public static final String CHATS_PATH = "chat";
    public static final String All_CHATS_PATH = "all";
    public static final String GET_CHATS_PATH = "get";


    //Codes for accepting or rejecting a join/invite request
    public static final int ACCEPT_CODE = 100;
    public static final int REJECT_CODE = 101;


    //Default Authentication Failed Message
    public static final String AUTHENTICATION_FAILED = "{\"Error\":\"Authentication Failed\"}";


    public static final int ACTIVITY_TYPE_REQUEST_INVITE = 500;
    public static final int ACTIVITY_TYPE_REQUEST_JOIN = 501;

    public static final int ACTIVITY_TYPE_REQUEST_INVITE_ACCEPTED = 503;
    public static final int ACTIVITY_TYPE_REQUEST_JOIN_ACCEPTED = 504;

    public static final int ACTIVITY_TYPE_REQUEST_INVITE_REJECTED = 505;
    public static final int ACTIVITY_TYPE_REQUEST_JOIN_REJECTED = 506;

}

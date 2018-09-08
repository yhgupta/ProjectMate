package com.projectmate.projectmate.CodeChefAPI;

public class AuthPostDataClass {
    private String grant_type = "authorization_code";
    private String code;
    private String client_id = APIContract.CLIENT_ID;
    private String client_secret = APIContract.CLIENT_SECRET;
    private String redirect_uri = "r://auth";

    public AuthPostDataClass(String code) {
        this.code = code;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}

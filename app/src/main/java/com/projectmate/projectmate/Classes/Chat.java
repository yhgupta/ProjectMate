
//variables initialization and its getters and setters with constructors and constructor overloading
package com.projectmate.projectmate.Classes;

public class Chat {

    private  String name;
    private  String lastMessage;
    private Boolean WhoseMessage;

    public Chat(String name, String lastMessage){
        this.name = name;
        this.lastMessage = lastMessage;
    }

    public Chat(String name, String lastMessage, Boolean whoseMessage){
        this.name = name;
        this.lastMessage = lastMessage;
        this.WhoseMessage = whoseMessage;
    }

    public  String getName() { return name; }
    public  String getLastMessage() { return lastMessage; }
    public Boolean getWhoseMessage(){ return WhoseMessage; }


}

package com.projectmate.projectmate.Classes;

public class Message {
    private BasicUser sender;
    private BasicUser receiver;

    private String message;

    public Message(BasicUser sender, BasicUser receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public BasicUser getSender() {
        return sender;
    }

    public void setSender(BasicUser sender) {
        this.sender = sender;
    }

    public BasicUser getReceiver() {
        return receiver;
    }

    public void setReceiver(BasicUser receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

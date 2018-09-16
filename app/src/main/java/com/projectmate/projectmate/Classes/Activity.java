package com.projectmate.projectmate.Classes;

public class Activity {
    private int id;

    private BasicUser sender;
    private BasicUser receiver;

    private BasicProject project;

    private int activity_type;

    public Activity(int id, BasicUser sender, BasicUser receiver, BasicProject project, int activity_type) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.project = project;
        this.activity_type = activity_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public BasicProject getProject() {
        return project;
    }

    public void setProject(BasicProject project) {
        this.project = project;
    }

    public int getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(int activity_type) {
        this.activity_type = activity_type;
    }
}

package com.sreekanth.dev.ilahianz.model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String time;
    private boolean isseen;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public Chat(String sender, String receiver, String message, String Time, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = Time;
        this.isseen = isseen;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

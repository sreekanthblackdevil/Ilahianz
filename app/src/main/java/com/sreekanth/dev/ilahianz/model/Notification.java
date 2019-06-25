package com.sreekanth.dev.ilahianz.model;

public class Notification {

    private String Heading;
    private String Content;
    private String Target;
    private String Time;
    private String From;
    public Notification() {
    }

    public Notification(String heading, String content, String target, String time, String from) {
        Heading = heading;
        Content = content;
        Target = target;
        Time = time;
        From = from;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }
}
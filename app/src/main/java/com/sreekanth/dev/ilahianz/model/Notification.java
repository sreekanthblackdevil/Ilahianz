package com.sreekanth.dev.ilahianz.model;

public class Notification {

    private String Heading;
    private String Content;
    private String Target;
    private String Type;
    private String Time;
    private String Date;
    private String Category;
    private String From;
    private String UId;
    private String Color;

    public Notification() {
    }

    public Notification(String heading, String content,
                        String target, String type,
                        String time, String date,
                        String category, String from,
                        String UId, String color) {
        Heading = heading;
        Content = content;
        Target = target;
        Type = type;
        Time = time;
        Date = date;
        Category = category;
        Color = color;
        From = from;
        this.UId = UId;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }
}


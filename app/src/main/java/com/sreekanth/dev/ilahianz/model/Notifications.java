package com.sreekanth.dev.ilahianz.model;

public class Notifications {

    private String Content, Title, Date, Time, From, To;

    public Notifications() {
    }

    public Notifications(String content, String title, String date, String time, String from, String to) {
        Content = content;
        Title = title;
        this.Date = date;
        this.Time = time;
        this.From = from;
        this.To = to;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        this.To = to;
    }


}

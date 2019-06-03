package com.sreekanth.dev.ilahianz.model;

public class Story {

    private String Uid;
    private String contents;
    private String heading;
    private String time;
    private String date;
    private boolean Public;

    public Story(String uid, String contents, String heading,
                 String time, String date, boolean aPublic) {
        Uid = uid;
        this.contents = contents;
        this.heading = heading;
        this.time = time;
        this.date = date;
        Public = aPublic;
    }

    public Story() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPublic() {
        return Public;
    }

    public void setPublic(boolean aPublic) {
        Public = aPublic;
    }
}

package com.example.christospaspalieris.educationprogram;

/**
 * Created by peira on 09-Jul-17.
 */

public class Post {

    private String title;
    private String desc;
    private String poster_image;
    private String uid;
    private String pid;
    private String reactions;

    public Post()
    {}

    public Post(String title, String desc, String poster_image, String uid, String pid, String reactions) {
        this.title = title;
        this.desc = desc;
        this.poster_image = poster_image;
        this.uid = uid;
        this.pid = pid;
        this.reactions = reactions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPoster_image() {
        return poster_image;
    }

    public void setPoster_image(String poster_image) {
        this.poster_image = poster_image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getReactions() {
        return reactions;
    }

    public void setReactions(String reactions) {
        this.reactions = reactions;
    }
}

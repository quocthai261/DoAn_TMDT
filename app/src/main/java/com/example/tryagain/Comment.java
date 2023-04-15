package com.example.tryagain;

public class Comment {
    private String name, comment, id, ProfileImage, date;

    public Comment() {
    }

    public Comment(String name, String comment, String id, String profileImage, String date) {
        this.name = name;
        this.comment = comment;
        this.id = id;
        ProfileImage = profileImage;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

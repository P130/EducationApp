package com.example.christospaspalieris.educationprogram;

/**
 * Created by peira on 09-Sep-17.
 */

public class Friends {

    private String image;
    private String username;
    private String role;

    public Friends()
    {

    }

    public Friends(String image, String username, String role) {
        this.image = image;
        this.username = username;
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

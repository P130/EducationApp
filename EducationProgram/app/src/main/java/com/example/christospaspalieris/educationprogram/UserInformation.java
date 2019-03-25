package com.example.christospaspalieris.educationprogram;

/**
 * Created by Christos Paspalieris on 22-Apr-17.
 */

public class UserInformation {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String age;
    private String sex;
    private String image;
    private String role;

    public UserInformation(){

    }

    public UserInformation(String username, String first_name, String last_name, String email, String password,String age,String sex, String image, String role) {
        this.username = username;
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.image = image;
        this.role = role;
    }

    public String getEmail() { return email; }

    public String getUsername() {
        return username;
    }

    public String getAge() { return age; }

    public String getFirstName(){ return firstName; }

    public String getLastName(){ return lastName; }

    public String getPassword(){ return password; }

    public String getImage() { return image; }

    public String getRole() { return role; }
}

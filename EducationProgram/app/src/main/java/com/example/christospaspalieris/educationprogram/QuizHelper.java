package com.example.christospaspalieris.educationprogram;

/**
 * Created by peira on 17-Sep-17.
 */

class QuizHelper {

    String Test_Name;
    public static int Have_Test;

    public QuizHelper(){

    }
    public QuizHelper(String Test_Name){
        this.Test_Name = Test_Name;
    }

    public String getTitle() {
        return Test_Name;
    }

    public void setTitle(String Test_Name) {
        this.Test_Name = Test_Name;
    }

}
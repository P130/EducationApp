package com.example.christospaspalieris.educationprogram;

/**
 * Created by HitCore on 21/09/2017.
 */

public class Students_Score {

    private int Score;
    private int Time;
    private String User_ID;
    private float User_Rate;

    public Students_Score() {
    }

    public Students_Score(int score, int time, String student_uid,float rate) {
        Score = score;
        Time = time;
        User_ID = student_uid;
        User_Rate = rate;
    }

    public void setScore(int score) {
        Score = score;
    }

    public void setTime(int time) {
        Time = time;
    }

    public int getScore() {
        return Score;
    }

    public int getTime() {
        return Time;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public float getUser_Rate() {
        return User_Rate;
    }

    public void setUser_Rate(int user_Rate) {
        User_Rate = user_Rate;
    }
}

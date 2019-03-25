package com.example.christospaspalieris.educationprogram;

import com.google.firebase.storage.StorageReference;

/**
 * Created by Christos Paspalieris on 28-May-17.
 */

public class Questions {

    private String questionText;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private String correctAnswer;


    public Questions()
    {

    }
    public Questions(String questionText, String choiceA, String choiceB, String choiceC, String choiceD, String correctAnswer) {
        this.questionText = questionText;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correctAnswer = correctAnswer;


    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }



}


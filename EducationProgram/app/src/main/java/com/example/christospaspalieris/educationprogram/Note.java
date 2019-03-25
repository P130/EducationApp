package com.example.christospaspalieris.educationprogram;

/**
 * Created by peira on 11-Jul-17.
 */

public class Note {

    private String coordinatesX,coordinatesY,note,author;

    public Note()
    {

    }

    public Note(String coordinatesX, String coordinatesY,String note,String author) {
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
        this.note = note;
        this.author = author;
    }

    public String getCoordinatesX() {
        return coordinatesX;
    }

    public String getCoordinatesY() { return coordinatesY; }

    public String getNote() {
        return note;
    }

    public String getAuthor() { return author; }

}
package com.example.issorrossi.accelerometer;

/**
 * Created by issoRRossi on 4/30/2017.
 */

public class HighScore {

    private int id;
    private String name;
    private int score;

    public HighScore(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){ this.name = name; }

    public int getScore(){
        return score;
    }
    public void setScore(int score){ this.score = score; }

    public int getId(){return id; }
    public void setId(int id) {this.id = id;}
}
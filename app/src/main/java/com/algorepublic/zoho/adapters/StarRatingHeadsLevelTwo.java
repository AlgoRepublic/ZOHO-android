package com.algorepublic.zoho.adapters;

import java.util.ArrayList;

/**
 * Created by android on 2/3/16.
 */
public class StarRatingHeadsLevelTwo {

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public ArrayList<StarRatingHeadsLevelThree> getLevelThrees() {
        return levelThrees;
    }
    public void setLevelThrees(ArrayList<StarRatingHeadsLevelThree> levelThrees) {
        this.levelThrees = levelThrees;
    }


    ArrayList<StarRatingHeadsLevelThree> levelThrees = new ArrayList<>();


    private String title ;
    private int ID;
}

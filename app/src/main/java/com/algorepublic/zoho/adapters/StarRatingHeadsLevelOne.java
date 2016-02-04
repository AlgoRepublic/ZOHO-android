package com.algorepublic.zoho.adapters;

import java.util.ArrayList;

/**
 * Created by android on 2/3/16.
 */
public class StarRatingHeadsLevelOne {


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

    public ArrayList<StarRatingHeadsLevelTwo> getLevelTwos() {
        return levelTwos;
    }
    public void setLevelTwos(ArrayList<StarRatingHeadsLevelTwo> levelTwos) {
        this.levelTwos = levelTwos;
    }


    ArrayList<StarRatingHeadsLevelTwo> levelTwos = new ArrayList<>();
    private String title ;
    private int ID;
}

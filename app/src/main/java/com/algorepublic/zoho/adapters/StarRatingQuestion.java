package com.algorepublic.zoho.adapters;

/**
 * Created by android on 2/24/16.
 */
public class StarRatingQuestion {


    public void setQuestion(String question){
        this.question = question;
    }
    public String getQuestion(){
        return question;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public String getComment(){
        return comment;
    }

    public void setProgress(Integer progress){
        this.progress = progress;
    }
    public Integer getProgress(){
        return progress;
    }

    public void setID(Integer Id){
        this.ID = Id;
    }
    public Integer getID(){
        return ID;
    }

    private String question;
    private String comment;
    private Integer progress;
    private Integer ID;

}

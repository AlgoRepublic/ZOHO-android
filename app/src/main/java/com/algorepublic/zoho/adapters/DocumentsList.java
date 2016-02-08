package com.algorepublic.zoho.adapters;

/**
 * Created by android on 1/7/16.
 */
public class DocumentsList  implements Comparable<DocumentsList>{

    public int getID() {
        return ID;
    }
    public void setID(int id) {
        this.ID = id;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDescription() {
        return fileDescription;
    }
    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }
    public String getFileSizeInByte() {
        return fileSizeInByte;
    }
    public void setFileSizeInByte(String fileSizeInByte) {
        this.fileSizeInByte = fileSizeInByte;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedMilli() {
        return createdMilli;
    }
    public void setCreatedMilli(String createdMilli) {
        this.createdMilli = createdMilli;
    }

    public int getFileTypeID() {
        return fileTypeID;
    }
    public void setFileTypeID(int fileTypeID) {
        this.fileTypeID = fileTypeID;
    }

    public boolean getIsFav() {
        return isFav;
    }
    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    private int ID ;
    private String fileName ;
    private String fileDescription;
    private String fileSizeInByte;
    private String createdAt;
    private String createdMilli;
    private int fileTypeID;
    private boolean isFav;

    @Override
    public int compareTo(DocumentsList another) {
        return (Long.valueOf(this.getCreatedMilli()).compareTo(Long.valueOf(another.getCreatedMilli())));
    }
}

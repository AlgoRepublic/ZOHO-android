package com.algorepublic.zoho.adapters;

import java.io.File;

/**
 * Created by android on 2/16/16.
 */
public class AttachmentList {

    public String getFileName() {
    return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    public Integer getFileID() {
        return fileID;
    }
    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    private File file;
    private String fileName;
    private Integer fileID;
    private String fileUrl;
    private boolean isDeleted;
}

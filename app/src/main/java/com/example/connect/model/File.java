package com.example.connect.model;

public class File {
    private String fileName;
    private int fileImgId;

    public File(String fileName, int fileImgId) {
        this.fileName = fileName;
        this.fileImgId = fileImgId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileImgId() {
        return fileImgId;
    }

    public void setFileImgId(int fileImgId) {
        this.fileImgId = fileImgId;
    }
}

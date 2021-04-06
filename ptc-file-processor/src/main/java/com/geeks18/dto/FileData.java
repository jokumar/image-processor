package com.geeks18.dto;

import java.io.Serializable;

public class FileData implements Serializable {

    private String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}

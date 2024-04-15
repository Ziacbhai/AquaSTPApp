package com.ziac.aquastpapp.Activities;

public class DataPart {
    private final String fileName;
    private final byte[] content;
    private String type = null;

    public DataPart(String fileName, byte[] content, String s) {
        this.fileName = fileName;
        this.content = content;
        this.type = type;

    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public String getType() {
        return type;
    }
}

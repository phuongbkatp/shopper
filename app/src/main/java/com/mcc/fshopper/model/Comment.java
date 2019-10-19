package com.mcc.fshopper.model;

/**
 * Created by nasir3 on 11/16/17.
 */

public class Comment {

    private String id;
    private String message;
    private String dateTime;

    public Comment(String id, String message, String dateTime){
        this.id = id;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return dateTime;
    }
}

package com.example.android.chatapp.model;

/**
 * Created by tunde on 4/1/2018.
 */

public class Message {
    String mMessage,mUsername;
    String mCreatedAt;

    public Message(){

    }
    public Message(String message,String userName, String createdAt){
        mMessage = message;
        mUsername = userName;
        mCreatedAt = createdAt;

    }

    public String getMessage(){
        return mMessage;
    }
    public String getTimeCreated(){
        return mCreatedAt;
    }
    public String getUsername(){
        return mUsername;
    }
    public void setMessage(String message){
        mMessage = message;
    }
    public void setUsername(String username){
        mUsername = username;
    }
    public void setTimeCreated(String createdAt){
        mCreatedAt = createdAt;
    }

}

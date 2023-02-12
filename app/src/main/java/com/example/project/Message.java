package com.example.project;

public class Message {
    private String sender;
    private String receiver;
    private int type;
    private String timestamp;
    private String message;
    private byte[] photo;

    public static final int text = 1;
    public static final int pic = 2;
    public static final int loc = 3;

    public Message(String sender, String receiver, int type, String timestamp, String message, byte[] photo){
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.timestamp = timestamp;
        this.message = message;
        this.photo = photo;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getType(){
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getPhoto() {
        return photo;
    }
}

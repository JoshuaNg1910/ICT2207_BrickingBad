package com.example.project;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT, profilepic BLOB)");
        MyDB.execSQL("CREATE TABLE chat(sender TEXT NOT NULL, receiver TEXT NOT NULL, type TEXT NOT NULL, message TEXT, photo BLOB, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        MyDB.execSQL("DROP TABLE IF EXISTS chat");
    }

    public void updateChatsSender(String changeUsername, String currentUsername){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sender", changeUsername);
        MyDB.update("chat", cv, "sender = ?", new String[] {currentUsername});
    }

    public void updateChatsReceiver(String changeUsername, String currentUsername){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("receiver", changeUsername);
        MyDB.update("chat", cv, "receiver = ?", new String[] {currentUsername});
    }

    public void sendChat(Message message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sender", message.getSender());
        cv.put("receiver", message.getReceiver());
        cv.put("type", message.getType());
        cv.put("message", message.getMessage());
        cv.put("photo", message.getPhoto());
        cv.put("timestamp", message.getTimestamp());
        db.insert("chat", null, cv);
    }

    public ArrayList<Message> getAllMessages(String sender, String receiver){
        ArrayList<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM chat WHERE (sender = ? AND RECEIVER = ?) OR (SENDER = ? AND RECEIVER = ?) ORDER BY timestamp ASC", new String[] {sender, receiver, receiver, sender});
        if (cursor.moveToFirst()){
            int senderIndex = cursor.getColumnIndex("sender");
            int receiverIndex = cursor.getColumnIndex("receiver");
            int typeIndex = cursor.getColumnIndex("type");
            int messageIndex = cursor.getColumnIndex("message");
            int photoIndex = cursor.getColumnIndex("photo");
            int timestampIndex = cursor.getColumnIndex("timestamp");
            if (senderIndex != -1 && receiverIndex != -1 && typeIndex != -1 && messageIndex != -1 && photoIndex != -1 && timestampIndex != -1) {
                do {
                    String send = cursor.getString(senderIndex);
                    String receive = cursor.getString(receiverIndex);
                    int type = cursor.getInt(typeIndex);
                    String message = cursor.getString(messageIndex);
                    byte[] photo = cursor.getBlob(photoIndex);
                    String timestamp = cursor.getString(timestampIndex);
                    Message msg = new Message(send, receive, type, timestamp, message, photo);
                    messages.add(msg);
                } while (cursor.moveToNext());
            }
        }
        return messages;
    }

    public Cursor getAllUsersData(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username, profilepic FROM users WHERE username != ?", new String[]{username});
        return cursor;
    }

    public Boolean insertData(String username, String password, byte[] dp){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("profilepic", dp);
        long result = MyDB.insert("users", null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[] {username, password});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public byte[] retrieveImage(String username){
        byte[] dp = null;
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT profilepic FROM users WHERE username = ?", new String[] {username});
        if(cursor.moveToFirst()){
            dp = cursor.getBlob(0);
        }
        return dp;
    }

    public void updateData(String changeUsername, String currentUsername, byte[] dp){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("profilepic", dp);
        if (changeUsername.equals(currentUsername)){
            MyDB.update("users", cv, "username = ?", new String[] {currentUsername});
        }
        else{
            cv.put("username", changeUsername);
            MyDB.update("users", cv, "username = ?", new String[] {currentUsername});
        }
    }

    public void updatePassword(String username, String currentPass, String newPass){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("password", newPass);
        MyDB.update("users", cv, "username = ? AND password = ?", new String[] {username, currentPass});
    }

}
package com.example.project;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<String> chats = new ArrayList<>();

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT, profilepic BLOB)");
        MyDB.execSQL("CREATE TABLE messages(message TEXT PRIMARY KEY)");
        MyDB.execSQL("CREATE TABLE chats(chat TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        MyDB.execSQL("DROP TABLE IF EXISTS messages");
        MyDB.execSQL("DROP TABLE IF EXISTS chats");
    }
    public boolean insertMessage(String message){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("message", message);
        long results = MyDB.insert("messages", null, contentValues);
        if(results == -1)
            return false;
        else
            return true;
    }

    public boolean insertChat(String chat){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("chat", chat);
        long results = MyDB.insert("chats", null, contentValues);
        if(results == -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> loadChats() {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM chats", null);
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                chats.add(cursor.getString(cursor.getColumnIndexOrThrow("chat")));
            } while (cursor.moveToNext());
        }
        return chats;
    }

    public ArrayList<String> loadMessages() {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM messages", null);
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                messages.add(cursor.getString(cursor.getColumnIndexOrThrow("message")));
            } while (cursor.moveToNext());
        }
        return messages;
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
}
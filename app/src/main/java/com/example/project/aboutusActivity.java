package com.example.project;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class aboutusActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    Button alexButton;
    Button bransonButton;
    Button elsonButton;
    Button joshuaButton;
    Button tecklingButton;
    Button terenceButton;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    View header;
    CircleImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        alexButton = findViewById(R.id.alexButton);
        bransonButton = findViewById(R.id.bransonButton);
        elsonButton = findViewById(R.id.elsonButton);
        joshuaButton = findViewById(R.id.joshuaButton);
        tecklingButton = findViewById(R.id.tecklingButton);
        terenceButton = findViewById(R.id.terenceButton);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.aboutusToolbar);
        header = navigationView.getHeaderView(0);
        imageView = header.findViewById(R.id.image);
        textView = header.findViewById(R.id.user);
        String encodedDP = getSharedPreferences("session", MODE_PRIVATE).getString("image", "");
        byte[] dp = Base64.decode(encodedDP, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(dp, 0, dp.length);
        String username = getSharedPreferences("session", MODE_PRIVATE).getString("username", "");
        imageView.setImageBitmap(bitmap);
        textView.setText(username);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(aboutusActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(aboutusActivity.this, Manifest.permission.CAMERA)){
                        ActivityCompat.requestPermissions(aboutusActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    }
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
            }
        });
        alexButton.setOnClickListener(this);
        bransonButton.setOnClickListener(this);
        elsonButton.setOnClickListener(this);
        joshuaButton.setOnClickListener(this);
        tecklingButton.setOnClickListener(this);
        terenceButton.setOnClickListener(this);
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
            imageView.setImageBitmap(photo);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.alexButton){
            Intent intent = new Intent(this, alexActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.bransonButton) {
            Intent intent = new Intent(this, bransonActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.elsonButton) {
            Intent intent = new Intent(this, elsonActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.joshuaButton) {
            Intent intent = new Intent(this, joshuaActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.tecklingButton) {
            Intent intent = new Intent(this, tecklingActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.terenceButton) {
            Intent intent = new Intent(this, terenceActivity.class);
            startActivity(intent);
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
            case R.id.nav_chat:
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_aboutus:
                Intent aboutus = new Intent(this, aboutusActivity.class);
                startActivity(aboutus);
                break;
            case R.id.nav_logout:
                SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
                Boolean isLoggedIn = getSharedPreferences("session", MODE_PRIVATE).getBoolean("isLoggedIn", false);
                Log.i("Logged In", isLoggedIn.toString());
                SharedPreferences.Editor editor = session.edit();
                editor.clear();
                editor.commit();
                isLoggedIn = getSharedPreferences("session", MODE_PRIVATE).getBoolean("isLoggedIn", false);
                Log.i("Logged In", isLoggedIn.toString());
                Intent logout = new Intent(this, HomeActivity.class);
                startActivity(logout);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
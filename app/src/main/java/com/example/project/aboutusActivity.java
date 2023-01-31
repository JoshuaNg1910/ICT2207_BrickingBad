package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_newsfeed:
                break;
            case R.id.nav_chat:
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_aboutus:
                Intent intent = new Intent(this, aboutusActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
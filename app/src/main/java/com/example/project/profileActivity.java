package com.example.project;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    View header;
    CircleImageView imageView, circleView;
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.profileToolbar);
        editText = findViewById(R.id.username);
        circleView = findViewById(R.id.imageView);
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileActivity.this, profileActivity.class);
                startActivity(intent);
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
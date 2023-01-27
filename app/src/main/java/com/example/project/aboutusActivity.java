package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class aboutusActivity extends AppCompatActivity implements View.OnClickListener{

    Button alexButton;
    Button bransonButton;
    Button elsonButton;
    Button joshuaButton;
    Button tecklingButton;
    Button terenceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alexButton = findViewById(R.id.alexButton);
        bransonButton = findViewById(R.id.bransonButton);
        elsonButton = findViewById(R.id.elsonButton);
        joshuaButton = findViewById(R.id.joshuaButton);
        tecklingButton = findViewById(R.id.tecklingButton);
        terenceButton = findViewById(R.id.terenceButton);
        alexButton.setOnClickListener(this);
        bransonButton.setOnClickListener(this);
        elsonButton.setOnClickListener(this);
        joshuaButton.setOnClickListener(this);
        tecklingButton.setOnClickListener(this);
        terenceButton.setOnClickListener(this);
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
}
package com.example.GraduationProject.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.GraduationProject.Controller.Patients.PationtLogin;
import com.example.GraduationProject.R;

// FIRST PAGE WHEN RUNNING THE APP  SPLASH SCREEN
public class SplachScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplachScreen.this, PationtLogin.class));
                finish();
            }

        },1000);
    }
}
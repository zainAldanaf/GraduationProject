package com.example.GraduationProject.Doctors;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.GraduationProject.R;

import java.util.Locale;

public class DoctorSettings extends AppCompatActivity {

     Switch themeSwitch;
     Spinner languageSpinner;
     LinearLayout profile_settings;
     TextView go_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        themeSwitch = findViewById(R.id.theme_switch);
        languageSpinner = findViewById(R.id.language_spinner);

        go_profile=findViewById(R.id.profile_go_settings);
        go_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorSettings.this, DoctorProfile.class));
            }
        });
        profile_settings=findViewById(R.id.profile_settings_page);
        profile_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorSettings.this, DoctorProfile.class));

            }
        });
        // Handle Theme Switch
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Handle Language Selection
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedLanguage = (String) parent.getItemAtPosition(position);
                setLocale(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Change App Language
    private void setLocale(String language) {
        Locale locale;
        switch (language) {
            case "Arabic":
                locale = new Locale("ar");
                break;
            case "French":
                locale = new Locale("fr");
                break;
            default:
                locale = new Locale("en");
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Refresh Activity
        recreate();
    }
}
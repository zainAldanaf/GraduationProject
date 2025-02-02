package com.example.GraduationProject.Doctors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.GraduationProject.INFO_Page;
import com.example.GraduationProject.R;

public class DoctorSettings extends AppCompatActivity {

     Switch themeSwitch;
     Spinner languageSpinner;
     TextView info_settings;
     TextView profile_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.d_one), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

      //  themeSwitch = findViewById(R.id.theme_switch);
      //  languageSpinner = findViewById(R.id.language_spinner);

        profile_settings = findViewById(R.id.Profile_Settings);
        profile_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorSettings.this, DoctorProfile.class));
            }
        });
        info_settings = findViewById(R.id.Info_Settings);
        info_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorSettings.this, INFO_Page.class));

            }
        });
    }}
        // Handle Theme Switch
//        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            }
//        });
//
//        // Handle Language Selection
//        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
//                String selectedLanguage = (String) parent.getItemAtPosition(position);
//                setLocale(selectedLanguage);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//    }
//
//    // Change App Language
//    private void setLocale(String language) {
//        Locale locale;
//        switch (language) {
//            case "Arabic":
//                locale = new Locale("ar");
//                break;
//            case "French":
//                locale = new Locale("fr");
//                break;
//            default:
//                locale = new Locale("en");
//        }
//
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.setLocale(locale);
//        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
//
//        // Refresh Activity
//        recreate();
//    }

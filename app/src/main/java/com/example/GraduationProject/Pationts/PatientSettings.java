package com.example.GraduationProject.Pationts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.GraduationProject.Doctors.DoctorProfile;
import com.example.GraduationProject.Doctors.DoctorSettings;
import com.example.GraduationProject.INFO_Page;
import com.example.GraduationProject.R;

public class PatientSettings extends AppCompatActivity {


    TextView info_settings;
    TextView profile_pat_settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profile_pat_settings = findViewById(R.id.Profile_Pat_Settings);
        profile_pat_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientSettings.this, ProfilePAtiont.class));
            }
        });
        info_settings = findViewById(R.id.Info_Settings);
        info_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientSettings.this, INFO_Page.class));

            }
        });

    }
}
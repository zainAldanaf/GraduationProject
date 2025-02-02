package com.example.GraduationProject;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.GraduationProject.Pationts.DetailsScreen;
import com.example.GraduationProject.Pationts.HomeScreen;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BookOppiontment extends AppCompatActivity {

    private EditText userName, userAddress;
    private TextView selectedDateTime;
    private Button pickDateTime, bookAppointment;
    private Calendar calendar;
    private String dateString, timeString;
    private FirebaseFirestore db;
    private static final String CHANNEL_ID = "appointment_channel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_oppiontment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.name_opp);
        userAddress = findViewById(R.id.address_opp);
        selectedDateTime = findViewById(R.id.selected_time_date);
        pickDateTime = findViewById(R.id.pick_date_time);
        bookAppointment = findViewById(R.id.add_oppintment);
        calendar = Calendar.getInstance();
        db = FirebaseFirestore.getInstance();

        // Pick Date & Time
        pickDateTime.setOnClickListener(v -> showDateTimePicker());

        // Save appointment to Firebase
        bookAppointment.setOnClickListener(v -> saveAppointmentToFirestore());
        createNotificationChannel();
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    dateString = dateFormat.format(calendar.getTime());

                    showTimePicker();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    timeString = timeFormat.format(calendar.getTime());

                    selectedDateTime.setText("Selected: " + dateString + " " + timeString);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void saveAppointmentToFirestore() {
        String name = userName.getText().toString();
        String address = userAddress.getText().toString();

        if (name.isEmpty() || address.isEmpty() || dateString == null || timeString == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("userName", name);
        appointment.put("address", address);
        appointment.put("date", dateString);
        appointment.put("time", timeString);

        db.collection("appointments").add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Appointment Booked Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookOppiontment.this, HomeScreen.class));
                    sendNotification();
                })

                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error Booking Appointment", Toast.LENGTH_SHORT).show());
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Appointment Channel";
            String description = "Channel for appointment notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Appointment Confirmed")
                .setContentText("Your appointment is scheduled for " + dateString + " at " + timeString)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}

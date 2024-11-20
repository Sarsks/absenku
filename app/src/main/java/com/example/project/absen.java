package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class absen extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView textViewUserId, textView;
    private EditText locationInput;
    private Button btnSubmitAttendance;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);

        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        textViewUserId = findViewById(R.id.textViewUserId);
        textView = findViewById(R.id.text1);
        locationInput = findViewById(R.id.location);
        btnSubmitAttendance = findViewById(R.id.kirim);

        // Fetch and display user data
        fetchAndSetData();

        // Get location and handle permissions
        getLocation();

        // Handle submit attendance action
        btnSubmitAttendance.setOnClickListener(v -> submitAttendance());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }

    private void fetchAndSetData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId != null) {
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String kelas = documentSnapshot.getString("kelas");
                            textView.setText(kelas);
                        } else {
                            textView.setText("Dokumen tidak ditemukan");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Absen", "Error fetching data", e);
                        textView.setText("Error loading data");
                    });
        } else {
            textView.setText("User ID tidak ditemukan");
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                // Check for mock location
                if (isMockLocation(location)) {
                    Toast.makeText(this, "Fake location detected! Please turn off mock location.", Toast.LENGTH_LONG).show();
                    btnSubmitAttendance.setEnabled(false); // Disable submit button
                    return;
                }

                double userLatitude = location.getLatitude();
                double userLongitude = location.getLongitude();

                // Reference location (e.g., office)
                double referenceLatitude = -7.559759;
                double referenceLongitude = 110.831231;

                // Calculate distance between user and reference location
                double distance = calculateDistance(userLatitude, userLongitude, referenceLatitude, referenceLongitude);

                // Maximum allowed radius (10 km)
                double maxRadius = 10.0;

                if (distance <= maxRadius) {
                    locationInput.setText(userLatitude + ", " + userLongitude);
                    Toast.makeText(this, "Location is valid, you are within the radius!", Toast.LENGTH_SHORT).show();
                    btnSubmitAttendance.setEnabled(true); // Enable submit button
                } else {
                    Toast.makeText(this, "You are out of the allowed location radius!", Toast.LENGTH_LONG).show();
                    btnSubmitAttendance.setEnabled(false); // Disable submit button
                }
            } else {
                Toast.makeText(this, "Unable to retrieve location", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isMockLocation(Location location) {
        // Check for mock location (Available from API 18+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return location.isFromMockProvider();
        }
        return false;
    }

    private void submitAttendance() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        if (userId == null) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String kelas = documentSnapshot.getString("kelas");
                    }

                    String name = sharedPreferences.getString("userName", "Unknown User");
                    String location = locationInput.getText().toString();
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    Attendance attendance = new Attendance(name, "Hadir", location, timestamp, "kelas");

                    db.collection("user_attendance").document(userId)
                            .collection("attendance")
                            .add(attendance)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Attendance successfully recorded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to record attendance", Toast.LENGTH_SHORT).show();
                            });
                });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Mengarahkan ke Home screen atau mengakhiri activity sesuai kebutuhan

        Intent intent = new Intent(absen.this, MainActivity.class); // Ganti `spas.class` dengan Activity utama Anda
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

        // Optional: Memberikan feedback jika ingin pindah ke Home screen
        // Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        // homeIntent.addCategory(Intent.CATEGORY_HOME);
        // homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(homeIntent);
    }
}

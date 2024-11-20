package com.example.project;

import android.content.Intent;
import java.util.UUID;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class register extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, nomerEditText, kelasEditText;
    private Button registerButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();


        usernameEditText = findViewById(R.id.eusername);
        passwordEditText = findViewById(R.id.epassword);
        nomerEditText = findViewById(R.id.eAbsen);
        kelasEditText = findViewById(R.id.eKelas);
        registerButton = findViewById(R.id.RegisterButton);


        registerButton.setOnClickListener(view -> registerUser());
    }


    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String kelas = kelasEditText.getText().toString().trim();
        String absen = nomerEditText.getText().toString().trim();

        // Validasi input
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(register.this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat objek data pengguna
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        user.put("kelas", kelas); // Simpan nilai kelas dengan benar
        user.put("absen", absen); // Simpan nilai absen dengan benar
        user.put("id", UUID.randomUUID().toString()); // Tambahkan ID unik

        // Simpan data pengguna di Firestore
        db.collection("users").document(username) // Gunakan username sebagai ID dokumen
                .set(user, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(register.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                    // Lakukan tindakan lain, seperti pindah ke halaman login
                    startActivity(new Intent(register.this, LOGIN.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(register.this, "Registrasi gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



}

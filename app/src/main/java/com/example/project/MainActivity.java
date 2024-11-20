package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button btnlogout, btnabsen, btnhistory, btnkalender;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;



        });
        btnlogout = findViewById(R.id.btns);
        btnabsen =findViewById(R.id.btnabsen);
        btnhistory = findViewById(R.id.history);
        btnkalender = findViewById(R.id.button3);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Jika belum login, pindah ke LoginActivity
            startActivity(new Intent(MainActivity.this, LOGIN.class));
            finish();
        }
        btnlogout.setOnClickListener(view -> logout());
        btnabsen.setOnClickListener(view -> ab());
        btnhistory.setOnClickListener(view -> ac());
        btnkalender.setOnClickListener(view -> kalender());
    }

    private  void   logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // Pindah kembali ke LoginActivity
        startActivity(new Intent(MainActivity.this, LOGIN.class));
        finish();

    }
    private  void  ab(){
        startActivity(new Intent(MainActivity.this,absen.class));
        finish();

    }
    private  void  ac(){
        startActivity(new Intent(MainActivity.this,history.class));
        finish();

    }

    private  void  kalender(){
        startActivity(new Intent(MainActivity.this,kalender.class));
        finish();

    }
}
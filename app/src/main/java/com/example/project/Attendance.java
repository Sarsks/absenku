package com.example.project;

public class Attendance {
    private String name;
    private String status;
    private String location;
    private String timestamp;
    private String kelas;  // Pastikan untuk mendeklarasikan kelas

    // Konstruktor tanpa argumen (diperlukan oleh Firestore)
    public Attendance() {
        // Kosong
    }

    // Konstruktor dengan argumen
    public Attendance(String name, String status, String location, String timestamp, String kelas) {
        this.name = name;
        this.status = status;
        this.location = location;
        this.timestamp = timestamp;
        this.kelas = kelas;  // Menginisialisasi kelas
    }

    // Getter dan Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getKelas() {
        return kelas;  // Getter untuk kelas
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;  // Setter untuk kelas
    }
}

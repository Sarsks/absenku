plugins {
    alias(libs.plugins.android.application) // Plugin untuk aplikasi Android
    id("com.google.gms.google-services") // Plugin Google Services
}

android {
    namespace = "com.example.project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Android dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase dependencies
    implementation(libs.firebase.auth)
    implementation("com.google.firebase:firebase-firestore") // Firestore


    // Location services
    implementation(libs.play.services.location) // Play Services Location

    // Firebase Analytics and Firebase BOM
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1")) // Firebase BOM untuk kompatibilitas versi

    // Google Play Services dependencies
    implementation("com.google.android.gms:play-services-base:17.6.0")
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation(libs.firebase.storage)


    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

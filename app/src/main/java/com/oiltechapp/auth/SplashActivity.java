package com.oiltechapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.oiltechapp.R;
//import com.oiltechapp.ui.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.oiltechapp.ui.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 с

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Включение оффлайн-режима Firestore
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)  // Кэширование данных
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        // Проверка авторизации
        FirebaseAuth auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
            if (auth.getCurrentUser() != null) {
                // Пользователь уже вошёл - переход в MainActivity
                startActivity(new Intent(this, MainActivity.class));
            } else {
                // Пользователь не авторизован - переход на экран входа
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}
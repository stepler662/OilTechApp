package com.oiltechapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.oiltechapp.R;
import com.oiltechapp.auth.LoginActivity;
import com.oiltechapp.qr.QRScannerActivity;
import com.oiltechapp.tests.TestsActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Настройка ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Главное меню");
        }

        Button btnTests = findViewById(R.id.btnTests);
        Button btnQRScanner = findViewById(R.id.btnQRScanner);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnTests.setOnClickListener(v -> {
            startActivity(new Intent(this, TestsActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        btnQRScanner.setOnClickListener(v -> {
            startActivity(new Intent(this, QRScannerActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        // Исправленная кнопка выхода
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity(); // Закрывает все предыдущие Activity
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    @Override
    public void onBackPressed() {
        // Выход из приложения при нажатии "Назад"
        finishAffinity();
    }
}
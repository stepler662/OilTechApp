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

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Инициализация кнопок
        Button btnTests = findViewById(R.id.btnTests);
        Button btnQRScanner = findViewById(R.id.btnQRScanner);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Обработчики нажатий
        btnTests.setOnClickListener(v ->
                startActivity(new Intent(this, TestsActivity.class)));

        btnQRScanner.setOnClickListener(v ->
                startActivity(new Intent(this, QRScannerActivity.class)));

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
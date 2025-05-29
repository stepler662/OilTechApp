package com.oiltechapp.qr;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.oiltechapp.R;

public class QRScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        // Настройка ActionBar с кнопкой "Назад"
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("QR-сканер");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Возврат в главное меню
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }
}
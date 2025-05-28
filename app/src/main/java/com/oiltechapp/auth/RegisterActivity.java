package com.oiltechapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.oiltechapp.R;
import com.oiltechapp.database.AppDatabase;
import com.oiltechapp.models.User;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "Activity created");

        // Обработчик для кнопки регистрации
        findViewById(R.id.btnRegister).setOnClickListener(v -> handleRegistration());

        // Обработчик для текста "Уже есть аккаунт? Войти"
        findViewById(R.id.textViewLoginLink).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Обработчик для логотипа (возврат назад)
        findViewById(R.id.ivLogo).setOnClickListener(v -> onBackPressed());
    }

    private void handleRegistration() {
        try {
            Log.d(TAG, "Register button clicked");

            String username = ((android.widget.EditText)findViewById(R.id.etRegUsername)).getText().toString();
            String password = ((android.widget.EditText)findViewById(R.id.etRegPassword)).getText().toString();

            Log.d(TAG, "Username: " + username + ", Password: " + password);

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    Log.d(TAG, "Starting DB operation");
                    AppDatabase db = AppDatabase.getInstance(this);

                    User existingUser = db.userDao().getUserByUsername(username);
                    runOnUiThread(() -> {
                        if (existingUser != null) {
                            Toast.makeText(this, "Пользователь уже существует", Toast.LENGTH_SHORT).show();
                        } else {
                            db.userDao().insertUser(new User(username, password));
                            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Database error", e);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Ошибка базы данных: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }).start();

        } catch (Exception e) {
            Log.e(TAG, "General error", e);
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
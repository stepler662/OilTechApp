package com.oiltechapp.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.oiltechapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.oiltechapp.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText eEmail;
    private EditText ePassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Инициализация UI элементов
        eEmail = findViewById(R.id.etUsername); // Переименуйте в etEmail в layout, если нужно
        ePassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Обработчик кнопки входа
        btnLogin.setOnClickListener(v -> handleLogin());

        // Обработчик кнопки регистрации
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = eEmail.getText().toString().trim();
        String password = ePassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        // Аутентификация через Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Успешный вход
                        Toast.makeText(LoginActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Ошибка входа
                        Toast.makeText(LoginActivity.this,
                                "Ошибка входа: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Firebase закрывает соединение автоматически
    }
}
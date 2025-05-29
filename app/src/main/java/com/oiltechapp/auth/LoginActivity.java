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
import com.oiltechapp.ui.MainMenuActivity;

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

        // Привязка UI элементов
        initializeViews();

        // Установка обработчиков событий
        setupClickListeners();
    }

    private void initializeViews() {
        eEmail = findViewById(R.id.etEmail);
        ePassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupClickListeners() {
        // Обработка входа
        btnLogin.setOnClickListener(v -> handleLogin());

        // Переход на регистрацию
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void handleLogin() {
        String email = eEmail.getText().toString().trim();
        String password = ePassword.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        authenticateUser(email, password);
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Пароль должен содержать минимум 6 символов", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void authenticateUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        navigateToMainActivity(email);
                    } else {
                        showError(task.getException().getMessage());
                    }
                });
    }

    private void navigateToMainActivity(String email) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void showError(String errorMessage) {
        Toast.makeText(
                LoginActivity.this,
                "Ошибка входа: " + errorMessage,
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
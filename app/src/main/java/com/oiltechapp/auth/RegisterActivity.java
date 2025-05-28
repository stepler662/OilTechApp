package com.oiltechapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oiltechapp.R;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER_DEBUG";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Обработчик кнопки регистрации
        findViewById(R.id.btnRegister).setOnClickListener(v -> handleRegistration());

        // Переход на экран входа
        findViewById(R.id.textViewLoginLink).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void handleRegistration() {
        String email = ((android.widget.EditText) findViewById(R.id.etRegEmail)).getText().toString().trim();
        String password = ((android.widget.EditText) findViewById(R.id.etRegPassword)).getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Пароль должен содержать минимум 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создание пользователя в Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Дополнительные данные пользователя (если нужны)
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            userData.put("created_at", System.currentTimeMillis());

                            // Сохранение в Firestore (опционально)
                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                                        finish(); // Закрываем активити после успешной регистрации
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Ошибка: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
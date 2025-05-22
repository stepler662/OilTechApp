package com.oiltechapp.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.oiltechapp.R;
import com.oiltechapp.database.AppDatabase;
import com.oiltechapp.models.User;
import com.oiltechapp.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText eUsername;
    private EditText ePassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Инициализация базы данных
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class,
                        "oiltech-db")
                .allowMainThreadQueries() // Для упрощения (в реальном приложении используйте потоки/корутины)
                .build();

        // Инициализация UI элементов
        eUsername = findViewById(R.id.etUsername);
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
        String username = eUsername.getText().toString().trim();
        String password = ePassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверка пользователя в базе данных
        User user = db.userDao().getUser(username, password);

        if (user != null) {
            // Успешный вход
            Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show();

            // Переход на MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Закрыть LoginActivity
        } else {
            // Неверные данные
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрываем соединение с базой данных
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
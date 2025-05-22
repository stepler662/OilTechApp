package com.oiltechapp.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.oiltechapp.R;
import com.oiltechapp.database.AppDatabase;
import com.oiltechapp.models.User;

public class RegisterActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText etRegUsername;
    private EditText etRegPassword;
    private Button btnRegister;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Инициализация базы данных
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class,
                        "oiltech-db")
                .allowMainThreadQueries()
                .build();

        // Инициализация UI элементов
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        // Обработчик кнопки регистрации
        btnRegister.setOnClickListener(v -> handleRegistration());

        // Обработчик ссылки для входа (добавляем эту часть)
        tvLoginLink.setOnClickListener(v -> {
            // Закрываем текущую активити и возвращаемся к экрану входа
            finish();
        });
    }

    private void handleRegistration() {
        String username = etRegUsername.getText().toString().trim();
        String password = etRegPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверка и регистрация пользователя
        User existingUser = db.userDao().getUserByUsername(username);
        if (existingUser != null) {
            Toast.makeText(this, "Пользователь уже существует", Toast.LENGTH_SHORT).show();
        } else {
            db.userDao().insertUser(new User(username, password));
            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
            finish(); // Закрываем экран регистрации
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
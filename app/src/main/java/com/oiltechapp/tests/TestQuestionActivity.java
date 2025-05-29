package com.oiltechapp.tests;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.oiltechapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class TestQuestionActivity extends AppCompatActivity {
    private static final int TOTAL_QUESTIONS = 10;
    private static final String TAG = "TestQuestionActivity";

    private TextView questionNumberTextView, questionTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;
    private Button nextQuestionButton;
    private ImageButton backButton;

    private FirebaseFirestore db;
    private String ticketId;
    private List<Question> questions = new ArrayList<>();
    private List<Integer> userAnswers = new ArrayList<>();
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        // Логирование для отладки
        Log.d(TAG, "onCreate called");

        // Инициализация UI элементов
        questionNumberTextView = findViewById(R.id.questionNumberTextView);
        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        option1RadioButton = findViewById(R.id.option1RadioButton);
        option2RadioButton = findViewById(R.id.option2RadioButton);
        option3RadioButton = findViewById(R.id.option3RadioButton);
        option4RadioButton = findViewById(R.id.option4RadioButton);
        nextQuestionButton = findViewById(R.id.nextQuestionButton);
        backButton = findViewById(R.id.backButton);

        // Получаем ticketId из Intent
        ticketId = getIntent().getStringExtra("ticketId");
        if (ticketId == null) {
            Log.e(TAG, "ticketId is null");
            Toast.makeText(this, "Ошибка: Не указан билет", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "ticketId: " + ticketId);

        // Инициализация Firestore
        db = FirebaseFirestore.getInstance();

        // Загружаем вопросы
        loadQuestions();

        // Обработчик кнопки "Назад"
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });

        // Обработчик кнопки "Следующий вопрос"
        nextQuestionButton.setOnClickListener(v -> {
            Log.d(TAG, "Next question button clicked");
            // Сохраняем ответ пользователя
            int selectedOption = getSelectedOption();
            if (selectedOption != -1) {
                userAnswers.add(selectedOption);
                currentQuestionIndex++;
                if (currentQuestionIndex < TOTAL_QUESTIONS) {
                    displayQuestion();
                } else {
                    showResults();
                }
            } else {
                Toast.makeText(this, "Выберите вариант ответа", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestions() {
        Log.d(TAG, "Loading questions for ticketId: " + ticketId);
        db.collection("tickets").document(ticketId).collection("questions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully loaded questions, count: " + task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String questionText = document.getString("question_text");
                            List<String> options = (List<String>) document.get("options");
                            Long correctOptionLong = document.getLong("correct_option");
                            int correctOption = correctOptionLong != null ? correctOptionLong.intValue() : 0;

                            // Проверка на null для questionText и options
                            if (questionText != null && options != null && options.size() == 4) {
                                questions.add(new Question(questionText, options, correctOption));
                            } else {
                                Log.w(TAG, "Invalid question data: " + document.getId());
                            }
                        }

                        if (questions.isEmpty()) {
                            Log.w(TAG, "No questions found for ticketId: " + ticketId);
                            Toast.makeText(this, "Вопросы не найдены", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                        if (questions.size() > TOTAL_QUESTIONS) {
                            questions = questions.subList(0, TOTAL_QUESTIONS);
                        }

                        displayQuestion();
                    } else {
                        Log.e(TAG, "Error loading questions", task.getException());
                        Toast.makeText(this, "Ошибка загрузки вопросов: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load questions", e);
                    Toast.makeText(this, "Ошибка загрузки вопросов: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    private void displayQuestion() {
        if (questions.isEmpty()) {
            Log.e(TAG, "Cannot display question: questions list is empty");
            Toast.makeText(this, "Нет вопросов для отображения", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (currentQuestionIndex >= questions.size()) {
            Log.e(TAG, "Current question index out of bounds: " + currentQuestionIndex);
            showResults();
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        questionNumberTextView.setText("Вопрос " + (currentQuestionIndex + 1) + "/" + TOTAL_QUESTIONS);
        questionTextView.setText(currentQuestion.getQuestionText());
        option1RadioButton.setText(currentQuestion.getOptions().get(0));
        option2RadioButton.setText(currentQuestion.getOptions().get(1));
        option3RadioButton.setText(currentQuestion.getOptions().get(2));
        option4RadioButton.setText(currentQuestion.getOptions().get(3));
        optionsRadioGroup.clearCheck();

        if (currentQuestionIndex == TOTAL_QUESTIONS - 1) {
            nextQuestionButton.setText("Посмотреть результаты");
        } else {
            nextQuestionButton.setText("Следующий вопрос");
        }
    }

    private int getSelectedOption() {
        int checkedId = optionsRadioGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.option1RadioButton) return 0;
        if (checkedId == R.id.option2RadioButton) return 1;
        if (checkedId == R.id.option3RadioButton) return 2;
        if (checkedId == R.id.option4RadioButton) return 3;
        return -1;
    }

    private void showResults() {
        if (userAnswers.size() != TOTAL_QUESTIONS) {
            Log.w(TAG, "User answers count does not match total questions: " + userAnswers.size());
        }

        int correctAnswers = 0;
        for (int i = 0; i < Math.min(userAnswers.size(), questions.size()); i++) {
            if (userAnswers.get(i) == questions.get(i).getCorrectOption()) {
                correctAnswers++;
            }
        }
        int incorrectAnswers = TOTAL_QUESTIONS - correctAnswers;

        Intent intent = new Intent(this, TestResultsActivity.class);
        intent.putExtra("totalQuestions", TOTAL_QUESTIONS);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("incorrectAnswers", incorrectAnswers);
        intent.putExtra("ticketId", ticketId);
        startActivity(intent);
        finish();
    }
}


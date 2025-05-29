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

public class TestQuestionActivity extends AppCompatActivity {
    private static final int TOTAL_QUESTIONS = 10;
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
        db = FirebaseFirestore.getInstance();

        // Загружаем вопросы
        loadQuestions();

        // Обработчик кнопки "Назад"
        backButton.setOnClickListener(v -> finish());

        // Обработчик кнопки "Следующий вопрос"
        nextQuestionButton.setOnClickListener(v -> {
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
        db.collection("tickets").document(ticketId).collection("questions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String questionText = document.getString("question_text");
                            List<String> options = (List<String>) document.get("options");
                            Long correctOptionLong = document.getLong("correct_option");
                            int correctOption = correctOptionLong != null ? correctOptionLong.intValue() : 0;
                            questions.add(new Question(questionText, options, correctOption));
                        }
                        // Сортируем вопросы, если нужно, или берем первые 10
                        if (questions.size() > TOTAL_QUESTIONS) {
                            questions = questions.subList(0, TOTAL_QUESTIONS);
                        }
                        displayQuestion();
                    } else {
                        Toast.makeText(this, "Ошибка загрузки вопросов", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayQuestion() {
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
        int correctAnswers = 0;
        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            if (userAnswers.get(i) == questions.get(i).getCorrectOption()) {
                correctAnswers++;
            }
        }
        int incorrectAnswers = TOTAL_QUESTIONS - correctAnswers;

        // Запускаем новую Activity или показываем Dialog с результатами
        Intent intent = new Intent(this, TestResultsActivity.class);
        intent.putExtra("totalQuestions", TOTAL_QUESTIONS);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("incorrectAnswers", incorrectAnswers);
        intent.putExtra("ticketId", ticketId);
        startActivity(intent);
        finish();
    }
}


package com.oiltechapp.tests;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.oiltechapp.R;

public class TestResultsActivity extends AppCompatActivity {
    private TextView totalQuestionsTextView, correctAnswersTextView, incorrectAnswersTextView;
    private Button restartButton;
    private String ticketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);

        totalQuestionsTextView = findViewById(R.id.totalQuestionsTextView);
        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        incorrectAnswersTextView = findViewById(R.id.incorrectAnswersTextView);
        restartButton = findViewById(R.id.restartButton);

        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int incorrectAnswers = getIntent().getIntExtra("incorrectAnswers", 0);
        ticketId = getIntent().getStringExtra("ticketId");

        totalQuestionsTextView.setText("Всего вопросов: " + totalQuestions);
        correctAnswersTextView.setText("Правильных ответов: " + correctAnswers);
        incorrectAnswersTextView.setText("Неправильных ответов: " + incorrectAnswers);

        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestQuestionActivity.class);
            intent.putExtra("ticketId", ticketId);
            startActivity(intent);
            finish();
        });
    }
}

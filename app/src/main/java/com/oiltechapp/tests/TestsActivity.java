package com.oiltechapp.tests;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.oiltechapp.R;

public class TestsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        Button buttonTicket1 = findViewById(R.id.buttonTicket1);
        Button buttonTicket2 = findViewById(R.id.buttonTicket2);
        Button buttonTicket3 = findViewById(R.id.buttonTicket3);

        buttonTicket1.setOnClickListener(v -> startTest("ticket_1"));
        buttonTicket2.setOnClickListener(v -> startTest("ticket_2"));
        buttonTicket3.setOnClickListener(v -> startTest("ticket_3"));
    }

    private void startTest(String ticketId) {
        Intent intent = new Intent(this, TestQuestionActivity.class);
        intent.putExtra("ticketId", ticketId);
        startActivity(intent);
    }
}
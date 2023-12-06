package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class StudyMethodActivity extends AppCompatActivity {

    Button questionnaireButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_method);

        questionnaireButton = findViewById(R.id.questionnaireButton);
        questionnaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestionnaire();
            }
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

    }

    // Start Questionnaire
    private void startQuestionnaire() {
        Intent intent = new Intent(this, QuestionnaireActivity.class);
        startActivity(intent);
    }

    // Go Back
    private void goBack() {
        onBackPressed();
    }
}
package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class BreakTimerActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 300000;
    private ProgressBar progressBar_breakTimer;
    private TextView mTextViewCountDown_breakTimer;
    private ImageButton mButtonStartPause_breakTimer;
    private ImageButton mButtonReset_breakTimer;
    private ImageButton musicBtnBreakTimer;
    private CountDownTimer mCountDownBreakTimer;
    private boolean mBreakTimerRunning;
    private long mBreakTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_timer);

        mTextViewCountDown_breakTimer = findViewById(R.id.text_view_countdownBreakTimer);
        progressBar_breakTimer = findViewById(R.id.roundProgressBar_BreakTimer);
        mButtonStartPause_breakTimer = findViewById(R.id.startButton_BreakTimer);
        mButtonReset_breakTimer = findViewById(R.id.resetButton_BreakTimer);
        musicBtnBreakTimer = findViewById(R.id.musicButtonBreakTimer);

        // Start Timer Button
        mButtonStartPause_breakTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBreakTimerRunning) {
                    pauseBreakTimer();
                } else {
                    startBreakTimer();
                }
            }
        });

        // Reset Timer Button
        mButtonReset_breakTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBreakTimer();
            }
        });

        // Study Music Navigate
        musicBtnBreakTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BreakTimerActivity.this, StudyMusicActivity.class);
                startActivity(intent);
            }
        });

        updateBreakCountDownText();

    }

    private void startBreakTimer() {
        mCountDownBreakTimer = new CountDownTimer(mBreakTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBreakTimeLeftInMillis = millisUntilFinished;
                updateBreakCountDownText();
                updateProgressBarBreakTimer();
            }

            @Override
            public void onFinish() {
                mBreakTimerRunning = false;
                mButtonStartPause_breakTimer.setVisibility(View.INVISIBLE);
                mButtonReset_breakTimer.setVisibility(View.VISIBLE);

                // Save timer duration to Firestore when the timer finishes
                saveBreakTimerDurationToFirestore(START_TIME_IN_MILLIS - mBreakTimeLeftInMillis);

                // Start Pomodoro Timer
                startPomodoroTimer();

            }
        }.start();

        mBreakTimerRunning = true;
        mButtonReset_breakTimer.setVisibility(View.INVISIBLE);
    }

    private void pauseBreakTimer() {
        mCountDownBreakTimer.cancel();
        mBreakTimerRunning = false;
        mButtonReset_breakTimer.setVisibility(View.VISIBLE);
    }

    private void resetBreakTimer() {
        mBreakTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateBreakCountDownText();
        updateProgressBarBreakTimer();
        mButtonReset_breakTimer.setVisibility(View.INVISIBLE);
        mButtonStartPause_breakTimer.setVisibility(View.VISIBLE);
    }

    private void updateBreakCountDownText() {
        int minutes = (int) (mBreakTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mBreakTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown_breakTimer.setText(timeLeftFormatted);
    }

    private void updateProgressBarBreakTimer() {
        int progress = (int) ((START_TIME_IN_MILLIS - mBreakTimeLeftInMillis) * 100 / START_TIME_IN_MILLIS);
        progressBar_breakTimer.setProgress(progress);
    }

    private void saveBreakTimerDurationToFirestore(long timerDuration) {
        DbQuery.saveBreakTimerDuration(timerDuration, new AppCompleteListener() {
            @Override
            public void onSuccess() {
                // Handle success if needed
            }

            @Override
            public void onFailure() {
                // Handle failure if needed
            }
        });
    }

    private void startPomodoroTimer() {
        Intent intent = new Intent(BreakTimerActivity.this, PomodoroTimer.class);
        startActivity(intent);
        finish();
    }
}
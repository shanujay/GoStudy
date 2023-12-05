package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class PomodoroTimer extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 1500000;
    private ProgressBar progressBar_pomodoroTimer;
    private TextView mTextViewCountDown;
    private ImageButton mButtonStartPause;
    private ImageButton mButtonReset;
    private ImageButton musicBtnTimer, backBtnTimer;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_timer);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        progressBar_pomodoroTimer = findViewById(R.id.roundProgressBar_PomodoroTimer);
        mButtonStartPause = findViewById(R.id.startButton);
        mButtonReset = findViewById(R.id.resetButton);
        musicBtnTimer = findViewById(R.id.musicButtonTimer);
        backBtnTimer = findViewById(R.id.backButtonTimer);


        // Start Timer Button
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        // Reset Timer Button
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        // Study Music Navigate
        musicBtnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PomodoroTimer.this, StudyMusicActivity.class);
                startActivity(intent);
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);

                // Save timer duration to Firestore when the timer finishes
                saveTimerDurationToFirestore(START_TIME_IN_MILLIS - mTimeLeftInMillis);

                // Start Break Timer
                startBreakTimer();
            }
        }.start();

        mTimerRunning = true;
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateProgressBar();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateProgressBar() {
        int progress = (int) ((START_TIME_IN_MILLIS - mTimeLeftInMillis) * 100 / START_TIME_IN_MILLIS);
        progressBar_pomodoroTimer.setProgress(progress);
    }

    private void saveTimerDurationToFirestore(long timerDuration) {
        DbQuery.saveTimerDuration(timerDuration, new AppCompleteListener() {
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

    private void startBreakTimer() {
        Intent intent = new Intent(PomodoroTimer.this, BreakTimerActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.techcools.gostudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class StudyMusicActivity extends AppCompatActivity {

    ImageButton imageButton_music1, imageButton_music2, imageButton_music3, imageButton_music4, imageButton_music5;
    private boolean isResume;
    private MediaPlayer music1, music2, music3, music4, music5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_music);

        // Relaxing Music 1
        imageButton_music1 = findViewById(R.id.music_1_play);
        music1 = MediaPlayer.create(this, R.raw.music1);

        imageButton_music1.setOnClickListener(v -> {
            // Change Image of the button
            if (!isResume) {
                isResume = true;
                // Change the Icon
                imageButton_music1.setImageDrawable(getResources().getDrawable(R.drawable.next_icon));
            } else {
                isResume = false;
                // Change the Icon
                imageButton_music1.setImageDrawable(getResources().getDrawable(R.drawable.done_icon));
            }

            // Play and Stop Music
            if (music1.isPlaying()) {
                music1.pause();
            } else {
                music1.start();
            }
        });

        // Relaxing Music 2
        imageButton_music2 = findViewById(R.id.music_2_play);
        music2 = MediaPlayer.create(this, R.raw.music2);

        imageButton_music2.setOnClickListener(v -> {
            // Change Image of the button
            if (!isResume) {
                isResume = true;
                // Change the Icon
                imageButton_music2.setImageDrawable(getResources().getDrawable(R.drawable.next_icon));
            } else {
                isResume = false;
                // Change the Icon
                imageButton_music2.setImageDrawable(getResources().getDrawable(R.drawable.done_icon));
            }

            // Play and Stop Music
            if (music2.isPlaying()) {
                music2.pause();
            } else {
                music2.start();
            }
        });

        // Relaxing Music 3
        imageButton_music3 = findViewById(R.id.music_3_play);
        music3 = MediaPlayer.create(this, R.raw.music3);

        imageButton_music3.setOnClickListener(v -> {
            // Change Image of the button
            if (!isResume) {
                isResume = true;
                // Change the Icon
                imageButton_music3.setImageDrawable(getResources().getDrawable(R.drawable.next_icon));
            } else {
                isResume = false;
                // Change the Icon
                imageButton_music3.setImageDrawable(getResources().getDrawable(R.drawable.done_icon));
            }

            // Play and Stop Music
            if (music3.isPlaying()) {
                music3.pause();
            } else {
                music3.start();
            }
        });

        // Relaxing Music 4
        imageButton_music4 = findViewById(R.id.music_4_play);
        music4 = MediaPlayer.create(this, R.raw.music4);

        imageButton_music4.setOnClickListener(v -> {
            // Change Image of the button
            if (!isResume) {
                isResume = true;
                // Change the Icon
                imageButton_music4.setImageDrawable(getResources().getDrawable(R.drawable.next_icon));
            } else {
                isResume = false;
                // Change the Icon
                imageButton_music4.setImageDrawable(getResources().getDrawable(R.drawable.done_icon));
            }

            // Play and Stop Music
            if (music4.isPlaying()) {
                music4.pause();
            } else {
                music4.start();
            }
        });

        // Relaxing Music 5
        imageButton_music5 = findViewById(R.id.music_5_play);
        music5 = MediaPlayer.create(this, R.raw.music5);

        imageButton_music5.setOnClickListener(v -> {
            // Change Image of the button
            if (!isResume) {
                isResume = true;
                // Change the Icon
                imageButton_music5.setImageDrawable(getResources().getDrawable(R.drawable.next_icon));
            } else {
                isResume = false;
                // Change the Icon
                imageButton_music5.setImageDrawable(getResources().getDrawable(R.drawable.done_icon));
            }

            // Play and Stop Music
            if (music5.isPlaying()) {
                music5.pause();
            } else {
                music5.start();
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        // Stop the music when the activity is stopped
        if (music1 != null) {
            music1.pause();
        }
        if (music2 != null) {
            music2.pause();
        }
        if (music3 != null) {
            music3.pause();
        }
        if (music4 != null) {
            music4.pause();
        }
        if (music5 != null) {
            music5.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume playing the music when the activity is resumed
        if (isResume) {
            if (music1 != null) {
                music1.start();
            }
            if (music2 != null) {
                music2.start();
            }
            if (music3 != null) {
                music3.start();
            }
            if (music4 != null) {
                music4.start();
            }
            if (music5 != null) {
                music5.start();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Stop the music when the user presses the back button
        if (music1 != null) {
            music1.pause();
        }
        if (music2 != null) {
            music2.pause();
        }
        if (music3 != null) {
            music3.pause();
        }
        if (music4 != null) {
            music4.pause();
        }
        if (music5 != null) {
            music5.pause();
        }
        super.onBackPressed();
    }

}
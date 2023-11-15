package com.techcools.gostudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionnaireActivity extends AppCompatActivity {

    private static final int MAX_QUESTIONS = 20;
    private static final int MARKS_PER_ANSWER = 5;

    private RadioGroup radioGroup;
    private TextView questionText;
    private Button nextButton;
    private ImageButton closeButton;

    private List<Question> questions;
    private int currentQuestionIndex = 0;

    private int auditoryScore = 0;
    private int visualScore = 0;
    private int tactileScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        // Close Questionnaire
        closeButton = findViewById(R.id.closeQuestionnaire);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeQuestionnaire();
            }
        });

        // Initialize the list of questions
        questions = new ArrayList<>();
        // 20 Questions with Answers
        // Question 01
        questions.add(new Question("What kind of book would you like to read for fun?",
                Arrays.asList("A book with lots of words in it",
                        "A book with lots of pictures in it",
                        "A book with word searches or crossword puzzles")));
        // Question 02
        questions.add(new Question("When you are not sure how to spell a word, what are you most likely to do?",
                Arrays.asList("Spell it out loud to see if it sounds right",
                        "Write it down to see if it looks right",
                        "Trace the letters in the air (finger spelling)")));
        // Question 03
        questions.add(new Question("You're out shopping for clothes, and you're waiting in line to pay. What are you most likely to do while you are waiting?",
                Arrays.asList("Talk to the person next to you in line",
                        "Look around at other clothes on the racks",
                        "Fidget or move back and forth")));
        // Question 04
        questions.add(new Question("When you see the word \"cat,\" what do you do first?",
                Arrays.asList("Say the word \"cat\" to yourself",
                        "Picture a cat in your mind",
                        "Think about being with a cat (petting it or hearing it purr)")));
        // Question 05
        questions.add(new Question("What's the best way for you to study for a test?",
                Arrays.asList("Have someone ask you questions that you can answer out loud",
                        "Read the book or your notes and review pictures or charts",
                        "Make up index cards that you can review")));
        // Question 06
        questions.add(new Question("What's the best way for you to learn about how something works (like a computer or a video game)?",
                Arrays.asList("Read about it or listen to someone explain it",
                        "Get someone to show you",
                        "Figure it out on your own")));
        // Question 07
        questions.add(new Question(" If you went to a school dance, what would you be most likely to remember the next day?",
                Arrays.asList("The music that was played",
                        "The faces of the people who were there",
                        "The dance moves you did and the food you ate")));
        // Question 08
        questions.add(new Question("What do you find most distracting when you are trying to study?",
                Arrays.asList("Loud noises",
                        "People walking past you",
                        "An uncomfortable chair")));
        // Question 09
        questions.add(new Question("When you are angry, what are you most likely to do?",
                Arrays.asList("Yell and scream",
                        "Put on your \"mad\" face",
                        "Slam doors")));
        // Question 10
        questions.add(new Question(" When you are happy, what are you most likely to do?",
                Arrays.asList("Talk up a storm",
                        "Smile from ear to ear",
                        "Act really hyper")));
        // Question 11
        questions.add(new Question("When in a new place, how do you find your way around?",
                Arrays.asList(" Ask someone for directions",
                        "Look for a map or directory that shows you where everything is",
                        " Just start walking around until you find what you're looking for")));
        // Question 12
        questions.add(new Question("Of these three classes, which is your favorite?",
                Arrays.asList("Music class",
                        "Art Class",
                        "Gym Class")));
        // Question 13
        questions.add(new Question("When you hear a song on the radio, what are you most likely to do?",
                Arrays.asList("Sing or hum along with the music",
                        "Picture the video that goes along with it",
                        "Start dancing or tapping your foot")));
        // Question 14
        questions.add(new Question("What do you find most distracting when in class?",
                Arrays.asList("Noises from the hallway or outside the building (like traffic or someone cutting the grass)",
                        "Lights that are too bright or too dim",
                        "The temperature being too hot or too cold")));
        // Question 15
        questions.add(new Question("What do you like to do to relax?",
                Arrays.asList("Listen to music",
                        "Read",
                        "Exercise (walk, run, play sports, etc.)")));
        // Question 16
        questions.add(new Question("What is the best way for you to remember a friend's phone number?",
                Arrays.asList("Say it out loud over and over and over",
                        "Picture the numbers on the phone as you would dial them",
                        "Write it down or store it in your phone contact list")));
        // Question 17
        questions.add(new Question("If you won a game, which of these three prizes would you choose?",
                Arrays.asList("A music CD or mp3 download",
                        "A poster for the wall",
                        "A game of some kind (or a football or soccer ball, etc.)")));
        // Question 18
        questions.add(new Question("Which would you rather go to with a group of friends?",
                Arrays.asList("A concert",
                        "A movie",
                        "An amusement park")));
        // Question 19
        questions.add(new Question("What are you most likely to remember about new people you meet?",
                Arrays.asList("Their name but not their face",
                        "Their face but not their name",
                        "What you talked about with them")));
        // Question 20
        questions.add(new Question("When you give someone directions to your house, what are you most likely to tell them?",
                Arrays.asList("The names of the roads or streets they will be on",
                        "A description of building and landmarks they will pass on the way",
                        "\"Follow me—it will be easier if I just show you how to get there.\"")));


        radioGroup = findViewById(R.id.radioGroup);
        questionText = findViewById(R.id.questionText);
        nextButton = findViewById(R.id.nextButton);

        showQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processAnswer();
                if (currentQuestionIndex < questions.size()) {
                    showQuestion();
                } else {
                    showResults();
                }
            }
        });

    }

    // Show Question
    private void showQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionText.setText((currentQuestionIndex + 1) + ". " + currentQuestion.getPrompt());
        radioGroup.clearCheck();
        currentQuestionIndex++;

        // Set up the radio button labels based on the current question options
        RadioButton radioButtonA = findViewById(R.id.radioButtonA);
        RadioButton radioButtonB = findViewById(R.id.radioButtonB);
        RadioButton radioButtonC = findViewById(R.id.radioButtonC);

        List<String> options = currentQuestion.getOptions();
        radioButtonA.setText(options.get(0));
        radioButtonB.setText(options.get(1));
        radioButtonC.setText(options.get(2));
    }

    // Process Answer
    private void processAnswer() {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId == R.id.radioButtonA) {
            auditoryScore += MARKS_PER_ANSWER;
        } else if (selectedRadioButtonId == R.id.radioButtonB) {
            visualScore += MARKS_PER_ANSWER;
        } else if (selectedRadioButtonId == R.id.radioButtonC) {
            tactileScore += MARKS_PER_ANSWER;
        }
    }

    // Show Result
    private void showResults() {
        // Calculate the learning style based on the highest score
        String learningStyle;
        if (auditoryScore >= visualScore && auditoryScore >= tactileScore) {
            learningStyle = "Auditory";
        } else if (visualScore >= auditoryScore && visualScore >= tactileScore) {
            learningStyle = "Visual";
        } else {
            learningStyle = "Tactile";
        }

        // Display the results
        setContentView(R.layout.activity_results);

        TextView resultText = findViewById(R.id.resultText);
        resultText.setText(learningStyle);

        TextView auditoryScoreText = findViewById(R.id.auditoryScore);
        auditoryScoreText.setText("Auditory Score: " + auditoryScore + "%");

        TextView visualScoreText = findViewById(R.id.visualScore);
        visualScoreText.setText("Visual Score: " + visualScore + "%");

        TextView tactileScoreText = findViewById(R.id.tactileScore);
        tactileScoreText.setText("Tactile Score: " + tactileScore + "%");

        // Add button click listener
        Button closeButton = findViewById(R.id.closeResults);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the results activity and go back to the main activity
                navigateToHome();
            }
        });

        Button showMethods = findViewById(R.id.showStudyMethods);

        // Show the Learning Style Description
        if (learningStyle.equals("Auditory")) {
            // Show Auditory Description
            TextView auditoryDescription = findViewById(R.id.styleDescription);
            auditoryDescription.setText(getString(R.string.AuditoryDescription));

            // Show Auditory Study Methods
            showMethods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AuditoryStudyMethods();
                }
            });
        } else if (learningStyle.equals("Visual")) {
            // Show Visual Description
            TextView visualDescription = findViewById(R.id.styleDescription);
            visualDescription.setText(getString(R.string.VisualDescription));

            // Show Visual Study Methods
            showMethods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    VisualStudyMethods();
                }
            });
        } else {
            // Show Tactile Description
            TextView tactileDescription = findViewById(R.id.styleDescription);
            tactileDescription.setText(getString(R.string.TactileDescription));

            // Show Visual Study Methods
            showMethods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TactileStudyMethods();
                }
            });
        }
    }

    // Navigate to Home
    private void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the current activity
    }

    // Auditory Study Method
    private void AuditoryStudyMethods() {
        // Display the Auditory Study Methods
        setContentView(R.layout.activity_auditory_methods);
    }

    // Visual Study Method
    private void VisualStudyMethods() {
        // Display the Visual Study Methods
        setContentView(R.layout.activity_visual_methods);
    }

    // Tactile Study Method
    private void TactileStudyMethods() {
        // Display the Tactile Study Methods
        setContentView(R.layout.activity_tactile_methods);
    }

    // Close Questionnaire
    private void closeQuestionnaire() {
        Intent intent = new Intent(this, StudyMethodActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
package com.techcools.gostudy;

import java.util.List;

public class Question {

    private String prompt;
    private List<String> options;

    public Question(String prompt, List<String> options) {
        this.prompt = prompt;
        this.options = options;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<String> getOptions() {
        return options;
    }

}

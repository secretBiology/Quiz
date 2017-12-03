package com.secretbiology.minimalquiz.common;

import com.secretbiology.minimalquiz.activities.lobby.GameSettings;
import com.secretbiology.minimalquiz.background.OpenDBCategory;

import java.util.List;

public class Quiz {

    private List<Question> questionList;
    private GameSettings settings;
    private int currentQuestion = 0;
    private int score = 0;

    public Quiz() {
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }

    public Question getCurrentQuestion() {
        if (currentQuestion < questionList.size()) {
            Question q = questionList.get(currentQuestion);
            currentQuestion++;
            return q;
        } else {
            return null;
        }
    }

    public void addScore() {
        score += 5;
    }

    public int totalScore() {
        return questionList.size() * 5;
    }

    public int getScore() {
        return score;
    }

    public String category() {
        return OpenDBCategory.getCategoryByID(settings.getCategoryType()).toString();
    }

    public int getQuestionIndex() {
        return currentQuestion;
    }

}

package com.secretbiology.quiz;

/**
 * Created by Rohit Suratekar on 29-11-17 for Quiz.
 * All code is released under MIT License.
 */

public class Answer {
    private String text;
    private boolean isCorrect = false;
    private boolean isSelected = false;

    public Answer() {
    }

    public Answer(String text) {
        this.text = text;
    }

    public Answer(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

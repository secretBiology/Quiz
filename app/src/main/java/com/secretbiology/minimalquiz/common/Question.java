package com.secretbiology.minimalquiz.common;

import java.util.List;


public class Question {

    private String statement;
    private List<Answer> answerList;


    public Question() {
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

}

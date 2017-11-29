package com.secretbiology.quiz.background;

import java.util.List;

/**
 * Created by Rohit Suratekar on 29-11-17 for Quiz.
 * All code is released under MIT License.
 */

public class ResponseData {

    private int response_code;
    private List<OpenDBModel> results;

    public ResponseData() {
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public List<OpenDBModel> getResults() {
        return results;
    }

    public void setResults(List<OpenDBModel> results) {
        this.results = results;
    }
}

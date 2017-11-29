package com.secretbiology.quiz;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.secretbiology.helpers.general.Log;
import com.secretbiology.quiz.background.OpenDBCategory;
import com.secretbiology.quiz.background.ResponseData;
import com.secretbiology.quiz.background.RetrofitCalls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohit Suratekar on 29-11-17 for Quiz.
 * All code is released under MIT License.
 */

public class SinglePlayerViewModel extends ViewModel {

    private MutableLiveData<QuestionObject> questionObject = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<QuestionObject> getQuestionObject() {
        return questionObject;
    }

    public void getQuestion(OpenDBCategory category) {
        new RetrofitCalls().getQuestion(1, category).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData data = response.body();
                    if (data != null) {
                        if (data.getResponse_code() == 0 && data.getResults().size() > 0) {
                            QuestionObject obj = new QuestionObject();
                            obj.setQuestion(data.getResults().get(0).getQuestion());
                            List<Answer> answerList = new ArrayList<>();
                            for (String an : data.getResults().get(0).getIncorrect_answers()) {
                                answerList.add(new Answer(an));
                            }
                            answerList.add(new Answer(data.getResults().get(0).getCorrect_answer(), true));
                            Collections.shuffle(answerList);
                            obj.setAnswerList(answerList);
                            questionObject.postValue(obj);
                        } else {
                            error.postValue("Something went wrong");
                        }

                    } else {
                        error.postValue("No question found. Try another category.");
                    }


                } else {
                    error.postValue(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseData> call, @NonNull Throwable t) {

            }
        });
    }
}

package com.secretbiology.minimalquiz.activities.lobby;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.secretbiology.minimalquiz.background.OpenDBCategory;
import com.secretbiology.minimalquiz.background.OpenDBModel;
import com.secretbiology.minimalquiz.background.ResponseData;
import com.secretbiology.minimalquiz.background.RetrofitCalls;
import com.secretbiology.minimalquiz.common.Answer;
import com.secretbiology.minimalquiz.common.Database;
import com.secretbiology.minimalquiz.common.Question;
import com.secretbiology.minimalquiz.common.Quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dexter on 12/3/2017.
 */

public class NewGameViewModel extends ViewModel {

    private MutableLiveData<Quiz> question = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<Quiz> getQuestion() {
        return question;
    }

    public void retrieveQuestion(GameSettings settings) {
        if (settings.getDatabaseType() == Database.OPENDB.getType()) {
            getFromOpenDB(settings);
        }
    }

    private void getFromOpenDB(final GameSettings settings) {
        new RetrofitCalls().getQuestion(settings.getNoOfQuestions(), OpenDBCategory.getCategoryByID(settings.getCategoryType())).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                if (response.isSuccessful()) {

                    ResponseData data = response.body();
                    if (data != null) {
                        if (data.getResponse_code() == 0 && data.getResults().size() > 0) {
                            Quiz quiz = new Quiz();
                            List<Question> questionList = new ArrayList<>();

                            for (OpenDBModel model : data.getResults()) {

                                Question obj = new Question();
                                obj.setStatement(model.getQuestion());
                                List<Answer> answerList = new ArrayList<>();
                                for (String an : model.getIncorrect_answers()) {
                                    answerList.add(new Answer(an));
                                }
                                answerList.add(new Answer(model.getCorrect_answer(), true));
                                Collections.shuffle(answerList);
                                obj.setAnswerList(answerList);
                                questionList.add(obj);
                            }
                            quiz.setQuestionList(questionList);
                            quiz.setSettings(settings);
                            question.postValue(quiz);
                        } else {
                            error.postValue("Something went wrong");
                        }
                    }


                } else {
                    error.postValue(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseData> call, @NonNull Throwable t) {
                error.postValue(t.getLocalizedMessage());
            }
        });
    }
}

package com.secretbiology.minimalquiz.activities.game;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.secretbiology.helpers.general.General;
import com.secretbiology.minimalquiz.Home;
import com.secretbiology.minimalquiz.R;
import com.secretbiology.minimalquiz.activities.lobby.NewGame;
import com.secretbiology.minimalquiz.common.Answer;
import com.secretbiology.minimalquiz.common.AnswerAdapter;
import com.secretbiology.minimalquiz.common.Face;
import com.secretbiology.minimalquiz.common.Question;
import com.secretbiology.minimalquiz.common.Quiz;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SinglePlayer extends AppCompatActivity implements AnswerAdapter.OnAnswerClick {

    @BindView(R.id.sp_game_progress)
    ProgressBar progress;
    @BindView(R.id.sp_progress_text)
    TextView progressText;
    @BindView(R.id.sp_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.sp_time)
    TextView timer;
    @BindView(R.id.sp_question)
    TextView question;
    @BindView(R.id.sp_score)
    TextView score;
    @BindView(R.id.sp_category)
    TextView category;
    @BindView(R.id.sp_dp)
    ImageView dp;

    private Quiz quiz;

    private AnswerAdapter adapter;
    private List<Answer> answerList = new ArrayList<>();
    private SinglePlayerViewModel viewModel;
    private Question currentQuestion;
    private boolean answerLock = false;
    private int nextQuestionIn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(SinglePlayerViewModel.class);

        String s = getIntent().getStringExtra(NewGame.QUESTION_DATA);
        if (s == null) {
            General.makeLongToast(getApplicationContext(), "Unable to start quiz!");
            finish();
        } else {
            quiz = new Gson().fromJson(s, Quiz.class);
            currentQuestion = quiz.getCurrentQuestion();
            progress.setMax(quiz.getQuestionList().size());
        }

        adapter = new AnswerAdapter(answerList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subscribe();
        setUpInfo();
        updateUI();
    }

    private void updateUI() {
        if (currentQuestion != null) {
            progress.setProgress(quiz.getQuestionIndex());
            int per = quiz.getQuestionIndex() * 100 / quiz.getQuestionList().size();
            progressText.setText(getString(R.string.game_progress, per));
            answerList.clear();
            answerList.addAll(currentQuestion.getAnswerList());
            question.setText((Html.fromHtml(currentQuestion.getStatement())));
            score.setText(String.valueOf(quiz.getScore()));
            adapter.notifyDataSetChanged();
        } else {
            viewModel.stopAll();
            new AlertDialog.Builder(SinglePlayer.this)
                    .setTitle(getString(R.string.game_over))
                    .setMessage(getString(R.string.game_score_single,
                            quiz.getScore(), quiz.totalScore(), quiz.category()))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cancelGame();
                        }
                    }).show();

        }
    }

    private void setUpInfo() {
        category.setText(quiz.category());
        dp.setImageResource(Face.randomFace().getIcon());
    }

    private void subscribe() {
        viewModel.getTimer().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null) {
                    int timeLeft = quiz.getSettings().getTimePerQuestion() - integer;
                    timer.setText(getString(R.string.time_left, timeLeft));
                    if (timeLeft == 0) {
                        answerLock = true;
                        for (Answer a : answerList) {
                            a.setShowAnswer(true);
                        }
                        adapter.notifyDataSetChanged();
                        viewModel.showAnswerDelay();
                    }
                }
            }
        });
        viewModel.getChangeQuestion().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        next();
                    } else {
                        timer.setText(getString(R.string.next_question_in, SinglePlayerViewModel.delayTime - nextQuestionIn));
                        nextQuestionIn++;
                    }
                }
            }
        });
        viewModel.startTimer();
    }

    @Override
    public void onAnswerClicked(int position) {
        if (!answerLock) {
            Answer ans = answerList.get(position);
            ans.setSelected(true);
            for (Answer a : answerList) {
                a.setShowAnswer(true);
            }
            if (ans.isCorrect()) {
                quiz.addScore();
                score.setText(String.valueOf(quiz.getScore()));
            } else {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null) {
                    v.vibrate(500);
                }
            }
            adapter.notifyDataSetChanged();
            viewModel.showAnswerDelay();
            answerLock = true;

        }

    }

    @OnClick(R.id.sp_next_btn)
    public void next() {
        viewModel.stopWaiting();
        answerLock = false;
        currentQuestion = quiz.getCurrentQuestion();
        viewModel.startTimer();
        nextQuestionIn = 0;
        updateUI();
    }

    @OnClick(R.id.sp_cancel_btn)
    public void cancelGame() {
        viewModel.stopAll();
        startActivity(new Intent(this, Home.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}

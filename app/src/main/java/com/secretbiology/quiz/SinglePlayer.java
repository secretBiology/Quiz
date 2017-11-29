package com.secretbiology.quiz;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.secretbiology.helpers.general.General;
import com.secretbiology.quiz.background.OpenDBCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.cancel;
import static android.R.string.ok;

public class SinglePlayer extends AppCompatActivity implements AnswersAdapter.OnAnswerClick {

    @BindView(R.id.sg_question)
    TextView question;

    @BindView(R.id.sg_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.sg_category)
    TextView categoryText;

    @BindView(R.id.sg_recycler)
    RecyclerView recyclerView;

    private AnswersAdapter adapter;
    private List<Answer> answerList;
    private SinglePlayerViewModel viewModel;
    private QuestionObject currentQuestion;
    private OpenDBCategory currentCategory;
    private boolean isBackgroundBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(SinglePlayerViewModel.class);
        subscribe();
        currentQuestion = new QuestionObject();
        currentQuestion.setQuestion("");
        answerList = new ArrayList<>();
        adapter = new AnswersAdapter(answerList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        currentCategory = OpenDBCategory.randomCategory();
        categoryText.setText(currentCategory.toString());
        toggleScreen();
        viewModel.getQuestion(currentCategory);


    }

    private void subscribe() {
        viewModel.getQuestionObject().observe(this, new Observer<QuestionObject>() {
            @Override
            public void onChanged(@Nullable QuestionObject questionObject) {
                if (questionObject != null) {
                    toggleScreen();
                    currentQuestion = questionObject;
                    updateQuestion();
                }
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    toggleScreen();
                    new AlertDialog.Builder(SinglePlayer.this)
                            .setTitle("Oops...")
                            .setMessage(s)
                            .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });
    }

    @OnClick(R.id.sg_get_question)
    public void getQuestion() {
        toggleScreen();
        viewModel.getQuestion(currentCategory);
    }

    private void updateQuestion() {
        answerList.clear();
        answerList.addAll(currentQuestion.getAnswerList());
        question.setText(Html.fromHtml(currentQuestion.getQuestion()));
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.sg_category, R.id.sg_footnote})
    public void selectCategory() {
        showCategories();
    }

    private void showCategories() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Select category");

        String[] types = new String[OpenDBCategory.values().length];
        final List<OpenDBCategory> categories = new ArrayList<>();
        int i = 0;
        for (OpenDBCategory s : OpenDBCategory.values()) {
            categories.add(s);
            types[i] = s.toString();
            i++;
        }
        b.setPositiveButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentCategory = categories.get(which);
                toggleScreen();
                viewModel.getQuestion(currentCategory);
                categoryText.setText(currentCategory.toString());
            }

        });
        b.show();
    }


    @Override
    public void answer(int position) {
        if (answerList.get(position).isCorrect()) {
            General.makeLongToast(getApplicationContext(), "Correct !!");
        }
        answerList.get(position).setSelected(true);
        adapter.notifyDataSetChanged();
    }

    private void toggleScreen() {
        if (isBackgroundBusy) {
            progressBar.setVisibility(View.INVISIBLE);
            isBackgroundBusy = false;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            question.setText("Retrieving...");
            answerList.clear();
            adapter.notifyDataSetChanged();
            isBackgroundBusy = true;
        }
    }
}

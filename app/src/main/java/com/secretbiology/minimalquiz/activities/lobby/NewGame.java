package com.secretbiology.minimalquiz.activities.lobby;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.secretbiology.helpers.general.General;
import com.secretbiology.minimalquiz.R;
import com.secretbiology.minimalquiz.activities.JoinGame;
import com.secretbiology.minimalquiz.activities.game.MultiPlayer;
import com.secretbiology.minimalquiz.activities.game.SinglePlayer;
import com.secretbiology.minimalquiz.background.OpenDBCategory;
import com.secretbiology.minimalquiz.common.Database;
import com.secretbiology.minimalquiz.common.Quiz;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.cancel;

public class NewGame extends AppCompatActivity {

    public static final String QUESTION_DATA = "question_data";

    @BindView(R.id.ng_dismiss_btn)
    Button dismissWarning;
    @BindView(R.id.ng_seekbar)
    SeekBar seekBar;
    @BindView(R.id.ng_warning)
    LinearLayout warning;
    @BindView(R.id.ng_single_player)
    Button singlePlayer;
    @BindView(R.id.ng_multi_player)
    Button multiPlayer;
    @BindViews({R.id.ng_time1, R.id.ng_time2, R.id.ng_time3})
    List<Button> timeButtons;
    @BindView(R.id.ng_no_of_questions)
    TextView questionText;
    @BindView(R.id.ng_time_per_question)
    TextView timeText;
    @BindView(R.id.ng_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ng_start)
    Button startBtn;
    @BindView(R.id.ng_cat)
    TextView categoryText;
    @BindView(R.id.ng_database)
    TextView databaseText;


    private GameSettings gameSettings;
    private boolean isOnProxy = false;
    private boolean isBackgroundBusy = true;
    private NewGameViewModel newGameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        setTitle(R.string.new_game);
        ButterKnife.bind(this);
        newGameViewModel = ViewModelProviders.of(this).get(NewGameViewModel.class);

        gameSettings = new GameSettings();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                gameSettings.setNoOfQuestions(i);
                updateUI();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        isOnProxy = General.isOnProxy();
        if (isOnProxy) {
            warning.setVisibility(View.VISIBLE);
            multiPlayer.setAlpha(0.5f);
        } else {
            warning.setVisibility(View.GONE);
        }

        updateUI();
        toggleScreen();
        subscribe();
    }

    private void subscribe() {
        newGameViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    toggleScreen();
                    new AlertDialog.Builder(NewGame.this)
                            .setTitle(getString(R.string.oops))
                            .setMessage(s)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();
                }
            }
        });

        newGameViewModel.getQuestion().observe(this, new Observer<Quiz>() {
            @Override
            public void onChanged(@Nullable Quiz quiz) {
                if (quiz != null) {
                    toggleScreen();
                    if (gameSettings.isSinglePlayer()) {
                        Intent intent = new Intent(NewGame.this, SinglePlayer.class);
                        intent.putExtra(QUESTION_DATA, new Gson().toJson(quiz));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(NewGame.this, JoinGame.class);
                        intent.putExtra(QUESTION_DATA, new Gson().toJson(quiz));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private void updateUI() {
        seekBar.setProgress(gameSettings.getNoOfQuestions());
        fadeTime();
        questionText.setText(getString(R.string.no_of_questions, gameSettings.getNoOfQuestions()));
        timeText.setText(getString(R.string.time_per_question, gameSettings.getNoOfQuestions() * gameSettings.getTimePerQuestion()));
        singlePlayer.setAlpha(0.5f);
        multiPlayer.setAlpha(0.5f);
        if (gameSettings.isSinglePlayer()) {
            singlePlayer.setAlpha(1f);
        } else {
            multiPlayer.setAlpha(1f);
        }
        categoryText.setText(getCategoryText());
        databaseText.setText(getString(R.string.select_category, Database.getDatabase(gameSettings.getDatabaseType()).toString()));

    }

    @OnClick({R.id.ng_time1, R.id.ng_time2, R.id.ng_time3})
    public void setTime(Button button) {
        if (!isBackgroundBusy) {
            switch (timeButtons.indexOf(button)) {
                case 0:
                    gameSettings.setTimePerQuestion(10);
                    break;
                case 1:
                    gameSettings.setTimePerQuestion(15);
                    break;
                case 2:
                    gameSettings.setTimePerQuestion(20);
                    break;
            }
            updateUI();
        }
    }

    @OnClick(R.id.ng_single_player)
    public void singlePlayer() {
        if (!isBackgroundBusy) {
            gameSettings.setSinglePlayer(true);
            updateUI();
        }
    }

    @OnClick(R.id.ng_multi_player)
    public void multiPlayer() {
        if (!isBackgroundBusy) {
            if (isOnProxy) {
                warning.setVisibility(View.VISIBLE);
            } else {
                gameSettings.setSinglePlayer(false);
                updateUI();
            }
        }
    }


    private void fadeTime() {
        for (Button b : timeButtons) {
            b.setAlpha(0.5f);
        }
        switch (gameSettings.getTimePerQuestion()) {
            case 10:
                timeButtons.get(0).setAlpha(1f);
                break;
            case 20:
                timeButtons.get(2).setAlpha(1f);
                break;
            default:
                timeButtons.get(1).setAlpha(1f);
                gameSettings.setTimePerQuestion(15);
        }
    }

    @OnClick(R.id.ng_dismiss_btn)
    public void dismissWarning() {
        warning.setVisibility(View.GONE);
    }

    @OnClick(R.id.ng_start)
    public void startGame() {
        toggleScreen();
        newGameViewModel.retrieveQuestion(gameSettings);
    }

    private void toggleScreen() {

        if (isBackgroundBusy) {
            progressBar.setVisibility(View.INVISIBLE);
            startBtn.setEnabled(true);
            startBtn.setAlpha(1f);
            seekBar.setEnabled(true);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            warning.setVisibility(View.GONE);
            startBtn.setEnabled(false);
            startBtn.setAlpha(0.5f);
            seekBar.setEnabled(false);
        }

        isBackgroundBusy = !isBackgroundBusy;
    }

    @OnClick(R.id.ng_cat)
    public void showCategories() {
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
                gameSettings.setCategoryType(categories.get(which).getCategory());
                updateUI();
            }

        });
        b.show();
    }

    private String getCategoryText() {
        if (gameSettings.getDatabaseType() == Database.OPENDB.getType()) {
            return OpenDBCategory.getCategoryByID(gameSettings.getCategoryType()).toString();
        }
        return "N/A";
    }


}

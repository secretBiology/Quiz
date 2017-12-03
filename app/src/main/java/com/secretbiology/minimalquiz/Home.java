package com.secretbiology.minimalquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.secretbiology.helpers.general.General;
import com.secretbiology.minimalquiz.activities.JoinGame;
import com.secretbiology.minimalquiz.activities.lobby.NewGame;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity {

    @BindView(R.id.hm_warning)
    LinearLayout warning;
    @BindView(R.id.hm_join_btn)
    Button joinBtn;

    private boolean isOnProxy = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);

        isOnProxy = General.isOnProxy();
        if (isOnProxy) {
            warning.setVisibility(View.VISIBLE);
            joinBtn.setAlpha(0.5f);
        } else {
            warning.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.hm_new_btn)
    public void startNewGame() {
        startActivity(new Intent(this, NewGame.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick(R.id.hm_join_btn)
    public void joinExistingGame() {
        if (!isOnProxy) {
            startActivity(new Intent(this, JoinGame.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            warning.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.hm_dismiss_btn)
    public void dismissWarning() {
        warning.setVisibility(View.GONE);
    }

}

package com.secretbiology.minimalquiz.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.secretbiology.minimalquiz.R;

public class JoinGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game);
        setTitle(R.string.join_game);
    }
}

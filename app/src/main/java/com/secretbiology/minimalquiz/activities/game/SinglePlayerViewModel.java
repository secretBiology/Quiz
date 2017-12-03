package com.secretbiology.minimalquiz.activities.game;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;

public class SinglePlayerViewModel extends ViewModel {

    private MutableLiveData<Integer> timer = new MutableLiveData<>();
    private MutableLiveData<Boolean> changeQuestion = new MutableLiveData<>();
    private Handler mHandler = new Handler();
    private Handler mDelay = new Handler();
    private int delayTimer = 0;
    public static final int delayTime = 3;


    public MutableLiveData<Integer> getTimer() {
        return timer;
    }

    public MutableLiveData<Boolean> getChangeQuestion() {
        return changeQuestion;
    }

    private Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (timer.getValue() != null) {
                    timer.postValue(timer.getValue() + 1);
                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, 1000);
            }
        }
    };

    private Runnable mDelayChecker = new Runnable() {
        @Override
        public void run() {
            try {
                delayTimer++;
            } finally {
                if (delayTime == delayTimer) {
                    delayTimer = 0;
                    changeQuestion.postValue(true);
                } else {
                    changeQuestion.postValue(false);
                    mDelay.postDelayed(mDelayChecker, 1000);
                }
            }
        }
    };

    public void startTimer() {
        timer.postValue(0);
        mHandler.postDelayed(mStatusChecker, 1000);
    }

    private void stopTimer() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    public void showAnswerDelay() {
        stopTimer();
        delayTimer = 0;
        mDelay.postDelayed(mDelayChecker, 1000);
    }

    public void stopWaiting() {
        stopTimer();
        delayTimer = 0;
        mDelay.removeCallbacks(mDelayChecker);
    }

    public void stopAll() {
        mHandler.removeCallbacks(mStatusChecker);
        mDelay.removeCallbacks(mDelayChecker);
    }
}

package com.techcools.gostudy;

public interface TimerDataCompleteListener {
    void onSuccess(long totalDuration);
    void onFailure();
}


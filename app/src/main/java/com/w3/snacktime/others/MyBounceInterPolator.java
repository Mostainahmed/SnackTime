package com.w3.snacktime.others;

/**
 * Created by W3E16 on 24-Apr-18.
 */

public class MyBounceInterPolator implements android.view.animation.Interpolator{

    private double mAmplitude = 1;
    private double mFrequency = 10;

    public MyBounceInterPolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}


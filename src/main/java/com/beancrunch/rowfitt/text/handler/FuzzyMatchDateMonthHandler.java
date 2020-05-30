package com.beancrunch.rowfitt.text.handler;

import com.beancrunch.rowfitt.domain.Workout;

public class FuzzyMatchDateMonthHandler extends WorkoutTextHandler {

    @Override
    public String handleTextInStep(String text, Workout workout) {
        return text;
    }
}

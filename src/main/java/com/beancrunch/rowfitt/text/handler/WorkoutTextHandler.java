package com.beancrunch.rowfitt.text.handler;

import com.beancrunch.rowfitt.domain.Workout;

public abstract class WorkoutTextHandler {

    private WorkoutTextHandler successor;

    public String handleTextInChain(String text, Workout workout) {
        String handledText = handleTextInStep(text, workout);
        if (hasSuccessor()) {
            handledText = successor.handleTextInChain(handledText, workout);
        }
        return handledText;
    }

    public abstract String handleTextInStep(String text, Workout workout);

    public WorkoutTextHandler setSuccessor(WorkoutTextHandler successor) {
        this.successor = successor;
        return successor;
    }

    public boolean hasSuccessor() {
        return successor != null;
    }
}

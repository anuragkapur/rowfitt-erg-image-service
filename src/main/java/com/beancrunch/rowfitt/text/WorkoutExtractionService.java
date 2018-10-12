package com.beancrunch.rowfitt.text;

import com.beancrunch.rowfitt.domain.Workout;

public interface WorkoutExtractionService {

    Workout getWorkout(String workoutImageText);
}

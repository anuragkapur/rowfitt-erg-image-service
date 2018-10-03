package com.beancrunch.rowfitt;

import com.beancrunch.rowfitt.domain.Workout;
import com.beancrunch.rowfitt.domain.WorkoutType;

import java.io.IOException;

public interface ErgImageService {

    Workout getWorkoutFromImage(byte[] imageBinary) throws IOException;
}

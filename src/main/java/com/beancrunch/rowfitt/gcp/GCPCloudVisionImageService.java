package com.beancrunch.rowfitt.gcp;

import com.beancrunch.rowfitt.ErgImageService;
import com.beancrunch.rowfitt.domain.Workout;
import com.beancrunch.rowfitt.text.WorkoutExtractionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class GCPCloudVisionImageService implements ErgImageService {

    private GCPCloudVisionClient gcpCloudVisionClient;

    private WorkoutExtractionService workoutExtractionService;

    public Workout getWorkoutFromImage(byte[] imageBinary) throws IOException {
        return this.gcpCloudVisionClient
                .getTextFromImage(imageBinary)
                .map(workoutExtractionService::getWorkout)
                .orElse(new Workout());
    }
}

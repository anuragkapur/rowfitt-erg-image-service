package com.beancrunch.rowfitt;

import com.beancrunch.rowfitt.domain.Workout;
import com.beancrunch.rowfitt.domain.WorkoutTextSummary;
import com.beancrunch.rowfitt.domain.WorkoutType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@NoArgsConstructor
@AllArgsConstructor
public class GCPCloudVisionImageService implements ErgImageService {

    private GCPCloudVisionClient gcpCloudVisionClient;

    public Workout getWorkoutFromImage(byte[] imageBinary, WorkoutType workoutType) throws IOException {
        return this.gcpCloudVisionClient
                .getTextFromImage(imageBinary)
                .map(this::getWorkoutTextSummary)
                .map(this::getWorkout)
                .orElse(new Workout());
    }

    private WorkoutTextSummary getWorkoutTextSummary(String workoutImageText) {
        WorkoutTextSummary workoutTextSummary = new WorkoutTextSummary();
        String[] lines = workoutImageText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("time")) {
                if (i+1 < lines.length)
                    workoutTextSummary.setWorkoutMetricsText(lines[i+1]);
                if (i > 0) {
                    workoutTextSummary.setDate(lines[i-1]);
                }
            }
        }
        return workoutTextSummary;
    }

    private Workout getWorkout(WorkoutTextSummary workoutTextSummary) {
        Workout workout = new Workout();

        DateFormat format = new SimpleDateFormat("MMM dd yyyy");
        try {
            workout.setDate(format.parse(workoutTextSummary.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return workout;
    }
}

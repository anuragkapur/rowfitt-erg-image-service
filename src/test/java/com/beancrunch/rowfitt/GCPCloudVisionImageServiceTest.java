package com.beancrunch.rowfitt;

import com.beancrunch.rowfitt.domain.Workout;
import com.beancrunch.rowfitt.domain.WorkoutType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GCPCloudVisionImageServiceTest {

    @Mock
    GCPCloudVisionClient gcpCloudVisionClient;

    private static String singleDistanceWorkoutImageText1;

    @BeforeEach
    void setUp() {
        singleDistanceWorkoutImageText1 = "view Detail\n" +
                "4000m\n" +
                "Mar 13 2017\n" +
                "time meter 1500m-m\n" +
                "16:10.8 4000 2013 19\n" +
                "3:143 800 21014 19\n" +
                "314.4 1600 2101,5 19\n" +
                "3:15 2400 2102.3 19\n" +
                "3:15.3 3200 2:02.0 20\n" +
                "3:11.1 4000 1:59,4 21\n" +
                "CHANGE\n" +
                "UNITS\n" +
                "CHANGE\n" +
                "DISPLAY\n" +
                "MEN\n" +
                "ВАС";
    }

    @Test
    void getWorkoutFromSingleDistanceImage1() throws IOException {
        when(gcpCloudVisionClient.getTextFromImage(any())).thenReturn(Optional.of(singleDistanceWorkoutImageText1));
        ErgImageService ergImageService = new GCPCloudVisionImageService(gcpCloudVisionClient);
        Workout workout = ergImageService.getWorkoutFromImage(new byte[0], WorkoutType.SingleDistance);
        Assertions.assertNotNull(workout, "workout extracted from image must not be null");
        Assertions.assertEquals("", workout.toString(), "extracted workout attributes must be as expected");
    }
}
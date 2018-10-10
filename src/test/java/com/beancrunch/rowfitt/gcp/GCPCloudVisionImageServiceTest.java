package com.beancrunch.rowfitt.gcp;

import com.beancrunch.rowfitt.domain.Workout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.file.Paths.get;

@ExtendWith(MockitoExtension.class)
class GCPCloudVisionImageServiceTest {

    @Mock
    GCPCloudVisionClient gcpCloudVisionClient;

    private GCPCloudVisionImageService gcpCloudVisionImageService = new GCPCloudVisionImageService();

    @Test
    void testWorkoutTextSummaryExtraction() throws Exception {
        Optional<URL> ergTextsDirUrl = Optional.ofNullable(getClass().getClassLoader().getResource("temp-out"));
        ergTextsDirUrl
                .map(url -> getChildPathStream(get(url.getPath())))
                .orElseThrow(() -> new Exception("No files found to process"))
                .forEach(this::testWorkoutExtractionForFile);
    }

    private static Stream<Path> getChildPathStream(Path path) {
        try {
            return Files.list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    private void testWorkoutExtractionForFile(Path path) {
        try {
            System.out.println(path.getFileName());
            String lines = new String(Files.readAllBytes(path));
            Workout workout = gcpCloudVisionImageService.getWorkout(lines);
            System.out.println(workout.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.beancrunch.rowfitt;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class GCPCloudVisionImageServiceTest {

    @Test
    void getWorkoutFromImage() throws IOException {
        String fileName = "erg-single-distance4.jpg";
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).getFile());
        byte[] data = Files.readAllBytes(path);

        ErgImageService ergImageService = new GCPCloudVisionImageService();
        ergImageService.getWorkoutFromImage(data);
    }
}
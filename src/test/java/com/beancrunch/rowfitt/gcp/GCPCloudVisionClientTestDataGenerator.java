package com.beancrunch.rowfitt.gcp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GCPCloudVisionClientTestDataGenerator {

    private static final String ergTextDir = "/Users/anuragkapur/tech-stuff/workspace/beancrunch/" +
            "rowfitt-erg-image-service/src/test/resources/erg-texts";
    private static final String ergImagesDir = "/Users/anuragkapur/tech-stuff/workspace/beancrunch/" +
            "rowfitt-erg-image-service/src/test/resources/erg-images";

    private static void extractTextAndWriteToFile(Path p) {
        try {
            String fileName = p.getFileName().toString().split("\\.")[0];
            byte[] imageBytes = Files.readAllBytes(p);
            GCPCloudVisionClient gcpCloudVisionClient = new GCPCloudVisionClient();
            String text = gcpCloudVisionClient.getTextFromImage(imageBytes).orElse("No text found");
            Files.write(Paths.get(ergTextDir + "/" + fileName + ".txt"), text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTestForAllFiles() throws IOException {
        Path path = Paths.get(ergImagesDir);
        Files
                .list(path)
                .peek(System.out::println)
                .forEach(GCPCloudVisionClientTestDataGenerator::extractTextAndWriteToFile);
    }

    private void cleanupOutputDirectory() throws IOException {
        Path path = Paths.get(ergTextDir);
        Files
                .list(path)
                .filter(p -> !p.equals(path))
                .forEach(p -> new File(p.toString()).delete());
    }

    public static void main(String[] args) throws IOException {
        GCPCloudVisionClientTestDataGenerator gcpCloudVisionClientTestDataGenerator = new GCPCloudVisionClientTestDataGenerator();
        gcpCloudVisionClientTestDataGenerator.cleanupOutputDirectory();
        gcpCloudVisionClientTestDataGenerator.writeTestForAllFiles();
    }
}

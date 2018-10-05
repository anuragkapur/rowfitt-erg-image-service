package com.beancrunch.rowfitt.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.beancrunch.rowfitt.ErgImageService;
import com.beancrunch.rowfitt.gcp.GCPCloudVisionImageService;
import com.beancrunch.rowfitt.domain.Workout;

import java.io.IOException;
import java.util.Base64;

public class AWSLambdaHandler implements RequestHandler<String, Workout> {

    private ErgImageService ergImageService = new GCPCloudVisionImageService();

    @Override
    public Workout handleRequest(String base64EncodedImage, Context context) {
        try {
            System.out.println("AWSLambdaHandler will handle request");
            System.out.println("base64EncodedImage :: " + base64EncodedImage);
            byte[] imageBytes = Base64.getDecoder().decode(base64EncodedImage);
            return ergImageService.getWorkoutFromImage(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.beancrunch.rowfitt;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.beancrunch.rowfitt.domain.Workout;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class AWSLambdaHandler implements RequestStreamHandler {

    private ErgImageService ergImageService;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        System.out.println("will handle request");
        ergImageService = new GCPCloudVisionImageService();
        byte[] imageBytes = IOUtils.toByteArray(inputStream);
        System.out.println(imageBytes.length);
        System.out.println("string :: " + new String(imageBytes));
        Workout workout = new Workout();
        IOUtils.write("ok", outputStream, Charset.defaultCharset());
        //ergImageService.getWorkoutFromImage(imageBytes);
    }
}

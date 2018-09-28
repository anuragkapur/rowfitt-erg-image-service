package com.beancrunch.rowfitt;

import com.beancrunch.rowfitt.domain.Workout;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GCPCloudVisionImageService implements ErgImageService {

    public Workout getWorkoutFromImage(byte[] imageBinary) throws IOException {
        ImageAnnotatorClient imageAnnotatorClient = ImageAnnotatorClient.create();
        ByteString imageByteString = ByteString.copyFrom(imageBinary);

        // Builds the image annotation request
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imageByteString).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                                                                .addFeatures(feat)
                                                                .setImage(img)
                                                                .build();
        requests.add(request);

        // Perform text detection
        BatchAnnotateImagesResponse response = imageAnnotatorClient.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();
        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                System.out.printf("Error: %s\n", res.getError().getMessage());
            }

            System.out.println(res.getFullTextAnnotation().getText());
        }
        return null;
    }
}

package com.beancrunch.rowfitt.gcp;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class GCPCloudVisionClient {

    Optional<String> getTextFromImage(byte[] imageBinary) throws IOException {

        try (ImageAnnotatorClient imageAnnotatorClient = ImageAnnotatorClient.create()) {
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
            Optional<String> textOptional = response
                    .getResponsesList()
                    .stream()
                    .findFirst()
                    .map(r -> r.getFullTextAnnotation().getText());
            imageAnnotatorClient.shutdown();
            return textOptional;
        }
    }
}

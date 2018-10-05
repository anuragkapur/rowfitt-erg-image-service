package com.beancrunch.rowfitt.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.beancrunch.rowfitt.domain.Workout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class AWSAPIGatewayLambdaHandler extends AWSLambdaHandlerDecorator<Map<String, Object>, APIGatewayResponse> {

    private RequestHandler<String, Workout> requestHandler = new AWSLambdaHandler();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public APIGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        System.out.println("AWSAPIGatewayLambdaHandler will handle request");
        String base64EncodedImage = stringObjectMap.get("body").toString();
        System.out.println("body :: " + base64EncodedImage);
        Workout workout = requestHandler.handleRequest(base64EncodedImage, context);

        APIGatewayResponse apiGatewayResponse = getNewAPIGatewayResponse();
        populateResponseBody(workout, apiGatewayResponse);
        return apiGatewayResponse;
    }

    private void populateResponseBody(Workout workout, APIGatewayResponse apiGatewayResponse) {
        try {
            apiGatewayResponse.setBody(objectMapper.writeValueAsString(workout));
            System.out.println("response body :: " + apiGatewayResponse.getBody());
            apiGatewayResponse.setStatusCode(200);
        } catch (JsonProcessingException e) {
            apiGatewayResponse.setStatusCode(503);
            e.printStackTrace();
        }
    }

    private APIGatewayResponse getNewAPIGatewayResponse() {
        APIGatewayResponse apiGatewayResponse = new APIGatewayResponse();
        apiGatewayResponse.setBase64Encoded(false);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        apiGatewayResponse.setHeaders(headers);
        return apiGatewayResponse;
    }
}

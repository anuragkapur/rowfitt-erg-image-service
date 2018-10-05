package com.beancrunch.rowfitt.aws;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class APIGatewayResponse {

    private boolean isBase64Encoded;
    private int statusCode;
    private Map<String, String> headers;
    private Map<String, List<String>> multiValueHeaders;
    private String body;
}

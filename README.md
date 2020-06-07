# Rowfitt-Erg-Image-Service

Translates [erg workout images](/src/test/resources/erg-images) to structures JSON representation of the Workout.

[![Build Status](https://travis-ci.com/anuragkapur/rowfitt-erg-image-service.svg?branch=master)](https://travis-ci.com/anuragkapur/rowfitt-erg-image-service)

# High Level Approach
* Use GCP Cloud Vision API for OCR
* Use custom algorithm to translate unstructured text to Workout JSON representation

# Developer Guide

## Build and run tests
```bash
mvn clean install
```

## Deploy
```
sls deploy --aws-profile <aws_profile_name>
```
package com.google.sps.image;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import java.util.ArrayList;
import java.util.List;

public final class OcrAnalyser extends Analyser {

  @Override
  public List<String> analyse(String imageUrl) {
    List<String> elements = new ArrayList<>();
    List<AnnotateImageResponse> responses = getResponses(imageUrl, Type.TEXT_DETECTION);

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        System.out.format("Error: %s%n", res.getError().getMessage());
        return elements;
      }

      for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
        // System.out.format("Text: %s%n", annotation.getDescription());
        elements.add(annotation.getDescription().toLowerCase());
      }
    }

    return elements;
  }

  @Override
  public List<String> analyseStoredImage(String imageUrl) {
    List<String> elements = new ArrayList<>();
    List<AnnotateImageResponse> responses = getResponsesStoredImage(imageUrl, Type.TEXT_DETECTION);

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        System.out.format("Error: %s%n", res.getError().getMessage());
        return elements;
      }

      for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
        // System.out.format("Text: %s%n", annotation.getDescription());
        elements.add(annotation.getDescription().toLowerCase());
      }
    }

    return elements;
  }
}

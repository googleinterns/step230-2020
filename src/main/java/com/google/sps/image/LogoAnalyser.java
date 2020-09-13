package com.google.sps.image;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import java.util.ArrayList;
import java.util.List;

/** Extracts logos from images using Cloud Vision API. */
public final class LogoAnalyser extends Analyser {

  @Override
  public List<String> analyse(String imageUrl) {
    List<String> elements = new ArrayList<>();
    List<AnnotateImageResponse> responses = getResponses(imageUrl, Type.LOGO_DETECTION);

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        System.out.format("Error: %s%n", res.getError().getMessage());
        return elements;
      }

      for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
        elements.add(annotation.getDescription().toLowerCase());
      }
    }

    return elements;
  }

  @Override
  public List<String> analyseStoredImage(String imagePath) {
    List<String> elements = new ArrayList<>();
    List<AnnotateImageResponse> responses = getResponsesStoredImage(imagePath, Type.LOGO_DETECTION);

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        System.out.format("Error: %s%n", res.getError().getMessage());
        return elements;
      }

      for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
        elements.add(annotation.getDescription().toLowerCase());
      }
    }

    return elements;
  }
}

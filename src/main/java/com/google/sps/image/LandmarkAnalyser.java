package com.google.sps.image;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.LocationInfo;
import java.util.ArrayList;
import java.util.List;

/** Extracts landmarks from images using Cloud Vision API. */
public final class LandmarkAnalyser extends Analyser {

  @Override
  public List<String> analyse(String imageUrl) {
    List<String> elements = new ArrayList<>();
    List<AnnotateImageResponse> responses = getResponses(imageUrl, Type.LANDMARK_DETECTION);

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        System.out.format("Error: %s%n", res.getError().getMessage());
        return elements;
      }

      for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
        LocationInfo info = annotation.getLocationsList().listIterator().next();
        elements.add(annotation.getDescription().toLowerCase());
      }
    }

    return elements;
  }

  @Override
  public List<String> analyseStoredImage(String imagePath) {
    List<String> elements = new ArrayList<>();
    List<AnnotateImageResponse> responses = getResponsesStoredImage(imagePath, Type.LANDMARK_DETECTION);

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        System.out.format("Error: %s%n", res.getError().getMessage());
        return elements;
      }

      for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
        LocationInfo info = annotation.getLocationsList().listIterator().next();
        elements.add(annotation.getDescription().toLowerCase());
      }
    }

    return elements;
  }
}

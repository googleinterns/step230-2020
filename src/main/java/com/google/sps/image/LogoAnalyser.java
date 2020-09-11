package com.google.sps.image;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import java.util.ArrayList;
import java.util.List;

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

      // For full list of available annotations, see http://g.co/cloud/vision/docs
      for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
        // System.out.println(annotation.getDescription());
        elements.add(annotation.getDescription());
      }
    }

    return elements;
  }
}

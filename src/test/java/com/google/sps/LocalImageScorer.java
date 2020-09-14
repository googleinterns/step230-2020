package com.google.sps.image;

import java.util.List;
import java.util.Set;

/** Similar to ImageScorer, but for testing purposes. */
public final class LocalImageScorer {

  private static final float LANDMARK_SCORE = 0.8f;
  private static final float LOGO_SCORE = 0.5f;
  private static final float OCR_SCORE = 0.1f;
  private static final float LABEL_SCORE = 0.2f;

  private static final double EPS = 1e-5;

  private String[] keywords;

  private Analyser label;
  
  private Analyser landmark;

  private Analyser ocr;

  private Analyser logo;

  public LocalImageScorer(String[] keywords) {
    label = new LabelAnalyser();
    landmark = new LandmarkAnalyser();
    ocr = new OcrAnalyser();
    logo = new LogoAnalyser();

    this.keywords = keywords;
  }

  private float landmarkScore(String imagePath) {
    List<String> landmarkElements = landmark.analyseStoredImage(imagePath);

    if (!landmarkElements.isEmpty()) {
      return LANDMARK_SCORE;
    }
    return 0;
  }

  private float logoScore(String imagePath) {
    List<String> logoElements = logo.analyseStoredImage(imagePath);

    if (!logoElements.isEmpty()) {
      for (String keyword : keywords) {
        if (logoElements.contains(keyword)) {
          return LOGO_SCORE;
        }
      }
    }
    return 0;
  }

  private float ocrScore(String imagePath) {
    List<String> ocrElements = ocr.analyseStoredImage(imagePath);

    if (ocrElements.size() > 0) {
      return -OCR_SCORE;
    }
    return 0;
  }

  private float labelScore(String imagePath) {
    float score = 0;
    List<String> labelElements = label.analyseStoredImage(imagePath);

    if (!labelElements.isEmpty()) {
      for (String keyword : keywords) {
        if (labelElements.contains(keyword)) {
          score += LABEL_SCORE;
        }
      }
    }
    return score;
  }

  /**
   *  The algorithm that scores images.
   *  @param  imagePath link of image that needs to be scored.
   */
  public float score(String imagePath) {
    float imageScore = landmarkScore(imagePath);
    float logo = logoScore(imagePath);
  
    if (logo < EPS) {
      imageScore += ocrScore(imagePath);
      if (imageScore < 0) {
        return imageScore;
      }
    } else {
      imageScore += logo;
    }
    imageScore += labelScore(imagePath);
    
    return imageScore;
  }
}

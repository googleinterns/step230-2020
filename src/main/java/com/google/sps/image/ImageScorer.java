package com.google.sps.image;

import java.util.List;
import java.util.Set;

/** 
 *  Custom class of scoring images using
 *  the elements extracted using Cloud Vision API.
 */
public final class ImageScorer {

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

  public ImageScorer(String[] keywords) {
    label = new LabelAnalyser();
    landmark = new LandmarkAnalyser();
    ocr = new OcrAnalyser();
    logo = new LogoAnalyser();

    this.keywords = keywords;
  }

  private float landmarkScore(String imageUrl) {
    List<String> landmarkElements = landmark.analyse(imageUrl);

    if (!landmarkElements.isEmpty()) {
      return LANDMARK_SCORE;
    }
    return 0;
  }

  private float logoScore(String imageUrl) {
    List<String> logoElements = logo.analyse(imageUrl);

    if (!logoElements.isEmpty()) {
      for (String keyword : keywords) {
        if (logoElements.contains(keyword)) {
          return LOGO_SCORE;
        }
      }
    }
    return 0;
  }

  private float ocrScore(String imageUrl) {
    List<String> ocrElements = ocr.analyse(imageUrl);

    if (ocrElements.size() > 0) {
      return -OCR_SCORE;
    }
    return 0;
  }

  private float labelScore(String imageUrl) {
    float score = 0;
    List<String> labelElements = label.analyse(imageUrl);

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
   *  @param  imageUrl link of image that needs to be scored.
   */
  public float score(String imageUrl) {
    float imageScore = landmarkScore(imageUrl);
    float logo = logoScore(imageUrl);
  
    if (logo < EPS) {
      imageScore += ocrScore(imageUrl);
      if (imageScore < 0) {
        return imageScore;
      }
    } else {
      imageScore += logo;
    }
    imageScore += labelScore(imageUrl);
    
    return imageScore;
  }
}
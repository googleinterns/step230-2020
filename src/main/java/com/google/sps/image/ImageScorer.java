package com.google.sps.image;

public final class ImageScorer {

  private static final float LANDMARK_SCORE = 0.8f;

  private static final float LOGO_SCORE = 0.7f;

  private static final float OCR_SCORE = 0.5f;

  private static final float LABEL_SCORE = 0.1f;

  public static float score(String imageUrl) {
    float imageScore = 0f; 
    Analyser label = new LabelAnalyser();
    Analyser landmark = new LandmarkAnalyser();
    Analyser ocr = new OcrAnalyser();
    Analyser logo = new LogoAnalyser();

    if (!landmark.analyse(imageUrl).isEmpty()) {
      imageScore += LANDMARK_SCORE;
    } else if (!logo.analyse(imageUrl).isEmpty()) {
      imageScore += LOGO_SCORE;
    } else if (!ocr.analyse(imageUrl).isEmpty()) {
      imageScore += OCR_SCORE;
    }
    if (!label.analyse(imageUrl).isEmpty()) {
      imageScore += LABEL_SCORE;
    }
    
    return imageScore;
  }
}
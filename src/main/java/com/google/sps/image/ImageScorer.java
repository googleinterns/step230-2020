package com.google.sps.image;

public final class ImageScorer {

  public static void score(String imageUrl) {
     Analyser label = new LabelAnalyser();
     Analyser landmark = new LandmarkAnalyser();
     Analyser ocr = new OcrAnalyser();
     Analyser logo = new LogoAnalyser();

     label.analyse(imageUrl);
     landmark.analyse(imageUrl);
     ocr.analyse(imageUrl);
     logo.analyse(imageUrl);
  }
}
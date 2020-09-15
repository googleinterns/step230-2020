package com.google.sps.image;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import java.util.List;

@RunWith(JUnit4.class)
public final class ImageAnalyserTest {

  private static final String PARIS_IMAGE_PATH = "./src/test/image/paris.jpg";
  private static final String LONDON_IMAGE_PATH = "./src/test/image/bigben.jpg";
  private static final String GOOGLE_IMAGE_PATH = "./src/test/image/google.jpg";
  private static final String MORNING_IMAGE_PATH = "./src/test/image/morning.jpg";
  private static final String BIRTHDAY_IMAGE_PATH = "./src/test/image/birthday.jpg";
  private static final String CAT_IMAGE_PATH = "./src/test/image/cat.jpg";

  @Test
  public void parisLandmark() {
    Analyser landmark = new LandmarkAnalyser();
    List<String> actualElements = landmark.analyseStoredImage(PARIS_IMAGE_PATH);

    for (String element : actualElements) {
      if (element.contains("eiffel")) {
        return;
      }
    }
    Assert.fail("None element contains Eiffel Tour, but " + actualElements);
  }

  @Test
  public void londonLandmark() {
    Analyser landmark = new LandmarkAnalyser();
    List<String> actualElements = landmark.analyseStoredImage(LONDON_IMAGE_PATH);

    for (String element : actualElements) {
      if (element.contains("ben")) {
        return;
      }
    }
    Assert.fail("None element contains Big Ben, but " + actualElements);
  }

  @Test
  public void googleLogo() {
    Analyser logo = new LogoAnalyser();
    List<String> actualElements = logo.analyseStoredImage(GOOGLE_IMAGE_PATH);

    for (String element : actualElements) {
      if (element.contains("google")) {
        return;
      }
    }
    Assert.fail("None element contains Google Logo, but " + actualElements);
  }

  @Test
  public void morningDigitalFont() {
    Analyser ocr = new OcrAnalyser();
    List<String> actualElements = ocr.analyseStoredImage(MORNING_IMAGE_PATH);

    for (String element : actualElements) {
      if (element.contains("morning")) {
        return;
      }
    }
    Assert.fail("None element contains a morning text, but " + actualElements);
  }

  @Test
  public void birthdayHandwriting() {
    Analyser ocr = new OcrAnalyser();
    List<String> actualElements = ocr.analyseStoredImage(BIRTHDAY_IMAGE_PATH);

    for (String element : actualElements) {
      if (element.contains("birthday")) {
        return;
      }
    }
    Assert.fail("None element contains a birthday text, but " + actualElements);
  }

  @Test
  public void catLabel() {
    Analyser label = new LabelAnalyser();
    List<String> actualElements = label.analyseStoredImage(CAT_IMAGE_PATH);

    for (String element : actualElements) {
      if (element.contains("cat")) {
        return;
      }
    }
    Assert.fail("None element contains a cat label, but " + actualElements);
  }
}

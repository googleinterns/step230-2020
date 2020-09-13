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

  @Test
  public void parisLandmark() {
    Analyser landmark = new LandmarkAnalyser();
    List<String> actualElements = landmark.analyseStoredImage("./src/test/image/paris.jpg");

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
    List<String> actualElements = landmark.analyseStoredImage("./src/test/image/bigben.jpg");

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
    List<String> actualElements = logo.analyseStoredImage("./src/test/image/google.jpg");

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
    List<String> actualElements = ocr.analyseStoredImage("./src/test/image/morning.jpg");

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
    List<String> actualElements = ocr.analyseStoredImage("./src/test/image/birthday.jpg");

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
    List<String> actualElements = label.analyseStoredImage("./src/test/image/cat.jpg");

    for (String element : actualElements) {
      if (element.contains("cat")) {
        return;
      }
    }
    Assert.fail("None element contains a cat label, but " + actualElements);
  }
}

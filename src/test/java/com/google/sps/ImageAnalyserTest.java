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
    List<String> actualElements = landmark.analyse("../image/paris.jpg");

    for (String element : actualElements) {
      if (element.contains("eiffel")) {
        Assert.assertTrue(true);
      }
    }
    Assert.fail("None element contains Eiffel Tour.");
  }

  @Test
  public void sydnayLandmark() {
    Analyser landmark = new LandmarkAnalyser();
    List<String> actualElements = landmark.analyse("../image/opera.jpg");

    for (String element : actualElements) {
      if (element.contains("opera")) {
        Assert.assertTrue(true);
      }
    }
    Assert.fail("None element contains Opera House.");
  }

  @Test
  public void googleLogo() {
    Analyser logo = new LogoAnalyser();
    List<String> actualElements = logo.analyse("../image/google.jpg");

    for (String element : actualElements) {
      if (element.contains("google")) {
        Assert.assertTrue(true);
        return;
      }
    }
    Assert.fail("None element contains Google Logo.");
  }

  @Test
  public void morningDigitalFont() {
    Analyser ocr = new OcrAnalyser();
    List<String> actualElements = ocr.analyse("../image/morning.jpg");

    for (String element : actualElements) {
      if (element.contains("morning")) {
        Assert.assertTrue(true);
        return;
      }
    }
    Assert.fail("None element contains a morning text.");
  }

  @Test
  public void birthdayHandwriting() {
    Analyser ocr = new OcrAnalyser();
    List<String> actualElements = ocr.analyse("../image/morning.jpg");

    for (String element : actualElements) {
      if (element.contains("morning")) {
        Assert.assertTrue(true);
        return;
      }
    }
    Assert.fail("None element contains a morning text.");
  }

  @Test
  public void catLabel() {
    Analyser label = new LabelAnalyser();
    List<String> actualElements = label.analyse("../image/cat.jpg");

    for (String element : actualElements) {
      if (element.contains("cat")) {
        Assert.assertTrue(true);
        return;
      }
    }
    Assert.fail("None element contains a cat label.");
  }
}

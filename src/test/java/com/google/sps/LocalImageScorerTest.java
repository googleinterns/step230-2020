package com.google.sps.image;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import java.util.List;

@RunWith(JUnit4.class)
public final class LocalImageScorerTest {

  private static final String PARIS_IMAGE_PATH = "./src/test/image/paris.jpg";

  private static final String LONDON_IMAGE_PATH = "./src/test/image/bigben.jpg";

  private static final String GOOGLE_IMAGE_PATH = "./src/test/image/google.jpg";

  private static final String MORNING_IMAGE_PATH = "./src/test/image/morning.jpg";

  private static final String BIRTHDAY_IMAGE_PATH = "./src/test/image/birthday.jpg";

  private static final String CAT_IMAGE_PATH = "./src/test/image/cat.jpg";

  private static String getBestImage(String[] keywords, String[] imagePathList) {
    String bestImage = "";
    float bestScore = -1;
    LocalImageScorer scorer = new LocalImageScorer(keywords);

    for (String imagePath : imagePathList) {
      float score = scorer.score(imagePath);
      if (score > bestScore) {
        bestScore = score;
        bestImage = imagePath;
      }
    }
    return bestImage;
  }

  @Test
  public void parisKeyword() {
    String[] imagePathList = {PARIS_IMAGE_PATH, LONDON_IMAGE_PATH, 
                            MORNING_IMAGE_PATH, CAT_IMAGE_PATH};
    String[] keywords = {"paris"};
    String bestImage = getBestImage(keywords, imagePathList);
    
    Assert.assertEquals(PARIS_IMAGE_PATH, bestImage);
  }

  @Test
  public void googleKeyword() {
    String[] imagePathList = {GOOGLE_IMAGE_PATH, MORNING_IMAGE_PATH, 
                            BIRTHDAY_IMAGE_PATH, CAT_IMAGE_PATH};
    String[] keywords = {"google"};
    String bestImage = getBestImage(keywords, imagePathList);
    
    Assert.assertEquals(GOOGLE_IMAGE_PATH, bestImage);
  }

  @Test
  public void catKeyword() {
    String[] imagePathList = {BIRTHDAY_IMAGE_PATH, CAT_IMAGE_PATH, MORNING_IMAGE_PATH};
    String[] keywords = {"cat"};
    String bestImage = getBestImage(keywords, imagePathList);
    
    Assert.assertEquals(CAT_IMAGE_PATH, bestImage);
  }
}

package com.google.sps.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@RunWith(JUnit4.class)
public final class ImageSelectionTest {

  private final Set<String> keywords = new LinkedHashSet<>();

  // This is how every Bing image link starts
  private static final String EXPECTED_LINK = "https://tse";

  private void checkScrapingOutputLink() {
    ImageSelection imageSelection = new ImageSelection(keywords);

    try {
      String actualLink = imageSelection.getBestImage();
      Assert.assertEquals(EXPECTED_LINK, actualLink.substring(0, 11));
    } catch(IOException ex) {
      // Test fail because the function threw an exception
      Assert.fail(ex.getMessage());
    }
  }

  @Test
  public void singleKeyword() {
    keywords.add("london");

    checkScrapingOutputLink();
  }

  @Test
  public void specialCharactersKeywords() {
    keywords.add("travel");
    keywords.add("morning");
    keywords.add("sun");
    keywords.add("Washington, D.C.");

    checkScrapingOutputLink();
  }

  @Test
  public void commonKeywords() {
    keywords.add("job");
    keywords.add("promotion");
    keywords.add("engineer");

    checkScrapingOutputLink();
  }

  @Test
  public void addSpaceIntoKeywords() {
    keywords.add("good morning");

    checkScrapingOutputLink();
  }

  @Test
  public void removeExtraKeywords() {
    for (char letter = 'a'; letter <= 'z'; ++letter) {
      keywords.add(String.valueOf(letter));
    }

    checkScrapingOutputLink();
  }
}

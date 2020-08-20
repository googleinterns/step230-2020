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

  private Set<String> keywords;

  private static final String bingImgLinkStart = "https://tse";

  private static void checkLink(ImageSelection imageSelection) {
    try {
      String actualLink = imageSelection.getBestImage();
      Assert.assertEquals(bingImgLinkStart, actualLink.substring(0, 11));
    } catch(IOException ex) {
      // Test fail because the function threw an exception
      Assert.fail(ex.getMessage());
    }
  }

  @Before
  public void setUp() {
    keywords = new LinkedHashSet<>();
  }

  @Test
  public void fewKeywordsUrlGenerator() {
    keywords.add("london");

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }

  @Test
  public void moreKeywordsUrlGenerator() {
    keywords.add("travel");
    keywords.add("morning");
    keywords.add("sun");

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }

  @Test
  public void relevantKeywordsUrlGenerator() {
    keywords.add("job");
    keywords.add("promotion");
    keywords.add("engineer");

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }

  @Test
  public void spaceKeywordsUrlGenerator() {
    keywords.add("good morning");

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }

  @Test
  public void extraKeywordsUrlGenerator() {
    for (char letter = 'a'; letter <= 'z'; ++letter) {
      keywords.add(String.valueOf(letter));
    }

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }
}

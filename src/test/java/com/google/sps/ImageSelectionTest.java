package com.google.sps.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class ImageSelectionTest {

  private static final String bingImgLinkStart = "https://tse";

  private static void checkLink(ImageSelection imageSelection) {
    try {
      String actualLink = imageSelection.getBestImage();
      Assert.assertEquals(bingImgLinkStart, actualLink.substring(0, 11));
    } catch(IOException ex) {
      StringWriter error = new StringWriter();
      ex.printStackTrace(new PrintWriter(error));
      Assert.fail(error.toString());
    }
  }

  @Test
  public void fewKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    keywords.add("london");

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }

  @Test
  public void moreKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    keywords.add("travel");
    keywords.add("morning");
    keywords.add("sun");

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }

  @Test
  public void relevantKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    keywords.add("job");
    keywords.add("promotion");
    keywords.add("engineer");

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }

  @Test
  public void spaceKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    keywords.add("good morning");

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }

  @Test
  public void extraKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    for (char letter = 'a'; letter <= 'z'; ++letter) {
      keywords.add(String.valueOf(letter));
    }

    ImageSelection imageSelection = new ImageSelection(keywords);
    checkLink(imageSelection);
  }
}

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

  // This is how every Bing image link starts
  private static final String EXPECTED_LINK = "https://tse";

  private static final int ANALYSATION_DEPTH = 1;

  private static final int EXTRACTED_IMAGES = 1;

  private void assertOutputLink(Set<String[]> keywordQueries, String expectedLink) throws IOException {
    ImageSelection imageSelection = new ImageSelection(keywordQueries);

    String actualLink = imageSelection.getBestImage(ANALYSATION_DEPTH, EXTRACTED_IMAGES).get(0);
    Assert.assertEquals(expectedLink, actualLink.substring(0, 11));
  }

  @Test
  public void singleKeyword() throws IOException {
    Set<String[]> keywordQueries = new LinkedHashSet<>();
    String[] keywords = {"london"};
    keywordQueries.add(keywords);

    assertOutputLink(keywordQueries, EXPECTED_LINK);
  }

  @Test
  public void specialCharactersKeywords() throws IOException {
    Set<String[]> keywordQueries = new LinkedHashSet<>();
    String[] keywords = {"Washington D.C.", "travel"};
    keywordQueries.add(keywords);

    assertOutputLink(keywordQueries, EXPECTED_LINK);
  }

  @Test
  public void commonKeywords() throws IOException {
    Set<String[]> keywordQueries = new LinkedHashSet<>();
    String[] keywords = {"welcome", "google"};
    keywordQueries.add(keywords);

    assertOutputLink(keywordQueries, EXPECTED_LINK);
  }

  @Test
  public void addSpaceIntoKeywords() throws IOException {
    Set<String[]> keywordQueries = new LinkedHashSet<>();
    String[] keywords = {"job promotion"};
    keywordQueries.add(keywords);

    assertOutputLink(keywordQueries, EXPECTED_LINK);
  }
}

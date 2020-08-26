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

  private void assertOutputLink(Set<String> keywords, String expectedLink) throws IOException {
    ImageSelection imageSelection = new ImageSelection(keywords);

    String actualLink = imageSelection.getBestImage();
    Assert.assertEquals(expectedLink, actualLink.substring(0, 11));
  }

  @Test
  public void singleKeyword() throws IOException {
    Set<String> keywords = new LinkedHashSet<>();
    keywords.add("london");

    assertOutputLink(keywords, EXPECTED_LINK);
  }

  @Test
  public void specialCharactersKeywords() throws IOException {
    Set<String> keywords = new LinkedHashSet<>();

    keywords.add("travel");
    keywords.add("morning");
    keywords.add("sun");
    keywords.add("Washington, D.C.");

    assertOutputLink(keywords, EXPECTED_LINK);
  }

  @Test
  public void commonKeywords() throws IOException {
    Set<String> keywords = new LinkedHashSet<>();

    keywords.add("job");
    keywords.add("promotion");
    keywords.add("engineer");

    assertOutputLink(keywords, EXPECTED_LINK);
  }

  @Test
  public void addSpaceIntoKeywords() throws IOException {
    Set<String> keywords = new LinkedHashSet<>();
    keywords.add("good morning");

    assertOutputLink(keywords, EXPECTED_LINK);
  }

  @Test
  public void removeExtraKeywords() throws IOException {
    Set<String> keywords = new LinkedHashSet<>();
    for (char letter = 'a'; letter <= 'z'; ++letter) {
      keywords.add(String.valueOf(letter));
    }

    assertOutputLink(keywords, EXPECTED_LINK);
  }
}

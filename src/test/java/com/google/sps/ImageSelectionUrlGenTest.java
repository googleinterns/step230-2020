package com.google.sps.image;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import java.util.LinkedHashSet;
import java.util.Set;

@RunWith(JUnit4.class)
public final class ImageSelectionUrlGenTest {

  private static final int EXCEEDING_NO_LETTERS = 400;

  private static final String COPYRIGHT_FILTER = "&qft=+filterui:license-L2-L3-L4";

  private static final String SAFE_SEARCH_FILTER = "adlt=strict";

  @Test
  public void singleKeyword() {
    Set<String> keywords = new LinkedHashSet<>();

    keywords.add("london");
    ImageSelection imageSelection = new ImageSelection();
    String expected = "https://www.bing.com/images/search?q=+london";
    String actual = imageSelection.generateSearchUrl(keywords.toArray(new String[0]), COPYRIGHT_FILTER);

    Assert.assertTrue(actual.contains(expected));
    Assert.assertTrue(actual.contains(COPYRIGHT_FILTER));
    Assert.assertTrue(actual.contains(SAFE_SEARCH_FILTER));
  }

  @Test
  public void specialCharactersKeywords() {
    Set<String> keywords = new LinkedHashSet<>();

    keywords.add("travel");
    keywords.add("morning");
    keywords.add("sun");
    keywords.add("Washington, D.C.");

    ImageSelection imageSelection = new ImageSelection();
    String expected = "https://www.bing.com/images/search?q=+travel+morning+sun+Washington,+D.C.";
    String actual = imageSelection.generateSearchUrl(keywords.toArray(new String[0]), COPYRIGHT_FILTER);

    Assert.assertTrue(actual.contains(expected));
    Assert.assertTrue(actual.contains(COPYRIGHT_FILTER));
    Assert.assertTrue(actual.contains(SAFE_SEARCH_FILTER));
  }

  @Test
  public void commonKeywords() {
    Set<String> keywords = new LinkedHashSet<>();

    keywords.add("job");
    keywords.add("promotion");
    keywords.add("engineer");

    ImageSelection imageSelection = new ImageSelection();
    String expected = "https://www.bing.com/images/search?q=+job+promotion+engineer";
    String actual = imageSelection.generateSearchUrl(keywords.toArray(new String[0]), COPYRIGHT_FILTER);

    Assert.assertTrue(actual.contains(expected));
    Assert.assertTrue(actual.contains(COPYRIGHT_FILTER));
    Assert.assertTrue(actual.contains(SAFE_SEARCH_FILTER));
  }

  // Make sure that a blank space does not appear in the URL
  @Test
  public void addSpaceIntoKeywords() {
    Set<String> keywords = new LinkedHashSet<>();

    keywords.add("good morning");

    ImageSelection imageSelection = new ImageSelection();
    String expected = "https://www.bing.com/images/search?q=+good+morning";
    String actual = imageSelection.generateSearchUrl(keywords.toArray(new String[0]), COPYRIGHT_FILTER);

    Assert.assertTrue(actual.contains(expected));
    Assert.assertTrue(actual.contains(COPYRIGHT_FILTER));
    Assert.assertTrue(actual.contains(SAFE_SEARCH_FILTER));
  }

  // Not letting more than 10 keywords on the search engine
  @Test
  public void removeExtraKeywords() {
    Set<String> keywords = new LinkedHashSet<>();

    for (char letter = 'a'; letter <= 'z'; ++letter) {
      keywords.add(String.valueOf(letter));
    }

    ImageSelection imageSelection = new ImageSelection();
    String expected = "https://www.bing.com/images/search?q=+a+b+c+d+e+f+g+h+i+j&";
    String actual = imageSelection.generateSearchUrl(keywords.toArray(new String[0]), COPYRIGHT_FILTER);

    Assert.assertTrue(actual.contains(expected));
    Assert.assertTrue(actual.contains(COPYRIGHT_FILTER));
    Assert.assertTrue(actual.contains(SAFE_SEARCH_FILTER));
  }

  // Not letting more than 50 letters on the search engine
  @Test
  public void largeKeywords() {
    Set<String> keywords = new LinkedHashSet<>();

    keywords.add("goodmorningmate");
    keywords.add("thisissuchabeautifulday");
    keywords.add("enjoyit");
    keywords.add("haveagreattime");

    ImageSelection imageSelection = new ImageSelection();
    String expected = "https://www.bing.com/images/search?q=+goodmorningmate+thisissuchabeautifulday+enjoyit";
    String actual = imageSelection.generateSearchUrl(keywords.toArray(new String[0]), COPYRIGHT_FILTER);

    Assert.assertTrue(actual.contains(expected));
    Assert.assertTrue(actual.contains(COPYRIGHT_FILTER));
    Assert.assertTrue(actual.contains(SAFE_SEARCH_FILTER));
  }

  @Test
  public void truncateHugeKeywords() {
    Set<String> keywords = new LinkedHashSet<>();
    char[] keyword = new char[EXCEEDING_NO_LETTERS];  

    for (int i = 0; i < EXCEEDING_NO_LETTERS; ++i) {
      keyword[i] = 'a';
    }
    keywords.add(new String(keyword));

    ImageSelection imageSelection = new ImageSelection();
    String expected = "https://www.bing.com/images/search?q=&";
    String actual = imageSelection.generateSearchUrl(keywords.toArray(new String[0]), COPYRIGHT_FILTER);

    Assert.assertTrue(actual.contains(expected));
    Assert.assertTrue(actual.contains(COPYRIGHT_FILTER));
    Assert.assertTrue(actual.contains(SAFE_SEARCH_FILTER));
  }
}

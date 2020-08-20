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

  private final Set<String> keywords = new LinkedHashSet<>();

  private static final int EXCEEDING_NO_LETTERS = 400;

  @Test
  public void singleKeyword() {
    keywords.add("london");
    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+london&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void specialCharactersKeywords() {
    keywords.add("travel");
    keywords.add("morning");
    keywords.add("sun");
    keywords.add("Washington, D.C.");

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+travel+morning+sun+Washington,+D.C.&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void commonKeywords() {
    keywords.add("job");
    keywords.add("promotion");
    keywords.add("engineer");

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+job+promotion+engineer&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  // Make sure that a blank space does not appear in the URL
  @Test
  public void addSpaceIntoKeywords() {
    keywords.add("good morning");

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+good+morning&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  // Not letting more than 10 keywords on the search engine
  @Test
  public void removeExtraKeywords() {
    for (char letter = 'a'; letter <= 'z'; ++letter) {
      keywords.add(String.valueOf(letter));
    }

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+a+b+c+d+e+f+g+h+i+j&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  // Not letting more than 50 letters on the search engine
  @Test
  public void largeKeywords() {
    keywords.add("goodmorningmate");
    keywords.add("thisissuchabeautifulday");
    keywords.add("enjoyit");
    keywords.add("haveagreattime");

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+goodmorningmate+thisissuchabeautifulday+enjoyit&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void trucateHugeKeywords() {
    char[] keyword = new char[EXCEEDING_NO_LETTERS];  

    for (int i = 0; i < EXCEEDING_NO_LETTERS; ++i) {
      keyword[i] = 'a';
    }
    keywords.add(new String(keyword));

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }
}

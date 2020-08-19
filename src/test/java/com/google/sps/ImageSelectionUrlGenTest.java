package com.google.sps.image;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.List;

public final class ImageSelectionUrlGenTest {

  private static final int EXCEEDING_NO_LETTERS = 400;

  @Test
  public void fewKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    keywords.add("london");

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+london&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void moreKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    keywords.add("travel");
    keywords.add("morning");
    keywords.add("sun");

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+travel+morning+sun&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void relevantKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
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
  public void spaceKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    keywords.add("good morning");

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=+good+morning&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }

  // Not letting more than 10 keywords on the search engine
  @Test
  public void extraKeywordsUrlGenerator() {
    List<String> keywords = new ArrayList<>();
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
  public void extraLettersUrlGenerator() {
    List<String> keywords = new ArrayList<>();

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
  public void hugeKeywordUrlGenerator() {
    List<String> keywords = new ArrayList<>();
    char[] keyword = new char[this.EXCEEDING_NO_LETTERS];  

    for (int i = 0; i < this.EXCEEDING_NO_LETTERS; ++i) {
      keyword[i] = 'a';
    }
    keywords.add(new String(keyword));

    ImageSelection imageSelection = new ImageSelection(keywords);
    String expected = "https://www.bing.com/images/search?q=&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";
    String actual = imageSelection.generateSearchUrl();

    Assert.assertEquals(expected, actual);
  }
}
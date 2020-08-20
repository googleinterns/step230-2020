package com.google.sps.image;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ImageSelection {

  private Set<String> keywords;

  private static final String USER_AGENT = "Mozilla/5.0 (X11; CrOS x86_64 13099.85.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.110 Safari/537.36";
  
  private static final String BING_URL = "https://www.bing.com/images/search?q=";

  private static final String BING_QUERY_PARAM = "&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";

  private static final int MAX_NO_LETTERS = 50;

  private static final int MAX_NO_KEYWORDS = 10;

  public ImageSelection(Set<String> keywords) {
    this.keywords = keywords;
  }

  /**
   * @param keywords  words that will be added in the search URL.
   * @return          the URL where the search will be made.
   */
  public String generateSearchUrl() {
    int addedWords = 0;
    int addedLetters = 0;
    String searchUrl = new String(BING_URL);

    for (String word : keywords) {
      addedLetters += word.length();
      if (addedLetters >= this.MAX_NO_LETTERS) {
        break;
      }
      searchUrl = searchUrl + "+" + word.replaceAll(" ", "+");
      ++addedWords;
      if (addedWords >= this.MAX_NO_KEYWORDS) {
        break;
      }
    }
    searchUrl = searchUrl + BING_QUERY_PARAM;

    return searchUrl; 
  }

  public String getBestImage() throws IOException {
    List<String> imgSrc = new ArrayList<>();

    Document doc = Jsoup.connect(generateSearchUrl()).userAgent(this.USER_AGENT).get();
    Elements elements = doc.getElementsByTag("img");

    for (Element element : elements) {
      imgSrc.add(element.attr("abs:data-src"));
    }

    // Return first relevant image
    for (String imageUrl : imgSrc) {
      if (!imageUrl.equals("")) {
        return imageUrl;
      }
    }

    /**
     *   TODO: Multiple image selection.
     *   Relying on a single image is not enough.
     *   We must provide multiple images to the user.
     */
    return "";
  }
}

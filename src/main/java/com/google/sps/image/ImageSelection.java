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

  private final Set<String> keywords;

  private static final String USER_AGENT = "Mozilla/5.0 (X11; CrOS x86_64 13099.85.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.110 Safari/537.36";
  
  private static final int MAX_NO_LETTERS = 50;

  private static final int MAX_NO_KEYWORDS = 10;

  private static final int ANALYSATION_DEPTH = 8;

  public ImageSelection(Set<String> keywords) {
    this.keywords = keywords;
  }

  private String addFilter(String url, String filter) {
    return url + filter;
  }

  /**
   * @return  URL where the search will be made.
   */
  public String generateSearchUrl() {
    int addedWords = 0;
    int addedLetters = 0;
    String bingUrl = "https://www.bing.com/images/search?q=";

    for (String word : keywords) {
      addedLetters += word.length();
      if (addedLetters >= MAX_NO_LETTERS) {
        break;
      }
      bingUrl = bingUrl + "+" + word.replaceAll(" ", "+");
      ++addedWords;
      if (addedWords >= MAX_NO_KEYWORDS) {
        break;
      }
    }

    // Public Domain license
    final String publicLicenseFilter = "&qft=+filterui:license-L1";
    final String copyrightFilter = "&qft=+filterui:license-L2_L3_L4";

    final String bingQueryParam = "&form=IRFLTR&first=1&scenario=ImageBasicHover";
    final String safeSearchFilter = "&adlt=strict";

    bingUrl = addFilter(bingUrl, copyrightFilter);
    bingUrl = addFilter(bingUrl, bingQueryParam);
    bingUrl = addFilter(bingUrl, safeSearchFilter);
    return bingUrl; 
  }

  /**
   * This is an endpoint. Call this function to get a relevant image.
   *
   * @return  URL of the first image scraped from Bing Image Search.
   * @exception IOException if Bing doesn't map any image to the keywords.
   */
  public String getBestImage() throws IOException {
    List<String> imgSrc = new ArrayList<>();

    Document doc = Jsoup.connect(generateSearchUrl()).userAgent(USER_AGENT).get();
    Elements elements = doc.getElementsByTag("img");

    for (Element element : elements) {
      imgSrc.add(element.attr("abs:data-src"));
    }
    int analysedImages = 0;
    float bestImageScore = -1;
    String bestImage = "";
    ImageScorer imageScorer = new ImageScorer(keywords); 

    // Return first relevant image
    for (String imageUrl : imgSrc) {
      if (analysedImages > ANALYSATION_DEPTH) {
        break;
      }
      if (!imageUrl.isEmpty()) {
        float currentImageScore = imageScorer.score(imageUrl);
        System.out.println(analysedImages + " " + currentImageScore);

        if (currentImageScore > bestImageScore) {
          bestImageScore = currentImageScore;
          bestImage = imageUrl;
        }
        ++analysedImages;
      }
    }
    /**
     *   TODO: Multiple image selection.
     *   Relying on a single image is not enough.
     *   We must provide multiple images to the user.
     */
    return bestImage;
  }
}
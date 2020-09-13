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

  private final Set<String[]> keywordQueries;

  private static final String USER_AGENT = "Mozilla/5.0 (X11; CrOS x86_64 13099.85.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.110 Safari/537.36";
  
  private static final int MAX_NO_LETTERS = 50;

  private static final int MAX_NO_KEYWORDS = 10;

  private static final int MAX_NO_QUERIES = 3;

  // Public Domain license
  private static final String PUBLIC_LICENSE_FILTER = "&qft=+filterui:license-L1";
  
  private static final String USE_SHARE_FILTER = "&qft=+filterui:license-L2_L3_L4";

  public ImageSelection() {
    this.keywordQueries = new HashSet<>();
  }

  public ImageSelection(Set<String[]> keywordQueries) {
    this.keywordQueries = keywordQueries;
  }

  private String addFilter(String url, String filter) {
    return url + filter;
  }

  /**
   * @return  URL where the search will be made.
   */
  public String generateSearchUrl(String[] keywords, String copyrightFilter) {
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
   * @param     analysationDepth Maximm number of different queries that the web crawler does. 
   * @return    URL of the first image scraped from Bing Image Search.
   * @exception IOException if Bing doesn't map any image to the keywords.
   */
  public String getBestImage(int analysationDepth) throws IOException {

    int remainingSearches = MAX_NO_QUERIES;
    float bestImageScore = -1;
    String bestImage = "";
    for (String[] keywords : keywordQueries) {
      if (remainingSearches == 0) {
        break;
      }

      List<String> imgSrc = new ArrayList<>();

      Document doc = Jsoup.connect(generateSearchUrl(keywords, PUBLIC_LICENSE_FILTER)).userAgent(USER_AGENT).get();
      Elements elements = doc.getElementsByTag("img");

      for (Element element : elements) {
        imgSrc.add(element.attr("abs:data-src"));
      }
      
      int analysedImages = 0;
      ImageScorer imageScorer = new ImageScorer(keywords); 

      // Analyse a number of images to get the improve relevancy0
      for (String imageUrl : imgSrc) {
        if (analysedImages >= analysationDepth) {
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

      // Analyse the rest of images
      if (analysedImages < analysationDepth) {
        doc = Jsoup.connect(generateSearchUrl(keywords, USE_SHARE_FILTER)).userAgent(USER_AGENT).get();
        elements = doc.getElementsByTag("img");

        for (Element element : elements) {
          imgSrc.add(element.attr("abs:data-src"));
        }

        for (String imageUrl : imgSrc) {
          if (analysedImages >= analysationDepth) {
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
      }
      --remainingSearches;
    }
    
    /**
     *   TODO: Multiple image selection.
     *   Relying on a single image is not enough.
     *   We must provide multiple images to the user.
     */
    return bestImage;
  }
}
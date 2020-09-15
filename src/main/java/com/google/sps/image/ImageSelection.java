package com.google.sps.image;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/** Web crawler that extracts images given a set of keywords */
public final class ImageSelection {

  private final Set<String[]> keywordQueries;

  private static final String USER_AGENT = "Mozilla/5.0 (X11; CrOS x86_64 13099.85.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.110 Safari/537.36";
  
  private static final int MAX_NO_LETTERS = 50;
  private static final int MAX_NO_KEYWORDS = 10;
  private static final int MAX_NO_QUERIES = 3;

  // Public Domain license
  private static final String PUBLIC_LICENSE_FILTER = "&qft=+filterui:license-L1";
  // Free to share and use commercially license
  private static final String USE_SHARE_FILTER = "&qft=+filterui:license-L2_L3_L4";

  private static final String BACKUP_IMAGE1 = "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg";
  private static final String BACKUP_IMAGE2 = "https://media.nationalgeographic.org/assets/photos/167/142/7dbe792c-eb3b-4743-9135-2e6087c7446c.jpg";
  private static final String BACKUP_IMAGE3 = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.thecoromandel.com%2Fexplore%2Fpauanui-tairua-new-years-eve-fireworks-display&psig=AOvVaw11KkfPQsY02iLGH0BbTie8&ust=1600259849353000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCKjLsNSW6-sCFQAAAAAdAAAAABAV";
  private static final String BACKUP_IMAGE4 = "https://www.wallpaperup.com/uploads/wallpapers/2017/01/18/1074860/b2d88573c9f3d799a4b23048e5dd9193-700.jpg";

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
   * @param   keywords list of keywords to be added in the search query.
   * @param   copyrightFiter filter copyrighted images - added in the search query.
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

  private static float findBestImage(ImageScorer imageScorer, StringBuilder bestImage, 
                                    List<String> imageSources, int analysationDepth) {
    int analysedImages = 0;
    float bestImageScore = -1f;
    for (String imageUrl : imageSources) {
      if (analysedImages >= analysationDepth) {
        break;
      }
      if (!imageUrl.isEmpty()) {
        float currentImageScore = imageScorer.score(imageUrl);

        if (currentImageScore > bestImageScore) {
          bestImageScore = currentImageScore;
          bestImage.replace(0, bestImage.length(), imageUrl); 
        }
        ++analysedImages;
      }
    }                                   
    return bestImageScore;
  }

  private List<String> addBackups(List<String> images, int noBackups) {
    for (int i = 1; i <= noBackups; ++i) {
      switch(i) {
        case 1:
          images.add(BACKUP_IMAGE1);
          break;
        case 2:
          images.add(BACKUP_IMAGE2);
          break;
        case 3:
          images.add(BACKUP_IMAGE3);
          break;
        default:
          images.add(BACKUP_IMAGE4);
      }   
    }

    return images;
  }

  private List<String> getUrls(List<ImageDetails> images, int extractions) {
    List<String> imageUrls = new ArrayList<>();
    for (ImageDetails image : images) {
      imageUrls.add(image.getUrl());
    }

    imageUrls.removeAll(Arrays.asList("", null));
    if (imageUrls.size() < extractions) {
      imageUrls = addBackups(imageUrls, extractions - imageUrls.size());
    }

    return imageUrls;
  } 

  /**
   * This is an endpoint. Call this function to get a relevant image.
   *
   * @param     analysationDepth Maximm number of different queries that the web crawler does. 
   * @param     extractions Number of images returned.
   * @return    URL of the first image scraped from Bing Image Search.
   * @exception IOException if Bing doesn't map any image to the keywords.
   */
  public List<String> getBestImage(int analysationDepth, int extractions) throws IOException {
    List<ImageDetails> bestImages = new ArrayList<>();
    int remainingSearches = MAX_NO_QUERIES;
    StringBuilder bestImage = new StringBuilder("");

    for (String[] keywords : keywordQueries) {
      if (remainingSearches == 0) {
        break;
      }

      List<String> imageSources = new ArrayList<>();

      Document doc = Jsoup.connect(generateSearchUrl(keywords, PUBLIC_LICENSE_FILTER)).userAgent(USER_AGENT).get();
      Elements elements = doc.getElementsByTag("img");

      for (Element element : elements) {
        imageSources.add(element.attr("abs:data-src"));
      }
      
      
      ImageScorer imageScorer = new ImageScorer(keywords); 

      imageSources.removeAll(Arrays.asList("", null));
      float firstScore = findBestImage(imageScorer, bestImage, imageSources, analysationDepth);  
      bestImages.add(new ImageDetails(bestImage.toString(), firstScore));

      // Analyse the rest of images using the other license.
      if (imageSources.size() < analysationDepth) {
        int remainingAnalysations = analysationDepth - imageSources.size();
        imageSources.clear();
        doc = Jsoup.connect(generateSearchUrl(keywords, USE_SHARE_FILTER)).userAgent(USER_AGENT).get();
        elements = doc.getElementsByTag("img");

        for (Element element : elements) {
          imageSources.add(element.attr("abs:data-src"));
        }

        float secondScore = findBestImage(imageScorer, bestImage, imageSources, analysationDepth);
        if (secondScore > firstScore) {
            bestImages.remove(bestImages.size() - 1);
            bestImages.add(new ImageDetails(bestImage.toString(), secondScore));
        }
      }
      --remainingSearches;
    }


    return getUrls(bestImages, extractions);
  }
}

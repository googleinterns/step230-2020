package com.google.sps.image;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageSelection {

  private List<String> keywords;

  private String searchUrl;

  private static final String userAgent = "Mozilla/5.0 (X11; CrOS x86_64 13099.85.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.110 Safari/537.36";
  
  private static final String bingUrl = "https://www.bing.com/images/search?q=";

  private static final String bingQueryParam = "&qs=HS&form=QBIR&scope=images&sp=-1&pq=hap&sc=8-3&cvid=44CA4B129FEF4B93B6F764BD083213D3&first=1&scenario=ImageBasicHover";

  private static String generateSearchUrl(List<String> keywords) {
    String searchUrl = new String(bingUrl);

    for (String word : keywords) {
      searchUrl = searchUrl + "+" + word.replaceAll(" ", "+");
    }
    searchUrl = searchUrl + bingQueryParam;

    return searchUrl; 
  }

  public ImageSelection(List<String> keywords) {
    this.keywords = keywords;
    searchUrl = generateSearchUrl(keywords);
  }

  public String getBestImage() {
    List<String> imgSrc = new ArrayList<>();

    try {
      Document doc = Jsoup.connect(this.searchUrl).userAgent(this.userAgent).get();
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
    } catch (IOException e) { 
      e.printStackTrace();
    }
    
    return "";
  }
}

package com.google.sps.data;

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

  private static final String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36";
  
  private static final String bingUrl = "https://www.bing.com/images/search?q=";

  private static final String bingQueryParam = "&qs=n&form=QBIR&sp=-1&pq=pariu&sc=8-5&cvid=2E88E64A4CA142D2AD70842A37EEA1BF&first=1&scenario=ImageBasicHover";

  private static String generateSearchUrl(List<String> keywords) {
    String searchUrl = new String(bingUrl);
    
    for (String word : keywords) {
      searchUrl = searchUrl + "+" + word;
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
        imgSrc.add(element.attr("abs:src"));
      }

      // First 10 images are useless logos from bing.com
      return imgSrc.get(10);
    } catch (IOException e) { 
      e.printStackTrace();
    }
    
    return "";
  }
}
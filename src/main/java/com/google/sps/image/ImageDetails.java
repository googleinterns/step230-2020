package com.google.sps.image;

public final class ImageDetails {

  private final String url;

  private final float score;

  public ImageDetails(String url, float score) {
    this.url = url;
    this.score = score;
  }

  public String getUrl() {
    return url;
  }

  public float getScore() {
    return score;
  }
}

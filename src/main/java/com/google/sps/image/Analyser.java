package com.google.sps.image;

import java.util.List;

public interface Analyser {

  /**
   * Analyses the elements inside the picture.
   * @param   imageUrl Link of image that needs to be analysed.
   * @return  List of elements that appear in the picture.
   */
  public void analyse(String imageUrl);
}

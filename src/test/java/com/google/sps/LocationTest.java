package com.google.sps.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@RunWith(JUnit4.class)
public final class LocationTest {
  // This is how every Bing image link starts
  private static final double latitude = 3.768;
  private static final double longitude = 5.888888;

  private void assertLongitude(String location) throws IOException {
    Location loc = new Location(location);
    double actualLongitude = loc.getLongitude();
    Assert.assertEquals(this.longitude, actualLongitude, 0.5);
  }

  private void assertLatitude(String location) throws IOException {
    Location loc = new Location(location);
    double actualLatitude = loc.getLatitude();
    Assert.assertEquals(this.latitude, actualLatitude, 0.5);
  }

  @Test
  public void parseLongitude() throws IOException {
    String test = "Latitude: 3.768" + "\n" + "Longitude: 5.888888";
    assertLongitude(test);
  }

  @Test
  public void parseLatitude() throws IOException {
    String test = "Latitude: 3.768" + "\n" + "Longitude: 5.888888";
    assertLatitude(test);
  }

}
package com.google.sps.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RunWith(JUnit4.class)
public final class TextAnalyserTest {

  @Test
  public void analysePositiveSentiment() {
    try {
      TextAnalyser textAnalyser = new TextAnalyser("Today is a special day. It is your birthday, so enjoy it!");
      assertTrue("Sentiment score is not positive.", textAnalyser.getSentimentScore() > 0.0F);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void analyseNegativeSentiment() {
    try {
      TextAnalyser textAnalyser = new TextAnalyser("I don't like this.");
      assertTrue("Sentiment score is not negative.", textAnalyser.getSentimentScore() < 0.0F);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void analysePositiveMood() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Happy Birthday!");
    String expected = "thrilled";
    String actual = textAnalyser.getMood();

    assertEquals(expected, actual);
  }

  @Test
  public void analyseNegativeMood() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Smoking, well, there is no healthy amount of smoking." +
                                                  "Healthy here meant never having smoked.");
    String expected = "depressed";
    String actual = textAnalyser.getMood();

    assertEquals(expected, actual);
  }

  @Test
  public void testForCorrectEvents() {
    TextAnalyser textAnalyser = new TextAnalyser("Today is my Birthday. Today you got a job promotion.");
    Set<String> expected = new HashSet<>();
    Set<String> actual = textAnalyser.getEvents();

    expected.add("birthday");
    expected.add("job");
    expected.add("promotion");

    assertEquals(expected, actual);
  }

  @Test
  public void testEntityThatReturnsLocation() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("I am going to Paris this weekend.");
    assertTrue("There is no city displayed.", textAnalyser.getEntities().contains("paris"));
  }

  @Test
  public void testEntityThatReturnsName() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Hello, Mark!");
    assertTrue("There is no name displayed", textAnalyser.getEntities().contains("mark"));
  }

  @Test
  public void testEntityThatReturnsEvent() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("My wedding is coming soon!");
    assertTrue("There is no event displayed.", textAnalyser.getEntities().contains("wedding"));
  }

  @Test
  public void testOnlyLowerCaseKeyWords() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Hello, Mark! My Birthday is coming very soon and I want to" +
                                                  " invite you to my Party! It is in Paris. It is not expensive." +
                                                  " We will have so much fun! All of our friends will also come to the party");
    Set<String> expected = new HashSet<>();
    Set<String> actual = textAnalyser.getKeyWords();

    for(String keyWord : actual) {
      expected.add(keyWord.toLowerCase());
    }

    assertEquals(expected, actual);
  }

  @Test
  public void testCorrectKeyWords() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Happy Birthday! We will go to Paris!");
    Set<String> expected = new HashSet<>();
    Set<String> actual = textAnalyser.getKeyWords();

    expected.add("birthday");
    expected.add("paris");
    expected.add("serene");

    assertEquals(expected, actual);
  }

  @Test
  public void testEnoughWordsToGetCategory() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("A computer is a machine that can be instructed to carry" +
                                                 " out sequences of arithmetic or logical operations automatically" +
                                                 " via computer programming. Modern computers have the ability to" +
                                                 " follow generalized sets of operations, called programs.");
    assertTrue("Couldn't get the correct category.", textAnalyser.getCategories().contains("computers & electronics"));
  }

  @Test
  public void testNotEnoughWordsToGetCategory() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Hello!");
    Set<String> expected = new HashSet<>();
    Set<String> actual = textAnalyser.getCategories();

    assertEquals(expected, actual);
  }
}
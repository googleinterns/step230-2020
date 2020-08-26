package com.google.sps.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
* This class tests the functions from the TextAnalyser class.
*
* The format of a test name is:
* 1) conditions
* 2) what is the subject being tested (the input)
* 3) expected results (the output)
* These concepts are separated by _ e.g. conditions_input_output
**/

@RunWith(JUnit4.class)
public final class TextAnalyserTest {

  @Test
  public void analyse_positiveText_positiveSentimentScore() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Today is a special day. It is your birthday, so enjoy it!");
    assertTrue("Sentiment score is not positive.", textAnalyser.getSentimentScore() > 0.0F);
  }

  @Test
  public void analyse_negativeText_negativeSentimentScore() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("I don't like this.");
    assertTrue("Sentiment score is not negative.", textAnalyser.getSentimentScore() < 0.0F);
  }

  @Test
  public void analyse_positiveText_positiveMood() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Happy Birthday!");

    assertEquals("thrilled", textAnalyser.getMood());
  }

  @Test
  public void analyse_negativeText_negativeMood() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Smoking, well, there is no healthy amount of smoking." +
                                                  "Healthy here meant never having smoked.");

    assertEquals("depressed", textAnalyser.getMood());
  }

  @Test
  public void analyse_textWithEvents_eventsPresent() {
    TextAnalyser textAnalyser = new TextAnalyser("Today is my Birthday. Today you got a job promotion.");
    Set<String> expected = new HashSet<>();
    Set<String> actual = textAnalyser.getEvents();

    expected.add("birthday");
    expected.add("job");
    expected.add("promotion");

    assertEquals(expected, actual);
  }

  @Test
  public void analyse_textWithLocation_locationPresent() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("I am going to Paris this weekend.");
    assertTrue("There is no city displayed.", textAnalyser.getEntities().contains("paris"));
  }

  @Test
  public void analyse_textWithName_namePresent() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Hello, Mark!");
    assertTrue("There is no name displayed", textAnalyser.getEntities().contains("mark"));
  }

  @Test
  public void analyse_textWithEventEntity_eventPresent() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("My wedding is coming soon!");
    assertTrue("There is no event displayed.", textAnalyser.getEntities().contains("wedding"));
  }

  @Test
  public void toLowerCase_KeyWords_lowerCaseKeyWords() throws IOException {
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
  public void analyse_textContainingKeyWords_notEmptySet() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Happy Birthday! We will go to Paris!");
    assertFalse(textAnalyser.getKeyWords().isEmpty());
  }

  @Test
  public void analyse_textWithEnoughWordsToGetCategory_theCategory() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("A computer is a machine that can be instructed to carry" +
                                                 " out sequences of arithmetic or logical operations automatically" +
                                                 " via computer programming. Modern computers have the ability to" +
                                                 " follow generalized sets of operations, called programs.");
    assertTrue("Couldn't get the correct category.", textAnalyser.getCategories().contains("computers & electronics"));
  }

  @Test
  public void analyse_textWithNotEnoughWordsToGetCateogry_emptySet() throws IOException {
    TextAnalyser textAnalyser = new TextAnalyser("Hello!");

    assertEquals(Collections.emptySet(), textAnalyser.getCategories());
  }

  @Test
  public void checkHtmlInjection_HTMLCode_htmlInjection() {
    TextAnalyser textAnalyser = new TextAnalyser(
        "<html> <h1>Here are the results that match your query: </h1>" +
        "<h2>{user-query}</h2><ol><li>Result A<li>Result B</ol></html>"
    );

    assertEquals("html-injection", textAnalyser.checkInjection());
  }

  @Test
  public void checkHtmlInjection_noHTMLCode_noHtmlInjection() {
    TextAnalyser textAnalyser = new TextAnalyser("This is a code that doesn't contain html.");
    assertEquals("no-html-injection", textAnalyser.checkInjection());
  }
}
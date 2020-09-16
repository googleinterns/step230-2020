package com.google.sps.data;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public final class ParseMailsTest {
    @Test
    public void parseCorrect() {
        String str = "katya@yandex.ru second_str";
        List<String> expected = new ArrayList<String>();
        expected.add("katya@yandex.ru");
        expected.add("second_str");
        ParseMails parser = new ParseMails(str);
        List<String> real = parser.getMails();
        assertEquals(expected, real);
    }
}
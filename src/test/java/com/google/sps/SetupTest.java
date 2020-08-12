package com.google.sps;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SetupTest {

    @Test
    public void setupTest() {
		  Setup setup = new Setup();
      assertEquals("Hello World!", setup.echo());
    }
}
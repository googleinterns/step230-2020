package com.google.sps;

/**
 * Repository setup.
 */
public class Setup {
  public static String echo() {
    return "Hello World!";
  }
  public static void main(String[] args) {
    Setup setup = new Setup();
    System.out.println(setup.echo());
  }
}
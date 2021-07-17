package com.lapha.logics;

import java.io.IOException;
import java.net.URL;

public class Utils {

  public static void validateUserName(String userName) throws Exception {
    boolean invalid =
        userName == null || userName.length() == 0 || !userName.matches("[A-Za-z0-9_]+");
    if (invalid) {
      throw new Exception("Invalid username provided.");
    }
  }

  public static void validateUrl(String urlToValidate) throws IOException {
    URL url = new URL(urlToValidate);
  }
}

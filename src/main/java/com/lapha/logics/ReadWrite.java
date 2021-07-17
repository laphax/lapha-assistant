package com.lapha.logics;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadWrite {

  public static void write(String file, JSONObject jsonObject) throws IOException {
    try (FileWriter writer = new FileWriter(file)) {
      jsonObject.write(writer);
    }
  }

  public static JSONObject read(String file) throws IOException {
    try (FileReader reader = new FileReader(file)) {
      JSONTokener jsonTokener = new JSONTokener(reader);
      return new JSONObject(jsonTokener);
    }
  }
}

package com.lapha;

import com.formdev.flatlaf.FlatLightLaf;
import com.lapha.frames.Main;

import java.awt.*;

public class Lapha {

  public static void main(String[] args) {
    FlatLightLaf.install();
    EventQueue.invokeLater(() -> {
      Main main = Main.getMain();
      main.defaultShow(null);
    });
  }
}
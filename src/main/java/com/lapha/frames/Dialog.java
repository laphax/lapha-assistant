package com.lapha.frames;

import javax.swing.*;
import java.awt.*;

public class Dialog extends JDialog {

  public Dialog() {
    super();
  }

  public void defaultShow() {
    defaultShow(null);
  }

  public void defaultShow(Component parent) {
    this.setResizable(false);
    this.setLocationRelativeTo(parent);
    this.pack();
    this.setVisible(true);
  }
}

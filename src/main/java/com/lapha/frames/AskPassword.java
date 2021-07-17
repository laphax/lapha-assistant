package com.lapha.frames;


import javax.swing.*;
import java.awt.*;

public class AskPassword extends Dialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JPasswordField passwordField1;

  private char[] pass = null;

  public AskPassword() {
    super();
    this.setTitle("Ask Password");
    this.setContentPane(contentPane);

    this.setModal(true);
    this.getRootPane().setDefaultButton(buttonOK);
    this.buttonOK.addActionListener(e -> {
      char[] password = this.passwordField1.getPassword();
      if (password == null || password.length == 0) {
        Message.showMessage("Invalid Password.", TrayIcon.MessageType.WARNING, this);
      } else {
        pass = password;
        this.dispose();
      }
    });
  }

  public char[] getPass() {
    return pass;
  }
}

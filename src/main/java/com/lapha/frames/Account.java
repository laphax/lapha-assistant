package com.lapha.frames;

import com.lapha.logics.ReadWrite;
import com.lapha.logics.Utils;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Account extends Frame {

  final static String hostKey = "host";
  final static String userNameKey = "username";
  final static String file = "orange.hrms.json";
  static JSONObject jsonObject = null;

  // init
  private JTextField hostTextField;
  private JTextField userNameTextField;
  private JPanel accountPanel;
  private JButton saveButton;

  public Account() {
    super();
    this.setTitle("Orange Account");
    this.setContentPane(accountPanel);

    saveButton.addActionListener(e -> {
      String host = hostTextField.getText();
      String userName = userNameTextField.getText();

      String errorMessage = "";
      try {
        Utils.validateUrl(host);
      } catch (MalformedURLException malformedURLException) {
        errorMessage = "Host url is invalid.";
      } catch (IOException ioException) {
        errorMessage = "Host url validation failed.";
      }

      try {
        Utils.validateUserName(userName);
      } catch (Exception exception) {
        errorMessage = "Unacceptable username.";
      }

      if (errorMessage.length() > 0) {
        Message.showMessage(errorMessage, TrayIcon.MessageType.ERROR, this);
      } else {
        // now save
        JSONObject object = new JSONObject();
        object.put(hostKey, host);
        object.put(userNameKey, userName);
        try {
          boolean updateMainMenu = !Account.haveAccount();
          ReadWrite.write(file, object);
          jsonObject = object;
          Message.showMessage("Success.", TrayIcon.MessageType.INFO, this);
          this.dispose();
          PunchInOut punchInOut = new PunchInOut();
          punchInOut.defaultShow(this);

          // menu update
          if(updateMainMenu){
            Main.getMain().updateMenuBar();
          }
        } catch (Exception exception) {
          errorMessage = "Failed to save.";
          Message.showMessage(errorMessage, TrayIcon.MessageType.ERROR, this);
        }
      }
    });

    try {
      read();
      this.hostTextField.setText(jsonObject.getString(hostKey));
      this.userNameTextField.setText(jsonObject.getString(userNameKey));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public static boolean haveAccount() {
    return new File(file).exists();
  }

  private static String getValue(String key) {
    try {
      if (jsonObject == null) {
        if (haveAccount()) {
          read();
          if (jsonObject != null) {
            return jsonObject.getString(key);
          }
        }
      } else {
        return jsonObject.getString(key);
      }
    } catch (Exception ignored) {
    }
    return null;
  }

  public static String getHost() {
    return getValue(hostKey);
  }

  public static String getUserName() {
    return getValue(userNameKey);
  }

  public static void read() throws Exception {
    if (haveAccount()) {
      JSONObject temp = ReadWrite.read(file);
      Utils.validateUrl(temp.getString(hostKey));
      Utils.validateUserName(temp.getString(userNameKey));
      jsonObject = new JSONObject();
      jsonObject.put(hostKey, temp.getString(hostKey));
      jsonObject.put(userNameKey, temp.getString(userNameKey));
    } else {
      throw new Exception("Orange HRMS account is not found.");
    }
  }

}

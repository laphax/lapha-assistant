package com.lapha.frames;

import com.lapha.logics.hrms.Orange;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.Map;

public class PunchInOut extends Frame {
  private JPanel punchInOutPanel;
  private JLabel punchInOutSatusLabel;
  private JLabel punchInOutStatusValueLabel;
  private JTextArea puncInOutTextArea;
  private JButton getStatusButton;
  private JButton punchInOutButton;

  private String password = null;
  private final Orange orange = new Orange(Account.getHost());

  public PunchInOut() throws URISyntaxException {

    super();
    this.setTitle("Punch In/Out");
    this.setContentPane(punchInOutPanel);

    this.getStatusButton.addActionListener(
        e -> {
          // set check true
          this.punchInOutStatusValueLabel.setText("Checking...");
          this.punchInOutButton.setEnabled(false);

          if (this.password == null) {
            AskPassword askPassword = new AskPassword();
            askPassword.defaultShow(this);
            char[] password = askPassword.getPass();
            this.password = new String(password);
          }

          try {
            this.orange.setUserName(Account.getUserName());
            this.orange.setPassword(password);

            Map<String, String> cookies = orange.getCookies();
            boolean punchedInStatus = orange.getPunchedInStatus(cookies);
            if (punchedInStatus) {
              punchInOutStatusValueLabel.setText("Punched In");
              punchInOutButton.setText("Out");
            } else {
              punchInOutStatusValueLabel.setText("Punched Out");
              punchInOutButton.setText("In");
            }
            punchInOutButton.setEnabled(true);
          } catch (Exception exception) {
            punchInOutStatusValueLabel.setText("Unknown");
            punchInOutButton.setEnabled(false);
            password = null;
          }
        });

    // now punch in out
    punchInOutButton.addActionListener(
        e -> {
          String note = puncInOutTextArea.getText();
          boolean toPunchIn = punchInOutButton.getText().equalsIgnoreCase("In");
          try {
            if (toPunchIn) {
              if (orange.punchIn(note)) {
                punchInOutButton.setText("Out");
                punchInOutStatusValueLabel.setText("Punched In");
              }
            } else {
              if (orange.punchOut(note)) {
                punchInOutButton.setText("In");
                punchInOutStatusValueLabel.setText("Punched Out");
              }
            }
          } catch (Exception exception) {
            punchInOutButton.setEnabled(false);
          }
        });
  }
}

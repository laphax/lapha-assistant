package com.lapha.frames;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Message extends Dialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JLabel messageLabel;
  private JLabel messageTypeLabel;

  public Message() {
    super();
    setModal(true);
    setContentPane(contentPane);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> onOK());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOK() {
    // add your code here
    dispose();
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  public static void showMessage(String message, TrayIcon.MessageType messageType, Component parent) {
    Message dialog = new Message();
    dialog.setTitle(messageType.name());
    dialog.setMinimumSize(new Dimension(200, 80));
    dialog.messageTypeLabel.setText(messageType.name());
    dialog.messageLabel.setText(message);
    dialog.defaultShow(parent);
  }

}

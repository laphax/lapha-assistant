package com.lapha.frames;

import com.lapha.logics.hrms.Orange;
import com.lapha.logics.schedules.Scheduler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

class MyLabel extends JLabel {

  public MyLabel() {
    super("", null, LEADING);
  }

  @Override
  public boolean isOpaque() {
    return true;
  }
}

public class Main extends Frame {

  private boolean enableReminder = true;
  static final Main main = new Main();

  private Main() {
    super();
    createUI();
  }

  public static Main getMain() {
    return main;
  }

  private void createUI() {

    this.createMenuBar();

    Color[] colors = {
      Color.black,
      Color.blue,
      Color.cyan,
      Color.gray,
      Color.green,
      Color.lightGray,
      Color.magenta,
      Color.orange,
      Color.pink,
      Color.red,
      Color.white,
      Color.yellow
    };

    List<JLabel> labels = new ArrayList<>(colors.length);
    for (Color color : colors) {
      JLabel label = createColoredLabel(color);
      labels.add(label);
    }

    createLayout(labels.toArray(new JLabel[0]));

    this.setTitle("Lapha");
    this.setSize(400, 185);
    this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    this.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            if (enableReminder) {
              Scheduler scheduler = Scheduler.getInstance();
              boolean result = scheduler.scheduleDefault();
              if (result) {
                Notification.showTrayNotification(main, "Default reminders are scheduled.");
              }
            }
          }
        });
  }

  private void createMenuBar() {

    // this is main menu bar
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Lapha");

    // Reminder
    JMenu reminderMenu = new JMenu("Reminders");
    // defaults
    JMenu defaultReminders = new JMenu("Default");
    // Stay hydrated
    JMenuItem dSHReminder = new JMenuItem("Reminder: Drink Water");
    dSHReminder.addActionListener(e -> {});
    dSHReminder.setEnabled(false);
    defaultReminders.add(dSHReminder);
    // Stay Active
    JMenuItem dSAReminder = new JMenuItem("Reminder: Body position");
    dSAReminder.addActionListener(e -> {});
    dSAReminder.setEnabled(false);
    defaultReminders.add(dSAReminder);
    reminderMenu.add(defaultReminders);
    // custom user can add.
    JMenuItem customReminders = new JMenuItem("Custom");
    customReminders.setEnabled(false);
    reminderMenu.add(customReminders);
    menu.add(reminderMenu);

    // for HRMS Support.
    JMenu hrmsMenu = new JMenu("HRMS");

    // sub item
    if (Account.haveAccount()) {
      JMenu orangeHrMenu = new JMenu("Orange HRMS");
      // edit
      JMenuItem editAccount = new JMenuItem("Edit Account");
      editAccount.addActionListener(
          e -> {
            Account account = new Account();
            account.defaultShow();
          });
      orangeHrMenu.add(editAccount);

      // punch in /out
      JMenuItem puchInOutItem = new JMenuItem("Punch In/Out");
      puchInOutItem.addActionListener(
          e -> {
            try {
              PunchInOut punchInOut = new PunchInOut();
              punchInOut.defaultShow(this);
            } catch (URISyntaxException uriSyntaxException) {
              Message.showMessage("Error", TrayIcon.MessageType.ERROR, this);
            }
          });
      orangeHrMenu.add(puchInOutItem);
      hrmsMenu.add(orangeHrMenu);
    } else {
      JMenuItem orangeHrMenuItem = new JMenuItem("Orange HRMS");
      orangeHrMenuItem.addActionListener(
          e -> {
            try {
              Orange.hrmsAuto(this);
            } catch (URISyntaxException uriSyntaxException) {
              uriSyntaxException.printStackTrace();
              Message.showMessage(
                  uriSyntaxException.getMessage(), TrayIcon.MessageType.ERROR, this);
            }
          });
      hrmsMenu.add(orangeHrMenuItem);
    }

    menu.add(hrmsMenu);

    // add to menu
    menu.addSeparator();

    // Exit
    JMenuItem exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
    exitMenuItem.setToolTipText("Exit Application");
    exitMenuItem.addActionListener((e -> exit()));
    menu.add(exitMenuItem);

    menuBar.add(menu);
    setJMenuBar(menuBar);
    pack();
  }

  public void updateMenuBar() {
    main.createMenuBar();
  }

  private JLabel createColoredLabel(Color color) {
    JLabel label = new MyLabel();
    label.setMinimumSize(new Dimension(90, 40));
    label.setBackground(color);
    label.setToolTipText(color.toString());
    return label;
  }

  private void createLayout(JLabel[] labels) {
    Container container = getContentPane();
    GroupLayout gl = new GroupLayout(container);
    container.setLayout(gl);
    //
    gl.setAutoCreateContainerGaps(true);
    gl.setAutoCreateGaps(true);

    gl.setHorizontalGroup(
        gl.createParallelGroup()
            .addGroup(
                gl.createSequentialGroup()
                    .addComponent(labels[0])
                    .addComponent(labels[1])
                    .addComponent(labels[2])
                    .addComponent(labels[3]))
            .addGroup(
                gl.createSequentialGroup()
                    .addComponent(labels[4])
                    .addComponent(labels[5])
                    .addComponent(labels[6])
                    .addComponent(labels[7]))
            .addGroup(
                gl.createSequentialGroup()
                    .addComponent(labels[8])
                    .addComponent(labels[9])
                    .addComponent(labels[10])
                    .addComponent(labels[11])));

    gl.setVerticalGroup(
        gl.createSequentialGroup()
            .addGroup(
                gl.createParallelGroup()
                    .addComponent(labels[0])
                    .addComponent(labels[1])
                    .addComponent(labels[2])
                    .addComponent(labels[3]))
            .addGroup(
                gl.createParallelGroup()
                    .addComponent(labels[4])
                    .addComponent(labels[5])
                    .addComponent(labels[6])
                    .addComponent(labels[7]))
            .addGroup(
                gl.createParallelGroup()
                    .addComponent(labels[8])
                    .addComponent(labels[9])
                    .addComponent(labels[10])
                    .addComponent(labels[11])));

    pack();
  }

  public boolean isEnableReminder() {
    return enableReminder;
  }

  public void setEnableReminder(boolean enableReminder) {
    this.enableReminder = enableReminder;
  }

  public void exit() {
    Scheduler.getInstance().cancel();
    System.exit(0);
  }
}

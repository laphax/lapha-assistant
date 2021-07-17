package com.lapha.frames;


import com.lapha.logics.Ref;
import com.lapha.logics.schedules.Scheduler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

public class Notification {

  private static TrayIcon trayIcon;

  static {
    try {
      if(SystemTray.isSupported()) {
        SystemTray tray = SystemTray.getSystemTray();
        // popup menu
        PopupMenu popupMenu = new PopupMenu();
        // show
        MenuItem showMenuItem = new MenuItem("Show");
        showMenuItem.addActionListener( e -> {
          Main.getMain().setVisible(true);
        });
        popupMenu.add(showMenuItem);
        // Reminders
        // need for toggle
        Ref<Boolean> enableNextReminder = new Ref<>(true);
        MenuItem enableDisableNotificationMenuItem = new MenuItem("Disable Reminders");
        enableDisableNotificationMenuItem.addActionListener( e -> {
          if(enableNextReminder.getRef()) {
            Scheduler.getInstance().cancel();
            enableDisableNotificationMenuItem.setLabel("Enable Reminders");
          } else {
            Scheduler.getInstance().scheduleDefault();
            enableDisableNotificationMenuItem.setLabel("Disable Reminders");
          }
          enableNextReminder.setRef(!enableNextReminder.getRef());
        });
        popupMenu.add(enableDisableNotificationMenuItem);
        //maybe exit too
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.addActionListener( e -> Main.getMain().exit());
        popupMenu.add(exitMenuItem);

        Image image = ImageIO.read(Objects.requireNonNull(Main.getMain().getClass().getClassLoader().getResource("icons/lapha.png")));
        trayIcon = new TrayIcon(image, "Lapha", popupMenu);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
      }
    } catch (Exception e){
      trayIcon = null;
    }
  }

  public static void showTrayNotification(Component parent, String message) {
    boolean tray = trayIcon != null;
    if(tray){
      try {
        trayIcon.displayMessage("Notification", message, TrayIcon.MessageType.INFO);
        tray = true;
      } catch (Exception e){
        tray = false;
      }
    }

    if(!tray){
      Message.showMessage(message, TrayIcon.MessageType.INFO, parent);
    }
  }
}

package com.lapha.logics.schedules;

import com.lapha.frames.Notification;

import java.util.TimerTask;

public class ChangePosition extends TimerTask {

  @Override
  public void run() {
    Notification.showTrayNotification(
        null, "Stay Healthy. It is recommended to change body position at every 45 minutes.");
  }
}

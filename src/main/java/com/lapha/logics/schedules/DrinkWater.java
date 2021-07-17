package com.lapha.logics.schedules;

import com.lapha.frames.Notification;

import java.util.TimerTask;

public class DrinkWater extends TimerTask {
  @Override
  public void run() {
    Notification.showTrayNotification(null, "Stay Hydrated. It is recommended to drink a glass of water at every hour.");
  }
}

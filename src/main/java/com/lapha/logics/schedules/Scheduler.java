package com.lapha.logics.schedules;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {

  // isScheduledDefault
  private static Timer timer;
  private static boolean scheduledDefault = false;
  private static final Scheduler scheduler = new Scheduler();

  private Scheduler() {}

  public static Scheduler getInstance() {
    return scheduler;
  }

  private void ensureTimer() {
    if (timer == null) {
      timer = new Timer("lapha-reminder-timer");
    }
  }

  public void schedule(TimerTask timerTask, long delay, long period) {
    if (period % 5 != 0) {
      return;
    }
    ensureTimer();
    timer.scheduleAtFixedRate(timerTask, delay, period);
  }

  public boolean scheduleDefault() {
    if (scheduledDefault) {
      return false;
    }
    // every 1 hour
    DrinkWater drinkWater = new DrinkWater();
    schedule(drinkWater, 60 * 60 * 1000, 60 * 60 * 1000);
    // every 45 minutes
    ChangePosition changePosition = new ChangePosition();
    schedule(changePosition, 45 * 60 * 1000, 45 * 60 * 1000);
    scheduledDefault = true;
    return true;
  }

  public static boolean isScheduledDefault() {
    return scheduledDefault;
  }

  public void cancel() {
    if (timer != null) {
      timer.cancel();
      timer.purge();
      timer = null;
    }
    scheduledDefault = false;
  }
}

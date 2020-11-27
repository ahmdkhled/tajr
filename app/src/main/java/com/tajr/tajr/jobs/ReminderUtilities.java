package com.tajr.tajr.jobs;


import android.content.Context;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by aviator on 16/03/18.
 */

public class ReminderUtilities {

    private static final String REMINDER_JOB_TAG = "hydration_reminder_tag";
    private static FirebaseJobDispatcher dispatcher;

    private static boolean sInitialized;
    private static Driver driver;

    private static int interval_time;

    synchronized public static void scheduleOrderReminder(Context context) {

        driver = new GooglePlayDriver(context);
        dispatcher = new FirebaseJobDispatcher(driver);


        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(OrderReminderJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(60*15,60*16))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(constraintReminderJob);
        sInitialized = true;
    }

    synchronized public static void cancelOrdersReminder() {
        driver.cancelAll();
    }

    public static boolean getDriver() {
        return driver.isAvailable();
    }
}
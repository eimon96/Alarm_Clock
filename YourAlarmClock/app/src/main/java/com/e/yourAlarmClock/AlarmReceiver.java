package com.e.yourAlarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context k1, Intent k2) {

        Intent intent = new Intent(k1, Blue_Screen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        k1.startActivity(intent);

        // NEEDS ALLOW ON TOP PERMISSION FROM THE USER

    }

}
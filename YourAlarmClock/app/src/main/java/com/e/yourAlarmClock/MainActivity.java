package com.e.yourAlarmClock;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Notification.FLAG_NO_CLEAR;



public class MainActivity extends AppCompatActivity {


    private static final String TAG = "DEDBUG";
    private NumberPicker pickerHour;
    private NumberPicker pickerMin;
    private final String[] HOURS = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private final String[] MINUTES = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
            "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
    private String wra; private String lepta; // for the first time app used and for the x button
    private final String defaultwra = "09";
    private final String defaultlepta = "06";
    private TextView mAlarm;
    public static final int MY_NOTIFICATION_ID = 1;
    private PendingIntent notifIntent;
    private long[] mVibratePattern = {0, 200, 200, 300};
    public static NotificationManager mNotificationManager;
    private Spinner spin;
    private String item; private String retrieveItem;
    private ArrayAdapter<String> dataAdapter;
    public static boolean canc;
    public static final int res_code = 1;

    public static final String FILE_NAME = "prefs.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlarm = findViewById(R.id.NoAlarms);

        spin = findViewById(R.id.spinnerSongs);

        List<String> songs = new ArrayList<String>();
        songs.add("Beat 1");  //dark
        songs.add("Beat 2");  //tlou
        songs.add("Beat 3");  //beat1
        songs.add("Beat 4");  //beat2
        songs.add("Beat 5");  //re

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, songs);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);


        //NumberPickers
        pickerHour = findViewById(R.id.hour_picker);
        pickerMin = findViewById(R.id.minute_picker);
        pickerHour.setMaxValue(23); pickerHour.setMinValue(0);
        pickerMin.setMaxValue(59); pickerMin.setMinValue(0);
        pickerHour.setDisplayedValues(HOURS);
        pickerMin.setDisplayedValues(MINUTES);

        wra = defaultwra; lepta = defaultlepta;
        pickerHour.setValue(9); pickerMin.setValue(6);


        pickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //null
            }
        });
        pickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //null
            }
        });


        Intent mNotificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notifIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                mNotificationIntent, FLAG_NO_CLEAR);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        FloatingActionButton fab = findViewById(R.id.floatingb);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                mAlarm.setText("Alarm set: " + HOURS[pickerHour.getValue()] + ":" +  MINUTES[pickerMin.getValue()]
                        + "\n" + "\t" + "\t" + "\t" + item);
                NotificationN();

                canc = false;
                AlarmOK(Integer.parseInt(HOURS[pickerHour.getValue()]), Integer.parseInt(MINUTES[pickerMin.getValue()]), canc);

            }

        });

        FloatingActionButton fab2 = findViewById(R.id.floatingb2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                wra = defaultwra; lepta = defaultlepta;
                pickerHour.setValue(9); pickerMin.setValue(6);
                mAlarm.setText("No Alarm Yet");
                mNotificationManager.cancel(MY_NOTIFICATION_ID);

                item = spin.getItemAtPosition(0).toString();
                dataAdapter.notifyDataSetChanged();
                spin.setAdapter(dataAdapter);

                canc = true;
                AlarmOK(Integer.parseInt(HOURS[pickerHour.getValue()]), Integer.parseInt(MINUTES[pickerMin.getValue()]), canc);



                Blue_Screen.mMediaPlayer.stop();




            }}
        );

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //null
            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dotmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem about) {
        int id = about.getItemId();
        if(id == R.id.about){
            Intent iAbout = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(iAbout);
            return true;
        }
        return super.onOptionsItemSelected(about);
    }


    private void NotificationN() {

        Notification.Builder notificationBuilder = new Notification.Builder(
                getApplicationContext())
                .setContentTitle("Alarm Set")
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(false)
                .setContentIntent(notifIntent)
                .setSound(null)
                .setVibrate(mVibratePattern)
                .setContentText(HOURS[pickerHour.getValue()] + ":" + MINUTES[pickerMin.getValue()])
                .setOngoing(true);


        // For Android 10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "9696";
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Alarm Set", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());
    }



    private void savePrefs() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));


            Integer nn = pickerHour.getValue();
            writer.println(nn);

            Integer mm = pickerMin.getValue();
            writer.println(mm);

            writer.println(item);

            String ss = mAlarm.getText().toString();
            writer.println(ss);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    private void loadPrefs() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));


            String ss;

            ss = reader.readLine();
            Integer nn = Integer.parseInt(ss);
            pickerHour.setValue(nn);

            ss = reader.readLine();
            Integer mm = Integer.parseInt(ss);
            pickerMin.setValue(mm);

            ss = reader.readLine();
            retrieveItem = ss;

            ss = reader.readLine();
            if (!ss.equals("No Alarm Yet")) {
                ss += ("\n" + "\t" + "\t" + "\t" + retrieveItem);
            }
            mAlarm.setText(ss);


        } catch (IOException e) {
            e.printStackTrace();
            } finally {
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        File f = getFileStreamPath(FILE_NAME);
        if (f.length() != 0) {
            loadPrefs();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        savePrefs();
    }





    private void AlarmOK(int hour, int minute, boolean canc){
        Calendar now = Calendar.getInstance();
        Calendar calSet = (Calendar) now.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(now) <= 0) {
            //count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), res_code, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);


        if (canc) {
            alarmManager.cancel(pendingIntent);
        }

    }

}
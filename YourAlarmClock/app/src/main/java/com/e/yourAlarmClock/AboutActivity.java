package com.e.yourAlarmClock;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);
        TextView keimeno = findViewById(R.id.keimeno);
        keimeno.setMovementMethod(LinkMovementMethod.getInstance());

    }
}


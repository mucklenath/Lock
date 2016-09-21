package com.example.lockr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.content.Context;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat mSwitchd = null;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
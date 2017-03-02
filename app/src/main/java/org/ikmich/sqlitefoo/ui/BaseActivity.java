package org.ikmich.sqlitefoo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cog.android.Alerter;
import cog.android.Toaster;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void toast(Object message) {
        Toaster.toast(this, message.toString());
    }

    protected void alert(Object message) {
        Alerter.alert(this, message);
    }
}

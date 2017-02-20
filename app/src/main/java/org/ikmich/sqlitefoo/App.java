package org.ikmich.sqlitefoo;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 *
 */
public class App extends Application {
    private static App app;

    public App() {
        app = this;
    }

    public static Context getContext() {
        return app.getApplicationContext();
    }

    public static void toast(Object msg) {
        Toast.makeText(getContext(), msg.toString(), Toast.LENGTH_SHORT).show();
    }
}

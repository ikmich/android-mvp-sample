package org.ikmich.sqlitefoo;

import android.app.Application;
import android.content.Context;

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
}

package de.httptandooripalace.restaurantorderprinter;

import android.app.Application;
import android.content.res.Configuration;

import helpers.SharedPrefHelper;

/**
 * Created by uizen on 3/8/2017.
 */

public class MainApplication extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefHelper.deleteSharedPrefs(getApplicationContext());

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}

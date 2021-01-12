package com.balran.deliveryapp.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String APP_SETTINGS_FILE = "APP_SETTINGS";

    private SharedPreferencesManager(){}

    private static SharedPreferences getSharedPreferences(Context ctx){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(APP_SETTINGS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void setSomeStringValue(Context ctx, String dataLabel, String dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(dataLabel, dataValue);
        editor.commit();
    }

    public static String getSomeStringValue(Context ctx,String dataLabel){
        return getSharedPreferences(ctx).getString(dataLabel,null);
    }

    public static void setSomeIntValue(Context ctx, String dataLabel, int dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(dataLabel, dataValue);
        editor.commit();
    }

    public static int getSomeIntValue(Context ctx,String dataLabel){
        return getSharedPreferences(ctx).getInt(dataLabel, -1);
    }

    public static void deleteAllValues(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}

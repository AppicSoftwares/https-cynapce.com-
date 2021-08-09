package com.alcanzar.cynapse.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomPreference {

    public static final String PREF_NAME = "Cynapse";
    public static final String USER_ID = "USER_ID";
    public static final String isLoggedIn = "isLoggedIn";
    public static final String CheckAddKey="CheckAddKey";

    public static void writeBoolean(Context context, String key, boolean value) {

        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {

        return getPreferences(context).edit();
    }
    public static void removeKey(Context context, String key) {

        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(key);
        editor.commit();
    }
    public static void removeAll(Context context) {

        SharedPreferences.Editor editor = getEditor(context);
        editor.clear();
        editor.commit();
    }


}


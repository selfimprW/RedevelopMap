package com.selfimpr.map.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.selfimpr.map.MApplication;

public final class SharedPreferenceUtils {
    private static SharedPreferenceUtils instance = new SharedPreferenceUtils();
    private static SharedPreferences sharedPreferences = MApplication.getApplication().getSharedPreferences("RedevelopMap.TencentMap", Context.MODE_PRIVATE);

    private SharedPreferenceUtils() {
    }

    public static SharedPreferenceUtils getInstance() {
        return instance;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void setLong(String key, Long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public boolean containKey(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 删除保存的文件
     */
    public static void deleteAll() {
        sharedPreferences.edit().clear().apply();
    }

    /**
     * 获得保存的文件大小
     */
    public double getSize() {
        return sharedPreferences.getAll().toString().length() * (double) 2;
    }

    /**
     * 清楚某个文件
     */
    public void deleteSpFile(String name) {
        SharedPreferences temp_sp = MApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor temp_editor = temp_sp.edit();
        temp_editor.clear().apply();

    }

    /**
     * 清楚某一条单元
     */
    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}

package com.ck.toec.toec_linker.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ck.toec.toec_linker.base.BaseApp;

/**
 * Created by wm on 2017/11/10.
 * sharedpreference操作类 单例模式
 */

public class SharedPreferenceUtil {
    private SharedPreferences mSP;

    public static SharedPreferenceUtil getInstance() {
        return SPHolder.mInstance;
    }

    private static class SPHolder {
        private static final SharedPreferenceUtil mInstance = new SharedPreferenceUtil();
    }

    private SharedPreferenceUtil() {
        mSP = BaseApp.getAppContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    public SharedPreferenceUtil putInt(String key, int value) {
        mSP.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mSP.getInt(key, defValue);
    }

    public SharedPreferenceUtil putString(String key, String value) {
        mSP.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String defValue) {
        return mSP.getString(key, defValue);
    }

    public SharedPreferenceUtil putBoolean(String key, boolean value) {
        mSP.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSP.getBoolean(key, defValue);
    }
}

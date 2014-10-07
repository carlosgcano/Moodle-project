package com.google.android.gcm.demo.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {

private SharedPreferences sharedPreferences;
private Editor editor;

public SharedPreferencesHelper(Context context) {
//this.sharedPreferences = Context.getPreferences("MisPreferencias",Context.MODE_PRIVATE);    
this.editor = sharedPreferences.edit(); }

public String GetPreferences(String key) {
    return sharedPreferences.getString(key, "");
}

public void SavePreferences(String key, String value) {
editor.putString(key, value);    
editor.commit();  
}
} 
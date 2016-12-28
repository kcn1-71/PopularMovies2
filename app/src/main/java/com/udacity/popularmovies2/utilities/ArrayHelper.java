package com.udacity.popularmovies2.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ArrayHelper {
    public static final String NO_PREF_SAVED = "NOPREFSAVED";
    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public ArrayHelper(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    public void saveArray(String key, ArrayList<Integer> array) {
        JSONArray jArray = new JSONArray(array);
        editor.remove(key);
        editor.putString(key, jArray.toString());
        editor.commit();
    }


    public ArrayList<Integer> getArray(String key) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        String jArrayString = prefs.getString(key, NO_PREF_SAVED);
        if (jArrayString.matches(NO_PREF_SAVED)) return getDefaultArray();
        else {
            try {
                JSONArray jArray = new JSONArray(jArrayString);
                for (int i = 0; i < jArray.length(); i++) {
                    array.add(jArray.getInt(i));
                }
                return array;
            } catch (JSONException e) {
                return getDefaultArray();
            }
        }
    }

    private ArrayList<Integer> getDefaultArray() {
        ArrayList<Integer> array = new ArrayList<Integer>();
        return array;
    }
}
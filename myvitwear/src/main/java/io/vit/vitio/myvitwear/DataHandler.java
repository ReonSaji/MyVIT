/*
 * Copyright (c) 2015 GDG VIT Vellore.
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.vit.vitio.myvitwear;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataHandler {

    private static final String TAG = DataHandler.class.getSimpleName();
    private Context context;
    private SharedPreferences preferences;
    private static DataHandler mInstance;

    public DataHandler(Context c) {
        this.context = c;

        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        mInstance = this;

    }

    public static DataHandler getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DataHandler(context.getApplicationContext());
        return mInstance;
    }


    private void saveString(String key, String string) {
        preferences.edit().putString(key, string).commit();
    }

    private String getString(String key, String def) {

        return preferences.getString(key, def);
    }

    public void saveCoursesJson(String coursesJson) {
        saveString("courses", coursesJson);
    }

    public String getCoursesJson() {
        return getString("courses", null);
    }


}

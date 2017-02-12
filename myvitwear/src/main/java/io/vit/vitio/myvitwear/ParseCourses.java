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

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ParseCourses {
    private static final String TAG = ParseCourses.class.getSimpleName();

    private JSONArray mResponseArray;

    public ParseCourses(JSONArray json) {
        mResponseArray = json;
    }

    public List<Course> getCoursesList() {
        List<Course> courseList = new ArrayList<>();
        try {
            if (mResponseArray != null) {
                for (int i = 0; i < mResponseArray.length(); i++) {
                    JSONObject c = mResponseArray.getJSONObject(i);

                    Course course = getCourse(c);
                    if (course != null)
                        courseList.add(course);
                    else
                        continue;
                }
                return courseList;
            } else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Course getCourse(JSONObject c) {
        Course course = new Course(c);
        try {

            course.setCLASS_NUMBER(c.getString(Course.KEY_CLASNBR));
            course.setCOURSE_TITLE(c.getString(Course.KEY_TITLE));
            course.setCOURSE_CODE(c.getString(Course.KEY_CODE));
            course.setCOURSE_SLOT(c.getString(Course.KEY_SLOT));
            course.setCOURSE_VENUE(c.getString(Course.KEY_VENUE));
            course.setCOURSE_FACULTY(c.getString(Course.KEY_FACULTY));
            course.setCOURSE_ATTENDANCE(c.getString(Course.KEY_ATTENDANCE));
            course.setCOURSE_TYPE_SHORT(c.getString(Course.KEY_TYPE_SHORT));
            return course;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}

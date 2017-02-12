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

package io.vit.vitio.Instances;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.vit.vitio.Managers.Parsers.ParseCourses;
import io.vit.vitio.R;

import static io.vit.vitio.Managers.ConnectDatabase.COLUMNS;

/**
 * Created by shalini on 21-06-2015.
 */

public class Course {

    //Constants
    public static String COURSE_MODE_CBL="CBL";
    public static String COURSE_MODE_PBL="PBL";
    public static String COURSE_MODE_PBC="PBC";
    public static String COURSE_MODE_LBC="LBC";



    //Course Attributes
    private String CLASS_NUMBER;
    private String COURSE_CODE;
    private String COURSE_TITLE;
    private String COURSE_TYPE;
    private String COURSE_TYPE_SHORT;
    private Ltpc COURSE_LTPC;
    private String COURSE_MODE;
    private String COURSE_OPTION;
    private String COURSE_SLOT;
    private String COURSE_VENUE;
    private Faculty COURSE_FACULTY;
    private String COURSE_REGISTRATIONSTATUS;
    private String COURSE_BILL_DATE;
    private String COURSE_BILL_NUMBER;
    private String COURSE_PROJECT_TITLE;
    private String COURSE_MARKS_SUPPORTED;
    private Attendance COURSE_ATTENDANCE;
    private List<Timing> COURSE_TIMING;
    private List<Mark> COURSE_MARKS;
    private JSONObject COURSE_JSON;
    public boolean isSeminar=false;

    public Course() {
    }

    public Course(JSONObject object) {
        COURSE_JSON = object;
    }

    public static String getSchool(String regno){
        if(regno!=null){
            String tag=regno.replaceAll("[0-9]*","").trim();
            switch (tag){
                case "BIT":
                case "MSE":
                case "BAM":
                case "BCA":
                case "MCA":
                    return "SITE";
                case "BCE":
                case "BCS":
                    return "SCSE";
                case "BEC":
                    return "SENSE";
                case "BEE":
                    return "SELECT";
                case "BME":
                    return "SMBS";
                default:
                    return "UNKNOWN";
            }
        }
        return "NULL";
    }

    public void setCLASS_NUMBER(String value) {
        this.CLASS_NUMBER = value;
    }

    public void setCOURSE_CODE(String value) {
        this.COURSE_CODE = value;
    }

    public void setCOURSE_TYPE(String value) {
        this.COURSE_TYPE = value;
    }

    public void setCOURSE_TITLE(String value) {
        this.COURSE_TITLE = value;
    }

    public void setCOURSE_LTPC(String value) {
        this.COURSE_LTPC = new Ltpc(value);
    }

    public void setCOURSE_LTPC(Ltpc value) {
        this.COURSE_LTPC = value;
    }

    public void setCOURSE_MODE(String value) {
        this.COURSE_MODE = value;
    }

    public void setCOURSE_OPTION(String value) {
        this.COURSE_OPTION = value;
    }

    public void setCOURSE_SLOT(String value) {
        this.COURSE_SLOT = value;
    }

    public void setCOURSE_VENUE(String value) {
        this.COURSE_VENUE = value;
    }

    public void setCOURSE_FACULTY(String value) {
        this.COURSE_FACULTY = new Faculty(value);
    }

    public void setCOURSE_FACULTY(Faculty value) {
        this.COURSE_FACULTY = value;
    }

    public void setCOURSE_FACULTY(String name, String school) {
        this.COURSE_FACULTY = new Faculty(name, school);
    }

    public void setCOURSE_REGISTRATIONSTATUS(String value) {
        this.COURSE_REGISTRATIONSTATUS = value;
    }

    public void setCOURSE_BILL_DATE(String value) {
        this.COURSE_BILL_DATE = value;
    }

    public void setCOURSE_BILL_NUMBER(String value) {
        this.COURSE_BILL_NUMBER = value;
    }

    public void setCOURSE_PROJECT_TITLE(String value) {
        this.COURSE_PROJECT_TITLE = value;
    }

    public void setCOURSE_ATTENDANCE(Attendance value) {
        this.COURSE_ATTENDANCE = value;
    }

    public void setCOURSE_TIMING(List<Timing> value) {
        this.COURSE_TIMING = value;
    }

    public void setCOURSE_MARKS(List<Mark> value) {
        this.COURSE_MARKS = value;
    }

    public void setJson(JSONObject value) {
        this.COURSE_JSON = value;
    }

    public String getCOURSE_TYPE_SHORT() {
        return COURSE_TYPE_SHORT;
    }

    public void setCOURSE_TYPE_SHORT(String COURSE_TYPE_SHORT) {
        this.COURSE_TYPE_SHORT = COURSE_TYPE_SHORT;
    }

    public String getCLASS_NUMBER() {
        return this.CLASS_NUMBER;
    }

    public String getCOURSE_CODE() {
        return this.COURSE_CODE;
    }

    public String getCOURSE_TYPE() {
        return this.COURSE_TYPE;
    }

    public String getCOURSE_TITLE() {
        return this.COURSE_TITLE;
    }

    public Ltpc getCOURSE_LTPC() {
        return this.COURSE_LTPC;
    }

    public String getCOURSE_MODE() {
        return this.COURSE_MODE;
    }

    public String getCOURSE_OPTION() {
        return this.COURSE_OPTION;
    }

    public String getCOURSE_SLOT() {
        return this.COURSE_SLOT;
    }

    public String getCOURSE_VENUE() {
        return this.COURSE_VENUE;
    }

    public Faculty getCOURSE_FACULTY() {
        return this.COURSE_FACULTY;
    }

    public String getCOURSE_REGISTRATIONSTATUS() {
        return this.COURSE_REGISTRATIONSTATUS;
    }

    public String getCOURSE_BILL_DATE() {
        return this.COURSE_BILL_DATE;
    }

    public String getCOURSE_BILL_NUMBER() {
        return this.COURSE_BILL_NUMBER;
    }

    public String getCOURSE_PROJECT_TITLE() {
        return this.COURSE_PROJECT_TITLE;
    }

    public Attendance getCOURSE_ATTENDANCE() {
        return this.COURSE_ATTENDANCE;
    }

    public List<Timing> getCOURSE_TIMING() {
        return this.COURSE_TIMING;
    }

    public List<Mark> getCOURSE_MARKS() {
        return this.COURSE_MARKS;
    }

    public JSONObject getCOURSE_JSON() {
        return COURSE_JSON;
    }

    public String getBuilding() {
        String school=getCOURSE_VENUE().replaceAll("G{0,}[0-9]+","").trim();
        return school;

    }

    public int getBuildingImageId() {
        String buil=getBuilding();
        if(buil.toUpperCase().contains("SJT")){
            return R.drawable.sjt_vectory;
        }
        else if(buil.toUpperCase().contains("SMV")){
            return R.drawable.smvy;
        }
        else if(buil.toUpperCase().contains("TT")){
            return R.drawable.tty;
        }
        else if(buil.toUpperCase().contains("MB")){
            return R.drawable.mb_vectory;
        }
        else if(buil.toUpperCase().contains("GDN")){
            return R.drawable.gdny;
        }
        else {
            return R.drawable.cdmm_mod;
        }

    }

    public ContentValues getContentValues() throws JSONException {
        ContentValues values = new ContentValues();

        values.put(COLUMNS[0], getCLASS_NUMBER());
        values.put(COLUMNS[1], getCOURSE_TITLE());
        values.put(COLUMNS[2], getCOURSE_SLOT());
        values.put(COLUMNS[3], getCOURSE_TYPE());
        values.put(COLUMNS[4], getCOURSE_TYPE_SHORT());
        Log.d("type", getCOURSE_TYPE_SHORT());
        values.put(COLUMNS[5], getCOURSE_LTPC().toString());
        values.put(COLUMNS[6], getCOURSE_CODE());
        values.put(COLUMNS[7], getCOURSE_MODE());
        values.put(COLUMNS[8], getCOURSE_OPTION());
        values.put(COLUMNS[9], getCOURSE_VENUE());
        values.put(COLUMNS[10], getCOURSE_FACULTY().toString());
        values.put(COLUMNS[11], getCOURSE_REGISTRATIONSTATUS());
        values.put(COLUMNS[12], getCOURSE_BILL_DATE());
        values.put(COLUMNS[13], getCOURSE_BILL_NUMBER());
        values.put(COLUMNS[14], getCOURSE_PROJECT_TITLE());
        values.put(COLUMNS[15], getCOURSE_JSON().toString());
        values.put(COLUMNS[16], getCOURSE_ATTENDANCE().getJson().toString());
        values.put(COLUMNS[17], getCOURSE_JSON().getJSONArray("timings").toString());
        values.put(COLUMNS[18], getCOURSE_JSON().getJSONObject("marks").toString());
        return values;
    }

    public static Course fromCursor(Cursor cursor) throws JSONException {

        Course course=new Course();
        course.setCLASS_NUMBER(cursor.getString(0));
        course.setCOURSE_TITLE(cursor.getString(1));
        course.setCOURSE_SLOT(cursor.getString(2));
        course.setCOURSE_TYPE(cursor.getString(3));
        course.setCOURSE_TYPE_SHORT(cursor.getString(4));
        course.setCOURSE_LTPC(cursor.getString(5));
        course.setCOURSE_CODE(cursor.getString(6));
        course.setCOURSE_MODE(cursor.getString(7));
        course.setCOURSE_OPTION(cursor.getString(8));
        course.setCOURSE_VENUE(cursor.getString(9));
        course.setCOURSE_FACULTY(cursor.getString(10));
        course.setCOURSE_REGISTRATIONSTATUS(cursor.getString(11));
        course.setCOURSE_BILL_DATE(cursor.getString(12));
        course.setCOURSE_BILL_NUMBER(cursor.getString(13));
        course.setCOURSE_PROJECT_TITLE(cursor.getString(14));
        course.setJson(new JSONObject(cursor.getString(15)));
        course.setCOURSE_ATTENDANCE(ParseCourses.getAttendance(new JSONObject(cursor.getString(16))));
        course.setCOURSE_TIMING(ParseCourses.getTimings(new JSONArray(cursor.getString(17))));
        course.setCOURSE_MARKS(ParseCourses.getCouseMarks(new JSONObject(cursor.getString(18))));

        return course;
    }
}

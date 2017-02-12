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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.R;



public class Course {

    public static final String KEY_CLASNBR = "course_class_number";
    public static final String KEY_TITLE = "course_title";
    public static final String KEY_SLOT = "course_slot";
    public static final String KEY_TYPE = "course_type";
    public static final String KEY_TYPE_SHORT = "course_type_short";
    public static final String KEY_LTPC = "course_ltpc";
    public static final String KEY_CODE = "course_code";
    public static final String KEY_MODE = "course_mode";
    public static final String KEY_OPTION = "course_option";
    public static final String KEY_VENUE = "course_venue";
    public static final String KEY_FACULTY = "course_faculty";
    public static final String KEY_REGISTRATIONSTATUS = "course_registrationstatus";
    public static final String KEY_BILL_DATE = "course_date";
    public static final String KEY_BILL_NUMBER = "course_bill_number";
    public static final String KEY_PROJECT_TITLE = "course_project_title";
    public static final String KEY_COURSE_JSON = "course_json";
    public static final String KEY_ATTENDANCE = "course_attendance";
    public static final String KEY_TIMINGS = "course_timings";
    public static final String KEY_MARKS = "course_marks";




    //Course Attributes
    private String CLASS_NUMBER;
    private String COURSE_CODE;
    private String COURSE_TITLE;
    private String COURSE_TYPE_SHORT;
    private String COURSE_SLOT;
    private String COURSE_VENUE;
    private String COURSE_FACULTY;
    private String COURSE_ATTENDANCE;
    private JSONObject COURSE_JSON;

    public Course() {
    }

    public Course(JSONObject object) {
        COURSE_JSON = object;
    }



    public void setCLASS_NUMBER(String value) {
        this.CLASS_NUMBER = value;
    }

    public void setCOURSE_CODE(String value) {
        this.COURSE_CODE = value;
    }

    public void setCOURSE_TITLE(String value) {
        this.COURSE_TITLE = value;
    }

    public void setCOURSE_SLOT(String value) {
        this.COURSE_SLOT = value;
    }

    public void setCOURSE_VENUE(String value) {
        this.COURSE_VENUE = value;
    }

    public void setCOURSE_FACULTY(String value) {
        this.COURSE_FACULTY = value;
    }

    public void setCOURSE_ATTENDANCE(String value) {
        this.COURSE_ATTENDANCE = value;
    }

    public void setJson(JSONObject value) {
        this.COURSE_JSON = value;
    }

    public void setCOURSE_TYPE_SHORT(String COURSE_TYPE_SHORT) {
        this.COURSE_TYPE_SHORT = COURSE_TYPE_SHORT;
    }

    public String getCOURSE_TYPE_SHORT() {
        return COURSE_TYPE_SHORT;
    }

    public String getCLASS_NUMBER() {
        return this.CLASS_NUMBER;
    }

    public String getCOURSE_CODE() {
        return this.COURSE_CODE;
    }

    public String getCOURSE_TITLE() {
        return this.COURSE_TITLE;
    }

    public String getCOURSE_SLOT() {
        return this.COURSE_SLOT;
    }

    public String getCOURSE_VENUE() {
        return this.COURSE_VENUE;
    }

    public String getCOURSE_FACULTY() {
        return this.COURSE_FACULTY;
    }

    public String getCOURSE_ATTENDANCE() {
        return this.COURSE_ATTENDANCE;
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

    public List<Item> getItemList(){
        ArrayList<Item> list=new ArrayList<>();
        list.add(new Item("Slot",getCOURSE_SLOT()));
        list.add(new Item("Venue",getCOURSE_VENUE()));
        list.add(new Item("Faculty",getCOURSE_FACULTY()));
        list.add(new Item("Attendance",getCOURSE_ATTENDANCE()));
        return list;
    }


}

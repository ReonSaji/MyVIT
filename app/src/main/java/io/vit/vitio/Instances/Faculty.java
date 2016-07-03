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

/**
 * Created by shalini on 21-06-2015.
 */
public class Faculty {
    private String NAME;
    private String SCHOOL;
    private String facultyString;
    private String NUMBER;
    private String DESIGNATION;
    private String EMAIL;
    private String INTERCOM;
    private String MOBILE;
    private String ROOM;

    public Faculty(String NAME, String SCHOOL) {
        this.NAME = NAME;
        this.SCHOOL = SCHOOL;
    }

    public Faculty(String s){
        if(s!=null) {
            facultyString=s;
            String arr[]=s.split("-");
            if(arr.length==2){
                this.NAME=arr[0].trim();
                this.SCHOOL=arr[1].trim();
            }
            else
            {
                this.NAME = "N/A";
                this.SCHOOL = "N/A";
            }
        }
        else
        {
            this.NAME = "N/A";
            this.SCHOOL = "N/A";
        }
    }

    public Faculty(Faculty f){
        this.NAME=f.getNAME();
        this.SCHOOL=f.getSCHOOL();
    }

    public Faculty(){
        this.NAME = "N/A";
        this.SCHOOL = "N/A";
    }

    public String getNAME(){
        return NAME;
    }

    public String getSCHOOL(){
        return SCHOOL;
    }


    public void setNAME(String value){
        NAME=value;
    }

    public void setSCHOOL(String value){
        SCHOOL=value;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getFacultyString() {
        return facultyString;
    }

    public void setFacultyString(String facultyString) {
        this.facultyString = facultyString;
    }

    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getINTERCOM() {
        return INTERCOM;
    }

    public void setINTERCOM(String INTERCOM) {
        this.INTERCOM = INTERCOM;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getROOM() {
        return ROOM;
    }

    public void setROOM(String ROOM) {
        this.ROOM = ROOM;
    }

    @Override
    public String toString() {
        return facultyString;
    }


}

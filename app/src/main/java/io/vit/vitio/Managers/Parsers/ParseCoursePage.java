package io.vit.vitio.Managers.Parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.vit.vitio.Instances.Faculty;
import io.vit.vitio.Instances.GenericUpload;
import io.vit.vitio.Instances.Lecture;

/**
 * Created by Prince Bansal on 14-01-2016.
 */
public class ParseCoursePage {

    public static int IS_SLOTS = 10, IS_FACULTIES = 11, IS_UPLOADS = 12;

    private JSONObject myJsonObject;
    private List<String> slotList;
    private List<Faculty> facultyList;
    private Map<String, ArrayList> uploadsMap;
    private boolean VALIDITY = true;
    private int type = -1;

    public ParseCoursePage() {
    }

    public ParseCoursePage(JSONObject object, int type) {
        this.type = type;
        myJsonObject = object;
    }

    public ParseCoursePage(String res, int type) {
        this.type = type;
        try {
            myJsonObject = new JSONObject(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse() {
        if (myJsonObject != null) {
            if (type == IS_SLOTS) {
                parseSlots();
            } else if (type == IS_FACULTIES) {
                parseFaculties();
            } else if (type == IS_UPLOADS) {
                parseUploads();
            }
        }
    }


    private void parseSlots() {
        try {
            slotList = new ArrayList<>();
            //TODO Remind Shubho to change courses to slots
            JSONObject object = myJsonObject.getJSONObject("courses");
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = object.getString(key);
                slotList.add(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseFaculties() {
        try {
            facultyList = new ArrayList<>();
            //TODO Remind Shubho to change courses to faculties
            JSONObject object = myJsonObject.getJSONObject("courses");
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = object.getString(key);
                Faculty faculty = new Faculty();
                faculty.setNUMBER(key);
                faculty.setNAME(value.split("-")[1]);
                facultyList.add(faculty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseUploads() {
        try {
            uploadsMap = new HashMap<>();
            JSONObject object = myJsonObject.getJSONObject("uploads");
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (object.get(key) != null&&!object.get(key).toString().equals("null")) {
                    if (key.equals("text_material") || key.equals("assignments")) {
                        JSONArray array = object.getJSONArray(key);
                        if (array.length() > 0) {
                            ArrayList<GenericUpload> list = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                GenericUpload genericUpload = new GenericUpload();
                                genericUpload.setFileName(jsonObject.getString("name"));
                                genericUpload.setLink(jsonObject.getString("link"));
                                list.add(genericUpload);

                            }
                            if (key.equals("text_material"))
                                uploadsMap.put("Text/Reference Material", list);
                            else if (key.equals("assignments"))
                                uploadsMap.put("Assignments", list);
                        }
                    } else if (key.equals("lecture")) {
                        JSONArray array = object.getJSONArray(key);
                        if (array.length() > 0) {
                            ArrayList<Lecture> list = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                Lecture lecture = new Lecture();
                                lecture.setDate(jsonObject.getString("date"));
                                lecture.setDay(jsonObject.getString("day"));
                                lecture.setTopic(jsonObject.getString("topic"));
                                lecture.setFileName(jsonObject.getJSONObject("material").getString("name"));
                                lecture.setLink(jsonObject.getJSONObject("material").getString("link"));
                                list.add(lecture);
                            }
                            uploadsMap.put("Lectures", list);
                        }
                    } else if (key.equals("question_paper")) {
                        if (object.get(key) != null && !object.toString().equals("null")) {
                            JSONObject paperObject = object.getJSONObject(key);
                            Iterator<String> paperIterator = paperObject.keys();
                            while (paperIterator.hasNext()) {
                                ArrayList<GenericUpload> quesList = new ArrayList<>();
                                ArrayList<GenericUpload> ansList = new ArrayList<>();
                                String mKey = paperIterator.next();
                                JSONObject jsonObject = paperObject.getJSONObject(mKey);
                                GenericUpload questionPaper = new GenericUpload();
                                questionPaper.setFileName(jsonObject.getJSONObject("question_paper").getString("name"));
                                questionPaper.setLink(jsonObject.getJSONObject("question_paper").getString("link"));
                                quesList.add(questionPaper);
                                GenericUpload answerKey = new GenericUpload();
                                answerKey.setFileName(jsonObject.getJSONObject("question_paper").getString("name"));
                                answerKey.setLink(jsonObject.getJSONObject("question_paper").getString("link"));
                                ansList.add(answerKey);

                                uploadsMap.put(mKey + " Question Paper", quesList);
                                uploadsMap.put(mKey + " Answer Key", ansList);
                            }
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getCoursePageSlotList() {
        return slotList;
    }

    public List<Faculty> getCoursePageFacultyList() {
        return facultyList;
    }

    public Map<String, ArrayList> getCoursePageUploadMap() {
        return uploadsMap;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

package io.vit.vitio.Managers.Parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.vit.vitio.Instances.Faculty;
import io.vit.vitio.Instances.Slot;

/**
 * Created by Prince Bansal on 14-01-2016.
 */
public class ParseCoursePage {

    public static int IS_SLOTS = 10, IS_FACULTIES = 11, IS_UPLOADS = 12;

    private JSONObject myJsonObject;
    private List<String> slotList;
    private List<Faculty> facultyList;
    private List<String> uploadsList;
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
            JSONObject object = myJsonObject.getJSONObject("slots");
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
            JSONObject object = myJsonObject.getJSONObject("faculties");
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
            uploadsList = new ArrayList<>();
            JSONArray array = myJsonObject.getJSONArray("uploads");
            for (int i = 0; i < array.length(); i++) {
                String url = array.getString(i);
                uploadsList.add(url);
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

    public List<String> getCoursePageUploadList() {
        return uploadsList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

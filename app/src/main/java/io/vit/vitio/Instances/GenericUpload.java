package io.vit.vitio.Instances;

/**
 * Created by Prince Bansal Local on 04-07-2016.
 */

public class GenericUpload {

    private String fileName;
    private String link;
    private int progress=0;
    private String courseCode;
    private String courseSlot;
    private String courseFaculty;

    public GenericUpload() {
    }

    public GenericUpload(String fileName, String link) {
        this.fileName = fileName;
        this.link = link;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseSlot() {
        return courseSlot;
    }

    public void setCourseSlot(String courseSlot) {
        this.courseSlot = courseSlot;
    }

    public String getCourseFaculty() {
        return courseFaculty;
    }

    public void setCourseFaculty(String courseFaculty) {
        this.courseFaculty = courseFaculty;
    }
}

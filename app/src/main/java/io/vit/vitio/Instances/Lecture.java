package io.vit.vitio.Instances;

/**
 * Created by Prince Bansal Local on 07-07-2016.
 */

public class Lecture extends GenericUpload {

    private String date;
    private String day;
    private String topic;

    public Lecture() {
    }

    public Lecture(String fileName, String link, String date, String day, String topic) {
        super(fileName, link);
        this.date = date;
        this.day = day;
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}

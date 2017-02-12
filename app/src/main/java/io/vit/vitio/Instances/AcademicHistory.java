package io.vit.vitio.Instances;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Prince Bansal Local on 14-04-2016.
 */
public class AcademicHistory {

    private HashMap<String, String> gradesSummary;
    private List<GradeHistory> gradesHistory;
    private GradesAggregate gradesAggregate;

    public HashMap<String, String> getGradesSummary() {
        return gradesSummary;
    }

    public void setGradesSummary(HashMap<String, String> gradesSummary) {
        this.gradesSummary = gradesSummary;
    }

    public List<GradeHistory> getGradesHistory() {
        return gradesHistory;
    }

    public void setGradesHistory(List<GradeHistory> gradesHistory) {
        this.gradesHistory = gradesHistory;
    }

    public GradesAggregate getGradesAggregate() {
        return gradesAggregate;
    }

    public void setGradesAggregate(GradesAggregate gradesAggregate) {
        this.gradesAggregate = gradesAggregate;
    }

    public class GradeHistory {
        Course course;
        String grade;

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
    }

    public class GradesAggregate {
        private String cgpa;
        private String creditsEarned;
        private String creditsRegistered;
        private String rank;

        public String getCgpa() {
            return cgpa;
        }

        public void setCgpa(String cgpa) {
            this.cgpa = cgpa;
        }

        public String getCreditsEarned() {
            return creditsEarned;
        }

        public void setCreditsEarned(String creditsEarned) {
            this.creditsEarned = creditsEarned;
        }

        public String getCreditsRegistered() {
            return creditsRegistered;
        }

        public void setCreditsRegistered(String creditsRegistered) {
            this.creditsRegistered = creditsRegistered;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }


}

package io.vit.vitio.Instances;

import android.widget.RemoteViews;

/**
 * Created by Prince Bansal Local on 14-04-2016.
 */
public class Grade {

    public static final String S_GRADE="S";
    public static final String A_GRADE="A";
    public static final String B_GRADE="B";
    public static final String C_GRADE="C";
    public static final String D_GRADE="D";
    public static final String E_GRADE="E";
    public static final String F_GRADE="F";
    public static final String N_GRADE="N";

    private static final String[] GRADES_PREFIX_SET={S_GRADE,A_GRADE,B_GRADE,C_GRADE,D_GRADE,E_GRADE,F_GRADE,N_GRADE};
    public static final int GRADES_WEIGHT_SET[]={10,9,8,7,6,5,0,0};
    public String name;
    public int weight;

    /**
    * @param name One of {@link #S_GRADE},{@link #A_GRADE},{@link #B_GRADE},{@link #C_GRADE},{@link #D_GRADE},{@link #E_GRADE},{@link #F_GRADE} or {@link #N_GRADE}.
    **/

    public Grade(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

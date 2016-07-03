package io.vit.vitio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import io.vit.vitio.Extras.CustomFloatingActionButton;
import io.vit.vitio.Extras.CustomFloatingActionsMenu;
import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Fragments.SubjectView.TimingAdapter;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Mark;
import io.vit.vitio.Instances.Timing;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseTimeTable;

/**
 * Created by Prince Bansal Local on 03-02-2016.
 */
public class SubjectViewActivity extends AppCompatActivity implements View.OnClickListener{

    //Marks views
    private TextView quiz1Head, quiz2Head, quiz3Head, cat1Head, cat2Head, assignmentHead, internalsHead, cgpaHead, qouteHead, qouteWriter, errorMessage;

    //Declare Contents
    private TextView quiz1Marks, quiz2Marks, quiz3Marks, cat1Marks, cat2Marks, assignmentMarks, internalsMarks, cgpaContent;

    private LinearLayout cat1Indicator, cat2Indicator, internalsIndicator, layoutContainer;


    private TextView subName, subType, subCode, subPer, subSlot, subVenue, attended, subSchool, subFaculty, attendClassText, missClassText;
    private FloatingActionButton fab;
    private ImageView subColorCircle, back_button, schoolImage,buildingTint;
    private LinearLayout  missMinus, missAdd, attendMinus, attendAdd,fullViewTint;
    private RecyclerView recyclerView;
    //private FloatingActionButton attFab, marksFab, reminderFab;
    private FloatingActionButton attFab, marksFab,coursePageFab;
    private FloatingActionMenu floatingMenu;
    private String MY_CLASS_NUMBER;
    private Course myCourse;
    private int MISS_COUNTS = 0;
    private int ATTEND_COUNTS = 0;
    private DataHandler dataHandler;
    Typeface typeface;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private NestedScrollView nestedScrollView;
    private MyTheme theme;
    private String SUB_NAME_ID;
    private String IMAGE_NAME_ID;

    private ParseTimeTable parseTimeTable;
    private TimingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_view_fragment_trial_two);
        init();
        setInit();
        setFonts();
        //setTransitions();

        if (myCourse != null) {
            setData();
        }
    }

    private void init() {

        //Get Arguments
        try {
            MY_CLASS_NUMBER = getIntent().getExtras().getString("class_number");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            finish();
        }
        //Marks Views initialized
        quiz1Head = (TextView) findViewById(R.id.quiz1_head);
        quiz2Head = (TextView) findViewById(R.id.quiz2_head);
        quiz3Head = (TextView) findViewById(R.id.quiz3_head);
        cat1Head = (TextView) findViewById(R.id.cat1_head);
        cat2Head = (TextView) findViewById(R.id.cat2_head);
        assignmentHead = (TextView) findViewById(R.id.assignment_head);
        internalsHead = (TextView) findViewById(R.id.internals_head);
        quiz1Marks = (TextView) findViewById(R.id.quiz1_marks);
        quiz2Marks = (TextView) findViewById(R.id.quiz2_marks);
        quiz3Marks = (TextView) findViewById(R.id.quiz3_marks);
        cat1Marks = (TextView) findViewById(R.id.cat1_marks);
        cat2Marks = (TextView) findViewById(R.id.cat2_marks);
        assignmentMarks = (TextView) findViewById(R.id.assignment_marks);
        internalsMarks = (TextView) findViewById(R.id.internals_marks);
        errorMessage = (TextView) findViewById(R.id.error_message);


        cat1Indicator = (LinearLayout) findViewById(R.id.cat1_indicator);
        cat2Indicator = (LinearLayout) findViewById(R.id.cat2_indicator);
        internalsIndicator = (LinearLayout) findViewById(R.id.internals_indicator);
        layoutContainer = (LinearLayout) findViewById(R.id.layout_container);



        //Initialize TextViews
        subName = (TextView) findViewById(R.id.subject_name);
        subType = (TextView) findViewById(R.id.subject_type);
        subCode = (TextView) findViewById(R.id.subject_code);
        subSlot = (TextView) findViewById(R.id.subject_slot);
        subVenue = (TextView) findViewById(R.id.subject_venue);
        subPer = (TextView) findViewById(R.id.subject_perc);
        subFaculty = (TextView) findViewById(R.id.subject_faculty);
        //subSchool = (TextView) findViewById(R.id.subject_school);
        attended = (TextView) findViewById(R.id.subject_att_out_total);
        attendClassText = (TextView) findViewById(R.id.attend_class_text);
        missClassText = (TextView) findViewById(R.id.miss_class_text);

        //Initialize ImageViews
        //subColorCircle = (ImageView) findViewById(R.id.subject_color_circle);
        missMinus = (LinearLayout) findViewById(R.id.miss_minus);
        missAdd = (LinearLayout) findViewById(R.id.miss_plus);
        attendMinus = (LinearLayout) findViewById(R.id.attend_minus);
        attendAdd = (LinearLayout) findViewById(R.id.attend_plus);
        fullViewTint= (LinearLayout) findViewById(R.id.full_view_tint);
        //back_button = (ImageView) findViewById(R.id.back_button);
        schoolImage = (ImageView) findViewById(R.id.school_image);
        buildingTint = (ImageView) findViewById(R.id.building_tint);

        //Initialize LinearLayouts
       // attBar = (LinearLayout) findViewById(R.id.subject_per_bar);

        //Initialize Others
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        attFab = (FloatingActionButton) findViewById(R.id.att_fab);
        marksFab = (FloatingActionButton) findViewById(R.id.marks_fab);
        coursePageFab = (FloatingActionButton) findViewById(R.id.course_page_fab);
        //reminderFab = (FloatingActionButton) findViewById(R.id.reminder_fab);
        floatingMenu = (FloatingActionMenu) findViewById(R.id.left_labels);
        dataHandler = DataHandler.getInstance(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scrollview);
        theme = new MyTheme(this);
        parseTimeTable=new ParseTimeTable(dataHandler.getCoursesList(),dataHandler);
    }

    private void setInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            subName.setTransitionName(SUB_NAME_ID);
            schoolImage.setTransitionName(IMAGE_NAME_ID);
        }



        //Drawable navIcon= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white_24dp, null);
        //navIcon=new ScaleDrawable(navIcon,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46.0f,getResources().getDisplayMetrics()),(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46.0f,getResources().getDisplayMetrics())).getDrawable();
        //navIcon.setBounds(0,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46.0f,getResources().getDisplayMetrics()),(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46.0f,getResources().getDisplayMetrics()));

        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //collapsingToolbarLayout.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_back,null));
        //collapsingToolbarLayout.setPadding(10,0,0,10);
        myCourse = dataHandler.getCourse(MY_CLASS_NUMBER);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //back_button.setOnClickListener(this);
        marksFab.setOnClickListener(this);
        attFab.setOnClickListener(this);
        coursePageFab.setOnClickListener(this);
        //reminderFab.setOnClickListener(this);
        missMinus.setOnClickListener(this);
        missAdd.setOnClickListener(this);
        attendMinus.setOnClickListener(this);
        attendAdd.setOnClickListener(this);
        fullViewTint.setOnClickListener(this);
        floatingMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {

                    fullViewTint.setVisibility(View.VISIBLE);
                } else {

                    fullViewTint.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setTransitions() {
        if(Build.VERSION.SDK_INT>=21) {
            //setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.explode));
            //setReenterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
            /*((Transition)this.getEnterTransition()).addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if(Build.VERSION.SDK_INT>=21){
                        Log.d("cir","cir");
                        schoolImage.setVisibility(View.INVISIBLE);
                        circularRevealSchoolImage();
                    }
                    else{
                        schoolImage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });*/
        }
    }

    private void setFonts() {
        typeface = theme.getMyThemeTypeface();
        subCode.setTypeface(typeface);
        subName.setTypeface(typeface);
        subPer.setTypeface(typeface);
        subVenue.setTypeface(typeface);
        subType.setTypeface(typeface);
        subSlot.setTypeface(typeface);
        subFaculty.setTypeface(typeface);
        //subSchool.setTypeface(typeface);
        attended.setTypeface(typeface);
        attendClassText.setTypeface(typeface);
        missClassText.setTypeface(typeface);

        quiz1Head.setTypeface(typeface);
        quiz2Head.setTypeface(typeface);
        quiz3Head.setTypeface(typeface);
        cat1Head.setTypeface(typeface);
        cat2Head.setTypeface(typeface);
        assignmentHead.setTypeface(typeface);
        internalsHead.setTypeface(typeface);
        //cgpaHead.setTypeface(typeface);

        quiz1Marks.setTypeface(typeface);
        quiz2Marks.setTypeface(typeface);
        quiz3Marks.setTypeface(typeface);
        cat1Marks.setTypeface(typeface);
        cat2Marks.setTypeface(typeface);
        assignmentMarks.setTypeface(typeface);
        internalsMarks.setTypeface(typeface);


    }

    private void setData() {
        subName.setText(myCourse.getCOURSE_TITLE());
        subSlot.setText(myCourse.getCOURSE_SLOT());
        subType.setText(myCourse.getCOURSE_TYPE());
        subCode.setText(myCourse.getCOURSE_CODE());
        subFaculty.setText(myCourse.getCOURSE_FACULTY().getNAME());
        //subSchool.setText(myCourse.getCOURSE_FACULTY().getSCHOOL());
        schoolImage.setImageResource(myCourse.getBuildingImageId());
        subPer.setText(myCourse.getCOURSE_ATTENDANCE().getPERCENTAGE() + "%");
        subVenue.setText(myCourse.getCOURSE_VENUE());
        attended.setText("attended " + myCourse.getCOURSE_ATTENDANCE().getATTENDED_CLASSES() + " out " + myCourse.getCOURSE_ATTENDANCE().getTOTAL_CLASSES());
        //attBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), myCourse.getCOURSE_ATTENDANCE().getPERCENTAGE()));
        List<Timing> courseTimings = parseTimeTable.getCourseTimingByParsing(myCourse);
        adapter=new TimingAdapter(this,courseTimings);
        recyclerView.setAdapter(adapter);
        setMarks();

    }

    private void setMarks() {
            List<Mark> markList = myCourse.getCOURSE_MARKS();
            float tempInternals = 0;
            boolean internalsFlag = false;
            if (markList != null) {
                errorMessage.setVisibility(TextView.GONE);
                layoutContainer.setVisibility(LinearLayout.VISIBLE);
                if (!markList.get(0).getMARK().isEmpty() && !markList.get(0).getMARK().equals("null") && markList.get(0).getMARK() != null) {
                    cat1Marks.setText(markList.get(0).getMARK() + "/50");
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(0).getMARK()) * 0.30);
                    if (Float.parseFloat(markList.get(0).getMARK()) < 25) {
                        cat1Indicator.setVisibility(LinearLayout.VISIBLE);
                    }
                }
                if (!markList.get(1).getMARK().isEmpty() && !markList.get(1).getMARK().equals("null") && markList.get(1).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(1).getMARK()) * 0.30);
                    cat2Marks.setText(markList.get(1).getMARK() + "/50");
                    if (Float.parseFloat(markList.get(1).getMARK()) < 25) {
                        cat2Indicator.setVisibility(LinearLayout.VISIBLE);
                    }
                }
                if (!markList.get(2).getMARK().isEmpty() && !markList.get(2).getMARK().equals("null") && markList.get(2).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(2).getMARK()));
                    quiz1Marks.setText(markList.get(2).getMARK() + "/5");
                }
                if (!markList.get(3).getMARK().isEmpty() && !markList.get(3).getMARK().equals("null") && markList.get(3).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(3).getMARK()));
                    quiz2Marks.setText(markList.get(3).getMARK() + "/5");
                }
                if (!markList.get(4).getMARK().isEmpty() && !markList.get(4).getMARK().equals("null") && markList.get(4).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(4).getMARK()));
                    quiz3Marks.setText(markList.get(4).getMARK() + "/5");
                }
                if (!markList.get(5).getMARK().isEmpty() && !markList.get(5).getMARK().equals("null") && markList.get(5).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(5).getMARK()));
                    assignmentMarks.setText(markList.get(5).getMARK() + "/5");
                }
                if (internalsFlag == true) {
                    internalsMarks.setText(tempInternals + "/50");
                    if (tempInternals < 25) {
                        internalsIndicator.setVisibility(LinearLayout.VISIBLE);
                    }
                }
            } else {
                errorMessage.setVisibility(TextView.VISIBLE);
                layoutContainer.setVisibility(LinearLayout.GONE);
            }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.marks_fab:
                floatingMenu.close(true);
                Intent intent = new Intent(this, MarksActivity.class);
                intent.putExtra("class_number", MY_CLASS_NUMBER);
                startActivity(intent);
                break;
            case R.id.att_fab:
                floatingMenu.close(true);
                Intent intent1 = new Intent(this, AttendanceActivity.class);
                intent1.putExtra("class_number", MY_CLASS_NUMBER);
                startActivity(intent1);
                /*Intent intent1 = new Intent(getActivity(), SampleActivity.class);
                startActivity(intent1);*/
                break;
            case R.id.course_page_fab:
                floatingMenu.close(true);
                Intent intent2 = new Intent(this, CoursePageActivity.class);
                intent2.putExtra("course_code", myCourse.getCOURSE_CODE());
                startActivity(intent2);
                /*Intent intent1 = new Intent(getActivity(), SampleActivity.class);
                startActivity(intent1);*/
                break;
            case R.id.reminder_fab:
                Log.d("rem", "fab");
                Toast.makeText(this, "Reminders coming soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.miss_minus:
                if (MISS_COUNTS > 0) {
                    MISS_COUNTS--;
                    reflectAttendance();
                }
                break;
            case R.id.miss_plus:
                MISS_COUNTS++;
                reflectAttendance();
                break;
            case R.id.attend_minus:
                if (ATTEND_COUNTS > 0) {
                    ATTEND_COUNTS--;
                    reflectAttendance();
                }
                break;
            case R.id.attend_plus:
                ATTEND_COUNTS++;
                reflectAttendance();
                break;
            case R.id.full_view_tint:
                if(floatingMenu.isOpened()){
                    floatingMenu.close(true);
                }
                break;
            default:

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //((HomeActivity)getActivity()).hideToolbar();
        theme.refreshTheme();
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this,theme.getMyThemeMainColor()));
        //floatingMenu.setBackgroundResource(theme.getMyThemeMainColor())
        buildingTint.setImageResource(theme.getTodayHeaderColor());
        floatingMenu.setMenuButtonColorNormal(ContextCompat.getColor(this,theme.getMyThemeMainColor()));
        attFab.setColorNormal(ContextCompat.getColor(this,theme.getMyThemeMainColor()));
        marksFab.setColorNormal(ContextCompat.getColor(this,theme.getMyThemeMainColor()));
        coursePageFab.setColorNormal(ContextCompat.getColor(this,theme.getMyThemeMainColor()));
        setFonts();
    }

    private void reflectAttendance() {
        attendClassText.setText(ATTEND_COUNTS > 1 ? "If you attend " + ATTEND_COUNTS + " classes" : "If you attend " + ATTEND_COUNTS + " class");
        missClassText.setText(MISS_COUNTS > 1 ? "If you miss " + MISS_COUNTS + " classes" : "If you miss " + MISS_COUNTS + " class");
        int attClass = myCourse.getCOURSE_ATTENDANCE().getATTENDED_CLASSES();
        int totClass = myCourse.getCOURSE_ATTENDANCE().getTOTAL_CLASSES();
        int mulFactor = 1;
        if (myCourse.getCOURSE_TYPE_SHORT().equals("L")) {
            mulFactor = myCourse.getCOURSE_LTPC().getPRACTICAL();
        }
        if (dataHandler.getSemester().equals("SS")) {
            int modAtt = myCourse.getCOURSE_ATTENDANCE().getModifiedPercentage(attClass + ATTEND_COUNTS * 2 * mulFactor, totClass + (ATTEND_COUNTS + MISS_COUNTS) * 2 * mulFactor);
            attended.setText("attended " + (attClass + ATTEND_COUNTS * 2 * mulFactor) + " out " + (totClass + (ATTEND_COUNTS + MISS_COUNTS) * 2 * mulFactor));
            subPer.setText(modAtt + " %");
            //attBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), modAtt));

        } else {
            int modAtt = myCourse.getCOURSE_ATTENDANCE().getModifiedPercentage(attClass + ATTEND_COUNTS * mulFactor, totClass + (ATTEND_COUNTS + MISS_COUNTS) * mulFactor);
            attended.setText("attended " + (attClass + ATTEND_COUNTS * mulFactor) + " out " + (totClass + (ATTEND_COUNTS + MISS_COUNTS) * mulFactor));
            subPer.setText(modAtt + " %");
            //attBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), modAtt));

        }
    }

    public void setSubNameId(String s) {
        SUB_NAME_ID = s;
    }

    public void setImageNameId(String s) {
        IMAGE_NAME_ID = s;
    }

}

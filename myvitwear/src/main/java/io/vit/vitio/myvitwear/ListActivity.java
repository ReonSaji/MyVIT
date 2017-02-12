package io.vit.vitio.myvitwear;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.vit.vitio.R;

public class ListActivity extends WearableActivity implements WearableListView.ClickListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView noContent;
    private WearableListView listView;
    private List<Course> listCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        setInit();
        setData();
    }

    private void init() {
        listView = (WearableListView) findViewById(R.id.wearable_list);
        noContent=(TextView)findViewById(R.id.no_content);
        mContainerView=(BoxInsetLayout)findViewById(R.id.container);
    }

    private void setInit() {
        setAmbientEnabled();
        listView.setClickListener(this);
        toggleVisibility(false);
    }

    private void setData() {
        if(DataHandler.getInstance(this).getCoursesJson()!=null){
            try {
                ParseCourses parseCourses=new ParseCourses(new JSONArray(DataHandler.getInstance(this).getCoursesJson()));
                listCourses=parseCourses.getCoursesList();
                if(listCourses!=null&&listCourses.size()>0){
                    listView.setAdapter(new Adapter(this, listCourses));
                }else{
                    toggleVisibility(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                toggleVisibility(true);
            }
        }else{
            toggleVisibility(true);
        }
    }

    private void toggleVisibility(boolean noContentVisible) {
        if(noContentVisible){
            noContent.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            noContent.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        /*if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }*/
    }

    // WearableListView click listener
    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer tag = (Integer) v.itemView.getTag();
        // use this data to complete some action ...
        Intent intent=new Intent(this,CardActivity.class);
        intent.putExtra("class_number",listCourses.get(tag.intValue()).getCLASS_NUMBER());
        startActivity(intent);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

}

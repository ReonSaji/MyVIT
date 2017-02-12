package io.vit.vitio.myvitwear;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.CardScrollView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.vit.vitio.R;

public class CardActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);


    private TextView code,title;
    private CardScrollView cardScrollView;
    private CardFrame cardFrame;
    private RecyclerView wearableRecyclerView;
    private String classNumber;
    private Course myCourse;
    private CourseDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        init();
        setInit();
        setData();

    }

    private void init() {
        //cardScrollView = (CardScrollView) findViewById(R.id.card_scroll_view);
        cardFrame= (CardFrame) findViewById(R.id.card_frame);
        wearableRecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        code=(TextView)findViewById(R.id.code);
        title=(TextView)findViewById(R.id.title);
    }


    private void setInit() {

        setAmbientEnabled();
        /*cardScrollView.setExpansionEnabled(true);
        cardScrollView.setExpansionDirection(1);
        cardScrollView.setExpansionFactor(10.0F);*/

        cardFrame.setExpansionEnabled(true);
        cardFrame.setExpansionDirection(1);
        cardFrame.setExpansionFactor(10.0F);

        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent().hasExtra("class_number")) {
            classNumber=getIntent().getStringExtra("class_number");
        }else {
            finish();
        }
        wearableRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void setData() {

        if(DataHandler.getInstance(this).getCoursesJson()!=null){
            try {
                ParseCourses parseCourses=new ParseCourses(new JSONArray(DataHandler.getInstance(this).getCoursesJson()));
                List<Course> listCourses=parseCourses.getCoursesList();
                if(listCourses!=null&&listCourses.size()>0){
                    for(Course c:listCourses){
                        if(c.getCLASS_NUMBER().equals(classNumber)){
                            myCourse=c;
                            break;
                        }
                    }
                }else{
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                finish();
            }
        }else{
            finish();
        }

        if(myCourse!=null){
            adapter=new CourseDetailsAdapter(myCourse.getItemList());
            wearableRecyclerView.setAdapter(adapter);
            code.setText(myCourse.getCOURSE_CODE());
            title.setText(myCourse.getCOURSE_TITLE());
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

    public class CourseDetailsAdapter extends RecyclerView.Adapter<CourseDetailsAdapter.CourseViewHolder>{

        private List<Item> itemList;

        public CourseDetailsAdapter(List<Item> itemList) {
            this.itemList = itemList;
        }

        @Override
        public CourseDetailsAdapter.CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.course_item,parent,false);
            CourseViewHolder courseViewHolder=new CourseViewHolder(view);
            return courseViewHolder;
        }

        @Override
        public void onBindViewHolder(CourseDetailsAdapter.CourseViewHolder holder, int position) {
            holder.valueView.setText(itemList.get(position).value);
            holder.titleView.setText(itemList.get(position).title);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
        public class CourseViewHolder extends RecyclerView.ViewHolder{

            TextView titleView,valueView;

            public CourseViewHolder(View itemView) {
                super(itemView);
                titleView=(TextView)itemView.findViewById(R.id.title);
                valueView=(TextView)itemView.findViewById(R.id.value);
            }
        }
    }


}

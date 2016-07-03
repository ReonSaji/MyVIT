package io.vit.vitio;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Extras.ErrorDefinitions;
import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.Instances.Faculty;
import io.vit.vitio.Managers.ConnectAPI;
import io.vit.vitio.Managers.Parsers.ParseCoursePage;

/**
 * Created by alfainfinity on 09/01/16.
 */
public class CoursePageActivity extends AppCompatActivity implements ConnectAPI.RequestListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Spinner slotSpinner;
    private LinearLayout outerLayout;
    private ConnectAPI connectAPI;
    private String courseId;
    private FacultyListAdapter facultyListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        init();
        setInit();
        //setData();
    }

    private void init() {
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        //progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        slotSpinner=(Spinner)findViewById(R.id.slot_spinner);
        //outerLayout=(LinearLayout)findViewById(R.id.outer_layout);
        connectAPI=new ConnectAPI(this);

    }

    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> defaultSlotList=new ArrayList<>();
        defaultSlotList.add("Filter Slot");
        slotSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, defaultSlotList));
        ArrayList<Faculty> defaultFacultyList=new ArrayList<>();
        Faculty defFaculty=new Faculty();
        defFaculty.setNAME("Filter Faculty");
        defFaculty.setNUMBER("");
        defaultFacultyList.add(defFaculty);
        connectAPI.setOnRequestListener(this);
        //courseId=getIntent().getStringExtra("course_code");

    }

    private void setData() {
        connectAPI.getCoursePageSlots(courseId);
    }

    @Override
    public void onRequestInitiated(int code) {
        Log.d("fetchCoursePage","initiated");
        showLoaders();
    }

    @Override
    public void onRequestCompleted(ReturnParcel parcel, int code) {
        Log.d("coursePage","reqcom");

        hideLoaders();
        if(parcel.getRETURN_CODE()== ErrorDefinitions.CODE_SUCCESS){

            ParseCoursePage parseCoursePage= (ParseCoursePage) parcel.getRETURN_PARCEL_OBJECT();
            if(parseCoursePage.getType()==ParseCoursePage.IS_SLOTS){
                slotSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,parseCoursePage.getCoursePageSlotList()));
            }  else if(parseCoursePage.getType()==ParseCoursePage.IS_UPLOADS){
                facultyListAdapter=new FacultyListAdapter(this,parseCoursePage.getCoursePageUploadList());
                recyclerView.setAdapter(facultyListAdapter);
            }else{
                finish();
            }
        }
        else{
            showMessage(parcel.getRETURN_MESSAGE());
        }
    }

    @Override
    public void onErrorRequest(ReturnParcel parcel, int code) {
        hideLoaders();
        showMessage(parcel.getRETURN_MESSAGE());
    }

    private void showLoaders() {

        outerLayout.setVisibility(LinearLayout.VISIBLE);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    private void hideLoaders(){
        outerLayout.setVisibility(LinearLayout.GONE);
        progressBar.setVisibility(ProgressBar.GONE);
    }

    public void showMessage(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    class FacultySpinnerAdapter extends ArrayAdapter<Faculty>{

        public FacultySpinnerAdapter(Context context, List<Faculty> facultyList) {
            super(context, android.R.layout.simple_list_item_2,facultyList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView);
        }

        public View initView(int position, View convertView){
            if(convertView==null){
                convertView=View.inflate(getContext(),android.R.layout.simple_list_item_2,null);
            }
            TextView tv1=(TextView)convertView.findViewById(android.R.id.text1);
            TextView tv2=(TextView)convertView.findViewById(android.R.id.text2);
            tv1.setTextColor(ContextCompat.getColor(CoursePageActivity.this,R.color.white));
            tv2.setTextColor(ContextCompat.getColor(CoursePageActivity.this,R.color.white));
            tv1.setText(getItem(position).getNAME());
            tv2.setText(getItem(position).getNUMBER());
            return convertView;
        }
    }

    private class FacultyListAdapter extends RecyclerView.Adapter<FacultyListAdapter.FacultyViewHolder> {
        private final List<String> list;
        private LayoutInflater inflater;
        private Context c;

        public FacultyListAdapter(Context context, List<String> list) {
            this.list = list;
            c = context;
            inflater = LayoutInflater.from(c);
        }


        @Override
        public FacultyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.course_page_activity_recycler_row, parent, false);
            FacultyViewHolder facultyViewHolder = new FacultyViewHolder(view);
            return facultyViewHolder;
        }


        @Override
        public void onBindViewHolder(FacultyViewHolder holder, int position) {
            if (list != null) {
                holder.uploadTitle.setText(list.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if (list != null)
                return list.size();
            else return 0;
        }



        class FacultyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView uploadTitle;

            public FacultyViewHolder(View itemView) {
                super(itemView);
                uploadTitle = (TextView) itemView.findViewById(R.id.upload_title);
                if (list != null)
                    itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }

        }
    }

}

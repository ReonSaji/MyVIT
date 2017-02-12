package io.vit.vitio.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.vit.vitio.Extras.Blur.HoverTouchHelper;
import io.vit.vitio.Extras.ErrorDefinitions;
import io.vit.vitio.Extras.MyThumbnail;
import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Fragments.FileExplorerFragment;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Faculty;
import io.vit.vitio.Instances.GenericUpload;
import io.vit.vitio.Instances.Lecture;
import io.vit.vitio.Managers.ConnectAPI;
import io.vit.vitio.Managers.CoursePageDownloadService;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseCoursePage;
import io.vit.vitio.R;

public class CoursePageActivity extends AppCompatActivity implements ConnectAPI.RequestListener,
        AdapterView.OnItemSelectedListener, FileExplorerFragment.OnRefreshListener {

    private static final String TAG = CoursePageActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 25;
    private static int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 22;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Spinner coursesSpinner, slotsSpinner, facultiesSpinner;
    private LinearLayout outerLayout;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout frameLayout;
    private TextView instructor, courseHead, slotHead, facultyHead;

    private FileExplorerFragment fileExFragment;

    private List<Course> allCoursesList;
    private ArrayList<String> defaultCourseList;
    private ArrayList<String> defaultSlotList;
    private ArrayList<Faculty> defaultFacultiesList;
    private Map<String, ArrayList> uploadsMap;
    private UploadsListAdapter uploadsListAdapter;

    private String initCourseCode;
    private boolean canTriggerSpinners;
    private boolean isInitProcessed = true;

    private MyTheme myTheme;
    private DataHandler dataHandler;
    private ConnectAPI connectAPI;
    private Typeface typeface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(io.vit.vitio.R.layout.activity_course_page);
        init();
        setInit();
        setData();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        slotsSpinner = (Spinner) findViewById(R.id.slot_spinner);
        coursesSpinner = (Spinner) findViewById(R.id.course_spinner);
        facultiesSpinner = (Spinner) findViewById(R.id.faculty_spinner);
        //outerLayout=(LinearLayout)findViewById(R.id.outer_layout);
        instructor = (TextView) findViewById(R.id.instructor);
        courseHead = (TextView) findViewById(R.id.course_head);
        slotHead = (TextView) findViewById(R.id.slot_head);
        facultyHead = (TextView) findViewById(R.id.faculty_head);
        connectAPI = new ConnectAPI(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        dataHandler = DataHandler.getInstance(this);
        allCoursesList = dataHandler.getCoursesList();
        if (getIntent().hasExtra("course")) {
            initCourseCode = getIntent().getStringExtra("course");
            Log.i(TAG, "init: course" + initCourseCode);
        }

        myTheme = new MyTheme(this);

        fileExFragment = new FileExplorerFragment();
    }

    private void setInit() {
        hideLoaders();
        fileExFragment.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        connectAPI.setOnRequestListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_nav_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Course Page");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myTheme.refreshTheme();
        setFonts();
        ((AppBarLayout) findViewById(R.id.appbar)).setBackgroundResource(myTheme.getMyThemeBackgroundColor());

        slotsSpinner.setEnabled(false);
        facultiesSpinner.setEnabled(false);
        defaultFacultiesList = new ArrayList<>();
        defaultSlotList = new ArrayList<>();
        defaultCourseList = new ArrayList<>();

        defaultCourseList.add("Select Course");

        defaultFacultiesList.add(new Faculty("Select Faculty"));

        defaultSlotList.add("Select Slot");
        slotsSpinner.setAdapter(new GeneralSpinnerAdapter(this, defaultSlotList));
        facultiesSpinner.setAdapter(new FacultySpinnerAdapter(this, defaultFacultiesList));
        coursesSpinner.setAdapter(new GeneralSpinnerAdapter(this, defaultCourseList));
    }

    private void setData() {
        //connectAPI.getCoursePageSlots(courseId);

        //SAmple data
        defaultCourseList = new ArrayList<>();
        for (int i = 0; i < allCoursesList.size(); i++) {
            defaultCourseList.add(allCoursesList.get(i).getCOURSE_CODE() + " - " + allCoursesList.get(i).getCOURSE_TITLE());
        }


        coursesSpinner.setAdapter(new GeneralSpinnerAdapter(this, defaultCourseList));
        if (initCourseCode != null) {
            Log.i(TAG, "setData: initCourse" + initCourseCode);
            for (int i = 0; i < defaultCourseList.size(); i++) {
                if (initCourseCode.equals(defaultCourseList.get(i).split("-")[0].trim())) ;
                {
                    isInitProcessed = true;
                    Log.i(TAG, "setData: equals");
                    coursesSpinner.setSelection(i, true);
                    break;
                }

            }
            isInitProcessed = true;
        }
        isInitProcessed = true;
        coursesSpinner.setOnItemSelectedListener(this);
        slotsSpinner.setOnItemSelectedListener(this);
        facultiesSpinner.setOnItemSelectedListener(this);

        showLoaders("Please select a course", false);

    }

    private void setFonts() {
        typeface = myTheme.getMyThemeTypeface();
        courseHead.setTypeface(typeface);
        slotHead.setTypeface(typeface);
        facultyHead.setTypeface(typeface);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.download_directory) {
            //showMessage("Download directory clicked");
            /*Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/My VIT/"));
            Log.i(TAG, "onOptionsItemSelected: " + uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setType("vnd.android.cursor.dir*//*");
            if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
                startActivity(Intent.createChooser(intent, "Open Folder"));
            } else {
                showMessage("Unable to open");
            }*/
            //Intent intent=new Intent(this,FileExplore.class);
            //startActivity(intent);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                fileExFragment = new FileExplorerFragment();
                fileExFragment.setOnRefreshListener(this);
                fileExFragment.show(getFragmentManager(), "show");
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    showPermissionRationale();

                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestInitiated(int code) {
        Log.d("fetchCoursePage", "initiated");
        switch (code) {
            case ConnectAPI.COURSE_PAGE_SLOTS_CODE:
                showLoaders("Fetching Slolts...", true);
                break;
            case ConnectAPI.COURSE_PAGE_FACULTIES_CODE:
                showLoaders("Fetching Faculties...", true);
                break;
            case ConnectAPI.COURSE_PAGE_UPLOADS_CODE:
                showLoaders("Loading Course Page...", true);
                break;
            case ConnectAPI.LOGIN_CODE:
                showLoaders("Loading...", true);
        }
    }

    @Override
    public void onRequestCompleted(ReturnParcel parcel, int code) {
        Log.d("coursePage", "reqcom");

        hideLoaders();
        if (parcel.getRETURN_CODE() == ErrorDefinitions.CODE_SUCCESS) {

            ParseCoursePage parseCoursePage = (ParseCoursePage) parcel.getRETURN_PARCEL_OBJECT();

            switch (code) {
                case ConnectAPI.COURSE_PAGE_SLOTS_CODE:
                    defaultSlotList = (ArrayList) parseCoursePage.getCoursePageSlotList();
                    if (defaultSlotList.size() >= 1) {
                        canTriggerSpinners = true;
                        slotsSpinner.setAdapter(new GeneralSpinnerAdapter(this, defaultSlotList));
                        slotsSpinner.setEnabled(true);
                        showLoaders("Select Slot", false);

                    } else {
                        showLoaders("No slots available", false);
                        canTriggerSpinners = false;
                        slotsSpinner.setAdapter(new GeneralSpinnerAdapter.Builder(this).defaultAdapter().build());
                    }
                    break;
                case ConnectAPI.COURSE_PAGE_FACULTIES_CODE:
                    defaultFacultiesList = (ArrayList<Faculty>) parseCoursePage.getCoursePageFacultyList();
                    if (defaultFacultiesList.size() >= 1) {
                        canTriggerSpinners = true;
                        facultiesSpinner.setAdapter(new FacultySpinnerAdapter(this, parseCoursePage.getCoursePageFacultyList()));
                        facultiesSpinner.setEnabled(true);
                        showLoaders("Select Faculty", false);
                    } else {
                        showLoaders("No faculties available", false);
                        canTriggerSpinners = false;
                        facultiesSpinner.setAdapter(new FacultySpinnerAdapter.Builder(this).defaultAdapter().build());
                    }
                    break;
                case ConnectAPI.COURSE_PAGE_UPLOADS_CODE:
                    uploadsMap = parseCoursePage.getCoursePageUploadMap();
                    Log.i(TAG, "onRequestCompleted: map" + uploadsMap.toString());
                    if (uploadsMap.size() >= 1) {
                        uploadsListAdapter = new UploadsListAdapter(this, uploadsMap);
                        recyclerView.setAdapter(uploadsListAdapter);
                        hideLoaders();
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        showLoaders("No material found", false);
                    }
            }
        } else {
            showLoaders(parcel.getRETURN_MESSAGE(), false);
        }
    }

    @Override
    public void onErrorRequest(ReturnParcel parcel, int code) {
        hideLoaders();
        showMessage(parcel.getRETURN_MESSAGE());
    }

    private void showLoaders(String s, boolean showProgressBar) {

        instructor.setVisibility(LinearLayout.VISIBLE);
        instructor.setText(s);
        if (showProgressBar)
            progressBar.setVisibility(ProgressBar.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideLoaders() {
        instructor.setVisibility(LinearLayout.GONE);
        progressBar.setVisibility(ProgressBar.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    public void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.course_spinner:
                if (isInitProcessed)
                    canTriggerSpinners = false;
                connectAPI.loginAndGetCoursePageSlots(allCoursesList.get(position).getCOURSE_CODE());
                slotsSpinner.setEnabled(false);
                facultiesSpinner.setEnabled(false);
                try {
                    slotsSpinner.setAdapter(new GeneralSpinnerAdapter.Builder(this).add("Select Slot").build());
                    facultiesSpinner.setAdapter(new FacultySpinnerAdapter.Builder(this).add(new Faculty("Select faculty", null)).build());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.slot_spinner:
                facultiesSpinner.setEnabled(false);
                try {
                    facultiesSpinner.setAdapter(new FacultySpinnerAdapter.Builder(this).add(new Faculty("Select faculty", null)).build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (canTriggerSpinners)
                    connectAPI.loginAndGetCoursePageFaculties(allCoursesList.get(coursesSpinner.getSelectedItemPosition()).getCOURSE_CODE(), defaultSlotList.get(position));
                break;
            case R.id.faculty_spinner:
                if (canTriggerSpinners)
                    connectAPI.loginAndGetCoursePageUploads(allCoursesList.get(coursesSpinner.getSelectedItemPosition()).getCOURSE_CODE(),
                            slotsSpinner.getSelectedItem().toString(), defaultFacultiesList.get(position).getNUMBER());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onRefresh(boolean shouldShow) {
        if (shouldShow) {
            fileExFragment.dismiss();
            fileExFragment.show(getFragmentManager(), "show");
        }
    }

    static class FacultySpinnerAdapter extends ArrayAdapter<Faculty> {

        public FacultySpinnerAdapter(Context context, List<Faculty> facultyList) {
            super(context, android.R.layout.simple_list_item_2, facultyList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return initView(position, convertView, false);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView, true);
        }

        public View initView(int position, View convertView, boolean b) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.activity_course_page_spinner_item, null);
            }
            TextView tv1 = (TextView) convertView.findViewById(R.id.spinnerText);
            tv1.setText(getItem(position).getNAME());
            if (!b) {
                tv1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }
            return convertView;
        }

        public static class Builder {
            private List<Faculty> list;
            private Context context;

            public Builder(Context context) {
                this.context = context;
                list = new ArrayList<>();
            }

            public Builder add(Faculty faculty) throws Exception {
                if (faculty != null && faculty.getNAME() != null) {
                    list.add(faculty);
                } else {
                    throw new Exception("Invalid entry. Either Faculty instance is null or getNAME returned null");
                }
                return this;
            }

            public Builder defaultAdapter() {
                Faculty faculty = new Faculty();
                faculty.setNAME("N/A");
                list.add(faculty);
                return this;
            }

            public FacultySpinnerAdapter build() {
                return new FacultySpinnerAdapter(context, list);
            }
        }


    }

    static class GeneralSpinnerAdapter extends ArrayAdapter<String> {

        public GeneralSpinnerAdapter(Context context, List<String> list) {
            super(context, R.layout.activity_course_page_spinner_item, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return initView(position, convertView, false);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView, true);
        }

        public View initView(int position, View convertView, boolean b) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.activity_course_page_spinner_item, null);
            }
            TextView tv1 = (TextView) convertView.findViewById(R.id.spinnerText);
            tv1.setText(getItem(position));
            if (!b) {
                tv1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }
            return convertView;
        }

        public static class Builder {
            private final Context context;
            private List<String> list;

            public Builder(Context context) {
                this.context = context;
                list = new ArrayList<>();
            }

            public Builder add(String item) throws Exception {
                if (item != null) {
                    list.add(item);
                } else {
                    throw new Exception("Invalid entry. Item is null");
                }
                return this;
            }

            public Builder defaultAdapter() {
                list.add("N/A");
                return this;
            }

            public GeneralSpinnerAdapter build() {
                return new GeneralSpinnerAdapter(context, list);
            }
        }
    }

    private class UploadsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_UPLOAD = 1;
        private static final int TYPE_HEAD = 2;
        private final Map<String, ArrayList> map;
        private ArrayList<Object> uploadArrayList;
        private LayoutInflater inflater;
        private Context c;

        public UploadsListAdapter(Context context, Map<String, ArrayList> map) {
            this.map = map;
            c = context;
            inflater = LayoutInflater.from(c);
            Iterator<String> iterator = map.keySet().iterator();
            uploadArrayList = new ArrayList<>();
            while (iterator.hasNext()) {
                //uploadArrayList.add(null);
                String key = iterator.next();
                uploadArrayList.add(key);
                uploadArrayList.addAll(map.get(key));
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_UPLOAD) {
                View view = inflater.inflate(R.layout.activity_course_page_recycler_row_upload, parent, false);
                UploadViewHolder uploadViewHolder = new UploadViewHolder(view);
                return uploadViewHolder;
            } else {
                View view = inflater.inflate(R.layout.activity_course_page_recycler_row_head, parent, false);
                HeadViewHolder headViewHolder = new HeadViewHolder(view);
                return headViewHolder;
            }

        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (getItemViewType(position) == TYPE_UPLOAD) {

                UploadViewHolder uploadViewHolder = (UploadViewHolder) holder;
                Object item = uploadArrayList.get(position);
                Bundle expandData = new Bundle();
                String selectedCode = allCoursesList.get(coursesSpinner.getSelectedItemPosition()).getCOURSE_CODE();
                String selectedSlot = slotsSpinner.getSelectedItem().toString();
                String selectedFaculty = ((Faculty) facultiesSpinner.getSelectedItem()).getNAME();
                expandData.putString("course_code", selectedCode);
                expandData.putString("course_slot", selectedSlot);
                expandData.putString("course_faculty", selectedFaculty);
                if (item instanceof Lecture) {
                    Log.i(TAG, "onBindViewHolder: type lecture");
                    Lecture lecture = (Lecture) item;
                    uploadViewHolder.uploadDate.setVisibility(View.VISIBLE);
                    uploadViewHolder.uploadDay.setVisibility(View.VISIBLE);
                    uploadViewHolder.uploadTitle.setText(lecture.getTopic());
                    uploadViewHolder.uploadDate.setText(lecture.getDate().substring(0, lecture.getDate().lastIndexOf("-")));
                    uploadViewHolder.uploadDay.setText(lecture.getDay());
                    uploadViewHolder.setDownProgress(lecture.getProgress());
                    uploadViewHolder.link = lecture.getLink();
                    expandData.putString("link", lecture.getLink());
                    expandData.putString("name", lecture.getTopic());
                    expandData.putString("date", lecture.getDate().substring(0, lecture.getDate().lastIndexOf("-")));
                    expandData.putString("day", lecture.getDay());
                } else if (item instanceof GenericUpload) {
                    Log.i(TAG, "onBindViewHolder: type genric");
                    uploadViewHolder.uploadTitle.setText(((GenericUpload) item).getFileName());
                    uploadViewHolder.uploadDate.setVisibility(View.GONE);
                    uploadViewHolder.uploadDay.setVisibility(View.GONE);
                    uploadViewHolder.link = ((GenericUpload) item).getLink();
                    expandData.putString("link", ((GenericUpload) item).getLink());
                    expandData.putString("name", ((GenericUpload) item).getFileName());
                    expandData.putString("date", null);
                    expandData.putString("day", null);
                    uploadViewHolder.setDownProgress(((GenericUpload) item).getProgress());
                }
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (map.get(key).contains(item)) {
                        expandData.putString("head", key);
                        break;
                    }
                }
                HoverTouchHelper.make(frameLayout, uploadViewHolder.uploadTitle, coordinatorLayout, expandData);

            } else {
                /*int c = 0;
                int i = 0;
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    int d = c + i;
                    String key = iterator.next();
                    if (position == d) {
                        HeadViewHolder viewHolder = (HeadViewHolder) holder;
                        viewHolder.head.setText(key);
                        break;
                    }
                    c += map.get(key).size();
                }*/
                HeadViewHolder viewHolder = (HeadViewHolder) holder;
                viewHolder.head.setText(uploadArrayList.get(position).toString());

            }

        }

        @Override
        public int getItemCount() {
            /*if (map != null) {
                int c = 0;
                Iterator iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    c += map.get(iterator.next()).size();
                }
                c += map.size();
                return c;
            } else return 0;*/
            return uploadArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            //Log.i(TAG, "getItemViewType:position " + position);
            /*int c = 0;
            int i = 0;
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                //Log.i(TAG, "getItemViewType:c:i:c+i: " + c + ":" + i + ":" + (c + i));
                if (position == c + i) {
                    return TYPE_HEAD;
                }
                c += map.get(iterator.next()).size();
                i++;

                //Log.i(TAG, "getItemViewType:after:c:i:c+i: " + c + ":" + i + ":" + (c + i));
            }
            return TYPE_UPLOAD;*/
            if (uploadArrayList.get(position) instanceof String) {
                return TYPE_HEAD;
            } else {
                return TYPE_UPLOAD;
            }

        }

        public ArrayList<Object> getUploadsList() {
            return uploadArrayList;
        }

        class UploadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            MyThumbnail uploadTitle;
            TextView uploadDate, uploadDay;
            FrameLayout downloadView;
            LinearLayout downloadProgress;
            MyThumbnail outer;
            FrameLayout root;
            String link;

            public UploadViewHolder(View itemView) {
                super(itemView);
                uploadTitle = (MyThumbnail) itemView.findViewById(R.id.file_name);
                uploadTitle.setSelected(true);
                uploadDate = (TextView) itemView.findViewById(R.id.date);
                uploadDay = (TextView) itemView.findViewById(R.id.day);
                downloadView = (FrameLayout) itemView.findViewById(R.id.download_view);
                downloadProgress = (LinearLayout) itemView.findViewById(R.id.download_progress);
                root = (FrameLayout) itemView.findViewById(R.id.root);
                uploadDate.setTypeface(typeface);
                uploadDay.setTypeface(typeface);
                uploadTitle.setTypeface(typeface);
                downloadProgress.setBackgroundResource(myTheme.getMyThemeMainColor());


                downloadView.setOnClickListener(this);
            }


            @Override
            public void onClick(View view) {
                try {

                    final GenericUpload upload = ((GenericUpload) uploadArrayList.get(getAdapterPosition()));
                    String path = CoursePageDownloadService.BASE_PATH +
                            allCoursesList.get(coursesSpinner.getSelectedItemPosition()).getCOURSE_CODE() +
                            "/" + slotsSpinner.getSelectedItem().toString() +
                            "/" + ((Faculty) facultiesSpinner.getSelectedItem()).getNAME() + "/";
                    if (ContextCompat.checkSelfPermission(CoursePageActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        final String filePathIfExists = checkFileExists(path, upload.getFileName());
                        Log.i(TAG, "onClick: " + filePathIfExists);
                        if (filePathIfExists != null) {
                            AlertDialog alertDialog = null;
                            final AlertDialog.Builder builder = new AlertDialog.Builder(CoursePageActivity.this)
                                    .setTitle("Choose an action")
                                    .setMessage("It appears you have already downloaded this file previously.")
                                    .setCancelable(false)
                                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                        }
                                    })
                                    .setPositiveButton("Open file", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            openFile(CoursePageActivity.this, new File(filePathIfExists));
                                        }
                                    })
                                    .setNegativeButton("Download file", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                                dialog.dismiss();
                                                downloadFile(uploadArrayList.get(getAdapterPosition()), getAdapterPosition());
                                            }
                                        }
                                    });
                            alertDialog = builder.create();
                            alertDialog.show();


                        } else if (upload.getProgress() > 0) {
                            showMessage("Download in progress");
                        } else {
                            downloadFile(uploadArrayList.get(getAdapterPosition()), getAdapterPosition());
                        }

                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(CoursePageActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                            showPermissionRationale();

                        } else {

                            ActivityCompat.requestPermissions(CoursePageActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Error");
                }

            }

            public void setDownProgress(int progress) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        0, progress);
                downloadProgress.setLayoutParams(params);
            }

        }

        private void downloadFile(Object o, int adapterPosition) {
            String link = "";
            if (o instanceof Lecture) {
                link = ((Lecture) o).getLink();
            } else {
                link = ((GenericUpload) o).getLink();
            }

            Log.i(TAG, "onClick: link" + link);
            if (link != null && !link.equals("")) {
                Intent downloadIntent = new Intent(CoursePageActivity.this, CoursePageDownloadService.class);
                downloadIntent.putExtra("pos", adapterPosition);
                downloadIntent.putExtra("course_code", allCoursesList.get(coursesSpinner.getSelectedItemPosition()).getCOURSE_CODE());
                downloadIntent.putExtra("course_slot", slotsSpinner.getSelectedItem().toString());
                downloadIntent.putExtra("course_faculty", ((Faculty) facultiesSpinner.getSelectedItem()).getNAME());
                downloadIntent.putExtra("link", link);
                downloadIntent.putExtra("name", ((GenericUpload) o).getFileName());
                downloadIntent.putExtra("receiver", new DownloadReceiver(new Handler()));

                if (ContextCompat.checkSelfPermission(CoursePageActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new LoginTask().execute(downloadIntent);

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CoursePageActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        showPermissionRationale();

                    } else {

                        ActivityCompat.requestPermissions(CoursePageActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                    }
                }
            }
        }

        class HeadViewHolder extends RecyclerView.ViewHolder {

            TextView head;

            public HeadViewHolder(View itemView) {
                super(itemView);
                head = (TextView) itemView.findViewById(R.id.head);
                head.setTypeface(typeface);
            }


        }
    }

    private String checkFileExists(String path, String fileName) {

        File checkFile = new File(path);
        File[] list = checkFile.listFiles();
        if (list != null && list.length > 0) {
            Log.i(TAG, "checkFileExists: " + true);
            for (File f : list) {
                Log.i(TAG, "checkFileExists List: " + f.getAbsolutePath());
                if (f.getName().substring(0, f.getName().lastIndexOf(".")).equals(fileName) ||
                        f.getName().equals(fileName)) {
                    return f.getAbsolutePath();
                }
            }
        }
        return null;
    }

    private void showPermissionRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Access to External Storage")
                .setMessage("This permission is required so that all your course page downloads" +
                        " can be arranged in the folders sequentially which will make it easier to access" +
                        " the files and folders.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.requestPermissions(CoursePageActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                    }
                });
        builder.create().show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMessage("Thank you for granting permission. Please try downloading again.");
            }
        }else if (requestCode == MY_PERMISSIONS_READ_EXTERNAL_STORAGE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMessage("Thank you for granting permission. Now you will be able to access files.");
            }
        }
    }



    @SuppressLint("ParcelCreator")
    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == CoursePageDownloadService.UPDATE_PROGRESS) {
                Log.i(TAG, "onReceiveResult: " + resultData.toString());
                int progress = resultData.getInt("progress");
                int pos = resultData.getInt("pos");
                String courseCode = resultData.getString("course_code");
                String courseSlot = resultData.getString("course_slot");
                String courseFaculty = resultData.getString("course_faculty");
                String fn = resultData.getString("name");
                Log.i(TAG, "onReceiveResult: namefile:" + fn);
                if (courseCode.equals(allCoursesList.get(coursesSpinner.getSelectedItemPosition()).getCOURSE_CODE()) &&
                        courseSlot.equals(slotsSpinner.getSelectedItem().toString()) &&
                        courseFaculty.equals(((Faculty) facultiesSpinner.getSelectedItem()).getNAME())) {
                    if (uploadsListAdapter != null) {

                        ((GenericUpload) uploadsListAdapter.getUploadsList().get(pos)).setProgress(progress);
                        ((GenericUpload) uploadsListAdapter.getUploadsList().get(pos)).setFileName(fn);


                        uploadsListAdapter.notifyItemChanged(pos);
                    }
                }
            }
        }


    }

    public class LoginTask extends AsyncTask<Intent, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Intent... params) {
            if (true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CoursePageActivity.this.startService(params[0]);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMessage("Login Failed");
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public static void openFile(final Context context, File url) {
        // Create URI
        File file = url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No app to open this type of file", Toast.LENGTH_SHORT).show();
        }

    }

}

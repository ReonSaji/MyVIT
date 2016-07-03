package io.vit.vitio.Fragments.Profile;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.vit.vitio.Extras.CustomViewPager;
import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.FacultyActivity;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.R;

/**
 * Created by Prince Bansal Local on 05-02-2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView regNoHead, schoolHead, regNo, school, facultyAdvisorHead, gradesHead;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FrameLayout facultyAdvisorLayout,gradesLayout;
    private ImageView profileImage;

    private MyTheme myTheme;
    private Typeface typeface;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        init(rootView);
        setInit(rootView);

        return rootView;
    }


    private void init(View rootView) {

        regNoHead = (TextView) rootView.findViewById(R.id.regno_head);
        schoolHead = (TextView) rootView.findViewById(R.id.school_head);
        facultyAdvisorHead = (TextView) rootView.findViewById(R.id.faculy_ad_head);
        gradesHead = (TextView) rootView.findViewById(R.id.grades_head);
        regNo = (TextView) rootView.findViewById(R.id.regno);
        school = (TextView) rootView.findViewById(R.id.school);
        profileImage=(ImageView)rootView.findViewById(R.id.profile_image);

        collapsingToolbarLayout=(CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing_toolbar);

        facultyAdvisorLayout=(FrameLayout)rootView.findViewById(R.id.faculy_advisor_layout);
        gradesLayout=(FrameLayout)rootView.findViewById(R.id.grades_layout);

        myTheme=new MyTheme(getActivity());

    }


    private void setInit(View rootView) {
        collapsingToolbarLayout.setTitle("Prince Bansal");

        facultyAdvisorLayout.setOnClickListener(this);
        gradesLayout.setOnClickListener(this);
        Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primary = ContextCompat.getColor(getActivity(),myTheme.getMyThemeMainColor());
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        myTheme.refreshTheme();
        ((HomeActivity) getActivity()).setToolbarFormat(1);
        ((HomeActivity) getActivity()).changeStatusBarColor(1);
        collapsingToolbarLayout.setContentScrimColor(myTheme.getMyThemeMainColor());
        setFonts();
    }

    private void setFonts() {
        typeface=myTheme.getMyThemeTypeface();
        regNoHead.setTypeface(myTheme.getMyThemeTypeface());
        schoolHead.setTypeface(myTheme.getMyThemeTypeface());
        facultyAdvisorHead.setTypeface(myTheme.getMyThemeTypeface());
        gradesHead.setTypeface(myTheme.getMyThemeTypeface());
        regNo.setTypeface(myTheme.getMyThemeTypeface());
        school.setTypeface(myTheme.getMyThemeTypeface());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.faculy_advisor_layout:
                startActivity(new Intent(getActivity(), FacultyActivity.class));
                break;
            case R.id.grades_layout:
                break;
        }
    }
}

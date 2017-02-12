/*
 * Copyright (c) 2015 GDG VIT Vellore.
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.vit.vitio.Fragments.Courses;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.vit.vitio.Extras.SlidingTabLayout;
import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Activities.HomeActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Grade;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.R;

/**
 * Created by shalini on 16-06-2015.
 */
public class CoursesFragment extends Fragment {
    public static List<Course> allCoursesList;
    private DataHandler dataHandler;
    private static final int NUM_PAGES = 8;
    private ViewPager pager;
    private TextView currentHeader,earnedHeader,totalHeader,currentContent,earnedContent,totalContent;
    private SliderAdapter adapter;
    private SlidingTabLayout tabs;
    private Typeface typeface;
    private MyTheme theme;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.courses_fragment, container, false);
        Log.d("oncreate", "oncrate");
        init(rootView);
        setInit();
        setTransitions();
        setData();
        return rootView;
    }

    private void init(ViewGroup rootView) {
        Log.d("initcf", "initcf");
        adapter=new SliderAdapter(getChildFragmentManager());
        pager= (ViewPager) rootView.findViewById(R.id.pager);
        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        dataHandler=DataHandler.getInstance(getActivity());
        allCoursesList=dataHandler.getCoursesList();
        theme=new MyTheme(getActivity());

        currentHeader=(TextView)rootView.findViewById(R.id.current_head);
        earnedHeader=(TextView)rootView.findViewById(R.id.earned_head);
        totalHeader=(TextView)rootView.findViewById(R.id.total_head);

        currentContent=(TextView)rootView.findViewById(R.id.current_content);
        earnedContent=(TextView)rootView.findViewById(R.id.left_content);
        totalContent=(TextView)rootView.findViewById(R.id.total_content);
    }


    private void setInit() {
        Log.d("setinitcf", "setinitcf");

        //tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.transparent);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout

        setFonts();
    }

    private void setData() {
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        setCredits();
    }

    private void setCredits() {
        int sum=0;
        for(Course c:allCoursesList){
            sum+=c.getCOURSE_LTPC().getCREDITS();
        }
        currentContent.setText(String.valueOf(sum));
    }


    private void setTransitions() {
        if(Build.VERSION.SDK_INT>=21) {
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
            setReenterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onresume", "onresume");
        ((HomeActivity) getActivity()).setToolbarFormat(1);
        ((HomeActivity) getActivity()).changeStatusBarColor(1);
        String s="a";
        Grade d=new Grade(s);
        theme.refreshTheme();
        tabs.setViewPager(pager);
        currentContent.setTextColor(ContextCompat.getColor(getContext(),theme.getMyThemeMainColor()));
        earnedContent.setTextColor(ContextCompat.getColor(getContext(),theme.getMyThemeMainColor()));
        totalContent.setTextColor(ContextCompat.getColor(getContext(),theme.getMyThemeMainColor()));
        //tabs.setBackgroundColor(theme.getToolbarColorTypedArray().getColor(1,-1));
        setFonts();
    }

    private void setFonts() {
        typeface = theme.getMyThemeTypeface();
        currentHeader.setTypeface(typeface);
        earnedHeader.setTypeface(typeface);
        totalHeader.setTypeface(typeface);
        currentContent.setTypeface(typeface);
        earnedContent.setTypeface(typeface);
        totalContent.setTypeface(typeface);
    }

    private class SliderAdapter extends FragmentStatePagerAdapter {
        String[] tabs;

        public SliderAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            tabs = getResources().getStringArray(R.array.tabs_name_courses);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("getItem", String.valueOf(position));
            PagerFragment fragment= new PagerFragment();
            Bundle bundle=new Bundle();
            bundle.putInt("mode", position);
            fragment.setArguments(bundle);
            return fragment;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override

        public int getCount() {
            return NUM_PAGES;
        }
    }

}

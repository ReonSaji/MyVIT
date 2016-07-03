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

package io.vit.vitio.Fragments.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Fragments.Spotlight.SpotlightFragment;
import io.vit.vitio.Instances.Message;
import io.vit.vitio.Managers.Parsers.ParseSpotlight;
import io.vit.vitio.R;

/**
 * Created by shalini on 28-06-2015.
 */
public class FacultyAdvisorFragment extends Fragment {

    private RecyclerView recyclerView;
    private Typeface typeface;
    private MyTheme myTheme;
    private int MODE = 0;
    private ParseSpotlight parseSpotlight;
    private TextView noContentView;
    private List<Message> myMessages;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.spotlight_pager_fragment, container, false);
        init(rootView);
        setInit();
        setData();
        return rootView;
    }


    private void init(ViewGroup rootView) {
    }


    private void setInit() {
    }

    private void setData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        myTheme.refreshTheme();
        typeface = myTheme.getMyThemeTypeface();
    }
}

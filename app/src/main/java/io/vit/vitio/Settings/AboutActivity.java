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

package io.vit.vitio.Settings;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import io.vit.vitio.Extras.TypeFaceSpan;
import io.vit.vitio.R;

/**
 * Created by shalini on 13-07-2015.
 */
public class AboutActivity extends AppCompatActivity{


    private TextView head,content1,content2,title1,title2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs_about_fragment);
        changeStatusBarColor(getResources().getColor(R.color.black));
        init();
        setFonts();
    }

    private void init() {

        head=(TextView)findViewById(R.id.head);
        content1=(TextView)findViewById(R.id.content1);
        content2=(TextView)findViewById(R.id.content2);
        title1=(TextView)findViewById(R.id.title1);
        title2=(TextView)findViewById(R.id.title2);
    }

    private void setFonts() {
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/segoe-bold.ttf");
        head.setTypeface(typeFace);
        content1.setTypeface(typeFace);
        content2.setTypeface(typeFace);
        title2.setTypeface(typeFace);
        title1.setTypeface(typeFace);
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(color);
        }
    }
}

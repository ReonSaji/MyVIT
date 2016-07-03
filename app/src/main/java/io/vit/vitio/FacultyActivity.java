package io.vit.vitio;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.vit.vitio.Extras.CustomViewPager;
import io.vit.vitio.Extras.Themes.MyTheme;

/**
 * Created by Prince Bansal Local on 05-02-2016.
 */
public class FacultyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, designation, school, contactHead, email, number, cabinHead, address, intercommNumber;
    private LinearLayout facultyInfoHolder,showBar;

    private MyTheme myTheme;
    private Typeface typeface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_activity);
        init();
        setInit();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.fac_name);
        designation = (TextView) findViewById(R.id.fac_designation);
        school = (TextView) findViewById(R.id.fac_school);
        contactHead = (TextView) findViewById(R.id.contact_head);
        email = (TextView) findViewById(R.id.fac_email);
        number = (TextView) findViewById(R.id.fac_number);
        cabinHead = (TextView) findViewById(R.id.cabin_head);
        address = (TextView) findViewById(R.id.fac_cabin_address);
        intercommNumber = (TextView) findViewById(R.id.fac_cabin_intercom);

        facultyInfoHolder=(LinearLayout)findViewById(R.id.fac_info_holder);
        showBar=(LinearLayout)findViewById(R.id.show_bar);

        myTheme=new MyTheme(this);

    }

    private void setInit() {

        toolbar.setNavigationIcon(R.drawable.ic_close_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myTheme.refreshTheme();
        name.setTextColor(ContextCompat.getColor(this,myTheme.getMyThemeMainColor()));
        contactHead.setTextColor(ContextCompat.getColor(this,myTheme.getMyThemeMainColor()));
        cabinHead.setTextColor(ContextCompat.getColor(this,myTheme.getMyThemeMainColor()));
        facultyInfoHolder.setBackgroundResource(myTheme.getMyThemeRoundedStrokeBackground());
        showBar.setBackgroundResource(myTheme.getMyThemeRoundedSolidBackground());

    }

    @Override
    protected void onResume() {
        super.onResume();
        myTheme.refreshTheme();
        setFonts();

    }

    private void setFonts() {
        typeface = myTheme.getMyThemeTypeface();

        name.setTypeface(myTheme.getMyThemeTypeface());
        designation.setTypeface(myTheme.getMyThemeTypeface());
        school.setTypeface(myTheme.getMyThemeTypeface());
        contactHead.setTypeface(myTheme.getMyThemeTypeface());
        email.setTypeface(myTheme.getMyThemeTypeface());
        number.setTypeface(myTheme.getMyThemeTypeface());
        cabinHead.setTypeface(myTheme.getMyThemeTypeface());
        address.setTypeface(myTheme.getMyThemeTypeface());
        intercommNumber.setTypeface(myTheme.getMyThemeTypeface());

    }
}

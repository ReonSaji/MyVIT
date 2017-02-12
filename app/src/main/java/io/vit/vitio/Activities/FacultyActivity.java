package io.vit.vitio.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.vit.vitio.Extras.CustomViewPager;
import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.R;

/**
 * Created by Prince Bansal Local on 05-02-2016.
 */
public class FacultyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, designation, school, contactHead, email, number, cabinHead, address, intercommNumber;
    private LinearLayout facultyInfoHolder,showBar;

    private MyTheme myTheme;
    private Typeface typeface;
    private ImageView facultyImage;

    private String sName,sDesignation,sSchool,sEmail,sNumber,sAddress,sIntercom,sPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_activity);
        init();
        setInit();
        setData();
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
        facultyImage=(ImageView)findViewById(R.id.faculty_image);

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

        //facultyImage.setImageDrawable(new ColorDrawable(ContextCompat.getColor(this,myTheme.getMyThemeMainColor())));

        sName=getIntent().hasExtra("name")?getIntent().getStringExtra("name"):"-";
        sDesignation=getIntent().hasExtra("desg")?getIntent().getStringExtra("desg"):"-";
        sSchool=getIntent().hasExtra("school")?getIntent().getStringExtra("school"):"-";
        sEmail=getIntent().hasExtra("email")?getIntent().getStringExtra("email"):"-";
        sNumber=getIntent().hasExtra("mobile")?getIntent().getStringExtra("mobile"):"-";
        sAddress=getIntent().hasExtra("room")?getIntent().getStringExtra("room"):"-";
        sIntercom=getIntent().hasExtra("intercom")?getIntent().getStringExtra("intercom"):"-";
        sPhoto=getIntent().hasExtra("photo")?getIntent().getStringExtra("photo"):"-";
    }

    private void setData() {
        name.setText(sName);
        designation.setText(sDesignation);
        school.setText(sSchool);
        email.setText(sEmail);
        number.setText(sNumber);
        address.setText(sAddress);
        intercommNumber.setText(sIntercom);

        if(sPhoto!=null&&!sPhoto.equals("-")) {
            byte[] decoded = Base64.decode(sPhoto, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            facultyImage.setImageBitmap(bitmap);
        }
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

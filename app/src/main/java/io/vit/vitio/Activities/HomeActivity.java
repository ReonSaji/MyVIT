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

package io.vit.vitio.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Fragments.Today.TodayFragment;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.QuickstartPreferences;
import io.vit.vitio.Managers.RegistrationIntentService;
import io.vit.vitio.Navigation.NavigationDrawerFragment;
import io.vit.vitio.R;

/**
 * Created by shalini on 14-06-2015.
 */
public class HomeActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LinearLayout nonDrawerView;
    private DataHandler dataHandler;
    private MyTheme theme;
    NavigationDrawerFragment navigationDrawerFragment;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = HomeActivity.class.getSimpleName();

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        checkFirstTimeUser();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarFormat(0);
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_fragment);
        navigationDrawerFragment.setUp(drawerLayout, toolbar, this);

    }


    private void init() {
        dataHandler = DataHandler.getInstance(this);
        nonDrawerView = (LinearLayout) findViewById(R.id.restdrawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        theme = new MyTheme(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken && !intent.getBooleanExtra("already", false)) {

                    Toast.makeText(HomeActivity.this, "Yay! You are registered to push notification.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        drawerLayout.addDrawerListener(this);
    }

    private void initializeFragment() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment today = new TodayFragment();
        ft.add(R.id.main_fragment_holder, today);
        if (Build.VERSION.SDK_INT >= 21)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        changeStatusBarColor(0);
    }

    public void slideLayout(float x) {
        nonDrawerView.setTranslationX(x);
    }

    public void setToolbarFormat(int pos) {
        theme.refreshTheme();
        if (!getSupportActionBar().isShowing()) {
            getSupportActionBar().show();
        }
        String title[] = getResources().getStringArray(R.array.drawer_list_titles);
        Log.d("setTooltheme", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("theme", "null"));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, theme.getMyThemeMainColor()));
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorres.getResourceId(pos, -1))));
        SpannableString s = new SpannableString(title[pos]);
        s.setSpan(theme.getMyThemeTypeFaceSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    public void setToolbarFormat(int pos, String t) {
        theme.refreshTheme();
        if (!getSupportActionBar().isShowing()) {
            getSupportActionBar().show();
        }
        String title = t;
        TypedArray colorres = theme.getToolbarColorTypedArray();
        Log.d("setTooltheme", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("theme", "null"));
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorres.getResourceId(pos, -1))));
        SpannableString s = new SpannableString(title);
        s.setSpan(theme.getMyThemeTypeFaceSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    public void hideToolbar() {
        getSupportActionBar().hide();
    }

    private void checkFirstTimeUser() {
        if (dataHandler.getFirstTimeUser()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();

        } else {
            initializeFragment();
        }
    }

    public void changeStatusBarColor(int i) {
        theme.refreshTheme();
        Log.d("getStatheme", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("theme", "null"));
        Log.d("myTh", String.valueOf(theme.getMyThemeCode()));
        TypedArray colorres = theme.getStatusColorTypedArray();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(colorres.getResourceId(i, -1)));
        }
    }

    @Override
    public void onBackPressed() {
        /*if(getSupportFragmentManager().getBackStackEntryCount()!=0) {
            Log.d("cF", getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).toString());
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1)) {
                Log.d("instance", "today");
                finish();
            }
        }*/
        super.onBackPressed();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Install latest version of Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        Log.i(TAG, "onDrawerOpened: ");
        navigationDrawerFragment.updateProfileImage();
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}

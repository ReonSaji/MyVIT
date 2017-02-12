package io.vit.vitio.Fragments.Profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Activities.FacultyActivity;
import io.vit.vitio.Activities.HomeActivity;
import io.vit.vitio.Instances.Faculty;
import io.vit.vitio.Managers.ConnectAPI;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseCourses;
import io.vit.vitio.R;

/**
 * Created by Prince Bansal Local on 05-02-2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 23;
    private static final double IMAGE_MAX_SIZE = 512;
    private TextView regNoHead, schoolHead, regNo, school, facultyAdvisorHead, gradesHead;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FrameLayout facultyAdvisorLayout, gradesLayout;
    private ImageView profileImage, editImage;

    private MyTheme myTheme;
    private Typeface typeface;

    private DataHandler dataHandler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        init(rootView);
        setInit(rootView);
        setData();

        return rootView;
    }

    private void init(View rootView) {

        dataHandler = DataHandler.getInstance(getActivity());
        regNoHead = (TextView) rootView.findViewById(R.id.regno_head);
        schoolHead = (TextView) rootView.findViewById(R.id.school_head);
        facultyAdvisorHead = (TextView) rootView.findViewById(R.id.faculy_ad_head);
        gradesHead = (TextView) rootView.findViewById(R.id.grades_head);
        regNo = (TextView) rootView.findViewById(R.id.regno);
        school = (TextView) rootView.findViewById(R.id.school);
        profileImage = (ImageView) rootView.findViewById(R.id.profile_image);
        editImage = (ImageView) rootView.findViewById(R.id.edit_image);

        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        facultyAdvisorLayout = (FrameLayout) rootView.findViewById(R.id.faculy_advisor_layout);
        gradesLayout = (FrameLayout) rootView.findViewById(R.id.grades_layout);

        myTheme = new MyTheme(getActivity());

    }


    private void setInit(View rootView) {

        facultyAdvisorLayout.setOnClickListener(this);
        gradesLayout.setOnClickListener(this);
        editImage.setOnClickListener(this);

    }


    private void setData() {
        try {

            if (dataHandler.getProfileImagePath() != null) {

                setProfileImage(dataHandler.getProfileImagePath());
            }
            else {
                if (dataHandler.getProfileImageEncoded() == null) {
                    ConnectAPI connectAPI = new ConnectAPI(getActivity());
                    connectAPI.setOnRequestListener(new ConnectAPI.RequestListener() {
                        @Override
                        public void onRequestInitiated(int code) {
                            if (code == ConnectAPI.PROFILE_IMAGE_CODE)
                                profileImage.setImageResource(R.drawable.done_for_day);
                        }

                        @Override
                        public void onRequestCompleted(ReturnParcel parcel, int code) {
                            if (code == ConnectAPI.PROFILE_IMAGE_CODE) {
                                String data = (String) parcel.getRETURN_PARCEL_OBJECT();
                                byte[] decoded = Base64.decode(data.substring(1, data.length() - 1), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                                profileImage.setImageBitmap(bitmap);
                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        int primary = ContextCompat.getColor(getActivity(), myTheme.getMyThemeMainColor());
                                        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                                    }
                                });
                            }
                        }

                        @Override
                        public void onErrorRequest(ReturnParcel parcel, int code) {
                            profileImage.setImageResource(R.drawable.weekend);
                        }
                    });
                    connectAPI.loginAndFetchProfileImage();

                } else {
                    String data = dataHandler.getProfileImageEncoded();
                    byte[] decoded = Base64.decode(data.substring(1, data.length() - 1), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                    profileImage.setImageBitmap(bitmap);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int primary = ContextCompat.getColor(getActivity(), myTheme.getMyThemeMainColor());
                            collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                        }
                    });

                }
            }

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

        regNo.setText(dataHandler.getRegNo());
        school.setText(dataHandler.getSchool());
        collapsingToolbarLayout.setTitle(dataHandler.getName());
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
        typeface = myTheme.getMyThemeTypeface();
        regNoHead.setTypeface(myTheme.getMyThemeTypeface());
        schoolHead.setTypeface(myTheme.getMyThemeTypeface());
        facultyAdvisorHead.setTypeface(myTheme.getMyThemeTypeface());
        gradesHead.setTypeface(myTheme.getMyThemeTypeface());
        regNo.setTypeface(myTheme.getMyThemeTypeface());
        school.setTypeface(myTheme.getMyThemeTypeface());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faculy_advisor_layout:
                Intent facultyAdvisorIntent = new Intent(getActivity(), FacultyActivity.class);
                Faculty f = ParseCourses.parseFacultyAdvisor(dataHandler.getFacultyAdvisor());
                if (f != null) {
                    facultyAdvisorIntent.putExtra("name", f.getNAME());
                    facultyAdvisorIntent.putExtra("desg", f.getDESIGNATION());
                    facultyAdvisorIntent.putExtra("email", f.getEMAIL());
                    facultyAdvisorIntent.putExtra("intercom", f.getINTERCOM());
                    facultyAdvisorIntent.putExtra("mobile", f.getMOBILE());
                    facultyAdvisorIntent.putExtra("room", f.getROOM());
                    facultyAdvisorIntent.putExtra("school", f.getSCHOOL());
                    facultyAdvisorIntent.putExtra("photo", f.getPHOTO());
                }
                startActivity(facultyAdvisorIntent);

                break;
            case R.id.grades_layout:
                Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_image:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), 0);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        showPermissionRationale();

                    } else {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                    }
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == getActivity().RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String[] fileColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, fileColumns, null, null, null);
            cursor.moveToFirst();
            int cIndex = cursor.getColumnIndex(fileColumns[0]);
            String picturePath = cursor.getString(cIndex);
            cursor.close();
            if (picturePath != null) {
                DataHandler.getInstance(getActivity()).saveProfileImagePath(picturePath);
                setProfileImage(picturePath);
            }
        }
    }

    private void showPermissionRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Access to External Storage")
                .setMessage("This permission is required to access pictures from phone's internal" +
                        " storage. The image can thus be used as your profile image")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.requestPermissions(getActivity(),
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
                showMessage("Thank you for granting permission.");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 0);

            }
        }
    }

    private void showMessage(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    private void setProfileImage(final String picturePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = decodeFile(new File(picturePath));
                    if (bitmap != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                profileImage.setImageBitmap(bitmap);
                            }
                        });
                    }else{
                        dataHandler.saveProfileImagePath(null);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(ProfileFragment.this.getActivity(),"Problem loading file",Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    private Bitmap decodeFile(File f) throws Exception{
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();

        return b;
    }

}

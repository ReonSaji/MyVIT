package io.vit.vitio.Managers;

import android.net.Uri;


public class DatabaseContract {


    //Content Types
    public static final String CONTENT_ITEM_TYPE_COURSE = "io.vit.vitio.cursor.item/vitio.course";
    public static final String CONTENT_TYPE_DIR_COURSE = "io.vit.vitio.cursor.dir/vitio.course";

    public static final String AUTHORITY = "io.vit.vitio.provider";
    // content://<authority>/<path to type>
    public static final Uri CONTENT_URI_COURSE = Uri.parse("content://" + AUTHORITY + "/courses");


}

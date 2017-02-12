package io.vit.vitio.Managers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import static io.vit.vitio.Managers.DatabaseContract.AUTHORITY;


public class ConnectContentProvider extends ContentProvider {

    public static final UriMatcher URI_MATCHER = buildUriMatcher();
    public static final String PATH_COURSE = "courses";
    public static final int PATH_TOKEN_COURSE = 100;
    public static final String PATH_FOR_ID_COURSE = "courses/*";
    public static final int PATH_FOR_ID_TOKEN_COURSE = 200;

    // Uri Matcher for the content provider
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AUTHORITY;
        matcher.addURI(authority, PATH_COURSE, PATH_TOKEN_COURSE);
        matcher.addURI(authority, PATH_FOR_ID_COURSE, PATH_FOR_ID_TOKEN_COURSE);
        return matcher;
    }

    // Content Provider stuff

    private ConnectDatabase dbHelper;

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        dbHelper = new ConnectDatabase(ctx);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case PATH_TOKEN_COURSE:
                return DatabaseContract.CONTENT_TYPE_DIR_COURSE;
            case PATH_FOR_ID_TOKEN_COURSE:
                return DatabaseContract.CONTENT_ITEM_TYPE_COURSE;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            // retrieve course list
            case PATH_TOKEN_COURSE: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ConnectDatabase.TABLE_COURSES);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_FOR_ID_TOKEN_COURSE: {
                int classNbr = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ConnectDatabase.TABLE_COURSES);
                builder.appendWhere(ConnectDatabase.KEY_CLASNBR + "=" + classNbr);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        switch (token) {
            case PATH_TOKEN_COURSE: {
                long id = db.insertWithOnConflict(ConnectDatabase.TABLE_COURSES, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return DatabaseContract.CONTENT_URI_COURSE.buildUpon().appendPath(String.valueOf(id)).build();
            }
            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        int rowsDeleted = -1;
        switch (token) {
            case (PATH_TOKEN_COURSE):
                rowsDeleted = db.delete(ConnectDatabase.TABLE_COURSES, selection, selectionArgs);
                break;
            case (PATH_FOR_ID_TOKEN_COURSE):
                String courseIdWhereClause = ConnectDatabase.KEY_CLASNBR + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    courseIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(ConnectDatabase.TABLE_COURSES, courseIdWhereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // Notifying the changes, if there are any
        if (rowsDeleted != -1)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    /**
     * Man..I'm tired..
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        switch (token) {
            case PATH_TOKEN_COURSE: {
                long id = db.update(ConnectDatabase.TABLE_COURSES, values, selection, selectionArgs);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return (int) id;
            }
            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }

}
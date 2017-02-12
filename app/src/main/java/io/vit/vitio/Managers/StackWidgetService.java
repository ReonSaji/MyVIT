/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vit.vitio.Managers;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Managers.Parsers.ParseCourses;
import io.vit.vitio.R;
import io.vit.vitio.Widget.MainWidget;
import io.vit.vitio.Widget.WidgetItem;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    class StackRemoteViewsFactory implements RemoteViewsFactory {
        private int mCount = 10;
        private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
        private Context mContext;
        private int mAppWidgetId;


        public StackRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        public void onCreate() {
            // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
            // for example downloading or creating content etc, should be deferred to onDataSetChanged()
            // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
            Cursor cursor=getContentResolver().query(DatabaseContract.CONTENT_URI_COURSE,
                    new String[]{ ConnectDatabase.KEY_CLASNBR,ConnectDatabase.KEY_TITLE,ConnectDatabase.KEY_CODE,
                    ConnectDatabase.KEY_ATTENDANCE,ConnectDatabase.KEY_TYPE_SHORT,ConnectDatabase.KEY_VENUE},null,null, null);
            mCount=cursor.getCount();
            for (int i = 0; i < mCount; i++) {
                cursor.moveToNext();
                try {
                    mWidgetItems.add(new WidgetItem(cursor.getString(1),cursor.getString(2),
                            ParseCourses.getAttendance(new JSONObject(cursor.getString(3))).getPERCENTAGE()+" %",
                            cursor.getString(0),cursor.getString(4),cursor.getString(5)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            /*if(true){
                for (int i = 0; i < 5; i++) {
                        mWidgetItems.add(new WidgetItem("Software Engineering "+i,"ITE30"+i,
                                "7"+i+" %",
                                "1234",i%2==0?"l":"t","SJT10"+i));
                }
            }*/

            // We sleep for 3 seconds here to show how the empty view appears in the interim.
            // The empty view is set in the StackWidgetProvider and should be a sibling of the
            // collection view.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void onDestroy() {
            // In onDestroy() you should tear down anything that was setup for your data source,
            // eg. cursors, connections, etc.
            mWidgetItems.clear();
        }

        public int getCount() {
            return mCount;
        }


        public RemoteViews getViewAt(int position) {
            // position will always range from 0 to getCount() - 1.

            // We construct a remote views item based on our widget item xml file, and set the
            // text based on the position.
            Log.i("widget", "getViewAt: "+mWidgetItems.get(position).courseTitle+":"+mWidgetItems.get(position).type);
            String type="";
            if(mWidgetItems.get(position).type.equalsIgnoreCase("t"))
                type="theory";
            else if(mWidgetItems.get(position).type.equalsIgnoreCase("l"))
                type="lab";
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            rv.setTextViewText(R.id.subject_code, mWidgetItems.get(position).courseCode);
            rv.setTextViewText(R.id.subject_name, mWidgetItems.get(position).courseTitle);
            rv.setTextViewText(R.id.subject_type, type);
            rv.setTextViewText(R.id.subject_per, mWidgetItems.get(position).attendance);
            rv.setTextViewText(R.id.subject_venue, mWidgetItems.get(position).venue);

            // Next, we set a fill-intent which will be used to fill-in the pending intent template
            // which is set on the collection view in StackWidgetProvider.
            Bundle extras = new Bundle();
            extras.putInt(MainWidget.EXTRA_ITEM, position);
            extras.putString("class_number",mWidgetItems.get(position).classNumber);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

            // You can do heaving lifting in here, synchronously. For example, if you need to
            // process an image, fetch something from the network, etc., it is ok to do it here,
            // synchronously. A loading view will show up in lieu of the actual contents in the
            // interim.


            // Return the remote views object.
            return rv;
        }

        public RemoteViews getLoadingView() {
            // You can create a custom loading view (for instance when getViewAt() is slow.) If you
            // return null here, you will get the default loading view.
            return null;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public long getItemId(int position) {
            return position;
        }

        public boolean hasStableIds() {
            return true;
        }

        public void onDataSetChanged() {
            // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
            // on the collection view corresponding to this factory. You can do heaving lifting in
            // here, synchronously. For example, if you need to process an image, fetch something
            // from the network, etc., it is ok to do it here, synchronously. The widget will remain
            // in its current state while work is being done here, so you don't need to worry about
            // locking up the widget.
        }
    }
}
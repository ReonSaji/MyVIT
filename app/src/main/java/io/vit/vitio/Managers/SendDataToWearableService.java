package io.vit.vitio.Managers;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.vit.vitio.Instances.Course;

import static com.google.android.gms.wearable.Wearable.NodeApi;
import static io.vit.vitio.Managers.ConnectDatabase.COLUMNS;

/**
 * Created by Prince Bansal Local on 21-07-2016.
 */

public class SendDataToWearableService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = SendDataToWearableService.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private boolean isDAtaApi = false;


    public SendDataToWearableService() {
        super();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra("data")) {
            isDAtaApi = intent.getBooleanExtra("data", false);
            Log.i(TAG, "onHandleIntent: isData");
        }
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected: " + isDAtaApi);
        notifyWearable();
    }

   /* private void sendDataToWearable() {
        Log.i(TAG, "sendDataToWearable: called");
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/vitio");
        putDataMapReq.getDataMap().putInt("int", 2);
        putDataMapReq.getDataMap().putLong("Time", System.currentTimeMillis());
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        putDataReq.setUrgent();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if (result.getStatus().isSuccess()) {
                    Log.d("dataapimob", "Data item set: " + result.getDataItem().getUri());
                } else {
                    Log.i(TAG, "onResult: fail");
                }
            }
        });

    }*/


    public void notifyWearable() {
        Log.i(TAG, "notifyWearable: called");
        Context context = getApplicationContext();

        Uri coursesUri = DatabaseContract.CONTENT_URI_COURSE;

        // we'll query our contentProvider, as always
        Cursor cursor = context.getContentResolver().query(coursesUri, null, null, null, null);
        final JSONArray data = new JSONArray();
        while (cursor.moveToNext()) {


            try {
                Course course = Course.fromCursor(cursor);
                JSONObject values = new JSONObject();
                values.put(COLUMNS[0], course.getCLASS_NUMBER());
                values.put(COLUMNS[1], course.getCOURSE_TITLE());
                values.put(COLUMNS[2], course.getCOURSE_SLOT());
                //values.put(COLUMNS[3], course.getCOURSE_TYPE());
                values.put(COLUMNS[4], course.getCOURSE_TYPE_SHORT());
                //Log.d("type", course.getCOURSE_TYPE_SHORT());
                //values.put(COLUMNS[5], course.getCOURSE_LTPC().toString());
                values.put(COLUMNS[6], course.getCOURSE_CODE());
                //values.put(COLUMNS[7], course.getCOURSE_MODE());
                //values.put(COLUMNS[8], course.getCOURSE_OPTION());
                values.put(COLUMNS[9], course.getCOURSE_VENUE());
                values.put(COLUMNS[10], course.getCOURSE_FACULTY().getNAME());
                //values.put(COLUMNS[11], course.getCOURSE_REGISTRATIONSTATUS());
                //values.put(COLUMNS[12], course.getCOURSE_BILL_DATE());
                //values.put(COLUMNS[13], course.getCOURSE_BILL_NUMBER());
                //values.put(COLUMNS[14], course.getCOURSE_PROJECT_TITLE());
                //values.put(COLUMNS[15], course.getCOURSE_JSON().toString());
                values.put(COLUMNS[16], course.getCOURSE_ATTENDANCE().getPERCENTAGE());
                //values.put(COLUMNS[17], course.getCOURSE_JSON().getJSONArray("timings").toString());
                //values.put(COLUMNS[18], course.getCOURSE_JSON().getJSONObject("marks").toString());
                data.put(values);
            } catch (JSONException e) {
                //e.printStackTrace();
            }
        }
        cursor.close();


        Log.d("prince", "prince");

        if (mGoogleApiClient == null) {
            Log.i(TAG, "notifyWearable: gapi=null");

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = NodeApi.getConnectedNodes(mGoogleApiClient).await();
                Log.d("prince", "princep");
                for (Node node : nodes.getNodes()) {
                    Log.i(TAG, "run: node" + node.getDisplayName());
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient,
                            node.getId(), "/vitio", data.toString().getBytes()).await();
                    if (result.getStatus().isSuccess()) {
                        Log.i("Adapter", "run: Message " + data.toString() + " sent to " + node.getDisplayName());
                    } else {
                        Log.i("Adapter", "run: Error sending message");
                    }

                }
                mGoogleApiClient.disconnect();
                stopSelf();
            }
        }).start();

    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended: ");
        stopSelf();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(TAG, "onConnectionFailed: ");
        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        mGoogleApiClient.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

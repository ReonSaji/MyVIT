package io.vit.vitio.myvitwear.Services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.vit.vitio.myvitwear.DataHandler;

/**
 * Created by Prince Bansal Local on 14-07-2016.
 */

public class DataLayerListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.i("msg", "onMessageReceived: ");
        if (messageEvent.getPath().equals("/vitio")) {
            final String message = new String(messageEvent.getData());
            if (!TextUtils.isEmpty(message)) {
                Log.i("watch0", "onMessageReceived: " + message);
                DataHandler.getInstance(getApplicationContext()).saveCoursesJson(message);


            }

        }
    }


}

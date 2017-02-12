package io.vit.vitio.Managers;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import io.vit.vitio.R;

public class RegistrationIntentService extends IntentService {

    private static final String GCM_SERVER_URL="http://myffcs.in:8080/admin/message/register/";

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    SharedPreferences sharedPreferences;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER,false)){
            Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("already",true);
            LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(registrationComplete);
        }else {


            try {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
                // See https://developers.google.com/cloud-messaging/android/start for details on this file.
                // [START get_token]
                InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
               String token = instanceID.getToken(getString(R.string.gcm_senderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + token);

                // TODO: Implement this method to send any registration to your app's servers.
                sendRegistrationToServer(token);

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                // [END register_for_gcm]
            } catch (Exception e) {
                Log.d(TAG, "Failed to complete token refresh", e);
                // If an exception happens while fetching the new token or updating our registration data
                // on a third-party server, this ensures that we'll attempt the update at a later time.
                sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
            }
        }
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        String tempUrl=GCM_SERVER_URL+"?regID="+token;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, tempUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("gcmserverResponse",response);
                        sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();

                        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
                        registrationComplete.putExtra("already",false);
                        LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(registrationComplete);
                        // Notify UI that registration has completed, so the progress indicator can be hidden.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,2,1));
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
package io.vit.vitio.myvitwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

import io.vit.vitio.R;

public class WearActivity extends Activity implements DataApi.DataListener,MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button button;
    private DelayedConfirmationView delayedView;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);
                button = (Button) findViewById(R.id.button);
                delayedView = (DelayedConfirmationView) findViewById(R.id.delayedView);
                delayedView.setTotalTimeMs(3000);
                showOnlyButton();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.disconnect();
            Wearable.DataApi.removeListener(mGoogleApiClient,this);
            Wearable.MessageApi.removeListener(mGoogleApiClient,this);
        }
    }

    public void beginCountdown(View view) {
        button.setVisibility(View.GONE);
        delayedView.setVisibility(View.VISIBLE);
        delayedView.setListener(new DelayedConfirmationView.DelayedConfirmationListener() {
            @Override
            public void onTimerFinished(View view) {
                Intent intent=new Intent(WearActivity.this, PickerActivity.class);
                startActivity(intent);
            }
            @Override
            public void onTimerSelected(View view) {
                Intent intent=new Intent(WearActivity.this, ConfirmationActivity.class);
                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,ConfirmationActivity.SUCCESS_ANIMATION);
                startActivity(intent);
            }
        });
        delayedView.start();
    }

    public void showOnlyButton() {
        button.setVisibility(View.VISIBLE);
        delayedView.setVisibility(View.GONE);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d("dsad","data");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("dsad","msg");

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient,this);
        Wearable.MessageApi.addListener(mGoogleApiClient,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

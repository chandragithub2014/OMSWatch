package watch.oms.omswatch.MessageAPI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import watch.oms.omswatch.MainActivity;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

/**
 * Created by 245742 on 9/28/2015.
 */
public class ListenerService extends WearableListenerService  /*implements OMSReceiveListener*/ {
    private  String webServiceMessage;
    GoogleApiClient mGoogleApiClient;


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i("test", "onMessageReceived()");
        if(messageEvent.getPath().equals("/message_path")) {
            final String message = new String(messageEvent.getData());
            Log.d("TAG", "Received JSON Message:::" + message);
            if(message.contains("config")){
                String configResponse = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG", "ConfigResponse:::" + configResponse);
                OMSApplication.getInstance().setConfigDataAPIResponse(configResponse);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("result", "config");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }else if(message.contains("transget")){
                String transGetResponse = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","TransResponse::::"+transGetResponse);
                OMSApplication.getInstance().setTransDataAPIResponse(transGetResponse);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("result", "transget");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }else if(message.contains("transpost")){
                String transPostResponse = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","transPostResponse::::"+transPostResponse);
                OMSApplication.getInstance().setTransPostDataAPIResponse(transPostResponse);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("result", "transpost");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        Log.d("TAG", "OnDataChanged Wear");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().equals("/image")) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                Asset profileAsset = dataMapItem.getDataMap().getAsset("imageurl");

                Bitmap bitmap = loadBitmapFromAsset(profileAsset);

                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                messageIntent.putExtra("byteArray", bs.toByteArray());

                // messageIntent.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

                //         ProductDetailFragment.setImage(bitmap);
                // Do something with the bitmap
            }
        }
    }


    public Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
                mGoogleApiClient.blockingConnect(40, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w("ListenerService", "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }
    /*@Override
        public void onDataChanged(DataEventBuffer dataEvents) {

            super.onDataChanged(dataEvents);
        }
    */
    /*private void makeWebServiceCalls(){
        if(webServiceMessage.equalsIgnoreCase("reminderCount")){
            if(MedDataConstants.USE_TEST_SERVICE){
                new MedDataPostAsyncTaskHelper(ListenerService.this, ListenerService.this, "reminderCount").execute("https://test-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
            }else {
                new MedDataPostAsyncTaskHelper(ListenerService.this, ListenerService.this, "reminderCount").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
            }
        }
    }*/
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ListenerService","ListenerService Mobile:::::");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }
/*
    @Override
    public void receiveResult(String result) {
        Log.d("TAG","Receive Result:::"+result);
        String next = "";
        if(result.equalsIgnoreCase("reminderCount")) {
            next = "worklistCount";
            String reminderResponse = MobileApplication.getInstance().getReminderList();
            try {
                JSONArray jsonArray1 = new JSONArray(reminderResponse);
                MobileApplication.getInstance().setReminderCount(jsonArray1.length());
                MessageService.getInstance().startMessageService(getApplicationContext(), "reminderCount");
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }
        if(result.equalsIgnoreCase("worklistCount")){
            next="";
            String workListResponse = MobileApplication.getInstance().getPatientList();
            try {
                JSONArray jsonArray1 = new JSONArray(workListResponse);
                MobileApplication.getInstance().setWorkListCount(jsonArray1.length());
                MessageService.getInstance().startMessageService(getApplicationContext(), "worklistCount");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        if(next.equalsIgnoreCase("worklistCount")) {
            if (MedDataConstants.USE_TEST_SERVICE) {
                new MedDataPostAsyncTaskHelper(ListenerService.this, ListenerService.this, "worklistCount").execute("https://test-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
            } else {
                new MedDataPostAsyncTaskHelper(ListenerService.this, ListenerService.this, "worklistCount").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
            }
        }
    }*/
}

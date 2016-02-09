package watch.oms.omswatch.MessageAPI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import watch.oms.omswatch.application.OMSApplication;

/**
 * Created by CHANDRASAIMOHAN on 10/23/2015.
 */
public class MessageService implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {
    Context ctx;
    GoogleApiClient mGoogleApiClient;
    private static MessageService instance;
    private String type;

    private MessageService(){

    }

    public static MessageService getInstance(){
        if(instance == null){
            instance = new MessageService();
        }
        return instance;
    }
 /*  public MessageService(Context ctx){
       this.ctx=ctx;
   }*/
    @Override
    public void onConnected(Bundle bundle) {
        if(mGoogleApiClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                    for(Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result=null;
                        if(type.equalsIgnoreCase("config")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("config"+"$"+ OMSApplication.getInstance().getConfigDataAPIURL()).getBytes()).await();
                        } if(type.equalsIgnoreCase("transget")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("transget"+"$"+ OMSApplication.getInstance().getTransDataAPIURL()).getBytes()).await();
                        }

                   /*     if(type.equalsIgnoreCase("worklist")){
                            Log.d("TAG","Send message when WorkList");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("worklist"+"$"+MobileApplication.getInstance().getPatientList()).getBytes()).await();
                        }else if(type.equalsIgnoreCase("reminderlist")){
                            Log.d("TAG","Send message when reminderlist");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("reminderlist"+"$"+MobileApplication.getInstance().getReminderList()).getBytes()).await();
                        }else if(type.equalsIgnoreCase("myaccount")){
                            Log.d("TAG","Send message when myaccount");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("myaccount"+"$"+MobileApplication.getInstance().getAccountDetails()).getBytes()).await();
                        }else if(type.equalsIgnoreCase("locations")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("locations"+"$"+MobileApplication.getInstance().getLocationList()).getBytes()).await();
                            Log.d("TAG","Send message when locations"+"locations"+"$"+MobileApplication.getInstance().getLocationList());
                    }else if(type.equalsIgnoreCase("physicians")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("physicians"+"$"+MobileApplication.getInstance().getPhysicianList()).getBytes()).await();
                            Log.d("TAG","Send message when physicians"+"physicians"+"$"+MobileApplication.getInstance().getPhysicianList());
                        }else if(type.equalsIgnoreCase("disposition")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("disposition"+"$"+MobileApplication.getInstance().getDispositionList()).getBytes()).await();
                            Log.d("TAG","Send message when disposition"+"disposition"+"$"+MobileApplication.getInstance().getDispositionList());
                        }
                        else if(type.equalsIgnoreCase("gender")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("gender"+"$"+MobileApplication.getInstance().getGender()).getBytes()).await();
                            Log.d("TAG","Send message when gender"+"gender"+"$"+MobileApplication.getInstance().getGender());
                        }else if(type.equalsIgnoreCase("notestype")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("notestype"+"$"+MobileApplication.getInstance().getNotesType()).getBytes()).await();
                            Log.d("TAG","Send message when notestype"+"notestype"+"$"+MobileApplication.getInstance().getNotesType());
                        }else if(type.equalsIgnoreCase("billing")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("billing"+"$"+MobileApplication.getInstance().getBillingList()).getBytes()).await();
                            Log.d("TAG","Send message when billing"+"billing"+"$"+MobileApplication.getInstance().getBillingList());
                        }else if(type.equalsIgnoreCase("bulk")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("bulk"+"$"+MobileApplication.getInstance().getBulkUpdateResponse()).getBytes()).await();
                            Log.d("TAG","Send message when bulk"+"bulk"+"$"+MobileApplication.getInstance().getBulkUpdateResponse());
                        }else if(type.equalsIgnoreCase("accountUpdate")){
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("accountUpdate"+"$"+MobileApplication.getInstance().getAccount_update_response()).getBytes()).await();
                            Log.d("TAG","Send message when accountUpdate"+"accountUpdate"+"$"+MobileApplication.getInstance().getAccount_update_response());
                        }else if(type.equalsIgnoreCase("handoffSearch")){
                            //handoffSearch
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("handoffSearch"+"$"+MobileApplication.getInstance().getHandsOffSearchResponse()).getBytes()).await();
                            Log.d("TAG","Send message when handoffSearch"+"handoffSearch"+"$"+MobileApplication.getInstance().getHandsOffSearchResponse());
                        }else if(type.equalsIgnoreCase("handoffpatient")){
                            //handoffpatient
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("handoffpatient"+"$"+MobileApplication.getInstance().getPatientHandOffResponse()).getBytes()).await();
                            Log.d("TAG","Send message when handoffpatient"+"handoffpatient"+"$"+MobileApplication.getInstance().getPatientHandOffResponse());
                        }else if(type.equalsIgnoreCase("revertpatient")){
                            //revertpatient
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("revertpatient"+"$"+MobileApplication.getInstance().getPatientRevertResponse()).getBytes()).await();
                            Log.d("TAG","Send message when revertpatient"+"revertpatient"+"$"+MobileApplication.getInstance().getPatientRevertResponse());
                        }else  if(type.equalsIgnoreCase("reminderCount")){
                            Log.d("TAG","Send message when reminderCount");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("reminderCount"+"$"+ MobileApplication.getInstance().getReminderCount()).getBytes()).await();
                        }else  if(type.equalsIgnoreCase("worklistCount")){
                            Log.d("TAG","Send message when worklistCount");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("patientlistCount"+"$"+ MobileApplication.getInstance().getWorkListCount()).getBytes()).await();
                        }else if(type.equalsIgnoreCase("notes")){
                            Log.d("TAG","Send message when Notes");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("notes"+"$"+ MobileApplication.getInstance().getPatientNotes()).getBytes()).await();
                        } else if(type.equalsIgnoreCase("updatePatient")){
                            Log.d("TAG","Send message when updatePatient");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("updatePatient"+"$"+ MobileApplication.getInstance().getPatientRevertResponse()).getBytes()).await();
                        }

                        else {
                           result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", MobileApplication.getInstance().getPropertiesJSON().getBytes()).await();
                        }*/
                          if(!result.getStatus().isSuccess()){
                            Log.e("test", "error");
                        } else {
                            Log.i("test", "success!! sent to: " + node.getDisplayName());
                        }
                    }
                }
            }).start();

        } else {
            Log.e("test", "not connected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void startMessageService(Context ctx,String type){
        this.ctx=ctx;
        this.type=type;
        Log.d("TAG","Message Type:::"+type);
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    public void stopMessageService(Context ctx){
        this.ctx=ctx;
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}

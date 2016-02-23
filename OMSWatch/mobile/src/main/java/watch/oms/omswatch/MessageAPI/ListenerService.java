package watch.oms.omswatch.MessageAPI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;


import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import watch.oms.omswatch.MainActivity;
import watch.oms.omswatch.Parser.OMSConfigDBParser;
import watch.oms.omswatch.Parser.PostAsyncTaskHelper;
import watch.oms.omswatch.Parser.TransDBParser;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

/**
 * Created by 245742 on 9/28/2015.
 */
public class ListenerService extends WearableListenerService  implements OMSReceiveListener {
    private  String webServiceMessage;
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i("test", "onMessageReceived()");
       if(messageEvent.getPath().equals("/message_path")) {
            final String message = new String(messageEvent.getData());
          //   webServiceMessage = message;
            Log.d("TAG", "Received url:::" + message);

           if(message.contains("config")){
            String configURL  = message.substring(message.lastIndexOf("$") + 1);
               Log.d("TAG","Config URL::::"+configURL);
               webServiceMessage = configURL;
               OMSApplication.getInstance().setConfigDataAPIURL(configURL);
               makeWebServiceCalls("config");
           }else if(message.contains("transget")){
               String transURL  = message.substring(message.lastIndexOf("$") + 1);
               Log.d("TAG","transURL::::"+transURL);
               webServiceMessage = transURL;
               OMSApplication.getInstance().setTransDataAPIURL(transURL);
               makeWebServiceCalls("transget");
           }else if(message.contains("transpost")){
             String transPostURL = message.substring(message.lastIndexOf("$") + 1);
               Log.d("TAG","transPostURL::::"+transPostURL);
               webServiceMessage = transPostURL;
               OMSApplication.getInstance().setTransPostDataAPIURL(transPostURL);
               makeWebServiceCalls("transpost");
           }else if(message.contains("imageurl")){
               String imageURL = message.substring(message.lastIndexOf("$") + 1);
               Log.d("TAG","ImageURL::::::"+imageURL);
               OMSApplication.getInstance().setDataAPIImageURL(imageURL);




               MessageService.getInstance().startMessageService(getApplicationContext(), "imageurl");
           }

           /* else  if(message.equalsIgnoreCase("companion")) {
               // Bitmap bitmap  = getBitmapFromURL(message);
               Intent i = new Intent(this, MainActivity.class);
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(i);
          } else  if(message.contains("bulk")){
              String bulkList = message.substring(message.lastIndexOf("$") + 1);
              Log.d("TAG","bulkList:::"+bulkList);
              Intent i = new Intent(this, MainActivity.class);

              i.putExtra("BULK", "bulk");
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
              MobileApplication.getInstance().setBulkUpdatedList(bulkList);
          } else  if(message.contains("accountUpdate")){
              String account_updated_details = message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setUpdatedAccountDetails(account_updated_details);
              Log.d("TAG", "account_updated_details:::" + account_updated_details);
              Intent i = new Intent(this, MainActivity.class);

              i.putExtra("ACCOUNT_UPDATE", "accountUpdate");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);


          }else  if(message.contains("handoffSearch")){
              String handsOff_search_json  = message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setHandOffSearchJSON(handsOff_search_json);
              Log.d("TAG", "handsOff_search_json:::" + handsOff_search_json);
              Intent i = new Intent(this, MainActivity.class);

              i.putExtra("HANDOFF_SEARCH", "handoffSearch");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
          }else if(message.contains("handoffpatient")){
              String handoffpatient_json =  message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setHandOffPatientJSON(handoffpatient_json);
              Log.d("TAG", "handoffpatient_json:::" + handoffpatient_json);
              Intent i = new Intent(this, MainActivity.class);
              i.putExtra("HANDOFF_PATIENT", "handoffpatient");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
          }else if(message.contains("revertpatient")){
              String  revertpatient_json =  message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setPatientRevertJSON(revertpatient_json);
              Log.d("TAG", "revertpatient_json:::" + revertpatient_json);
              Intent i = new Intent(this, MainActivity.class);
              i.putExtra("REVERT_PATIENT", "revertpatient");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
          }else if(message.contains("notes")){
              String notes_json =  message.substring(message.lastIndexOf("$") + 1);
               MobileApplication.getInstance().setPatientNotes(notes_json);
              Log.d("TAG","Patient Notes request JSON::::"+notes_json);
              Intent i = new Intent(this, MainActivity.class);
              i.putExtra("NOTES_PATIENT", "notes");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);

          }else if(message.contains("patientUpdate")){
             String patientDetailUpdateJSON =  message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setUpdatePatientDetails(patientDetailUpdateJSON);
              Log.d("PatientUpdate","patientDetailUpdateJSON"+patientDetailUpdateJSON);
              Intent i = new Intent(this, MainActivity.class);
              i.putExtra("UPDATE_PATIENT", "updatePatient");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
          }
           else if (message.contains("reminderCount")){
              makeWebServiceCalls();
          }*/

       }else {
           super.onMessageReceived(messageEvent);
        }
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


    private void makeWebServiceCalls(String type){
       if(type.equalsIgnoreCase("config")){
           new OMSConfigDBParser(ListenerService.this, ListenerService.this).execute(OMSApplication.getInstance().getConfigDataAPIURL());
       } else if(type.equalsIgnoreCase("transget")){
          new TransDBParser(ListenerService.this,ListenerService.this).execute(OMSApplication.getInstance().getTransDataAPIURL());
       }else if(type.equalsIgnoreCase("transpost")){
             String response = OMSApplication.getInstance().getTransPostDataAPIURL();
           String transURL = "";
           JSONObject jsonPayload = null;
           try {
               JSONObject jsonObject = new JSONObject(response);
               transURL  = jsonObject.getString("posturl");
               jsonPayload = (jsonObject.getJSONObject("jsonpayload"));

           }

           catch (JSONException e){
               e.printStackTrace();
           }
           if(!TextUtils.isEmpty(transURL) && jsonPayload!=null){
               new PostAsyncTaskHelper(ListenerService.this,ListenerService.this,jsonPayload).execute(transURL);
           }
       }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ListenerService","ListenerService Mobile:::::");
    }

    @Override
    public void receiveResult(String result) {
        Log.d("TAG","Receive Result:::"+result);
        String next = "";
        if(result.contains(OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
                .getValue())) {
            next = "worklistCount";

            try {
               /* JSONArray jsonArray1 = new JSONArray(reminderResponse);
                MobileApplication.getInstance().setReminderCount(jsonArray1.length());*/
                MessageService.getInstance().startMessageService(getApplicationContext(), "config");
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }else if(result.contains(OMSMessages.NETWORK_RESPONSE_ERROR.getValue())){
            MessageService.getInstance().startMessageService(getApplicationContext(), "config");

        }else if(result.contains(OMSMessages.CONFIG_DB_ERROR.getValue())){
            MessageService.getInstance().startMessageService(getApplicationContext(), "config");
        } else if(result.equalsIgnoreCase("transgetfailure")){
            MessageService.getInstance().startMessageService(getApplicationContext(), "transget");
        }else if(result.equalsIgnoreCase("transgetsuccess")){
            MessageService.getInstance().startMessageService(getApplicationContext(), "transget");
        }else if(result.equalsIgnoreCase("transpostsuccess")){
            MessageService.getInstance().startMessageService(getApplicationContext(), "transpost");
        }else if(result.equalsIgnoreCase("transpostfailure")){
            MessageService.getInstance().startMessageService(getApplicationContext(), "transpost");
        }

    }
}

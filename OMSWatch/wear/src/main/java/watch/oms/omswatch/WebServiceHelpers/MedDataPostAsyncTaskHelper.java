package watch.oms.omswatch.WebServiceHelpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import watch.oms.omswatch.interfaces.OMSReceiveListener;

/**
 * Created by CHANDRASAIMOHAN on 10/23/2015.
 */
public class MedDataPostAsyncTaskHelper extends AsyncTask<String,Void,String> {
Context ctx;
    public static ProgressDialog pd =null;
    private OMSReceiveListener inReceiveListener;
    String url;
    String jsonObject;
    JSONObject requestjsonObject;
    String type="";
    private boolean isFace=false;


    public MedDataPostAsyncTaskHelper(Context ctx, OMSReceiveListener inReceiveListener, String type){
        this.ctx=ctx;
        this.inReceiveListener=inReceiveListener;
        this.requestjsonObject=requestjsonObject;
        this.type = type;
        isFace = true;

        pd = new ProgressDialog(ctx);
        pd.setMessage("Authorizing...");
        pd.setCancelable(false);
        pd.show();

    }


   /* public MedDataPostAsyncTaskHelper(Context ctx, OMSReceiveListener inReceiveListener, JSONObject requestjsonObject){
        this.ctx=ctx;
        this.inReceiveListener=inReceiveListener;
        this.requestjsonObject=requestjsonObject;
        pd = new ProgressDialog(ctx);
        pd.setMessage("Authorizing...");
        pd.setCancelable(false);
        pd.show();
    }

    public MedDataPostAsyncTaskHelper(Context ctx, OMSReceiveListener inReceiveListener, JSONObject requestjsonObject, String type){
        this.ctx=ctx;
        this.inReceiveListener=inReceiveListener;
        this.requestjsonObject=requestjsonObject;
        this.type = type;
        pd = new ProgressDialog(ctx);
        if(TextUtils.isEmpty(type)) {
            pd.setMessage("Authorizing..." + type);
        }else{
            pd.setMessage("Fetching..." + type);
        }
        pd.setCancelable(false);
        pd.show();
    }*/
    @Override
    protected String doInBackground(String... params) {
        String response="";
        url = params[0];
        response = doURLConnectionForWatch("http://androidexample.com/media/webservice/JsonReturn.php");
        return response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String response) {
        if(!TextUtils.isEmpty(response)){
            if(!isFace) {
                pd.dismiss();
            }
            Log.d("TAG", type+" Response::::" + response);
           // String flag = parseResponse(response);
            inReceiveListener.receiveResult("Y");

        }
     //   super.onPostExecute(response);
    }



    private String doURLConnectionForWatch(String requestURL){
     /*Login_Id: "test", Password: "test"
        */

        String response="";
        //do this wherever you are wanting to POST
        URL url;
        HttpURLConnection conn;

        String data ="";

        //String loginURL = "https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser";
        try{
            url=new URL(requestURL);
            //you need to encode ONLY the values of the parameters
           /* String param="Login_Id=" + URLEncoder.encode("test1","UTF-8")+
                    "&Password="+URLEncoder.encode("test@123","UTF-8");*/
            data +="&" + URLEncoder.encode("data", "UTF-8") + "="+"com";
        //    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.cognizant.com", 6050));

            conn=(HttpURLConnection)url.openConnection(/*proxy*/);
            //set the output to true, indicating you are outputting(uploading) POST data
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
//once you set the output to true, you don’t really need to set the request method to post, but I’m doing it anyway
            conn.setRequestMethod("POST");
            //     conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            //   conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

        /*    JSONObject loginJsonObject = new JSONObject();
            loginJsonObject.put("Login_Id", "test1");
            loginJsonObject.put("Password", "test@123");
            JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("request", loginJsonObject);*/
           /* //send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(URLEncoder.encode(requestJsonObject.toString(),"UTF-8"));
            out.flush();
            out.close();*/

            // Write serialized JSON data to output stream.
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(requestjsonObject.toString());

            // Close streams and disconnect.
            writer.close();
            out.close();
            //  urlConnection.disconnect();


            //build the string to store the response text from the server
            //  String response= “”;

//start listening to the stream
            Log.d("TAG","Response Code:::"+conn.getResponseCode());
            if(conn.getResponseCode() == 200) {
                Scanner inStream = new Scanner(conn.getInputStream());

//process the stream and store it in StringBuilder
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            }
        }
        //catch some error
        catch(MalformedURLException ex){
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Log.d("Reponse::::",""+response);
        return response;
    }

    private String doURLConnectionPost(String loginURL){
        /*Login_Id: "test", Password: "test"
        */

        String response="";
        //do this wherever you are wanting to POST
        URL url;
        HttpURLConnection conn;
        //String loginURL = "https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser";
        try{
            url=new URL(loginURL);
            //you need to encode ONLY the values of the parameters
            String param="Login_Id=" + URLEncoder.encode("test1","UTF-8")+
            "&Password="+URLEncoder.encode("test@123","UTF-8");

            conn=(HttpURLConnection)url.openConnection();
            //set the output to true, indicating you are outputting(uploading) POST data
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
//once you set the output to true, you don’t really need to set the request method to post, but I’m doing it anyway
            conn.setRequestMethod("POST");
       //     conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
         //   conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

        /*    JSONObject loginJsonObject = new JSONObject();
            loginJsonObject.put("Login_Id", "test1");
            loginJsonObject.put("Password", "test@123");
            JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("request", loginJsonObject);*/
           /* //send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(URLEncoder.encode(requestJsonObject.toString(),"UTF-8"));
            out.flush();
            out.close();*/

            // Write serialized JSON data to output stream.
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(requestjsonObject.toString());

            // Close streams and disconnect.
            writer.close();
            out.close();
          //  urlConnection.disconnect();


            //build the string to store the response text from the server
          //  String response= “”;

//start listening to the stream
            Log.d("TAG","Response Code:::"+conn.getResponseCode());
            if(conn.getResponseCode() == 200) {
                Scanner inStream = new Scanner(conn.getInputStream());

//process the stream and store it in StringBuilder
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            }
        }
        //catch some error
        catch(MalformedURLException ex){
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return response;
    }






}

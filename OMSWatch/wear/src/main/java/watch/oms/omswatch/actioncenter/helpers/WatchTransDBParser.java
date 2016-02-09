/*
 * Copyright (C) 2013 - Cognizant Technology Solutions. 
 * This file is a part of OneMobileStudio 
 * Licensed under the OneMobileStudio, Cognizant Technology Solutions, 
 * Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *      http://www.cognizant.com/
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package watch.oms.omswatch.actioncenter.helpers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import watch.oms.omswatch.MessageAPI.MessageService;
import watch.oms.omswatch.R;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.parser.OMSDBParserHelper;
import watch.oms.omswatch.parser.OMSServerMapperHelper;
import watch.oms.omswatch.utils.OMSAlertDialog;

public class WatchTransDBParser implements OMSReceiveListener {
	private final String TAG = this.getClass().getSimpleName();
	private Context appContext = null;
	//private ContentValues contentvalues = null;
	private ProgressDialog progressDialog = null;
	private OMSReceiveListener rListener = null;
	private OMSDBParserHelper configDBParserHelper = null;
	private OMSServerMapperHelper servermapperhelper = null;
	private String uniqueRowId = null;
	private SharedPreferences sp;

	private int configAppId = OMSDefaultValues.NONE_DEF_CNT.getValue();
    private String errorObject;
    private String errorMessageKey;
    private String errorMessageVal="";
    private String errorCodeKey="";
    private String errorCodeVal="";
    private String modifiedDate="0.0";
    private Map<String,String> rootAttribute;
    private static final String ALL_TABLES = "all";
 // AppMonitor
 	//private NetworkUsageAnalyzer analyzer = null;
 	private int connectionID = OMSDefaultValues.NONE_DEF_CNT.getValue();
    private String serviceURL="";

	public WatchTransDBParser(Context ctx, OMSReceiveListener receiveListener,
							  String uniqueId, int appId, String loadingMessage, boolean hideProgressbar,String tableName,String modifiedDate,String serviceURL) {
		Log.d(TAG, "Trans DB Start:::" + System.currentTimeMillis());

		appContext = ctx;
		configDBParserHelper = new OMSDBParserHelper();
		servermapperhelper = new OMSServerMapperHelper();

		rListener = receiveListener;
		uniqueRowId = uniqueId;
		configAppId = appId;
        this.modifiedDate = modifiedDate;
        this.tableName = tableName;
        this.serviceURL = serviceURL;
		sp = appContext.getSharedPreferences("TRANS_DB_COLS", 0);

		progressDialog = new ProgressDialog(appContext);

		// AppMonitor
		Random myRandom = new Random();
		connectionID = myRandom.nextInt(Integer.MAX_VALUE);
	//	analyzer = NetworkUsageAnalyzer.getInstance(appContext);
	/*	if(!TextUtils.isEmpty(loadingMessage)){
			progressDialog.setMessage(loadingMessage);
		}else{
		progressDialog.setMessage(ctx.getResources().getString(R.string.receive_transdb));
		}
		if(!progressDialog.isShowing())
			progressDialog.show();*/
        registerForBroadCast();
	}
	public WatchTransDBParser(Context ctx, OMSReceiveListener receiveListener,
							  String uniqueId, int appId, String loadingMessage,String tableName,String modifiedDate,String serviceURL) {
		Log.d(TAG, "Trans DB Start:::"+System.currentTimeMillis());
		appContext = ctx;
		configDBParserHelper = new OMSDBParserHelper();
		servermapperhelper = new OMSServerMapperHelper();

		rListener = receiveListener;
		uniqueRowId = uniqueId;
		configAppId = appId;
        this.modifiedDate = modifiedDate;
		this.tableName = tableName;
        this.serviceURL = serviceURL;
		sp = appContext.getSharedPreferences("TRANS_DB_COLS", 0);

		progressDialog = new ProgressDialog(appContext);
		if(!TextUtils.isEmpty(loadingMessage)){
			progressDialog.setMessage(loadingMessage);
		}else{
		progressDialog.setMessage(ctx.getResources().getString(R.string.receive_transdb));
		}
		if(!progressDialog.isShowing())
			progressDialog.show();
		
		// AppMonitor
				Random myRandom = new Random();
				connectionID = myRandom.nextInt(Integer.MAX_VALUE);
			//	analyzer = NetworkUsageAnalyzer.getInstance(appContext);
        registerForBroadCast();
	}

    public void callMessageService(String configURL){
        Log.d(TAG,
                " Config DB URL::"+configURL);
        MessageService.getInstance().startMessageService(appContext, "transget");
    }
    private void registerForBroadCast(){
        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(appContext).registerReceiver(messageReceiver, messageFilter);
    }

/*	@Override
	protected String doInBackground(String... args) {
		//HttpClient httpclient = null;
		String result = null;
		String parserResponse = null;
		String serviceUrl = args[0];
		
		if(args.length>=2)
			tableName = args[1];
		else
			Log.i(TAG, "Table Name not provided.");
		
		if(args.length>=3)
			modifiedDate= args[2];
		else
	        modifiedDate=null;
		//Added code for HTTPURL COnnection
		if(!OMSDBManager.checkNetworkConnectivity()){
			parserResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return parserResponse;
		}
		parserResponse = fetchURLConnectionConfigResponse(serviceUrl);
		
		//Added code for HTTPURLCOnnection
*//*
		HttpResponse response = null;
		StatusLine statusLine = null;
		HttpEntity httpEntity = null;
		InputStream inStream = null;
		ActionCenterHelper actionCenterHelper = new ActionCenterHelper(
				appContext);
		String modifiedDate = servermapperhelper.getLastModifiedTime(tableName);
		if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE){
			HashMap<String,String> errorResponseHashmap = servermapperhelper.getWebserviceResponseData(tableName);
			if(errorResponseHashmap!=null) {
				errorObject=errorResponseHashmap.get("errorobject");
				errorMessageKey = errorResponseHashmap.get("messagekey");
				errorCodeKey = errorResponseHashmap.get("codekey");
			}
		}
		
		// modifieddate

		Log.d(TAG, "Trans Service Url :" + serviceUrl);

		if(!OMSDBManager.checkNetworkConnectivity()){
			parserResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return parserResponse;
		}


		try {
			httpclient = AppSecurityAndPerformance.getInstance()
					.getAppGuardSecuredHttpClient(serviceUrl);
		} catch (Exception e) {
			Log.e(TAG,
					"Error Occured doInBackground(getAppGuardSecuredHttpClient):"
							+ e.getMessage());
			e.printStackTrace();
		}
		try {
			HttpGet httpget = new HttpGet(serviceUrl);
			// This changes are for Oasis Project. # Start
			//http://dmz.qa.oasis.pearsontc.com/oasiswebservices/OasisRestService/User.svc/User/ValidateUser/lashburn/oasis
			//		if(serviceUrl.contains("oasiswebservices") && ! serviceUrl.contains("ValidateUser")){
			//			
			//			httpget.setHeader("UserKey",
			//					OMSApplication.getInstance().getUserKey());
			//		}
			// This changes are for Oasis Project. # End

			response = httpclient.execute(httpget);
			statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == OMSConstants.STATUSCODE_OK) {

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					try {
						InputStream instream = entity.getContent();

						result = convertStreamToString(instream);
						Log.d(TAG, "Trans Service Response :" + result);
						parserResponse = transDBParser(result);
						if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE) {
						    //result ="BLGetFailed";
							JSONObject reader = new JSONObject(result);
							if(!TextUtils.isEmpty(errorObject)){
								if(reader.has(errorObject)) {
								JSONObject sys  = reader.getJSONObject(errorObject);
								if(!TextUtils.isEmpty(errorMessageKey)) {
									errorMessageVal = sys.getString(errorMessageKey);
								}
								if(!TextUtils.isEmpty(errorCodeKey)) {
									errorCodeVal = sys.getString(errorCodeKey);
								}
							}else{
								if(!TextUtils.isEmpty(errorMessageKey)) {
									errorMessageVal = reader.getString(errorMessageKey);
								}
								if(!TextUtils.isEmpty(errorCodeKey)) {
									errorCodeVal = reader.getString(errorCodeKey);
								}
							}
								
							}
							}
						
						ContentValues contentValues = new ContentValues();
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
								OMSDatabaseConstants.GET_TYPE_REQUEST);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
								OMSDatabaseConstants.ACTION_STATUS_FINISHED);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
								serviceUrl);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
								tableName);
						actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
								uniqueRowId,configAppId);
						instream.close();
					} catch (IOException e) {
						Log.e(TAG,
								"IOException occurred while parsing the Service response."
										+ e.getMessage());
						e.printStackTrace();
					} catch (ParseException e) {
						Log.e(TAG,
								"ParseException occurred while parsing the Service response."
										+ e.getMessage());
						e.printStackTrace();
					}
				}
				//	parserResponse = OMSMessages.ACTION_CENTER_SUCCESS.getValue();
				//parserResponse = "BLGetSuccess";

			} else { // Failure
				parserResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
				ContentValues contentValues = new ContentValues();
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
						OMSDatabaseConstants.ACTION_STATUS_TRIED);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
						OMSDatabaseConstants.GET_TYPE_REQUEST);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
						serviceUrl);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
						tableName);
				actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
						uniqueRowId,configAppId);
				httpEntity = response.getEntity();
				if (httpEntity != null) {
					inStream = httpEntity.getContent();
					result = convertStreamToString(inStream);
					
					if (result != null) {
						Log.d(TAG, "Trans Service Response :" + result);
					} else {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put(OMSMessages.ERROR.getValue(),
								statusLine.getStatusCode());
						jsonObject.put(OMSMessages.ERROR_DESCRIPTION.getValue(),
								statusLine.toString());
						result = jsonObject.toString();

					}
				}
			}
		} catch (SocketException e) {
			Log.e(TAG,
					"SocketException occurred while excecuting the Trans Service."
							+ e.getMessage());
			e.printStackTrace();
			parserResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
		} catch (IOException e) {
			Log.e(TAG,
					"IOException occurred while while excecuting the Trans Service."
							+ e.getMessage());
			e.printStackTrace();
			parserResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();

		} catch (JSONException e) {
			Log.e(TAG,
					"JSONException occurred while while excecuting the Trans Service."
							+ e.getMessage());
			e.printStackTrace();
			parserResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
		} catch (Exception e) {
			Log.e(TAG,
					"Exception occurred while while excecuting the Trans Service."
							+ e.getMessage());
			e.printStackTrace();
			parserResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
		}
*//*
		return parserResponse;
	}*/

	/*@Override
	protected void onPostExecute(String result) {
		if (rListener != null) {
			rListener.receiveResult(result);
		}
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();

			if(result.equalsIgnoreCase("BLGetFailed")){

				if(!TextUtils.isEmpty(errorMessageVal) && !TextUtils.isEmpty(errorCodeVal)){
					//	Toast.makeText(appContext, errorMessageVal, Toast.LENGTH_LONG).show();
						OMSAlertDialog
						.displayAlertDialog(
								appContext,
								errorCodeVal+" "+errorMessageVal,
								"Ok");
					}else if(!TextUtils.isEmpty(errorMessageVal) && TextUtils.isEmpty(errorCodeVal))
					{
						OMSAlertDialog
						.displayAlertDialog(
								appContext,
								errorMessageVal,
								"Ok");
					} else if(TextUtils.isEmpty(errorMessageVal) && !TextUtils.isEmpty(errorCodeVal)){
						OMSAlertDialog
						.displayAlertDialog(
								appContext,
								errorCodeVal,
								"Ok");
					}
					//Toast.makeText(appContext, errorMessageVal, Toast.LENGTH_LONG).show();
				else{
		    //   Toast.makeText(appContext, "Get BL failed", Toast.LENGTH_LONG).show();
				}

			}
		}
		Log.d(TAG, "Trans DB End:::"+System.currentTimeMillis());
	}*/

	/**
	 * Converts Input Stream into String.
	 * 
	 * @param is
	 * @return
	 */
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = null;
		StringBuilder sb = null;
		String line = null;

		try {
			sb = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line + OMSMessages.NEWLINE_CHAR.getValue());
			}
		} catch (IOException e) {
			Log.e(TAG,
					"IOException occurred while converting stream to string."
							+ e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				is.close();
			} catch (IOException e) {
				Log.e(TAG,
						"IOException occurred while converting stream to string."
								+ e.getMessage());
				e.printStackTrace();
			}
		}
		String rr =  sb.toString();
		rr = rr.replaceAll("\\n", "");
		rr = rr.replaceAll("\\t", "");
		return rr;
	}

	String tableName = null;
	/**
	 * Parses Service response and stores into respective DB table.
	 * 
	 * @param response
	 */
	private String transDBParser(String response) {
		Log.d(TAG, "TableName :" + tableName);
		String primaryKeyVal = null;
		double timeStamp = 0.0;
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		JSONObject tableJSON = null;
		Iterator<String> tableIterator = null;
		String servertableName = null;
		int updateId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		double latestModifiedTimeStamp = 0.0;
		String colName = null;
		String colVal = null;
		int insertedRowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Iterator<String> rootIterator = null;
		String result = "BLGetFailed";
		Set cols= new HashSet<String>();
		int responseKeyCount = 0;
		//boolean isSuccessOneTime = false;

		/*SharedPreferences sp = appContext.getSharedPreferences(
				"TRANS_DB_COLS", 0);*/
		/*
		 * Dummy Json for Testing without Service call. The file should be
		 * exists in Assets folder for Testing.
		 */
		if (OMSConstants.USE_DUMMY_JSON) {
			try {
				response = loadFile(OMSMessages.DUMMY_JSON_FILENAME.getValue());
			} catch (IOException e) {
				Log.e(TAG,
						"Exception occurred in loading dummy Json file from Assets Folder."
								+ e.getMessage());
				e.printStackTrace();
			}
		}

		try {
			jsonObject = new JSONObject(response);
			
			rootAttribute= getRootAttribute(jsonObject);			
			
			if(jsonObject.has("visiteddate")){
				latestModifiedTimeStamp = jsonObject.getDouble("visiteddate");
			}

			//latestModifiedTimeStamp = jsonObject.getDouble("visiteddate");

			responseKeyCount = jsonObject.length();

			rootIterator = jsonObject.keys();
			while (rootIterator.hasNext()) {
				try {
					servertableName = (String) rootIterator.next();
					// This changes are for Oasis Project. # Start
					if(!servertableName.equalsIgnoreCase("visiteddate")/* && !servertableName.equalsIgnoreCase("error")*/){
						// This changes are for Oasis Project. # End - > && !servertableName.equalsIgnoreCase("error")
						
						jsonArray = jsonObject.optJSONArray(servertableName);
						
						/*tableName = servermapperhelper
									.getClientTableName(servertableName);*/
						if (jsonArray != null) {
							//cols = sp.getStringSet(servertableName, cols);

							for (int i = 0; i < jsonArray.length(); i++) {
								//contentvalues = new ContentValues();
								tableJSON = jsonArray.getJSONObject(i);								
								processJsonData(servertableName, tableJSON);								
							}
						}else{
							tableJSON = jsonObject.optJSONObject(servertableName);
							if(tableJSON!=null){
								processJsonData(servertableName, tableJSON);
							}
						}
						if(jsonArray!=null || tableJSON!=null){
							updateId = servermapperhelper
									.updateModifiedTimeStampForTransTable(
											tableName, latestModifiedTimeStamp);

							result = "BLGetSuccess";
							//isSuccessOneTime = true;
							
							if (updateId == OMSDefaultValues.NONE_DEF_CNT.getValue()) {
								Log.e(TAG,
										"Failed to Upadate Modified Date with Max TimeStamp into Table :["
												+ tableName + "]");
							}
						}
					}
				} catch (JSONException e) {

					Log.e(TAG,
							"Exception occurred while parsing the Trans Service response."
									+ e.getMessage());
					//if(!isSuccessOneTime)result ="BLGetFailed";
					result ="BLGetFailed";
					e.printStackTrace();
					continue;
				}
			}

		} catch (JSONException e) {
			Log.e(TAG,
					"Exception occurred while parsing the Trans Service response."
							+ e.getMessage());
			e.printStackTrace();
			result ="BLGetFailed";
		} catch (ParseException e) {
			Log.e(TAG,
					"ParseException occurred while parsing the transDB."
							+ e.getMessage());
			result ="BLGetFailed";
			e.printStackTrace();
		}

		if(responseKeyCount==1 && latestModifiedTimeStamp>0) result = "BLGetSuccess";
		return result;
	}

	private String loadFile(String fileName) throws IOException {
		// Create a InputStream to read the file into
		InputStream iS;
		// get the file as a stream
		iS = appContext.getApplicationContext().getAssets().open(fileName);

		// create a buffer that has the same size as the InputStream
		byte[] buffer = new byte[iS.available()];
		// read the text file as a stream, into the buffer
		iS.read(buffer);
		// create a output stream to write the buffer into
		ByteArrayOutputStream oS = new ByteArrayOutputStream();
		// write this buffer to the output stream
		oS.write(buffer);
		// Close the Input and Output streams
		oS.close();
		iS.close();

		// return the output stream as a String
		return oS.toString();
	}
	
	
	private Iterator tableIterator;
	private void processJsonData(String servertableName, JSONObject tableJSON){

		Iterator tableIterator;		
		ContentValues contentvalues = new ContentValues();
		String colName;
		String primaryKeyVal;
		Double timeStamp;
		
		Set cols= new HashSet<String>();
		cols = sp.getStringSet(servertableName, cols);

		String colVal;

		try{
			
			//adding root attribute to table if it is there in table
			if(rootAttribute!=null){
				for(String rootKey:rootAttribute.keySet()){
					if(cols!=null && cols.contains(rootKey)){						
						contentvalues.put(rootKey, rootAttribute.get(rootKey));
					}else{
						Log.d(TAG, "skipping server column:"+rootKey);
					}
				}
			}
			
			tableIterator = tableJSON.keys();
			while (tableIterator.hasNext()) {
				colName = (String) tableIterator.next();
				if (colName
						.equalsIgnoreCase(OMSDatabaseConstants.UNIQUE_ROW_ID)) {
					primaryKeyVal = tableJSON
							.getString(colName);
					contentvalues.put(colName, primaryKeyVal);
				} else if (colName
						.equalsIgnoreCase(OMSDatabaseConstants.MODIFIED_DATE)) {
					timeStamp = tableJSON.getDouble(colName);
					contentvalues.put(colName, timeStamp);
				} else {
					if(cols!=null && cols.contains(colName)){
						colVal = tableJSON.getString(colName);
						contentvalues.put(colName, colVal.trim());
					}else{
						Log.d(TAG, "skipping server column:"+colName);
					}
				}
	
			}
			
		}catch(JSONException je){
			Log.e(TAG, "Error:"+je.getMessage());
		}
		// This changes are for Oasis Project. # Start
		//Oasis Tables only
		//									if(servertableName.equalsIgnoreCase("BillToDetails")
		//											||servertableName.equalsIgnoreCase("OrdSearchCriteriaList")
		//											||servertableName.equalsIgnoreCase("OrderDetails")
		//											||servertableName.equalsIgnoreCase("OrderSearch")
		//											||servertableName.equalsIgnoreCase("OrderTitles")
		//											||servertableName.equalsIgnoreCase("Orders")
		//											||servertableName.equalsIgnoreCase("DeliveryInfo")
		//											||servertableName.equalsIgnoreCase("ShipmentDetails")
		//											||servertableName.equalsIgnoreCase("AccountShipToDetail"))
		//										contentvalues.put("isdelete", 0);	
		// This changes are for Oasis Project. # End
		
		if (OMSConstants.tableKeyMap.size() > 0 
				&& 	OMSConstants.tableKeyMap.containsKey(servertableName)) {
				String serverPrimaryKeyVal = contentvalues.get(OMSConstants.tableKeyMap.get(servertableName)).toString();
				Log.d(TAG, "serverPrimaryKey:"+servertableName+"."+OMSConstants.tableKeyMap.get(servertableName)+"["+serverPrimaryKeyVal+"]");
			if (serverPrimaryKeyVal != null) {
				contentvalues
						.put(OMSDatabaseConstants.UNIQUE_ROW_ID,serverPrimaryKeyVal);
			}									
		}
		//Log.d(TAG, "contentvalues:"+contentvalues);
		int insertedRowId = configDBParserHelper
				.insertOrUpdateTransDB(servertableName,
						contentvalues);
		Log.d(TAG, "Inserted into " + servertableName
				+ " insert value : " + insertedRowId);
	
	}
	
	private Map<String,String> getRootAttribute(JSONObject inJsonObject){		
		JSONArray jsonArray = null;
		JSONObject jSONObject;
		Iterator rootIterator = inJsonObject.keys();
		String key = null;
		Map<String,String> rootAttrMap = new HashMap<String,String>();		
		
		try {
			while (rootIterator.hasNext()) {			
					key = (String) rootIterator.next();
					jsonArray = inJsonObject.optJSONArray(key);
					if(jsonArray == null){
						jSONObject = inJsonObject.optJSONObject(key);
						if(jSONObject==null){
							rootAttrMap.put(key, inJsonObject.getString(key));
							Log.d(TAG, "key.value:"+key+"."+inJsonObject.getString(key));
						}
					}		
			}
		}catch(Exception ex){
			Log.e(TAG, "Error:"+ex.getMessage());
		}
		return rootAttrMap;
	}
	
	
	private HttpURLConnection getHttpURLConnection(String url){
		//Added code for HttpURLConnection	
		   // AppMonitor
				
				URL obj = null;
				try {
					obj = new URL(url);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				HttpURLConnection conn = null;
				try {
					conn = (HttpURLConnection) obj.openConnection();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		 
				// optional default is GET
				try {
					 int timeoutSocket = OMSConstants.TIMEOUT_SOCKET;
					 conn.setReadTimeout(timeoutSocket);
			         conn.setConnectTimeout(timeoutSocket);
			         /* optional request header */
			         conn.setRequestProperty("Content-Type", "application/json");

		                /* optional request header */
			         conn.setRequestProperty("Accept", "application/json");
					 conn.setRequestMethod("GET");
				} catch (ProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				conn.setDoInput(true);
				
		        return conn;
			//End HttpURL Connection	
	}
	
	
	@SuppressWarnings("unused")
	private String fetchURLConnectionConfigResponse(String serviceURL){
		
        String traceType = OMSApplication.getInstance().getTraceType();
		OMSApplication.getInstance().setTraceType(OMSConstants.TRACE_TYPE_SERVER);
		// AppMonitor
		
		/*analyzer.startNetworkConnection(serviceURL, OMSMessages.CONNECTION_PREFIX.getValue()
				+ connectionID);*/
		ActionCenterHelper actionCenterHelper = new ActionCenterHelper(
				appContext);
		String configResponse = null;
		String response="";
		InputStream inputStream = null;
		HttpURLConnection urlConnection = getHttpURLConnection(serviceURL);
		  try {
			  urlConnection.connect();
			}  catch (SocketException e) {
				Log.e(TAG,
						"SocketException occurred while excecuting the Trans Service."
								+ e.getMessage());
				e.printStackTrace();
				configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
			} catch (IOException e) {
				Log.e(TAG,
						"IOException occurred while while excecuting the Trans Service."
								+ e.getMessage());
				e.printStackTrace();
				configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();

			} catch (Exception e) {
				Log.e(TAG,
						"Exception occurred while while excecuting the Trans Service."
								+ e.getMessage());
				e.printStackTrace();
				configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
			}
		if(urlConnection!=null){
			// AppMonitor
			//analyzer.updateConnectionStatus(connectionID, true);
				try {
					int statusCode = urlConnection.getResponseCode();
					if (statusCode ==  OMSConstants.STATUSCODE_OK) {
						inputStream = new BufferedInputStream(urlConnection.getInputStream());
						 response = convertStreamToString(inputStream);
						 Log.d(TAG, "GETBL Response for HTTPURLConnection:::"+response);
						// Create a Reader from String
						 // configResponse = transDBParser(response);
						    
						    Reader stringReader = new StringReader(response);
							readJsonStream(stringReader);
							
							
							ContentValues contentValues = new ContentValues();
							contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
									OMSDatabaseConstants.GET_TYPE_REQUEST);
							contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
									OMSDatabaseConstants.ACTION_STATUS_FINISHED);
							contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
									serviceURL);
							contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
									tableName);
							actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
									uniqueRowId,configAppId);
							// AppMonitor
							/*analyzer.receivedConnectionResponse(connectionID,
									urlConnection.getContentLength(),
									OMSDatabaseConstants.GET_TYPE_REQUEST);*/
							
						/*	ServerDBUpdateHelper dbhelper = new ServerDBUpdateHelper(appContext);
							dbhelper.insertCallTraceTypeData(ConsoleDBConstants.CALL_TRACE_TYPE_TABLE, ""+OMSApplication.getInstance().getAppId());*/
/*							Log.i(TAG, "Server URL::"+serviceURL); 
							Log.i(TAG, "Server Response time::"+OMSApplication.getInstance().getServerProcessDuration());
*/							Log.i(TAG, "ServerTime::"+OMSApplication.getInstance().getServerProcessDuration()+"\t"+"DBTime::"+OMSApplication.getInstance().getDatabaseProcessDuration()+"\t"+serviceURL );
							OMSApplication.getInstance().setTraceType(traceType);
							configResponse =  OMSMessages.BL_SUCCESS.getValue();
					}else{
						Log.e(TAG, "status code["+urlConnection.getResponseCode()+"] Reason["+urlConnection.getResponseMessage()+"]");
						
						configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
						ContentValues contentValues = new ContentValues();
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
								OMSDatabaseConstants.ACTION_STATUS_TRIED);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
								OMSDatabaseConstants.GET_TYPE_REQUEST);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
								serviceURL);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
								tableName);
						actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
								uniqueRowId,configAppId);
						inputStream = new BufferedInputStream(urlConnection.getInputStream());
						// AppMonitor
						/*analyzer.receivedConnectionResponse(connectionID,
								urlConnection.getContentLength(),
								OMSDatabaseConstants.GET_TYPE_REQUEST);*/
						
					    response = convertStreamToString(inputStream);
	                     if (response != null) {
							return configResponse;
							
						} else {
							try {
								JSONObject jsonObject = new JSONObject();
								jsonObject.put(OMSMessages.ERROR.getValue(),
										urlConnection.getResponseCode());
								jsonObject.put(
										OMSMessages.ERROR_DESCRIPTION.getValue(),
										urlConnection.getResponseMessage());
								response = jsonObject.toString();
							} catch (JSONException e) {
								Log.e(TAG,
										"JSONException occurred when is ConfigDBParse Failed."
												+ e.getMessage());
								e.printStackTrace();
							}
						}
					}
				}  catch (SocketException e) {
					Log.e(TAG,
							"SocketException occurred while excecuting the Trans Service."
									+ e.getMessage());
					e.printStackTrace();
					configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
				} catch (IOException e) {
					Log.e(TAG,
							"IOException occurred while while excecuting the Trans Service."
									+ e.getMessage());
					e.printStackTrace();
					configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();

				}  catch (Exception e) {
					Log.e(TAG,
							"Exception occurred while while excecuting the Trans Service."
									+ e.getMessage());
					e.printStackTrace();
					configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
				}
		}else{
			configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
		}
		return configResponse;
	}
	
	
	
	
	
	
	//Added bulk Trans Code Thread  implementation
	
	/**
	 * Parses Service response and stores into respective DB table.
	 * 
	 * @param pStringReader
	 */
	private void readJsonStream(Reader pStringReader) {

		JsonReader reader = null;
		List<ContentValues> rows = null;
		String tableName = null;
		String colName = null;
		ExecutorService executor = Executors.newFixedThreadPool(10);
		double latestModifiedTimeStamp = 0.0f;
		final String VISITED_DATE = "visiteddate";
		final String MESSAGE = "message";
		final String ADDITION_MESSAGE = "additionMessage";
		final String VISITED_DATE_MAPPER = "visiteddatemapper";
		List<String> tableNames = new ArrayList<String>();
        final String DB_PROCESS_DURATION="dbprocessduration";
        final String SERVER_PROCESS_DURATION="serverprocessduration";
		try {
			Log.d(TAG, "@@@@@@@@@@ Trans DB Tables Start @@@@@@@@@@");
			reader = new JsonReader(pStringReader);
			reader.setLenient(true);
			reader.beginObject();

			// Iterate through each table data
			while (reader.hasNext()) {

				colName = reader.nextName();
				if (colName.equals(VISITED_DATE)) {

					latestModifiedTimeStamp = reader.nextDouble();
					// Update Trans Table
					/*servermapperhelper.updateModifiedTimeStampForTransTable(
							ALL_TABLES, latestModifiedTimeStamp);*/
					if (Integer.parseInt(OMSApplication
							.getInstance().getAppId()) == 10) {
						servermapperhelper
								.updateModifiedTimeStampForVisitedDateMapper(
										OMSApplication
												.getInstance()
												.getEditTextHiddenVal(),
										latestModifiedTimeStamp);
					}
					continue;
				} else if (colName.equals(MESSAGE)) {
					Log.e(TAG, "Trans DB gave error response - message - "
							+ reader.nextString());
					continue;
				} else if (colName.equals(ADDITION_MESSAGE)) {
					Log.e(TAG,
							"Trans DB gave error response - additionMessage - "
									+ reader.nextString());
					continue;
				}else if (VISITED_DATE_MAPPER.equalsIgnoreCase(colName)){
					Log.d(TAG,
							"Skipping internal Table "+VISITED_DATE_MAPPER+" lookup");
					reader.skipValue();
					continue;
				}
//Fetch dbprocess duration serverprocess duration
				else if(DB_PROCESS_DURATION.equalsIgnoreCase(colName)){
					String dbDuration = reader.nextString();
					OMSApplication.getInstance().setDatabaseProcessDuration(dbDuration);
					/*Log.i(TAG,
							"DB Process Duration"
									+ dbDuration);*/
					continue;
				}else if(SERVER_PROCESS_DURATION.equalsIgnoreCase(colName)){
					String serverProcessDuration = reader.nextString();
					OMSApplication.getInstance().setServerProcessDuration(serverProcessDuration);
					/*Log.i(TAG,
							"server process duration "
									+ serverProcessDuration);*/
					continue;
				}
				// Get Table Name
				tableName = servermapperhelper.getClientTableName(colName);

				if (tableName == null) {
					Log.e(TAG,
							"Table Name was not found in ServerMapperHelper - "
									+ colName);
					// Tables created only on the server sometimes dont find
					// entry in ServerMapper. So, allowing those tables here
					tableNames.add(colName);
				} else {
					tableNames.add(tableName);
				}

				rows = readAllRowDataForTable(reader, tableName);

				// Update DB only if we have valid Table name
				if (tableName != null) {
					Runnable worker = new DbWorkerThread(colName, rows);
					executor.execute(worker);
				}
			}
			reader.endObject();

			Log.d(TAG, "Waiting for DB Worker Threads to Complete");
			// Request for Shutdown. This will wait till the db updates are
			// complete. Wait till the db update is complete and then invoke the
			// time stamp update to avoid db locks.
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

			Log.d(TAG, "DB Worker Threads Completed");
			// Update Modified Time Stamp for All Trans Tables
			executor = Executors.newFixedThreadPool(1);
			Runnable worker = new DbWorkerThreadToUpdateTimeStamp(tableNames,
					latestModifiedTimeStamp);
			executor.execute(worker);

			// Request for Shutdown. This will wait till the db updates are
			// complete
			Log.d(TAG, "Waiting for DB Timestamp Update Worker Thread to Complete");
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

			Log.d(TAG, "DB Timestamp Update Worker Thread Completed");
			Log.d(TAG, "@@@@@@@@@@ Trans DB Tables End @@@@@@@@@@");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
			while (!executor.isTerminated()) {
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private List<ContentValues> readAllRowDataForTable(JsonReader reader,
			String pTableName) throws IOException {
		List<ContentValues> messages = new ArrayList<ContentValues>();
		ContentValues contentValues = null;
		List<String> transColsSet = new ArrayList<String>();
		int skippedRowsCount = 0;

		if (pTableName != null) {
			transColsSet = servermapperhelper.getTransColumnSet(pTableName);
		}

		reader.beginArray();
		while (reader.hasNext()) {
			contentValues = readSingleRowData(reader, transColsSet, pTableName);

			if (pTableName != null && validateRowData(contentValues)) {
				messages.add(contentValues);
			} else {
				skippedRowsCount++;
			}
		}
		reader.endArray();

		if (skippedRowsCount > 0 && pTableName != null) {
			Log.d(TAG, "Skipped #" + skippedRowsCount + " records for table - "
					+ pTableName);
		}
		return messages;
	}

	private boolean validateRowData(ContentValues pContentValues) {

		String usidValue = pContentValues
				.getAsString(OMSDatabaseConstants.UNIQUE_ROW_ID);
		if (usidValue != null && usidValue.equals(OMSConstants.NULL_STRING)) {
			return false;
		}

		/*
		 * String isDeleteValue = pContentValues
		 * .getAsString(DatabaseConstants.IS_DELETE); if (isDeleteValue != null
		 * && isDeleteValue.equals(Constants.IS_DELETE_ONE)) { return false; }
		 */
		return true;
	}

	private ContentValues readSingleRowData(JsonReader reader,
			List<String> transColsSet, String tableName) {
		ContentValues contentValues = new ContentValues();
		String colName = null;
		String colValue = null;
		try {
			reader.beginObject();

			while (reader.hasNext()) {
				colName = null;
				colValue = null;
				colName = reader.nextName();
				colValue = reader.nextString();

				// If Table Name is null, return empty ContentValues
				if (tableName != null) {
					if ((transColsSet != null && !transColsSet.isEmpty())
							&& transColsSet.contains(colName)) {
						if (TextUtils.isEmpty(colValue)
								|| colValue.equals(OMSConstants.NULL_STRING)) {
							colValue = OMSConstants.EMPTY_STRING;
						}
						contentValues.put(colName, colValue);
					} else {
						// Log.d(TAG, "Ignored column :" + colName
						// + " from Table - " + tableName);
					}
				}
			}
			reader.endObject();
		} catch (IOException e) {
			Log.e(TAG, "IOException:: ColName - "
					+ (colName == null ? OMSConstants.EMPTY_STRING : colName));
			e.printStackTrace();
		}
		return contentValues;
	}

	/**
	 * 
	 * Worker thread to insert rows into each Trans Table
	 * 
	 */
	private class DbWorkerThread implements Runnable {

		private String tableName;
		private List<ContentValues> contentValuesList;

		public DbWorkerThread(String pTableName,
				List<ContentValues> pContentValuesList) {
			this.tableName = pTableName;
			this.contentValuesList = new ArrayList<ContentValues>(
					pContentValuesList.size());
			contentValuesList.addAll(pContentValuesList);
			pContentValuesList.clear();
			pContentValuesList = null;

		}

		@Override
		public void run() {
			if (tableName != null && !tableName.equals(OMSConstants.NULL_STRING)) {
				// Log.d(TAG, "Inserting to " + tableName + " - Records#"
				// + contentValuesList.size());
				insertOrUpdateDB();
				Log.d(TAG, tableName + " updated successfully ");
			}
		}

		private void insertOrUpdateDB() {

			// In case of custom tables, clear default values
			if (servermapperhelper.getClientTableName(tableName) == null) {
				Log.d(TAG,"Deleting default rows for table - " + tableName);
				configDBParserHelper.deleteAllDefaultRows(tableName);
			}
			/*for (ContentValues contentValues : contentValuesList) {
				configDBParserHelper.insertOrUpdateTransDB(tableName,
						contentValues);
			}*/
			if(modifiedDate!=null){
			configDBParserHelper.insertOrUpdateTransDBDBBulk(tableName,
					contentValuesList,modifiedDate);
			}else{
				configDBParserHelper.insertOrUpdateTransDBDBBulk(tableName,
						contentValuesList);
			}
			this.contentValuesList.clear();
			this.contentValuesList = null;

		}

		@Override
		public String toString() {
			return tableName;
		}

	}

	/**
	 * 
	 * Worker Thread to update last modified timestamp in all Trans tables
	 * 
	 */
	private class DbWorkerThreadToUpdateTimeStamp implements Runnable {

		private List<String> tableNames = null;
		private double latestModifiedTimeStamp = 0.0f;
		int updateStatus = 0;

		public DbWorkerThreadToUpdateTimeStamp(List<String> pTableNames,
				double pLatestModifiedTimeStamp) {
			this.tableNames = pTableNames;
			this.latestModifiedTimeStamp = pLatestModifiedTimeStamp;
		}

		@Override
		public void run() {
			// Log.d(TAG,
			// "Start - Updating Modified Time Stamp for All Trans tables");
			for (String tableName : tableNames) {
				updateStatus = servermapperhelper
						.updateModifiedTimeStampForTransTable(tableName,
								latestModifiedTimeStamp);

				if (updateStatus == 0) {
					Log.e(TAG,
							"Failed to Upadate Modified Date with Max TimeStamp into Table :["
									+ tableName + "]");
				}
			}
			// Log.d(TAG,
			// "End - Updating Modified Time Stamp for All Trans tables - " +
			// tableNames.size());
		}

	}
	
	

	
	//End of Bulk

    public class MessageReceiver extends BroadcastReceiver {
        String configResponse= "";
        @Override
        public void onReceive(Context context, Intent intent) {
            //  String message = intent.getStringExtra("message");
            //Log.v("myTag", "Main activity received message: " + message);

            try {
                //
                if (intent.hasExtra("result")) {
                    String result_data = intent.getStringExtra("result");
                    if (result_data.equalsIgnoreCase("transget")) {
                        //Log.d("TAG", "revertpatient Response::::" + MobileApplication.getInstance().getPatientRevertResponse());
                        //new CustomToast(getActivity(),getActivity()).displayToast("Received ConfigDB");

                        String response = OMSApplication.getInstance().getTransDataAPIResponse();
                        processResponse(response);



                        if (!TextUtils.isEmpty(response)) {
// Create a Reader from String
                            Reader stringReader = new StringReader(response);
                            readJsonStream(stringReader);
                            // AppMonitor
						/*	analyzer.receivedConnectionResponse(connectionID,
									urlConnection.getContentLength(),
									OMSDatabaseConstants.GET_TYPE_REQUEST);*/

                            configResponse = OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
                                    .getValue();
                        } else {
                            configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
                        }
                        receiveResult(configResponse);

                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

	@Override
	public void receiveResult(String result) {
		rListener.receiveResult(result);
	}


    private void  processResponse(String responseJSON){
        ActionCenterHelper actionCenterHelper = new ActionCenterHelper(
                appContext);
        String configResponse = null;
        try {
            JSONObject responseJSONObject = new JSONObject(responseJSON);
        /*    responseJSON.put("statuscode",OMSConstants.STATUSCODE_OK);
            responseJSON.put("serviceResponse",response);*/
            int responseStatusCode = responseJSONObject.getInt("statuscode");
            String response = responseJSONObject.getString("serviceResponse");
            if (responseStatusCode ==  OMSConstants.STATUSCODE_OK) {

                Log.d(TAG, "GETBL Response for HTTPURLConnection:::"+response);
                // Create a Reader from String
                // configResponse = transDBParser(response);

                Reader stringReader = new StringReader(response);
                readJsonStream(stringReader);


                ContentValues contentValues = new ContentValues();
                contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
                        OMSDatabaseConstants.GET_TYPE_REQUEST);
                contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
                        OMSDatabaseConstants.ACTION_STATUS_FINISHED);
                contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
                        serviceURL);
                contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
                        tableName);
                actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
                        uniqueRowId,configAppId);
                // AppMonitor
							/*analyzer.receivedConnectionResponse(connectionID,
									urlConnection.getContentLength(),
									OMSDatabaseConstants.GET_TYPE_REQUEST);*/

						/*	ServerDBUpdateHelper dbhelper = new ServerDBUpdateHelper(appContext);
							dbhelper.insertCallTraceTypeData(ConsoleDBConstants.CALL_TRACE_TYPE_TABLE, ""+OMSApplication.getInstance().getAppId());*/
/*							Log.i(TAG, "Server URL::"+serviceURL);
							Log.i(TAG, "Server Response time::"+OMSApplication.getInstance().getServerProcessDuration());
*/			//				Log.i(TAG, "ServerTime::"+OMSApplication.getInstance().getServerProcessDuration()+"\t"+"DBTime::"+OMSApplication.getInstance().getDatabaseProcessDuration()+"\t"+serviceURL );
           //     OMSApplication.getInstance().setTraceType(traceType);
                configResponse =  OMSMessages.BL_SUCCESS.getValue();
                receiveResult(configResponse);
            }else{
              //  Log.e(TAG, "status code[" + urlConnection.getResponseCode() + "] Reason[" + urlConnection.getResponseMessage() + "]");

                configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
                ContentValues contentValues = new ContentValues();
                contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
                        OMSDatabaseConstants.ACTION_STATUS_TRIED);
                contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
                        OMSDatabaseConstants.GET_TYPE_REQUEST);
                contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
                        serviceURL);
                contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
                        tableName);
                actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
                        uniqueRowId,configAppId);

                if (response.equalsIgnoreCase("transgetfailure")) {
                    configResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
                    receiveResult(configResponse);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject.put(OMSMessages.ERROR.getValue(),
                                jsonObject.get(OMSMessages.ERROR.getValue()));
                        jsonObject.put(
                                OMSMessages.ERROR_DESCRIPTION.getValue(),
                                jsonObject.get(OMSMessages.ERROR_DESCRIPTION.getValue()));
                        response = jsonObject.toString();
                        receiveResult(configResponse);
                    } catch (JSONException e) {
                        Log.e(TAG,
                                "JSONException occurred when is ConfigDBParse Failed."
                                        + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
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
package watch.oms.omswatch.Parser;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

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
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.utils.OMSUtils;


/**
 * 
 * @author 280779
 * 
 *         Config Parser: 1) Invokes Config Service and retrieves the response
 *         in Asynhnoronous Task. 2) Parses the Service JSON response and
 *         inserts/updates the data into Config DB tables.
 * 
 */
public class OMSConfigDBParser extends AsyncTask<String, Void, String> {
	private final String TAG = this.getClass().getSimpleName();
	private Context appContext = null;
//	private OMSDBParserHelper configDBParserHelper = null;
	private OMSReceiveListener rListener = null;
	private ProgressDialog pDialog = null;
	//private NetworkUsageAnalyzer analyzer = null;
	private int connectionID = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private static final String CONNECTION_PREFIX = "CON";
	private String errToast;
	
	public OMSConfigDBParser(Context ctx, OMSReceiveListener receiveListener) {
		Log.d(TAG, "Config DB Start:::" + System.currentTimeMillis());
		appContext = ctx;
	//	configDBParserHelper = new OMSDBParserHelper();
		Random myRandom = new Random();
		connectionID = myRandom.nextInt(Integer.MAX_VALUE);
		//analyzer = NetworkUsageAnalyzer.getInstance(appContext);
		rListener = receiveListener;

		if (!OMSConstants.USE_SPLASH) {
			pDialog = new ProgressDialog(appContext);
			pDialog.setMessage("Receiving ConfigDB"/*ctx.getResources().getString(
					R.string.receive_configdb)*/);
			pDialog.setCancelable(false);
			pDialog.show();
		}


	}



	@Override
	protected String doInBackground(String... args) {
		String result = null;
		String configResponse = null;
		String serviceUrl = args[0];
		/*HttpClient httpclient = null;
		HttpResponse response = null;
		HttpEntity httpEntity = null;*/
		InputStream inStream = null;
    
		//Added for HTTPURLConnectivity	
		if(!OMSUtils.getInstance().checkNetworkConnectivity(appContext)){
			configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return configResponse;
		}
		
		Log.d(TAG,
				" Config DB URL::"+serviceUrl);
		/*configResponse = OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
				.getValue();*/
		
		/*if(Integer.parseInt(OMSConstants.APP_ID) == -200 ){
			configResponse = enableOnceInADayWebServiceCall(serviceUrl);
		}
		else */{
		configResponse = 	fetchURLConnectionConfigResponse(serviceUrl);
		}
	/*	httpclient = AppSecurityAndPerformance.getInstance()
				.getAppGuardSecuredHttpClient(serviceUrl);*/
		
		//End
		
/*		HttpParams httpParameters = new BasicHttpParams();
		// AppMonitor
		analyzer.startNetworkConnection(serviceUrl, CONNECTION_PREFIX
				+ connectionID);
		Log.d(TAG, "ConfigDB serviceUrl :" + serviceUrl);
		
		if(!OMSDBManager.checkNetworkConnectivity()){
			configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return configResponse;
		}

		
		int timeoutSocket = OMSConstants.TIMEOUT_SOCKET;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		httpclient = AppSecurityAndPerformance.getInstance()
				.getAppGuardSecuredHttpClient(serviceUrl);

		HttpGet httpget = new HttpGet(serviceUrl);
		// AppMonitor
		analyzer.sentConnectionRequest(connectionID, serviceUrl.length());

		try {
			response = httpclient.execute(httpget);
			// AppMonitor
			analyzer.updateConnectionStatus(connectionID, true);

			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == OMSConstants.STATUSCODE_OK) {
				httpEntity = response.getEntity();
				if (httpEntity != null) {
					try {
						String jsonContent = EntityUtils
								.toString(httpEntity,"UTF-8");
						Log.i(TAG, "Config Service Response: " + jsonContent);
						jsonContent.replace("\\n", "");
						jsonContent.replace("\\t", "");
						// Create a Reader from String
						Reader stringReader = new StringReader(jsonContent);
						readJsonStream(stringReader);;
						// AppMonitor
						analyzer.receivedConnectionResponse(connectionID,
								httpEntity.getContentLength(),
								OMSDatabaseConstants.GET_TYPE_REQUEST);
					} catch (IOException e) {
						Log.e(TAG,
								"Exception occurred while parsing the Service response."
										+ e.getMessage());
						e.printStackTrace();
					}
				}
				configResponse = OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
						.getValue();

			} else {
				
				Log.e(TAG, "status code["+statusLine.getStatusCode()+"] Reason["+statusLine.getReasonPhrase()+"]");
				
				configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
				
				// Failed
				httpEntity = response.getEntity();
				if (httpEntity != null) {
					inStream = httpEntity.getContent();
					// AppMonitor
					analyzer.receivedConnectionResponse(connectionID,
							httpEntity.getContentLength(),
							OMSDatabaseConstants.GET_TYPE_REQUEST);
					result = convertStreamToString(inStream);

					if (result != null) {
						
						errToast = statusLine.getReasonPhrase();
						
						return configResponse;
						
					} else {
						try {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put(OMSMessages.ERROR.getValue(),
									statusLine.getStatusCode());
							jsonObject.put(
									OMSMessages.ERROR_DESCRIPTION.getValue(),
									statusLine.toString());
							result = jsonObject.toString();
						} catch (JSONException e) {
							Log.e(TAG,
									"JSONException occurred when is ConfigDBParse Failed."
											+ e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
		}
		catch(SocketTimeoutException e){
			Log.e(TAG,
					"SocketTimeoutException occurred while excecuting the Config Service."
							+ e.getMessage());
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
			}
			e.printStackTrace();
		}
		catch (SocketException e) {
			Log.e(TAG,
					"SocketException occurred while excecuting the Config Service."
							+ e.getMessage());
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
			}
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG,
					"IOException occurred while while excecuting the Config Service."
							+ e.getMessage());
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
			}
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG,
					"Exception occurred while excecuting the Config Service."
							+ e.getMessage());
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
			}
			e.printStackTrace();
		}
*/
		return configResponse;
	}

	@Override
	protected void onPostExecute(String result) {
		Log.d(TAG, "Config DB End:::" + System.currentTimeMillis());
		if(errToast!=null) Toast.makeText(appContext, errToast, Toast.LENGTH_SHORT).show();
		
		if (rListener != null) {
			rListener.receiveResult(result);
		}

		if (!OMSConstants.USE_SPLASH) {
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

	/**
	 * Converts Input Stream to String.
	 * 
	 * @param is
	 * @return String
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + OMSMessages.NEWLINE_CHAR.getValue());
			}
		} catch (IOException e) {
			Log.e("Config Parser",
					"IOException occurred while Converting Stream to String."
							+ e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e("Config Parser",
						"IOException occurred while Converting Stream to String."
								+ e.getMessage());
				e.printStackTrace();
			}
		}
		String rr =  sb.toString();
		rr.replace("\\n", "");
		rr.replace("\\t", "");
		return rr;
	}

	/**
	 * Parses Service response and stores into respective DB table.
	 *
	 * @param
	 */
	/*private void readJsonStream(Reader pStringReader) {
		double latestModifiedTimeStamp = 0.0f;
		JsonReader reader = null;
		List<ContentValues> rows = null;
		String tableName = null;
		ExecutorService executor = Executors.newFixedThreadPool(10);
		final String VISITED_DATE = "visiteddate";
		OMSServerMapperHelper servermapperhelper=new OMSServerMapperHelper();
	   final String DB_PROCESS_DURATION="dbprocessduration";
	   final String SERVER_PROCESS_DURATION="serverprocessduration";

		try {
			Log.d(TAG, "@@@@@@@@@@ Config DB Tables Start @@@@@@@@@@");
			reader = new JsonReader(pStringReader);
			reader.setLenient(true);
			reader.beginObject();
			// Iterate through each table data
			while (reader.hasNext()) {
				tableName = reader.nextName();
				if (tableName.equals(VISITED_DATE)) {

					latestModifiedTimeStamp = reader.nextDouble();
					
					*//*servermapperhelper.updateModifiedTimeStampForAppsTable(
							 latestModifiedTimeStamp);*//*
					*//*if (Integer.parseInt(OMSApplication
							.getInstance().getAppId()) == 10) {
						servermapperhelper
								.updateModifiedTimeStampForVisitedDateMapper(
										OMSApplication
												.getInstance()
												.getEditTextHiddenVal(),
										latestModifiedTimeStamp);
					}*//*
					continue;
				}
				if (tableName.equals(OMSConstants.NULL_STRING)) {
					continue;
				}
				//Fetch dbprocess duration serverprocess duration
				else if(DB_PROCESS_DURATION.equalsIgnoreCase(tableName)){
					String dbDuration = reader.nextString();
					OMSApplication.getInstance().setDatabaseProcessDuration(dbDuration);
					Log.i(TAG,
							"DB Process Duration"
									+ dbDuration);
					continue;
				} if(SERVER_PROCESS_DURATION.equalsIgnoreCase(tableName)){
					String serverProcessDuration = reader.nextString();
					OMSApplication.getInstance().setServerProcessDuration(serverProcessDuration);
					Log.i(TAG,
							"server process duration "
									+ serverProcessDuration);
					continue;
				}
				rows = readAllRowDataForTable(reader, tableName);

				Runnable worker = new DbWorkerThread(tableName, rows);
				executor.execute(worker);
			}
			reader.endObject();
			executor.shutdown();
			while (!executor.isTerminated()) {
			}
			Log.d(TAG, "@@@@@@@@@@ Config DB Tables End @@@@@@@@@@");
			// Update Apps Table
			Log.d(TAG, "@@@@@@@@@@ Updating AppsTable with ConfigLastModifieddate:"+latestModifiedTimeStamp);
			servermapperhelper.updateModifiedTimeStampForAppsTable(
					 latestModifiedTimeStamp);

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
				Log.e(TAG,
						"IOException occurred while loading file from Assets folder."
								+ e.getMessage());
				e.printStackTrace();
			}

		}

	}*/

	private List<ContentValues> readAllRowDataForTable(JsonReader reader,
			String pTableName) throws IOException {
		List<ContentValues> messages = new ArrayList<ContentValues>();
		ContentValues contentValues = null;
		int skippedRowsCount = 0;
		reader.beginArray();
		while (reader.hasNext()) {
			contentValues = readSingleRowData(reader);
			if (validateRowData(contentValues)) {
				messages.add(contentValues);
			} else {
				skippedRowsCount++;
			}
		}

		if (skippedRowsCount > 0) {
			Log.d(TAG, "Skipped #" + skippedRowsCount + " records for table - "
					+ pTableName);
		}
		reader.endArray();
		return messages;
	}

	private boolean validateRowData(ContentValues pContentValues) {
		boolean result = true;

		String usidValue = pContentValues
				.getAsString(OMSDatabaseConstants.UNIQUE_ROW_ID);
		if (usidValue != null && usidValue.equals(OMSConstants.NULL_STRING)) {
			result = false;
			return result;
		}
		//Commented the below code to get update in local db if the record is soft deleted in server.
		/*String isDeleteValue = pContentValues
				.getAsString(OMSDatabaseConstants.IS_DELETE);
		if (isDeleteValue != null
				&& isDeleteValue.equals(OMSConstants.IS_DELETE_ONE)) {
			result = false;
		}*/
		return result;
	}

	private ContentValues readSingleRowData(JsonReader reader) {
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
				if (colValue.equals(OMSConstants.NULL_STRING)) {
					colValue = OMSConstants.EMPTY_STRING;
				}
				
			   if(!colName.equalsIgnoreCase("isdirty")){
				  contentValues.put(colName, colValue);
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

	private class DbWorkerThread implements Runnable {

		private String tableName;
		private List<ContentValues> contentValuesSet;

		public DbWorkerThread(String pTableName,
				List<ContentValues> pContentValues) {
			this.tableName = pTableName;
			this.contentValuesSet = new ArrayList<ContentValues>(
					pContentValues.size());
			this.contentValuesSet.addAll(pContentValues);
			pContentValues.clear();
			pContentValues = null;
		}

		@Override
		public void run() {
			Log.d(TAG, "Inserting to " + tableName + " - Records#"
					+ contentValuesSet.size());
			insertOrUpdateDB();
			Log.d(TAG, tableName + " updated successfully ");
		}

		private void insertOrUpdateDB() {

			/*List<ContentValues> contentValuesList = new ArrayList<ContentValues>(
					contentValuesSet.size());
			for (ContentValues contentValue : contentValuesSet) {
				contentValuesList.add(contentValue);
			}

			configDBParserHelper.insertOrUpdateConfigDBBulk(tableName,
					contentValuesList);*/
			
			/*configDBParserHelper.insertOrUpdateConfigDBBulk(tableName,
					contentValuesSet);*/
			this.contentValuesSet.clear();
			this.contentValuesSet = null;
		/*	contentValuesList.clear();
			contentValuesList = null;*/
		}

		@Override
		public String toString() {
			return tableName;
		}

	}

	/**
	 * Loads 'jsonconfig.json' file from Assets folder and returns as String.
	 * 
	 * @param fileName
	 * @return Json String
	 * @throws IOException
	 */
	public static String LoadFile(String fileName, Context context)
			throws IOException {
		// Create a InputStream to read the file into
		InputStream inputstream;
		// get the file as a stream
		inputstream = context.getAssets().open(fileName);

		// create a buffer that has the same size as the InputStream
		byte[] buffer = new byte[inputstream.available()];
		// read the text file as a stream, into the buffer
		inputstream.read(buffer);
		// create a output stream to write the buffer into
		ByteArrayOutputStream oS = new ByteArrayOutputStream();
		// write this buffer to the output stream
		oS.write(buffer);
		// Close the Input and Output streams
		oS.close();
		inputstream.close();

		// return the output stream as a String
		return oS.toString();
	}
	
	
	private HttpURLConnection getHttpURLConnection(String url){
		//Added code for HttpURLConnection	
		   // AppMonitor
				/*analyzer.startNetworkConnection(url, CONNECTION_PREFIX
						+ connectionID);*/
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
				// AppMonitor
				//analyzer.sentConnectionRequest(connectionID, url.length());
		        return conn;
			//End HttpURL Connection	
	}
	
	
	@SuppressWarnings("unused")
	private String fetchURLConnectionConfigResponse(String serviceURL){
		Log.d(TAG, "ConfigDBURL::"+serviceURL);
		String configResponse = null;
		String response="";
		InputStream inputStream = null;
		HttpURLConnection urlConnection = null;
		/*HttpURLConnection urlConnection = (HttpURLConnection) AppSecurityAndPerformance.getInstance()
				.getAppGuardSecuredHttpsUrlConnection(serviceURL);*/
	//	URL url = new URL(serviceURL);
	//	HttpURLConnection urlConnection = 	(HttpURLConnection) url.openConnection();
	//	HttpURLConnection urlConnection = getHttpURLConnection(serviceURL);
	//	urlConnection.setConnectTimeout(50000);
		// AppMonitor
	/*			analyzer.startNetworkConnection(serviceURL, OMSMessages.CONNECTION_PREFIX.getValue()
						+ connectionID);
*/
		try {
			  URL url = new URL(serviceURL);
			   urlConnection = 	(HttpURLConnection) url.openConnection();
			  //	HttpURLConnection urlConnection = getHttpURLConnection(serviceURL);
			  urlConnection.setConnectTimeout(50000);
			  urlConnection.connect();
			} catch (IOException e1) {

				Log.e(TAG,
						"IOException occurred while while excecuting the Config Service."
								+ e1.getMessage());
				if (rListener != null) {
					rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
				}
				e1.printStackTrace();
			}
		if(urlConnection!=null){
			// AppMonitor
				//		analyzer.updateConnectionStatus(connectionID, true);

				try {
					int statusCode = urlConnection.getResponseCode();
					if (statusCode ==  OMSConstants.STATUSCODE_OK) {
						inputStream = new BufferedInputStream(urlConnection.getInputStream());
						 response = convertStreamToString(inputStream);
						 Log.d(TAG, "ConfigResponse for HTTPURLConnection:::"+response);
                        OMSApplication.getInstance().setConfigDataAPIResponse(response);
						/*// Create a Reader from String
							Reader stringReader = new StringReader(response);
							readJsonStream(stringReader);*/
							// AppMonitor
						/*	analyzer.receivedConnectionResponse(connectionID,
									urlConnection.getContentLength(),
									OMSDatabaseConstants.GET_TYPE_REQUEST);*/
							
							configResponse = OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
									.getValue();
					}else{
						Log.e(TAG, "status code["+urlConnection.getResponseCode()+"] Reason["+urlConnection.getResponseMessage()+"]");
						
						configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
						inputStream = new BufferedInputStream(urlConnection.getInputStream());
						// AppMonitor
					/*	analyzer.receivedConnectionResponse(connectionID,
								urlConnection.getContentLength(),
								OMSDatabaseConstants.GET_TYPE_REQUEST);*/
						
					    response = convertStreamToString(inputStream);
	                     if (response != null) {
							
							errToast = urlConnection.getResponseMessage();
							OMSApplication.getInstance().setConfigDataAPIResponse("Error"+"$"+errToast);
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
                                OMSApplication.getInstance().setConfigDataAPIResponse("Error"+"$"+response);
							} catch (JSONException e) {
								Log.e(TAG,
										"JSONException occurred when is ConfigDBParse Failed."
												+ e.getMessage());
								e.printStackTrace();
							}
						}
					}
				}
				
				catch(SocketTimeoutException e){
					Log.e(TAG,
							"SocketTimeoutException occurred while excecuting the Config Service."
									+ e.getMessage());
					if (rListener != null) {
						rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
					}
					e.printStackTrace();
				}
				catch (SocketException e) {
					Log.e(TAG,
							"SocketException occurred while excecuting the Config Service."
									+ e.getMessage());
					if (rListener != null) {
						rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
					}
					e.printStackTrace();
				} 
				
				catch (IOException e) {

					Log.e(TAG,
							"IOException occurred while while excecuting the Config Service."
									+ e.getMessage());
					if (rListener != null) {
						rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
					}
					e.printStackTrace();
			}
		}else{
			configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
		}
		return configResponse;
	}

	
	/*private String enableOnceInADayWebServiceCall(String serviceUrl){
		String configResponse = ""; 	  
		String dateFormat="";
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(appContext);
	//	if(Integer.parseInt(OMSConstants.APP_ID) == -200 )
         {
        	 
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");   
			long currenttime = System.currentTimeMillis();
		    Date resultdate = new Date(currenttime);
		    dateFormat = sdf.format(resultdate);
			String configAPPId = (OMSApplication.getInstance().getAppId());
			String date1 = sharedPreferences.getString(configAPPId,"");
			if(!TextUtils.isEmpty(date1)) {
			Date d1 = null;
			try {
				d1 = sdf.parse(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    long diffe=   resultdate.getTime() - d1.getTime();
		    long hours = diffe / (60 * 60 * 1000);
		    if((hours+1)>=24){
		        Editor editor = sharedPreferences.edit();  
		    	editor.putString(configAPPId, dateFormat);
				editor.commit();
		    	configResponse = 	fetchURLConnectionConfigResponse(serviceUrl);
		    }else{
		    	configResponse = OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
						.getValue();
		    }
		    //System.out.println ("Hours Differences: " + diffe / (60 * 60 * 1000));
			}else{
				Editor editor = sharedPreferences.edit();  
				editor.putString(configAPPId, dateFormat);
				editor.commit();
				configResponse = 	fetchURLConnectionConfigResponse(serviceUrl);
			}
			
			
		      
		      
		      
		}
		return configResponse;
	}*/

}

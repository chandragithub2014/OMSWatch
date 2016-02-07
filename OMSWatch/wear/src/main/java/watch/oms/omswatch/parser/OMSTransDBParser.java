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
package watch.oms.omswatch.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import watch.oms.omswatch.R;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.utils.OMSAlertDialog;


/**
 * 
 * @author 280779
 * 
 *         Trans Parser: 1) Invokes Trans Service and retrieves the response in
 *         Asynhnoronous Task. 2) Parses the Service JSON response and
 *         inserts/updates the data into Trans DB tables.
 * 
 */

public class OMSTransDBParser extends AsyncTask<String, Void, String> {
	private static final String ALL_TABLES = "all";
	private final String TAG = this.getClass().getSimpleName();
	private Context appContext;
	private ProgressDialog pDialog;
	private OMSReceiveListener rListener;
	private OMSDBParserHelper configDBParserHelper;
	private OMSServerMapperHelper servermapperhelper;

	// AppMonitor
//	private NetworkUsageAnalyzer analyzer = null;
	private int connectionID = OMSDefaultValues.NONE_DEF_CNT.getValue();
	
    private String errorObject;
    private String errorMessageKey;
    private String errorMessageVal="";
    private String errorCodeKey="";
    private String errorCodeVal="";

	public OMSTransDBParser(Context ctx, OMSReceiveListener receiveListener) {
		appContext = ctx;
		// AppMonitor
		Random myRandom = new Random();
		connectionID = myRandom.nextInt(Integer.MAX_VALUE);
	//	analyzer = NetworkUsageAnalyzer.getInstance(appContext);
		configDBParserHelper = new OMSDBParserHelper();
		servermapperhelper = new OMSServerMapperHelper();
		rListener = receiveListener;

		if (!OMSConstants.USE_SPLASH) {
			pDialog = new ProgressDialog(appContext);
			pDialog.setMessage("Receiving TransDB..");
			pDialog.setCancelable(false);
			pDialog.show();
		}
	}

	@Override
	protected String doInBackground(String... args) {
		//HttpClient httpclient = null;
		String result = null;
		String parserResponse = null;
		String serviceUrl = args[0];
		/*HttpResponse response = null;
		StatusLine statusLine = null;
		HttpEntity httpEntity = null;*/
		boolean transToDownload = false;

		/*if (Integer.parseInt(OMSApplication.getInstance()
				.getAppId()) == 10) {
			double lastmodifiedTime = 0.0f;
			OMSServerMapperHelper helper = new OMSServerMapperHelper();

			lastmodifiedTime = helper
					.getLastModifiedTimeStamp(OMSApplication
							.getInstance().getEditTextHiddenVal());
			serviceUrl = OMSConstants.SERVER_NAME
					+ "/AiM/service/config/getConfigByAppId/"
					+ OMSApplication.getInstance()
							.getEditTextHiddenVal() + "/" + lastmodifiedTime;
		}*/
		Log.d(TAG, "Trans DB Service Url: " + serviceUrl);
		//Added code HTTPURLConnection
		if(!OMSDBManager.checkNetworkConnectivity()){
			parserResponse = OMSMessages.TRANS_DATABASE_FAILURE.getValue();
			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return parserResponse;
		}
		
		parserResponse = fetchURLConnectionTransResponse(serviceUrl);
//Commented code for HTTPURLConnection
/*		
		if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE){
			HashMap<String,String> errorResponseHashmap = servermapperhelper.getWebserviceResponseData(ALL_TABLES);
			if(errorResponseHashmap!=null) {
				errorObject=errorResponseHashmap.get("errorobject");
				errorMessageKey = errorResponseHashmap.get("messagekey");
				errorCodeKey = errorResponseHashmap.get("codekey");
			}
		}

		
		if(!OMSDBManager.checkNetworkConnectivity()){
			parserResponse = OMSMessages.TRANS_DATABASE_FAILURE.getValue();			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return parserResponse;
		}


		// AppMonitor
		analyzer.startNetworkConnection(serviceUrl,
				OMSMessages.CONNECTION_PREFIX.getValue() + connectionID);

		httpclient = AppSecurityAndPerformance.getInstance()
				.getAppGuardSecuredHttpClient(serviceUrl);

		HttpGet httpget = new HttpGet(serviceUrl);
		// AppMonitor
		analyzer.sentConnectionRequest(connectionID, serviceUrl.length());
		// for download limit check
		try {
			transToDownload = AppSecurityAndPerformance.getInstance()
					.downLoadLimitCheck(serviceUrl);
		} catch (IOException e1) {
			Log.e(TAG,
					"Error Occured in doInBackground(AppSecurityAndPerformance) ,Error is"
							+ e1.getMessage());
			e1.printStackTrace();
		}
		if (transToDownload) {
			try {
				response = httpclient.execute(httpget);
				// AppMonitor
				analyzer.updateConnectionStatus(connectionID, true);
				statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == OMSConstants.STATUSCODE_OK) {
					httpEntity = response.getEntity();
					if (httpEntity != null) {

						try {
							String jsonContent = EntityUtils
									.toString(httpEntity);
							Log.d(TAG, "Trans Servicce Response :"
									+ jsonContent);
							
							if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE) {
							    //result ="BLGetFailed";
								JSONObject reader;
								try {
									reader = new JSONObject(result);
								
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
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
							// Create a Reader from String
							Reader stringReader = new StringReader(jsonContent);
							readJsonStream(stringReader);
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
					parserResponse = OMSMessages.TRANS_DATABASE_SUCCESS.getValue();
				} else { // Failure
					parserResponse = OMSMessages.TRANS_DATABASE_FAILURE.getValue();
					httpEntity = response.getEntity();

					if (httpEntity != null) {

						// AppMonitor
						analyzer.receivedConnectionResponse(connectionID,
								httpEntity.getContentLength(),
								OMSDatabaseConstants.GET_TYPE_REQUEST);
						result = EntityUtils.toString(httpEntity);

						if (result != null) {
							Log.w(TAG, "Trans Service Response :" + result);
							result ="BLGetFailed";
							if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE) {
							    //result ="BLGetFailed";
								JSONObject reader;
								try {
									reader = new JSONObject(result);
								
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
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
							
							return parserResponse;
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
										"JSONException occurred when is TransDBParse Failed."
												+ e.getMessage());
								e.printStackTrace();
							}
						}
					}
				}
			} 
			
			catch(SocketTimeoutException e){
				Log.e(TAG,
						"SocketTimeoutException occurred while excecuting the trans Service."
								+ e.getMessage());
				if (rListener != null) {
					rListener.receiveResult(OMSMessages.TRANS_DB_ERROR.getValue());
				}
				e.printStackTrace();
			}
			catch (SocketException e) {
				Log.e(TAG,
						"SocketException occurred while excecuting the Trans Service."
								+ e.getMessage());
				analyzer.updateConnectionStatus(connectionID, false);
				analyzer.receivedConnectionResponse(connectionID, 0,
						OMSDatabaseConstants.GET_TYPE_REQUEST);
				e.printStackTrace();

				if (rListener != null) {
					rListener.receiveResult(OMSMessages.TRANS_DB_ERROR.getValue());
				}
				return OMSMessages.TRANS_DB_ERROR.getValue();
			} catch (IOException e) {
				analyzer.updateConnectionStatus(connectionID, false);
				analyzer.receivedConnectionResponse(connectionID, 0,
						OMSDatabaseConstants.GET_TYPE_REQUEST);
				Log.e(TAG,
						"IOException occurred while while excecuting the Trans Service."
								+ e.getMessage());
				e.printStackTrace();
				return OMSMessages.TRANS_DB_ERROR.getValue();

			}
			// for download limit check
		} else {
			parserResponse = OMSMessages.TRANS_DB_ERROR.getValue();
			Log.e(TAG, AppGuardConstants.LOG_DOWNLOAD_EXCEEDS_LIMIT);
			return parserResponse;
		}
		*/
		
		return parserResponse;
	}

	@Override
	protected void onPostExecute(String result) {
		if (!OMSConstants.USE_SPLASH) {
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
		if (rListener != null) {
			rListener.receiveResult(result);
		}
		if(!TextUtils.isEmpty(errorMessageVal) && !TextUtils.isEmpty(errorCodeVal)){
		//	Toast.makeText(appContext, errorMessageVal, Toast.LENGTH_LONG).show();
			OMSAlertDialog
			.displayAlertDialog(
                    appContext,
                    errorCodeVal + " " + errorMessageVal,
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
	}

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
					servermapperhelper.updateModifiedTimeStampForTransTable(
							ALL_TABLES, latestModifiedTimeStamp);
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
					Log.i(TAG,
							"Server DB Process Duration"
									+ dbDuration);
					OMSApplication.getInstance().setDatabaseProcessDuration(dbDuration);
					continue;
				}else if(SERVER_PROCESS_DURATION.equalsIgnoreCase(colName)){
					String serverProcessDuration = reader.nextString();
					OMSApplication.getInstance().setServerProcessDuration(serverProcessDuration);
					Log.i(TAG,
							"server process duration "
									+ serverProcessDuration);
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
			
			configDBParserHelper.insertOrUpdateTransDBDBBulk(tableName,
					contentValuesList);
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
	
	
	
	private HttpURLConnection getHttpURLConnection(String url){
		

		//Added code for HttpURLConnection	
		   // AppMonitor
				/*analyzer.startNetworkConnection(url, OMSMessages.CONNECTION_PREFIX.getValue()
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
					e1.printStackTrace();
				}
				conn.setDoInput(true);
				// AppMonitor
			//	analyzer.sentConnectionRequest(connectionID, url.length());
		        return conn;
			//End HttpURL Connection	
	}
	
	
	private String fetchURLConnectionTransResponse(String serviceURL){
		boolean transToDownload = false;
		String configResponse = null;
		String response="";
		InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
	/*	HttpURLConnection urlConnection = (HttpURLConnection) AppSecurityAndPerformance.getInstance()
				.getAppGuardSecuredHttpsUrlConnection(serviceURL);
		// AppMonitor
		analyzer.startNetworkConnection(serviceURL, OMSMessages.CONNECTION_PREFIX.getValue()
				+ connectionID);*/
	//	HttpURLConnection urlConnection = getHttpURLConnection(serviceURL);
		// for download limit check
				/*try {
					transToDownload = AppSecurityAndPerformance.getInstance()
							.downLoadLimitCheck(serviceURL);
				} catch (IOException e1) {
					Log.e(TAG,
							"Error Occured in doInBackground(AppSecurityAndPerformance) ,Error is"
									+ e1.getMessage());
					e1.printStackTrace();
				}*/
        try {
            URL url = new URL(serviceURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            //	HttpURLConnection urlConnection = getHttpURLConnection(serviceURL);
        }catch (IOException e){
            e.printStackTrace();
        }
				
		if(urlConnection!=null){
			
			urlConnection.setConnectTimeout(50000);
			  try {
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
	 //         if(transToDownload)
              {
			// AppMonitor
			//			analyzer.updateConnectionStatus(connectionID, true);

				try {
					int statusCode = urlConnection.getResponseCode();
					if (statusCode ==  OMSConstants.STATUSCODE_OK) {
						inputStream = new BufferedInputStream(urlConnection.getInputStream());
						 response = convertStreamToString(inputStream);
						 Log.d(TAG, "TransResponse for HTTPURLConnection:::"+response);
						// Create a Reader from String
							Reader stringReader = new StringReader(response);
							readJsonStream(stringReader);
							// AppMonitor
						/*	analyzer.receivedConnectionResponse(connectionID,
									urlConnection.getContentLength(),
									OMSDatabaseConstants.GET_TYPE_REQUEST);*/
							
							configResponse =  OMSMessages.TRANS_DATABASE_SUCCESS.getValue();
									
					}else{
						Log.e(TAG, "status code["+urlConnection.getResponseCode()+"] Reason["+urlConnection.getResponseMessage()+"]");
						
						configResponse = OMSMessages.TRANS_DATABASE_FAILURE.getValue();
						inputStream = new BufferedInputStream(urlConnection.getInputStream());
						// AppMonitor
						/*analyzer.receivedConnectionResponse(connectionID,
								urlConnection.getContentLength(),
								OMSDatabaseConstants.GET_TYPE_REQUEST);*/
						
					    response = convertStreamToString(inputStream);
	                     if (response != null) {
							
					//		errToast = urlConnection.getResponseMessage();
							
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
				} catch (IOException e) {
					/*analyzer.updateConnectionStatus(connectionID, false);
					analyzer.receivedConnectionResponse(connectionID, 0,
							OMSDatabaseConstants.GET_TYPE_REQUEST);*/
					Log.e(TAG,
							"IOException occurred while while excecuting the Trans Service."
									+ e.getMessage());
					e.printStackTrace();
					return OMSMessages.TRANS_DB_ERROR.getValue();
			}
		}		/*else{
					
				}*/
		}else{
			configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
		}
		return configResponse;
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

}

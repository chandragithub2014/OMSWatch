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
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
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

import watch.oms.omswatch.R;

import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.utils.OMSUtils;

public class TransDBParser extends AsyncTask<String, Void, String> {
	private final String TAG = this.getClass().getSimpleName();
	private Context appContext = null;
	//private ContentValues contentvalues = null;
	private ProgressDialog progressDialog = null;
	private OMSReceiveListener rListener = null;
	//private OMSDBParserHelper configDBParserHelper = null;
	//private OMSServerMapperHelper servermapperhelper = null;
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

	public TransDBParser(Context ctx,OMSReceiveListener receiveListener){
         appContext = ctx;
		 rListener = receiveListener;
	}
 	



	@Override
	protected String doInBackground(String... args) {
		//HttpClient httpclient = null;
		String result = null;
		String parserResponse = null;
		String serviceUrl = args[0];
		

		//Added code for HTTPURL COnnection
		if(!OMSUtils.getInstance().checkNetworkConnectivity(appContext)){
			parserResponse = OMSMessages.ACTION_CENTER_FAILURE.getValue();
			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return parserResponse;
		}
		parserResponse = fetchURLConnectionConfigResponse(serviceUrl);
		
		//Added code for HTTPURLCOnnection
/*		
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
*/
		return parserResponse;
	}

	@Override
	protected void onPostExecute(String result) {
		if (rListener != null) {
			rListener.receiveResult(result);
		}
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		Log.d(TAG, "Trans DB End:::"+System.currentTimeMillis());
	}

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
			Log.e(TAG, "Error:" + ex.getMessage());
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
				configResponse = "transgetfailure";
			} catch (IOException e) {
				Log.e(TAG,
						"IOException occurred while while excecuting the Trans Service."
								+ e.getMessage());
				e.printStackTrace();
				configResponse = "transgetfailure";

			} catch (Exception e) {
				Log.e(TAG,
						"Exception occurred while while excecuting the Trans Service."
								+ e.getMessage());
				e.printStackTrace();
				configResponse = "transgetfailure";
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
						    JSONObject responseJSON = new JSONObject();
						    responseJSON.put("statuscode",OMSConstants.STATUSCODE_OK);
						    responseJSON.put("serviceResponse",response);
						    OMSApplication.getInstance().setTransDataAPIResponse(responseJSON.toString());

						   /* Reader stringReader = new StringReader(response);
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
							*//*analyzer.receivedConnectionResponse(connectionID,
									urlConnection.getContentLength(),
									OMSDatabaseConstants.GET_TYPE_REQUEST);*//*
							
						*//*	ServerDBUpdateHelper dbhelper = new ServerDBUpdateHelper(appContext);
							dbhelper.insertCallTraceTypeData(ConsoleDBConstants.CALL_TRACE_TYPE_TABLE, ""+OMSApplication.getInstance().getAppId());*//*
*//*							Log.i(TAG, "Server URL::"+serviceURL);
							Log.i(TAG, "Server Response time::"+OMSApplication.getInstance().getServerProcessDuration());
*//*							Log.i(TAG, "ServerTime::"+OMSApplication.getInstance().getServerProcessDuration()+"\t"+"DBTime::"+OMSApplication.getInstance().getDatabaseProcessDuration()+"\t"+serviceURL );
							OMSApplication.getInstance().setTraceType(traceType);*/
							configResponse =  "transgetsuccess";//OMSMessages.BL_SUCCESS.getValue();
					}else{
						Log.e(TAG, "status code[" + urlConnection.getResponseCode() + "] Reason[" + urlConnection.getResponseMessage() + "]");
						
						configResponse = "transgetfailure";
						/*ContentValues contentValues = new ContentValues();
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
								OMSDatabaseConstants.ACTION_STATUS_TRIED);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
								OMSDatabaseConstants.GET_TYPE_REQUEST);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
								serviceURL);
						contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
								tableName);
						actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
								uniqueRowId,configAppId);*/
						inputStream = new BufferedInputStream(urlConnection.getInputStream());
						// AppMonitor
						/*analyzer.receivedConnectionResponse(connectionID,
								urlConnection.getContentLength(),
								OMSDatabaseConstants.GET_TYPE_REQUEST);*/
						
					    response = convertStreamToString(inputStream);
	                     if (response != null) {
							 JSONObject responseJSON = new JSONObject();
							 responseJSON.put("statuscode",urlConnection.getResponseCode());
							 responseJSON.put("responsemessage",urlConnection.getResponseMessage());
							 responseJSON.put("serviceResponse",configResponse);
							 OMSApplication.getInstance().setTransDataAPIResponse(responseJSON.toString());
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
								JSONObject responseJSON = new JSONObject();
								responseJSON.put("statuscode",urlConnection.getResponseCode());
								responseJSON.put("responsemessage","");
								responseJSON.put("serviceResponse",response);
								OMSApplication.getInstance().setTransDataAPIResponse(responseJSON.toString());
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
					configResponse = "transgetfailure";
				} catch (IOException e) {
					Log.e(TAG,
							"IOException occurred while while excecuting the Trans Service."
									+ e.getMessage());
					e.printStackTrace();
					configResponse = "transgetfailure";

				}  catch (Exception e) {
					Log.e(TAG,
							"Exception occurred while while excecuting the Trans Service."
									+ e.getMessage());
					e.printStackTrace();
					configResponse = "transgetfailure";
				}
		}else{
			try {
				JSONObject responseJSON = new JSONObject();
				responseJSON.put("statuscode", "");
				responseJSON.put("responsemessage", "");
				responseJSON.put("serviceResponse", OMSMessages.ACTION_CENTER_FAILURE.getValue());
				OMSApplication.getInstance().setTransDataAPIResponse(responseJSON.toString());
			}catch (JSONException e){
				e.printStackTrace();
			}
			configResponse = "transgetfailure";
		}
		return configResponse;
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


	/**
	 * 
	 * Worker Thread to update last modified timestamp in all Trans tables
	 * 
	 */

	
	

	
	//End of Bulk
}
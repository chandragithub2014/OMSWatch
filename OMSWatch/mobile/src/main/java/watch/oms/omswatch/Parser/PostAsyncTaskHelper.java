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
import android.net.ParseException;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.IllegalFormatConversionException;
import java.util.Iterator;
import java.util.Random;

import watch.oms.omswatch.R;


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
 *         Asynchronous Task Helper to execute the 'POST' type Actions.
 */
public class PostAsyncTaskHelper extends AsyncTask<String, Void, String> {

	private final String TAG = this.getClass().getSimpleName();
	private Context context = null;
	private ProgressDialog progressDialog = null;
	private JSONObject jsonPayLoad = null;
	private String tableName = null;
	//private ActionCenterHelper actionCenterHelper = null;
	private String uniqueRowId = null;
	//private OMSTransHelper transdbHelper = null;
	private OMSReceiveListener rListener = null;
	private int configAppId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private String progressString;
	private String clientTableName = null;
	private String serviceUrl	=	null;
	private String schemaName=null;
	//private OMSServerMapperHelper servermapperhelper;
	
    private String errorObject;
    private String errorMessageKey;
    private String errorMessageVal="";
    private String errorCodeKey="";
    private String errorCodeVal="";
    private String successMessageColumn="";
	private String successMessage="";
	//private NetworkUsageAnalyzer analyzer = null;
	private int connectionID = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private static final String CONNECTION_PREFIX = "CON";
	
	public PostAsyncTaskHelper(Context c,OMSReceiveListener receiveListener,JSONObject JSONPayLoadList) {
		this.context = c;
		/*this.actionCenterHelper = new ActionCenterHelper(context);
		this.transdbHelper = new OMSTransHelper(context);*/
		this.jsonPayLoad = JSONPayLoadList;
		this.rListener = receiveListener;
		/*this.uniqueRowId = uniqueId;
		this.configAppId = appId;
*/		this.clientTableName = clientTableName;
/*		servermapperhelper = new OMSServerMapperHelper();
		progressString = c.getResources().getString(R.string.post);
		progressDialog = new ProgressDialog(context);
		if(!TextUtils.isEmpty(loadMessage)){
			progressDialog.setMessage(loadMessage);
		}else{
		progressDialog.setMessage(progressString);
		}
		progressDialog.show();*/
	}

	@Override
	protected String doInBackground(String... args) {
		serviceUrl = args[0];
		/*tableName = args[1];
		schemaName=args[2];
		if(args.length>=5){
			successMessageColumn=args[3];
		    successMessage=args[4];
	    }*/
		String response = null;
		String result = null;
		//HttpClient httpClient = null;
		// AppMonitor Variable
		 /*analyzer = NetworkUsageAnalyzer
				.getInstance(context);*/
		// AppMonitor Variable
    	 connectionID = new Random().nextInt(Integer.MAX_VALUE);
		Log.i(TAG, "Service URL : " + serviceUrl);
		Log.d(TAG, "JsonPayLoad : " + jsonPayLoad.toString());
	//	Log.d(TAG, "Table Name : " + tableName);
		if(!OMSUtils.getInstance().checkNetworkConnectivity(context)){
			response = OMSMessages.ACTION_CENTER_FAILURE.getValue();
			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return response;
		}	
		
	  response = 	fetchPostResponse(jsonPayLoad,serviceUrl);
		
/*		if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE){
			String serverTableName = servermapperhelper.getServerTableName(clientTableName);
			HashMap<String,String> errorResponseHashmap = servermapperhelper.getWebserviceResponseData(serverTableName);
			if(errorResponseHashmap!=null) {
				errorObject=errorResponseHashmap.get("errorobject");
				errorMessageKey = errorResponseHashmap.get("messagekey");
				errorCodeKey = errorResponseHashmap.get("codekey");
			}
		}
		if(!OMSDBManager.checkNetworkConnectivity()){
			response = OMSMessages.ACTION_CENTER_FAILURE.getValue();
			Log.d(TAG,
					" Network not available.Please try to connect after some time");
			return response;
		}

			try {
				httpClient = AppSecurityAndPerformance.getInstance()
						.getAppGuardSecuredHttpClient(serviceUrl);
			} catch (Exception e) {
				Log.e(TAG, "Exception occurred in getAppGuardSecuredHttpClient"
						+ e.getMessage());
				e.printStackTrace();
			}

		HttpPost httpPost = new HttpPost(serviceUrl);
		StringEntity entity = null;

		try {
			entity = new StringEntity(jsonPayLoad.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG,
					"UnsupportedEncodingException occurred while converting jSon Payload to String"
							+ e.getMessage());
			e.printStackTrace();
		}

		httpPost.setEntity(entity);
		// AppMonitor START
		analyzer.startNetworkConnection(serviceUrl, String.valueOf(connectionID));
		analyzer.sentConnectionRequest(connectionID, jsonPayLoad.toString()
				.length());
		// AppMonitor END
		httpPost.setHeader(OMSMessages.ACCEPT.getValue(),
				OMSMessages.APP_JSON.getValue());
		httpPost.setHeader(OMSMessages.CONTENT_TYPE.getValue(),
				OMSMessages.APP_JSON.getValue());
		// This changes are for Oasis Project. # Start
//		if(serviceUrl.contains("oasiswebservices") && ! serviceUrl.contains("ValidateUser")){
//				httpPost.setHeader("UserKey",
//					OMSApplication.getInstance().getUserKey());
//			}
		// This changes are for Oasis Project. # END
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			// AppMonitor START
			analyzer.updateConnectionStatus(connectionID, true);
			analyzer.receivedConnectionResponse(connectionID, statusLine
					.toString().length(), OMSDatabaseConstants.POST_TYPE_REQUEST);
			// AppMonitor END

			Log.d(TAG, "Response Code :" + statusLine);

			if (statusLine != null
			// Success 200
					&& statusLine.getStatusCode() == OMSConstants.STATUSCODE_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();

				if (httpEntity != null) {
					if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE){
						String serverTableName = servermapperhelper.getServerTableName(clientTableName);
						if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE) {
						    //result ="BLGetFailed";
							JSONObject reader;
							try {
								reader = new JSONObject(EntityUtils.toString(httpEntity));
							
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
						response= processPostResponse(serverTableName, httpEntity);
					}
					else{
						response= processPostResponse(clientTableName, httpEntity);
					}
						return response;
				} else {
					Log.e(TAG, "HttpEntity is NULL :" + httpEntity);
				}
			} else { // Failure
				response = OMSMessages.ACTION_CENTER_FAILURE.getValue();
				HttpEntity httpEntity = httpResponse.getEntity();

				if (httpEntity != null) {
					InputStream is = httpEntity.getContent();
					result = convertStreamToString(is);

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
					if (result != null) {
						Log.d(TAG, "Post Service Response :" + result);
						try {
							JSONObject resultJSON = new JSONObject(result);
							
							JSONObject childResponse = resultJSON.getJSONObject("response");
							if(!childResponse.getString("code").equals("00")){
								response = OMSMessages.BL_FAILURE.getValue();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
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
									"JSON Exception occurred in ActionCenterFailure."
											+ e.getMessage());
							e.printStackTrace();
						}
					}
				}

				parseJSONObject(jsonPayLoad.toString(),
						OMSDatabaseConstants.STATUS_TYPE_TRIED, 0.0);
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
						OMSDatabaseConstants.ACTION_STATUS_TRIED);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
						serviceUrl);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
						tableName);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
						OMSDatabaseConstants.POST_TYPE_REQUEST);
				contentValues.put(OMSDatabaseConstants.BL_SCHEMA_NAME,
						schemaName);
				actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
						uniqueRowId,configAppId);
				
				//response = OMSMessages.FAILED.getValue();
				response = OMSMessages.BL_FAILURE.getValue();
				Log.e(TAG, "Post Action Failed");

			}
		} catch (SocketTimeoutException se) {
			progressDialog.dismiss();
			Log.e(TAG,
					"SocketTimeoutException occurred while Posting the Actions"
							+ se.getMessage());
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.ACTION_CENTER_FAILURE
						.getValue());
			}
			se.printStackTrace();
		} catch (ClientProtocolException e) {
			progressDialog.dismiss();
			Log.e(TAG,
					"ClientProtocolException occurred while Posting the Actions"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			progressDialog.dismiss();
			Log.e(TAG,
					"IOException occurred while Posting the Actions"
							+ e.getMessage());
			e.printStackTrace();
			
			ContentValues contentValues = new ContentValues();
			contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
					OMSDatabaseConstants.ACTION_STATUS_TRIED);
			contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
					serviceUrl);
			contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
					tableName);
			contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
					OMSDatabaseConstants.POST_TYPE_REQUEST);
			contentValues.put(OMSDatabaseConstants.BL_SCHEMA_NAME,
					schemaName);
			actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
					uniqueRowId,configAppId);
			response = OMSMessages.BL_FAILURE.getValue();
			return response;
		}
*/
		return response;
	}

	protected void onPostExecute(String result) {
		if (rListener != null) {
			rListener.receiveResult(result);
		}
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
			
			if(result.equalsIgnoreCase(OMSMessages.ACTION_CENTER_FAILURE
						.getValue()))
				 result = "BL Post Failed.";	
			
		//	Toast.makeText(context, result, Toast.LENGTH_LONG).show();
		}
		
		/*if(!TextUtils.isEmpty(errorMessageVal) && !TextUtils.isEmpty(errorCodeVal)){
			//	Toast.makeText(appContext, errorMessageVal, Toast.LENGTH_LONG).show();
				OMSAlertDialog
				.displayAlertDialog(
                        context,
                        errorCodeVal + " " + errorMessageVal,
                        "Ok");
			}else if(!TextUtils.isEmpty(errorMessageVal) && TextUtils.isEmpty(errorCodeVal))
			{
				OMSAlertDialog
				.displayAlertDialog(
						context,
						errorMessageVal,
						"Ok");
			} else if(TextUtils.isEmpty(errorMessageVal) && !TextUtils.isEmpty(errorCodeVal)){
				OMSAlertDialog
				.displayAlertDialog(
						context,
						errorCodeVal,
						"Ok");
			}*/
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		progressDialog.show();
	}

	/**
	 * Converts InputStream into String
	 * 
	 * @param is
	 * @return String
	 */
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + OMSMessages.NEWLINE_CHAR.getValue());
			}
		} catch (IOException e) {
			Log.e(TAG,
					"IOException occurred while convertStreamToString"
							+ e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				is.close();
			} catch (IOException e) {
				Log.e(TAG, "IOException occurred while convertStreamToString"
						+ e.getMessage());
			}
		}
		return sb.toString();
	}

	/**
	 * Parse JSON Object and insert into DB with status as 'tried'.
	 * 
	 * @param
	 * @param
	 */
	@SuppressWarnings("unchecked")
	/*private void parseJSONObject(String jsonobject, String statusType,
			double modifiedDate) {
		JSONArray jsonArray = null;
		JSONObject tableJSON = null;
		Iterator<String> tableIterator = null;
		Iterator<String> childIterator = null;
		JSONObject childJSON = null;
		ContentValues contentvalues = null;
		String colName = null;

		try {
			JSONObject toBeParsed = new JSONObject(jsonobject);

			childJSON = new JSONObject(jsonobject);
			childIterator = childJSON.keys();
			while (childIterator.hasNext()) {
				
				tableName= (String) childIterator
						.next();
				Log.d(TAG, "Next:::"+tableName);
				jsonArray = toBeParsed.getJSONArray(tableName);

				if (jsonArray != null) {

					for (int i = 0; i < jsonArray.length(); i++) {
						contentvalues = new ContentValues();
						tableJSON = jsonArray.getJSONObject(i);
						tableIterator = tableJSON.keys();

						while (tableIterator.hasNext()) {
							colName = (String) tableIterator.next();

							if (colName
									.equalsIgnoreCase(OMSDatabaseConstants.UNIQUE_ROW_ID)) {
								contentvalues.put(colName,
										tableJSON.getString(colName));
								contentvalues
										.put(OMSDatabaseConstants.TRANS_TABLE_STATUS_COLUMN,
												statusType);
								contentvalues.put(
										OMSDatabaseConstants.TRANS_MODIFIED_DATE,
										modifiedDate);
								if(!TextUtils.isEmpty(successMessage) && !TextUtils.isEmpty(successMessageColumn)){
									
								
								if(statusType.equalsIgnoreCase(OMSDatabaseConstants.STATUS_TYPE_FINISHED)){
									contentvalues.put(
											successMessageColumn,
											successMessage);
								}else{
									contentvalues.put(
											successMessageColumn,
											"");
								}
								}
								transdbHelper.updateTransDataStatus(tableName,
										contentvalues,
										tableJSON.getString(colName));
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			Log.e(TAG,
					"JSON Exception occurred while Parsing the Service response."
							+ e.getMessage());
			e.printStackTrace();
		}

	}
	*/
	/**
	 * @param
	 * @param
	 * @return
	 */
	/*private String processPostResponse(String clientTableName, HttpEntity httpEntity){
		String 		response 			=	null;
		String 		jsonString			=	null;
		JSONObject 	responseJSONObject	=	null;
		
		try{
			jsonString = EntityUtils.toString(httpEntity);
			
			JSONObject responseWebServiceJSON =  new JSONObject(jsonString);
			
			responseJSONObject = responseWebServiceJSON
					.getJSONObject(OMSMessages.RESPONSE_STRING
							.getValue());
		}catch(JSONException jex){
			if(jex!=null && jex.getMessage().contains("No value for "+(OMSMessages.RESPONSE_STRING.getValue()))){
				Log.d(TAG, "response key is not present. Assuming json response contains data set");
				// This changes are for Oasis Project. # Start
				DataParser dataParser = new DataParser(context);
				// This changes are for Oasis Project. # End
				dataParser.parseAndUpdate(jsonString);
				response = "BLPostSuccess";
			}else{
				Log.e(TAG,
						"Exception occurred while reading the json response. Error is:"
								+ jex.getMessage());
			}
			return response = OMSMessages.ACTION_CENTER_FAILURE.getValue();
		} catch (ParseException e) {
			Log.e(TAG,
					"Exception occurred while parsing the json response. Error is:"
							+ e.getMessage());
		} catch (IOException e) {
			Log.e(TAG,
					"IO Exception occurred while parsing the json response. Error is:"
							+ e.getMessage());
		}
				
		try{
			String code = responseJSONObject
					.getString(OMSMessages.CODE.getValue());
			if (code.equals(OMSMessages.DEFAULT_JSON_CODE.getValue())) {
				Log.i(TAG,
						"Response Message :"
								+ responseJSONObject
										.getString(OMSMessages.MESSAGE
												.getValue()));
				double processedModifiedDate = responseJSONObject
						.getDouble(OMSMessages.PROCESSED_DATE
								.getValue());
				
				parseJSONObject(jsonPayLoad.toString(),
						OMSDatabaseConstants.STATUS_TYPE_FINISHED,
						processedModifiedDate);
				progressString = context.getResources().getString(
						R.string.post_success);
				response = "BLPostSuccess";

			} else {
				Log.e(TAG,
						"Response Message :"
								+ responseJSONObject
										.getString(OMSMessages.MESSAGE
												.getValue()));
				try {
					Log.e(TAG,
							"Response Message Additional:"
									+ responseJSONObject
											.getString(OMSMessages.ERROR_ADDITIONAL_MESSAGE
													.getValue()));
				} catch (JSONException je) {
					// ignore if field is not available
				}
				response = OMSMessages.ACTION_CENTER_FAILURE
						.getValue();
				
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
						OMSDatabaseConstants.ACTION_STATUS_TRIED);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
						serviceUrl);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
						tableName);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
						OMSDatabaseConstants.POST_TYPE_REQUEST);
				contentValues.put(OMSDatabaseConstants.BL_SCHEMA_NAME,
						schemaName);
			actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
						uniqueRowId,configAppId);
			}
		} catch (JSONException e) {
			Log.e(TAG,
					"Exception occurred while updating the Action Queue status."
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalFormatConversionException e) {
			Log.e(TAG,
					"Exception occurred while updating the Action Queue status."
							+ e.getMessage());
			e.printStackTrace();
		}
		return response;
	}*/
	
	private String fetchPostResponse(JSONObject jsonPayLoad,String serviceURL){
		String postResponse="";
		InputStream inputStream = null;
		OutputStream os = null;
		HttpURLConnection conn=null;
		try {
		     /* conn = (HttpURLConnection) AppSecurityAndPerformance.getInstance()
					.getAppGuardSecuredHttpsUrlConnection(serviceURL);*/
			URL urlToRequest = new URL(serviceURL);
			conn =	(HttpURLConnection) urlToRequest.openConnection();
			 String jsonMessage = jsonPayLoad.toString();
			 conn.setReadTimeout( 10000 /*milliseconds*/ );
			  conn.setConnectTimeout( 15000 /* milliseconds */ );
			  conn.setRequestMethod("POST");
			  conn.setDoInput(true);
			  conn.setDoOutput(true);
			  conn.setFixedLengthStreamingMode(jsonMessage.getBytes().length);
			  
			//make some HTTP header nicety
			  conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			  conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			  conn.setRequestProperty("Content-Encoding", "gzip");


			/*// AppMonitor
				analyzer.startNetworkConnection(serviceURL, OMSMessages.CONNECTION_PREFIX.getValue()
						+ connectionID);
				if(conn!=null){
					// AppMonitor
					analyzer.updateConnectionStatus(connectionID, true);
				}
			  //open*/
			  conn.connect();
			//setup send
			   os = new BufferedOutputStream(conn.getOutputStream());
			   os.write(jsonMessage.getBytes());
			  //clean up
			  os.flush();
			  inputStream  = new BufferedInputStream(conn.getInputStream());
		      String serviceResponse = convertStreamToString(inputStream);
			  int statusCode = conn.getResponseCode();
			 
			 if(statusCode ==  OMSConstants.STATUSCODE_OK){
				// AppMonitor
					/*analyzer.receivedConnectionResponse(connectionID,
							conn.getContentLength(),
							OMSDatabaseConstants.POST_TYPE_REQUEST);*/
		 if (serviceResponse != null) { 
			 Log.d(TAG, "PostResponse for HTTPURLConnection:::"+serviceResponse);
             try {
                 JSONObject responseJSON = new JSONObject();
                 responseJSON.put("statuscode", OMSConstants.STATUSCODE_OK);
                 responseJSON.put("serviceResponse", serviceResponse);

                 OMSApplication.getInstance().setTransPostDataAPIResponse(responseJSON.toString());
             }catch (JSONException e){
                 e.printStackTrace();
             }
             postResponse =  "transpostsuccess";
			/* postResponse= processHttpURLConnectionPostResponse(clientTableName,serviceResponse);
			 String traceType = OMSApplication.getInstance().getTraceType();
			 OMSApplication.getInstance().setTraceType(OMSConstants.TRACE_TYPE_SERVER);
				*//*ServerDBUpdateHelper dbhelper = new ServerDBUpdateHelper(context);
				dbhelper.insertCallTraceTypeData(ConsoleDBConstants.CALL_TRACE_TYPE_TABLE, ""+OMSApplication.getInstance().getAppId());*//*
				*//*Log.i(TAG, "Server Response time::"+OMSApplication.getInstance().getServerProcessDuration());
				Log.i(TAG, "DataBase Response time::"+OMSApplication.getInstance().getDatabaseProcessDuration());
				*//*	Log.i(TAG, serviceURL+"\n"+ "ServerResponseTime::"+OMSApplication.getInstance().getServerProcessDuration()+"\n"+"DataBaseResponseTime::"+OMSApplication.getInstance().getDatabaseProcessDuration());
				OMSApplication.getInstance().setTraceType(traceType);	*/

		 }else{

             postResponse =  "transpostfailure";
             try {
             JSONObject responseJSON = new JSONObject();
             responseJSON.put("statuscode", "00");
             responseJSON.put("serviceResponse", postResponse);
             OMSApplication.getInstance().setTransPostDataAPIResponse(postResponse);
             }catch (JSONException e){
                 e.printStackTrace();
             }
         }

			 }else{

				  postResponse = "transpostfailure";
				  inputStream  = new BufferedInputStream(conn.getInputStream());
				// AppMonitor
					/*analyzer.receivedConnectionResponse(connectionID,
							conn.getContentLength(),
							OMSDatabaseConstants.POST_TYPE_REQUEST);*/
 			    	 String result = convertStreamToString(inputStream);
					if (result != null) {
						Log.d(TAG, "Post Service Response :" + result);
						try {
							JSONObject resultJSON = new JSONObject(result);
							
							JSONObject childResponse = resultJSON.getJSONObject("response");
							if(!childResponse.getString("code").equals("00")){

								postResponse = "transpostfailure";// OMSMessages.BL_FAILURE.getValue();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
                        try {
                            JSONObject responseJSON = new JSONObject();
                            responseJSON.put("statuscode", "00");
                            responseJSON.put("serviceResponse", postResponse);
                            OMSApplication.getInstance().setTransPostDataAPIResponse(responseJSON.toString());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
					} else {
						try {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put(OMSMessages.ERROR.getValue(),
									conn.getResponseCode());
							jsonObject.put(
									OMSMessages.ERROR_DESCRIPTION.getValue(),
									conn.getResponseMessage());
							result = jsonObject.toString();
                            try {
                                JSONObject responseJSON = new JSONObject();
                                responseJSON.put("statuscode", conn.getResponseCode());
                                responseJSON.put("serviceResponse", conn.getResponseMessage());
                                OMSApplication.getInstance().setTransPostDataAPIResponse(responseJSON.toString());
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
							
						} catch (JSONException e) {
							Log.e(TAG,
									"JSON Exception occurred in ActionCenterFailure."
											+ e.getMessage());
							e.printStackTrace();
						}
					}
					
			  	
					/*parseJSONObject(jsonPayLoad.toString(),
							OMSDatabaseConstants.STATUS_TYPE_TRIED, 0.0);
					
					ContentValues contentValues = new ContentValues();
					contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
							OMSDatabaseConstants.ACTION_STATUS_TRIED);
					contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
							serviceUrl);
					contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
							tableName);
					contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
							OMSDatabaseConstants.POST_TYPE_REQUEST);
					contentValues.put(OMSDatabaseConstants.BL_SCHEMA_NAME,
							schemaName);
					actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
							uniqueRowId,configAppId);*/
					
					//response = OMSMessages.FAILED.getValue();
					postResponse = "transpostfailure";//OMSMessages.BL_FAILURE.getValue();
					Log.e(TAG, "Post Action Failed");
			  }

		} catch (SocketTimeoutException se) {
			progressDialog.dismiss();
			Log.e(TAG,
					"SocketTimeoutException occurred while Posting the Actions"
							+ se.getMessage());
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.ACTION_CENTER_FAILURE
						.getValue());
			}
			se.printStackTrace();
		}/* catch (ClientProtocolException e) {
			progressDialog.dismiss();
			Log.e(TAG,
					"ClientProtocolException occurred while Posting the Actions"
							+ e.getMessage());
			e.printStackTrace();
		}*/
	    catch(ProtocolException pe){
	    	progressDialog.dismiss();
			postResponse = "transpostfailure";// OMSMessages.BL_FAILURE.getValue();
			Log.e(TAG, "ProtocolException");
	    }
		catch(MalformedURLException mle){
			progressDialog.dismiss();
			postResponse = OMSMessages.BL_FAILURE.getValue();
			Log.e(TAG, "MalformedURLException");
		}
	
		catch (IOException e) {
		//	progressDialog.dismiss();
            Log.e(TAG,
                    "IOException occurred while Posting the Actions"
                            + e.getMessage());
			e.printStackTrace();
			
		/*	ContentValues contentValues = new ContentValues();
			contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
					OMSDatabaseConstants.ACTION_STATUS_TRIED);
			contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
					serviceUrl);
			contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
					tableName);
			contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
					OMSDatabaseConstants.POST_TYPE_REQUEST);
			contentValues.put(OMSDatabaseConstants.BL_SCHEMA_NAME,
					schemaName);
			actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
					uniqueRowId,configAppId);*/
			postResponse = "transpostfailure";// OMSMessages.BL_FAILURE.getValue();
			return postResponse;
		}

		  finally {  
			  //clean up
			  try {
				os.close();
			}
			  catch(NullPointerException ex){
				  postResponse = OMSMessages.BL_FAILURE.getValue();
				Log.e(TAG, "NullPointerException");
			  }
			  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				inputStream.close();
			} catch(NullPointerException ex){
				  postResponse = OMSMessages.BL_FAILURE.getValue();
				 Log.e(TAG, "NullPointerException");
			  }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  conn.disconnect();
			}
		return postResponse;
	}
	
	
	/**
	 * @param
	 * @param
	 * @return
	 */
	/*private String processHttpURLConnectionPostResponse(String clientTableName,String postResponse){
		String 		response 			=	null;
		String 		jsonString			=	null;
		JSONObject 	responseJSONObject	=	null;
		
		try{
			//jsonString = EntityUtils.toString(httpEntity);
			
			JSONObject responseWebServiceJSON =  new JSONObject(postResponse);
			
			responseJSONObject = responseWebServiceJSON
					.getJSONObject(OMSMessages.RESPONSE_STRING
							.getValue());
			String  dbProcessDuration = responseWebServiceJSON.getString("dbprocessduration");
			String serverProcessDuration = responseWebServiceJSON.getString("serverprocessduration");
			
			if(!TextUtils.isEmpty(dbProcessDuration)){
				OMSApplication.getInstance().setDatabaseProcessDuration(dbProcessDuration);
			}
			if(!TextUtils.isEmpty(serverProcessDuration)){
				OMSApplication.getInstance().setServerProcessDuration(serverProcessDuration);
			}
			
			*//*  "dbprocessduration": 297,
			  "serverprocessduration": 297
			  
			  String d*//*
		}catch(JSONException jex){
			if(jex!=null && jex.getMessage().contains("No value for "+(OMSMessages.RESPONSE_STRING.getValue()))){
				Log.d(TAG, "response key is not present. Assuming json response contains data set");
				// This changes are for Oasis Project. # Start
				DataParser dataParser = new DataParser(context);
				// This changes are for Oasis Project. # End
				dataParser.parseAndUpdate(jsonString);
				response = "BLPostSuccess";
			}else{
				Log.e(TAG,
						"Exception occurred while reading the json response. Error is:"
								+ jex.getMessage());
			}
			return response = OMSMessages.ACTION_CENTER_FAILURE.getValue();
		} catch (ParseException e) {
			Log.e(TAG,
					"Exception occurred while parsing the json response. Error is:"
							+ e.getMessage());
		}
				
		try{
			String code = responseJSONObject
					.getString(OMSMessages.CODE.getValue());
			if (code.equals(OMSMessages.DEFAULT_JSON_CODE.getValue())) {
				Log.i(TAG,
						"Response Message :"
								+ responseJSONObject
										.getString(OMSMessages.MESSAGE
												.getValue()));
				double processedModifiedDate = responseJSONObject
						.getDouble(OMSMessages.PROCESSED_DATE
								.getValue());
				
				parseJSONObject(jsonPayLoad.toString(),
						OMSDatabaseConstants.STATUS_TYPE_FINISHED,
						processedModifiedDate);
				progressString = context.getResources().getString(
						R.string.post_success);
				response = "BLPostSuccess";

			} else {
				Log.e(TAG,
						"Response Message :"
								+ responseJSONObject
										.getString(OMSMessages.MESSAGE
												.getValue()));
				try {
					Log.e(TAG,
							"Response Message Additional:"
									+ responseJSONObject
											.getString(OMSMessages.ERROR_ADDITIONAL_MESSAGE
													.getValue()));
				} catch (JSONException je) {
					// ignore if field is not available
				}
				response = OMSMessages.ACTION_CENTER_FAILURE
						.getValue();
				
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS,
						OMSDatabaseConstants.ACTION_STATUS_TRIED);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL,
						serviceUrl);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME,
						tableName);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE,
						OMSDatabaseConstants.POST_TYPE_REQUEST);
				contentValues.put(OMSDatabaseConstants.BL_SCHEMA_NAME,
						schemaName);
			actionCenterHelper.insertOrUpdateTransactionFailQueue(contentValues,
						uniqueRowId,configAppId);
			}
		} catch (JSONException e) {
			Log.e(TAG,
					"Exception occurred while updating the Action Queue status."
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalFormatConversionException e) {
			Log.e(TAG,
					"Exception occurred while updating the Action Queue status."
							+ e.getMessage());
			e.printStackTrace();
		}
		return response;
	}*/
	
}
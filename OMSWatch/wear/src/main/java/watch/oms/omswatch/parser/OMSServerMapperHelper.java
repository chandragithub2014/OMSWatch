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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSMessages;

/**
 * @author 280779 ServerMapperHelper - This helper class is used to retrieve
 *         data from server mapper table to synchronize server side data with
 *         client end data
 */
public class OMSServerMapperHelper {

	private final String TAG = this.getClass().getSimpleName();

	/**
	 * get client table name corresponding to the server table name
	 * 
	 * @param serverTableName
	 * @return String
	 */
	public String getClientTableName(String serverTableName) {
		String clientTableName = null;
		Cursor serverMapperCursor;
		try {
			serverMapperCursor = OMSDBManager
					.getConfigDB()
					.query(OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
							new String[] { OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME },
							OMSDatabaseConstants.SERVER_MAPPER_SERVERTABLENAME
									+ "='"
									+ serverTableName
									+ "'"
									+ " and "
									+ OMSDatabaseConstants.CONFIGDB_APPID
									+ " = '"
									+ OMSApplication.getInstance()
											.getAppId() + "'", null, null,
							null, null);
			if (serverMapperCursor.moveToFirst()) {
				clientTableName = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME));
			}
			serverMapperCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getClientTableName for input parameter serverTableName["
							+ serverTableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getClientTableName for input parameter serverTableName["
							+ serverTableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return clientTableName;
	}

	/**
	 * update date in servermapper table after synchronizing the client database
	 * 
	 * @param tableName
	 * @param maxTimeStamp
	 * @return int
	 */
	public int updateModifiedTimeStampForTransTable(String tableName,
			double maxTimeStamp) {
		int updatedRow = -1;
		try {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put(OMSDatabaseConstants.MODIFIED_DATE, maxTimeStamp);
			updatedRow = OMSDBManager.getConfigDB().update(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
					contentvalues,
					OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME
							+ "= ? "
							+ " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '"
							+ OMSApplication.getInstance()
									.getAppId() + "'",
					new String[] { tableName });
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method updateModifiedTimeStampForTransTable for input parameter tableName["
							+ tableName
							+ "], maxTimeStamp["
							+ maxTimeStamp
							+ "]. " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return updatedRow;
	}



	/**
	 * fetch last modified date from server mapper table
	 * 
	 * @param appID
	 * @return double
	 */
	public double getLastModifiedTimeStamp(String appID) {
		double modifiedDate = 0.0f;
		Cursor transCursor;
		try {
			transCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.VISITED_DATE_MAPPER_TABLE_NAME, null,
					OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appID + "'",
					null, null, null, null);
			if (transCursor.moveToFirst()) {
				modifiedDate = transCursor.getDouble(transCursor
						.getColumnIndex(OMSDatabaseConstants.MODIFIED_DATE));
				Log.d(TAG, "getLastModifiedTimeStamp :: " + modifiedDate);
			}
			transCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getLastModifiedTimeStamp for input parameter appID["
							+ appID + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return modifiedDate;

	}

	

	/**
	 * generate trans url by passing client table name to fetch data from server
	 * 
	 * @param clientTableName
	 * @param requestType
	 *            get/post
	 * @return String
	 */
	public String getTransURL(String clientTableName, String requestType) {
		String url = "";
		double modifiedDate = 0.0;
		String serverTableName = "";
		Cursor serverMapperCursor = null;
		String serverUrl = null;
		String serverSchema = null;
		String serverGetUrl = null;

		try {
			serverMapperCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
					null,
					OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME
					+ "='"
					+ clientTableName
					+ "'"
					+ " and "
					+ OMSDatabaseConstants.CONFIGDB_APPID
					+ " = '"
					+ OMSApplication.getInstance()
					.getAppId() + "'", null, null, null, null);

			if (serverMapperCursor.moveToFirst()) {
				modifiedDate = serverMapperCursor.getDouble(serverMapperCursor
						.getColumnIndex(OMSDatabaseConstants.MODIFIED_DATE));
				serverTableName = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_SERVERTABLENAME));
				//if (!Constants.USE_SERVER_MAPPER_GET_POST_URLS)
					serverUrl = serverMapperCursor
					.getString(serverMapperCursor
							.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_URL));
				//else {
					serverGetUrl = serverMapperCursor
							.getString(serverMapperCursor
									.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_GET_URL));
			//	}


				serverSchema = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_SCHEMA));
			}
			if ((!OMSConstants.USE_SERVER_MAPPER_GET_POST_URLS) || TextUtils.isEmpty(serverGetUrl) ) {
				url = serverUrl + OMSMessages.PATH_SEP_WIN.getValue()
						+ requestType + OMSMessages.PATH_SEP_WIN.getValue()
						+ serverSchema + OMSMessages.PATH_SEP_WIN.getValue()
						+ serverTableName + OMSMessages.PATH_SEP_WIN.getValue()
						+ modifiedDate;
			} else {
				if(OMSConstants.USE_GENERIC_URL){
					url=serverGetUrl+"?"+"modifieddate="+modifiedDate;
				}else{
					url = serverGetUrl + OMSMessages.PATH_SEP_WIN.getValue()
							+ modifiedDate;
			   }
			}
			
			serverMapperCursor.close();

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransURL for input parameter clientTableName["
							+ clientTableName + "],requestType[" + requestType
							+ "]. " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return url;
	}



	/**
	 * get server table name corresponding to the server table name
	 * 
	 * @param clientTableName
	 * @return String
	 */
	public String getServerTableName(String clientTableName) {
		String serverTableName = null;
		Cursor serverMapperCursor;
		try {
			serverMapperCursor = OMSDBManager
					.getConfigDB()
					.query(OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
							new String[] { OMSDatabaseConstants.SERVER_MAPPER_SERVERTABLENAME },
							OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME
									+ "='"
									+ clientTableName
									+ "'"
									+ " and "
									+ OMSDatabaseConstants.CONFIGDB_APPID
									+ " = '"
									+ OMSApplication.getInstance()
											.getAppId() + "' and isdelete = 0", null, null,
							null, null);
			if (serverMapperCursor.moveToFirst()) {
				serverTableName = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_SERVERTABLENAME));
			}
			serverMapperCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getServerTableName for input parameter serverTableName["
							+ clientTableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getServerTableName for input parameter serverTableName["
							+ clientTableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return serverTableName;
	}





	/**
	 * get client table name corresponding to the server table name for Console
	 * 
	 * @param serverTableName
	 * @return String
	 */
	public String getClientTableNameForConsoleDB(String serverTableName,
			int appId) {
		String clientTableName = null;
		Cursor serverMapperCursor;
		try {
			serverMapperCursor = OMSDBManager
					.getConfigDB()
					.query(OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
							new String[] { OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME },
							OMSDatabaseConstants.SERVER_MAPPER_SERVERTABLENAME
									+ "='" + serverTableName + "'" + " and "
									+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
									+ appId + "'", null, null, null, null);
			if (serverMapperCursor.moveToFirst()) {
				clientTableName = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME));
			}
			serverMapperCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getClientTableName for input parameter serverTableName["
							+ serverTableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getClientTableName for input parameter serverTableName["
							+ serverTableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return clientTableName;
	}

	/**
	 * update date in servermapper table after synchronizing the client database
	 * for Console
	 * 
	 * @param tableName
	 * @param maxTimeStamp
	 * @return int
	 */
	public int updateModifiedTimeStampForConsoleTransTable(String tableName,
			double maxTimeStamp, int appID) {
		int updatedRow = -1;
		try {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put(OMSDatabaseConstants.MODIFIED_DATE, maxTimeStamp);
			updatedRow = OMSDBManager.getConfigDB().update(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
					contentvalues,
					OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME + "= ? "
							+ " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appID + "'", new String[] { tableName });
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method updateModifiedTimeStampForTransTable for input parameter tableName["
							+ tableName
							+ "], maxTimeStamp["
							+ maxTimeStamp
							+ "]. " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return updatedRow;
	}

	/**
	 * update date in visiteddatemapper table after synchronizing the client
	 * database initial load
	 * 
	 *
	 * @param appID
	 * @param timeStamp
	 * @return int - number of inserted record
	 */
	public int updateModifiedTimeStampForVisitedDateMapper(String appID,
			double timeStamp) {
		int noOfRowsUpdated = 0;
		try {
			ContentValues cvalues = new ContentValues();
			cvalues.put(OMSDatabaseConstants.MODIFIED_DATE, timeStamp);
			cvalues.put(OMSDatabaseConstants.CONFIGDB_APPID, appID);
			cvalues.put(OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE, "0");

			// Trigger an update query
			noOfRowsUpdated = TransDatabaseUtil.update(
					OMSDatabaseConstants.VISITED_DATE_MAPPER_TABLE_NAME, cvalues,
					"appid = ? ", new String[]{appID});
			if (noOfRowsUpdated>0)
			{
			Log.d(TAG,
					"Record updated into visitedDateMapper table with appid : "
							+ appID + " and timestamp :" + timeStamp);
			}

			// If updated did not happen, trigger insert
			if (noOfRowsUpdated == 0) {
				int noOfRowsInserted = (int) TransDatabaseUtil.insert(
						OMSDatabaseConstants.VISITED_DATE_MAPPER_TABLE_NAME, null,
						cvalues);
				Log.d(TAG,
						"Record inserted into visitedDateMapper table with appid : "
								+ appID + " and timestamp :" + timeStamp);
				return noOfRowsInserted;
			}

		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method updateModifiedTimeStampForVisitedDateMapper for input parameter "
							+ "tableName["
							+ OMSDatabaseConstants.VISITED_DATE_MAPPER_TABLE_NAME
							+ "], "
							+ "appID["
							+ appID
							+ "], "
							+ "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method updateModifiedTimeStampForVisitedDateMapper for input parameter "
							+ "tableName["
							+ OMSDatabaseConstants.VISITED_DATE_MAPPER_TABLE_NAME
							+ "], "
							+ "appID["
							+ appID
							+ "], "
							+ "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method updateModifiedTimeStampForVisitedDateMapper for input parameter "
							+ "tableName["
							+ OMSDatabaseConstants.VISITED_DATE_MAPPER_TABLE_NAME
							+ "], "
							+ "appID["
							+ appID
							+ "], "
							+ "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return noOfRowsUpdated;
	}

	public String getLastModifiedTime(String tableName) {
		double modifiedDate = 0.0;
		Cursor serverMapperCursor = null;

		try {
			serverMapperCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
					null,
					OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME
							+ "='"
							+ tableName
							+ "'"
							+ " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '"
							+ OMSApplication.getInstance()
									.getAppId() + "'", null, null, null, null);

			if (serverMapperCursor.moveToFirst()) {
				modifiedDate = serverMapperCursor.getDouble(serverMapperCursor
						.getColumnIndex(OMSDatabaseConstants.MODIFIED_DATE));
			}
			serverMapperCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransURL for input parameter clientTableName["
							+ tableName + "]. " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return "" + modifiedDate;
	}
	
	
	
	public int getPaginationCurrentPage(String tableName) {
		int currentPage = 1;
		Cursor serverMapperCursor = null;
		try {
			serverMapperCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
					null,
					OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME
							+ "='"
							+ tableName
							+ "'"
							+ " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '"
							+ OMSApplication.getInstance()
									.getAppId() + "'", null, null, null, null);

			if (serverMapperCursor.moveToFirst()) {
				currentPage = serverMapperCursor.getInt(serverMapperCursor
						.getColumnIndex(OMSDatabaseConstants.CURRENT_PAGE));
			}
			
			if(currentPage < 1)
				currentPage = 1;
			serverMapperCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getPaginationCurrentPage for input parameter clientTableName["
							+ tableName + "]. " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return currentPage;
	}
	
	public void setPaginationCurrentPage(String tableName,int currentPage) {
		try{
			ContentValues cvalues = new ContentValues();
			cvalues.put(OMSDatabaseConstants.CURRENT_PAGE, currentPage);

			// Trigger an update query
			int 	noOfRowsUpdated = OMSDBManager.getConfigDB().update(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME, cvalues,
					OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME
					+ "='"
					+ tableName
					+ "'"
					+ " and "
					+ OMSDatabaseConstants.CONFIGDB_APPID
					+ " = '"
					+ OMSApplication.getInstance()
					.getAppId() + "'",null);
			if (noOfRowsUpdated>0){
				Log.d(TAG,"Record updated into SERVER_MAPPER_TABLE_NAME table with appid : "
								+ OMSApplication.getInstance()
								.getAppId() + " and CURRENT_PAGE :" + currentPage);
			}

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method setPaginationCurrentPage for input parameter clientTableName["
							+ tableName + "]. " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Fetches Trans Table column List
	 * 
	 * @param tableName
	 * @return Set<String>
	 */
	public List<String> getTransColumnSet(String tableName) {
		Cursor pragmaCursor = null;
		List<String> columnList = new ArrayList<String>();
		try {
			pragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
					"pragma table_info(" + tableName + ");", null);
			if (pragmaCursor.getCount() > 0) {
				if (pragmaCursor.moveToFirst()) {
					do {
						columnList
								.add(pragmaCursor.getString(pragmaCursor
										.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)));
					} while (pragmaCursor.moveToNext());
				}
			}
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getTransColumnSet for input parameter tableName["
							+ tableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransColumnSet for input parameter tableName["
							+ tableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return columnList;
	}

	
	
	public int updateModifiedTimeStampForAppsTable(
			double maxTimeStamp) {
		int updatedRow = -1;
		try {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put(OMSDatabaseConstants.CONFIG_LAST_MODIFIED_DATE, maxTimeStamp);
		/*	updatedRow = OMSDBManager.getConfigDB().update(
					OMSDatabaseConstants.APPS_TABLE_SCREEN_TABLE_NAME,
					contentvalues,
					      OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '"
							+ OMSApplication.getInstance()
									.getAppId() + "'",
					null);*/
			
			updatedRow = OMSDBManager.getConfigDB().update(
					OMSDatabaseConstants.APPS_TABLE_SCREEN_TABLE_NAME,
					contentvalues,
					      OMSDatabaseConstants.CONFIGDB_APPID
							+ " =?",
							new String[] { OMSApplication.getInstance()
							.getAppId() });
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method updateModifiedTimeStampForTransTable for input parameter tableName["
							+ OMSDatabaseConstants.APPS_TABLE_SCREEN_TABLE_NAME
							+ "], maxTimeStamp["
							+ maxTimeStamp
							+ "]. " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return updatedRow;
	}
	
	
	public HashMap<String,String> getWebserviceResponseData(String tableName) {
		HashMap<String,String> errorResponse = new HashMap<String,String>();
		Cursor serverMapperCursor = null;

		try {
			serverMapperCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
					null,
					OMSDatabaseConstants.SERVER_MAPPER_SERVERTABLENAME
							+ "='"
							+ tableName
							+ "'"
							+ " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '"
							+ OMSApplication.getInstance()
									.getAppId() + "'", null, null, null, null);

			if (serverMapperCursor.moveToFirst()) {
				String errorOnject = serverMapperCursor.getString(serverMapperCursor
						.getColumnIndex("errorobject"));
				if(!TextUtils.isEmpty(serverMapperCursor.getString(serverMapperCursor
						.getColumnIndex("errorobject")))){
					
					String tmp=errorOnject;
					errorResponse.put("errorobject", serverMapperCursor.getString(serverMapperCursor
						.getColumnIndex("errorobject")));
				}else{
					errorResponse.put("errorobject", "");
				}
				
				if(!TextUtils.isEmpty(serverMapperCursor.getString(serverMapperCursor
						.getColumnIndex("messagekey")))){
					errorResponse.put("messagekey", serverMapperCursor.getString(serverMapperCursor
						.getColumnIndex("messagekey")));
				}else{
					errorResponse.put("messagekey", "");
				}
				
				if(!TextUtils.isEmpty(serverMapperCursor.getString(serverMapperCursor
						.getColumnIndex("codekey")))){
					errorResponse.put("codekey", serverMapperCursor.getString(serverMapperCursor
						.getColumnIndex("codekey")));
				}else{
					errorResponse.put("codekey", "");
				}
			}
			serverMapperCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransURL for input parameter clientTableName["
							+ tableName + "]. " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return errorResponse;
	}
	
	


	
	
	/**
	 * generate trans url by passing client table name to fetch data from server
	 * 
	 * @param clientTableName
	 * @param
	 *            //get/post
	 * @return String
	 */
	public String getTransGetURL(String clientTableName) {
		String url = "";
		double modifiedDate = 0.0;
		String serverTableName = "";
		Cursor serverMapperCursor = null;
		String serverUrl = null;
		String serverSchema = null;
		String serverGetUrl = null;

		try {
			serverMapperCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
					null,
					OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME
					+ "='"
					+ clientTableName
					+ "'"
					+ " and "
					+ OMSDatabaseConstants.CONFIGDB_APPID
					+ " = '"
					+ OMSApplication.getInstance()
					.getAppId() + "' and isdelete = 0", null, null, null, null);

			if (serverMapperCursor.moveToFirst()) {
				modifiedDate = serverMapperCursor.getDouble(serverMapperCursor
						.getColumnIndex(OMSDatabaseConstants.MODIFIED_DATE));
				serverTableName = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_SERVERTABLENAME));
			
				/*	serverUrl = serverMapperCursor
					.getString(serverMapperCursor
							.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_URL));*/
			

					serverUrl = serverMapperCursor
					.getString(serverMapperCursor
							.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_GET_URL));
				serverSchema = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_SCHEMA));
			}

			
			serverMapperCursor.close();

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransURL for input parameter clientTableName["
							+ clientTableName + "] " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return serverUrl;
	}
	
	
	/**
	 * generate trans url by passing client table name to fetch data from server
	 * 
	 * @param clientTableName
	 * @param //requestType
	 *            get/post
	 * @return String
	 */
	public String getTransPostURL(String clientTableName) {
		String url = "";
		double modifiedDate = 0.0;
		String serverTableName = "";
		Cursor serverMapperCursor = null;
		String serverUrl = null;
		String serverSchema = null;
		String serverGetUrl = null;

		try {
			serverMapperCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.SERVER_MAPPER_TABLE_NAME,
					null,
					OMSDatabaseConstants.SERVER_MAPPER_CLIENTTABLE_NAME
					+ "='"
					+ clientTableName
					+ "'"
					+ " and "
					+ OMSDatabaseConstants.CONFIGDB_APPID
					+ " = '"
					+ OMSApplication.getInstance()
					.getAppId() + "' and isdelete = 0", null, null, null, null);

			if (serverMapperCursor.moveToFirst()) {
				modifiedDate = serverMapperCursor.getDouble(serverMapperCursor
						.getColumnIndex(OMSDatabaseConstants.MODIFIED_DATE));
				serverTableName = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_SERVERTABLENAME));
			
				/*	serverUrl = serverMapperCursor
					.getString(serverMapperCursor
							.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_URL));*/
			

					serverUrl = serverMapperCursor
					.getString(serverMapperCursor
							.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_POST_URL));
				serverSchema = serverMapperCursor
						.getString(serverMapperCursor
								.getColumnIndex(OMSDatabaseConstants.SERVER_MAPPER_SCHEMA));
			}

			
			serverMapperCursor.close();

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransURL for input parameter clientTableName["
							+ clientTableName + "] " + "Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return serverUrl;
	}
	
}
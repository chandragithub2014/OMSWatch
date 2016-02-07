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

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;


/**
 * @author 280779 ConfigTransDBParserHelper - this helper class is used to
 *         insert/update data into config db during initial loading
 */
public class OMSDBParserHelper {

	private static final String TRANS_DB = "TransDataBase";
	private static final String TRANSXML = "transDBXML";
	private final String TAG = this.getClass().getSimpleName();

	/**
	 * This method is used to insert/update data into configDB schema
	 * 
	 * @param tableName
	 * @param cvalues
	 * @param primaryKey
	 * @param primaryKeyVal
	 * @param timestampColName
	 * @param timeStamp
	 * @return int - number of inserted record
	 */
	public int insertOrUpdateConfigDB(String tableName, ContentValues cvalues,
			String primaryKey, String primaryKeyVal, String timestampColName,
			double timeStamp) {

		int insertedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {

			Cursor dbCursor = OMSDBManager.getConfigDB().query(
					tableName,
					null,
					primaryKey
							+ "='"
							+ primaryKeyVal
							+ "'"
							+ " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '"
							+ Integer.parseInt(OMSApplication
									.getInstance().getAppId()) + "'", null,
					null, null, null);
			if (dbCursor.moveToFirst()) {
				insertedId = OMSDBManager.getConfigDB().update(
						tableName,
						cvalues,
						primaryKey
								+ "= ? "
								+ " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID
								+ " = '"
								+ Integer.parseInt(OMSApplication
										.getInstance().getAppId()) + "'",
						new String[] { primaryKeyVal });
			} else {
				insertedId = (int) OMSDBManager.getConfigDB().insert(tableName,
						null, cvalues);
			}
			dbCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName[" + tableName + "], " + "primaryKey["
							+ primaryKey + "], " + "primaryKeyVal["
							+ primaryKeyVal + "], " + "timestampColName["
							+ timestampColName + "], " + "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName[" + tableName + "], " + "primaryKey["
							+ primaryKey + "], " + "primaryKeyVal["
							+ primaryKeyVal + "], " + "timestampColName["
							+ timestampColName + "], " + "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName[" + tableName + "], " + "primaryKey["
							+ primaryKey + "], " + "primaryKeyVal["
							+ primaryKeyVal + "], " + "timestampColName["
							+ timestampColName + "], " + "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return insertedId;
	}

	/**
	 * This method is used to delete the default rows with ModifiedData as 1.00
	 * 
	 * @param tableName
	 * @return int - number of default records deleted
	 */
	public int deleteAllDefaultRows(String tableName) {
		int returnValue = 0;
		try {
			int noOfRowsDeleted = TransDatabaseUtil.delete(tableName,
					OMSDatabaseConstants.MODIFIED_DATE,
					new String[]{OMSConstants.DEFAULT_MODIFIED_TIME});
			Log.d(TAG, "# of rows deleted from Table: " + tableName + " is "
					+ noOfRowsDeleted);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * This method is used to delete all rows from the table
	 * 
	 * @param tableName
	 * 
	 * @return int - number of record deleted
	 */
	public int deleteAllRows(String tableName) {
		int returnValue = 0;
		try {
			int noOfRowsDeleted = TransDatabaseUtil.delete(tableName,
					OMSConstants.DEFAULT_WHERE_CLAUSE_FOR_DELETE, null);
			Log.d(TAG, "# of rows deleted from Table: " + tableName + " is "
					+ noOfRowsDeleted);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * This method is used to insert/updare record in trans db schema during
	 * initial load
	 * 
	 * @param tableName
	 * @param cvalues
	 * @return int - number of inserted record
	 */
	public int insertOrUpdateTransDB(String tableName, ContentValues cvalues) {
		int returnValue = 0;
		int noOfRowsUpdated = -1;
		try {

			
			if (Integer.parseInt(OMSApplication.getInstance()
					.getAppId()) == 10) {
			// Perform an update first
			noOfRowsUpdated = TransDatabaseUtil.update(
					tableName,
					cvalues,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "= ?  and appid = ?",
					new String[] { cvalues
							.getAsString(OMSDatabaseConstants.UNIQUE_ROW_ID), OMSApplication
							.getInstance().getEditTextHiddenVal() });
			}else{
				// Perform an update first
				noOfRowsUpdated = TransDatabaseUtil.update(
						tableName,
						cvalues,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
						new String[] { cvalues
								.getAsString(OMSDatabaseConstants.UNIQUE_ROW_ID)});
			}

			if (noOfRowsUpdated == 0) {
				// No rows updated. So, perform an insert
				int noOfRowsInserted = (int) TransDatabaseUtil.insert(
						tableName, null, cvalues);
				returnValue = noOfRowsInserted;
			} else {
				returnValue = noOfRowsUpdated;
			}
			// Cursor dbCursor = DBManager.getTransDB().query(tableName, null,
			// primaryKey + "='" + primaryKeyVal + "'", null, null, null,
			// null);
			// if (dbCursor.moveToFirst()) {
			// insertedId = DBManager.getTransDB().update(tableName, cvalues,
			// primaryKey + "= ? ", new String[] { primaryKeyVal });
			// } else {
			// insertedId = (int) DBManager.getTransDB().insert(tableName,
			// null, cvalues);
			// }
			// dbCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter ");
			e.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * fetch maximum modified data from given table of transDB schema
	 * 
	 * @param tableName
	 * @param timestampColName
	 * @return double
	 */
	public double getMaxModifiedTimeStamp(String tableName,
			String timestampColName) {
		double maxVal = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {
			Cursor transCursor = TransDatabaseUtil.query(
					tableName,
					new String[] { "MAX(" + timestampColName + ")"
							+ " AS maxtimestamp " }, null, null, null, null,
					null);

			if (transCursor.moveToFirst()) {
				maxVal = transCursor.getDouble(0);
			}
			transCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getMaxModifiedTimeStamp for input parameter tableName["
							+ tableName
							+ "], timestampColName["
							+ timestampColName
							+ "]. "
							+ "Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method getMaxModifiedTimeStamp for input parameter tableName["
							+ tableName
							+ "], timestampColName["
							+ timestampColName
							+ "]. "
							+ "Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getMaxModifiedTimeStamp for input parameter tableName["
							+ tableName
							+ "], timestampColName["
							+ timestampColName
							+ "]. "
							+ "Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return maxVal;
	}

	/**
	 * this method returns transDBxml from configDB's Transdatabase table
	 * 
	 * @return String
	 */
	public String getTransXML(int appId) {
		String response = "";
		try {
			Cursor configCursor = OMSDBManager.getConfigDB().query(TRANS_DB,
					new String[] { TRANSXML },
					OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId + "'",
					null, null, null, null);
			if (configCursor.moveToFirst()) {
				response = configCursor.getString(configCursor
						.getColumnIndex(TRANSXML));
			}
			configCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getTransXML. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransXML. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method getTransXML. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * This method returns the version number for given appid
	 * 
	 * @param appId
	 * @return int
	 * 
	 */
	public int getVersionNumber(int appId) {
		int version = 0;
		try {
			Cursor configCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.TRANSDBVERSIONMAPPER_TABLE_NAME, null,
					OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId + "'",
					null, null, null, null);
			if (configCursor.moveToFirst()) {
				version = configCursor
						.getInt(configCursor
								.getColumnIndex(OMSDatabaseConstants.TRANSDBVERSIONMAPPER_VERSION));
			}
			configCursor.close();
		} catch (SQLException e) {
			Log.e(TAG, "Error occurred in method getVersionNumber. Error is:"
					+ e.getMessage());
			return version;
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Error occurred in method getVersionNumber. Error is:"
					+ e.getMessage());
			return version;
		} catch (IllegalStateException e) {
			Log.e(TAG, "Error occurred in method getVersionNumber. Error is:"
					+ e.getMessage());
			return version;
		}
		return version;
	}

	/**
	 * This method inserts new or updates version number of TransDB
	 * 
	 * @param versionnumber
	 * @param appId
	 * @return
	 */
	public int insertOrUpdateVersionNumber(int versionnumber, int appId) {
		int insertedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Cursor versionCursor = null;
		try {
			versionCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.TRANSDBVERSIONMAPPER_TABLE_NAME, null,
					OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId + "'",
					null, null, null, null);
			if (versionCursor.moveToFirst()) {
				// update
				ContentValues contentValues = new ContentValues();
				contentValues.put(
						OMSDatabaseConstants.TRANSDBVERSIONMAPPER_VERSION,
						versionnumber);
				insertedId = OMSDBManager.getConfigDB().update(
						OMSDatabaseConstants.TRANSDBVERSIONMAPPER_TABLE_NAME,
						contentValues,
						OMSDatabaseConstants.CONFIGDB_APPID + "= ? ",
						new String[] { Integer.toString(appId) });

			} else {
				// insert
				ContentValues contentValues = new ContentValues();
				contentValues.put(
						OMSDatabaseConstants.TRANSDBVERSIONMAPPER_VERSION,
						versionnumber);
				contentValues.put(OMSDatabaseConstants.CONFIGDB_APPID, appId);
				insertedId = (int) OMSDBManager.getConfigDB().insert(
						OMSDatabaseConstants.TRANSDBVERSIONMAPPER_TABLE_NAME,
						null, contentValues);

			}

			versionCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateVersionNumber. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateVersionNumber. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateVersionNumber. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return insertedId;
	}

	/**
	 * This method returns the modified of corresponding appid from apps table
	 * of ConfigDB
	 * 
	 * @param appId
	 * @return
	 */
	public double getLastModifiedConfigDateFromAppsTable(int appId) {
		double modifiedDate = 0.0;
		Cursor appIdModifiedDateCursor = null;
		try {
			appIdModifiedDateCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.APPS_TABLE_SCREEN_TABLE_NAME, null,
					OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId + "'",
					null, null, null, null);
			if (appIdModifiedDateCursor.moveToFirst()) {
				modifiedDate = appIdModifiedDateCursor
						.getDouble(appIdModifiedDateCursor
								.getColumnIndex(OMSDatabaseConstants.CONFIG_LAST_MODIFIED_DATE));
			} else {
				modifiedDate = 0.0;
			}
			appIdModifiedDateCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getModifiedDateFromAppsTable. Error is:"
							+ e.getMessage());
			return modifiedDate;
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getModifiedDateFromAppsTable. Error is:"
							+ e.getMessage());
			return modifiedDate;
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method getModifiedDateFromAppsTable. Error is:"
							+ e.getMessage());
			return modifiedDate;
		}
		return modifiedDate;
	}

	/**
	 * This method is used to insert/updare record in policy db schema during
	 * initial load
	 * 
	 * @param tableName
	 * @return int - number of inserted record
	 */
	/*public int insertOrUpdatePolicyDB(String tableName, ContentValues cvalues,
			String primaryKey, String primaryKeyVal, String timestampColName,
			double timeStamp) {
		int insertedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {
			if(AppSecurityAndPerformanceHelper.getPolicyDB()!=null){
				Cursor dbCursor = AppSecurityAndPerformanceHelper.getPolicyDB()
						.query(tableName, null,
								primaryKey + "='" + primaryKeyVal + "'", null,
								null, null, null);
				if (dbCursor.moveToFirst()) {
					insertedId = AppSecurityAndPerformanceHelper.getPolicyDB()
							.update(tableName, cvalues, primaryKey + "= ? ",
									new String[] { primaryKeyVal });
				} else {
					insertedId = (int) AppSecurityAndPerformanceHelper
							.getPolicyDB().insert(tableName, null, cvalues);
				}
				dbCursor.close();
			}else{
				Log.w(TAG, "PolicyDB is null. App Guard/Monitor might not be configured");
			}
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter "
							+ "tableName[" + tableName + "], " + "primaryKey["
							+ primaryKey + "], " + "primaryKeyVal["
							+ primaryKeyVal + "], " + "timestampColName["
							+ timestampColName + "], " + "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter "
							+ "tableName[" + tableName + "], " + "primaryKey["
							+ primaryKey + "], " + "primaryKeyVal["
							+ primaryKeyVal + "], " + "timestampColName["
							+ timestampColName + "], " + "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateTransDB for input parameter "
							+ "tableName[" + tableName + "], " + "primaryKey["
							+ primaryKey + "], " + "primaryKeyVal["
							+ primaryKeyVal + "], " + "timestampColName["
							+ timestampColName + "], " + "timeStamp["
							+ timeStamp + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return insertedId;
	}*/

	// bulk
	public void insertOrUpdateConfigDBBulk(String tableName,
			List<ContentValues> cvList) {
		int i = 0;
		String appId = OMSApplication.getInstance().getAppId();
		try {
			OMSDBManager.getConfigDB().beginTransaction();
			for (i = 0; i < cvList.size(); i++) {

				// Trigger an update first
				int noOfRowsUpdated = OMSDBManager.getConfigDB().update(
						tableName,
						cvList.get(i),
						OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? " + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
								+ appId + "'",
						new String[] { cvList.get(i).getAsString(
								OMSDatabaseConstants.UNIQUE_ROW_ID) });

				if (noOfRowsUpdated == 0) {
					// No rows updated. So, Trigger an insert
					OMSDBManager.getConfigDB().insert(tableName, null,
							cvList.get(i));
				}

			}
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} finally {
			// End the transaction
			OMSDBManager.getConfigDB().setTransactionSuccessful();
			OMSDBManager.getConfigDB().endTransaction();
		}
	}

	public String createInsertStatement(String tableName, ContentValues cv,
			List<String> columnNames) {
		final StringBuilder s = new StringBuilder();
		/*
		 * for (String key : cv.keySet()) { String myKey = key; String myValue
		 * =(String) cv.get(key);
		 * 
		 * 
		 * }
		 */
		s.append("INSERT INTO ").append(tableName).append(" (");
		for (int i = 0; i < columnNames.size(); i++) {
			s.append(columnNames.get(i)).append(" ,");
		}
		int length = s.length();
		s.delete(length - 2, length);

		for (int i = 0; i < columnNames.size(); i++) {
			s.append(" ? ,");
		}
		length = s.length();
		s.delete(length - 2, length);
		s.append(")");
		return s.toString();
	}
	// bulk

	public String getUsidFromUserid(String tableName,String userId,String userIdColumn){
		String usid="";
		try {

			Cursor dbCursor = TransDatabaseUtil.query(
					tableName,
					null,
					userIdColumn
							+ "='"
							+ userId
							+ "'"
							, null,
					null, null, null);
			if(dbCursor.moveToFirst()){
				usid  = 	dbCursor.getString(dbCursor.getColumnIndex("usid"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return usid;
	}
	
	
	// bulk
	public void insertOrUpdateTransDBDBBulk(String tableName,
			List<ContentValues> cvList) {
		int i = 0;
		String appId = OMSApplication.getInstance().getAppId();
		try {
			TransDatabaseUtil.getTransDB().beginTransaction();
			for (i = 0; i < cvList.size(); i++) {
				
				// Trigger an update first
					int noOfRowsUpdated = 0;
				if (Integer.parseInt(OMSApplication.getInstance()
						.getAppId()) == 10) {
				 noOfRowsUpdated = TransDatabaseUtil.getTransDB().update(
						tableName,
						cvList.get(i),
						OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? " + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = ?",
						new String[] { cvList.get(i).getAsString(
								OMSDatabaseConstants.UNIQUE_ROW_ID),
								 OMSApplication
								.getInstance().getEditTextHiddenVal()});
				}
				else{
					 noOfRowsUpdated = TransDatabaseUtil.getTransDB().update(
							tableName,
							cvList.get(i),
							OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
							new String[] { cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) });
				}
				if (noOfRowsUpdated == 0) {
					// No rows updated. So, Trigger an insert
					TransDatabaseUtil.getTransDB().insert(tableName, null,
							cvList.get(i));
				}

			}
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} finally {
			// End the transaction
			TransDatabaseUtil.getTransDB().setTransactionSuccessful();
			TransDatabaseUtil.getTransDB().endTransaction();
		}
	}
	
	
	public void insertOrUpdateTransDBDBBulk(String tableName,
			List<ContentValues> cvList,String modifiedDate) {
		int i = 0;
		String appId = OMSApplication.getInstance().getAppId();
		try {
			TransDatabaseUtil.getTransDB().beginTransaction();
			for (i = 0; i < cvList.size(); i++) {
				
				// Trigger an update first
					int noOfRowsUpdated = 0;
		/*if(modifiedDate.equalsIgnoreCase("0.0") && Integer.parseInt(OMSConstants.APP_ID )> 0){
				TransDatabaseUtil.getTransDB().insert(tableName, null,
						cvList.get(i));
			}else*/{
				if (Integer.parseInt(OMSApplication.getInstance()
						.getAppId()) == 10) {
				 noOfRowsUpdated = TransDatabaseUtil.getTransDB().update(
						tableName,
						cvList.get(i),
						OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? " + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = ?",
						new String[] { cvList.get(i).getAsString(
								OMSDatabaseConstants.UNIQUE_ROW_ID),
							     OMSApplication
								.getInstance().getEditTextHiddenVal()});
				}
				else{
					 noOfRowsUpdated = TransDatabaseUtil.getTransDB().update(
							tableName,
							cvList.get(i),
							OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
							new String[] { cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) });
				}
				if (noOfRowsUpdated == 0) {
					// No rows updated. So, Trigger an insert
					TransDatabaseUtil.getTransDB().insert(tableName, null,
							cvList.get(i));
				}
			}	

			}
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateConfigDB for input parameter "
							+ "tableName["
							+ tableName
							+ "], "
							+ "primaryKey["
							+ OMSDatabaseConstants.UNIQUE_ROW_ID
							+ "], "
							+ "primaryKeyVal["
							+ cvList.get(i).getAsString(
									OMSDatabaseConstants.UNIQUE_ROW_ID) + "], "
							+ "timestampColName[" + "" + "], " + "timeStamp["
							+ "" + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} finally {
			// End the transaction
			TransDatabaseUtil.getTransDB().setTransactionSuccessful();
			TransDatabaseUtil.getTransDB().endTransaction();
		}
	}
}

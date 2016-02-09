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
package watch.oms.omswatch.actioncenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import watch.oms.omswatch.R;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.helpers.ActionCenterHelper;
import watch.oms.omswatch.constants.ConsoleDBConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSJulianDateGenerator;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;


public class CustomAction {

	private String TAG = this.getClass().getSimpleName();
	private Activity context;
	private OMSReceiveListener rListener;
	private int configDBAppId;
	private int methodNumber = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private static final String TABLENAME = "personaapp";
	private static final String TEMP_TABLE_NAME = "temptable";
	private static final String POLICYMASTER_TABLENAME = "policymaster";
	private static final String PERSONAMASTER_TABLENAME = "personamaster";
	private static final String DB = "10";
	private String actionUsid = null;
	private ActionCenterHelper actionCenterHelper = null;
	private List<String> transUsidList = null;
	private String filterColumnName;
	private String filterColumnValue;

	public CustomAction(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, int appId) {
		context = FragmentContext;
		rListener = receiveListener;
		configDBAppId = appId;
		actionCenterHelper = new ActionCenterHelper(context);
	}

	public void setCustomData(JSONObject jsInputJsonObject, int methodId,
			String actionUsid, int configDBAppId, List<String> transUsidList,
			String filterColumnName, String filterColumnValue) {
		methodNumber = methodId;
		this.actionUsid = actionUsid;
		this.configDBAppId = configDBAppId;
		this.transUsidList = transUsidList;
		this.filterColumnName = filterColumnName;
		this.filterColumnValue = filterColumnValue;
		switch (methodNumber) {
		case 1:
			customActionMethod1();
			break;
		case 2:
			customActionMethod2();
			break;
		case 3:
			customActionMethod3();
			break;
		case 4:
			customActionMethod4();
			break;
		case 5:
			customActionMethod5();
			break;
		case 6:
			customActionMethod6();
			break;
		case 7:
			customActionMethod7();
			break;
		case 8:
			customActionMethod8();
			break;
		case 9:
			customActionMethod9();
			break;
		case 11:
			customActionMethod11();
			break;
		case 12:
			customActionMethod12();
			break;
		}
	}

	public void customActionMethod1() {
		// Code for Action
		Cursor screenAppidCursor, customActionCursor = null;
		int appid = 0;
		String customActionWhere = null;
		try {
			// get appids using wherecluase
			screenAppidCursor = TransDatabaseUtil.query(
					TABLENAME,
					new String[]{OMSDatabaseConstants.CONFIGDB_APPID},
					filterColumnName + " = " + "'" + filterColumnValue + "'"
							+ "or " + OMSDatabaseConstants.TRANS_PERSONA_ID
							+ " = '0'", null, null, null, null);

			// make where clause to get appids and usids
			if (screenAppidCursor.moveToFirst()) {
				do {
					if (screenAppidCursor
							.getInt(screenAppidCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID)) != 0) {
						appid = screenAppidCursor
								.getInt(screenAppidCursor
										.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
					}
					if (customActionWhere == null) {
						customActionWhere = "appid != " + appid;
					} else {
						customActionWhere = customActionWhere + " and "
								+ "appid != " + appid;
					}
				} while (screenAppidCursor.moveToNext());
				customActionCursor = TransDatabaseUtil.query(
						ConsoleDBConstants.APPMASTER_TABLE_NAME,
						new String[] { OMSDatabaseConstants.CONFIGDB_APPID,
								OMSDatabaseConstants.UNIQUE_ROW_ID,
								OMSDatabaseConstants.TRANS_IMAGE_URL },
						customActionWhere, null, null, null, null);
			}

			// get all appids
			else {
				customActionCursor = TransDatabaseUtil.query(
						ConsoleDBConstants.APPMASTER_TABLE_NAME,
						new String[] { OMSDatabaseConstants.CONFIGDB_APPID,
								OMSDatabaseConstants.UNIQUE_ROW_ID,
								OMSDatabaseConstants.TRANS_IMAGE_URL }, null,
						null, null, null, null);
			}

			TransDatabaseUtil.delete(TEMP_TABLE_NAME, null, null);

			// insert all appids and usids into temptable
			if (customActionCursor.moveToFirst()) {
				do {
					String usid = null;
					String imageUrl = null;
					if (customActionCursor
							.getString(customActionCursor
									.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID)) != null) {
						usid = customActionCursor
								.getString(customActionCursor
										.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));
					}
					if (customActionCursor
							.getInt(customActionCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID)) != 0) {
						appid = customActionCursor
								.getInt(customActionCursor
										.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
					}
					if (customActionCursor
							.getString(customActionCursor
									.getColumnIndex(OMSDatabaseConstants.TRANS_IMAGE_URL)) != null) {
						imageUrl = customActionCursor
								.getString(customActionCursor
										.getColumnIndex(OMSDatabaseConstants.TRANS_IMAGE_URL));
					}
					ContentValues contentValues = new ContentValues();
					contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID, usid);
					contentValues.put(OMSDatabaseConstants.CONFIGDB_APPID,
							appid);
					contentValues.put(OMSDatabaseConstants.TRANS_IMAGE_URL,
							imageUrl);
					contentValues.put(OMSDatabaseConstants.TRANS_PERSONA_ID,
							filterColumnValue);

					TransDatabaseUtil.insert(TEMP_TABLE_NAME, null,
							contentValues);
				} while (customActionCursor.moveToNext());
			}

			ContentValues contentValues = new ContentValues();
			contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
					OMSDatabaseConstants.ACTION_STATUS_FINISHED);
			actionCenterHelper.insertOrUpdateActionQueueByActionusid(
					contentValues, actionUsid, configDBAppId);

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method method1 customaction. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}

		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	public void customActionMethod2() {
		// Code for Action
		Cursor deviceDetailsCursor = null;
		try {
			if (!filterColumnValue
					.equalsIgnoreCase(OMSDatabaseConstants.DEFAULT_PERSONA_ID)) {
				deviceDetailsCursor = TransDatabaseUtil.query(
						ConsoleDBConstants.DEVICEDETAILS_TABLE_NAME,
						new String[] { OMSDatabaseConstants.TRANS_DEVICE_ID,
								OMSDatabaseConstants.UNIQUE_ROW_ID,
								OMSDatabaseConstants.TRANS_IMAGE_URL },
						filterColumnName + " != " + "'" + filterColumnValue
								+ "'", null, null, null, null);
			}

			TransDatabaseUtil.delete(TEMP_TABLE_NAME, null, null);

			// insert all appids and usids into temptable
			if (deviceDetailsCursor.moveToFirst()) {
				do {
					String usid = null;
					String deviceid = null;
					String imageUrl = null;
					if (deviceDetailsCursor
							.getString(deviceDetailsCursor
									.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID)) != null) {
						usid = deviceDetailsCursor
								.getString(deviceDetailsCursor
										.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));
					}
					if (deviceDetailsCursor
							.getString(deviceDetailsCursor
									.getColumnIndex(OMSDatabaseConstants.TRANS_DEVICE_ID)) != null) {
						deviceid = deviceDetailsCursor
								.getString(deviceDetailsCursor
										.getColumnIndex(OMSDatabaseConstants.TRANS_DEVICE_ID));
					}
					if (deviceDetailsCursor
							.getString(deviceDetailsCursor
									.getColumnIndex(OMSDatabaseConstants.TRANS_IMAGE_URL)) != null) {
						imageUrl = deviceDetailsCursor
								.getString(deviceDetailsCursor
										.getColumnIndex(OMSDatabaseConstants.TRANS_IMAGE_URL));
					}
					ContentValues contentValues = new ContentValues();
					contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID, usid);
					contentValues.put(OMSDatabaseConstants.TRANS_DEVICE_ID,
							deviceid);
					contentValues.put(OMSDatabaseConstants.TRANS_IMAGE_URL,
							imageUrl);
					contentValues.put(OMSDatabaseConstants.TRANS_PERSONA_ID,
							filterColumnValue);

					TransDatabaseUtil.insert(TEMP_TABLE_NAME, null,
							contentValues);
				} while (deviceDetailsCursor.moveToNext());
			}
			ContentValues contentValues = new ContentValues();
			contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
					OMSDatabaseConstants.ACTION_STATUS_FINISHED);
			actionCenterHelper.insertOrUpdateActionQueueByActionusid(
					contentValues, actionUsid, configDBAppId);

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method method2 customaction. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	public void customActionMethod3() {
		// Code for Action
		Cursor appDetailsCursor = null;
		String personaid = null;
		Calendar cal;
		int appid = 0;
		try {
			if (transUsidList.get(0) != null) {
				appDetailsCursor = TransDatabaseUtil.query(
						TEMP_TABLE_NAME,
						new String[] { OMSDatabaseConstants.CONFIGDB_APPID,
								OMSDatabaseConstants.TRANS_PERSONA_ID,
								OMSDatabaseConstants.TRANS_IMAGE_URL },
						OMSDatabaseConstants.UNIQUE_ROW_ID + " = '"
								+ transUsidList.get(0) + "'", null, null, null,
						null);

				if (appDetailsCursor.moveToFirst()) {
					do {
						String imageUrl = null;
						if (appDetailsCursor
								.getInt(appDetailsCursor
										.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID)) != 0) {
							appid = appDetailsCursor
									.getInt(appDetailsCursor
											.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
						}
						if (appDetailsCursor
								.getString(appDetailsCursor
										.getColumnIndex(OMSDatabaseConstants.TRANS_PERSONA_ID)) != null) {
							personaid = appDetailsCursor
									.getString(appDetailsCursor
											.getColumnIndex(OMSDatabaseConstants.TRANS_PERSONA_ID));
						}
						if (appDetailsCursor
								.getString(appDetailsCursor
										.getColumnIndex(OMSDatabaseConstants.TRANS_IMAGE_URL)) != null) {
							imageUrl = appDetailsCursor
									.getString(appDetailsCursor
											.getColumnIndex(OMSDatabaseConstants.TRANS_IMAGE_URL));
						}
						cal = Calendar.getInstance();
						ContentValues contentValues = new ContentValues();
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(cal.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.CONFIGDB_APPID,
								appid);
						contentValues.put(
								OMSDatabaseConstants.TRANS_PERSONA_ID,
								personaid);
						contentValues.put(OMSDatabaseConstants.TRANS_IMAGE_URL,
								imageUrl);
						contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
								Long.toString(cal.getTimeInMillis()));

						if (personaid
								.equalsIgnoreCase(OMSDatabaseConstants.DEFAULT_PERSONA_ID)) {
							TransDatabaseUtil.delete(
									TABLENAME,
									OMSDatabaseConstants.CONFIGDB_APPID + " = "
											+ appid, null);
						}
						TransDatabaseUtil.insert(TABLENAME, null,
								contentValues);
					} while (appDetailsCursor.moveToNext());
				}
			}

			ContentValues contentValues = new ContentValues();
			contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
					OMSDatabaseConstants.ACTION_STATUS_FINISHED);
			actionCenterHelper.insertOrUpdateActionQueueByActionusid(
					contentValues, actionUsid, configDBAppId);

		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method method3 customaction. Error is:"
							+ e.getMessage());
		}
		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	public void customActionMethod4() {
		// Code for Action
		Cursor deviceDetailsCursor = null;
		String personaid = null;
		String deviceid = null;
		try {
			if (transUsidList.get(0) != null) {
				deviceDetailsCursor = TransDatabaseUtil.query(
						TEMP_TABLE_NAME,
						new String[] { OMSDatabaseConstants.TRANS_DEVICE_ID,
								OMSDatabaseConstants.TRANS_PERSONA_ID },
						OMSDatabaseConstants.UNIQUE_ROW_ID + " = '"
								+ transUsidList.get(0) + "'", null, null, null,
						null);

				if (deviceDetailsCursor.moveToFirst()) {
					do {
						if (deviceDetailsCursor
								.getString(deviceDetailsCursor
										.getColumnIndex(OMSDatabaseConstants.TRANS_DEVICE_ID)) != null) {
							deviceid = deviceDetailsCursor
									.getString(deviceDetailsCursor
											.getColumnIndex(OMSDatabaseConstants.TRANS_DEVICE_ID));
						}
						if (deviceDetailsCursor
								.getString(deviceDetailsCursor
										.getColumnIndex(OMSDatabaseConstants.TRANS_PERSONA_ID)) != null) {
							personaid = deviceDetailsCursor
									.getString(deviceDetailsCursor
											.getColumnIndex(OMSDatabaseConstants.TRANS_PERSONA_ID));
						}
						ContentValues contentValues = new ContentValues();
						contentValues.put(
								OMSDatabaseConstants.TRANS_PERSONA_ID,
								personaid);
						TransDatabaseUtil.update(
								ConsoleDBConstants.DEVICEDETAILS_TABLE_NAME,
								contentValues,
								OMSDatabaseConstants.TRANS_DEVICE_ID + "=?",
								new String[] { deviceid });
					} while (deviceDetailsCursor.moveToNext());
				}
			}

			ContentValues contentValues = new ContentValues();
			contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
					OMSDatabaseConstants.ACTION_STATUS_FINISHED);
			actionCenterHelper.insertOrUpdateActionQueueByActionusid(
					contentValues, actionUsid, configDBAppId);

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method method4 customaction. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	public void customActionMethod5() {
		// Code for Action
		long rowid = -1;
		ContentValues contentValues = null;
		Calendar cal = null;
		try {
			if (transUsidList.get(0) != null) {
				String transusid = transUsidList.get(0);
				contentValues = new ContentValues();
				contentValues.put(OMSDatabaseConstants.POLICY_MASTER_POLICYID,
						transusid);
				cal = Calendar.getInstance();
				contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
						Long.toString(cal.getTimeInMillis()));
				rowid = TransDatabaseUtil.update(
						POLICYMASTER_TABLENAME, contentValues,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "=?",
						new String[] { transusid });

				if (rowid > 0) {
					try {
						contentValues.clear();
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES1_APPUSAGE_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES1_CONNECTION_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES1_APPMETADATA_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES1_DEVICEMETADATA_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues.put(
								OMSDatabaseConstants.POLICY_FEATURES1_POLICYID,
								transusid);
						cal = Calendar.getInstance();
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(cal.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
								Long.toString(cal.getTimeInMillis()));
						TransDatabaseUtil
								.insert(OMSDatabaseConstants.POLICY_FEATURES1_TABLE_NAME,
										null, contentValues);

						contentValues.clear();
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES2_APPLAUNCH_TIME_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES2_ERROR_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES2_EXCEPTION_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES2_UI_RENDER_TIME_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES2_NETWORK_LATENCY_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES2_WARNING_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES2_INFO_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues.put(
								OMSDatabaseConstants.POLICY_FEATURES2_POLICYID,
								transusid);
						cal = Calendar.getInstance();
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(cal.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
								Long.toString(cal.getTimeInMillis()));
						TransDatabaseUtil
								.insert(OMSDatabaseConstants.POLICY_FEATURES2_TABLE_NAME,
										null, contentValues);

						contentValues.clear();
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_APPLOGGING,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_AUTHENTICATION,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_DATA_AT_REST_ENCRYPTION,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_DATA_ENCRYPTION_KEY,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_DB_DATA_ENCRYPTION,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_FILE_SHARING_ACCESS,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_HIDE_SCREEN,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_SECURES_PASTE_BOARD,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_SQL_INJECTION_CHECK,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES3_VALIDATE_SQL_INJECTION,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues.put(
								OMSDatabaseConstants.POLICY_FEATURES3_POLICYID,
								transusid);
						cal = Calendar.getInstance();
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(cal.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
								Long.toString(cal.getTimeInMillis()));
						TransDatabaseUtil
								.insert(OMSDatabaseConstants.POLICY_FEATURES3_TABLE_NAME,
										null, contentValues);

						contentValues.clear();
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES4_BATTERY_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES4_CPU_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES4_DISK_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES4_MEMORY_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.POLICY_FEATURES4_NETWORK_MONITOR,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues.put(
								OMSDatabaseConstants.POLICY_FEATURES4_POLICYID,
								transusid);
						cal = Calendar.getInstance();
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(cal.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
								Long.toString(cal.getTimeInMillis()));
						TransDatabaseUtil
								.insert(OMSDatabaseConstants.POLICY_FEATURES4_TABLE_NAME,
										null, contentValues);

						contentValues.clear();
						contentValues
								.put(OMSDatabaseConstants.GEO_FENCING_GEOFENCING_STATUS,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues.put(
								OMSDatabaseConstants.GEO_FENCING_POLICYID,
								transusid);
						cal = Calendar.getInstance();
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(cal.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
								Long.toString(cal.getTimeInMillis()));
						TransDatabaseUtil.insert(
								OMSDatabaseConstants.GEO_FENCING_TABLE_NAME,
								null, contentValues);

						contentValues.clear();
						contentValues
								.put(OMSDatabaseConstants.NETWORK_PROTECT_MULTITHREAD_CLIENT,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.NETWORK_PROTECT_NETWORK_PROTECT_STATUS,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues.put(
								OMSDatabaseConstants.NETWORK_PROTECT_POLICYID,
								transusid);
						cal = Calendar.getInstance();
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(cal.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
								Long.toString(cal.getTimeInMillis()));
						TransDatabaseUtil
								.insert(OMSDatabaseConstants.NETWORK_PROTECT_TABLE_NAME,
										null, contentValues);

						contentValues.clear();
						contentValues.put(
								OMSDatabaseConstants.TIME_FENCING_IDLE_STATUS,
								OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.TIME_FENCING_TIME_FENCING_STATUS,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues.put(
								OMSDatabaseConstants.TIME_FENCING_USAGE_STATUS,
								OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues
								.put(OMSDatabaseConstants.TIME_FENCING_TIME_FRAME_STATUS,
										OMSDatabaseConstants.SWITCH_OFF_INT_VALUE);
						contentValues.put(
								OMSDatabaseConstants.TIME_FENCING_POLICYID,
								transusid);
						cal = Calendar.getInstance();
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(cal.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
								Long.toString(cal.getTimeInMillis()));
						TransDatabaseUtil.insert(
								OMSDatabaseConstants.TIME_FENCING_TABLE_NAME,
								null, contentValues);

					} catch (IllegalArgumentException e) {
						Log.e(TAG,
								"Error occurred in method method5 customaction. Error is:"
										+ e.getMessage());

					} catch (SQLException e) {
						Log.e(TAG,
								"Error occurred in method method5 customaction. Error is:"
										+ e.getMessage());

					}
				}
			}

			contentValues.clear();
			contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
					OMSDatabaseConstants.ACTION_STATUS_FINISHED);
			actionCenterHelper.insertOrUpdateActionQueueByActionusid(
					contentValues, actionUsid, configDBAppId);

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method method5 customaction. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	public void customActionMethod6() {
		// Code for Action
		ContentValues contentValues = null;
		Calendar cal = null;
		try {
			if (transUsidList.get(0) != null) {
				String transusid = transUsidList.get(0);
				contentValues = new ContentValues();
				contentValues.put(OMSDatabaseConstants.TRANS_PERSONA_ID,
						transusid);
				cal = Calendar.getInstance();
				contentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
						Long.toString(cal.getTimeInMillis()));
				TransDatabaseUtil.update(PERSONAMASTER_TABLENAME,
						contentValues,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "=?",
						new String[] { transusid });
			}
			contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
					OMSDatabaseConstants.ACTION_STATUS_FINISHED);
			actionCenterHelper.insertOrUpdateActionQueueByActionusid(
					contentValues, actionUsid, configDBAppId);

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method method6 customaction. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	public void customActionMethod7() {
		// Code for Action
		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	public void customActionMethod8() {
		// Code for Action
		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	public void customActionMethod9() {
		// Code for Action
		String uniqueId = null;
		String blName = null;
		String blActionusid = null;
		String blCompleteUsid = null;
		String getActionUsid = null;
		String postActionUsid = null;
		String mailActionUsid = null;
		String callActionUsid = null;
		String smsActionUsid = null;
		String customActionUsid = null;
		String socialIntegrationActionUsid = null;
		int isBLDelete = 1;
		int isGetDelete = 0;
		int isPostDelete = 0;
		int isMailDelete = 0;
		int isCallDelete = 0;
		int isSmsDelete = 0;
		int isSocialDelete = 0;
		int isCustomDelete = 0;

		for (int k = 0; k < transUsidList.size(); k++) {
			uniqueId = transUsidList.get(k);
			Cursor screenDesignActionCursor = null;
			screenDesignActionCursor = OMSDBManager.getSpecificDB(DB).query(
					OMSDatabaseConstants.SCREEN_DESIGN_ACTION, null,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "='" + uniqueId + "'",
					null, null, null, null);
			if (screenDesignActionCursor.moveToFirst()) {
				blName = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_BL_NAME));
				blActionusid = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_BL_ACTION_USID));

				isBLDelete = screenDesignActionCursor
						.getInt(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_BL_IS_DELETE));

				getActionUsid = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_GET_ACTION_USID));
				isGetDelete = screenDesignActionCursor
						.getInt(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_GET_IS_DELETE));
				postActionUsid = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_POST_ACTION_USID));
				isPostDelete = screenDesignActionCursor
						.getInt(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_POST_IS_DELETE));

				mailActionUsid = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_MAIL_ACTION_USID));
				isMailDelete = screenDesignActionCursor
						.getInt(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_MAIL_IS_DELETE));

				callActionUsid = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_CALL_ACTION_USID));
				isCallDelete = screenDesignActionCursor
						.getInt(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_CALL_IS_DELETE));

				smsActionUsid = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_SMS_ACTION_USID));
				isSmsDelete = screenDesignActionCursor
						.getInt(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_SMS_IS_DELETE));

				customActionUsid = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_CUSTOM_ACTION_USID));
				isCustomDelete = screenDesignActionCursor
						.getInt(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_CUSTOM_IS_DELETE));

				socialIntegrationActionUsid = screenDesignActionCursor
						.getString(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_SOCIAL_INTEGRATION_ACTION_USID));
				isSocialDelete = screenDesignActionCursor
						.getInt(screenDesignActionCursor
								.getColumnIndex(OMSDatabaseConstants.SCREE_DESIGN_SOCIAL_INTEGRATION_IS_DELETE));

			}
			screenDesignActionCursor.close();
			if (blName != null && blName.length() > 0) {
				Cursor blCompleteCursor = null;
				blCompleteCursor = OMSDBManager.getSpecificDB(DB).query(
						OMSDatabaseConstants.BL_COMPLETE,
						null,
						OMSDatabaseConstants.BL_COMPLETE_BL_NAME + "='"
								+ blName + "'", null, null, null, null);
				if (blCompleteCursor.moveToFirst()) {
					blCompleteUsid = blCompleteCursor
							.getString(blCompleteCursor
									.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));
				}
			}

			if (!TextUtils.isEmpty(blActionusid)) {
				Cursor actionCursor = null;
				actionCursor = OMSDBManager.getSpecificDB(DB).query(
						OMSDatabaseConstants.ACTION_TABLE_NAME,
						null,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "='"
								+ blActionusid + "'", null, null, null, null);
				if (actionCursor.moveToFirst()) {
					ContentValues contentValues = new ContentValues();
					if (!TextUtils.isEmpty(blCompleteUsid)) {
						contentValues.put(
								OMSDatabaseConstants.ACTION_TABLE_ACTION_ID,
								blCompleteUsid);
					}

					isBLDelete = isBLDelete == 0 ? 1 : 0;
					contentValues.put(OMSDatabaseConstants.IS_DELETE,
							isBLDelete);
					int updatedID = OMSDBManager.getSpecificDB(DB).update(
							OMSDatabaseConstants.ACTION_TABLE_NAME,
							contentValues,
							OMSDatabaseConstants.UNIQUE_ROW_ID + "=?",
							new String[] { blActionusid });
					if (updatedID != -1) {
						Toast.makeText(
								context,
								context.getResources().getString(
										R.string.update_success),
								Toast.LENGTH_LONG).show();
					}
				}
				actionCursor.close();
			}

			// update isDelete,modified date in Action Table and
			// corresponding Get/post/bl with
			// updateActionAndActionTables(DatabaseConstants.ACTION_TABLE_NAME,DatabaseConstants.BL_TABLE_NAME,blActionusid,
			// isBLDelete);
			updateActionAndActionTables(OMSDatabaseConstants.ACTION_TABLE_NAME,
					OMSDatabaseConstants.GET_TABLE_NAME, getActionUsid,
					isGetDelete);
			updateActionAndActionTables(OMSDatabaseConstants.ACTION_TABLE_NAME,
					OMSDatabaseConstants.POST_TABLE_NAME, postActionUsid,
					isPostDelete);
			updateActionAndActionTables(OMSDatabaseConstants.ACTION_TABLE_NAME,
					OMSDatabaseConstants.MAIL_TABLE_NAME, mailActionUsid,
					isMailDelete);
			updateActionAndActionTables(OMSDatabaseConstants.ACTION_TABLE_NAME,
					"Contacts", callActionUsid, isCallDelete);
			updateActionAndActionTables(OMSDatabaseConstants.ACTION_TABLE_NAME,
					"CustomAction", customActionUsid, isCustomDelete);
			updateActionAndActionTables(OMSDatabaseConstants.ACTION_TABLE_NAME,
					"SocialIntegration", socialIntegrationActionUsid,
					isSocialDelete);
			updateActionAndActionTables(OMSDatabaseConstants.ACTION_TABLE_NAME,
					"Contacts", smsActionUsid, isSmsDelete);
		}
		// Call the below line after the action is done for giving control backk
		rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS.getValue());
	}

	private void updateActionAndActionTables(String actionTable,
			String actionsTable, String uniqueActionId, int isDelete) {
		Cursor actionCursor = null;
		actionCursor = OMSDBManager.getSpecificDB(DB).query(
				actionTable,
				null,
				OMSDatabaseConstants.UNIQUE_ROW_ID + "='" + uniqueActionId
						+ "'", null, null, null, null);
		ContentValues actionContentValues;

		if (actionCursor.moveToFirst()) {

			isDelete = isDelete == 0 ? 1 : 0;

			actionContentValues = new ContentValues();

			actionContentValues.put(OMSDatabaseConstants.IS_DELETE, isDelete);
			actionContentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
					OMSJulianDateGenerator.getJulDate());
			OMSDBManager.getSpecificDB(DB).update(actionTable,
					actionContentValues,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "=?",
					new String[] { uniqueActionId });

			Cursor actionsCursor = null;
			actionsCursor = OMSDBManager.getSpecificDB(DB).query(
					actionsTable,
					null,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "='" + uniqueActionId
							+ "'", null, null, null, null);
			if (actionsCursor.moveToFirst()) {

				actionContentValues = new ContentValues();
				actionContentValues.put(OMSDatabaseConstants.IS_DELETE,
						isDelete);
				actionContentValues.put(OMSDatabaseConstants.MODIFIED_DATE,
						OMSJulianDateGenerator.getJulDate());
				OMSDBManager.getSpecificDB(DB).update(actionsTable,
						actionContentValues,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "=?",
						new String[] { uniqueActionId });
			}

			actionsCursor.close();
		}
		actionCursor.close();

	}

	public void customActionMethod11() {
		/*new PostPoliciesAsyncTaskHelper(context, null, rListener, 0, "20")
				.execute();
		ContentValues contentValues = new ContentValues();
		contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
				OMSDatabaseConstants.ACTION_STATUS_FINISHED);
		actionCenterHelper.insertOrUpdateActionQueueByActionusid(contentValues,
				actionUsid, configDBAppId);*/
	}

	public void customActionMethod12() {
	/*	new PostPoliciesAsyncTaskHelper(context, null, rListener, 0, "30")
				.execute();*/
	}
}

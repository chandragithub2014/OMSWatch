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
 *//*

package watch.oms.omswatch.actioncenter.helpers;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.ConsoleDBConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;


public class ServerDBUpdateHelper {
	private String TAG = this.getClass().getSimpleName();
	private Context localContext = null;

	public ServerDBUpdateHelper(Context context) {
		this.localContext = context;
	}

	public Integer insertAppLaunchtimeData(String tableName, String appId) {
		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_APPID, appId);
		
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
	*/
/*	newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_LAUNCHTIME,
				ResponseAnalyzer.appLaunchTime);*//*

		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_STATUS,"fresh");
//		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_TIMESTAMP,
//				ResponseAnalyzer.appLaunchTimestamp);
		String usid = Long.toString(cal.getTimeInMillis());
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USID,
				usid);
		OMSApplication.getInstance().setAppLaunchTimeUsid(usid);
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_MODIFIEDDATE,
				System.currentTimeMillis());
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertAppLaunchtimeData. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertAppLaunchtimeData. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return (int) rowId;

	}

	public Integer insertAppMetaData(String tableName, String appId) {
		GoalManager.appname  = 	(String) localContext.getApplicationInfo()
		.loadLabel(localContext.getPackageManager());
		try {
			GoalManager.applicationversion = localContext.getPackageManager()
					.getPackageInfo(localContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();

		newrowvalues.put(ConsoleDBConstants.APPMETADATA_APPID, OMSApplication.getInstance().getAppId());
		newrowvalues.put(ConsoleDBConstants.APPMETADATA_USERID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.APPMETADATA_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.APPMETADATA_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
		
		newrowvalues.put(ConsoleDBConstants.APPMETADATA_APPNAME,
				GoalManager.appname);
		newrowvalues.put(ConsoleDBConstants.APPMETADATA_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.APPMETADATA_APPLICATION_VERSION,
				GoalManager.applicationversion);
		Location currGeoLocation = getCurrentGeoPosition();
		if (currGeoLocation != null && currGeoLocation.getLatitude() != 0) {
			newrowvalues.put(ConsoleDBConstants.APPMETADATA_LATITUDE,
					currGeoLocation.getLatitude());
		} else {
			newrowvalues.put(ConsoleDBConstants.APPMETADATA_LATITUDE,
					UserEnagementAnalyzer.latid);
		}
		if (currGeoLocation != null && currGeoLocation.getLongitude() != 0) {
			newrowvalues.put(ConsoleDBConstants.APPMETADATA_LONGITUDE,
					currGeoLocation.getLongitude());
		} else {
			newrowvalues.put(ConsoleDBConstants.APPMETADATA_LONGITUDE,
					UserEnagementAnalyzer.longid);
		}

		newrowvalues.put(ConsoleDBConstants.APPMETADATA_USID,
				Long.toString(cal.getTimeInMillis()));
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_MODIFIEDDATE,
				System.currentTimeMillis());
		newrowvalues.put(ConsoleDBConstants.DEVICEINFO_OSVERSION,
				UserEnagementAnalyzer.deviceOSVersion);
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG, "Error occurred in method insert appmetadata. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG, "Error occurred in method insert appmetadata. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	private Location getCurrentGeoPosition() {
		Location currentLocation = new Location("");
		try {
			LocationManager locationManager = (LocationManager) localContext
					.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				currentLocation = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

		} catch (SecurityException e) {
			Log.e(TAG,
					"Error Occured in getCurrentGeoPosition()" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error Occured in getCurrentGeoPosition()" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error Occured in getCurrentGeoPosition()" + e.getMessage());
			e.printStackTrace();
		}
		return currentLocation;
	}

	public Integer insertAppUsage(String tableName, String appId) {

		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();

		newrowvalues.put(ConsoleDBConstants.APPUSAGE_APPID, appId);
		newrowvalues.put(ConsoleDBConstants.APPUSAGE_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.APPUSAGE_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.APPUSAGE_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
		
		newrowvalues.put(ConsoleDBConstants.APPUSAGE_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.APPUSAGE_APPUSAGEDURATION,
				UserEnagementAnalyzer.appUsageTime);
		newrowvalues.put(ConsoleDBConstants.APPUSAGE_USID,
				Long.toString(cal.getTimeInMillis()));
		newrowvalues.put(ConsoleDBConstants.APPUSAGE_MODIFIEDDATE,
				System.currentTimeMillis());
		if (!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())) {
			newrowvalues.put(ConsoleDBConstants.APPUSAGE_APPNAME,
					OMSApplication.getInstance().getAppName());
		}
		
		newrowvalues.put("usersession", OMSApplication.getInstance().getUserSessionId());
		newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
		
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG, "Error occurred in method insert appusage. Error is:"
					+ e.getMessage());
		} catch (IllegalStateException e) {
			Log.e(TAG, "Error occurred in method insert appusage. Error is:"
					+ e.getMessage());
		}

		return (int) rowId;

	}

	public Integer insertBatteryInfo(String tableName, String appId) {
		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		newrowvalues.put(ConsoleDBConstants.BATTERYINFO_APPID, appId);

		newrowvalues.put(ConsoleDBConstants.BATTERYINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.BATTERYINFO_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.BATTERYINFO_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
		
		newrowvalues.put(ConsoleDBConstants.BATTERYINFO_REMAININGLIFE,
				ResourceAnalyzer.batteryRemainingLife);
		newrowvalues.put(ConsoleDBConstants.BATTERYINFO_BATTERYSTATUS,
				ResourceAnalyzer.batterystatus);
		newrowvalues.put(ConsoleDBConstants.BATTERYINFO_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.BATTERYINFO_USID,
				Long.toString(cal.getTimeInMillis()));
		newrowvalues.put(ConsoleDBConstants.BATTERYINFO_MODIFIEDDATE,
				System.currentTimeMillis());
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())){
			newrowvalues.put(ConsoleDBConstants.BATTERYINFO_APPNAME,OMSApplication.getInstance().getAppName());
		}
		
		newrowvalues.put("usersession", OMSApplication.getInstance().getUserSessionId());
		newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
		
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insert battery info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insert battery info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	public Integer insertCPUInfo(String tableName, String appId) {
		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		newrowvalues.put(ConsoleDBConstants.CPUINFO_APPID, appId);

		newrowvalues.put(ConsoleDBConstants.CPUINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.CPUINFO_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.CPUINFO_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
		
		newrowvalues.put(ConsoleDBConstants.CPUINFO_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.CPUINFO_APPCPU,
				ResourceAnalyzer.cpuUsage+" "+"sec");
//		newrowvalues.put(ConsoleDBConstants.CPUINFO_TIMESTAMP,
//				ResourceAnalyzer.cpuTimestamp);
		newrowvalues.put(ConsoleDBConstants.CPUINFO_USID,
				Long.toString(cal.getTimeInMillis()));
		newrowvalues.put(ConsoleDBConstants.CPUINFO_MODIFIEDDATE,
				System.currentTimeMillis());
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())){
			newrowvalues.put(ConsoleDBConstants.CPUINFO_APPNAME,OMSApplication.getInstance().getAppName());
		}
		
		newrowvalues.put("usersession", OMSApplication.getInstance().getUserSessionId());
		newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG, "Error occurred in method insert cpu info. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG, "Error occurred in method insert cpu info. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	public Integer insertDeviceInfo(String tableName, String appId) {
		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		Cursor sbCursor = null;
		String deviceid = null;
		Boolean flag = false;
		try {
			sbCursor = AppSecurityAndPerformanceHelper.getDashBoardDB().query(
					tableName,
					new String[] { ConsoleDBConstants.DEVICEINFO_DEVICEID },
					null, null, null, null, null);
			while (sbCursor.moveToNext()) {
				if (sbCursor
						.getString(sbCursor
								.getColumnIndex(ConsoleDBConstants.DEVICEINFO_DEVICEID)) != null) {
					deviceid = sbCursor
							.getString(sbCursor
									.getColumnIndex(ConsoleDBConstants.DEVICEINFO_DEVICEID));
					if (deviceid.equalsIgnoreCase(OMSApplication
							.getInstance().getDeviceId()))
						flag = true;
				}
			}
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method getting deviceid from tablename["
							+ tableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getting deviceid from tablename["
							+ tableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (sbCursor != null)
				sbCursor.close();
		}
		if (!flag) {
			newrowvalues.put(ConsoleDBConstants.DEVICEINFO_APPID, appId);

			newrowvalues.put(ConsoleDBConstants.DEVICEINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
			if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
				newrowvalues.put(ConsoleDBConstants.DEVICEINFO_USERID,
						OMSApplication.getInstance().getDeviceId());
			}
			else{
				newrowvalues.put(ConsoleDBConstants.DEVICEINFO_USERID,
					OMSApplication.getInstance().getLoginUserName());
			}
			
			if (UserEnagementAnalyzer.deviceOSVersion == null) {
				UserEnagementAnalyzer.deviceOSVersion = "";
			}
			newrowvalues.put(ConsoleDBConstants.DEVICEINFO_STATUS,"fresh");
			newrowvalues.put(ConsoleDBConstants.DEVICEINFO_OSVERSION,
					UserEnagementAnalyzer.deviceOSVersion);
			newrowvalues.put(ConsoleDBConstants.DEVICEINFO_TYPE,
					UserEnagementAnalyzer.deviceType);
			newrowvalues.put(ConsoleDBConstants.DEVICEINFO_USID,
					Long.toString(cal.getTimeInMillis()));
			newrowvalues.put(ConsoleDBConstants.DEVICEINFO_MODIFIEDDATE,
					System.currentTimeMillis());
			if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())){
			newrowvalues.put(ConsoleDBConstants.DEVICEINFO_APPNAME,OMSApplication.getInstance().getAppName());
			}
			
			
			newrowvalues.put("usersession", OMSApplication.getInstance().getUserSessionId());
			newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
			try {
				rowId = AppSecurityAndPerformanceHelper.getDashBoardDB()
						.insert(tableName, null, newrowvalues);
			} catch (SQLException e) {
				Log.e(TAG,
						"Error occurred in method insert device info. Error is:"
								+ e.getMessage());
				e.printStackTrace();
			} catch (IllegalStateException e) {
				Log.e(TAG,
						"Error occurred in method insert device info. Error is:"
								+ e.getMessage());
				e.printStackTrace();
			}
		}

		return (int) rowId;

	}

	public Integer insertDiskInfo(String tableName, String appId) {

		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		newrowvalues.put(ConsoleDBConstants.DISKINFO_APPID, OMSApplication.getInstance().getAppId());

		newrowvalues.put(ConsoleDBConstants.DISKINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.DISKINFO_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.DISKINFO_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
		
		newrowvalues.put(ConsoleDBConstants.DISKINFO_FREEDISK,
				ResourceAnalyzer.internalFree);
		newrowvalues.put(ConsoleDBConstants.DISKINFO_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.DISKINFO_TOTALDISK,
				ResourceAnalyzer.internalTotal);
		newrowvalues.put(ConsoleDBConstants.DISKINFO_USEDDISK,
				ResourceAnalyzer.internalUsed);
		newrowvalues.put(ConsoleDBConstants.DISKINFO_SDFREE,
				ResourceAnalyzer.sdcardFree);
		newrowvalues.put(ConsoleDBConstants.DISKINFO_SDTOTAL,
				ResourceAnalyzer.sdcardTotal);
		newrowvalues.put(ConsoleDBConstants.DISKINFO_SDUSED,
				ResourceAnalyzer.sdcardUsed);
		newrowvalues.put(ConsoleDBConstants.DISKINFO_USID,
				Long.toString(cal.getTimeInMillis()));
//		newrowvalues.put(ConsoleDBConstants.DISKINFO_TIMESTAMP,
//				AppMonitorServerService.convertMilliToDate(System
//						.currentTimeMillis()));
		newrowvalues.put(ConsoleDBConstants.DISKINFO_MODIFIEDDATE,
				System.currentTimeMillis());
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())){
		newrowvalues.put(ConsoleDBConstants.DISKINFO_APPNAME,OMSApplication.getInstance().getAppName());
		}
		
		newrowvalues.put("usersession", OMSApplication.getInstance().getUserSessionId());
		newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
		
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG, "Error occurred in method insert disk info. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG, "Error occurred in method insert disk info. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	public Integer insertFeatureInfo(String tableName, FeatureData fd,
			String appId) {

		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();

		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_APPID, appId);

		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.FEATUREINFO_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.FEATUREINFO_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
		
		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_CUSTOMDATA,
				fd.getCustomData());
		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_DURATION,
				fd.getDuration());

		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_FEATUREID,
				fd.getFeatureid());
		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_FREQUENCY,
				fd.getFrequency());
		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_NAME,
				fd.getFeatureName());
		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_USID,
				Long.toString(cal.getTimeInMillis()));
//		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_TIMESTAMP,
//				AppMonitorServerService.convertMilliToDate(System
//						.currentTimeMillis()));
		newrowvalues.put(ConsoleDBConstants.FEATUREINFO_MODIFIEDDATE,
				System.currentTimeMillis());
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insert feature info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insert feature info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	public String getCustomData(String tablename, String columnName,
			long startTime) {
		String customData = null;

		Cursor sbCursor = null;
		try {
			sbCursor = TransDatabaseUtil.query(tablename,
					new String[] { columnName, "max(modifieddate)" }, null,
					null, null, null, null);

			while (sbCursor.moveToNext()) {

				if (sbCursor.getString(sbCursor.getColumnIndex(columnName)) != null) {
					customData = sbCursor.getString(sbCursor
							.getColumnIndex(columnName));
				}
			}
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method getCustomData for input parameter tablename["
							+ tablename + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getCustomData for input parameter tablename["
							+ tablename + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (sbCursor != null)
				sbCursor.close();
		}

		return customData;
	}

	public Integer insertMemoryInfo(String tableName, String appId) {

		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_APPID, appId);

		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.MEMORYINFO_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.MEMORYINFO_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}

		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_TOTALRAM,
				ResourceAnalyzer.devRAMMemTotal);
		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_FREEMEMORY,
				ResourceAnalyzer.devRAMMemFree);
		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_USEDMEMORY,
				ResourceAnalyzer.devRAMMemUsed);
		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_APPMEMORY,
				ResourceAnalyzer.applicationMemory);
		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_USID,
				Long.toString(cal.getTimeInMillis()));
//		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_TIMESTAMP,
//				AppMonitorServerService.convertMilliToDate(System
//						.currentTimeMillis()));
		newrowvalues.put(ConsoleDBConstants.MEMORYINFO_MODIFIEDDATE,
				System.currentTimeMillis());
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())){
			newrowvalues.put(ConsoleDBConstants.MEMORYINFO_APP_NAME,OMSApplication.getInstance().getAppName());
		}
		
		
		newrowvalues.put("usersession", OMSApplication.getInstance().getUserSessionId());
		newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
		
		
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG, "Error occurred in method insert memory info. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG, "Error occurred in method insert memory info. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		}
		return (int) rowId;

	}

	public Integer insertConnectionInfo(String tableName, String appId) {

		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_APPID, appId);

		newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}

		newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_CONNECTIONSTATUS,
				UserEnagementAnalyzer.connectionStatus);
		newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_TYPE,
				UserEnagementAnalyzer.connectionType);
		newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_USID,
				Long.toString(cal.getTimeInMillis()));
//		newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_TIMESTAMP,
//				AppMonitorServerService.convertMilliToDate(System
//						.currentTimeMillis()));
		newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_MODIFIEDDATE,
				System.currentTimeMillis());
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())){
			newrowvalues.put(ConsoleDBConstants.CONNECTIONINFO_APP_NAME,OMSApplication.getInstance().getAppName());
		}
		
		newrowvalues.put("usersession", OMSApplication.getInstance().getUserSessionId());
		newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
		
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insert connection info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insert connection info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	public Integer insertNetworkInfo(String tableName, ConnectionDetails con,
			String appId) {

		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();

		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_APPID, appId);

		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.NETWORKINFO_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.NETWORKINFO_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}

		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_DURATION,
				con.getConnectionLatency());
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_HOSTNAME,
				con.getHostName());
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_LATENCY,
				con.getConnectionLatency());
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_REQUESTSIZE,
				con.getRequestSize());
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_RESPONSESIZE,
				con.getResponseSize());
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_REQUEST_TYPE,
				con.getRequestType());
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_NETWORKSTATUS,
				con.isConnectionStatus());
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_USID,
				Long.toString(cal.getTimeInMillis()));
//		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_TIMESTAMP,
//				AppMonitorServerService.convertMilliToDate(System
//						.currentTimeMillis()));
		newrowvalues.put(ConsoleDBConstants.NETWORKINFO_MODIFIEDDATE,
				System.currentTimeMillis());
	    newrowvalues.put(ConsoleDBConstants.NETWORK_DB_PROCESS_DURATION, OMSApplication.getInstance().getDatabaseProcessDuration());
		newrowvalues.put(ConsoleDBConstants.SERVER_PROCESS_DURATION, OMSApplication.getInstance().getServerProcessDuration());
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())){
			newrowvalues.put(ConsoleDBConstants.NETWORKINFO_APP_NAME,OMSApplication.getInstance().getAppName());
		}
		
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getUserSessionId())){
		newrowvalues.put("usersession", OMSApplication.getInstance().getUserSessionId());
		newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
		}
		
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insert network info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insert network info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	public Integer insertUIRenderTime(String tableName, UIDetails u,
			String appId) {

		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();

		newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_APPID, appId);

		newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}

		newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_RENDERTIME,
				u.getRenderTime());
		newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_UINAME, u.getUiName());
		*/
/*newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_USID,
				Long.toString(cal.getTimeInMillis()));*//*

		newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_USID,
				OMSApplication.getInstance().getScreensessionId());
//		newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_TIMESTAMP,
//				u.getTimestamp());
		newrowvalues.put(ConsoleDBConstants.UIRENDERTIME_MODIFIEDDATE,
				System.currentTimeMillis());
		newrowvalues.put("uiscreentitle",u.getUiscreentitle());
		//String usersession = "";
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppLaunchTimeUsid())) {
			newrowvalues.put(ConsoleDBConstants.UIRENDER_USER_SESSION, OMSApplication.getInstance().getAppLaunchTimeUsid());
		}else{
			newrowvalues.put(ConsoleDBConstants.UIRENDER_USER_SESSION, "0.0");
		}
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getAppName())){
			newrowvalues.put(ConsoleDBConstants.UIRENDER_APP_NAME,OMSApplication.getInstance().getAppName());
		}
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insert uirendertime info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insert uirendertime info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	public Integer insertTransactionInfo(String tableName,
			TransactionDetails tran, String appId) {

		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();

		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_APPID, appId);

		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}

		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_STATUS,"fresh");
		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_DURATION,
				tran.getTransactionDuration());
		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_NAME,
				tran.getTransactionName());
		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_TRANSACTIONID,
				tran.getTransactionID());
		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_TRANSTATUS,
				tran.getTransactionStatus());
		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_USID,
				Long.toString(cal.getTimeInMillis()));
//		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_TIMESTAMP,
//				tran.getTransactionTimestamp());
		newrowvalues.put(ConsoleDBConstants.TRANSACTIONINFO_MODIFIEDDATE,
				System.currentTimeMillis());
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insert transaction info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insert transaction info. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}

		return (int) rowId;

	}

	@SuppressWarnings("unchecked")
	private String getDeviceId()
	{
		String deviceId = null;
		if( OMSApplication.getInstance().getLoginCredentialHash()!=null){
			Hashtable <String,String>loginHash = OMSApplication.getInstance().getLoginCredentialHash();
			if(loginHash!=null)
				
			{
				if(!TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserColumnName()))
				if(loginHash.get(OMSApplication.getInstance().getLoginUserColumnName())!=null)
					{
						deviceId = loginHash.get(OMSApplication.getInstance().getLoginUserColumnName());
					}
			}
		}
		return deviceId;
		
	}
	
	

	public Integer insertAppLaunchtimeData(String tableName, String appId,String appLauchTime,String appName) {
		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_APPID, appId);
		
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_LAUNCHTIME,
				appLauchTime);
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_STATUS,"fresh");
//		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_TIMESTAMP,
//				ResponseAnalyzer.appLaunchTimestamp);
		String usid = Long.toString(cal.getTimeInMillis());
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USID,
				usid);
		OMSApplication.getInstance().setAppLaunchTimeUsid(usid);
		OMSApplication.getInstance().setUserSessionId(usid); //usersessionId
		
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_MODIFIEDDATE,
				System.currentTimeMillis());
		if(!TextUtils.isEmpty(appName)){
		newrowvalues.put(ConsoleDBConstants.APPLAUNCH_APPNAME,appName);
		}
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertAppLaunchtimeData. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertAppLaunchtimeData. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return (int) rowId;

	}
	
	
	
	//Insert CallTraceType 
	public Integer insertCallTraceTypeData(String tableName, String appId) {
		
		long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		if(!TextUtils.isEmpty(OMSApplication.getInstance().getUserSessionId())){
		ContentValues newrowvalues = new ContentValues();
		Calendar cal = Calendar.getInstance();
		newrowvalues.put(ConsoleDBConstants.CALL_TRACE_TYPE_APPID, appId);
		
		newrowvalues.put(ConsoleDBConstants.CALL_TRACE_TYPE_TRACE_TYPE, OMSApplication.getInstance().getTraceType());
		newrowvalues.put(ConsoleDBConstants.CALL_TRACE_TYPE_USER_SESSION,OMSApplication.getInstance().getUserSessionId());
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_STATUS,"fresh");
//		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_TIMESTAMP,
//				ResponseAnalyzer.appLaunchTimestamp);
		String usid = Long.toString(cal.getTimeInMillis());
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USID,
				usid);
		*/
/*OMSApplication.getInstance().setAppLaunchTimeUsid(usid);
		OMSApplication.getInstance().setUserSessionId(usid); //usersessionId
*//*

		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_MODIFIEDDATE,
				System.currentTimeMillis());
		
		newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_DEVICEID, OMSApplication.getInstance().getDeviceId());
		if(TextUtils.isEmpty(OMSApplication.getInstance().getLoginUserName())){
			newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USERID,
					OMSApplication.getInstance().getDeviceId());
		}
		else{
			newrowvalues.put(ConsoleDBConstants.APPLAUNCHTIME_USERID,
				OMSApplication.getInstance().getLoginUserName());
		}
		newrowvalues.put("screensession", OMSApplication.getInstance().getScreensessionId());
		try {
			rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
					tableName, null, newrowvalues);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method insertAppLaunchtimeData. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG,
					"Error occurred in method insertAppLaunchtimeData. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		}
		if(rowId>0){
			Log.i(TAG, ""+OMSApplication.getInstance().getTraceType());
		}
		return (int) rowId;

	}
	
	
	public Integer insertOrUpdateApmappListData( int appId,String appName){
		long rowId = -1;
		
		try {
			if(!checkForAppExistence(appId)) {
				ContentValues cv = new ContentValues();
				cv.put("status", "fresh");
				cv.put("isdelete",0);
				cv.put("appname",appName);
				cv.put("appid",appId);
				cv.put(ConsoleDBConstants.APPLAUNCHTIME_MODIFIEDDATE,
						System.currentTimeMillis());
				Calendar cal = Calendar.getInstance();
				String usid = Long.toString(cal.getTimeInMillis());
				cv.put(ConsoleDBConstants.APPLAUNCHTIME_USID,
						usid);
				rowId = AppSecurityAndPerformanceHelper.getDashBoardDB().insert(
						"apmapplist", null, cv);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (int)rowId;
	}
	
	private boolean checkForAppExistence(int appId) throws Exception{
		boolean isAppIdExists = false;
		String selection = OMSDatabaseConstants.CONFIGDB_APPID
				+ " = " + appId;
		Cursor transCursor = null;
		try {
			transCursor = AppSecurityAndPerformanceHelper.getDashBoardDB().query("apmapplist", null,
					selection, null, null, null, null);
			if(transCursor.moveToFirst()){
				isAppIdExists = true;
			}
			
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getTransDataForUsid  for input parameter tableName["
							+ "apmapplist" + "],  rowId[" + appId + "]. Error is:"
							+ e.getMessage());
			throw new Exception(e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransDataForUsid  for input parameter tableName["
							+ "apmapplist" + "],  rowId[" + appId + "]. Error is:"
							+ e.getMessage());
			throw new Exception(e.getMessage());
		}
		return isAppIdExists;
	}
	//End
}
*/

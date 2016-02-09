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
package watch.oms.omswatch.constants;

public class ConsoleDBConstants {

	// applaunchtime
	public static final String APPLAUNCHTIME_TABLE = "applaunchtime";
	public static final String APPLAUNCHTIME_ID = "id";
	public static final String APPLAUNCHTIME_APPID = "appid";
	public static final String APPLAUNCHTIME_DEVICEID = "deviceid";
	public static final String APPLAUNCHTIME_USERID = "userid";
	public static final String APPLAUNCHTIME_LAUNCHTIME = "applaunchtime";
	public static final String APPLAUNCHTIME_TIMESTAMP = "timestamp";
	public static final String APPLAUNCHTIME_ISDELETE = "isdelete";
	public static final String APPLAUNCHTIME_MODIFIEDDATE = "modifieddate";
	public static final String APPLAUNCHTIME_STATUS = "status";
	public static final String APPLAUNCHTIME_USID = "usid";
	public static final String APPLAUNCH_APPNAME= "appname";

	// appmetadata
	public static final String APPMETADATA_TABLE 		= "appmetadata";
	public static final String APPMETADATA_ID 			= "id";
	public static final String APPMETADATA_APPID 		= "appid";
	public static final String APPMETADATA_DEVICE_ID 	= "deviceid";
	public static final String APPMETADATA_USERID	 	= "userid";
	public static final String APPMETADATA_LATITUDE 	= "latitude";
	public static final String APPMETADATA_LONGITUDE 	= "longitude";
	public static final String APPMETADATA_TIMESTAMP 	= "timestamp";
	public static final String APPMETADATA_ISDELETE 	= "isdelete";
	public static final String APPMETADATA_MODIFIEDDATE = "modifieddate";
	public static final String APPMETADATA_STATUS 		= "status";
	public static final String APPMETADATA_USID 		= "usid";
	public static final String APPMETADATA_APPNAME 		= "appname";
	public static final String APPMETADATA_APPLICATION_VERSION = "appversion";
	public static final String APPLICATION_VERSION 		= "applicationversion";

	/* Column information for - appmaster */
	public static final String APPMASTER_TABLE_NAME = "appmaster";
	public static final String APPMASTER_APPID = "appid";
	public static final String APPMASTER_POLICYID = "policyid";

	/* Column information for - policyfeatures3 */
	public static final String APPGUARDPOLICIES_TABLE_NAME = "policymaster";
	public static final String APPGUARDPOLICIES_APPLOOGING = "applogging";
	public static final String APPGUARDPOLICIES_SECURE_PASTEBIOARD = "securepasteboard";
	public static final String APPGUARDPOLICIES_AUTHENTICATION = "authentication";
	public static final String APPGUARDPOLICIES_POLICYID = "policyid";
	public static final String APPGUARDPOLICIES_DOWNLOAD_LIMIT = "downloadlimit";
	public static final String APPGUARDPOLICIES_USID = "usid";
	public static final String APPGUARDPOLICIES_HIDDENSCREEN = "hidescreen";
	public static final String APPGUARDPOLICIES_FILESHAREING = "filesharingaccess";
	public static final String APPGUARDPOLICIES_APPID = "appid";
	public static final String APPGUARDPOLICIES_DB_DATAENCRYPTION = "dbdataencryption";
	public static final String APPGUARDPOLICIES_SQL_INJECTION_CHECK = "sqlinjectioncheck";
	public static final String APPGUARDPOLICIES_DATA_AT_REST_ENCRYPTION = "dataatrestencryption";
	public static final String APPGUARDPOLICIES_NO_OF_WRONG_ATTEEMPTS = "noofmaxwrongattempts";
	public static final String APPGUARDPOLICIES_ENCRYPTION_KEY = "dataencryptionkey";
	public static final String APPGUARDPOLICIES_VALIDATE_SQL_INJECTIONS = "validatesqlinjection";
	public static final String APPGUARDPOLICIES_APP_LOCKED = "applocked";
	public static final String APPGUARDPOLICIES_NETWORK_PROTECT_STATUS = "networkprotectstatus";

	/* Column information for - timefencing */
	public static final String TIMEFENCING_TABLE_NAME = "policymaster";
	public static final String TIMEFENCING_USAGESTATUS = "usagestatus";
	public static final String TIMEFENCING_TIMEFENCING_STATUS = "timefencingstatus";
	public static final String TIMEFENCING_ENDTIME = "endtime";
	public static final String TIMEFENCING_STARTTIME = "starttime";
	public static final String TIMEFENCING_POLICY_ID = "policyid";
	public static final String TIMEFENCING_TIMEFRAME_STATUS = "timeframestatus";
	public static final String TIMEFENCING_LOCATION_ID = "locationid";
	public static final String TIMEFENCING_APP_USAGE_STATUS = "usagestatus";
	public static final String TIMEFENCING_APP_USAGES_TIME = "appusagetime";
	public static final String TIMEFENCING_APP_IDLETIME = "appidletime";
	public static final String TIMEFENCING_APP_IDLE_STATUS = "idlestatus";
	public static final String TIMEFENCING_APPID = "appid";

	// appusage
	public static final String APPUSAGE_TABLE	 		= "appusage";
	public static final String APPUSAGE_ID 				= "id";
	public static final String APPUSAGE_APPID 			= "appid";
	public static final String APPUSAGE_DEVICEID 		= "deviceid";
	public static final String APPUSAGE_USERID 			= "userid";
	public static final String APPUSAGE_APPUSAGEDURATION= "appusageduration";
	public static final String APPUSAGE_TIMESTAMP 		= "timestamp";
	public static final String APPUSAGE_ISDELETE 		= "isdelete";
	public static final String APPUSAGE_STATUS 			= "status";
	public static final String APPUSAGE_USID 			= "usid";
	public static final String APPUSAGE_MODIFIEDDATE 	= "modifieddate";
    public static final String APPUSAGE_APPNAME = "appname";
	// batteryinfo
	public static final String BATTERYINFO_TABLE 		= "batteryinfo";
	public static final String BATTERYINFO_ID 			= "id";
	public static final String BATTERYINFO_APPID 		= "appid";
	public static final String BATTERYINFO_DEVICEID		= "deviceid";
	public static final String BATTERYINFO_USERID 		= "userid";
	public static final String BATTERYINFO_REMAININGLIFE= "remaininglife";
	public static final String BATTERYINFO_TIMESTAMP 	= "timestamp";
	public static final String BATTERYINFO_ISDELETE 	= "isdelete";
	public static final String BATTERYINFO_STATUS 		= "status";
	public static final String BATTERYINFO_USID 		= "usid";
	public static final String BATTERYINFO_MODIFIEDDATE = "modifieddate";
	public static final String BATTERYINFO_BATTERYSTATUS="batterystatus";
	public static final String BATTERYINFO_APPNAME = "appname";
	
	/* Column information for - geofencing */
	public static final String GEOFENCING_TABLE_NAME = "policymaster";
	public static final String GEOFENCING_LATITUDE = "latitude";
	public static final String GEOFENCING_STATUS = "geofencingstatus";
	public static final String GEOFENCING_APPID = "appid";
	public static final String GEOFENCING_POLICYID = "policyid";
	public static final String GEOFENCING_RADIUS = "radius";
	public static final String GEOFENCING_LONGITUDE = "longitude";

	// cpuinfo
	public static final String CPUINFO_TABLE 			= "cpuinfo";
	public static final String CPUINFO_ID 				= "id";
	public static final String CPUINFO_APPID 			= "appid";
	public static final String CPUINFO_DEVICEID 		= "deviceid";
	public static final String CPUINFO_USERID 			= "userid";
	public static final String CPUINFO_SYSTEMCPU 		= "systemcpu";
	public static final String CPUINFO_IDLECPU 			= "idlecpu";
	public static final String CPUINFO_APPCPU 			= "appcpuusage";
	public static final String CPUINFO_TIMESTAMP 		= "timestamp";
	public static final String CPUINFO_ISDELETE 		= "isdelete";
	public static final String CPUINFO_STATUS 			= "status";
	public static final String CPUINFO_USID 			= "usid";
	public static final String CPUINFO_MODIFIEDDATE 	= "modifieddate";
	public static final String CPUINFO_APPNAME = "appname";
	/* Column information for - networkprotect */
	public static final String NETWORKPROTECT_TABLE_NAME = "policymaster";
	public static final String NETWORKPROTECT_WAITTIMEOUT = "waittimeout";
	public static final String NETWORKPROTECT_CONNETIONTIMEOUT = "connectiontimeout";
	public static final String NETWORKPROTECT_SECURITYTYPE = "securitytype";
	public static final String NETWORKPROTECT_MULTITHREADCLIENT = "multithreadclient";
	public static final String NETWORKPROTECT_POLICYID = "policyid";
	public static final String NETWORKPROTECT_NETWORKPROTECT = "networkprotectstatus";
	public static final String APPGUARDPOLICIES_NETWORKPROTECT_APPID = "appid";
	// deviceinfo
	public static final String DEVICEINFO_TABLE = "deviceinfo";
	public static final String DEVICEINFO_ID = "id";
	public static final String DEVICEINFO_APPID = "appid";
	public static final String DEVICEINFO_DEVICEID = "deviceid";
	public static final String DEVICEINFO_USERID = "userid";
	public static final String DEVICEINFO_OSVERSION = "deviceosversion";
	public static final String DEVICEINFO_TYPE = "devicetype";
	public static final String DEVICEINFO_ISDELETE = "isdelete";
	public static final String DEVICEINFO_STATUS = "status";
	public static final String DEVICEINFO_USID = "usid";
	public static final String DEVICEINFO_MODIFIEDDATE = "modifieddate";
	public static final String DEVICEINFO_APPNAME = "appname";

	/* Column information for - devicedetails */
	public static final String DEVICEDETAILS_TABLE_NAME = "devicedetails";
	public static final String DEVICEDETAILS_IS_LOCKED = "isapplocked";
	public static final String DEVICEDETAILS_UNLOCKE_CODE = "unlockcode";
	public static final String DEVICEDETAILS_DEVICEID = "deviceid";

	// diskinfo
	public static final String DISKINFO_TABLE = "diskinfo";
	public static final String DISKINFO_ID = "id";
	public static final String DISKINFO_APPID = "appid";
	public static final String DISKINFO_DEVICEID = "deviceid";
	public static final String DISKINFO_USERID = "userid";
	public static final String DISKINFO_TOTALDISK = "totaldisk";
	public static final String DISKINFO_FREEDISK = "freedisk";
	public static final String DISKINFO_USEDDISK = "useddisk";
	public static final String DISKINFO_SDTOTAL = "sdcardtotal";
	public static final String DISKINFO_SDUSED = "sdcardused";
	public static final String DISKINFO_SDFREE = "sdcardfree";
	public static final String DISKINFO_TIMESTAMP = "timestamp";
	public static final String DISKINFO_ISDELETE = "isdelete";
	public static final String DISKINFO_STATUS = "status";
	public static final String DISKINFO_USID = "usid";
	public static final String DISKINFO_MODIFIEDDATE = "modifieddate";
	public static final String DISKINFO_APPNAME = "appname";

	// featureinfo
	public static final String FEATUREINFO_TABLE = "featureinfo";
	public static final String FEATUREINFO_ID = "id";
	public static final String FEATUREINFO_APPID = "appid";
	public static final String FEATUREINFO_DEVICEID = "deviceid";
	public static final String FEATUREINFO_USERID = "userid";
	public static final String FEATUREINFO_CUSTOMDATA = "customdata";
	public static final String FEATUREINFO_FEATUREID = "featureid";
	public static final String FEATUREINFO_NAME = "featurename";
	public static final String FEATUREINFO_DURATION = "featureduration";
	public static final String FEATUREINFO_FREQUENCY = "featurefrequency";
	public static final String FEATUREINFO_TIMESTAMP = "timestamp";
	public static final String FEATUREINFO_ISDELETE = "isdelete";
	public static final String FEATUREINFO_STATUS = "status";
	public static final String FEATUREINFO_USID = "usid";
	public static final String FEATUREINFO_MODIFIEDDATE = "modifieddate";

	// memoryinfo
	public static final String MEMORYINFO_TABLE = "memoryinfo";
	public static final String MEMORYINFO_ID = "id";
	public static final String MEMORYINFO_APPID = "appid";
	public static final String MEMORYINFO_DEVICEID = "deviceid";
	public static final String MEMORYINFO_USERID = "userid";
	public static final String MEMORYINFO_APPMEMORY = "appmemory";
	public static final String MEMORYINFO_FREEMEMORY = "freememory";
	public static final String MEMORYINFO_TOTALRAM = "totalram";
	public static final String MEMORYINFO_USEDMEMORY = "usedmemory";
	public static final String MEMORYINFO_TIMESTAMP = "timestamp";
	public static final String MEMORYINFO_ISDELETE = "isdelete";
	public static final String MEMORYINFO_STATUS = "status";
	public static final String MEMORYINFO_USID = "usid";
	public static final String MEMORYINFO_MODIFIEDDATE = "modifieddate";
	public static final String MEMORYINFO_APP_NAME = "appname";
	
	// connectioninfo
	public static final String CONNECTIONINFO_TABLE = "connectioninfo";
	public static final String CONNECTIONINFO_ID = "id";
	public static final String CONNECTIONINFO_APPID = "appid";
	public static final String CONNECTIONINFO_DEVICEID = "deviceid";
	public static final String CONNECTIONINFO_USERID = "userid";
	public static final String CONNECTIONINFO_TYPE = "connectiontype";
	public static final String CONNECTIONINFO_CONNECTIONSTATUS = "connectionstatus";
	public static final String CONNECTIONINFO_TIMESTAMP = "timestamp";
	public static final String CONNECTIONINFO_ISDELETE = "isdelete";
	public static final String CONNECTIONINFO_STATUS = "status";
	public static final String CONNECTIONINFO_USID = "usid";
	public static final String CONNECTIONINFO_MODIFIEDDATE = "modifieddate";
	public static final String CONNECTIONINFO_APP_NAME = "appname";
	
	// networkinfo
	public static final String NETWORKINFO_TABLE = "networkinfo";
	public static final String NETWORKINFO_ID = "id";
	public static final String NETWORKINFO_APPID = "appid";
	public static final String NETWORKINFO_DEVICEID = "deviceid";
	public static final String NETWORKINFO_USERID = "userid";
	public static final String NETWORKINFO_HOSTNAME = "hostname";
	public static final String NETWORKINFO_LATENCY = "latency";
	public static final String NETWORKINFO_REQUESTSIZE = "requestsize";
	public static final String NETWORKINFO_RESPONSESIZE = "responsesize";
	public static final String NETWORKINFO_DURATION = "duration";
	public static final String NETWORKINFO_REQUEST_TYPE = "requesttype";
	public static final String NETWORKINFO_TIMESTAMP = "timestamp";
	public static final String NETWORKINFO_ISDELETE = "isdelete";
	public static final String NETWORKINFO_STATUS = "status";
	public static final String NETWORKINFO_USID = "usid";
	public static final String NETWORKINFO_MODIFIEDDATE = "modifieddate";
	public static final String NETWORKINFO_NETWORKSTATUS = "networkstatus";
	public static final String NETWORKINFO_APP_NAME = "appname"; 
	public static final String NETWORK_DB_PROCESS_DURATION="dbprocessduration";
	public static final String SERVER_PROCESS_DURATION="serverprocessduration";
	
	// transactioninfo
	public static final String TRANSACTIONINFO_TABLE = "transactioninfo";
	public static final String TRANSACTIONINFO_ID = "id";
	public static final String TRANSACTIONINFO_APPID = "appid";
	public static final String TRANSACTIONINFO_DEVICEID = "deviceid";
	public static final String TRANSACTIONINFO_USERID = "userid";
	public static final String TRANSACTIONINFO_TRANSACTIONID = "transactionid";
	public static final String TRANSACTIONINFO_NAME = "transactionname";
	public static final String TRANSACTIONINFO_TRANSTATUS = "transactionstatus";
	public static final String TRANSACTIONINFO_DURATION = "transactionduration";
	public static final String TRANSACTIONINFO_TIMESTAMP = "timestamp";
	public static final String TRANSACTIONINFO_ISDELETE = "isdelete";
	public static final String TRANSACTIONINFO_STATUS = "status";
	public static final String TRANSACTIONINFO_USID = "usid";
	public static final String TRANSACTIONINFO_MODIFIEDDATE = "modifieddate";

	// uirendertime
	public static final String UIRENDERTIME_TABLE = "uirenderinfo";
	public static final String UIRENDERTIME_ID = "id";
	public static final String UIRENDERTIME_APPID = "appid";
	public static final String UIRENDERTIME_DEVICEID = "deviceid";
	public static final String UIRENDERTIME_USERID = "userid";
	public static final String UIRENDERTIME_UINAME = "uiname";
	public static final String UIRENDERTIME_RENDERTIME = "uirendartime";
	public static final String UIRENDERTIME_TIMESTAMP = "timestamp";
	public static final String UIRENDERTIME_ISDELETE = "isdelete";
	public static final String UIRENDERTIME_STATUS = "status";
	public static final String UIRENDERTIME_USID = "usid";
	public static final String UIRENDERTIME_MODIFIEDDATE = "modifieddate";
	public static final String UIRENDER_USER_SESSION = "usersession";
	public static final String UIRENDER_APP_NAME= "appname";
	
	public static final String APPGUARDPOLICIES_TIMEFENCING_TABLE_NAME = "timefencing";
	public static final String APPGUARDPOLICIES_TIMEFENCING_USAGESTATUS = "usagestatus";
	public static final String APPGUARDPOLICIES_TIMEFENCING_TIMEFENCING_STATUS = "timefencingstatus";
	public static final String APPGUARDPOLICIES_TIMEFENCING_ENDTIME = "endtime";
	public static final String APPGUARDPOLICIES_TIMEFENCING_STARTTIME = "starttime";
	public static final String APPGUARDPOLICIES_TIMEFENCING_POLICY_ID = "policyid";
	public static final String APPGUARDPOLICIES_TIMEFENCING_TIMEFRAME_STATUS = "timeframestatus";
	public static final String APPGUARDPOLICIES_TIMEFENCING_LOCATION_ID = "locationid";
	public static final String APPGUARDPOLICIES_TIMEFENCING_APP_USAGES_TIME = "appusagetime";
	public static final String APPGUARDPOLICIES_TIMEFENCING_APP_IDLETIME = "appidletime";
	public static final String APPGUARDPOLICIES_TIMEFENCING_APPID = "appid";
	
	
	// CALLTRACETYPE
		public static final String CALL_TRACE_TYPE_TABLE = "calltracetype";
		public static final String CALL_TRACE_TYPE_ID = "id";
		public static final String CALL_TRACE_TYPE_APPID = "appid";
		public static final String CALL_TRACE_TYPE_ISDELETE = "isdelete";
		public static final String CALL_TRACE_TYPE_MODIFIEDDATE = "modifieddate";
		public static final String CALL_TRACE_TYPE_STATUS = "status";
		public static final String CALL_TRACE_TYPE_USID = "usid";
		public static final String CALL_TRACE_TYPE_USER_SESSION = "usersession";
		public static final String CALL_TRACE_TYPE_TRACE_TYPE = "tracetype";

}

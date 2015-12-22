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
package watch.oms.omswatch.WatchDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

public class OMSDBManager extends SQLiteOpenHelper implements OMSReceiveListener {

	private final static String TAG = OMSDBManager.class.getSimpleName();

	private static final String POLICY_DB = "20.db";
	private static final String ALL_TABLE = "all";
	private static Context localContext;
	private static SQLiteDatabase configDB;
	private static SQLiteDatabase transDB;
	private static SQLiteDatabase specificDB;
	//private String TransDataBaseName;
	private OMSReceiveListener inReceiveListener;
	private static ConnectivityManager connectionManager;
	private static String currectActiveSchema = "";
	private SharedPreferences sp =null;
    private boolean isConfigCreated,isTransCreated;
	public OMSDBManager(Context context, OMSReceiveListener inReceiveListener) {
		super(context, OMSDatabaseConstants.CONFIG_DB_NAME, null, 1);
		localContext = context;
		this.inReceiveListener = inReceiveListener;
		sp = localContext.getSharedPreferences("TRANS_DB_COLS", 0);
	}

	public void load() {

		// create config database
		createConfigDataBase();
		OMSApplication.getInstance().setAppId(OMSConstants.APP_ID);
		createWatchTransDataBase();
        if(isConfigCreated && isTransCreated){
            receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
        }

	/*	// populate data into config database schema
		if (!(OMSConstants.BYPASS_WEBSERVICE_CALL_CONFIGDB)) {
			if (!(OMSApplication.getInstance().isGlobalByPass999())) {
				if (checkNetworkConnectivity()) {
					populateConfigDB();
				} else {
					createTransDB(Integer.parseInt(OMSApplication.getInstance().getAppId()));
				}
			} else {
				receiveResult(OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS.getValue());//OMSMessages.TRANS_DATABASE_SUCCESS
			}
		} else {
			receiveResult(OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS.getValue());
		}*/
	}

	private OMSDBManager() {
		super(localContext, OMSDatabaseConstants.CONFIG_DB_NAME, null, 1);
	}

	private void createConfigDataBase() {
		Log.i(TAG, "method createConfigDataBase");
		boolean dbExist = isConfigDBExists();
		Log.i(TAG, "is config DB exists- " + dbExist);
		if (dbExist) {
		} else {
			this.getReadableDatabase();
			try {
				copyConfigDatabase();
			} catch (IOException e) {
				Log.e(TAG,
						"IOException occurred in method createConfigDataBase . Error is:"
								+ e.getMessage());
				e.printStackTrace();
			}
		}
        isConfigCreated = true;
		openConfigDBConnection();
	}

	private boolean isConfigDBExists() {

		/*
		 * SQLiteDatabase checkDB = null; try { checkDB =
		 * SQLiteDatabase.openDatabase( localContext.getDatabasePath(
		 * DatabaseConstants.CONFIG_DB_NAME).toString(), null,
		 * SQLiteDatabase.OPEN_READONLY); } catch (SQLiteException e) {
		 * Log.e(TAG, "Config DB does not exist"); } if (checkDB != null) {
		 * checkDB.close(); } return checkDB != null ? true : false;
		 */

		return new File(localContext.getDatabasePath(
				OMSDatabaseConstants.CONFIG_DB_NAME).toString()).isFile();
	}

	private void copyConfigDatabase() throws IOException {
		Log.i(TAG,
				"method copyConfigDatabase, copying configDB schema from local to device");

		InputStream inputStream = localContext.getAssets().open(
				OMSDatabaseConstants.CONFIG_DB_NAME);
		String outFileName = localContext.getDatabasePath(
				OMSDatabaseConstants.CONFIG_DB_NAME).toString();
		OutputStream outputStream = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		outputStream.flush();
		outputStream.close();
		inputStream.close();
		this.close();
	}



	private void createWatchTransDataBase() {
		Log.i(TAG, "method createConfigDataBase");
		boolean dbExist = isWatchTransDBExists();
		Log.i(TAG, "is config DB exists- " + dbExist);
		if (dbExist) {
		} else {
			this.getReadableDatabase();
			try {
				copyWatchTransDatabase();
			} catch (IOException e) {
				Log.e(TAG,
						"IOException occurred in method createConfigDataBase . Error is:"
								+ e.getMessage());
				e.printStackTrace();
			}
		}
        isTransCreated = true;
		openTransDBConnection();
	}

	private boolean isWatchTransDBExists() {

		/*
		 * SQLiteDatabase checkDB = null; try { checkDB =
		 * SQLiteDatabase.openDatabase( localContext.getDatabasePath(
		 * DatabaseConstants.CONFIG_DB_NAME).toString(), null,
		 * SQLiteDatabase.OPEN_READONLY); } catch (SQLiteException e) {
		 * Log.e(TAG, "Config DB does not exist"); } if (checkDB != null) {
		 * checkDB.close(); } return checkDB != null ? true : false;
		 */

		return new File(localContext.getDatabasePath(
				OMSDatabaseConstants.TRANS_DB_NAME).toString()).isFile();
	}

	private void copyWatchTransDatabase() throws IOException {
		Log.i(TAG,
				"method copyWatchTransDatabase, copying TransDatabase schema from local to device");

		InputStream inputStream = localContext.getAssets().open(
				OMSDatabaseConstants.TRANS_DB_NAME);
		String outFileName = localContext.getDatabasePath(
				OMSDatabaseConstants.TRANS_DB_NAME).toString();
		OutputStream outputStream = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		outputStream.flush();
		outputStream.close();
		inputStream.close();
		this.close();
	}


/*
	public void populateConfigDB() {
		Log.i(TAG, "ConfigDB schema - data population starting ..");
		double modifiedDate = new OMSDBParserHelper()
		.getLastModifiedConfigDateFromAppsTable(Integer
				.parseInt(OMSApplication.getInstance()
						.getAppId()));
		String configURL="";
		if(OMSConstants.USE_GENERIC_URL) {
			String appId = OMSApplication.getInstance().getAppId();
			configURL=  OMSApplication.getInstance()
					.getConfigURL()+"10"+"?"+"modifieddate="+modifiedDate+"&"+"appid="+appId;
		}else{
			configURL = OMSApplication.getInstance()
					.getConfigURL()
					+ OMSMessages.PATH_SEP_WIN.getValue()
					+ modifiedDate;
		}
		//		String appId = OMSApplication.getInstance().getAppId();
		/*	configURL=  OMSApplication.getInstance()
				.getConfigURL()+"?"+"modifieddate="+modifiedDate+"&"+"appid="+appId;*/



	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public static SQLiteDatabase getConfigDB() {
		return configDB;
	}



	@Override
	public void receiveResult(String result) {
		
		android.util.Log.d(TAG, "OMS DBManager :: " + result);
		
		if(result!=null){
            if (result.contains(OMSMessages.TRANS_DATABASE_SUCCESS.getValue())) {
                Log.i(TAG, "TransDataBase  schema - data population finished");
                if (inReceiveListener != null) {
					Log.i(TAG, "inReceiveListener!=null");
                    inReceiveListener.receiveResult(result);
                }
            }
        }
	/*	if (result != null){
			if (result.contains(OMSMessages.POLICY_DATABASE_SUCCESS.getValue())) {
				Log.i(TAG, "PolicyDataBase  schema - data population finished");
				if (inReceiveListener != null) {
					inReceiveListener.receiveResult(result);
				}
			}	
			else if (result.contains(OMSMessages.TRANS_DATABASE_SUCCESS.getValue())) {
				Log.i(TAG, "transDB schema - data population finished");

*//*				if (inReceiveListener != null) {
					inReceiveListener.receiveResult(result);
				}
				
*//*				// Commented for AppGuard
				*//* Clean action queue as when config db is ready *//*
			*//*	if (!(OMSConstants.BYPASS_WEBSERVICE_CALL_CONFIGDB)) {
					if (!(OMSApplication.getInstance()
							.isGlobalByPass999())) {
						ActionCenterHelper acHelper = new ActionCenterHelper(
								localContext);
						acHelper.clearActionQueue();
						if (checkNetworkConnectivity()) {
							populatePoliciesDataBase();
						}else{
							receiveResult(OMSMessages.POLICY_DATABASE_FAILURE.getValue());
						}
					} else {
						createPolicyDataBase();
						receiveResult(OMSMessages.POLICY_DATABASE_SUCCESS.getValue());
					}
				} else {
					createPolicyDataBase();
					receiveResult(OMSMessages.POLICY_DATABASE_SUCCESS.getValue());
				}*//*
//added for AppGuard
				if (inReceiveListener != null) {
					inReceiveListener.receiveResult(result);
				}
			} else if (result.contains(OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
					.getValue())) {
				Log.i(TAG, "ConfigDB schema - data population finished");

				*//*if (inReceiveListener != null) {
					inReceiveListener.receiveResult(result);
				}*//*
				if (!(OMSConstants.BYPASS_WEBSERVICE_CALL_CONFIGDB)) {
					//if (!(OMSApplication.getInstance()
					//	.isGlobalByPass999())) {
					createTransDB(Integer.parseInt(OMSApplication.getInstance().getAppId()));
					//} else {
					//createTransDataBase();
					//receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
					//}
				} else {
					createTransDataBase();
					//receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());  pradeep
				}
			} else if (result.contains(OMSMessages.CONFIG_DB_ERROR.getValue())) {
				Log.i(TAG, "ConfigDB schema - data population failed");
				if (inReceiveListener != null) {
					inReceiveListener.receiveResult(result);
				}
			} else if (result.contains(OMSMessages.TRANS_DB_ERROR.getValue())
					|| result.contains("TransDataBaseFailure")) {
				Log.i(TAG, "TransDB schema - data population failed");
				if (inReceiveListener != null) {
					inReceiveListener.receiveResult(result);
				}
			} else{
				Log.e(TAG, "method receiveResult: result["+result+"] value not matched");
				if (inReceiveListener != null) {
					inReceiveListener.receiveResult(result);
				}
			}
		}else{
			Log.e(TAG, "method receiveResult: Result is null");
		}*/
	}

	private void openConfigDBConnection() {
		Log.i(TAG, "Opening Config DB Connection");
		if (configDB == null || !configDB.isOpen()) {
			configDB = SQLiteDatabase.openDatabase(localContext
					.getDatabasePath(OMSDatabaseConstants.CONFIG_DB_NAME)
					.toString(), null, SQLiteDatabase.OPEN_READWRITE);
		}
	}

	private static void openTransDBConnection() {
		String transDatabaseName =OMSDatabaseConstants.TRANS_DB_NAME;
		Log.i(TAG, "Opening Trans DB Connection for " + transDatabaseName);
		// Added to access data from GlobalConstants
		try{
			if (transDB == null || !transDB.isOpen()) {
				transDB = SQLiteDatabase.openDatabase(
						localContext.getDatabasePath(transDatabaseName).toString(),
						null, SQLiteDatabase.OPEN_READWRITE);
				TransDatabaseUtil.setTransDB(transDB, localContext);
			}
		} catch(Exception exception){

		}
	}

	// Verify Network Availability.
	public static boolean checkNetworkConnectivity() {
		Log.d("TAG","checkNetworkConnectivity() OMSDBMANAGER");
		connectionManager = (ConnectivityManager) localContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectionManager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}









	












	/*public static SQLiteDatabase getSpecificDB(String schemaName) {
		// If the request schema name is same as current active, the database is
		// already open.
		if (currectActiveSchema.equals(schemaName) && specificDB != null  && specificDB.isOpen()) {
			return specificDB;
		} else {
			Log.i(TAG, "Specific DB - New connection for " + schemaName);
			// Close the old schema, if any
			closeSpecificSchemaConnection();

			// Open new Schema
			String specificSchemaName = schemaName + OMSMessages.DB_EXT.getValue();
			specificDB = SQLiteDatabase.openDatabase(localContext
					.getDatabasePath(specificSchemaName).toString(), null,
					SQLiteDatabase.OPEN_READWRITE);

			// Set New schema as current active
			currectActiveSchema = schemaName;

			return specificDB;
		}*/



	public static void closeDBConnections() {
		closeConfigDBConnection();
		closeTransDBConnection();
		closeSpecificSchemaConnection();
	}

	private static void closeConfigDBConnection() {
		if (configDB != null && configDB.isOpen()) {
			configDB.close();
			configDB = null;
		}
	}

	public static void closeTransDBConnection() {
		if (transDB != null && transDB.isOpen()) {
			transDB.close();
			transDB = null;
		}
	}

	private static void closeSpecificSchemaConnection() {
		if (specificDB != null && specificDB.isOpen()) {
			if (specificDB != null)
				specificDB.close();
			specificDB = null;
		}
	}
	


	
}
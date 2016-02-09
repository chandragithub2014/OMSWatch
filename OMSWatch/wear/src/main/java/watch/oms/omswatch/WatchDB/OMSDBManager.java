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


import watch.oms.omswatch.MainActivity;
import watch.oms.omswatch.MessageAPI.MessageService;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.helpers.OMSTransDBGenerator;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.parser.OMSConfigDBParser;
import watch.oms.omswatch.parser.OMSDBParserHelper;
import watch.oms.omswatch.parser.OMSServerMapperHelper;
import watch.oms.omswatch.parser.OMSWatchConfigDBParser;

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
Log.d(TAG,"DBManager load()");
		// create config database
		createConfigDataBase();
		OMSApplication.getInstance().setAppId(OMSConstants.APP_ID);
		/*createWatchTransDataBase();
        if(isConfigCreated && isTransCreated){
            receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
        }*/

		// populate data into config database schema
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
		}
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



	public void createTransDB(int appid) {
		Log.i(TAG, "transDB schema - creation is in progress");
		OMSDBParserHelper ctdbHelper = new OMSDBParserHelper();
		String transXmlResponse = ctdbHelper.getTransXML(appid);


		if (OMSConstants.BYPASS_WEBSERVICE_CALL_CONFIGDB){
			createTransDataBase();
			receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
		}else{
			if(!isWatchTransDBExists()){
				if (!TextUtils.isEmpty(transXmlResponse) && (Integer.parseInt(OMSApplication.getInstance()
						.getAppId()) != 10)) {
					Log.i(TAG, "TransXMLResponse:::" + transXmlResponse);
					OMSTransDBGenerator tbHelper = new OMSTransDBGenerator(localContext);
					tbHelper.createTransDataBase(transXmlResponse,Integer.parseInt(OMSApplication.getInstance()
							.getAppId()));
					Log.i(TAG, "transDB schema is created");
				}
			}

            receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
            openTransDBConnection();
			// Skipping TransDataBase data filling for all the tables
			/*if(OMSApplication.getInstance().isEnableTransService()){

				if (!(OMSApplication.getInstance().isGlobalByPass999())) {
					OMSServerMapperHelper serverMapperHelper = new OMSServerMapperHelper();
					String transUrl = serverMapperHelper.getTransURL(ALL_TABLE,
							OMSMessages.GET.getValue());

					if(transUrl.contains("null") || transUrl==null){
						Toast.makeText(localContext, "Target host must not be null.", Toast.LENGTH_LONG).show();
						receiveResult(OMSMessages.TRANS_DB_ERROR.getValue());
					}else{
						if (checkNetworkConnectivity()) {
							Log.i(TAG, "transDB schema - data population starting ..");
							if (Integer.parseInt(OMSApplication.getInstance()
									.getAppId()) == 10) {
								double lastmodifiedTime = 0.0f;
								OMSServerMapperHelper helper = new OMSServerMapperHelper();

								lastmodifiedTime = helper
										.getLastModifiedTimeStamp(OMSApplication
												.getInstance().getEditTextHiddenVal());
								if(OMSConstants.USE_GENERIC_URL) {
									transUrl= OMSConstants.SERVER_NAME+"/AiM/"+OMSApplication.getInstance()
											.getEditTextHiddenVal()+"?"+"modifieddate="+lastmodifiedTime;
									transUrl = OMSConstants.SERVER_NAME+"/AiM/10?"+"modifieddate="+lastmodifiedTime+"&appid="+OMSApplication.getInstance()
											.getEditTextHiddenVal();
								}else{
									transUrl = OMSConstants.SERVER_NAME
											+ "/AiM/service/config/getConfigByAppId/"
											+ OMSApplication.getInstance()
											.getEditTextHiddenVal() + "/" + lastmodifiedTime;
								}
							}


							new OMSTransDBParser(localContext, OMSDBManager.this).execute(transUrl);
						}else{
							Log.d(TAG,
									"OMSTransDBParser.execute - Network not available.Please try to connect after some time");

							receiveResult(OMSMessages.NETWORK_RESPONSE_ERROR.getValue());
						}
					}
				}else{
					try {
						copy999999TransDatabase();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
				}
				OMSApplication.getInstance().setEnableTransService(false);
			}else{*/
				//		openTransDBConnection();
			//	receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
			//}

		//	openTransDBConnection();
		}
		/*if (!(OMSConstants.BYPASS_WEBSERVICE_CALL_CONFIGDB) && !(OMSApplication.getInstance().isGlobalByPass999())) {
			OMSServerMapperHelper serverMapperHelper = new OMSServerMapperHelper();
			String transUrl = serverMapperHelper.getTransURL(ALL_TABLE,
					OMSMessages.GET.getValue());

			if(transUrl.contains("null") || transUrl==null){
				Toast.makeText(localContext, "Target host must not be null.", Toast.LENGTH_LONG).show();
				receiveResult(OMSMessages.TRANS_DB_ERROR.getValue());
			}else{
				Log.i(TAG, "transDB schema - data population starting ..");
				new OMSTransDBParser(localContext, OMSDBManager.this).execute(transUrl);
			}
		}else{
			receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
		}*/

		/*OMSServerMapperHelper serverMapperHelper = new OMSServerMapperHelper();
		String transUrl = serverMapperHelper.getTransURL(ALL_TABLE,
				OMSMessages.GET.getValue());
		Log.i(TAG, "transDB schema - data population starting ..");

		if(transUrl.contains("null") && !OMSApplication.getInstance().isGlobalByPass999()){
			Toast.makeText(localContext, "Target host must not be null.", Toast.LENGTH_LONG).show();
			receiveResult(OMSMessages.TRANS_DB_ERROR.getValue());
		}else{
			new OMSTransDBParser(localContext, OMSDBManager.this).execute(transUrl);
		}*/
	}

	//
    private boolean isLocalTransExist() throws IOException {
        Log.i(TAG,
                "method copyConfigDatabase, copying configDB schema from local to device");
        String transDBName = ((OMSApplication) localContext
                .getApplicationContext()).getAppId()
                + OMSMessages.DB_EXT.getValue();
        try{
            InputStream inputStream = localContext.getAssets().open(transDBName);
            if(inputStream!=null) inputStream.close();
            return true;
        }catch(FileNotFoundException fnf){
            return false;
        }
    }

	private void createTransDataBase() {
		Log.i(TAG, "method createTransDataBase");
		boolean dbExist = isWatchTransDBExists();
		Log.i(TAG, "is trans DB exists- " + dbExist);
		if (dbExist) {
		} else {
//			this.getReadableDatabase();
			try {
				if(isLocalTransExist()){
					copyWatchTransDatabase();
				}
				if (isWatchTransDBExists())
				{
					SharedPreferences.Editor editor= sp.edit();
					openTransDBConnection();
					Cursor c=TransDatabaseUtil.getTransDB().rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'",null);
					//c.moveToFirst();
					while(c.moveToNext()){
						if(!c.getString(0).equalsIgnoreCase("sqlite_sequence") && !c.getString(0).equalsIgnoreCase("android_metadata"))
						{
							Cursor mCursor  = TransDatabaseUtil.getTransDB().rawQuery("pragma table_info('"+c.getString(0)+"')",null);
//							 mCursor.moveToFirst();
							ArrayList<String> colums = new ArrayList<String>();
							while(mCursor.moveToNext()){
								colums.add(mCursor.getString(mCursor.getColumnIndex("name")));
							}
							editor.putStringSet(c.getString(0), new HashSet<String>(colums));

						}

					}
					editor.commit();
				}else{
					OMSDBParserHelper ctdbHelper = new OMSDBParserHelper();
					String transXmlResponse = ctdbHelper.getTransXML(Integer.parseInt(OMSApplication.getInstance().getAppId()));
					if (!TextUtils.isEmpty(transXmlResponse) && (Integer.parseInt(OMSApplication.getInstance()
							.getAppId()) != 10)) {
						Log.i(TAG, "TransXMLResponse:::" + transXmlResponse);
						OMSTransDBGenerator tbHelper = new OMSTransDBGenerator(localContext);
						tbHelper.createTransDataBase(transXmlResponse,Integer.parseInt(OMSApplication.getInstance()
								.getAppId()));
						Log.i(TAG, "transDB schema is created");
						openTransDBConnection();
					}
				}
			} catch (IOException e) {
				Log.e(TAG, "Error occurred in method createTransDB . Error is:"
						+ e.getMessage());
				e.printStackTrace();
			}
		}
		openTransDBConnection();
		receiveResult(OMSMessages.TRANS_DATABASE_SUCCESS.getValue());
	}

	//

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
        OMSApplication.getInstance().setConfigDataAPIURL(configURL);

	//	new OMSConfigDBParser(localContext, OMSDBManager.this).execute(configURL);
        new OMSWatchConfigDBParser(localContext,OMSDBManager.this).callMessageService(configURL);
	}


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


        android.util.Log.d(TAG, "OMS DBManager :: "+result);
        if (result != null){
            if (result.contains(OMSMessages.POLICY_DATABASE_SUCCESS.getValue())) {
                Log.i(TAG, "PolicyDataBase  schema - data population finished");
                if (inReceiveListener != null) {
                    inReceiveListener.receiveResult(result);
                }
            }
            else if (result.contains(OMSMessages.TRANS_DATABASE_SUCCESS.getValue())) {
                Log.i(TAG, "transDB schema - data population finished");
//added for AppGuard
                if (inReceiveListener != null) {
                    inReceiveListener.receiveResult(result);
                }
            } else if (result.contains(OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
                    .getValue())) {
                Log.i(TAG, "ConfigDB schema - data population finished");

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
        }

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









	












	public static SQLiteDatabase getSpecificDB(String schemaName) {
        // If the request schema name is same as current active, the database is
        // already open.
        if (currectActiveSchema.equals(schemaName) && specificDB != null && specificDB.isOpen()) {
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
        }

    }

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
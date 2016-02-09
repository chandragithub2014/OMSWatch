package watch.oms.omswatch.actioncenter.helpers;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;

import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.parser.OMSDBParserHelper;
import watch.oms.omswatch.parser.OMSServerMapperHelper;


public class DataParser {
	
	private final String TAG = this.getClass().getSimpleName();
	
	private OMSServerMapperHelper servermapperhelper;
	Context context;

	public DataParser(Context c) {
		context = c;
	}

	/**
	 * Parses Service response and stores into respective DB table.
	 * 
	 * @param jsonContent
	 */
	public void parseAndUpdate(String jsonContent){

		// Create a Reader from String
		Reader stringReader = new StringReader(jsonContent);		
		
		// Reader pStringReader
		final String ALL_TABLES = "all";
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

		servermapperhelper = new OMSServerMapperHelper();
		if(jsonContent.contains("ProductDetails"))
		{

			String primaryKeyVal = null;
			double timeStamp = 0.0;
			JSONObject jsonObject = null;
			JSONArray jsonArray = null;
			JSONObject tableJSON = null;
			Iterator<String> tableIterator = null;
			String servertableName = null;
			int updateId = OMSDefaultValues.NONE_DEF_CNT.getValue();
			String columnName = null;
			String colVal = null;
			int insertedRowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
			Iterator<String> rootIterator = null;
			String result = "BLGetFailed";
			Set cols= new HashSet<String>();
			int responseKeyCount = 0;
			OMSDBParserHelper configDBParserHelper = new OMSDBParserHelper();
			SharedPreferences sp = context.getSharedPreferences(
					"TRANS_DB_COLS", 0);
			try {
				jsonObject = new JSONObject(jsonContent);
	            if(jsonObject.has("visiteddate")){
					
					latestModifiedTimeStamp = jsonObject.getDouble("visiteddate");
				}

				//latestModifiedTimeStamp = jsonObject.getDouble("visiteddate");
				
				responseKeyCount = jsonObject.length();
				
				rootIterator = jsonObject.keys();
				while (rootIterator.hasNext()) {
					try {
						servertableName = (String) rootIterator.next();
						if(!servertableName.equalsIgnoreCase("visiteddate") && !servertableName.equalsIgnoreCase("error")){
							JSONObject shipmentDetailsString = null;
									jsonArray = jsonObject.getJSONArray(servertableName);
								if (jsonArray != null) {
									
									
									cols = sp.getStringSet(servertableName, cols);
									
									for (int i = 0; i < jsonArray.length(); i++) {
										ContentValues contentvalues = new ContentValues();
										tableJSON = jsonArray.getJSONObject(i);
										tableIterator = tableJSON.keys();
										while (tableIterator.hasNext()) {
											columnName = (String) tableIterator.next();
											if (columnName
													.equalsIgnoreCase(OMSDatabaseConstants.UNIQUE_ROW_ID)) {
												primaryKeyVal = tableJSON
														.getString(columnName);
												contentvalues.put(columnName, primaryKeyVal);
											} else if (columnName
													.equalsIgnoreCase(OMSDatabaseConstants.MODIFIED_DATE)) {
												timeStamp = tableJSON.getDouble(columnName);
												contentvalues.put(columnName, timeStamp);
											} else {
												if(cols!=null && cols.contains(columnName)){
													colVal = tableJSON.getString(columnName);
													contentvalues.put(columnName, colVal.trim());
												}else{
													Log.d(TAG, "skipping server column:" + columnName);
												}
											}
			
										}
										//Oasis Tables only
										if(servertableName.equalsIgnoreCase("ProductDetails") )
											contentvalues.put("isdelete", 0);	
										insertedRowId = configDBParserHelper
												.insertOrUpdateTransDB(servertableName,
														contentvalues);
										Log.d(TAG, "Inserted into " + servertableName
												+ " insert value : " + insertedRowId);
									}
									
									updateId = servermapperhelper
											.updateModifiedTimeStampForTransTable(
													tableName, latestModifiedTimeStamp);
			
									result = "BLGetSuccess";
									if (updateId == OMSDefaultValues.NONE_DEF_CNT.getValue()) {
										Log.e(TAG,
												"Failed to Upadate Modified Date with Max TimeStamp into Table :["
														+ tableName + "]");
									}
								}
							}
						} catch (JSONException e) {

						Log.e(TAG,
								"Exception occurred while parsing the Trans Service response."
										+ e.getMessage());
					  	result ="BLGetFailed";
						e.printStackTrace();
						continue;
					}
				}

			} catch (JSONException e) {
				Log.e(TAG,
						"Exception occurred while parsing the Trans Service response."
								+ e.getMessage());
				e.printStackTrace();
			 	result ="BLGetFailed";
			} catch (ParseException e) {
				Log.e(TAG,
						"ParseException occurred while parsing the transDB."
								+ e.getMessage());
			 	result ="BLGetFailed";
				e.printStackTrace();
			}
			
			if(responseKeyCount==1 && latestModifiedTimeStamp>0) result = "BLGetSuccess";
		
		}
		else 
		{
			try {
				Log.d(TAG, "@@@@@@@@@@ Trans DB Tables Start @@@@@@@@@@");
				reader = new JsonReader(stringReader);
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

			OMSDBParserHelper configDBParserHelper  = new OMSDBParserHelper();
			
			// In case of custom tables, clear default values
			if (servermapperhelper.getClientTableName(tableName) == null) {
				Log.d(TAG,"Deleting default rows for table - " + tableName);
				configDBParserHelper.deleteAllDefaultRows(tableName);
			}
			for (ContentValues contentValues : contentValuesList) {
				configDBParserHelper.insertOrUpdateTransDB(tableName,
						contentValues);
			}

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
	
	private String transDBParser(String response, String tableName) {
		String primaryKeyVal = null;
		double timeStamp = 0.0;
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		JSONObject tableJSON = null;
		Iterator<String> tableIterator = null;
		String servertableName = null;
		int updateId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		double latestModifiedTimeStamp = 0.0;
		String colName = null;
		String colVal = null;
		int insertedRowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Iterator<String> rootIterator = null;
		String result = "BLGetFailed";
		Set cols= new HashSet<String>();
		int responseKeyCount = 0;
		OMSDBParserHelper configDBParserHelper = new OMSDBParserHelper();
		SharedPreferences sp = context.getSharedPreferences(
				"TRANS_DB_COLS", 0);
		try {
			jsonObject = new JSONObject(response);
            if(jsonObject.has("visiteddate")){
				
				latestModifiedTimeStamp = jsonObject.getDouble("visiteddate");
			}

			//latestModifiedTimeStamp = jsonObject.getDouble("visiteddate");
			
			responseKeyCount = jsonObject.length();
			
			rootIterator = jsonObject.keys();
			while (rootIterator.hasNext()) {
				try {
					servertableName = (String) rootIterator.next();
					if(!servertableName.equalsIgnoreCase("visiteddate") && !servertableName.equalsIgnoreCase("error")){
						JSONObject shipmentDetailsString = null;
							/*if(servertableName.equalsIgnoreCase("ShipmentDetails"))
							{
								shipmentDetailsString = jsonObject.getJSONObject(servertableName);
								if(shipmentDetailsString!=null)
								{
									JSONArray tempJsonArray = new JSONArray();
									tempJsonArray.put(shipmentDetailsString);
									jsonObject.put(servertableName, tempJsonArray);
									//shipmentDetailsString= "["+shipmentDetailsString+"]";
								}
									jsonArray = jsonObject.getJSONArray(servertableName);
							}
							else*/
								jsonArray = jsonObject.getJSONArray(servertableName);
							/*tableName = servermapperhelper
									.getClientTableName(servertableName);*/
							if (jsonArray != null) {
								
								
								cols = sp.getStringSet(servertableName, cols);
								
								for (int i = 0; i < jsonArray.length(); i++) {
									ContentValues contentvalues = new ContentValues();
									tableJSON = jsonArray.getJSONObject(i);
									tableIterator = tableJSON.keys();
									while (tableIterator.hasNext()) {
										colName = (String) tableIterator.next();
										if (colName
												.equalsIgnoreCase(OMSDatabaseConstants.UNIQUE_ROW_ID)) {
											primaryKeyVal = tableJSON
													.getString(colName);
											contentvalues.put(colName, primaryKeyVal);
										} else if (colName
												.equalsIgnoreCase(OMSDatabaseConstants.MODIFIED_DATE)) {
											timeStamp = tableJSON.getDouble(colName);
											contentvalues.put(colName, timeStamp);
										} else {
											if(cols!=null && cols.contains(colName)){
												colVal = tableJSON.getString(colName);
												contentvalues.put(colName, colVal.trim());
											}else{
												Log.d(TAG, "skipping server column:"+colName);
											}
										}
		
									}
									//Oasis Tables only
									if(servertableName.equalsIgnoreCase("BillToDetails")
											||servertableName.equalsIgnoreCase("OrdSearchCriteriaList")
											||servertableName.equalsIgnoreCase("OrderDetails")
											||servertableName.equalsIgnoreCase("OrderSearch")
											||servertableName.equalsIgnoreCase("OrderTitles")
											||servertableName.equalsIgnoreCase("Orders")
											||servertableName.equalsIgnoreCase("DeliveryInfo")
											||servertableName.equalsIgnoreCase("ShipmentDetails")
											||servertableName.equalsIgnoreCase("AccountShipToDetail"))
										contentvalues.put("isdelete", 0);	
									insertedRowId = configDBParserHelper
											.insertOrUpdateTransDB(servertableName,
													contentvalues);
									Log.d(TAG, "Inserted into " + servertableName
											+ " insert value : " + insertedRowId);
								}
								
								updateId = servermapperhelper
										.updateModifiedTimeStampForTransTable(
												tableName, latestModifiedTimeStamp);
		
								result = "BLGetSuccess";
								if (updateId == OMSDefaultValues.NONE_DEF_CNT.getValue()) {
									Log.e(TAG,
											"Failed to Upadate Modified Date with Max TimeStamp into Table :["
													+ tableName + "]");
								}
							}
						}
					} catch (JSONException e) {

					Log.e(TAG,
							"Exception occurred while parsing the Trans Service response."
									+ e.getMessage());
				  	result ="BLGetFailed";
					e.printStackTrace();
					continue;
				}
			}

		} catch (JSONException e) {
			Log.e(TAG,
					"Exception occurred while parsing the Trans Service response."
							+ e.getMessage());
			e.printStackTrace();
		 	result ="BLGetFailed";
		} catch (ParseException e) {
			Log.e(TAG,
					"ParseException occurred while parsing the transDB."
							+ e.getMessage());
		 	result ="BLGetFailed";
			e.printStackTrace();
		}
		
		if(responseKeyCount==1 && latestModifiedTimeStamp>0) result = "BLGetSuccess";
		return result;
	}
	
}

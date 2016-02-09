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
package watch.oms.omswatch.actioncenter.blexecutor.helper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;


import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLColumnDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLIUDTO;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLGlobalValueGenerator;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

public class BLIUAsyncTaskHelper extends AsyncTask<String, Void, String> {

	private final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private OMSReceiveListener rListener;
	private String uniqueId = null;
	private List<BLIUDTO> IUList;
	private List<String> transUsidList;
	private String destinationColumnIndex = null;
	private boolean isForceUpdate;
	BLGlobalValueGenerator blGlobalValGenerator;
	private boolean hasUsidInDestination;
	public BLIUAsyncTaskHelper(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList) {
		context = FragmentContext;
		rListener = receiveListener;
		this.transUsidList =  new ArrayList<String>(transUsidList);
		blGlobalValGenerator = new BLGlobalValueGenerator();

	}

	@Override
	protected String doInBackground(String... args) {
		int res = OMSDefaultValues.NONE_DEF_CNT.getValue();
		String xml_file = args[0];
		res = processIUBL(xml_file);
		return Integer.toString(res);
	}

	private int processIUBL(String targetXml) {
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			try {
				IUList = xmlParser.parseIUXML(response);
				if (IUList != null) {
					/*
					 * if(sourceTableName.equalsIgnoreCase(destinationTableName))
					 * { processIUList1(IUList); } else
					 */{
						result = processIUList(IUList);
					}
				}
			} catch (XmlPullParserException e) {
				Log.e(TAG, "Error Occured in processIUBL:" + e.getMessage());
				e.printStackTrace();
			}

		} catch (IOException e) {
			Log.e(TAG, "Error Occured in processIUBL:" + e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			Log.e(TAG, "Error Occured in processIUBL:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	private int processIUList(List<BLIUDTO> iUList2) {
		
		List<BLColumnDTO> targetColList = new ArrayList<BLColumnDTO>();
		Hashtable<String, String> targetSourceColumnNamesHashList = new Hashtable<String, String>();
		Object primaryKeyVal = null;
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		// ContentValues cv = new ContentValues();
		try{
		String sourceTableName = iUList2.get(0).sourceTableName;
		String destinationTableName = iUList2.get(0).destinationTableName;
		String destinationprimaryKey = iUList2.get(0).destinationPrimaryKey;
		String destinationprimaryKeyType = iUList2.get(0).destinationPrimaryKeyType;
		String sourcePrimaryKey = iUList2.get(0).sourcePrimaryKey;
		String matchedSourceDestinationPrimaryKey = null;
		isForceUpdate = iUList2.get(0).isForceUpdate;
		String sourceWhere=iUList2.get(0).sourceWhere;
		String destWhere = iUList2.get(0).destinationWhere;
		String sourceWhereType="TEXT";
		//String destinationWhereType="TEXT";
		Object sourceWhereVal="";
		if(sourcePrimaryKey.equalsIgnoreCase("forceupdate")){
			isForceUpdate=true;
		}
		
     /*  if(sourceTableName.equalsIgnoreCase("cartheader") && destinationTableName.equalsIgnoreCase("userdetails")){
    	   isForceUpdate=true;
       }*/
		List<BLColumnDTO> destinationColList = iUList2.get(0).destinationColList;
		List<BLColumnDTO> sourceColList = iUList2.get(0).sourceColList;
		String sourceSchemaName = iUList2.get(0).sourceSchemaName;
		String destinationSchemaName = iUList2.get(0).destinationSchemaName;
		
		if(sourceWhere!=null && sourceWhere.length()>0) {
			Cursor PragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
					"pragma table_info(" + sourceTableName + ");", null);
			if(PragmaCursor.getCount() > 0){
				if (PragmaCursor.moveToFirst()) {
				do {
					String columnName = PragmaCursor
							.getString(PragmaCursor
									.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
					if (columnName.equalsIgnoreCase(sourceWhere)) {
						sourceWhereType = PragmaCursor
								.getString(PragmaCursor
										.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
					}
				} while (PragmaCursor.moveToNext());
				}
			}
		}
		
		
		/*if(destWhere!=null && destWhere.length()>0) {
			Cursor PragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
					"pragma table_info(" + destinationTableName + ");", null);
			if(PragmaCursor.getCount() > 0){
				if (PragmaCursor.moveToFirst()) {
				do {
					String columnName = PragmaCursor
							.getString(PragmaCursor
									.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
					if (columnName.equalsIgnoreCase(destWhere)) {
						destinationWhereType = PragmaCursor
								.getString(PragmaCursor
										.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
					}
				} while (PragmaCursor.moveToNext());
				}
			}
		}*/
		
		
		if (destinationprimaryKeyType == null) {
			Cursor PragmaCursor =  OMSDBManager.getSpecificDB(destinationSchemaName).rawQuery(
					"pragma table_info(" + destinationTableName + ");", null);
			if (PragmaCursor.getCount() > 0) {
				if (PragmaCursor.moveToFirst()) {
					do {
						String columnName = PragmaCursor
								.getString(PragmaCursor
										.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
						if (columnName.equalsIgnoreCase(destinationprimaryKey)) {
							destinationprimaryKeyType = PragmaCursor
									.getString(PragmaCursor
											.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
						}
					} while (PragmaCursor.moveToNext());
				}

			}
		}
		
		


		if (destinationprimaryKey != null) {
			if (destinationColList != null && destinationColList.size() > 0) {
				for (int i = 0; i < destinationColList.size(); i++) {
					if (destinationColList.get(i).columnName
							.equalsIgnoreCase(destinationprimaryKey)) {
						destinationColumnIndex = destinationColList.get(i).columnIndex;
					}
				}
			}
			if (destinationColumnIndex != null) {
				if (sourceColList != null && sourceColList.size() > 0) {
					for (int i = 0; i < sourceColList.size(); i++) {
						if (sourceColList.get(i).columnIndex
								.equalsIgnoreCase(destinationColumnIndex)) {
							matchedSourceDestinationPrimaryKey = sourceColList
									.get(i).columnName;
						}
					}
				}
			}
		}

		if (sourcePrimaryKey != null
				&& sourcePrimaryKey.equalsIgnoreCase("bulk")) {
			if (sourceTableName != null && sourceTableName.length() > 0
					&& sourceSchemaName != null && sourceTableName.length() > 0) {
				if (transUsidList != null) {
					transUsidList.clear();
					transUsidList = new ArrayList<String>();
				}
				Cursor sourceCursor1 = OMSDBManager
						.getSpecificDB(sourceSchemaName)
						.rawQuery(
								"select usid from "
										+ sourceTableName
										+ " where "
										+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
										+ " <> '1' LIMIT 0,5001", null);
				// .query(sourceTableName,
				// new String[] {"usid"} ,
				// OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
				// + " <> '1', null, null, null, null);
				try {
					if (sourceCursor1 != null && sourceCursor1.getCount() > 0)
						if (sourceCursor1.moveToFirst()) {
							do {
								transUsidList.add(sourceCursor1
										.getString(sourceCursor1
												.getColumnIndex("usid")));
							} while (sourceCursor1.moveToNext());
						}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sourceCursor1.close();
				}

			}
		}

		for (int j = 0; j < sourceColList.size(); j++) {
			for (int i = 0; i < destinationColList.size(); i++) {
				if (sourceColList.get(j).columnIndex
						.equalsIgnoreCase(destinationColList.get(i).columnIndex)) {
					targetColList.add(destinationColList.get(i));
					// targetSourceColumnNameList.add(sourceColList.get(i));
					targetSourceColumnNamesHashList.put(
							sourceColList.get(j).columnIndex,
							sourceColList.get(j).columnName);

				}
			}
		}
		
		
		for(int i=0;i<targetColList.size();i++){
			if(targetColList.get(i).columnName.equalsIgnoreCase("usid")){
				hasUsidInDestination=true;
			}
		}
		
		Cursor PragmaCursor1 = OMSDBManager.getSpecificDB(sourceSchemaName)
				.rawQuery("pragma table_info(" + sourceTableName + ");", null);

		for (int i = 0; i < targetColList.size(); i++) {

			if (PragmaCursor1.getCount() > 0) {
				if (PragmaCursor1.moveToFirst()) {
					do {
						String columnName = PragmaCursor1
								.getString(PragmaCursor1
										.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
						if(sourceColList.get(i).isGlobal){
							sourceColList.get(i).columnType = "globalIU";
						}
						else if (columnName
								.equalsIgnoreCase(sourceColList.get(i).columnName)) {
							sourceColList.get(i).columnType = PragmaCursor1
									.getString(PragmaCursor1
											.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
						} else if(sourceColList.get(i).columnName.equalsIgnoreCase("") && sourceColList.get(i).columnValue!=null &&sourceColList.get(i).columnValue.length()>0){
							sourceColList.get(i).columnType = "constant";
						}
					} while (PragmaCursor1.moveToNext());
				}

			}
		}
		ContentValues tempContentValues = null;
		for (int k = 0; k < transUsidList.size(); k++) {
			tempContentValues = new ContentValues();
			if (targetColList != null && targetColList.size() > 0) {
				uniqueId = transUsidList.get(k);
				primaryKeyVal = uniqueId;
				Log.i(TAG, " transUsidList index : " + k + " |  uniqueId : "
						+ uniqueId);
				Cursor sourceCursor = null;
				try {

					sourceCursor = OMSDBManager.getSpecificDB(sourceSchemaName)
							.query(sourceTableName,
									null,
									OMSDatabaseConstants.UNIQUE_ROW_ID + "='"
											+ uniqueId + "'"+ "and "
													+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
													+ " <> '1'", null, null, null,
									null);

					if (sourceCursor.moveToFirst()) {
						if (destinationprimaryKeyType
								.equalsIgnoreCase(OMSMessages.INTEGER
										.getValue())) {
							primaryKeyVal = sourceCursor.getInt(sourceCursor
									.getColumnIndex(destinationprimaryKey));
						} else if (destinationprimaryKeyType
								.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || destinationprimaryKeyType.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
							primaryKeyVal = sourceCursor.getString(sourceCursor
									.getColumnIndex(destinationprimaryKey));
						} else if (destinationprimaryKeyType
								.equalsIgnoreCase(OMSMessages.REAL_CAPS
										.getValue())) {
							primaryKeyVal = sourceCursor.getDouble(sourceCursor
									.getColumnIndex(destinationprimaryKey));
						}
						if (matchedSourceDestinationPrimaryKey != null) {
							if (destinationprimaryKeyType
									.equalsIgnoreCase(OMSMessages.INTEGER
											.getValue())) {
								primaryKeyVal = sourceCursor
										.getInt(sourceCursor
												.getColumnIndex(matchedSourceDestinationPrimaryKey));
							} else if (destinationprimaryKeyType
									.equalsIgnoreCase(OMSMessages.TEXT
											.getValue()) || destinationprimaryKeyType.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
								primaryKeyVal = sourceCursor
										.getString(sourceCursor
												.getColumnIndex(matchedSourceDestinationPrimaryKey));
							} else if (destinationprimaryKeyType
									.equalsIgnoreCase(OMSMessages.REAL_CAPS
											.getValue())) {
								primaryKeyVal = sourceCursor
										.getDouble(sourceCursor
												.getColumnIndex(matchedSourceDestinationPrimaryKey));
							}
						} else {
							if (destinationprimaryKeyType
									.equalsIgnoreCase(OMSMessages.INTEGER
											.getValue())) {
								primaryKeyVal = sourceCursor
										.getInt(sourceCursor
												.getColumnIndex(destinationprimaryKey));
							} else if (destinationprimaryKeyType
									.equalsIgnoreCase(OMSMessages.TEXT
											.getValue()) || destinationprimaryKeyType.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
								primaryKeyVal = sourceCursor
										.getString(sourceCursor
												.getColumnIndex(destinationprimaryKey));
							} else if (destinationprimaryKeyType
									.equalsIgnoreCase(OMSMessages.REAL_CAPS
											.getValue())) {
								primaryKeyVal = sourceCursor
										.getDouble(sourceCursor
												.getColumnIndex(destinationprimaryKey));
							} else {
								primaryKeyVal = sourceCursor
										.getString(sourceCursor
												.getColumnIndex(destinationprimaryKey));
							}

						}
                        
					//	write code
						if(sourceWhere!=null && sourceWhere.length()>0) {
							if (sourceWhereType
									.equalsIgnoreCase(OMSMessages.INTEGER
											.getValue())) {
								sourceWhereVal = sourceCursor
										.getInt(sourceCursor
												.getColumnIndex(sourceWhere));
							} else if (sourceWhereType
									.equalsIgnoreCase(OMSMessages.TEXT
											.getValue()) || sourceWhereType
											.equalsIgnoreCase(OMSMessages.BIGINT
													.getValue())) {
								sourceWhereVal = sourceCursor
										.getString(sourceCursor
												.getColumnIndex(sourceWhere));
							} else if (sourceWhereType
									.equalsIgnoreCase(OMSMessages.REAL_CAPS
											.getValue())) {
								sourceWhereVal = sourceCursor
										.getDouble(sourceCursor
												.getColumnIndex(sourceWhere));
							} else {
								sourceWhereVal = sourceCursor
										.getString(sourceCursor
												.getColumnIndex(sourceWhere));
							}
						}
						// String srcColType = null;
						for (int i = 0; i < targetColList.size(); i++) {

							/*
							 * if (PragmaCursor1.getCount() > 0) {
							 * if(PragmaCursor1.moveToFirst()){ do{ String
							 * columnName =
							 * PragmaCursor1.getString(PragmaCursor1
							 * .getColumnIndex
							 * (OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
							 * if(columnName
							 * .equalsIgnoreCase(sourceColList.get(i
							 * ).columnName)){ srcColType =
							 * PragmaCursor1.getString(PragmaCursor1
							 * .getColumnIndex
							 * (OMSDatabaseConstants.PRAGMA_COLUMN_NAME2)); }
							 * }while(PragmaCursor1.moveToNext()); }
							 * 
							 * }
							 */
							// +targetColList.get(i).columnName+" | column type:"+sourceColList.get(i).columnType);
							
							if (sourceColList.get(i).columnType != null) {
								if (sourceColList.get(i).columnType
										.equalsIgnoreCase(OMSMessages.TEXT
												.getValue()) || sourceColList.get(i).columnType
												.equalsIgnoreCase(OMSMessages.BIGINT
														.getValue())) {
								if(	sourceColList.get(i).columnValue!=null &&  sourceColList.get(i).columnValue.length()>0) {
									tempContentValues
									.put(targetColList.get(i).columnName,
											sourceColList.get(i).columnValue);
								}
								else{	tempContentValues
											.put(targetColList.get(i).columnName,
													sourceCursor
															.getString(sourceCursor
																	.getColumnIndex(targetSourceColumnNamesHashList
																			.get(targetColList
																					.get(i).columnIndex))));
								}
								} else if (sourceColList.get(i).columnType
										.equalsIgnoreCase(OMSMessages.INTEGER
												.getValue())) {
									if(	sourceColList.get(i).columnValue!=null &&  sourceColList.get(i).columnValue.length()>0) {
										tempContentValues
										.put(targetColList.get(i).columnName,
												sourceColList.get(i).columnValue);
									}else{
									tempContentValues
											.put(targetColList.get(i).columnName,
													sourceCursor
															.getInt(sourceCursor
																	.getColumnIndex(targetSourceColumnNamesHashList
																			.get(targetColList
																					.get(i).columnIndex))));
									}
								} else if (sourceColList.get(i).columnType
										.equalsIgnoreCase(OMSMessages.REAL
												.getValue())) {
									if(	sourceColList.get(i).columnValue!=null &&  sourceColList.get(i).columnValue.length()>0) {
										tempContentValues
										.put(targetColList.get(i).columnName,
												sourceColList.get(i).columnValue);
									}else{
									tempContentValues
											.put(targetColList.get(i).columnName,
													sourceCursor
															.getDouble(sourceCursor
																	.getColumnIndex(targetSourceColumnNamesHashList
																			.get(targetColList
																					.get(i).columnIndex))));

									}

								}else if (sourceColList.get(i).columnType
										.equalsIgnoreCase("globalIU")) {
									 String globalVal = blGlobalValGenerator.getGlobalBLValue(sourceColList.get(i).columnName);
									 tempContentValues
										.put(targetColList.get(i).columnName,
												globalVal);
								}
								else {
									if(	sourceColList.get(i).columnValue!=null &&  sourceColList.get(i).columnValue.length()>0) {
										tempContentValues
										.put(targetColList.get(i).columnName,
												sourceColList.get(i).columnValue);
									}else{
									tempContentValues
									.put(targetColList.get(i).columnName,
											sourceCursor
													.getString(sourceCursor
															.getColumnIndex(targetSourceColumnNamesHashList
																	.get(targetColList
																			.get(i).columnIndex))));
									}

								}
							}
						}
					} else {
						sourceCursor = null;
						tempContentValues = new ContentValues();
						sourceCursor = OMSDBManager.getSpecificDB(
								sourceSchemaName).query(sourceTableName, null,
										OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
										+ " <> '1'", null, null, null, null);
						if (sourceCursor.moveToFirst()) {
							for (int i = 0; i < targetColList.size(); i++) {
								// String srcColType = null;
								/*
								 * if (PragmaCursor1.getCount() > 0) {
								 * if(PragmaCursor1.moveToFirst()){ do{ String
								 * columnName =
								 * PragmaCursor1.getString(PragmaCursor1
								 * .getColumnIndex
								 * (OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
								 * if
								 * (columnName.equalsIgnoreCase(targetColList.get
								 * (i).columnName)){ srcColType =
								 * PragmaCursor1.getString(PragmaCursor1
								 * .getColumnIndex
								 * (OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
								 * System
								 * .out.println("srcColType : "+srcColType); }
								 * }while(PragmaCursor1.moveToNext()); }
								 * 
								 * }
								 */
								if (sourceColList.get(i).columnType != null)
									if (sourceColList.get(i).columnType
											.equalsIgnoreCase(OMSMessages.TEXT
													.getValue()) || sourceColList.get(i).columnType
													.equalsIgnoreCase(OMSMessages.BIGINT
															.getValue())) {
										if(	sourceColList.get(i).columnValue!=null &&  sourceColList.get(i).columnValue.length()>0) {
											tempContentValues
											.put(targetColList.get(i).columnName,
													sourceColList.get(i).columnValue);
											//get sourceWhereVal from sourceColList to filter the records
											if(targetColList.get(i).columnName.equalsIgnoreCase(sourceWhere)){
												sourceWhereVal = sourceColList.get(i).columnValue;
											}
										}else{
										tempContentValues
												.put(targetColList.get(i).columnName,
														sourceCursor
																.getString(sourceCursor
																		.getColumnIndex(targetSourceColumnNamesHashList
																				.get(targetColList
																						.get(i).columnIndex))));
										if(targetColList.get(i).columnName.equalsIgnoreCase(sourceWhere)){
											sourceWhereVal = sourceCursor
													.getString(sourceCursor
															.getColumnIndex(targetSourceColumnNamesHashList
																	.get(targetColList
																			.get(i).columnIndex)));
										}
										}
									} else if (sourceColList.get(i).columnType
											.equalsIgnoreCase(OMSMessages.INTEGER
													.getValue())) {
										if(	sourceColList.get(i).columnValue!=null &&  sourceColList.get(i).columnValue.length()>0) {
											tempContentValues
											.put(targetColList.get(i).columnName,
													sourceColList.get(i).columnValue);
										}else{
										tempContentValues
												.put(targetColList.get(i).columnName,
														sourceCursor
																.getInt(sourceCursor
																		.getColumnIndex(targetSourceColumnNamesHashList
																				.get(targetColList
																						.get(i).columnIndex))));
										}
									} else if (sourceColList.get(i).columnType
											.equalsIgnoreCase(OMSMessages.REAL
													.getValue())) {
										if(	sourceColList.get(i).columnValue!=null &&  sourceColList.get(i).columnValue.length()>0) {
											tempContentValues
											.put(targetColList.get(i).columnName,
													sourceColList.get(i).columnValue);
										}else{
										tempContentValues
												.put(targetColList.get(i).columnName,
														sourceCursor
																.getDouble(sourceCursor
																		.getColumnIndex(targetSourceColumnNamesHashList
																				.get(targetColList
																						.get(i).columnIndex))));

										}

									}else if (sourceColList.get(i).columnType
											.equalsIgnoreCase("globalIU")) {
										 String globalVal = blGlobalValGenerator.getGlobalBLValue(sourceColList.get(i).columnName);
										 tempContentValues
											.put(targetColList.get(i).columnName,
													globalVal);
									 
									}else {
										if(	sourceColList.get(i).columnValue!=null &&  sourceColList.get(i).columnValue.length()>0) {
											tempContentValues
											.put(targetColList.get(i).columnName,
													sourceColList.get(i).columnValue);
										}else{
										tempContentValues
										.put(targetColList.get(i).columnName,
												sourceCursor
														.getString(sourceCursor
																.getColumnIndex(targetSourceColumnNamesHashList
																		.get(targetColList
																				.get(i).columnIndex))));
										}

									}
							}
						}
					}
					/*
					 * contentValuesList.add(tempContentValues);
					 * 
					 * if(k%100 ==0) { result =
					 * insertOrUpdateBulk(destinationTableName,
					 * destinationSchemaName, contentValuesList);
					 * contentValuesList.clear(); } else
					 * if(transUsidList.size()<=99) { result =
					 * insertOrUpdateDestinationTable(destinationTableName,
					 * destinationprimaryKey, primaryKeyVal, tempContentValues,
					 * destinationSchemaName); contentValuesList.clear(); }
					 */

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (sourceCursor != null)
						sourceCursor.close();
				}
				// sourceCursor.close();

			}
			Log.i(TAG, " tempContentValues : "+tempContentValues);
      if(destWhere!=null && destWhere.length()>0){
    	  result = insertOrUpdateDestinationTableWithWhere(destinationTableName, destWhere, sourceWhereVal, tempContentValues, destinationSchemaName);
      }
      else{
    	  result = insertOrUpdateDestinationTable(destinationTableName,
      
					destinationprimaryKey, primaryKeyVal, tempContentValues,
					destinationSchemaName);
      }
		}
		// result = insertOrUpdateBulk(destinationTableName,
		// destinationSchemaName, contentValuesList);
	}catch(SQLiteException e){
			Log.e(TAG, "SQLiteException"+e.getMessage());
		}catch(Exception e){
			Log.e(TAG, "Exception"+e.getMessage());
		}
		return result;
	}

	private int insertOrUpdateDestinationTable(String destinationTable,
			String pkname, Object pkval, ContentValues cval, String schemaName) {
		Log.i(TAG, "Content Values :" + cval);
		int insertedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try{
		/*if(!hasUsidInDestination){
			Calendar cal = Calendar.getInstance();
			String newUSID = Long.toString(cal.getTimeInMillis());
			cval.put(OMSDatabaseConstants.UNIQUE_ROW_ID, newUSID);
		}*/
		if(!cval.containsKey("status"))
			cval.put("status", OMSDatabaseConstants.STATUS_TYPE_NEW);
		
		if(pkname!=null && pkval!=null){
			cval.put(pkname, (String) pkval);
		}

		try {
			if(isForceUpdate){
				Cursor forceUpdateCursor = OMSDBManager.getSpecificDB(schemaName).query(destinationTable, null,  OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
															 + " <> '1'", null,null,null,null);
				if(forceUpdateCursor.moveToFirst()) {
					String usid = forceUpdateCursor.getString(forceUpdateCursor.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));
					if(usid!=null){
						pkname=OMSDatabaseConstants.UNIQUE_ROW_ID;
						pkval=usid;
						cval.put(pkname, (String) pkval);
					}
				}
			}
			
			Cursor destinationCursor = null;
			
			
			destinationCursor = OMSDBManager.getSpecificDB(schemaName)
					.query(destinationTable, null,
							pkname + "=" + "'" + pkval + "'"
															 + "AND " +
															 OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
															 + " <> '1'"
															 , null, null,
							null, null);

			if (destinationCursor.moveToFirst()) {
				insertedId = (int) OMSDBManager.getSpecificDB(schemaName)
						.update(destinationTable, cval, pkname + "= ? ",
								new String[] { pkval.toString() });
			} else {
				if(!hasUsidInDestination){
					Calendar cal = Calendar.getInstance();
					String newUSID = Long.toString(cal.getTimeInMillis());
					cval.put(OMSDatabaseConstants.UNIQUE_ROW_ID, newUSID);
				}
				Calendar juliancal = Calendar.getInstance();
				Long timeinmillis = juliancal.getTimeInMillis();
				cval.put(OMSDatabaseConstants.ACTION_QUEUE_MODIFIED_DATE,timeinmillis);
				cval.put(OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE, 0);
				//String readableDate = OMSAlertDialog.getReadableDate(timeinmillis);
				/*if(!TextUtils.isEmpty(readableDate)){
					cval.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, readableDate);
				}else{
					cval.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, "");
				}*/
				insertedId = (int) OMSDBManager.getSpecificDB(schemaName)
						.insertWithOnConflict(destinationTable, null, cval,SQLiteDatabase.CONFLICT_IGNORE);
			}
			destinationCursor.close();
		} catch (SQLException e) {
			Log.e(OMSDatabaseConstants.BL_TYPE_IU,
					"Error occurred in method insertOrUpdateDestinationTable for input parameter destinationTable["
							+ destinationTable
							+ "],pkname["
							+ pkname
							+ "],pkval["
							+ pkval
							+ "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		Log.i(TAG, "insertOrUpdateDestinationTable () : insertedId :"
				+ insertedId);
	}catch(SQLiteException e){
		Log.e(TAG, "SQLiteException"+e.getMessage());
	}catch(Exception e){
		Log.e(TAG, "Exception"+e.getMessage());
	}
		return insertedId;
	}

	
	private int insertOrUpdateDestinationTableWithWhere(String destinationTable,
			String whereColumnName, Object whereColVal, ContentValues cval, String schemaName) {
		int insertedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Object newWhereColval = whereColVal;
		Log.i(TAG, "Content Values :" + cval);
		try {
		if(!cval.containsKey("status"))
			cval.put("status", OMSDatabaseConstants.STATUS_TYPE_NEW);
		
		if(whereColumnName!=null && whereColVal!=null && !TextUtils.isEmpty(whereColumnName)&&!TextUtils.isEmpty(""+whereColVal)){
			/*if(whereColVal instanceof Integer) {
				Integer convertedWhere= (Integer)whereColVal;
				cval.put(whereColumnName, Integer.toString(convertedWhere));
				
			}else{*/
			cval.put(whereColumnName, (String)""+newWhereColval);
			//}
		}

		
			if(isForceUpdate){
				Cursor forceUpdateCursor = OMSDBManager.getSpecificDB(schemaName).query(destinationTable, null,  OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
															 + " <> '1'", null,null,null,null);
				if(forceUpdateCursor.moveToFirst()) {
					String usid = forceUpdateCursor.getString(forceUpdateCursor.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));
					if(usid!=null){
						whereColumnName=OMSDatabaseConstants.UNIQUE_ROW_ID;
						newWhereColval=usid;
						cval.put(whereColumnName, (String)""+usid);
					}
				}
			}
			
			Cursor destinationCursor = null;
			
			
			destinationCursor = OMSDBManager.getSpecificDB(schemaName)
					.query(destinationTable, null,
							whereColumnName + "=" + "'" + newWhereColval + "'"
															 + "AND " +
															 OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
															 + " <> '1'"
															 , null, null,
							null, null);

			if (destinationCursor.moveToFirst()) {
				insertedId = (int) OMSDBManager.getSpecificDB(schemaName)
						.update(destinationTable, cval, whereColumnName + "= ? ",
								new String[] { newWhereColval.toString() });
			} else {
				if(!hasUsidInDestination){
					Calendar cal = Calendar.getInstance();
					String newUSID = Long.toString(cal.getTimeInMillis());
					cval.put(OMSDatabaseConstants.UNIQUE_ROW_ID, newUSID);
				}
				//Calendar juliancal = Calendar.getInstance();
				//Long timeinmillis = juliancal.getTimeInMillis();
				cval.put(OMSDatabaseConstants.ACTION_QUEUE_MODIFIED_DATE,String.valueOf(getJulDate()));
				cval.put(OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE, 0);
				//String readableDate = OMSAlertDialog.getReadableDate(timeinmillis);
				/*if(!TextUtils.isEmpty(readableDate)){
					cval.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, readableDate);
				}else{
					cval.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, "");
				}*/
				insertedId = (int) OMSDBManager.getSpecificDB(schemaName)
						.insertWithOnConflict(destinationTable, null, cval,SQLiteDatabase.CONFLICT_IGNORE);
			}
			destinationCursor.close();
		} catch (SQLException e) {
			Log.e(OMSDatabaseConstants.BL_TYPE_IU,
					"Error occurred in method insertOrUpdateDestinationTable for input parameter destinationTable["
							+ destinationTable
							+ "],pkname["
							+ whereColumnName
							+ "],pkval["
							+ whereColVal
							+ "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		Log.i(TAG, "insertOrUpdateDestinationTable () : insertedId :"
				+ insertedId);
		return insertedId;
	}
	
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		/*
		 * pDialog = new ProgressDialog(context);
		 * pDialog.setMessage(context.getResources().getString(R.string.bl_iu));
		 * if(!pDialog.isShowing()) pDialog.show();
		 */
	}

	@Override
	protected void onPostExecute(String result) {
		/*
		 * if(!pDialog.isShowing()) pDialog.dismiss();
		 */
		/*
		 * if (Integer.parseInt(result) != -1) { if (rListener != null) {
		 * pDialog.dismiss();
		 * rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue()); } } else
		 * { if (rListener != null) { pDialog.dismiss();
		 * rListener.receiveResult(OMSMessages.BL_FAILURE.getValue()); } }
		 */
		if (rListener != null)
			rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
		Log.d("TAG", "Result inInsertUpdateActionCenterAsyncTaskHelper:::"
				+ result);
		super.onPostExecute(result);

	}

	public int insertOrUpdateBulk(String destinationTableName,
			String destinationSchemaName, List<ContentValues> cvList) {
		int i = 0;
		int noOfRowsUpdated = -1;
		String appId = OMSApplication.getInstance().getAppId();
		SQLiteDatabase destinationDatabase = null;
		try {
			destinationDatabase = OMSDBManager
					.getSpecificDB(destinationSchemaName);
			destinationDatabase.beginTransaction();
			for (i = 0; i < cvList.size(); i++) {

				// Trigger an update first
				noOfRowsUpdated = destinationDatabase.update(
						destinationTableName,
						cvList.get(i),
						OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? " + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
								+ appId + "'",
						new String[] { cvList.get(i).getAsString(
								OMSDatabaseConstants.UNIQUE_ROW_ID) });

				if (noOfRowsUpdated == 0) {
					// No rows updated. So, Trigger an insert
					destinationDatabase.insert(destinationTableName, null,
							cvList.get(i));
				}

			}
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateBulk for input parameter "
							+ "tableName["
							+ destinationTableName
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
					"Error occurred in method insertOrUpdateBulk for input parameter "
							+ "tableName["
							+ destinationTableName
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
					"Error occurred in method insertOrUpdateBulk for input parameter "
							+ "tableName["
							+ destinationTableName
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
			if (destinationDatabase != null) {
				destinationDatabase.setTransactionSuccessful();
				destinationDatabase.endTransaction();
			}
		}
		return noOfRowsUpdated;
	}
	
	
	
	public static double getJulDate() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		double extra = (100.0 * year) + month - 190002.5;
		double julianDay = (367.0 * year)
				- (Math.floor(7.0 * (year + Math.floor((month + 9.0) / 12.0)) / 4.0))
				+ Math.floor((275.0 * month) / 9.0) + day
				+ ((hour + ((minute + (second / 60.0)) / 60.0)) / 24.0)
				+ 1721013.5 - ((0.5 * extra) / Math.abs(extra)) + 0.5;
		DecimalFormat sixDigitFormat = new DecimalFormat("#.######");
		return Double.valueOf(sixDigitFormat.format(julianDay));
	}
}

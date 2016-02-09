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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLBreakDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLExecutorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLFailedExecutorDTO;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.utils.OMSAlertDialog;

public class BLHelper {

	private String TAG = this.getClass().getSimpleName();
	public Context context;

	public BLHelper(Context c) {
		context = c;
	}

	public List<BLExecutorDTO> getBLDetails(String actionusId, int appId) {
		List<BLExecutorDTO> blList = new ArrayList<BLExecutorDTO>();
		Cursor blCursor = null;

		try {

			blCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.BL_TABLE_NAME,
					null,
					OMSDatabaseConstants.BL_PARENT_USID + "=" + "'" + actionusId
							+ "'" + "and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'" + "AND "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null,
					OMSDatabaseConstants.BL_ACTION_ORDER);
			if (blCursor.moveToFirst()) {
				do {
					BLExecutorDTO bl = new BLExecutorDTO();
					bl.blType = blCursor.getString(blCursor
							.getColumnIndex(OMSMessages.BL_TYPE.getValue()));
					bl.blXml = blCursor.getString(blCursor
							.getColumnIndex(OMSMessages.XML.getValue()));
					bl.blusid= blCursor.getString(blCursor
							.getColumnIndex("usid"));
					blList.add(bl);
				} while (blCursor.moveToNext());
			}
			blCursor.close();

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getBLDetails for input parameter actionusId["
							+ actionusId + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return blList;
	}

	public int insertOrUpdateData(String tableName, String usid,
			ContentValues cvals) {
		int updatedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Cursor iuCursor = null;
		String Constants = OMSDatabaseConstants.UNIQUE_ROW_ID;
		try {
			if (usid != null) {
				iuCursor = TransDatabaseUtil.query(
						tableName,
						null,
						Constants + "=" + "'" + usid + "'" + "AND "
								+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
								+ " <> '1'", null, null, null, null);
				if (iuCursor.moveToFirst()) {
					updatedId = TransDatabaseUtil.update(tableName, cvals,
							Constants + "= ? ", new String[] { usid });
					Log.i(TAG,"Record Updated:"+updatedId);
				}
				else
				{
					cvals.put("usid", usid);
					cvals.put("isdelete", "0");
					Calendar juliancal = Calendar.getInstance();
					Long timeinmillis = juliancal.getTimeInMillis();
					String readableDate = OMSAlertDialog.getReadableDate(timeinmillis);
					/*if(!TextUtils.isEmpty(readableDate)){
						cvals.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, readableDate);
					}else{
						cvals.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, "");
					}*/
					cvals.put(OMSDatabaseConstants.COMMON_COLUMN_NAME_MODIFIED_DATE,String.valueOf(getJulDate()));
					updatedId = (int) TransDatabaseUtil.insert(tableName, null, cvals);
					Log.i(TAG,"Record Inserted:"+updatedId);
				}
			} else {
				iuCursor = TransDatabaseUtil
						.query(tableName,
								null,
								OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
										+ " <> '1'", null, null, null, null);
				Calendar juliancal = Calendar.getInstance();
				Long timeinmillis = juliancal.getTimeInMillis();
				String readableDate = OMSAlertDialog.getReadableDate(timeinmillis);
				/*if(!TextUtils.isEmpty(readableDate)){
					cvals.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, readableDate);
				}else{
					cvals.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, "");
				}*/
				if (iuCursor.moveToFirst()) {
					updatedId = TransDatabaseUtil.update(tableName, cvals,
							null, null);
					Log.i(TAG,"Record Inserted:"+updatedId);
				}
			}
		} catch (SQLException e) {
			Log.e(TAG, "Error occurred in method insertOrUpdateData. Error is:"
					+ e.getMessage());
			e.printStackTrace();
		} 
		finally{
			if(iuCursor!=null){
				iuCursor.close();
			}
		}
		return updatedId;
	}

	public String getFirstUsidFromDestinationTable(String tableName, String colName,
			String usid, String dataType, String primaryKey) {
		String newUsid="";
		String colVal = "";
		String primaryKey_Column_name = primaryKey;
		Cursor blCursor = null;
		try {
			blCursor = TransDatabaseUtil.query(
					tableName,
					null,
					primaryKey_Column_name + "=" + "'" + usid + "'"
							+ "AND "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null, null);
			if (blCursor.moveToFirst()) {
				newUsid = usid;
			}else{
				
					blCursor = TransDatabaseUtil
							.query(tableName,
									null,
									OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
											+ " <> '1'", null, null, null, null);
					if (blCursor.moveToFirst()) {
						newUsid = 	blCursor.getString(blCursor
								.getColumnIndex("usid"));
					}
			}
			} catch (IllegalArgumentException e) {
				Log.e(TAG,
						"Error occurred in method getColuValForColName. Error is:"
								+ e.getMessage());
				e.printStackTrace();
			}
		return newUsid;
		
	}
	public String getColuValForColName(String tableName, String colName,
			String usid, String dataType, String primaryKey) {
		String colVal = "";
		String primaryKey_Column_name = primaryKey;
		Cursor blCursor = null;
		if (usid == null) {
			try {
				blCursor = TransDatabaseUtil
						.query(tableName,
								null,
								OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
										+ " <> '1'", null, null, null, null);

				if (blCursor.moveToFirst()) {
					if (dataType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
						int colValue = blCursor.getInt(blCursor
								.getColumnIndex(colName));
						colVal = Integer.toString(colValue);
						if(checkForExponentialType(colVal)){
							colVal = String.format("%f", Double.parseDouble(colVal)) ;
	      					 Log.d(TAG,"Formatted::::"+colVal); 
	      				int col_int = 	 Float.valueOf(colVal.toString()).intValue();
	      				colVal = Integer.toString(col_int);
						}
					} else if (dataType.equalsIgnoreCase(OMSMessages.REAL
							.getValue())) {
						double colValue = blCursor.getDouble(blCursor
								.getColumnIndex(colName));
						colVal = Double.toString(colValue);
                       if(checkForExponentialType(colVal)){
                    	 colVal = String.format("%f", Double.parseDouble(colVal)) ;
      					 Log.d(TAG,"Formatted::::"+colVal); 
      					 //val = Float.valueOf(val.toString()).intValue();
      					 //Log.d(TAG,"converted Formatted val::::"+val);
						}
					}
				} else {
					colVal = OMSMessages.MIN_INDEX_STR.getValue();
				}
			} catch (IllegalArgumentException e) {
				Log.e(TAG,
						"Error occurred in method getColuValForColName. Error is:"
								+ e.getMessage());
				e.printStackTrace();
			}
		} else {
			try {
				blCursor = TransDatabaseUtil.query(
						tableName,
						null,
						primaryKey_Column_name + "=" + "'" + usid + "'"
								+ "AND "
								+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
								+ " <> '1'", null, null, null, null);
				if (blCursor.moveToFirst()) {
					if (dataType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
						int colValue = blCursor.getInt(blCursor
								.getColumnIndex(colName));
						colVal = Integer.toString(colValue);
						if(checkForExponentialType(colVal)){
							colVal = String.format("%f", Double.parseDouble(colVal)) ;
	      					 Log.d(TAG,"Formatted::::"+colVal); 
	      				int col_int = 	 Float.valueOf(colVal.toString()).intValue();
	      				colVal = Integer.toString(col_int);
						}
					} else if (dataType.equalsIgnoreCase(OMSMessages.REAL
							.getValue())) {
						double colValue = blCursor.getDouble(blCursor
								.getColumnIndex(colName));
						colVal = Double.toString(colValue);
						 if(checkForExponentialType(colVal)){
	                    	 colVal = String.format("%f", Double.parseDouble(colVal)) ;
	      					 Log.d(TAG,"Formatted::::"+colVal); 
	      					 //val = Float.valueOf(val.toString()).intValue();
	      					 //Log.d(TAG,"converted Formatted val::::"+val);
							}
					}else{
						int colValue = blCursor.getInt(blCursor
								.getColumnIndex(colName));
						colVal = Integer.toString(colValue);
						if(checkForExponentialType(colVal)){
							colVal = String.format("%f", Double.parseDouble(colVal)) ;
	      					 Log.d(TAG,"Formatted::::"+colVal); 
	      				int col_int = 	 Float.valueOf(colVal.toString()).intValue();
	      				colVal = Integer.toString(col_int);
						}
					}
				}else{
					try {
						blCursor = TransDatabaseUtil
								.query(tableName,
										null,
										OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
												+ " <> '1'", null, null, null, null);

						if (blCursor.moveToFirst()) {
							if (dataType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
								int colValue = blCursor.getInt(blCursor
										.getColumnIndex(colName));
								colVal = Integer.toString(colValue);
								if(checkForExponentialType(colVal)){
									colVal = String.format("%f", Double.parseDouble(colVal)) ;
			      					 Log.d(TAG,"Formatted::::"+colVal); 
			      				int col_int = 	 Float.valueOf(colVal.toString()).intValue();
			      				colVal = Integer.toString(col_int);
								}
							} else if (dataType.equalsIgnoreCase(OMSMessages.REAL
									.getValue())) {
								double colValue = blCursor.getDouble(blCursor
										.getColumnIndex(colName));
								colVal = Double.toString(colValue);
		                       if(checkForExponentialType(colVal)){
		                    	 colVal = String.format("%f", Double.parseDouble(colVal)) ;
		      					 Log.d(TAG,"Formatted::::"+colVal); 
		      					 //val = Float.valueOf(val.toString()).intValue();
		      					 //Log.d(TAG,"converted Formatted val::::"+val);
								}
							}
							else{
								int colValue = blCursor.getInt(blCursor
										.getColumnIndex(colName));
								colVal = Integer.toString(colValue);
								if(checkForExponentialType(colVal)){
									colVal = String.format("%f", Double.parseDouble(colVal)) ;
			      					 Log.d(TAG,"Formatted::::"+colVal); 
			      				int col_int = 	 Float.valueOf(colVal.toString()).intValue();
			      				colVal = Integer.toString(col_int);
								}
								
								
							}
							
						} else {
							colVal = OMSMessages.MIN_INDEX_STR.getValue();
						}
					} catch (IllegalArgumentException e) {
						Log.e(TAG,
								"Error occurred in method getColuValForColName. Error is:"
										+ e.getMessage());
						e.printStackTrace();
					}
				}
			} catch (IllegalArgumentException e) {
				Log.e(TAG,
						"Error occurred in method getColuValForColName. Error is:"
								+ e.getMessage());
				e.printStackTrace();
			}
		}
		return colVal;
	}

	public String getColuValForColName(String tableName, String colName,
			 String dataType, boolean isForceUpdate) {
		String colVal="";
		return colVal;
	}
	
	public List<String> getTransUsids(String tableName) {
		ArrayList<String> transList = new ArrayList<String>();
		Cursor transDBDataCursor = null;
		try {
			transDBDataCursor = TransDatabaseUtil.query(tableName, null,
					OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE + " <> '1'",
					null, null, null, null);
			if (transDBDataCursor.moveToFirst()) {
				do {
					transList.add(transDBDataCursor.getString(transDBDataCursor
							.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID)));
				} while (transDBDataCursor.moveToNext());
			}
			transDBDataCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransUsids for input parameter tableName["
							+ tableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return transList;
	}

	public List<String> getFinalForColValList(String tableName, String ColName) {
		List<String> transList = new ArrayList<String>();
		Cursor transDBDataCursor = null;
		String ColType = null;
		try {
			Cursor	PragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
					"pragma table_info(" + tableName + ");", null);
		if (PragmaCursor.getCount() > 0) {
			if(PragmaCursor.moveToFirst()){
				do{
					String columnName = PragmaCursor.getString(PragmaCursor
							.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));	
					if(columnName.equalsIgnoreCase(ColName)){
						ColType = PragmaCursor.getString(PragmaCursor
								.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
					}
				}while(PragmaCursor.moveToNext());
			}
			
		}
			transDBDataCursor = TransDatabaseUtil.query(tableName, null,
					OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE + " <> '1'",
					null, null, null, null);
			if(ColType!=null)
			if (transDBDataCursor.moveToFirst()) {
				do {
					if (ColType.equalsIgnoreCase(OMSMessages.TEXT.getValue()) ||
							ColType.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
						transList.add(transDBDataCursor
								.getString(transDBDataCursor
										.getColumnIndex(ColName)));
					}
					if (ColType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
						transList.add(Integer.toString(transDBDataCursor
								.getInt(transDBDataCursor
										.getColumnIndex(ColName))));
					}
				} while (transDBDataCursor.moveToNext());
			}
			transDBDataCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getTransUsids for input parameter tableName["
							+ tableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return transList;
	}

	public List<BLExecutorDTO> getBLDetailsBasedOnUsid(String blusId, int appId) {
		Log.i(TAG, "getBLDetailsBasedOnUsid() BLUsid : " +blusId);
		List<BLExecutorDTO> blList = new ArrayList<BLExecutorDTO>();
		if(blusId==null || blusId.equalsIgnoreCase("") || blusId.equalsIgnoreCase("null") )
			return null;
		Cursor blCursor = null;
		try {
			blCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.BL_TABLE_NAME,
					null,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "=" + "'" + blusId + "'"
							+ "AND "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null, null);
			if (blCursor.moveToFirst()) {
				do {
					BLExecutorDTO bl = new BLExecutorDTO();
					bl.blType = blCursor.getString(blCursor
							.getColumnIndex(OMSMessages.BL_TYPE.getValue()));
					bl.blXml = blCursor.getString(blCursor
							.getColumnIndex(OMSMessages.XML.getValue()));
					blList.add(bl);

				} while (blCursor.moveToNext());
			}

			blCursor.close();

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getBLDetails for input parameter blusId["
							+ blusId + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return blList;
	}

	public boolean checkBreakConditionBL(String initTableName,String primaryColName,
			String primaryColVal, List<BLBreakDTO> breakList) {
		Cursor blCursor = null;

		boolean isBreak = false;
		//String tableName;
		String breakColName;
		String breakColVal;
		String breakColType = null;
		try {
			if (breakList != null && breakList.size()>0) {
				//tableName = breakList.get(0).breakTableName;
				breakColName = breakList.get(0).breakColumnName;
				breakColVal = breakList.get(0).breakColVal;
				if(breakColVal.equals("") || breakColVal.length()==0 || breakColVal==null){
					breakColVal = "-1";
				}
				/*if(tableName.equals(""))
				{
					tableName = initTableName;
				}*/
			Cursor	PragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
						"pragma table_info(" + initTableName + ");", null);
			if (PragmaCursor.getCount() > 0) {
				if(PragmaCursor.moveToFirst()){
					do{
						String columnName = PragmaCursor.getString(PragmaCursor
								.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));	
						if(columnName.equalsIgnoreCase(breakColName)){
							breakColType = PragmaCursor.getString(PragmaCursor
									.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
						}
					}while(PragmaCursor.moveToNext());
				}
				
			}
				//breakColType = breakList.get(0).breakColType;
				blCursor = TransDatabaseUtil.query(
						initTableName,
						null,
						primaryColName + "=" + "'" + primaryColVal + "'"
								+ "AND "
								+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
								+ " <> '1'", null, null, null, null);
				Log.d("BLHelper", "BLCursor Length:::" + blCursor.getCount());
				if(breakColType!=null)
				if (blCursor.moveToFirst()) {
					if (breakColType.equalsIgnoreCase(OMSMessages.TEXT_SMALL
							.getValue())) {
						String colVal = blCursor.getString(blCursor
								.getColumnIndex(breakColName));
						if (colVal.equalsIgnoreCase(breakColVal))
							return true;
					} else if (breakColType.equalsIgnoreCase(OMSMessages.INTEGER
							.getValue())) {
						int colVal = blCursor.getInt(blCursor
								.getColumnIndex(breakColName));
						if (colVal == Integer.parseInt(breakColVal)){
							isBreak = true;
							return true;
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method checkBreakConditionBL. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return isBreak;
	}

	
	public List<BLFailedExecutorDTO> getBLFailedDetails(String bltype, String status,int appId) {
		List<BLFailedExecutorDTO> blFailList = new ArrayList<BLFailedExecutorDTO>();
		Cursor blCursor = null;

		try {

			blCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.TRANSACTION_QUEUE_TABLE_NAME,
					null,
					OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE + "=" + "'" + bltype
							+ "'" + "and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'" + "and "+OMSDatabaseConstants.TRANSACTION_QUEUE_STATUS + "=" + "'" + status
							+"'"+ " AND "+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE + " <> '1'", null, null, null,
					null);
			if (blCursor.moveToFirst()) {
				do {
					BLFailedExecutorDTO bl = new BLFailedExecutorDTO();
					bl.actionusid =blCursor.getString(blCursor
							.getColumnIndex(OMSDatabaseConstants.TRANSACTION_QUEUE_ACTION_UNIQUE_ID));
					bl.serviceurl =blCursor.getString(blCursor
							.getColumnIndex(OMSDatabaseConstants.TRANSACTION_QUEUE_SERVER_URL));
					bl.dataTableName=blCursor.getString(blCursor
							.getColumnIndex(OMSDatabaseConstants.TRANSACTION_QUEUE_DATA_TABLE_NAME));
					bl.blType=blCursor.getString(blCursor
							.getColumnIndex(OMSDatabaseConstants.TRANSACTION_QUEUE_TYPE));
					bl.schema=blCursor.getString(blCursor
							.getColumnIndex(OMSDatabaseConstants.BL_SCHEMA_NAME));
					blFailList.add(bl);
				} while (blCursor.moveToNext());
			}
			blCursor.close();

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getBLFailedDetails for input parameter bltype["
							+ bltype + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return blFailList;
	}

	public String getBLCompleteUniqueId(String blName, int appId) {
		Cursor blCursor = null;
         String blCompleteUniqueId="";
		try {
            
			blCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.BL_TABLE_NAME,
					null,
					OMSDatabaseConstants.BL_ACTION_UNIQUE_NAME + "=" + "'"+ blName
							+ "'"+ "and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'" + "AND "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null,
							null);
			if (blCursor.moveToFirst()) {
				blCompleteUniqueId =  blCursor.getString(blCursor
						.getColumnIndex(OMSDatabaseConstants.MULTI_FORM_GRID_PARENT_USID));
			}
			blCursor.close();

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getBLDetails for input parameter blName["
							+ blName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return blCompleteUniqueId;
	}

	private boolean checkForExponentialType(String str){
		boolean isExponential=false;
		if(str.contains("E") || str.contains("e")){
			isExponential=true;
		}
		return isExponential;
	}

	public String getForceUpdatedUsid(String tableName, String colName,
			String usid, String dataType, String primaryKey) {
		String newUsid="";
		String colVal = "";
		String primaryKey_Column_name = primaryKey;
		Cursor blCursor = null;
		try {
			
				
					blCursor = TransDatabaseUtil
							.query(tableName,
									null,
									OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
											+ " <> '1'", null, null, null, null);
					if (blCursor.moveToFirst()) {
						newUsid = 	blCursor.getString(blCursor
								.getColumnIndex("usid"));
					}
					
					if(!TextUtils.isEmpty(newUsid)){
						
					}
			
			} catch (IllegalArgumentException e) {
				Log.e(TAG,
						"Error occurred in method getColuValForColName. Error is:"
								+ e.getMessage());
				e.printStackTrace();
			}
		return newUsid;
		
	}
	
	public int updateForceUpdatedFirstRow(String tableName, String usid,
			ContentValues cvals) {
		int updatedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Cursor iuCursor = null;
		String Constants = OMSDatabaseConstants.UNIQUE_ROW_ID;
		String firstRowUsid="";
		try{
			iuCursor = TransDatabaseUtil
					.query(tableName,
							null,
							OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			
			if (iuCursor.moveToFirst()) {
					firstRowUsid = 	iuCursor.getString(iuCursor
							.getColumnIndex("usid"));
			}
			if(!TextUtils.isEmpty(firstRowUsid)){
				updatedId = TransDatabaseUtil.update(tableName, cvals,
						Constants + "= ? ", new String[] { firstRowUsid });		
			}else{
				Calendar juliancal = Calendar.getInstance();
				Long timeinmillis = juliancal.getTimeInMillis();
	    		cvals.put("usid", Long.toString(timeinmillis));
				cvals.put("isdelete", "0");
				cvals.put(OMSDatabaseConstants.COMMON_COLUMN_NAME_MODIFIED_DATE,Long.toString(timeinmillis));
				updatedId = (int) TransDatabaseUtil.insert(tableName, null, cvals);
			}
		}
		 catch (SQLException e) {
				Log.e(TAG, "Error occurred in method insertOrUpdateData. Error is:"
						+ e.getMessage());
				e.printStackTrace();
			} 
		
		return updatedId;
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

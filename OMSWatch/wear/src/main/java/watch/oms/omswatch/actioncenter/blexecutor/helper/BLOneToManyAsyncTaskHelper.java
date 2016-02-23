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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.IllegalFormatConversionException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import watch.oms.omswatch.R;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLColumnDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLIUDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOneToManyDTO;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLGlobalValueGenerator;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.utils.OMSAlertDialog;

public class BLOneToManyAsyncTaskHelper extends AsyncTask<String, Void, String> {
	private final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private OMSReceiveListener rListener;
	String uniqueId = null;
	ProgressDialog pDialog;
	List<BLIUDTO> IUList;
	List<BLOneToManyDTO> oneToManyList;
	private List<String> transUsidList;
	BLGlobalValueGenerator blGlobalValGenerator;
	private boolean hasUsidInDestination;
	
	public BLOneToManyAsyncTaskHelper(Activity FragmentContext,
			int containerId, OMSReceiveListener receiveListener,
			List<String> transUsidList) {
		context = FragmentContext;
		rListener = receiveListener;
		this.transUsidList = transUsidList;
		blGlobalValGenerator = new BLGlobalValueGenerator();
	/*	pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getResources().getString(
				R.string.bl_one_many));
		pDialog.show();*/

	}

	@Override
	protected String doInBackground(String... args) {
		int res = OMSDefaultValues.NONE_DEF_CNT.getValue();
		String xml_file = args[0];
		res = processOneToManyBL(xml_file);
		return Integer.toString(res);
	}

	private int processOneToManyBL(String targetXml) {
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			try {
				oneToManyList = xmlParser.parseOneToManyXML(response);
				if (oneToManyList != null) {
					result = processOneToManyList(oneToManyList);
				}
			} catch (XmlPullParserException e) {
				Log.e(TAG,
						"Error Occured in processOneToManyBL:" + e.getMessage());
				e.printStackTrace();
			}

		} catch (IOException e) {
			Log.e(TAG, "Error Occured in processOneToManyBL:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	private int processOneToManyList(List<BLOneToManyDTO> iUList2) {
		List<BLColumnDTO> targetSourceColumnNameList = new ArrayList<BLColumnDTO>();
		Hashtable<String, String> targetSourceColumnNamesHashList = new Hashtable<String, String>();
		List<BLColumnDTO> targetColList = new ArrayList<BLColumnDTO>();
		List<BLColumnDTO> destinationTableColList = new ArrayList<BLColumnDTO>();
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues cv = new ContentValues();
		String sourceTableName = iUList2.get(0).sourceTableName;
		String destinationTableName = iUList2.get(0).destinationTableName;
		String parentTableName = iUList2.get(0).parentTableName;
		String destinationprimaryForeignKey = iUList2.get(0).destinationPrimaryForeignKey;
		String destinationprimaryForeignKeyType = iUList2.get(0).destinationPrimaryForeignKeyType;
		List<BLColumnDTO> destinationColList = iUList2.get(0).destinationColList;
		List<BLColumnDTO> sourceColList = iUList2.get(0).sourceColList;
		Object primaryForeignKeyVal = generateAndGetPrimaryKeyValueFromParentTable(destinationTableName);
		
		//Retrieve transusid list from source table
		 if(sourceTableName!=null && sourceTableName.length()>0 ){
				//if(transUsidList==null)
				{
				//	transUsidList.clear();
				
					
					transUsidList = new ArrayList<String>();
				Cursor	sourceCursor =  TransDatabaseUtil.query(
						sourceTableName,
						null, OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
								+ " <> '1'", null, null, null, null);
				if(sourceCursor.moveToFirst()){
					do{
						transUsidList.add(sourceCursor.getString(sourceCursor.getColumnIndex("usid")));
					}while(sourceCursor.moveToNext());
				}
				}	

			}
		
	
			if(destinationprimaryForeignKeyType==null){
		    	Cursor	PragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
							"pragma table_info(" + parentTableName + ");", null);
				if (PragmaCursor.getCount() > 0) {
					if(PragmaCursor.moveToFirst()){
						do{
								String columnName = PragmaCursor.getString(PragmaCursor
									.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));	
								if(columnName.equalsIgnoreCase(destinationprimaryForeignKey)){
									destinationprimaryForeignKeyType = PragmaCursor.getString(PragmaCursor
											.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
								}
							}while(PragmaCursor.moveToNext());
						}
					
					}
				}
		// Orders Table
		populateChildTableWithPrimaryForeignKeyVal(parentTableName,
				destinationprimaryForeignKey, destinationprimaryForeignKeyType,
				primaryForeignKeyVal);
//Chack for usid in destination collist
		for(int i=0;i<destinationColList.size();i++){
			if(destinationColList.get(i).columnName.equalsIgnoreCase("usid")){
				hasUsidInDestination=true;
			}
		}
		
		
		for (int i = 0; i < destinationColList.size(); i++) {
			for (int j = 0; j < sourceColList.size(); j++) {
				if (sourceColList.get(j).columnIndex
						.equalsIgnoreCase(destinationColList.get(i).columnIndex)) {
					targetColList.add(sourceColList.get(j));
					targetSourceColumnNameList.add(sourceColList.get(i));
					targetSourceColumnNamesHashList.put(
							sourceColList.get(i).columnIndex,
							sourceColList.get(i).columnName);
					destinationTableColList.add(destinationColList.get(i));
				}
			}
		}
		
		Cursor	PragmaCursor1 = TransDatabaseUtil.getTransDB().rawQuery(
				"pragma table_info(" + sourceTableName + ");", null);
		for (int k = 0; k < transUsidList.size(); k++) {
			if (targetColList != null && targetColList.size() > 0) {
				
				if(!destinationprimaryForeignKey.equalsIgnoreCase("usid")){
					String pkVal = "";
					try {
						Calendar juliancal = Calendar.getInstance();
						Long timeinmillis = juliancal.getTimeInMillis();
						pkVal = Long.toString(timeinmillis);
					}
					catch (IllegalFormatConversionException e) {
						Log.e(TAG,
								"Error Occured in generateAndGetPrimaryKeyValueFromParentTable:"
										+ e.getMessage());
						e.printStackTrace();
					}
					cv.put("usid", pkVal);
				}
				
				uniqueId = transUsidList.get(k);
				Cursor sourceCursor = null;
				String selection =   OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE 
						+ " <> '1'"  ;
				sourceCursor = TransDatabaseUtil
						.query(sourceTableName,
								null,
								OMSDatabaseConstants.UNIQUE_ROW_ID + "='"
										+ uniqueId + "'" + "and " +selection, null, null, null,
								null);
				if (sourceCursor.moveToFirst()) {

					for (int i = 0; i < targetColList.size(); i++) {
							String srcColType = null;
							if (PragmaCursor1.getCount() > 0) {
								if(PragmaCursor1.moveToFirst()){
									do{
										String columnName = PragmaCursor1.getString(PragmaCursor1
												.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));	
										if(sourceColList.get(i).isGlobal){
											sourceColList.get(i).columnType = "globalIU";
											srcColType = "globalIU";
										}
										else if(columnName.equalsIgnoreCase(targetColList.get(i).columnName)){
											srcColType = PragmaCursor1.getString(PragmaCursor1
													.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
										}
									}while(PragmaCursor1.moveToNext());
								}
								
							}
						if (srcColType.equals(OMSMessages.TEXT.getValue()) || srcColType.equals(OMSMessages.BIGINT.getValue())) {
							cv.put(destinationTableColList.get(i).columnName,
									sourceCursor.getString(sourceCursor
											.getColumnIndex(targetSourceColumnNamesHashList
													.get(targetColList.get(i).columnIndex))));
						} else if (srcColType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
							cv.put(destinationTableColList.get(i).columnName,
									sourceCursor.getString(sourceCursor
											.getColumnIndex(targetSourceColumnNamesHashList
													.get(targetColList.get(i).columnIndex))));
						} else if (srcColType.equalsIgnoreCase(OMSMessages.REAL.getValue())) {
							cv.put(destinationTableColList.get(i).columnName,
									sourceCursor.getString(sourceCursor
											.getColumnIndex(targetSourceColumnNamesHashList
													.get(targetColList.get(i).columnIndex))));
						}else if (srcColType.equalsIgnoreCase("globalIU")) {
							 String globalVal = blGlobalValGenerator.getGlobalBLValue(sourceColList.get(i).columnName);
							 cv
								.put(destinationTableColList.get(i).columnName,
										globalVal);
						}

						if (destinationprimaryForeignKeyType
								.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
							int primaryKeyVal = (Integer) primaryForeignKeyVal;
							cv.put(destinationprimaryForeignKey, primaryKeyVal);
						} else if (destinationprimaryForeignKeyType
								.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || 
								destinationprimaryForeignKeyType
								.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
							String primaryKeyVal = (String) primaryForeignKeyVal;
							cv.put(destinationprimaryForeignKey, primaryKeyVal);
						} else if (destinationprimaryForeignKeyType
								.equalsIgnoreCase(OMSMessages.REAL_CAPS.getValue())) {
							double primaryKeyVal = (Double) primaryForeignKeyVal;
							cv.put(destinationprimaryForeignKey, primaryKeyVal);
						}

					}
				}
				sourceCursor.close();

			}
			
			result = insertIntoDestinationTable(destinationTableName, cv);
		}
		return result;
	}

	private int populateChildTableWithPrimaryForeignKeyVal(
			String parentTableName, String destinationprimaryForeignKey,
			String destinationprimaryForeignKeyType, Object primaryForeignKeyVal) {
		int insertedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {
			ContentValues cv = new ContentValues();
			if (destinationprimaryForeignKeyType
					.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
				int primaryKeyVal = (Integer) primaryForeignKeyVal;
				cv.put(destinationprimaryForeignKey, primaryKeyVal);
			} else if (destinationprimaryForeignKeyType
					.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || 
					destinationprimaryForeignKeyType
					.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
				String primaryKeyVal = (String) primaryForeignKeyVal;
				cv.put(destinationprimaryForeignKey, primaryKeyVal);
			} else if (destinationprimaryForeignKeyType
					.equalsIgnoreCase(OMSMessages.REAL_CAPS.getValue())) {
				double primaryKeyVal = (Double) primaryForeignKeyVal;
				cv.put(destinationprimaryForeignKey, primaryKeyVal);
			}
			cv.put(OMSDatabaseConstants.TRANS_TABLE_STATUS_COLUMN,
					OMSDatabaseConstants.STATUS_TYPE_NEW);
			Calendar juliancal = Calendar.getInstance();
			Long timeinmillis = juliancal.getTimeInMillis();
			String readableDate = OMSAlertDialog.getReadableDate(timeinmillis);
			/*if(!TextUtils.isEmpty(readableDate)){
				cv.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, readableDate);
			}else{
				cv.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, "");
			}*/
			String usid_val = Long.toString(timeinmillis);
			cv.put(OMSDatabaseConstants.UNIQUE_ROW_ID, usid_val);
			cv.put(OMSDatabaseConstants.IS_DELETE, OMSDatabaseConstants.IS_DELETE_CONSTANT);
			cv.put(OMSDatabaseConstants.ACTION_QUEUE_MODIFIED_DATE, usid_val);
			
         Cursor forceUpdateCursor =  TransDatabaseUtil.query(parentTableName, null,  OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
		 + " <> '1'", null,null,null,null);
          if(forceUpdateCursor.moveToFirst()){
        	  String usid = forceUpdateCursor.getString(forceUpdateCursor.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));
               cv.remove(OMSDatabaseConstants.UNIQUE_ROW_ID);
        	  insertedId = (int) TransDatabaseUtil
						.update(parentTableName, cv, OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
								new String[] { usid.toString() });
          }else{
			insertedId = (int) TransDatabaseUtil.insert(parentTableName,
					null, cv);
          }
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error Occured in populateChildTableWithPrimaryForeignKeyVal:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return insertedId;
	}

	// Popualte Destination Table i.e . OrderDetails
	private int insertIntoDestinationTable(String destinationTable,
			ContentValues cval) {
		cval.put(OMSDatabaseConstants.TRANS_TABLE_STATUS_COLUMN,
				OMSDatabaseConstants.STATUS_TYPE_NEW);
		cval.put(OMSDatabaseConstants.IS_DELETE, OMSDatabaseConstants.IS_DELETE_CONSTANT);
		Calendar juliancal = Calendar.getInstance();
		Long timeinmillis = juliancal.getTimeInMillis();
		String readableDate = OMSAlertDialog.getReadableDate(timeinmillis);
		cval.put(OMSDatabaseConstants.ACTION_QUEUE_MODIFIED_DATE, Long.toString(timeinmillis));
		//if(!hasUsidInDestination)
		{
			Calendar cal = Calendar.getInstance();
			String newUSID = Long.toString(cal.getTimeInMillis());
			cval.put(OMSDatabaseConstants.UNIQUE_ROW_ID, newUSID);
		}
		/*if(!TextUtils.isEmpty(readableDate)){
			cval.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, readableDate);
		}else{
			cval.put(OMSDatabaseConstants.COMMON_TRANS_COLUMN_NAME_READABLE_DATE, "");
		}*/
		int insertedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {
			insertedId = (int) TransDatabaseUtil.insert(destinationTable,
					null, cval);
		} catch (SQLException e) {
			Log.e(TAG,
					"Error Occured in insertIntoDestinationTable:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return insertedId;
	}

	public Object generateAndGetPrimaryKeyValueFromParentTable(
			String destinationTableName) {
		Object pkVal = "";
		try {
			Calendar juliancal = Calendar.getInstance();
			pkVal = Long.toString(juliancal.getTimeInMillis());
		} catch (IllegalFormatConversionException e) {
			Log.e(TAG,
					"Error Occured in generateAndGetPrimaryKeyValueFromParentTable:"
							+ e.getMessage());
			e.printStackTrace();
		}
		
		return pkVal;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		if (Integer.parseInt(result) != OMSDefaultValues.NONE_DEF_CNT.getValue()) {
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
			}
		} else {
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_FAILURE.getValue());
			}
		}
	//	pDialog.dismiss();
		Log.d(TAG, "Result inInsertUpdateActionCenterAsyncTaskHelper:::"
				+ result);
	}

	public String LoadFile(String fileName, Context context) throws IOException {
		// Create a InputStream to read the file into
		InputStream input;
		// get the file as a stream
		input = context.getAssets().open(fileName);
		// create a buffer that has the same size as the InputStream
		byte[] buffer = new byte[input.available()];
		// read the text file as a stream, into the buffer
		input.read(buffer);
		// create a output stream to write the buffer into
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		// write this buffer to the output stream
		output.write(buffer);
		// Close the Input and Output streams
		output.close();
		input.close();
		// return the output stream as a String
		return output.toString();
	}

}

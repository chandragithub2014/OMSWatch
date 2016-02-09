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
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLDeleteDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLIUDTO;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.parser.OMSServerMapperHelper;
import watch.oms.omswatch.utils.OMSAlertDialog;
import watch.oms.omswatch.R;


public class BLDeleteAsyncTaskHelper extends AsyncTask<String, Void, String> {

	private final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private OMSReceiveListener rListener;
	private String uniqueId = null;
	private ProgressDialog pDialog;
	public List<BLIUDTO> IUList;
	private List<BLDeleteDTO> deleteList;
	private List<String> transUsidList;

	public BLDeleteAsyncTaskHelper(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList) {
		context = FragmentContext;
		rListener = receiveListener;
		this.transUsidList = transUsidList;
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getResources().getString(R.string.bl_delete));
		pDialog.show();

	}

	@Override
	protected String doInBackground(String... args) {
		int res = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try{
			String xml_file = args[0];
			if(transUsidList.size()>0) {
			for (int i = 0; i < transUsidList.size(); i++) {
				res = processDeleteBL(transUsidList.get(i), xml_file);
			}
			}
			else{
				res = processDeleteBL(null, xml_file);
			}
		
		}catch(Exception e){
			Log.d(TAG,"BL excecution Failed. Please check the Log.");
			e.printStackTrace();
		}
		return Integer.toString(res);
	}

	private int processDeleteBL(String usid, String targetXml) {
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		uniqueId = usid;
		try {
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			deleteList = xmlParser.parseBLDelete(response);
			if (deleteList != null) {
				result = processDelList(deleteList);
			}

		} catch (XmlPullParserException e) {
			Log.e(TAG,
					"Exception Occured in processDeleteBL: " + e.getMessage());

			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG,
					"Exception Occured in processDeleteBL: " + e.getMessage());

			e.printStackTrace();
		}
		return result;
	}

	private int processDelList(List<BLDeleteDTO> bLDelList) {
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		ContentValues cv = new ContentValues();
		if (!TextUtils.isEmpty(bLDelList.get(0).primaryKeyColum)
				&& bLDelList.get(0).primaryKeyColum.equalsIgnoreCase(OMSMessages.ALL
				.getValue())) {
			
			if(!TextUtils.isEmpty(bLDelList.get(0).isHardDelete)
					&& bLDelList.get(0).isHardDelete.equalsIgnoreCase("true")){
				result = (int) TransDatabaseUtil.deleteAllRecord(bLDelList.get(0).destinationTable);
			} else {
				// Delete all
				cv.put(OMSMessages.IS_DELETE.getValue(), 1);
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
				
				result = (int) TransDatabaseUtil.update(
						bLDelList.get(0).destinationTable, cv, null, null);
			}
		} else if(uniqueId!=null) {
			if(!TextUtils.isEmpty(bLDelList.get(0).isHardDelete) 
					&& bLDelList.get(0).isHardDelete.equalsIgnoreCase("true")){
				result = (int) TransDatabaseUtil.delete(bLDelList.get(0).destinationTable,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ", new String[] { uniqueId });
			}else{
				Cursor sourceCursor = TransDatabaseUtil.query(
						bLDelList.get(0).destinationTable, null,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "='" + uniqueId + "'",
						null, null, null, null);
				if (sourceCursor.moveToFirst()) {
					cv.put(OMSMessages.IS_DELETE.getValue(), 1);
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
					result = (int) TransDatabaseUtil.update(
								bLDelList.get(0).destinationTable, cv,
								OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
								new String[] { uniqueId });
				}
				sourceCursor.close();
			}
		}
		
		// reset pagination value
		new OMSServerMapperHelper().setPaginationCurrentPage(bLDelList.get(0).destinationTable,1);
		return result;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		if (Integer.parseInt(result) != -1) {
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
			}
		} else {
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_FAILURE.getValue());
			}
		}
		if(pDialog.isShowing())
		pDialog.dismiss();
		Log.d("TAG", "Result in BLDelete ActionCenterAsyncTaskHelper:::"
				+ result);
		super.onPostExecute(result);

	}

	public String LoadFile(String fileName, Context context) throws IOException {
		// Create a InputStream to read the file into
		InputStream inputStream;
		// get the file as a stream
		inputStream = context.getAssets().open(fileName);

		// create a buffer that has the same size as the InputStream
		byte[] buffer = new byte[inputStream.available()];
		// read the text file as a stream, into the buffer
		inputStream.read(buffer);
		// create a output stream to write the buffer into
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// write this buffer to the output stream
		outputStream.write(buffer);
		// Close the Input and Output streams
		outputStream.close();
		inputStream.close();

		// return the output stream as a String
		return outputStream.toString();
	}

}

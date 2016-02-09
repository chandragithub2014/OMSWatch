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
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import watch.oms.omswatch.R;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorActionCenter;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLBreakDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLExecutorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLForDTO;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;


public class BLFORAsyncTaskHelper implements OMSReceiveListener {
	private final String TAG = this.getClass().getSimpleName();;
	private Activity context;
	private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private OMSReceiveListener rListener;
	public String uniqueId = null;
	public ProgressDialog pDialog;
	public List<BLForDTO> ForList;
	public int index = 0;
	public List<String> colValList;
	public List<BLExecutorDTO> blDataList;
	private List<String> transUsidList;
	public List<BLBreakDTO> breakDataList = null;
	private int configDBAppId;

	public BLFORAsyncTaskHelper(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList,
			int appId) {
		context = FragmentContext;
		mContainerId = containerId;
		rListener = receiveListener;
		this.transUsidList = transUsidList;
		configDBAppId = appId;
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getResources().getString(R.string.bl_for));
	}

	public int processFORBLXML(String targetXml) {
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
	try{	
		try {
			boolean isBreak = false;
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			try {
				ForList = xmlParser.parseBLFor(response);
				if (ForList != null) {
					index = ForList.get(index).initialVal;
					breakDataList = ForList.get(0).breakList;
					blDataList = new BLHelper(context).getBLDetailsBasedOnUsid(
							ForList.get(0).doColVal, configDBAppId);
					colValList = new BLHelper(context).getFinalForColValList(
							ForList.get(0).initTableName,
							ForList.get(0).initcolumnName);
					if (blDataList != null && blDataList.size() > 0
							&& colValList != null && colValList.size() > 0) {
						if (transUsidList != null) {
							transUsidList.clear();
							transUsidList.add(colValList.get(index));
						} else {
							transUsidList = new ArrayList<String>();
							transUsidList.add(colValList.get(index));
						}

						if (breakDataList != null) {
							isBreak = new BLHelper(context)
									.checkBreakConditionBL(ForList.get(0).initTableName,
											ForList.get(0).initcolumnName,
											colValList.get(index),
											breakDataList);

						}
						if (isBreak) {
							rListener.receiveResult(OMSMessages.BL_SUCCESS
									.getValue());
						} else {
							new BLExecutorActionCenter(context, mContainerId,
									BLFORAsyncTaskHelper.this, -1,
									transUsidList).doBLActionType(blDataList);
						}
					} else {
						rListener.receiveResult(OMSMessages.BL_FAILURE.getValue());
					}
				}
			} catch (XmlPullParserException e) {
				Log.e(TAG, "Error Occured in processFORBLXML:" + e.getMessage());
				e.printStackTrace();
			}
		} catch (IOException e) {
			Log.e(TAG, "Error Occured in processFORBLXML:" + e.getMessage());
			e.printStackTrace();
		}
	}catch(Exception e){
		Toast.makeText(context, "BL excecution Failed. Please check the Log.", Toast.LENGTH_SHORT).show();
	}
		return result;
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

	@Override
	public void receiveResult(String result) {
		if (result.equals(OMSMessages.BL_SUCCESS.getValue()) || result.equals(OMSMessages.ACTION_CENTER_SUCCESS.getValue())
				|| result.equals(OMSMessages.REFRESH.getValue())) {
			index++;
			if (index < colValList.size()) {
				if (breakDataList != null) {
					boolean isBreak = new BLHelper(context)
							.checkBreakConditionBL(ForList.get(0).initTableName,
									ForList.get(0).initcolumnName,
									colValList.get(index), breakDataList);
					if (isBreak) {
						rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
					} else {
						if (transUsidList != null) {
							transUsidList.clear();
							transUsidList.add(colValList.get(index));
						} else {
							transUsidList = new ArrayList<String>();
							transUsidList.add(colValList.get(index));
						}

						new BLExecutorActionCenter(context, mContainerId,
								BLFORAsyncTaskHelper.this, -1, transUsidList)
								.doBLActionType(blDataList);
					}
				}

			} else {
				Log.d("BL FOR", "All FORs Done");
				rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
			}
		}
		else
		{
			Log.i("BL FOR", "failed :"+result);
		}

	}

}

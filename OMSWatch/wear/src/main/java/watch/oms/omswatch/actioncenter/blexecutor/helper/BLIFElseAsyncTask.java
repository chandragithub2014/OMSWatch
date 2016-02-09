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
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorActionCenter;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLConnectivityOperatorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLDestinationDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLExecutorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLFalseDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLIFProcessorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperandDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperandDataDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperatorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLStartDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLTrueDTO;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.R;


public class BLIFElseAsyncTask implements OMSReceiveListener {
	private final String TAG = this.getClass().getSimpleName();
	public List<String> conditionaList;
	public List<Object> expressionsList;
	public String destinationTableName;
	public String destinationType;
	public String destinationColumn;
	public String destinationPrimaryKey;

	private BLStartDTO newstart;
	public List<BLOperandDTO> tempnewoperandList;
	private String final_new_result = null;
	private int newIfCount = 0;

	private Activity context;
	private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private OMSReceiveListener rListener;
	private String uniqueId = null;
	private ProgressDialog pDialog;
	private List<String> transUsidList;
	boolean ifResult = false;
	private List<BLIFProcessorDTO> ifProcessorList;
	private BLIFProcessorDTO ifprocessorData;
	private List<Boolean> boolOperandList;
	private List<BLExecutorDTO> blDataList;
	private List<BLTrueDTO> newTrueList;
	private List<BLFalseDTO> newFalseList;
	private int appId;

	public BLIFElseAsyncTask(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList,int appId) {
		context = FragmentContext;
		mContainerId = containerId;
		rListener = receiveListener;
		this.transUsidList = transUsidList;
		this.appId = appId;
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getResources().getString(R.string.bl_if));
		if(!pDialog.isShowing())
		pDialog.show();
	}

	public void processAddBL(String targetXml) {

		if (transUsidList != null && transUsidList.size() > 0) {
			uniqueId = transUsidList.get(0);
		}
		initializeLists();
		try {
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);

			try {
				expressionsList = xmlParser.parseAddBL(response, OMSDatabaseConstants.BL_TYPE_IF);
				processDestinationList(xmlParser.getDestList());
				newTrueList = xmlParser.getTrueList();
				newFalseList = xmlParser.getFalseList();
				Log.i("BLIF:::::", "" + expressionsList);
			} catch (XmlPullParserException e) {
				Log.e(TAG, "Error occurred in method processAddBL. Error is:"
						+ e.getMessage());
				e.printStackTrace();
			}
		} catch (IOException e) {
			Log.e(TAG,
					"Error occurred in method processAddBL. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}

		processExpressionsList(expressionsList);
		
		Log.i("TAG", "Final Result:::::::::" + ifResult);
		if (ifResult) {
			if (newTrueList.size() > 0) {
				blDataList = new BLHelper(context).getBLDetailsBasedOnUsid(
						newTrueList.get(0).colVal, 0);
				if (blDataList != null && blDataList.size() > 0) {
					if (pDialog.isShowing()) {
						pDialog.dismiss();
					}
		
					BLExecutorActionCenter blExecutorActionCenter = new BLExecutorActionCenter(context, mContainerId,
						rListener, appId, transUsidList);
					blExecutorActionCenter.doBLAction(newTrueList.get(0).colVal,
									"");
				}
			}
		} else {

			if (newFalseList.size() > 0) {
				blDataList = new BLHelper(context).getBLDetailsBasedOnUsid(
						newFalseList.get(0).colVal, 0);
				if (blDataList != null && blDataList.size() > 0) {
					if (pDialog.isShowing()) {
						pDialog.dismiss();
					}
					
					BLExecutorActionCenter blExecutorActionCenter = new BLExecutorActionCenter(context, mContainerId,
							rListener, appId, transUsidList);
					blExecutorActionCenter.doBLAction(newFalseList.get(0).colVal,"");
				}
			}
		}
	}

	private void initializeLists() {
		new ArrayList<BLOperandDTO>();
		new ArrayList<BLStartDTO>();
		new ArrayList<Object>();
		ifProcessorList = new ArrayList<BLIFProcessorDTO>();
		boolOperandList = new ArrayList<Boolean>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processExpressionsList(List<Object> expressionList) {
		if (expressionList != null) {
			for (int i = 0; i < expressionList.size(); i++) {
				if (expressionList.get(i) instanceof BLOperatorDTO) {
					newstart = new BLStartDTO();
					newstart.operator = (BLOperatorDTO) expressionList.get(i);
				}

				else if (expressionList.get(i) instanceof BLConnectivityOperatorDTO) {
					ifprocessorData = new BLIFProcessorDTO();
					ifprocessorData.connectivityOperator = (BLConnectivityOperatorDTO) expressionList
							.get(i);
				}

				else if (((List) expressionList.get(i)).get(0)!=null && ((List) expressionList.get(i)).get(0) instanceof BLStartDTO) {

					List<BLStartDTO> startList = (List<BLStartDTO>) expressionList.get(i);
					processStartExpressionList(startList);
				}

			}
		}
	}

	public void processStartExpressionList(List<BLStartDTO> startList) {
		if (startList != null) {
			for (int i = 0; i < startList.size(); i++) {
				BLOperatorDTO operator = startList.get(i).operator;
				List<BLOperandDTO> operandList = startList.get(i).operandList;
				evaluateExpression(operandList, operator);
			}
		}
	}

	private void evaluateExpression(List<BLOperandDTO> operandList, BLOperatorDTO operator) {
		List<BLOperandDataDTO> operandDataList = null;
		if (operandList != null) {
			operandDataList = getColumnValInfo(operandList);
		}
		if (operandDataList != null) {
			getFinalIfResult(operandDataList, operator);
		}
	}

	private void getFinalIfResult(List<BLOperandDataDTO> operandDataList,
			BLOperatorDTO operator) {
		if (operandDataList.size() == 2) {
			if ((operandDataList.get(0).operandType
					.equalsIgnoreCase(OMSMessages.INTEGER.getValue()))
					&& (operandDataList.get(1).operandType
							.equalsIgnoreCase(OMSMessages.INTEGER.getValue()))) {
				if (operator.operatorName.equalsIgnoreCase(OMSMessages.GREATER
						.getValue())) {
					if (Integer.parseInt(operandDataList.get(0).operandVal) > Integer
							.parseInt(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}
				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.LESSER.getValue())) {
					if (Integer.parseInt(operandDataList.get(0).operandVal) < Integer
							.parseInt(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}
				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.EQUALS.getValue()) || operator.operatorName
						.equalsIgnoreCase("equals")) {

					if (Integer.parseInt(operandDataList.get(0).operandVal) == Integer
							.parseInt(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}

				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.NOT_EQUAL_TO.getValue()) ) {

					if (Integer.parseInt(operandDataList.get(0).operandVal) != Integer
							.parseInt(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}

				}
			} else if ((operandDataList.get(0).operandType
					.equalsIgnoreCase(OMSMessages.REAL.getValue()))
					&& (operandDataList.get(1).operandType
							.equalsIgnoreCase(OMSMessages.REAL.getValue()))) {

				if (operator.operatorName.equalsIgnoreCase(OMSMessages.GREATER
						.getValue())) {
					if (Double.parseDouble(operandDataList.get(0).operandVal) > Double
							.parseDouble(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}
				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.LESSER.getValue())) {
					if (Double.parseDouble(operandDataList.get(0).operandVal) < Double
							.parseDouble(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}
				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.EQUALS.getValue())) {

					if (Double.parseDouble(operandDataList.get(0).operandVal) == Double
							.parseDouble(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}

				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.NOT_EQUAL_TO.getValue())) {

					if (Double.parseDouble(operandDataList.get(0).operandVal) != Double
							.parseDouble(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}

				}

			}

			else if ((operandDataList.get(0).operandType
					.equalsIgnoreCase(OMSMessages.REAL.getValue()))
					&& (operandDataList.get(1).operandType
							.equalsIgnoreCase(OMSMessages.INTEGER.getValue()))) {

				if (operator.operatorName.equalsIgnoreCase(OMSMessages.GREATER
						.getValue())) {
					if (Double.parseDouble(operandDataList.get(0).operandVal) > Integer
							.parseInt(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}
				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.LESSER.getValue())) {
					if (Double.parseDouble(operandDataList.get(0).operandVal) < Integer
							.parseInt(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}
				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.EQUALS.getValue())) {

					if (Double.parseDouble(operandDataList.get(0).operandVal) == Integer
							.parseInt(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}

				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.NOT_EQUAL_TO.getValue())) {

					if (Double.parseDouble(operandDataList.get(0).operandVal) != Integer
							.parseInt(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}

				}

			} else if ((operandDataList.get(0).operandType
					.equalsIgnoreCase(OMSMessages.INTEGER.getValue()))
					&& (operandDataList.get(1).operandType
							.equalsIgnoreCase(OMSMessages.REAL.getValue()))) {

				if (operator.operatorName.equalsIgnoreCase(OMSMessages.GREATER
						.getValue())) {
					if (Integer.parseInt(operandDataList.get(0).operandVal) > Double
							.parseDouble(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}
				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.LESSER.getValue())) {
					if (Integer.parseInt(operandDataList.get(0).operandVal) < Double
							.parseDouble(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}
				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.EQUALS.getValue())) {

					if (Integer.parseInt(operandDataList.get(0).operandVal) == Double
							.parseDouble(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}

				} else if (operator.operatorName
						.equalsIgnoreCase(OMSMessages.NOT_EQUAL_TO.getValue())) {

					if (Integer.parseInt(operandDataList.get(0).operandVal) != Double
							.parseDouble(operandDataList.get(1).operandVal)) {
						ifResult = true;
					} else {
						ifResult = false;
					}

				}

			}
		}

		processIfExpression(ifResult);

	}

	// Return OPerandVal with its data type in list
	private List<BLOperandDataDTO> getColumnValInfo(List<BLOperandDTO> operandList) {
		List<BLOperandDataDTO> operandDataList = new ArrayList<BLOperandDataDTO>();
		for (int i = 0; i < operandList.size(); i++) {

			String colVal = getColValForOperand(
					operandList.get(i).operandTableName,
					operandList.get(i).operandPrimaryKey,
					operandList.get(i).operandType,
					operandList.get(i).constantValue,
					operandList.get(i).operandColumnName);
			BLOperandDataDTO tempOperandData = new BLOperandDataDTO();
			tempOperandData.operandVal = colVal;
			tempOperandData.operandType = operandList.get(i).operandType;
			operandDataList.add(tempOperandData);
		}
		return operandDataList;
	}

	private String getColValForOperand(String operandTableName,
			String operandPrimaryKey, String operandType, String constantValue,
			String operandColumnName) {
		Object val = null;
		if (operandType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
			if (operandColumnName
					.equalsIgnoreCase(OMSMessages.CONSTANT.getValue())) {
				val = (Integer) Integer.parseInt(constantValue);
			} else if (operandPrimaryKey.equalsIgnoreCase(OMSMessages.CONSTANT
					.getValue())) {
				uniqueId = constantValue;
				String value = new BLHelper(context).getColuValForColName(
						operandTableName, operandColumnName, null, operandType,
						operandPrimaryKey);
				val = (Integer) Integer.parseInt(value);
			} else {
				String value = new BLHelper(context).getColuValForColName(
						operandTableName, operandColumnName, uniqueId,
						operandType, operandPrimaryKey);
				val = (Integer) Integer.parseInt(value);
			}
		} else if (operandType.equalsIgnoreCase(OMSMessages.REAL.getValue())) {
			if (operandColumnName
					.equalsIgnoreCase(OMSMessages.CONSTANT.getValue())) {
				val = (Integer) Integer.parseInt(constantValue);
			} else if (operandPrimaryKey.equalsIgnoreCase(OMSMessages.CONSTANT
					.getValue())) {
				uniqueId = constantValue;
				String value = new BLHelper(context).getColuValForColName(
						operandTableName, operandColumnName, null, operandType,
						operandPrimaryKey);
				val = (Double) Double.parseDouble(value);
			} else {
				String value = new BLHelper(context).getColuValForColName(
						operandTableName, operandColumnName, uniqueId,
						operandType, operandPrimaryKey);
				val = (Double) Double.parseDouble(value);
			}
		}else if (operandType.equalsIgnoreCase(OMSMessages.TEXT.getValue())) {
			if (operandColumnName
					.equalsIgnoreCase(OMSMessages.CONSTANT.getValue())) {
				val = (Integer) Integer.parseInt(constantValue);
			} else if (operandPrimaryKey.equalsIgnoreCase(OMSMessages.CONSTANT
					.getValue())) {
				uniqueId = constantValue;
				String value = new BLHelper(context).getColuValForColName(
						operandTableName, operandColumnName, null, operandType,
						operandPrimaryKey);
				val = (Double) Double.parseDouble(value);
			} else {
				String value = new BLHelper(context).getColuValForColName(
						operandTableName, operandColumnName, uniqueId,
						operandType, operandPrimaryKey);
				val = (Double) Double.parseDouble(value);
			}
		}
		 
		else if (operandPrimaryKey.equalsIgnoreCase(OMSMessages.CONSTANT
				.getValue())) {
			uniqueId = constantValue;
			String value = new BLHelper(context).getColuValForColName(
					operandTableName, operandColumnName, null, operandType,
					operandPrimaryKey);
			val = (Double) Double.parseDouble(value);
		}
		
		return val.toString();
	}

	public void processDestinationList(List<BLDestinationDTO> destList) {
		if (destList != null) {
			for (int i = 0; i < destList.size(); i++) {
				destinationTableName = destList.get(i).destinataionTableName;
				destinationType = destList.get(i).destinationType;
				destinationColumn = destList.get(i).destinationColumnName;
				destinationPrimaryKey = destList.get(i).destinationPrimaryKey;
			}
		}
	}

	public void processTrueList(List<BLTrueDTO> trueList) {
		if (trueList != null) {

		}
	}

	public void processFalseList(List<BLFalseDTO> falseList) {
		if (falseList != null) {

		}
	}

	private void processIfExpression(boolean final_boolean) {

		boolOperandList.add(final_boolean);
		if (boolOperandList.size() == 2) {
			ifprocessorData.boolOperandList = boolOperandList;
			ifProcessorList.add(ifprocessorData);
			newIfCount = 0;
			boolOperandList = new ArrayList<Boolean>();
			processConnectorList(ifProcessorList);
		}/* else {
			newIfCount++;
		}*/

	}

	private void processConnectorList(List<BLIFProcessorDTO> ifProcessorList) {
		if (ifProcessorList.size() > 0) {
			List<Boolean> ifConnectivityOperatorList = ifProcessorList.get(0).boolOperandList;
			BLConnectivityOperatorDTO connOperator = ifProcessorList.get(0).connectivityOperator;

			if (ifConnectivityOperatorList.size() == 2) {
				if (connOperator.connectivityoperatorName
						.equalsIgnoreCase(OMSMessages.AND.getValue())) {
					if (ifConnectivityOperatorList.get(0)
							&& ifConnectivityOperatorList.get(1)) {
						ifResult = true;
						ifProcessorList = new ArrayList<BLIFProcessorDTO>();
					} else {
						ifResult = false;
						ifProcessorList = new ArrayList<BLIFProcessorDTO>();
					}
				} else if (connOperator.connectivityoperatorName
						.equalsIgnoreCase(OMSMessages.OR.getValue())) {
					if (ifConnectivityOperatorList.get(0)
							|| ifConnectivityOperatorList.get(1)) {
						ifResult = true;
						ifProcessorList = new ArrayList<BLIFProcessorDTO>();
					} else {
						ifResult = false;
						ifProcessorList = new ArrayList<BLIFProcessorDTO>();
					}
				}
				boolOperandList.add(ifResult);
			}
		}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void processFinalExpressionsList(List<Object> expressionList) {
		if (expressionList != null) {
			for (int i = 0; i < expressionList.size(); i++) {
				if (expressionList.get(i) instanceof BLOperatorDTO) {
					newstart = new BLStartDTO();
					newstart.operator = (BLOperatorDTO) expressionList.get(i);
				} else if (expressionList.get(i) instanceof BLConnectivityOperatorDTO) {
					ifprocessorData = new BLIFProcessorDTO();
					ifprocessorData.connectivityOperator = (BLConnectivityOperatorDTO) expressionList
							.get(i);
				} else if (((List) expressionList.get(i)).get(0) instanceof BLStartDTO) {
					List<BLStartDTO> startList = (List<BLStartDTO>) expressionList.get(i);
					processStartExpressionList(startList);
				}
			}
		}
	}

	@Override
	public void receiveResult(String result) {
		//rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
	}

}
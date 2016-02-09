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
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import watch.oms.omswatch.R;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLDestinationDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLExecutorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLNextDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperandDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperatorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLStartDTO;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;


public class TableBLExpressionAsyncTask extends AsyncTask<String, Void, String> {
	private final String TAG = this.getClass().getSimpleName();
	public List<String> conditionaList;
	public List<Object> expressionsList;
	public String destinationTableName;
	public String destinationType;
	public String destinationColumn;
	public String destinationPrimaryKey;

	public BLStartDTO newstart;
	public List<BLOperandDTO> newoperandList;
	public List<BLOperandDTO> tempnewoperandList;
	public List<BLStartDTO> newexpressionList;
	public List<Object> newexpressionsList;
	public String final_new_result = null;
	public int newCount = 0;

	private Activity context;
	private OMSReceiveListener rListener;
	public String uniqueId = null;
	public ProgressDialog pDialog;
	private ArrayList<String> transUsidList;
	public List<BLNextDTO> nextDataList;
	public List<BLExecutorDTO> blDataList;

	public TableBLExpressionAsyncTask(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, ArrayList<String> transUsidList) {
		context = FragmentContext;
		rListener = receiveListener;
		this.transUsidList = transUsidList;
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getResources().getString(
				R.string.bl_expression));
	//	pDialog.show();
	}

	public void processAddBL(String targetXml) {
		if (transUsidList != null && transUsidList.size() > 0) {
			Log.d(OMSDatabaseConstants.BL_TYPE_EXPRESSION, "TransUsid List Size:::" + transUsidList.size());
			uniqueId = transUsidList.get(0);
		} else {
			uniqueId = null;
		}
		initializeLists();
		try {
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);

			try {
				expressionsList = xmlParser.parseAddBL(response,OMSDatabaseConstants.BL_TYPE_EXPRESSION);
				processDestinationList(xmlParser.getDestList());
				nextDataList = xmlParser.getExpressionNextList();
				Log.i("BLIF:::::", "" + expressionsList);
			} catch (XmlPullParserException e) {
				Log.e(TAG,
						"Exception Occured in processAddBL: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (IOException e) {
			Log.e(TAG, "Exception Occured in processAddBL: " + e.getMessage());
			e.printStackTrace();
		}
		processExpressionsList(expressionsList);
	}

	private void initializeLists() {
		newoperandList = new ArrayList<BLOperandDTO>();
		newexpressionList = new ArrayList<BLStartDTO>();
		newexpressionsList = new ArrayList<Object>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processExpressionsList(List<Object> expressionList) {
		if (expressionList != null) {
			for (int i = 0; i < expressionList.size(); i++) {
				if (expressionList.get(i) instanceof BLOperatorDTO) {
					newstart = new BLStartDTO();
					newstart.operator = (BLOperatorDTO) expressionList.get(i);
				} else if (((List) expressionList.get(i)).get(0) instanceof BLStartDTO) {

					List<BLStartDTO> startList = (List<BLStartDTO>) expressionList.get(i);
					processStartExpressionList(startList);
				}
			}
		}
		Log.i("TAG", "Final Result:::::::::" + final_new_result);
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
		
		List<String> operandValList = null;
		Cursor cursor=null;
		try{
		if(operator.operatorName.equalsIgnoreCase("Sigma"))
		{
			BLOperandDTO operand1 =  operandList.get(0);
			String table = operand1.operandTableName;
			String column = operand1.operandColumnName;
			String columnType = operand1.operandType;
			List<String> sigmaOperandValList = new ArrayList<String>();
		//	Cursor cursor =OMSDBManager.getTransDB().query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
			 cursor = TransDatabaseUtil.query(table, new String[]{column}, OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
					 + " <> '1'", null, null, null, null);
			if(cursor!=null && cursor.moveToFirst())
				do{
					String val = null;
					if(columnType.equalsIgnoreCase("TEXT") || columnType.equalsIgnoreCase("BIGINT"))
						val = cursor.getString(cursor.getColumnIndex(column));
					else if(columnType.equalsIgnoreCase("INTEGER"))
						val = Integer.toString(cursor.getInt(cursor.getColumnIndex(column)));
					else if(columnType.equalsIgnoreCase("REAL"))
						val = Double.toString(cursor.getDouble(cursor.getColumnIndex(column)));
					if(val!=null)
						sigmaOperandValList.add(val);
				}while(cursor.moveToNext());
			getFinalResult(sigmaOperandValList, operator, columnType);
		}
		else if (operandList != null) {
			operandValList = getColumnVal(operandList);
			if (operandValList != null && destinationType != null) {
				getFinalResult(operandValList, operator, destinationType);
			}
		}
		}
		catch(Exception e){
			Log.d(TAG,"Exception in evaluateExpression"+e.getMessage());
		}
		finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}

	private void getFinalResult(List<String> operandValList, BLOperatorDTO operator,
			String destinationType2) {
		if (operator.operatorName != null) {
			if (operator.operatorName.equalsIgnoreCase(OMSMessages.ADD.getValue())|| operator.operatorName.equalsIgnoreCase(OMSMessages.SIGMA.getValue())) {
				if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
					int result = 0;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								+ Integer.parseInt(operandValList.get(i));
					}
					final_new_result = Integer.toString(result);
				} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {
					double result = 0;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								+ Double.parseDouble(operandValList.get(i));
					}
					final_new_result = Double.toString(result);
				}else if (destinationType2.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || destinationType2.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
					int result = 0;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								+ Integer.parseInt(operandValList.get(i));
					}
					final_new_result = Integer.toString(result);
				}
			}

			else if (operator.operatorName.equalsIgnoreCase(OMSMessages.MULTIPLY
					.getValue())) {
				if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
					int result = 1;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								* Integer.parseInt(operandValList.get(i));
					}
					final_new_result = Integer.toString(result);
				} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {
					double result = 1.0;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								* Double.parseDouble(operandValList.get(i));
					}

					final_new_result = Double.toString(result);
				}else if (destinationType2.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || destinationType2.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
					int result = 1;
					for (int i = 0; i < operandValList.size(); i++) {
//						if(!TextUtils.isEmpty(operandValList.get(i)))
						{
						result = result
								* Integer.parseInt(operandValList.get(i));
						final_new_result = Integer.toString(result);
						}
					}
					
				}
			}

			else if (operator.operatorName.equalsIgnoreCase(OMSMessages.SUBTRACT
					.getValue())) {
				if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
					int result = 0;
					for (int i = 0; i < operandValList.size(); i++) {
						int temp = Integer.parseInt(operandValList.get(i));
						if (result > temp) {
							//if(!TextUtils.isEmpty(operandValList.get(i)))
							result = result
									- Integer.parseInt(operandValList.get(i));
						} else {
							result = temp - result;
						}
					}
					final_new_result = Integer.toString(result);
				} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {
					double result = 0.0;
					for (int i = 0; i < operandValList.size(); i++) {
						double temp = Double.parseDouble(operandValList.get(i));
						if (result > temp) {
							//if(!TextUtils.isEmpty(operandValList.get(i)))
							result = result
									- Double.parseDouble(operandValList.get(i));
						} else {
							result = temp - result;
						}
					}
				}
			} else if (operator.operatorName.equalsIgnoreCase(OMSMessages.DIVISION
					.getValue())) {
				if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
					int result = 1;
					for (int i = 0; i < operandValList.size(); i++) {
						int temp = Integer.parseInt(operandValList.get(i));
						if (result > temp) {
							//if(!TextUtils.isEmpty(operandValList.get(i)))
							result = (result)
									/ (Integer.parseInt(operandValList.get(i)));
						} else {
							result = (temp) / (result);
						}
					}
				} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {
					double result = 1.0;
					for (int i = 0; i < operandValList.size(); i++) {
						double temp = Double.parseDouble(operandValList.get(i));
						if (result > temp) {
							//if(!TextUtils.isEmpty(operandValList.get(i)))
							result = (result)
									/ (Double
											.parseDouble(operandValList.get(i)));
						} else {
							result = (temp) / (result);
						}
					}

				}
			}
		}/*else if (operator.operatorName.equalsIgnoreCase(OMSMessages.SIGMA.getValue())) {
			
		} */
		else {
			if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue()) || destinationType2.equalsIgnoreCase(OMSMessages.TEXT.getValue())) {
				int result = 0;
				for (int i = 0; i < operandValList.size(); i++) {
					result = Integer.parseInt(operandValList.get(i));
				}
				final_new_result = Integer.toString(result);
			} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {
				double result = 1.0;
				for (int i = 0; i < operandValList.size(); i++) {
					//if(!TextUtils.isEmpty(operandValList.get(i)))
					result = Double.parseDouble(operandValList.get(i));
				}

				final_new_result = Double.toString(result);
			}
		}
		processfinalResult(final_new_result, destinationType2);

	}

	private List<String> getColumnVal(List<BLOperandDTO> operandList) {
		List<String> operandValList = new ArrayList<String>();
		for (int i = 0; i < operandList.size(); i++) {

			String colVal = getColValForOperand(
					operandList.get(i).operandTableName,
					operandList.get(i).operandPrimaryKey,
					operandList.get(i).operandType,
					operandList.get(i).constantValue,
					operandList.get(i).operandColumnName);
			operandValList.add(colVal);
		}
		return operandValList;

	}

	private String getColValForOperand(String operandTableName,
			String operandPrimaryKey, String operandType, String constantValue,
			String operandColumnName) {
		Object val = "";
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
				val = (Double) Double.parseDouble(constantValue);
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

	private void processfinalResult(String final_new_result2,
			String destinationType) {
		if (final_new_result2 != null) {
			BLOperandDTO newoperand = new BLOperandDTO();
			newoperand.operandColumnName = OMSMessages.CONSTANT.getValue();
			newoperand.constantValue = final_new_result2;
			newoperand.operandType = destinationType;
			newoperandList.add(newoperand);
			if (newCount == 1) {
				newstart.operandList = newoperandList;
				newexpressionList.add(newstart);
				newexpressionsList.add(newexpressionList);
				newCount = 0;
				newoperandList = new ArrayList<BLOperandDTO>();
				processFinalExpressionsList(newexpressionsList);

			} else {
				newexpressionList.clear();
				newexpressionsList.clear();
				newCount++;
			}
		}

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processFinalExpressionsList(List<Object> expressionList) {
		if (expressionList != null) {
			for (int i = 0; i < expressionList.size(); i++) {
				if (expressionList.get(i) instanceof BLOperatorDTO) {
					newstart = new BLStartDTO();
					newstart.operator = (BLOperatorDTO) expressionList.get(i);
				} else if (((List) expressionList.get(i)).get(0) instanceof BLStartDTO) {

					List<BLStartDTO> startList = (List<BLStartDTO>) expressionList.get(i);
					processStartExpressionList(startList);
				}

			}

		}

		Log.i("TAG", "Final Result:::::::::" + final_new_result);
	}

	@Override
	protected String doInBackground(String... args) {
		String xml_file = args[0];
		processAddBL(xml_file);
		if (final_new_result != null) 
		{
			if (destinationTableName != null && !destinationTableName.equalsIgnoreCase("")) {
				if (destinationPrimaryKey.equalsIgnoreCase(OMSMessages.CONSTANT.getValue())) {
					ContentValues cvalss = new ContentValues();
					cvalss.put(destinationColumn, final_new_result);
					new BLHelper(context).insertOrUpdateData(
							destinationTableName, null, cvalss);
				} else {
					ContentValues cvals = new ContentValues();
					cvals.put(destinationColumn, final_new_result);
					if (uniqueId != null) {
						new BLHelper(context).insertOrUpdateData(
								destinationTableName, uniqueId, cvals);
					}

				}
			}
		}
		return final_new_result;
	}

	@Override
	protected void onPostExecute(String result) {
//		Toast.makeText(context, "final_new_result:::" + result,
//				Toast.LENGTH_LONG).show();
	/*	if (nextDataList != null) {
			if (nextDataList.size() > 0) {
				blDataList = new BLHelper(context).getBLDetailsBasedOnUsid(
						nextDataList.get(0).colVal, 0);
				if (blDataList != null && blDataList.size() > 0) {
					new BLExecutorActionCenter(context, mContainerId, rListener, -1,
							transUsidList).doBLActionType(blDataList);
				}
			}
		}*/
	/*	if(pDialog!=null){
			pDialog.dismiss();
		}*/
		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}
		if (rListener != null) {
			rListener.receiveResult(OMSMessages.TABLE_BL_SUCCESS.getValue());
		}
		else
			rListener.receiveResult(OMSMessages.TABLE_BL_FAILURE.getValue());
		

	}
	
	public String getResult(String blXML){
		String xml_file = blXML;
		processAddBL(xml_file);
		if (final_new_result != null) 
		{
			if (destinationTableName != null && !destinationTableName.equalsIgnoreCase("")) {
				if (destinationPrimaryKey.equalsIgnoreCase(OMSMessages.CONSTANT.getValue())) {
					ContentValues cvalss = new ContentValues();
					cvalss.put(destinationColumn, final_new_result);
					new BLHelper(context).insertOrUpdateData(
							destinationTableName, null, cvalss);
				} else {
					ContentValues cvals = new ContentValues();
					cvals.put(destinationColumn, final_new_result);
					if (uniqueId != null) {
						new BLHelper(context).insertOrUpdateData(
								destinationTableName, uniqueId, cvals);
					}

				}
			}
		}
		/*if(pDialog!=null){
			pDialog.dismiss();
		}*/
		return final_new_result;
	}

}
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
import java.util.Hashtable;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.R;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorActionCenter;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLDestinationDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLExecutorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLNextDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperandDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperatorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLStartDTO;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;


public class BLExpressionAsyncTask extends AsyncTask<String, Void, String> {
	private final String TAG = this.getClass().getSimpleName();
	public List<String> conditionaList;
	public List<Object> expressionsList;
	public String destinationTableName;
	public String destinationType;
	public String destinationColumn;
	public String destinationPrimaryKey;
    public boolean destinationIsGlobal;
	public BLStartDTO newstart;
	public List<BLOperandDTO> newoperandList;
	public List<BLOperandDTO> tempnewoperandList;
	public List<BLStartDTO> newexpressionList;
	public List<Object> newexpressionsList;
	public String final_new_result = null;
	public int newCount = 0;

	private Activity context;
	private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private OMSReceiveListener rListener;
	public String uniqueId = null;
	public ProgressDialog pDialog;
	private List<String> transUsidList;
	public List<BLNextDTO> nextDataList;
	public List<BLExecutorDTO> blDataList;
	public boolean isForceUpdate;
	BLGlobalValueGenerator blGlobalValGenerator;
	public BLExpressionAsyncTask(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList) {
		context = FragmentContext;
		mContainerId = containerId;
		rListener = receiveListener;
		this.transUsidList = transUsidList;
		blGlobalValGenerator = new BLGlobalValueGenerator();
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getResources().getString(
				R.string.bl_expression));
		pDialog.show();
	}

	@SuppressWarnings("unchecked")
	public void processAddBL(String targetXml) throws Exception {
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);

			expressionsList = xmlParser.parseAddBL(response, OMSDatabaseConstants.BL_TYPE_EXPRESSION);
			isForceUpdate = xmlParser.isForceUpdate();
			processDestinationList(xmlParser.getDestList());
			nextDataList = xmlParser.getExpressionNextList();
			Log.i("BLIF:::::", "" + expressionsList);
			
		if (transUsidList != null && transUsidList.size() > 0) {
			Log.d(OMSDatabaseConstants.BL_TYPE_EXPRESSION, "TransUsid List Size:::" + transUsidList.size());
			
			for(int i = 0; i < transUsidList.size(); i++)
			{
				initializeLists();
				uniqueId = null;
				final_new_result = null;
				uniqueId = transUsidList.get(i);
				processExpressionsList(expressionsList);
				if (final_new_result != null) {
					if (destinationTableName != null) {
						//For Global result
						if(destinationIsGlobal){
							if(!TextUtils.isEmpty(destinationColumn) && !destinationColumn.equalsIgnoreCase("constant")){
								@SuppressWarnings("unchecked")
								Hashtable<String,Object> tempGlobalHash= OMSApplication
										.getInstance().getGlobalBLHash();
								if(tempGlobalHash!=null){
									tempGlobalHash.put(destinationColumn, final_new_result);
									OMSApplication.getInstance().setGlobalBLHash(tempGlobalHash);
								}
								/*String globalVal = blGlobalValGenerator.getGlobalBLValue(destinationColumn);
					    		   Object val = (Double) Double.parseDouble(globalVal);
					    		   final_new_result = val.toString();*/
					    		   
					    		    if(isForceUpdate){
					    		    ContentValues cvalss = new ContentValues();
									cvalss.put("status", "fresh");
									cvalss.put(destinationColumn, final_new_result);
									new BLHelper(context).updateForceUpdatedFirstRow(
											destinationTableName, null, cvalss);
					    		    }else{
					    		    	ContentValues cvals = new ContentValues();
										cvals.put("status", "fresh");
										cvals.put(destinationColumn, final_new_result);
										if (uniqueId != null) {
											new BLHelper(context).insertOrUpdateData(
													destinationTableName, uniqueId, cvals);
										}
					    		    }
					    		   
					    	   }
						}else if(isForceUpdate && !destinationIsGlobal){
							ContentValues cvalss = new ContentValues();
							cvalss.put("status", "fresh");
							cvalss.put(destinationColumn, final_new_result);
							new BLHelper(context).updateForceUpdatedFirstRow(
									destinationTableName, null, cvalss);
						}
						else if (destinationColumn.equalsIgnoreCase(OMSMessages.CONSTANT.getValue())) {
							ContentValues cvalss = new ContentValues();
							cvalss.put("status", "fresh");
							cvalss.put(destinationColumn, final_new_result);
							new BLHelper(context).insertOrUpdateData(
									destinationTableName, null, cvalss);
						} else {
							ContentValues cvals = new ContentValues();
							cvals.put("status", "fresh");
							cvals.put(destinationColumn, final_new_result);
							if (uniqueId != null) {
								new BLHelper(context).insertOrUpdateData(
										destinationTableName, uniqueId, cvals);
							}
						}
					}
				}
			}
		} else {
			uniqueId = null;
		}
	}

	private void initializeLists() {
		newCount = 0;
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
				} else if (((List) expressionList.get(i)) != null && ((List) expressionList.get(i)).get(0) instanceof BLStartDTO) {

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
		List<String> operandValList = null;
		if (operandList != null) {
			operandValList = getColumnVal(operandList);
		}
		if (operandValList != null && destinationType != null) {
			getFinalResult(operandValList, operator, destinationType);
		}
	}

	private void getFinalResult(List<String> operandValList, BLOperatorDTO operator,
			String destinationType2) {
		if (operator.operatorName != null) {
			destinationType2=destinationType2.trim();
			if (operator.operatorName.equalsIgnoreCase(OMSMessages.ADD.getValue())) {
/*				if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
					int result = 0;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								+ Integer.parseInt(operandValList.get(i));
					}
					final_new_result = Integer.toString(result);
				} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {*/
					double result = 0;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								+ Double.parseDouble(operandValList.get(i));
					}
					final_new_result = Double.toString(result);
				/*} else{

					int result = 0;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								+ Integer.parseInt(operandValList.get(i));
					}
					final_new_result = Integer.toString(result);
				
				}*/
			}
			else if (operator.operatorName.equalsIgnoreCase(OMSMessages.MULTIPLY
					.getValue())) {
/*				if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
					int result = 1;
					for (int i = 0; i < operandValList.size(); i++) {
						String valueString = operandValList.get(i);
						if(valueString.contains("."))
							valueString = valueString.substring(0, valueString.indexOf("."));
						result = result
								* Integer.parseInt(valueString);
					}
					final_new_result = Integer.toString(result);
				} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {*/
					double result = 1.0;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								* Double.parseDouble(operandValList.get(i));
					}

					final_new_result = Double.toString(result);
				/*} else{
					int result = 1;
					for (int i = 0; i < operandValList.size(); i++) {
						result = result
								* Integer.parseInt(operandValList.get(i));
					}

					final_new_result = Integer.toString(result);
				}*/
			}
			else if (operator.operatorName.equalsIgnoreCase(OMSMessages.SUBTRACT
					.getValue())) {
/*				if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
					int result = 0;
					int i = 0;
					int j = 0;
					if(operandValList.get(0) != null)
						i = Integer.parseInt(operandValList.get(0));
					
					if(operandValList.get(1) != null)
						j = Integer.parseInt(operandValList.get(1));
					
					result = i - j;
					final_new_result = Integer.toString(result);
				} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {*/
					double result = 0.0;
					double i  = 0.0;
					double j  = 0.0;
					if(operandValList.get(0) != null)
						i = Double.parseDouble(operandValList.get(0));
					
					if(operandValList.get(1) != null)
						j = Double.parseDouble(operandValList.get(1));
					
					result = i - j;
					final_new_result = Double.toString(result);
				/*} else{
					int result = 0;
					int i = 0;
					int j = 0;
					if(operandValList.get(0) != null)
						i = Integer.parseInt(operandValList.get(0));
					
					if(operandValList.get(1) != null)
						j = Integer.parseInt(operandValList.get(1));
					
					result = i - j;
					final_new_result = Integer.toString(result);
				
				}*/
			} else if (operator.operatorName.equalsIgnoreCase(OMSMessages.DIVISION
					.getValue())) {
/*				if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
					int result = 1;
					int i = 1;
					int j = 1;
					if(operandValList.get(0) != null)
						i = Integer.parseInt(operandValList.get(0));
					
					if(operandValList.get(1) != null)
						j = Integer.parseInt(operandValList.get(1));
					
					if(j == 0){
						final_new_result = "invalid expression";
					}else{ 
						result = i / j;
						final_new_result = Integer.toString(result);
					}
					
				} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {*/
					double result = 1.0;
					double i = 1.0;
					double j  = 1.0;
					if(operandValList.get(0) != null)
						i =  Double.parseDouble(operandValList.get(0));
					
					if(operandValList.get(1) != null)
						j =  Double.parseDouble(operandValList.get(1));
					
					if(j == 0){
						final_new_result = "invalid expression";
					}else{ 
						result = i / j;
						final_new_result = Double.toString(result);
					}

			/*	} else{
					int result = 1;
					int i = 1;
					int j = 1;
					if(operandValList.get(0) != null)
						i = Integer.parseInt(operandValList.get(0));
					
					if(operandValList.get(1) != null)
						j = Integer.parseInt(operandValList.get(1));
					
					if(j == 0){
						final_new_result = "invalid expression";
					}else{ 
						result = i / j;
						final_new_result = Integer.toString(result);
					}
				}*/
				
			}
		}/* else {
			if (destinationType2.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
				int result = 0;
				for (int i = 0; i < operandValList.size(); i++) {
					result = Integer.parseInt(operandValList.get(i));
				}
				final_new_result = Integer.toString(result);
			} else if (destinationType2.equalsIgnoreCase(OMSMessages.REAL.getValue())) {
				double result = 1.0;
				for (int i = 0; i < operandValList.size(); i++) {
					result = Double.parseDouble(operandValList.get(i));
				}

				final_new_result = Double.toString(result);
			}
		}*/
		processfinalResult(final_new_result, destinationType2);

	}

	private List<String> getColumnVal(List<BLOperandDTO> operandList) {
		List<String> operandValList = new ArrayList<String>();
		for (int i = 0; i < operandList.size(); i++) {
			String colVal="";
       if(operandList.get(i).isGlobal) {
    	   if(!TextUtils.isEmpty(operandList.get(i).operandColumnName) && !operandList.get(i).operandColumnName.equalsIgnoreCase("constant")){
    		   String globalVal = blGlobalValGenerator.getGlobalBLValue(operandList.get(i).operandColumnName);
    		   Object val = (Double) Double.parseDouble(globalVal);
    		   colVal = val.toString();
    		   
    	   }
       }
       else{
    	            colVal = getColValForOperand(
					operandList.get(i).operandTableName,
					operandList.get(i).operandPrimaryKey,
					operandList.get(i).operandType,
					operandList.get(i).constantValue,
					operandList.get(i).operandColumnName);
       }
			operandValList.add(colVal);
		}
		return operandValList;

	}

	private String getColValForOperand(String operandTableName,
			String operandPrimaryKey, String operandType, String constantValue,
			String operandColumnName ) {
		//try{
		Log.i(TAG,"OperandColumnName : "+operandColumnName);
		
		Object val = null;
/*		if (operandType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
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
				if(value !=null && !value.equalsIgnoreCase(""))
					val = (Integer) Integer.parseInt(value);
				else
					val=1;
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
				if(value !=null && !value.equalsIgnoreCase(""))
					val = (Double) Double.parseDouble(value);
				else
					val=1;
			}
		}
		else 
		{*/
			if (operandColumnName
					.equalsIgnoreCase(OMSMessages.CONSTANT.getValue())) {
				if(constantValue !=null && !constantValue.equalsIgnoreCase(""))
				{	
		/*			if(checkForExponentialType(constantValue)){
					//val = 	Double.parseDouble(constantValue);
					 val = String.format("%f", Double.parseDouble(constantValue)) ;
					 Log.d(TAG,"Formatted::::"+val); 
					 val = Float.valueOf(val.toString()).intValue();
					 Log.d(TAG,"converted Formatted val::::"+val);
					}else{
					val = (Integer) Integer.parseInt(constantValue);
					}*/
					
					val = (Double) Double.parseDouble(constantValue);
				}
				else
					val=1;
				//val = (Integer) Integer.parseInt(constantValue);
			} /*else if (operandPrimaryKey.equalsIgnoreCase(OMSMessages.CONSTANT
					.getValue())) {
				uniqueId = constantValue;
				String value = new BLHelper(context).getColuValForColName(
						operandTableName, operandColumnName, null, operandType,
						operandPrimaryKey);
				val = (Integer) Integer.parseInt(value);
			}*/ else {
				/*String usid = new BLHelper(context).getFirstUsidFromDestinationTable(
						operandTableName, operandColumnName, uniqueId,
						operandType, operandPrimaryKey);
				if(!TextUtils.isEmpty(usid)){
					uniqueId=usid;
				}*/
				if(isForceUpdate) {
					String usid = new BLHelper(context).getForceUpdatedUsid(operandTableName, operandColumnName, uniqueId,
						operandType, operandPrimaryKey);
					if(!TextUtils.isEmpty(usid)){
						uniqueId=usid;
					}
				}
				String value = new BLHelper(context).getColuValForColName(
						operandTableName, operandColumnName, uniqueId,
						operandType, operandPrimaryKey);
				if(value !=null && !value.equalsIgnoreCase("")){
					//val = (Integer) Integer.parseInt(value);
					val = (Double) Double.parseDouble(value);
				}else{
					val=1;
				}
			}
		//}
		Log.i(TAG,"Operand Value : "+val);
		return val.toString();
	//}
		
	}

	public void processDestinationList(List<BLDestinationDTO> destList) {
		if (destList != null) {
			for (int i = 0; i < destList.size(); i++) {
				destinationTableName = destList.get(i).destinataionTableName;
				destinationType = destList.get(i).destinationType;
				destinationColumn = destList.get(i).destinationColumnName;
				destinationPrimaryKey = destList.get(i).destinationPrimaryKey;
				destinationIsGlobal =  destList.get(i).isGlobal;
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
	}

	@Override
	protected String doInBackground(String... args) {
		
		try{
			String xml_file = args[0];
			processAddBL(xml_file);
			if (final_new_result != null && !final_new_result.equalsIgnoreCase("invalid expression")){
				if (destinationTableName != null) {
					if (destinationColumn.equalsIgnoreCase(OMSMessages.CONSTANT.getValue())) {
						ContentValues cvalss = new ContentValues();
					
						if(destinationType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
							//final_new_result =  Integer.parseInt(final_new_result);
							cvalss.put(destinationColumn, (int)Math.round(Double.parseDouble(final_new_result)));
						}else{
							cvalss.put(destinationColumn,  Double.parseDouble(final_new_result));
						}
				
						
						cvalss.put("status", "fresh");
						
						new BLHelper(context).insertOrUpdateData(
								destinationTableName, null, cvalss);
					} else if(!isForceUpdate) {
						ContentValues cvalss = new ContentValues();
						if(destinationType.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
							//final_new_result =  Integer.parseInt(final_new_result);
							cvalss.put(destinationColumn, (int)Math.round(Double.parseDouble(final_new_result)));
						}else{
							cvalss.put(destinationColumn,  Double.parseDouble(final_new_result));
						}
				
						
						cvalss.put("status", "fresh");
						//cvalss.put(destinationColumn, final_new_result);
						if (uniqueId != null) {
							new BLHelper(context).insertOrUpdateData(
									destinationTableName, uniqueId, cvalss);
						}
	
					}
				}
			}
		} catch(Exception e){
			return "invalid expression";
		}
		return final_new_result;
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i(TAG, "Final Expression Result:::" + result);
		if(result != null && result.equalsIgnoreCase("invalid expression")){
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_FAILURE.getValue());
			//	Toast.makeText(context, "Invalid Expression", Toast.LENGTH_SHORT).show();
			}
			
		}else{
		if (nextDataList != null) {
				if (nextDataList.size() > 0) {
					blDataList = new BLHelper(context).getBLDetailsBasedOnUsid(
							nextDataList.get(0).colVal, 0);
					if (blDataList != null && blDataList.size() > 0) {
						new BLExecutorActionCenter(context, mContainerId, rListener, -1,
								transUsidList).doBLActionType(blDataList);
					}
				}
			}
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
			}
		}
		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}

	}

	
	private boolean checkForExponentialType(String str){
		boolean isExponential=false;
		if(str.contains("E") || str.contains("e")){
			isExponential=true;
		}
		return isExponential;
	}
}
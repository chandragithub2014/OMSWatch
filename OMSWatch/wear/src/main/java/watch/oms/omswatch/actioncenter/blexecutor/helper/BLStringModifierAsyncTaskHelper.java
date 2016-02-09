package watch.oms.omswatch.actioncenter.blexecutor.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;




import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLStringModifierDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLStringModifierOperandDTO;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLGlobalValueGenerator;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

public class BLStringModifierAsyncTaskHelper extends AsyncTask<String, Void, String>{

	private final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private OMSReceiveListener rListener;
	BLGlobalValueGenerator blGlobalValGenerator;
	private List<String> transUsidList;
	BLStringModifierDTO blStringModifierDTO;
	
	public BLStringModifierAsyncTaskHelper(Activity FragmentContext, int containerId,
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
	 	res = processStringModifier(xml_file);
		return Integer.toString(res);
	}

	@Override
	protected void onPostExecute(String result) {
		if (Integer.parseInt(result) != OMSDefaultValues.NONE_DEF_CNT.getValue()) {
		if (rListener != null)
			{
			   rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
			}
		} else {
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_FAILURE.getValue());
			}
		}
		Log.d("TAG", "Result inInsertUpdateActionCenterAsyncTaskHelper:::"
				+ result);
		super.onPostExecute(result);
	}
	
	private int processStringModifier(String targetXml) {
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			try {
				blStringModifierDTO = xmlParser.parseStringModifier(response);
				if(blStringModifierDTO!=null) {
					result = 	processStringModifierObject(blStringModifierDTO);
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
	
	
	private int processStringModifierObject(BLStringModifierDTO stringModifierDTO){
		int updatedRow=-1;
		HashMap<Integer,BLStringModifierOperandDTO> stringModifierOperandHash = stringModifierDTO.stringModifierOperandHash;
		 HashMap<Integer,String> stringModifierOperatorHash = stringModifierDTO.stringModifierOperatorHash;
		 int operandCount = 1;
		 String message="";
		 String tableName="";
		 String dbName="";
		 String resultantColumn = "";
		 if(stringModifierDTO.stringModifierHash!=null && stringModifierDTO.stringModifierHash.size()>0){
			 HashMap<String,String> stringModifierTempHash = stringModifierDTO.stringModifierHash;
			 if(!TextUtils.isEmpty(stringModifierTempHash.get("table"))){
				 tableName = stringModifierTempHash.get("table");
			 }
			 
            if(!TextUtils.isEmpty(stringModifierTempHash.get("database"))){
            	dbName = stringModifierTempHash.get("database");
			 }
            
            
            if(!TextUtils.isEmpty(stringModifierTempHash.get("messageColumnName"))){
            	resultantColumn = stringModifierTempHash.get("messageColumnName");
			 }
			 
		 }
		 
		 
		 if(stringModifierOperandHash.size()>0 && stringModifierOperatorHash.size()>0) {
			 do{
				 BLStringModifierOperandDTO  tempOperand = stringModifierOperandHash.get(operandCount);
				 if(tempOperand.operandType.equalsIgnoreCase("Static")){
					  if(!TextUtils.isEmpty(tempOperand.columnName)){
						  if(stringModifierOperatorHash.get(operandCount)!=null){
								 Log.d("TAG","Operator::::"+stringModifierOperatorHash.get(operandCount));
								 if(stringModifierOperatorHash.get(operandCount).equalsIgnoreCase("concatenation")){
									 message=message + tempOperand.columnName +" " ;
								 }
							 }else{
								 message=message + tempOperand.columnName +" " ;
							 }
					  }
				 }else if(tempOperand.operandType.equalsIgnoreCase("Global")){
					 if(!TextUtils.isEmpty(tempOperand.columnName)){ 
					 if(!TextUtils.isEmpty((String)OMSApplication
								.getInstance().getGlobalBLHash().get(tempOperand.columnName))){
							String colVal = (String) OMSApplication
									.getInstance().getGlobalBLHash().get(tempOperand.columnName);
							if(stringModifierOperatorHash.get(operandCount)!=null){
								 Log.d("TAG","Operator::::"+stringModifierOperatorHash.get(operandCount));
								 if(stringModifierOperatorHash.get(operandCount).equalsIgnoreCase("concatenation")){
									 message=message + colVal +" ";
								 }
							 }else{
								 message=message + colVal +" ";
							 }
					  }
				   }
				 }if(tempOperand.operandType.equalsIgnoreCase("Dynamic")){
				String dynamicColVal = 	 getColumnValue(tableName,dbName,tempOperand.columnName);
				if(!TextUtils.isEmpty(dynamicColVal)){
					if(stringModifierOperatorHash.get(operandCount)!=null){
						 Log.d("TAG","Operator::::"+stringModifierOperatorHash.get(operandCount));
						 if(stringModifierOperatorHash.get(operandCount).equalsIgnoreCase("concatenation")){
							 message=message + dynamicColVal;
						 }
					 }else{
						 message=message + dynamicColVal;
					 }
					
				}
				 }
				 operandCount++;
			 }while(operandCount<=stringModifierOperandHash.size());
		 }
		 if(!TextUtils.isEmpty(message)){
	 updatedRow = 		 updateMessageColumn(resultantColumn,message,tableName,dbName);
		 }
		 return updatedRow;
	}
	
	private String getColumnValue(String tableName,String schemaName,String columnName){
		String columnVal="";
		String uniqueId = "";
		if(!TextUtils.isEmpty(tableName) && !TextUtils.isEmpty(schemaName) && !TextUtils.isEmpty(columnName)) {
		String columnType = getColumnType(tableName,schemaName,columnName);
	if(!TextUtils.isEmpty(columnType))	 {
		Cursor sourceCursor = null;
		if(transUsidList!=null && transUsidList.size()>0) {
		for (int k = 0; k < transUsidList.size(); k++) {
			uniqueId = transUsidList.get(k);
		
			try{
				sourceCursor = OMSDBManager.getSpecificDB(schemaName)
						.query(tableName,
								null,
								OMSDatabaseConstants.UNIQUE_ROW_ID + "='"
										+ uniqueId + "'"+ "and "
												+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
												+ " <> '1'", null, null, null,
								null);
				
				if(sourceCursor.moveToFirst()){
					if (columnType
							.equalsIgnoreCase(OMSMessages.INTEGER
									.getValue())) {
						columnVal = columnVal +Integer.toString(sourceCursor.getInt(sourceCursor
								.getColumnIndex(columnName)))+" ";
						
					} else if (columnType
							.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || columnType.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
						columnVal = columnVal +" "+sourceCursor.getString(sourceCursor
								.getColumnIndex(columnName))+" ";
					}else  if (columnType
							.equalsIgnoreCase(OMSMessages.REAL_CAPS
									.getValue())) {
						columnVal = columnVal +" "+ Double.toString(sourceCursor.getDouble(sourceCursor
								.getColumnIndex(columnName)))+" ";
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	 }
		}
		}
		return columnVal;
	}
	
	private String getColumnType(String tableName,String schemaName,String columnName){
		String columnType="";
		Cursor PragmaCursor1 = OMSDBManager.getSpecificDB(schemaName)
				.rawQuery("pragma table_info(" + tableName + ");", null);
		if(PragmaCursor1.getCount() > 0){
			if(PragmaCursor1.moveToFirst()){
				do{
					String pragmaColumnName = PragmaCursor1
							.getString(PragmaCursor1
									.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
					if(columnName.equalsIgnoreCase(pragmaColumnName)){
						columnType = 	PragmaCursor1
						.getString(PragmaCursor1
								.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
					}
				}while(PragmaCursor1.moveToNext());
			}
		}
		PragmaCursor1.close();
		return columnType;
	}
	
	
	private int updateMessageColumn(String resultantColmn,String resultantColVal,String tableName,String schemaName){
		int updatedRow = -1;
		String uniqueId = "";
		Cursor sourceCursor = null;
		if(transUsidList!=null && transUsidList.size()>0) {
			for (int k = 0; k < transUsidList.size(); k++) {
				uniqueId = transUsidList.get(k);
				try{
					sourceCursor = OMSDBManager.getSpecificDB(schemaName)
							.query(tableName,
									null,
									OMSDatabaseConstants.UNIQUE_ROW_ID + "='"
											+ uniqueId + "'"+ "and "
													+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
													+ " <> '1'", null, null, null,
									null);
					if(sourceCursor.moveToFirst()){
						ContentValues cval = new ContentValues();
						cval.put(resultantColmn, resultantColVal);
						updatedRow = (int) OMSDBManager.getSpecificDB(schemaName)
								.update(tableName, cval, OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
										new String[] { uniqueId.toString() });
					}
					sourceCursor.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return updatedRow;
	}
	
	//BLStringModifier xml
	
	/*
	 * 
	 <?xml version="1.0" encoding="UTF-8"?>
    <bl>
   <StringModifier tablename="orders" database="555551.sql">
      <output>
         <result columnname="message" />
      </output>
      <input>
         <operand type="Static" columnname="Thank You.Your Order with order id" />
         <operator name="concatenation" />
         <operand type="Dynamic" columnname="orderid" />
         <operator name="concatenation" />
         <operand type="Static" columnname="has been placed.We will send the order updated to your mobile number" />
         <operator name="concatenation" />
         <operand type="Global" columnname="mobilenumber" />
      </input>
   </StringModifier>
   </bl>
	 */
}

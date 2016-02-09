/**
 * 
 */
package watch.oms.omswatch.actioncenter.blexecutor.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;



import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLColumnDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLGlobalDTO;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

/**
 * @author 245742
 *
 */
public class BLGlobalAsyncTaskHelper extends AsyncTask<String, Void, String> {

	private final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private OMSReceiveListener rListener;
	private String uniqueId = null;
	private List<BLGlobalDTO> globalBLList;
	private List<String> transUsidList;
	private String destinationColumnIndex = null;
	private Hashtable<String,String> columnTypeHash = new Hashtable<String,String>();
	
	public BLGlobalAsyncTaskHelper(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList) {
		context = FragmentContext;
		rListener = receiveListener;
		this.transUsidList =  new ArrayList<String>(transUsidList);

	}
	@Override
	protected String doInBackground(String... args) {
		int res = OMSDefaultValues.NONE_DEF_CNT.getValue();
		String xml_file = args[0];
		res = processGlobalBL(xml_file);
		return Integer.toString(res);
	}

	
	private int processGlobalBL(String targetXml) {
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		try {
			String response = targetXml;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			try {
				globalBLList =  xmlParser.parseGlobalBLXML(response);
				if (globalBLList != null) {
					/*
					 * if(sourceTableName.equalsIgnoreCase(destinationTableName))
					 * { processIUList1(IUList); } else
					 */{
						result = processGlobalBLList(globalBLList);
					}
				}
			} catch (XmlPullParserException e) {
				Log.e(TAG, "Error Occured in processIUBL:" + e.getMessage());
				e.printStackTrace();
			}
			
		}catch (IOException e) {
			Log.e(TAG, "Error Occured in processing GlobalBL:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	private int processGlobalBLList(List<BLGlobalDTO> globalList) {
		int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
		String sourceTableName = globalList.get(0).sourceTableName;
		boolean isForceUpdate = false;
		isForceUpdate = globalList.get(0).isForceUpdate;
		List<BLColumnDTO> globalColList = globalList.get(0).sourceColList;
		if(globalColList!=null && globalColList.size()>0 && !TextUtils.isEmpty(sourceTableName)){
		generateColumnTypeHash(globalColList,sourceTableName);
		
		if(columnTypeHash!=null && columnTypeHash.size()>0){
			if(isForceUpdate){
				generateGlobalBlHashForForceUpdate(globalColList,sourceTableName,columnTypeHash);
			}else{
			generateGlobalBlHash(globalColList,sourceTableName,columnTypeHash);
			}
		}
	  }
		return result;
	}
	
	
	private void generateColumnTypeHash(List<BLColumnDTO>   globalColmnList,String  dataTable){
     //     generateTransDataList(dataTable);
		Cursor PragmaCursor = TransDatabaseUtil.getTransDB()
				.rawQuery("pragma table_info(" + dataTable + ");", null);
		for(int i=0;i<globalColmnList.size();i++) {
			if(PragmaCursor.getCount()>0) {
				if(PragmaCursor.moveToFirst()) {
				do{
					String columnName = PragmaCursor
							.getString(PragmaCursor
									.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
					if (columnName
							.equalsIgnoreCase(globalColmnList.get(i).columnName)) {
						columnTypeHash.put(columnName,PragmaCursor
								.getString(PragmaCursor
										.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2)));
						 
					}
				}while(PragmaCursor.moveToNext());
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void generateGlobalBlHash(List<BLColumnDTO>  globalColmnList,String dataTable,Hashtable<String,String> colTypeHash){
	    Hashtable<String,Object> globalBLHash ; //= new Hashtable<String,Object>();
	    if(OMSApplication.getInstance().getGlobalBLHash()!=null)
	    {
	    	globalBLHash = OMSApplication.getInstance().getGlobalBLHash();
	    }else{
	    	globalBLHash = new Hashtable<String,Object>();
	    }
		String uniqueId="";
		if(transUsidList!=null && transUsidList.size()>0){
			for (int k = 0; k < transUsidList.size(); k++) {
				uniqueId = transUsidList.get(k);
				Cursor sourceCursor = null;
				try{
					sourceCursor = TransDatabaseUtil
							.query(dataTable,
									null,
									OMSDatabaseConstants.UNIQUE_ROW_ID + "='"
											+ uniqueId + "'", null, null, null,
									null);
					if (sourceCursor.moveToFirst()) {
						for(int i=0;i<globalColmnList.size();i++) {
							String columnType  = colTypeHash.get(globalColmnList.get(i).columnName);
							if (columnType
									.equalsIgnoreCase(OMSMessages.INTEGER
											.getValue())) {
								globalBLHash.put(globalColmnList.get(i).columnName,sourceCursor.getInt(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName)));
								
							}else if (columnType
									.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || columnType
									.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
								/*if(!TextUtils.isEmpty(sourceCursor.getString(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName))))*/
								if(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName) > -1){
									globalBLHash.put(globalColmnList.get(i).columnName,sourceCursor.getString(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName)));
								}
								
							} else if (columnType
									.equalsIgnoreCase(OMSMessages.REAL_CAPS
											.getValue())) {
								globalBLHash.put(globalColmnList.get(i).columnName,sourceCursor.getDouble(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName)));
							}
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally {
					if (sourceCursor != null)
						sourceCursor.close();
				}
			}
		}
		if(globalBLHash!=null && globalBLHash.size()>0){
			OMSApplication.getInstance().setGlobalBLHash(globalBLHash);
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void generateGlobalBlHashForForceUpdate(List<BLColumnDTO>  globalColmnList,String dataTable,Hashtable<String,String> colTypeHash){
	    Hashtable<String,Object> globalBLHash ; //= new Hashtable<String,Object>();
	    if(OMSApplication.getInstance().getGlobalBLHash()!=null)
	    {
	    	globalBLHash = OMSApplication.getInstance().getGlobalBLHash();
	    }else{
	    	globalBLHash = new Hashtable<String,Object>();
	    }
		String uniqueId="";
	//	if(transUsidList!=null && transUsidList.size()>0){
	//		for (int k = 0; k < transUsidList.size(); k++) {
		//		uniqueId = transUsidList.get(k);
				Cursor sourceCursor = null;
				try{
					sourceCursor = TransDatabaseUtil
							.query(dataTable,
									null,
									OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null,
									null);
					if (sourceCursor.moveToFirst()) {
						for(int i=0;i<globalColmnList.size();i++) {
							String columnType  = colTypeHash.get(globalColmnList.get(i).columnName);
							if (columnType
									.equalsIgnoreCase(OMSMessages.INTEGER
											.getValue())) {
								globalBLHash.put(globalColmnList.get(i).columnName,sourceCursor.getInt(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName)));
								
							}else if (columnType
									.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || columnType
									.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
								/*if(!TextUtils.isEmpty(sourceCursor.getString(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName))))*/
								if(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName) > -1){
									globalBLHash.put(globalColmnList.get(i).columnName,sourceCursor.getString(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName)));
								}
								
							} else if (columnType
									.equalsIgnoreCase(OMSMessages.REAL_CAPS
											.getValue())) {
								globalBLHash.put(globalColmnList.get(i).columnName,sourceCursor.getDouble(sourceCursor.getColumnIndex(globalColmnList.get(i).columnName)));
							}
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally {
					if (sourceCursor != null)
						sourceCursor.close();
				}
		//	}
	//	}
				
		if(globalBLHash!=null && globalBLHash.size()>0){
			OMSApplication.getInstance().setGlobalBLHash(globalBLHash);
		}
	}
	
	private void generateTransDataList(String globalDataTable){
		transUsidList = new ArrayList<String>();
	Cursor	sourceCursor =  TransDatabaseUtil.query(
			globalDataTable,
			null, OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
					+ " <> '1'", null, null, null, null);
	    if(sourceCursor.moveToFirst()){
		do{
			transUsidList.add(sourceCursor.getString(sourceCursor.getColumnIndex("usid")));
		}while(sourceCursor.moveToNext());
	    }
	}
	@Override
	protected void onPostExecute(String result) {
		if (rListener != null)
			rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
		Log.d("TAG", "Result in GlobalBL AsyncTaskHelper:::"
				+ result);
		super.onPostExecute(result);
	}
}

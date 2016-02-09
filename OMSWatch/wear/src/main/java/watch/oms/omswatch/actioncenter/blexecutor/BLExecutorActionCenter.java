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
package watch.oms.omswatch.actioncenter.blexecutor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLColumnDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLExecutorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLGetDTO;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLCreateTableAsyncTaskHelper;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLDeleteAsyncTaskHelper;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLExpressionAsyncTask;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLFORAsyncTaskHelper;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLFileDownloader;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLGlobalAsyncTaskHelper;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLHelper;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLIFElseAsyncTask;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLIUAsyncTaskHelper;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLOneToManyAsyncTaskHelper;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLStringModifierAsyncTaskHelper;
import watch.oms.omswatch.actioncenter.helpers.ActionCenterHelper;
import watch.oms.omswatch.actioncenter.helpers.PostAsyncTaskHelper;
//import watch.oms.omswatch.actioncenter.helpers.ServerDBUpdateHelper;
import watch.oms.omswatch.actioncenter.helpers.TransDBParser;
import watch.oms.omswatch.actioncenter.helpers.WatchTransDBParser;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.ConsoleDBConstants;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.parser.OMSServerMapperHelper;


public class BLExecutorActionCenter implements OMSReceiveListener {
	private Activity context;
	private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private OMSReceiveListener rListener;
	private int configDBAppId;
	private List<BLExecutorDTO> blActionList = new ArrayList<BLExecutorDTO>();
	private int index = 0;
	private final String TAG = this.getClass().getSimpleName();
	private List<String> transUsidList;
	private String action_center_usid = null;
	private ActionCenterHelper actionCenterHelper;
	private String blResult;
	
	public BLExecutorActionCenter(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, int appId,
			List<String> transUsidList) {
		context = FragmentContext;
		mContainerId = containerId;
		rListener = receiveListener;
		configDBAppId = appId;
		this.transUsidList = transUsidList;
		actionCenterHelper = new ActionCenterHelper(context);
	}

	public void doBLAction(String actionusId, String actionQueueUniqueId) {
		Log.i(TAG, "actionusId : " + actionusId);
		try{
			action_center_usid = actionQueueUniqueId;
			blActionList = new BLHelper(context).getBLDetails(actionusId,
					configDBAppId);
			if (blActionList != null && !blActionList.isEmpty()) {
				doBLActionType(blActionList, index);
			}
			else{
				rListener.receiveResult(OMSMessages.ACTION_CENTER_FAILURE
						.getValue());
			}
		}catch(Exception e){
			//Toast.makeText(context, "BL excecution Failed.", Toast.LENGTH_SHORT).show();
		}
	}

	public void doBLAction(String blName) {
		/*ServerDBUpdateHelper dbhelper = new ServerDBUpdateHelper(
				context);
		OMSApplication.getInstance().setTraceType(OMSConstants.TRACE_TYPE_BL);
		dbhelper.insertCallTraceTypeData(ConsoleDBConstants.CALL_TRACE_TYPE_TABLE, ""+configDBAppId);*/
		Log.i(TAG, ""+OMSApplication.getInstance().getTraceType());
		Log.i(TAG, "doBLAction()::"+blName);
	try{
			String blCompleteUniqueId = new BLHelper(context).getBLCompleteUniqueId(blName, configDBAppId);
			if(!TextUtils.isEmpty(blCompleteUniqueId)) {
			blActionList = new BLHelper(context).getBLDetails(blCompleteUniqueId,
					configDBAppId);
			if (blActionList != null && !blActionList.isEmpty()) {
				doBLActionType(blActionList, index);
			}
			else{
				rListener.receiveResult(OMSMessages.REFRESH
						.getValue());
			}
		   }else{
			  rListener.receiveResult(OMSMessages.REFRESH
						.getValue());
		   }
		} catch(Exception e){
			e.printStackTrace();
			//Toast.makeText(context, "BL excecution Failed.", Toast.LENGTH_SHORT).show();
		}
	}
	public void doBLActionType(List<BLExecutorDTO> blactionList, int index) {
		try{
		//Log.i(TAG, ""+OMSApplication.getInstance().getTraceType());
		/*OMSApplication.getInstance().setUserSessionId(OMSApplication.getInstance().getUserSessionId());
		OMSApplication.getInstance().setScreensessionId(OMSApplication.getInstance().getScreensessionId());
		Log.i(TAG, "doBLActionType() : " + blactionList.get(index).blType);*/
		if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_FOR)) {
			new BLFORAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList, configDBAppId)
					.processFORBLXML(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_EXPRESSION)) {
			new BLExpressionAsyncTask(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_IF)) {
			new BLIFElseAsyncTask(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList, configDBAppId)
					.processAddBL(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_IU)) {
			new BLIUAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_ONE_TO_MANY)) {
			new BLOneToManyAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_DELETE)) {
			new BLDeleteAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_CREATE_TABLE)) {
			new BLCreateTableAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.POST_TYPE_REQUEST)) {
			try {
				OMSApplication.getInstance().setTraceType(OMSConstants.TRACE_TYPE_SERVER);
				Log.i(TAG, ""+OMSApplication.getInstance().getTraceType());
			/*	OMSApplication.getInstance().setUserSessionId(OMSApplication.getInstance().getUserSessionId());
				OMSApplication.getInstance().setScreensessionId(OMSApplication.getInstance().getScreensessionId());*/
			if (OMSDBManager.checkNetworkConnectivity()) {
				BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
				Hashtable<String,String> blList = xmlParser.processPostBL(
						blactionList.get(index).blXml,
						OMSDatabaseConstants.POST_TYPE_REQUEST);
				// get it from xml
				String clientTableName="";
				String schemaName="";
				String transUrl ="";
				String serverTableName="";
				String loadMessage="";
				String successMessageColumn="";
				String successMessage="";
				if(!TextUtils.isEmpty(OMSApplication.getInstance().getPostLoadingMessage())){
					OMSApplication.getInstance().setPostLoadingMessage("");
				}
				if(!TextUtils.isEmpty(blList.get("loadingmessage"))){
					loadMessage = blList.get("loadingmessage");
					OMSApplication.getInstance().setPostLoadingMessage(loadMessage);
				}
				if(!TextUtils.isEmpty(blList.get("datatablename"))){
			       clientTableName = blList.get("datatablename");
				}
				if(!TextUtils.isEmpty(blList.get("schema"))){
					schemaName = blList.get("schema");
					}
				if(!TextUtils.isEmpty(blList.get("successmessagecolumn"))){
					successMessageColumn=blList.get("successmessagecolumn");
				}
				if(!TextUtils.isEmpty(blList.get("successmessagetext"))){
					successMessage=blList.get("successmessagetext");
				}
/*				if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE) {
					
					if(!TextUtils.isEmpty(clientTableName)) {
						transUrl = new OMSServerMapperHelper().getTransPostURL(clientTableName);	
					}
				}else {
				if(!TextUtils.isEmpty(blList.get("url"))){
					transUrl = blList.get("url");
					}
				}*/
				
				if(!TextUtils.isEmpty(clientTableName)) {
					transUrl = new OMSServerMapperHelper().getTransPostURL(clientTableName);
				}
				JSONObject JSONPayLoad = null ;
				// get it from bl post xml from BL Table
				if(clientTableName.equalsIgnoreCase("all")){
				JSONPayLoad = actionCenterHelper.getJsonPayload(schemaName);
				}else{
				 JSONPayLoad = actionCenterHelper.formJSONPayLoad(schemaName,
						clientTableName);
				}
				serverTableName = new OMSServerMapperHelper().getServerTableName(clientTableName);
				if (JSONPayLoad != null && JSONPayLoad.length() > 0) {
					PostAsyncTaskHelper postAsyncTaskHelper = new PostAsyncTaskHelper(
							context, clientTableName, JSONPayLoad, blactionList.get(index).blusid,
							BLExecutorActionCenter.this, configDBAppId,loadMessage);

					postAsyncTaskHelper.execute(transUrl,serverTableName, /* clientTableName,*/schemaName,successMessageColumn,successMessage);
				} else {
					//rListener.receiveResult(OMSMessages.REFRESH.getValue());
					receiveResult(OMSMessages.BL_SUCCESS.getValue());
				}
				
				} else{
					Toast.makeText(context,"Network not available.Please check.",
							Toast.LENGTH_SHORT).show();
				}
			} catch (SQLException e) {
				rListener.receiveResult(OMSMessages.REFRESH.getValue());
				Log.e(TAG, "Exception in POST Action::" + e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				rListener.receiveResult(OMSMessages.REFRESH.getValue());
				e.printStackTrace();
				Log.e(TAG, "Exception in POST Action::" + e.getMessage());
			}
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.GET_TYPE_REQUEST)) {
			//OMSApplication.getInstance().setTraceType(OMSConstants.TRACE_TYPE_GET_LAUNCH);
			//Log.i(TAG, ""+OMSApplication.getInstance().getTraceType());
			/*OMSApplication.getInstance().setUserSessionId(OMSApplication.getInstance().getUserSessionId());
			OMSApplication.getInstance().setScreensessionId(OMSApplication.getInstance().getScreensessionId());*/
			Log.i(TAG, "doBLActionType() : " + blactionList.get(index).blType);
			try {
				List<String> queryArray = new ArrayList<String>();
				String queryString="";
				String modifiedDate="0.0";
				String serviceUrl="";
				boolean hasUserId=false;
				boolean hasModifiedDate=false;
				boolean hasLocation = false;
				boolean hasPagination = false; // Pagination Code
				int currentPage = 0;

		if (OMSDBManager.checkNetworkConnectivity()) {
				BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
/*				Hashtable<String,String> blList = xmlParser.processPostBL(
						blactionList.get(index).blXml,
						OMSDatabaseConstants.GET_TYPE_REQUEST);*/
				List<BLGetDTO> getBLList = xmlParser.processGetBL(blactionList.get(index).blXml,
						OMSDatabaseConstants.GET_TYPE_REQUEST);
				
				if(getBLList!=null && getBLList.size()>0){
					//data tablename
					if(!TextUtils.isEmpty(getBLList.get(0).getDataTable)){
						if(!TextUtils.isEmpty(getBLList.get(0).hasModifiedDate)){  // modified date
							if(getBLList.get(0).hasModifiedDate.equalsIgnoreCase("true") || getBLList.get(0).hasModifiedDate.equalsIgnoreCase("false")){
								hasModifiedDate=Boolean.parseBoolean(getBLList.get(0).hasModifiedDate);
								if(hasModifiedDate){
									 modifiedDate = new OMSServerMapperHelper().getLastModifiedTime(getBLList.get(0).getDataTable);
									 String modifiedQuery = "modifieddate="+modifiedDate;
									 queryArray.add(modifiedQuery);
								}
							}
						}
					}
					
					//hasLocation
					if(!TextUtils.isEmpty(getBLList.get(0).hasLocation)){
						if(getBLList.get(0).hasLocation.equalsIgnoreCase("true") || getBLList.get(0).hasLocation.equalsIgnoreCase("false")){
							hasLocation=Boolean.parseBoolean(getBLList.get(0).hasLocation);
						}
					}
					// hasuserid
					if(!TextUtils.isEmpty(getBLList.get(0).hasUserId)){
						if(getBLList.get(0).hasUserId.equalsIgnoreCase("true") || getBLList.get(0).hasUserId.equalsIgnoreCase("false")){
							hasUserId=Boolean.parseBoolean(getBLList.get(0).hasUserId);
						}
					}
					/*if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE) {
						
						if(!TextUtils.isEmpty(getBLList.get(0).getDataTable)) {
							serviceUrl = new OMSServerMapperHelper().getTransGetURL(getBLList.get(0).getDataTable);	
						}
					}
					else{
					if(!TextUtils.isEmpty(getBLList.get(0).getURL)){
						serviceUrl=getBLList.get(0).getURL;
					}
				   }
					*/
					
					if(!TextUtils.isEmpty(getBLList.get(0).getDataTable)) {
						serviceUrl = new OMSServerMapperHelper().getTransGetURL(getBLList.get(0).getDataTable);	
					}
					if(hasUserId){

						if(OMSApplication.getInstance().getLoginUserColumnName()!=null) {
						String userIdColumn=OMSApplication.getInstance().getLoginUserColumnName();
						
						String loginVal = "";
						@SuppressWarnings("unchecked")
						Hashtable<String,String> loginHash = OMSApplication.getInstance().getLoginCredentialHash();
						if(!TextUtils.isEmpty((String)OMSApplication
								.getInstance().getGlobalBLHash().get("userid"))){
							String colVal = (String)OMSApplication
									.getInstance().getGlobalBLHash().get("userid");
							String query = "userid"+"="+colVal.replace(" ", "%20");
							queryArray.add(query);
						}else if(loginHash!=null && loginHash.get(userIdColumn)!=null){
							loginVal = loginHash.get(userIdColumn);
							//serviceUrl=serviceUrl+"?"+"modifieddate="+modifiedDate+"&"+userIdColumn+"="+loginVal;
							String userIdQuery = userIdColumn+"="+loginVal;
							queryArray.add(userIdQuery);
						}

					}
					}
					
					@SuppressWarnings("unchecked")
					Hashtable<String,Object> tempGlobalHash= OMSApplication
							.getInstance().getGlobalBLHash();
					if(tempGlobalHash!=null) {
					List<BLColumnDTO> getColumnList = getBLList.get(0).getColList;
					if(getColumnList!=null && getColumnList.size()>0){
						for(int i=0;i<getColumnList.size();i++){
							if(!TextUtils.isEmpty((String)tempGlobalHash.get(getColumnList.get(i).columnName))){
								String colVal = (String)tempGlobalHash.get(getColumnList.get(i).columnName);
								String query = getColumnList.get(i).columnName+"="+colVal.replace(" ", "%20");
								queryArray.add(query);
							}
						}
					}
					
//					if(hasUserId){
//						if(!TextUtils.isEmpty((String)tempGlobalHash.get("userid"))){
//							String colVal = (String)tempGlobalHash.get("userid");
//							String query = "userid"+"="+colVal.replace(" ", "%20");
//							queryArray.add(query);
//						}
//					}
					if(hasLocation){
						if(!TextUtils.isEmpty( (String)tempGlobalHash.get("latitude"))){
							String latitude=(String)tempGlobalHash.get("latitude");
							String latitudeQuery = "latitude"+"="+latitude;
							queryArray.add(latitudeQuery);
						}
                        if(!TextUtils.isEmpty( (String)tempGlobalHash.get("longitude"))){
                        	String longitude=(String)tempGlobalHash.get("longitude");
							String longitudeQuery = "latitude"+"="+longitude;
							queryArray.add(longitudeQuery);
						}
                        if(!TextUtils.isEmpty( (String)tempGlobalHash.get("distance"))){
                        	String distance=(String)tempGlobalHash.get("distance");
							String distanceQuery = "distance"+"="+distance;
							queryArray.add(distanceQuery);
						}
						
					}
				  }
					
					
				}
				
				// Pagination Code
				if(!TextUtils.isEmpty(getBLList.get(0).hasPagination)){  //Pagination
					if(getBLList.get(0).hasPagination.equalsIgnoreCase("true") || getBLList.get(0).hasPagination.equalsIgnoreCase("false")){
						hasPagination=Boolean.parseBoolean(getBLList.get(0).hasPagination);
						if(hasPagination){
							currentPage = new OMSServerMapperHelper().getPaginationCurrentPage(getBLList.get(0).getDataTable);
							if(!TextUtils.isEmpty(getBLList.get(0).paginationParam)){  // modified date
								String paginationQuery = getBLList.get(0).paginationParam+"="+currentPage;
								queryArray.add(paginationQuery);
							}
						}
					}
				}
				
				
				if(queryArray!=null && queryArray.size()>0) {
					
					for(int i=0;i<queryArray.size();i++){
						if (i==0) {
							queryString+=queryArray.get(i);
						} else {
							queryString+="&"+queryArray.get(i);
						}
					}
					
				}
				
				if(queryString!=null && queryString.length()>0) {
					queryString = queryString.replaceAll(" ", "%20");
					if(serviceUrl.contains("?")) serviceUrl=serviceUrl+"&"+queryString;
					else serviceUrl=serviceUrl+"?"+queryString;
					if(!TextUtils.isEmpty(OMSApplication.getInstance().getGetLoadingMessage())){
						OMSApplication.getInstance().setGetLoadingMessage("");
					}
					OMSApplication.getInstance().setGetLoadingMessage(getBLList.get(0).blGetLoadingMessage);
					String serverTableName = new OMSServerMapperHelper().getServerTableName(getBLList.get(0).getDataTable);
					//modifiedDate = new OMSServerMapperHelper().getLastModifiedTime(getBLList.get(0).getDataTable); //pass modified date always
					if(hasPagination && currentPage > 1){
						/*new TransDBParser(context, BLExecutorActionCenter.this,
							blactionList.get(index).blusid, configDBAppId,getBLList.get(0).blGetLoadingMessage,true).execute(
									serviceUrl, serverTableName,modifiedDate);*/
						OMSApplication.getInstance().setTransDataAPIURL(serviceUrl);
						new WatchTransDBParser(context,BLExecutorActionCenter.this,blactionList.get(index).blusid, configDBAppId,getBLList.get(0).blGetLoadingMessage,true,serverTableName,modifiedDate,serviceUrl).callMessageService(serviceUrl);
						/*WatchTransDBParser(Context ctx, OMSReceiveListener receiveListener,
								String uniqueId, int appId, String loadingMessage, boolean hideProgressbar,String tableName,String modifiedDate,String serviceURL) {*/
					}else{
						/*new TransDBParser(context, BLExecutorActionCenter.this,
								blactionList.get(index).blusid, configDBAppId,getBLList.get(0).blGetLoadingMessage).execute(
										serviceUrl, serverTableName,modifiedDate);*/
						OMSApplication.getInstance().setTransDataAPIURL(serviceUrl);
						new WatchTransDBParser(context,BLExecutorActionCenter.this,blactionList.get(index).blusid, configDBAppId,getBLList.get(0).blGetLoadingMessage,serverTableName,modifiedDate,serviceUrl).callMessageService(serviceUrl);
					}
				}else{
				// This changes are for Oasis Project. # Start
					//Oasis URL code
//					if(serviceUrl.contains("OrderTracking/OrderSearch")){
//						OasisDetails oasisDetails = actionCenterHelper.getOasisData("127645");
//						if(oasisDetails.getCriteriaName().equalsIgnoreCase("PO Number")){
//							oasisDetails.setCriteriaName("PO");
//						} else if(oasisDetails.getCriteriaName().equalsIgnoreCase("Document Control Number")){
//							oasisDetails.setCriteriaName("DCN");
//						} else if(oasisDetails.getCriteriaName().equalsIgnoreCase("Invoice Number")){
//							oasisDetails.setCriteriaName("Invoice");
//						} else if(oasisDetails.getCriteriaName().equalsIgnoreCase("Containing ISBN")){
//							oasisDetails.setCriteriaName("ISBN");
//						}
//						serviceUrl = serviceUrl + "/" + oasisDetails.getCriteriaName() + "/" + oasisDetails.getCriteriacode();
//					}
//					else if(serviceUrl.contains("OrderTracking/OrderDetail")){
//						String orderNumberUsid = null;
//						OasisDetails oasisDetails = null;
//						if(transUsidList!=null)
//							orderNumberUsid = transUsidList.get(0);
//						oasisDetails = actionCenterHelper.getOasisOrderDetails(orderNumberUsid.trim());
//						/*if(oasisDetails.getCriteriaName().equalsIgnoreCase("PO Number")){
//							serachCriteria = "PO";
//						}*/
//						if(oasisDetails !=null)
//						serviceUrl = serviceUrl + "/DCN/" + oasisDetails.getOrderNumber() + "/"+oasisDetails.getOrderSystemIndicator();
//					}
//					else if(serviceUrl.contains("OrderTracking/OrderTitles") || serviceUrl.contains("OrderTracking/ShipmentDetails") || serviceUrl.contains("OrderTracking/BillTo")){
//						String orderNumberUsid = null;
//						OasisDetails oasisDetails = null;
//						if(transUsidList!=null)
//							orderNumberUsid = transUsidList.get(0);
//						oasisDetails = actionCenterHelper.getOasisOrderDetails(orderNumberUsid.trim());
//						/*if(oasisDetails.getCriteriaName().equalsIgnoreCase("PO Number")){
//							serachCriteria = "PO";
//						}*/
//						if(oasisDetails !=null)
//						{
//							if(serviceUrl.contains("OrderTracking/ShipmentDetails") || serviceUrl.contains("OrderTracking/BillTo"))
//								serviceUrl = serviceUrl + "/" + oasisDetails.getOrderNumber() + "/"+oasisDetails.getOrderSystemIndicator();
//							else
//								serviceUrl = serviceUrl + "/DCN/" + oasisDetails.getOrderNumber() + "/"+oasisDetails.getOrderSystemIndicator();
//						}
//					}
//					else if(serviceUrl.contains("OrderTracking/DeliverySummary")){
//						String orderNumberUsid = null;
//						OasisDetails oasisDetails = null;
//						if(transUsidList!=null)
//							orderNumberUsid = transUsidList.get(0);
//						oasisDetails = actionCenterHelper.getOasisShippmentDetails(orderNumberUsid.trim());
//						if(oasisDetails !=null)
//						serviceUrl = serviceUrl + "/"+ oasisDetails.getOrderNumber()+ "/"+ oasisDetails.getInvoiceNumber()+ "/"+oasisDetails.getOrderSystemIndicator();
//					}
//					else if(serviceUrl.contains("OrderTracking/PackageDetails")){
//						String orderNumberUsid = null;
//						OasisDetails oasisDetails = null;
//						if(transUsidList!=null)
//							orderNumberUsid = transUsidList.get(0);
//						oasisDetails = actionCenterHelper.getOasisDeliveryDetails(orderNumberUsid.trim());
//						if(oasisDetails !=null)
//						serviceUrl = serviceUrl + "/"+ oasisDetails.getOrderNumber()+ "/"+ oasisDetails.getInvoiceNumber()+ "/"+ oasisDetails.getParcelID()+ "/"+oasisDetails.getOrderSystemIndicator();
//					}
//					else if(serviceUrl.contains("ProductSearch/ShipToAccounts"))
//					{
//						serviceUrl = serviceUrl + "/09390559000";
//					}
					// This changes are for Oasis Project. # END
					if(!TextUtils.isEmpty(OMSApplication.getInstance().getGetLoadingMessage())){
						OMSApplication.getInstance().setGetLoadingMessage("");
					}
					OMSApplication.getInstance().setGetLoadingMessage(getBLList.get(0).blGetLoadingMessage);
					String serverTableName = new OMSServerMapperHelper().getServerTableName(getBLList.get(0).getDataTable);
					/*new TransDBParser(context, BLExecutorActionCenter.this,
							blactionList.get(index).blusid, configDBAppId,getBLList.get(0).blGetLoadingMessage).execute(
									serviceUrl, serverTableName);*/

					OMSApplication.getInstance().setTransDataAPIURL(serviceUrl);
					new WatchTransDBParser(context,BLExecutorActionCenter.this,blactionList.get(index).blusid, configDBAppId,getBLList.get(0).blGetLoadingMessage,serverTableName,"0.0",serviceUrl).callMessageService(serviceUrl);
				}
/*				// get it from bl post xml from BL table
				if(!TextUtils.isEmpty(blList.get("datatablename"))){
					 modifiedDate = new OMSServerMapperHelper().getLastModifiedTime(blList.get("datatablename"));
				}
				if(!TextUtils.isEmpty(blList.get("url"))){
					serviceUrl=blList.get("url");
					//serviceUrl=serviceUrl+"?"+"modifieddate="+modifiedDate;
				}
				
				if(!TextUtils.isEmpty(blList.get("hasuserid"))){
					if(blList.get("hasuserid").toString().equalsIgnoreCase("true") || blList.get("hasuserid").toString().equalsIgnoreCase("false")){
						hasUserId=Boolean.parseBoolean(blList.get("hasuserid"));
					}
				}*/
/*				
				if(OMSConstants.USE_GENERIC_URL){
					if(hasUserId){
						if(OMSApplication.getInstance().getLoginUserColumnName()!=null) {
						String userIdColumn=OMSApplication.getInstance().getLoginUserColumnName();
						
						String loginVal = "";
						@SuppressWarnings("unchecked")
						Hashtable<String,String> loginHash = OMSApplication.getInstance().getLoginCredentialHash();
						if(loginHash!=null && loginHash.get(userIdColumn)!=null){
							loginVal = loginHash.get(userIdColumn);
							serviceUrl=serviceUrl+"?"+"modifieddate="+modifiedDate+"&"+userIdColumn+"="+loginVal;
						}

					}}else{
					serviceUrl=serviceUrl+"?"+"modifieddate="+modifiedDate;
					}
				}else{
				serviceUrl = serviceUrl+"/"+modifiedDate;
			   }
				new TransDBParser(context, BLExecutorActionCenter.this,
						blactionList.get(index).blusid, configDBAppId).execute(
								serviceUrl, blList.get(0));*/
				
				} else {
					Toast.makeText(context,"Network not available.Please check.",
							Toast.LENGTH_SHORT).show();
				}
			} catch (IndexOutOfBoundsException e) {
				Log.e(TAG, "Exception in GET Action::" + e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				Log.e(TAG, "Exception in GET Action::" + e.getMessage());
				e.printStackTrace();

			}
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_DIALOG)) {
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			Map<String, String> mapMessage = xmlParser
					.processDialog(blactionList.get(index).blXml);
			Builder alert = new AlertDialog.Builder(context);
			alert.setTitle(OMSMessages.MESSAGE.getValue());
			alert.setMessage(mapMessage.get(OMSMessages.MESSAGE.getValue()));

			alert.setPositiveButton(
					mapMessage.get(OMSMessages.BUTTON2.getValue()),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							receiveResult(OMSMessages.BL_SUCCESS.getValue());
						}
					});

			alert.setNegativeButton(
					mapMessage.get(OMSMessages.BUTTON1.getValue()),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							receiveResult(OMSMessages.BL_CANCEL.getValue());
						}
					});

			alert.show();
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TOAST)) {
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			Map<String, String> mapMessage = xmlParser
					.processToast(blactionList.get(index).blXml);

			if (blResult == null || blResult.contentEquals(OMSMessages.BL_SUCCESS.getValue())) {
			Toast.makeText(context,
						mapMessage.get(OMSMessages.MESSAGE1.getValue()),
						Toast.LENGTH_SHORT).show();
				receiveResult(OMSMessages.BL_SUCCESS.getValue());
			} else if (blResult
					.contentEquals(OMSMessages.BL_FAILURE.getValue())) {
				Toast.makeText(context,
						mapMessage.get(OMSMessages.MESSAGE2.getValue()),
						Toast.LENGTH_SHORT).show();
				receiveResult(OMSMessages.BL_FAILURE.getValue());
			}
		  }else if (blactionList.get(index).blType
					.equalsIgnoreCase(OMSDatabaseConstants.BL_GLOBAL_BL) ||blactionList.get(index).blType
					.equalsIgnoreCase("globalbl") ) {
				new BLGlobalAsyncTaskHelper(context, mContainerId,
						BLExecutorActionCenter.this, transUsidList)
						.execute(blactionList.get(index).blXml);
			} else if (blactionList.get(index).blType
					.equalsIgnoreCase("filedownload")){
				new BLFileDownloader(context, mContainerId,
						BLExecutorActionCenter.this, transUsidList)
						.execute(blactionList.get(index).blXml);
			} /*else if (blactionList.get(index).blType
					.equalsIgnoreCase("fileupload")){
				new BLFileUploader(context, mContainerId,
						BLExecutorActionCenter.this, transUsidList)
						.execute(blactionList.get(index).blXml);
			}*/else if(blactionList.get(index).blType
					.equalsIgnoreCase(OMSDatabaseConstants.BL_STRING_MODIFIER)){
				new BLStringModifierAsyncTaskHelper(context, mContainerId,
						BLExecutorActionCenter.this, transUsidList).execute(blactionList.get(index).blXml);
			}
		  else{
				receiveResult(OMSMessages.BL_SUCCESS.getValue());
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void doBLActionType(List<BLExecutorDTO> blactionList) {
	//	Log.i(TAG, "blactionList.get(0) : " + blactionList.get(0).blXml);
	//	Log.i(TAG, "doBLActionType() : " + blactionList.get(index).blType);
		OMSApplication.getInstance().setTraceType(OMSConstants.TRACE_TYPE_BL);
	/*	OMSApplication.getInstance().setUserSessionId(OMSApplication.getInstance().getUserSessionId());
		OMSApplication.getInstance().setScreensessionId(OMSApplication.getInstance().getScreensessionId());*/
		
		Log.i(TAG, "doBLActionType() : " + blactionList.get(index).blType);
		if (blactionList.get(0).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_FOR)) {
			new BLFORAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList, configDBAppId)
					.processFORBLXML(blactionList.get(0).blXml);
		} else if (blactionList.get(0).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_EXPRESSION)) {
			new BLExpressionAsyncTask(context, mContainerId, rListener,
					transUsidList).execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_IF)) {
/*			new BLIFElseAsyncTask(context, mContainerId, rListener,
					transUsidList).processAddBL(blactionList.get(index).blXml);*/
			new BLIFElseAsyncTask(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList, configDBAppId)
					.processAddBL(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_IU)) {
			new BLIUAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_ONE_TO_MANY)) {
			new BLOneToManyAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_DELETE)) {
			new BLDeleteAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_CREATE_TABLE)) {
			new BLCreateTableAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.POST_TYPE_REQUEST)) {
			OMSApplication.getInstance().setTraceType(OMSConstants.TRACE_TYPE_SERVER);
			Log.i(TAG, ""+OMSApplication.getInstance().getTraceType());
		/*	OMSApplication.getInstance().setUserSessionId(OMSApplication.getInstance().getUserSessionId());
			OMSApplication.getInstance().setScreensessionId(OMSApplication.getInstance().getScreensessionId());*/
			try {
				
			if (OMSDBManager.checkNetworkConnectivity()) {
				BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
				Hashtable<String,String> blList = xmlParser.processPostBL(
						blactionList.get(index).blXml,
						OMSDatabaseConstants.POST_TYPE_REQUEST);
				// get it from xml
				// get it from bl post xml from BL table
				String clientTableName="";
				String transUrl ="";
				String schemaName="";
				String loadMessage="";
				String successMessageColumn="";
				String successMessage="";
				if(!TextUtils.isEmpty(OMSApplication.getInstance().getPostLoadingMessage())){
					OMSApplication.getInstance().setPostLoadingMessage("");
				}
				if(!TextUtils.isEmpty(blList.get("loadingmessage"))){
					loadMessage = blList.get("loadingmessage");
					OMSApplication.getInstance().setPostLoadingMessage(loadMessage);
				}
				if(!TextUtils.isEmpty(blList.get("datatablename"))){
			       clientTableName = blList.get("datatablename");
				}
				
				if(!TextUtils.isEmpty(blList.get("schema"))){
					schemaName = blList.get("schema");
					}
				if(!TextUtils.isEmpty(blList.get("successmessagecolumn"))){
					successMessageColumn=blList.get("successmessagecolumn");
				}
				if(!TextUtils.isEmpty(blList.get("successmessagetext"))){
					successMessage=blList.get("successmessagetext");
				}
	          /* if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE) {
					
					if(!TextUtils.isEmpty(clientTableName)) {
						transUrl = new OMSServerMapperHelper().getTransPostURL(clientTableName);	
					}
				}else {
				if(!TextUtils.isEmpty(blList.get("url"))){
					transUrl = blList.get("url");
					}
				}*/
	           if(!TextUtils.isEmpty(clientTableName)) {
					transUrl = new OMSServerMapperHelper().getTransPostURL(clientTableName);	
				}
				JSONObject JSONPayLoad = null ;
				if(clientTableName.equalsIgnoreCase("all")){
					JSONPayLoad = actionCenterHelper.getJsonPayload(schemaName);
					}else{
					 JSONPayLoad = actionCenterHelper.formJSONPayLoad(schemaName,
							clientTableName);
					}
/*				JSONObject JSONPayLoad = actionCenterHelper.formJSONPayLoad("",
						clientTableName);*/
				if (JSONPayLoad != null && JSONPayLoad.length() > 0) {
				String	serverTableName =  new OMSServerMapperHelper().getServerTableName(clientTableName);
					PostAsyncTaskHelper postAsyncTaskHelper = new PostAsyncTaskHelper(
							context, clientTableName, JSONPayLoad, blactionList.get(index).blusid,
							BLExecutorActionCenter.this, configDBAppId,loadMessage);
					if (blList != null && blList.size() > 0)
						postAsyncTaskHelper.execute(transUrl,
								serverTableName/*blList.get("datatablename")*/,schemaName,successMessageColumn,successMessage);
				} else {
					//rListener.receiveResult(OMSMessages.REFRESH.getValue());
					receiveResult(OMSMessages.BL_SUCCESS.getValue());
				}
				
			 } else{
				 Toast.makeText(context,"Network not available.Please check.",
							Toast.LENGTH_SHORT).show(); 
			 }
			} catch (SQLException e) {
				rListener.receiveResult(OMSMessages.REFRESH.getValue());
				Log.e(TAG, "Exception in POST Action::" + e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				rListener.receiveResult(OMSMessages.REFRESH.getValue());
				e.printStackTrace();
				Log.e(TAG, "Exception in POST Action::" + e.getMessage());
			}
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.GET_TYPE_REQUEST)) {
			OMSApplication.getInstance().setTraceType(OMSConstants.TRACE_TYPE_GET_LAUNCH);
			/*OMSApplication.getInstance().setUserSessionId(OMSApplication.getInstance().getUserSessionId());
			OMSApplication.getInstance().setScreensessionId(OMSApplication.getInstance().getScreensessionId());*/
			
			Log.i(TAG, "doBLActionType() : " + blactionList.get(index).blType);
			try {
			if (OMSDBManager.checkNetworkConnectivity()) {
				/*
				BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
				Hashtable<String,String> blList = xmlParser.processPostBL(
						blactionList.get(index).blXml,
						OMSDatabaseConstants.GET_TYPE_REQUEST);
				if (blList != null && blList.size() > 0)
					new TransDBParser(context, BLExecutorActionCenter.this,
							blactionList.get(index).blusid, configDBAppId).execute(
							blList.get("url"), blList.get("datatablename"));
			*/

				BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
				Hashtable<String,String> blList = xmlParser.processPostBL(
						blactionList.get(index).blXml,
						OMSDatabaseConstants.GET_TYPE_REQUEST);
				// get it from bl post xml from BL table
				String modifiedDate="0.0";
				String serviceUrl="";
				boolean hasUserId=false;
				String serverTableName="";
				//
				/*
									if(!TextUtils.isEmpty(getBLList.get(0).getDataTable)){
						if(!TextUtils.isEmpty(getBLList.get(0).hasModifiedDate)){  // modified date
							if(getBLList.get(0).hasModifiedDate.equalsIgnoreCase("true") || getBLList.get(0).hasModifiedDate.equalsIgnoreCase("false")){
								hasModifiedDate=Boolean.parseBoolean(getBLList.get(0).hasModifiedDate);
								if(hasModifiedDate){
									 modifiedDate = new OMSServerMapperHelper().getLastModifiedTime(getBLList.get(0).getDataTable);
									 String modifiedQuery = "modifieddate="+modifiedDate;
									 queryArray.add(modifiedQuery);
								}
							}
						}
					}
				*/
				//
				if(!TextUtils.isEmpty(blList.get("datatablename"))){
					if(!TextUtils.isEmpty(blList.get("modifieddate"))){  // modified date
						boolean hasModifiedDate=false;
						if(blList.get("modifieddate").toString().equalsIgnoreCase("true") || blList.get("modifieddate").toString().equalsIgnoreCase("false")){
							hasModifiedDate=Boolean.parseBoolean(blList.get("modifieddate"));
							if(hasModifiedDate){
								 modifiedDate = new OMSServerMapperHelper().getLastModifiedTime(blList.get("datatablename"));			
							}
						}	
					}
					
					 
				}
		/*		if(OMSConstants.USE_GENERIC_WEBSERVICE_ERROR_RESPONSE) {
					
					if(!TextUtils.isEmpty(blList.get("datatablename"))) {
						String clientTable = blList.get("datatablename");
						serviceUrl = new OMSServerMapperHelper().getTransGetURL(clientTable);	
						serverTableName =  new OMSServerMapperHelper().getServerTableName(clientTable);
					}
				}
				else{
				if(!TextUtils.isEmpty(blList.get("url"))){
					serviceUrl=blList.get("url");
					//serviceUrl=serviceUrl+"?"+"modifieddate="+modifiedDate;
				}
			}
			*/	
				if(!TextUtils.isEmpty(blList.get("datatablename"))) {
					String clientTable = blList.get("datatablename");
					serviceUrl = new OMSServerMapperHelper().getTransGetURL(clientTable);	
					serverTableName =  new OMSServerMapperHelper().getServerTableName(clientTable);
				}

				
				if(!TextUtils.isEmpty(blList.get("hasuserid"))){
					if(blList.get("hasuserid").toString().equalsIgnoreCase("true") || blList.get("hasuserid").toString().equalsIgnoreCase("false")){
						hasUserId=Boolean.parseBoolean(blList.get("hasuserid"));
					}
				}
				
				if(OMSConstants.USE_GENERIC_URL){
					if(hasUserId){
						if(OMSApplication.getInstance().getLoginUserColumnName()!=null) {
						String userIdColumn=OMSApplication.getInstance().getLoginUserColumnName();
						
						String loginVal = "";
						@SuppressWarnings("unchecked")
						Hashtable<String,String> loginHash = OMSApplication.getInstance().getLoginCredentialHash();
						if(loginHash!=null && loginHash.get(userIdColumn)!=null){
							loginVal = loginHash.get(userIdColumn);
							serviceUrl=serviceUrl+"?"+"modifieddate="+modifiedDate+"&"+userIdColumn+"="+loginVal;
						}

					}}else{
					serviceUrl=serviceUrl+"?"+"modifieddate="+modifiedDate;
					}
				}else{
				serviceUrl = serviceUrl+"/"+modifiedDate;
			   }
				String loadMessage ="";
				if(!TextUtils.isEmpty(blList.get("loadingmessage"))){
					 loadMessage = blList.get("loadingmessage");
				}
					
			
			/*	new TransDBParser(context, BLExecutorActionCenter.this,
						blactionList.get(index).blusid, configDBAppId,loadMessage).execute(
								serviceUrl,serverTableName ,modifiedDate*//*blList.get(0)*//*);*/

                OMSApplication.getInstance().setTransDataAPIURL(serviceUrl);
                new WatchTransDBParser(context,BLExecutorActionCenter.this,blactionList.get(index).blusid, configDBAppId,loadMessage,serverTableName,modifiedDate,serviceUrl).callMessageService(serviceUrl);
				
				} else {Toast.makeText(context,"Network not available.Please check.",
							Toast.LENGTH_SHORT).show();
				}
			} catch (IndexOutOfBoundsException e) {
				Log.e(TAG, "Exception in GET Action::" + e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				Log.e(TAG, "Exception in GET Action::" + e.getMessage());

			}
		}
		else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_DIALOG)) {
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			Map<String, String> mapMessage = xmlParser
					.processDialog(blactionList.get(index).blXml);
			Builder alert = new AlertDialog.Builder(context);
			alert.setTitle(OMSMessages.MESSAGE.getValue());
			alert.setMessage(mapMessage.get(OMSMessages.MESSAGE.getValue()));

			alert.setPositiveButton(
					mapMessage.get(OMSMessages.BUTTON2.getValue()),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							receiveResult(OMSMessages.BL_SUCCESS.getValue());
						}
					});

			alert.setNegativeButton(
					mapMessage.get(OMSMessages.BUTTON1.getValue()),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							receiveResult(OMSMessages.BL_CANCEL.getValue());
						}
					});

			alert.show();
		} else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_TOAST)) {
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			Map<String, String> mapMessage = xmlParser
					.processToast(blactionList.get(index).blXml);

			if (blResult == null || blResult.contentEquals(OMSMessages.BL_SUCCESS.getValue())) {
				Toast.makeText(context,
						mapMessage.get(OMSMessages.MESSAGE1.getValue()),
						Toast.LENGTH_SHORT).show();
				receiveResult(OMSMessages.BL_SUCCESS.getValue());
			} else  {
				Toast.makeText(context,
						mapMessage.get(OMSMessages.MESSAGE2.getValue()),
						Toast.LENGTH_SHORT).show();
				receiveResult(OMSMessages.BL_FAILURE.getValue());
			}
		}else if (blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_GLOBAL_BL)) {
			new BLGlobalAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		}else if (blactionList.get(index).blType
				.equalsIgnoreCase("filedownload")){
			new BLFileDownloader(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		}/*else if (blactionList.get(index).blType
				.equalsIgnoreCase("fileupload")){
			new BLFileUploader(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList)
					.execute(blactionList.get(index).blXml);
		}*/else if(blactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.BL_STRING_MODIFIER)){
			new BLStringModifierAsyncTaskHelper(context, mContainerId,
					BLExecutorActionCenter.this, transUsidList).execute(blactionList.get(index).blXml);
		}
		else{
			receiveResult(OMSMessages.BL_SUCCESS.getValue());
		}

	}

	@Override
	public void receiveResult(String result) {
		blResult = result;
		Log.i(TAG,"BL receiveResult()"+result);
		if(result!=null){
			if (result.contentEquals(OMSMessages.BL_SUCCESS.getValue()) ||
					result.contentEquals(OMSMessages.ACTION_CENTER_SUCCESS
							.getValue()) ) {
				index++;
				if (index < blActionList.size()) {
					doBLActionType(blActionList, index);
				} else {
					Log.d(TAG, "All BLs Done");
	/*				ContentValues contentValues = new ContentValues();
					contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
							OMSDatabaseConstants.ACTION_STATUS_FINISHED);
					actionCenterHelper.insertOrUpdateActionQueue(contentValues,
							action_center_usid, configDBAppId);*/
					/*rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS
							.getValue());*/
			
					  rListener.receiveResult(OMSMessages.REFRESH.getValue());
				}
			} else if (result.contentEquals(OMSMessages.BL_FAILURE.getValue()) || result.contentEquals(OMSMessages.ACTION_CENTER_FAILURE
					.getValue()) ) {
				/*rListener.receiveResult(OMSMessages.ACTION_CENTER_FAILURE
						.getValue());*/
				rListener.receiveResult(OMSMessages.REFRESH.getValue());
			} else if (result.contentEquals(OMSMessages.BL_CANCEL.getValue())) {
	/*			ContentValues contentValues = new ContentValues();
				contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
						OMSDatabaseConstants.ACTION_STATUS_FINISHED);
				actionCenterHelper.insertOrUpdateActionQueue(contentValues,
						action_center_usid, configDBAppId);*/
				//rListener.receiveResult("ActionCenterSuccess");
			//	rListener.receiveResult(OMSMessages.REFRESH.getValue());
				rListener.receiveResult("stop_navigation");
			}else if(result.contentEquals("BLGetSuccess")){
				new BLFailedTransactionExecutor(context, mContainerId,BLExecutorActionCenter.this, configDBAppId).doFailedTransactionAction(OMSDatabaseConstants.GET_TYPE_REQUEST,OMSDatabaseConstants.ACTION_STATUS_TRIED);
			}else if(result.contentEquals("BLPostSuccess")){
				new BLFailedTransactionExecutor(context, mContainerId,BLExecutorActionCenter.this, configDBAppId).doFailedTransactionAction(OMSDatabaseConstants.POST_TYPE_REQUEST,OMSDatabaseConstants.ACTION_STATUS_TRIED);
			}else{
				  rListener.receiveResult(OMSMessages.REFRESH.getValue());
			}
		}else{
			Log.e(TAG, "Result is null");
		}
	}

}

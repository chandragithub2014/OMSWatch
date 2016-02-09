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
package watch.oms.omswatch.actioncenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import watch.oms.omswatch.R;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorActionCenter;
import watch.oms.omswatch.actioncenter.helpers.ActionCenterHelper;
import watch.oms.omswatch.actioncenter.helpers.CustomActionData;
import watch.oms.omswatch.actioncenter.helpers.DoAction;
import watch.oms.omswatch.actioncenter.helpers.TransDBParser;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.parser.OMSServerMapperHelper;


/**
 * 
 * @author 280779
 * 
 *         Performs actions fired from UI component like Buttons, List items
 *         etc.,
 */

public class ActionCenter implements OMSReceiveListener {
	private final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private ActionCenterHelper actionCenterHelper;
	private OMSReceiveListener rListener;
	private String actionQueueId = null;
	private List<String> transUsidList;
	private List<DoAction> actionList;
	private int index = 0;
	private int configDBAppId;
	private String universalActionUniqueId = null;

	private String filterColumnName;
	private String filterColumnValue;
	//private TransparentProgressDialog pd;

	public ActionCenter(Activity aContext, int containerId,
			OMSReceiveListener receiveListener, int appId) {
		context = aContext;
		mContainerId = containerId;
		rListener = receiveListener;
		configDBAppId = appId;
		actionCenterHelper = new ActionCenterHelper(context);
	}

	public ActionCenter(Activity aContext, int containerId,
			OMSReceiveListener receiveListener, int appId,
			String filterColumnName, String filterColumnValue) {
		context = aContext;
		mContainerId = containerId;
		rListener = receiveListener;
		configDBAppId = appId;
		actionCenterHelper = new ActionCenterHelper(context);
		this.filterColumnName = filterColumnName;
		this.filterColumnValue = filterColumnValue;
	}

	/**
	 * Retrieves 'Actions' from Action Table and inserts into ActionQueue table
	 * as status 'fresh'.
	 * 
	 */
	public void doAction(String navusid, int buttonId,
			List<String> transUsidList) {
		this.transUsidList = transUsidList;
		/*actionCenterHelper.getFromActionTableAndUpdateActionQueue(navusid,
				buttonId, configDBAppId);*/
		actionList = actionCenterHelper.getActionListFromActionTable(navusid,
				buttonId, configDBAppId);
		// Retrieves the Actions which have 'fresh' status in ActionQueue.
		List<String> actionTypesList = actionCenterHelper
				.getActionTypesFromActionTable(navusid, buttonId, configDBAppId);
	//	executeActionQueue();
		filterActionListWithActionTypes(actionTypesList);
		if (actionList != null && !actionList.isEmpty()) {
			if (actionList.size() > 0) {
				// Process the Action based on the it's Type.
				doActionType(actionList, index);
			}
		}else{
			rListener.receiveResult(OMSMessages.REFRESH.getValue());
		}
	}

	private void filterActionListWithActionTypes(List<String> actionTypesList) {
		if (actionList != null && actionTypesList != null
				&& actionList.size() > 0 && actionTypesList.size() > 0) {
			List<DoAction> tempActionList = actionList;
			List<DoAction> targetActionList = new ArrayList<DoAction>();
			for (int i = 0, j = 0; i < actionTypesList.size(); i++, j++) {
				{
					if (actionTypesList.get(i).equalsIgnoreCase(
							tempActionList.get(j).actionType)) {
						targetActionList.add(tempActionList.get(j));
					}
				}
			}
			if (targetActionList != null && targetActionList.size() > 0) {
				actionList.clear();
				actionList.addAll(targetActionList);
			}
		}
	}

	/**
	 * Query ActionQueue for newly added rows whose status is fresh.
	 * 
	 */
	private void executeActionQueue() {
		String actionUsid = null;
		String actionType = null;
		Cursor actionQueueCursor = null;
		actionQueueCursor = actionCenterHelper
				.getActionQueueCursorForStatus(OMSDatabaseConstants.ACTION_STATUS_NEW);
		if (actionQueueCursor != null && actionQueueCursor.getCount() > 0) {
			actionList = new ArrayList<DoAction>();
			actionQueueCursor.moveToFirst();
			do {
				actionType = actionQueueCursor
						.getString(actionQueueCursor
								.getColumnIndex(OMSDatabaseConstants.ACTION_QUEUE_TYPE));
				actionUsid = actionQueueCursor.getString(actionQueueCursor
						.getColumnIndex(OMSDatabaseConstants.ACTION_UNIQUE_ID));
				actionQueueId = actionQueueCursor.getString(actionQueueCursor
						.getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));
				DoAction tempAction = new DoAction();
				tempAction.actionType = actionType;
				tempAction.actionUsid = actionUsid;
				tempAction.uniqueRowId = actionQueueId;
				actionList.add(tempAction);
			} while (actionQueueCursor.moveToNext());
		} else {
			rListener.receiveResult(OMSMessages.GET_FAILURE.getValue());
		}
		// close the cursor
		if (actionQueueCursor != null)
			actionQueueCursor.close();
	}

	/**
	 * Process the Action based on the it's Type like GET, POST, CALL, SMS, BL
	 * etc.,
	 * 
	 * @param actionList
	 *            - List of Action which have Status 'fresh'
	 * @param index
	 */
	private void doActionType(List<DoAction> actionList, int index) {
		OMSServerMapperHelper servermapperhelper = new OMSServerMapperHelper();
		if (actionList != null && !actionList.isEmpty()) {
			if (actionList.get(index).actionType
					.equalsIgnoreCase(OMSConstants.ACTION_TYPE_GET)) {
				List<String> getDataList = new ArrayList<String>();
				try {
					getDataList = actionCenterHelper.getGetData(
							actionList.get(index).actionUsid, configDBAppId);
					if (getDataList != null && !getDataList.isEmpty()) {
						String transUrl = servermapperhelper.getTransURL(
								getDataList.get(1), OMSMessages.GET.getValue());
						new TransDBParser(context, ActionCenter.this,
								actionList.get(index).uniqueRowId,
								configDBAppId,"").execute(transUrl);
					}
				} catch (IndexOutOfBoundsException e) {
					Log.e(TAG, "Exception in GET Action::" + e.getMessage());

				} catch (SQLException e) {
					Log.e(TAG, "Exception in GET Action::" + e.getMessage());

				}
			} /*else if (actionList.get(index).actionType
					.equalsIgnoreCase(OMSConstants.ACTION_TYPE_CALL)) {
				new Call(context, mContainerId, configDBAppId).action(
						actionList.get(index).actionUsid,
						actionList.get(index).uniqueRowId);
			} else if (actionList.get(index).actionType
					.equalsIgnoreCase(OMSConstants.ACTION_TYPE_SMS)) {
				new Sms(context, mContainerId, configDBAppId).action(
						actionList.get(index).actionUsid,
						actionList.get(index).uniqueRowId);
			} else if (actionList.get(index).actionType
					.equalsIgnoreCase(OMSConstants.ACTION_TYPE_MAIL)) {
				new Email(context, mContainerId, configDBAppId).action(
						actionList.get(index).actionUsid,
						actionList.get(index).uniqueRowId);
			}*/ else if (actionList.get(index).actionType
					.equalsIgnoreCase(OMSConstants.ACTION_TYPE_BL)) {
				new BLExecutorActionCenter(context, mContainerId,
						ActionCenter.this, configDBAppId, transUsidList)
						.doBLAction(actionList.get(index).actionUsid,
								actionList.get(index).uniqueRowId);
			} else if (actionList.get(index).actionType
					.equalsIgnoreCase(OMSConstants.ACTION_TYPE_MOBILE_STUDIO_UNIVERSAL_GET)) {/*
				Log.d(TAG,
						"ACTION_TYPE_MOBILE_STUDIO_UNIVERSAL_GET ACTION_TYPE_MOBILE_STUDIO_UNIVERSAL_GET");
				universalActionUniqueId = actionList.get(index).uniqueRowId;
				try {
					if (OMSDBManager.checkNetworkConnectivity()) {

						pd = new TransparentProgressDialog(context,
								R.drawable.ic_core_image_for_rotation, "Loading...");
						pd.setCancelable(false);
						pd.show();
						pd.setTitle(Html
								.fromHtml("<b><H1>"
										+ context.getResources().getString(
												R.string.app_logo_bottom)
										+ "</H1></b>"));

					}
					OMSDBManager manager = new OMSDBManager(context,
							ActionCenter.this);
					manager.load();
				} catch (IllegalArgumentException e) {
					Log.e(TAG,
							"Exception in MOBILE UNIVERSE GET Action::"
									+ e.getMessage());

				}

		*/	} else if (actionList.get(index).actionType
					.equalsIgnoreCase(OMSConstants.ACTION_TYPE_CUSTOM_ACTION)) {
				List<CustomActionData> customActionList = new ArrayList<CustomActionData>();
				String actionusid = actionList.get(index).actionUsid;
				customActionList = actionCenterHelper.getCustomActionData(
						actionusid, configDBAppId);
				if (customActionList.size() > 0) {
					int methodNumber = customActionList.get(0).methodNumber;
					if (transUsidList.size() > 0) {
						new CustomAction(context, mContainerId,
								ActionCenter.this, configDBAppId)
								.setCustomData(null, methodNumber, actionusid,
										configDBAppId, transUsidList,
										filterColumnName, filterColumnValue);
					}

				}
			} else if (actionList.get(index).actionType
					.equalsIgnoreCase(OMSConstants.ACTION_TYPE_SOCIAL_INTEGRATION)) {
				// Need to implement Social Integration related code
				ContentValues contentValues = new ContentValues();
				contentValues.put(OMSDatabaseConstants.ACTION_QUEUE_STATUS,
						OMSDatabaseConstants.ACTION_STATUS_FINISHED);
				actionCenterHelper.insertOrUpdateActionQueueByActionusid(
						contentValues, actionList.get(index).actionUsid,
						configDBAppId);
			}

		}
	}

	/**
	 * Retrieves 'Actions' from Action Table and inserts into ActionQueue table
	 * as status 'fresh'.
	 * 
	 */
	public void MobileStudioGetAction(String navusid, int buttonId,
			ArrayList<String> transUsidList) {
		this.transUsidList = transUsidList;
		actionList = actionCenterHelper.getActionListFromActionTable(navusid,
				buttonId, configDBAppId);
/*		actionCenterHelper.getFromActionTableAndUpdateActionQueue(navusid,
				buttonId, configDBAppId);
		// Retrieves the Actions which have 'fresh' status in ActionQueue.
		executeActionQueue();*/
		if (actionList != null && !actionList.isEmpty()) {
			if (actionList.size() > 0) {
				// Process the Action based on the it's Type.
				doActionType(actionList, index);
			}
		}
	}

	@Override
	public void receiveResult(String result) {
		if (result.contentEquals(OMSMessages.ACTION_CENTER_SUCCESS.getValue())) {
			index++;
			if (index < actionList.size()) {
				doActionType(actionList, index);
			} else {
				Log.d(TAG, "All Asyncs Done");
				rListener.receiveResult(OMSMessages.REFRESH.getValue());
			}
		} else if (result.contentEquals(OMSMessages.ACTION_CENTER_FAILURE
				.getValue())) {
			index++;
			if (index < actionList.size()) {
				doActionType(actionList, index);
			} else {
				Log.d(TAG, "All Asyncs Done");
				rListener.receiveResult(OMSMessages.REFRESH.getValue());
			}
		} else if (result.contains(OMSMessages.TRANS_DATABASE_SUCCESS
				.getValue())) {
			/*
			 * Toast.makeText(context,
			 * context.getResources().getString(R.string.transdb_success),
			 * Toast.LENGTH_LONG).show();
			 */
			
			/*if (pd != null && pd.isShowing()
					&& OMSDBManager.checkNetworkConnectivity()) {
				pd.dismiss();
			}*/
			rListener.receiveResult(OMSMessages.REFRESH.getValue());
		/*	if (OMSDBManager.checkNetworkConnectivity()) {
				
				 * pDialog.setMessage(context.getResources().getString(
				 * R.string.receive_policydb));
				 
				// pd.setTitle("Loading");
				pd.setTitle(Html.fromHtml("<b><H1>"
						+ context.getResources().getString(
								R.string.app_logo_bottom) + "<H1></b>"));
			}*/

		} else if (result.contains(OMSMessages.CONFIG_DATABASE_PARSE_SUCCESS
				.getValue())) {
			/*
			 * Toast.makeText( context,
			 * context.getResources().getString(R.string.configdb_success),
			 * Toast.LENGTH_LONG).show();
			 */
			/*if (OMSDBManager.checkNetworkConnectivity()) {
				*//*
				 * pDialog.setMessage(context.getResources().getString(
				 * R.string.receive_transdb));
				 *//*
				// pd.setTitle("Loading");
				pd.setTitle(Html.fromHtml("<b><H1>"
						+ context.getResources().getString(
								R.string.app_logo_bottom) + "</H1></b>"));
			}*/
		}else {
			rListener.receiveResult(OMSMessages.REFRESH.getValue());
		}
	}
}
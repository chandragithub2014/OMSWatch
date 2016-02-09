/**
 * 
 */
package watch.oms.omswatch.actioncenter.blexecutor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.actioncenter.blexecutor.dto.BLFailedExecutorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.helper.BLHelper;
import watch.oms.omswatch.actioncenter.helpers.ActionCenterHelper;
import watch.oms.omswatch.actioncenter.helpers.PostAsyncTaskHelper;
import watch.oms.omswatch.actioncenter.helpers.TransDBParser;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

/**
 * @author 245742
 *
 */
public class BLFailedTransactionExecutor implements OMSReceiveListener {
	
	private Activity context;
	private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private OMSReceiveListener rListener;
	private int configDBAppId;
	private List<BLFailedExecutorDTO> blFailedTransactionList = new ArrayList<BLFailedExecutorDTO>();
	private int index = 0;
	private final String TAG = this.getClass().getSimpleName();
//	private String action_center_usid = null;
	private ActionCenterHelper actionCenterHelper;
	private String blResult;
	
	public BLFailedTransactionExecutor(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, int appId
			) {
		context = FragmentContext;
		mContainerId = containerId;
		rListener = receiveListener;
		configDBAppId = appId;
		actionCenterHelper = new ActionCenterHelper(context);
	}

	public void doFailedTransactionAction(String BLType,String status) {
		Log.i(TAG, "BLType : " + BLType);
		blFailedTransactionList= new BLHelper(context).getBLFailedDetails(BLType, status, configDBAppId);
		if (blFailedTransactionList != null && !blFailedTransactionList.isEmpty()) {
			doBLFailedTransactionType(blFailedTransactionList, index);
		}
		else{
			rListener.receiveResult(OMSMessages.BL_SUCCESS
					.getValue());
		}
	}
	
	
	
	private void doBLFailedTransactionType(
			List<BLFailedExecutorDTO> blFailedTransactionList, int index) {
		if (blFailedTransactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.GET_TYPE_REQUEST)) {
			try {
				BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
				String loadingMessage="";
				if(!TextUtils.isEmpty(OMSApplication.getInstance().getGetLoadingMessage())){
					loadingMessage = OMSApplication.getInstance().getGetLoadingMessage();
				}
				// get it from bl post xml from BL table
				new TransDBParser(context, BLFailedTransactionExecutor.this,
						blFailedTransactionList.get(index).actionusid, configDBAppId,loadingMessage).execute(
								blFailedTransactionList.get(index).serviceurl, blFailedTransactionList.get(index).dataTableName);
			} catch (IndexOutOfBoundsException e) {
				Log.e(TAG, "Exception in GET Action::" + e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				Log.e(TAG, "Exception in GET Action::" + e.getMessage());
				e.printStackTrace();

			}
		}else if(blFailedTransactionList.get(index).blType
				.equalsIgnoreCase(OMSDatabaseConstants.POST_TYPE_REQUEST)){

			try {
				BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			/*	List<String> blList = xmlParser.processPostBL(
						blactionList.get(index).blXml,
						OMSDatabaseConstants.POST_TYPE_REQUEST);*/
				// get it from xml
				String clientTableName = blFailedTransactionList.get(index).dataTableName;
				String schemaName = blFailedTransactionList.get(index).schema;
				// get it from bl post xml from BL Table
				String transUrl = blFailedTransactionList.get(index).serviceurl;
				JSONObject JSONPayLoad=null;
				if(clientTableName.equalsIgnoreCase("all")){
					JSONPayLoad = actionCenterHelper.getJsonPayload(schemaName);
					}else{
					 JSONPayLoad = actionCenterHelper.formJSONPayLoad(schemaName,
							clientTableName);
					}
				/*JSONObject JSONPayLoad = actionCenterHelper.formJSONPayLoad("",
						clientTableName);*/
				if (JSONPayLoad != null && JSONPayLoad.length() > 0) {
					String loadingMessage="Loading";
					if(!TextUtils.isEmpty(OMSApplication.getInstance().getPostLoadingMessage())){
						loadingMessage = OMSApplication.getInstance().getPostLoadingMessage();
					}
					PostAsyncTaskHelper postAsyncTaskHelper = new PostAsyncTaskHelper(
							context, clientTableName, JSONPayLoad, blFailedTransactionList.get(index).actionusid,
							BLFailedTransactionExecutor.this, configDBAppId,loadingMessage);

					postAsyncTaskHelper.execute(transUrl, clientTableName,schemaName);
				} else {
					rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
				}
			} catch (SQLException e) {
				rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
				Log.e(TAG, "Exception in POST Action::" + e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
				e.printStackTrace();
				Log.e(TAG, "Exception in POST Action::" + e.getMessage());
			}
		
		}
	}

	@Override
	public void receiveResult(String result) {
		if (result.contentEquals("BLGetSuccess") ||
				result.contentEquals("BLPostSuccess") ) {
			index++;
			if (index < blFailedTransactionList.size()) {
				doBLFailedTransactionType(blFailedTransactionList, index);
			} else {
				Log.d(TAG, "All Pending BLs Done");
				rListener.receiveResult(OMSMessages.ACTION_CENTER_SUCCESS
						.getValue());
			}
		}else{
			rListener.receiveResult(OMSMessages.BL_SUCCESS
					.getValue());
		}
	}

}

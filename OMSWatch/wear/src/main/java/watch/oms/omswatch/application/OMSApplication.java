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
package watch.oms.omswatch.application;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import watch.oms.omswatch.R;
import watch.oms.omswatch.WatchDB.OMSDBManager;


public class OMSApplication<autoButtonNavHash> extends Application {
	private static OMSApplication singleton;
	private String appId;
	private String appSchema;
	private String appUrl;
	private String configUrl;
	private String transUrl;
	private String imageUrl;
	private String transpostUrl;
	private String currentNavUsid;
	private String deviceID;
	private Activity activity;
	private Activity mainActivity;
	
	private int roleID;
	
	private int previouslyClickedSlidingMenuItemPosition=-1;
	private boolean isInViewPager=false;
	private boolean isScrollerTemplate=false;
	private String appName="";
	private String serverProcessDuration="";
	private String databaseProcessDuration="";
	private String traceType="";
	
	public Activity getMainActivity() {
		return mainActivity;
	}

	private ViewGroup root, detail;
	private boolean isTablet;
	private int loginAttemptCount;
	private String loginUserName;
	private String multiFormEditTextHiddenValue;
	private String globalFilterColumnValue;
	private List<String> filterStackArrayList;
	private boolean isGlobal999ByPass;
	private boolean splitView = true;
	private Handler syncHandler = null;
	private Hashtable<String,Integer> testHash;
	private Hashtable<String,Boolean> executionHash;
	private boolean isProgressDialogVisible;	
	private ActionBarDrawerToggle mDrawerToggle;	
	private Map<String,String> retainWhereClause = new HashMap<String,String>();
	private Hashtable<String,String> loginCredentialHash;
	private String loginUserColumnName;
	private Hashtable<String,Object> globalBLHash;
	private Map<String,String> dependentPickerMap = new HashMap<String,String>();
	
	private boolean isEnableTransService = false;
	private boolean isAcceleroMeterSupported;

	private boolean isUserLoggedIn;
	private int logOutScreenOrder = -1;
	private String postLoadingMessage="";
	private String getLoadingMessage="";
	private String appLaunchTimeUsid="";
	private String userSessionId="";
	private String screensessionId="";
	private int widgetID;



	public int getRoleID() {
		return 1;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	
	public String getPostLoadingMessage() {
		return postLoadingMessage;
	}
	public void setPostLoadingMessage(String postLoadingMessage) {
		this.postLoadingMessage = postLoadingMessage;
	}
	public String getGetLoadingMessage() {
		return getLoadingMessage;
	}
	public void setGetLoadingMessage(String getLoadingMessage) {
		this.getLoadingMessage = getLoadingMessage;
	}
	public int getLogOutScreenOrder() {
		return logOutScreenOrder;
	}
	public void setLogOutScreenOrder(int logOutScreenOrder) {
		this.logOutScreenOrder = logOutScreenOrder;
	}

	public boolean isUserLoggedIn() {
		return isUserLoggedIn;
	}
	public void setUserLoggedIn(boolean isUserLoggedIn) {
		this.isUserLoggedIn = isUserLoggedIn;
	}
	public boolean isEnableTransService() {
		return isEnableTransService;
	}
	public void setEnableTransService(boolean firstTime) {
		this.isEnableTransService = firstTime;
	}
	public Map<String, String> getRetainWhereClause() {
		return retainWhereClause;
	}
	public void setRetainWhereClause(Map<String, String> retainWhereClause) {
		this.retainWhereClause = retainWhereClause;
	}
	
	public Map<String, String> getDependentPickerMap() {
		return dependentPickerMap;
	}
	public void setDependentPickerMap(Map<String, String> dependentPickerMap) {
		this.dependentPickerMap = dependentPickerMap;
	}

	public ActionBarDrawerToggle getmDrawerToggle() {
		return mDrawerToggle;
	}
	public void setmDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
		this.mDrawerToggle = mDrawerToggle;
	}
	public Hashtable<String, Boolean> getExecutionHash() {
		return executionHash;
	}
	public void setExecutionHash(Hashtable<String, Boolean> executionHash) {
		this.executionHash = executionHash;
	}
	
	public Handler getSyncHandler() {
		return syncHandler;
	}
	public void setSyncHandler(Handler syncHandler) {
		this.syncHandler = syncHandler;
	}

	public int currentAppvisionAppId = 0;

	
	
	public int getCurrentAppvisionAppId() {
		return currentAppvisionAppId;
	}
	public void setCurrentAppvisionAppId(int currentAppvisionAppId) {
		this.currentAppvisionAppId = currentAppvisionAppId;
	}
	
	public boolean isSplitView() {
		return splitView;
	}
	public void setSplitView(boolean splitView) {
		this.splitView = splitView;
	}
	
	private int backStackCount = -2;
	public int getBackStackCount() {
		return backStackCount;
	}
	public void setBackStackCount(int backStackCount) {
		this.backStackCount = backStackCount;
	}
	

	public boolean isTablet() {
		return isTablet;
	}

	public void setTablet(boolean isTablet) {
		this.isTablet = isTablet;
	}

	/*public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
		root = (ViewGroup) activity.findViewById(R.id.rootpanel);
		detail = (ViewGroup) activity.findViewById(R.id.detailpanel);
		setTablet(isTabletDevice());
	}*/
	
	public void setMainActivity(Activity activity) {
		this.mainActivity = activity;
	}

	@SuppressWarnings("rawtypes")
	public static OMSApplication getInstance() {
		return singleton;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		/*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		deviceID = telephonyManager.getDeviceId();
		deviceID = "352373051582570";*/
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appid) {
		appId = appid;
	}

	public String getAppSchema() {
		return appSchema;
	}

	public void setAppSchema(String schemaname) {
		appSchema = schemaname;
	}

	public String getServerURL() {
		return appUrl;
	}

	public void setServerUrl(String url) {
		appUrl = url;
	}

	public String getConfigURL() {
		return configUrl;
	}

	public void setConfigURL(String configurl) {
		configUrl = configurl;
	}

	public String getTransURL() {
		return transUrl;
	}

	public void setTransURL(String transurl) {
		transUrl = transurl;
	}

	public String getImageURL() {
		return imageUrl;
	}

	public void setImageURL(String imageurl) {
		imageUrl = imageurl;
	}

	public String getTransPostURL() {
		return transpostUrl;
	}

	public void setTransPostURL(String transposturl) {
		transpostUrl = transposturl;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		OMSDBManager.closeDBConnections();
		super.onTerminate();
	}

	public void setNavUsid(String uniqueId) {
		this.currentNavUsid = uniqueId;
	}

	public String getNavUsid() {
		return currentNavUsid;
	}

	public String getDeviceId() {
		if(deviceID==null || deviceID.isEmpty()){
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			deviceID = telephonyManager.getDeviceId();
			if(deviceID==null || deviceID.isEmpty()) deviceID ="DEVICE_ID_NOT_FOUND";
			else if(deviceID.equals("000000000000000")) deviceID ="ANDROID_NO_IMEI";
		}
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	/**
	 * Checks if the device is a tablet or a phone
	 * 
	 * @param
	 *
	 * @return Returns true if the device is a Tablet
	 */
	public boolean isTabletDevice() {
		// Verifies if the Generalized Size of the device is XLARGE to be
		// considered a Tablet
		boolean xlarge = ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);

		// If XLarge, checks if the Generalized Density is at least MDPI
		// (160dpi)
		if (xlarge) {
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

			// MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
			// DENSITY_TV=213, DENSITY_XHIGH=320
			if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
					|| metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
					|| metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
					|| metrics.densityDpi == DisplayMetrics.DENSITY_TV
					|| metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

				// Yes, this is a tablet!
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * This method handles screenMode changes
	 * 
	 */
	/*public void setScreenMode(boolean isFullScreen) {
		
*//*		if (!isTablet())
			isFullScreen = true;*//*

		if (isFullScreen) {
			if (root.getVisibility() != View.GONE) {
				root.setVisibility(View.GONE);
				LayoutParams lp;
				lp = detail.getLayoutParams();
				lp.width = LayoutParams.MATCH_PARENT;
				detail.setLayoutParams(lp);
				splitView = false;
				Log.d("GlobalConstantsApplication",
						"Screen Mode is Full screen ");
				
			}
		} else {

			if (root.getVisibility() != View.VISIBLE) {
				root.setVisibility(View.VISIBLE);
				LayoutParams lp;
				lp = detail.getLayoutParams();
				lp.width = 0;
				detail.setLayoutParams(lp);
				splitView = true;
				Log.d("GlobalConstantsApplication",
						"Screen Mode is Split screen ");
				
			}
		}
	}*/

	/*
	 * setAllowedAttempts method sets the loginAttemptCount
	 */
	public void setAllowedAttempts(int loginAttemptCount) {
		this.loginAttemptCount = loginAttemptCount;
	}

	/*
	 * setAllowedAttempts method retrieves the loginAttemptCount
	 */
	public int getAllowedAttempts() {
		return loginAttemptCount;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;

	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public String getEditTextHiddenVal() {
		return multiFormEditTextHiddenValue;
	}

	public void setEditTextHiddenVal(String multiFormEditTextHiddenValue) {
		this.multiFormEditTextHiddenValue = multiFormEditTextHiddenValue;
	}

	public String getGlobalFilterColumnVal() {
		return globalFilterColumnValue;
	}

	public void setGlobalFilterColumnVal(String globalFilterColumnValue) {
		this.globalFilterColumnValue = globalFilterColumnValue;
	}
	
	public List<String> getFilteredArrayList(){
		return filterStackArrayList;
	}
	public void setFilteredArrayList(List<String> filterStackArrayList){
		this.filterStackArrayList = filterStackArrayList;
	}
	
	public boolean isGlobalByPass999(){
		return isGlobal999ByPass;
	}
	
	public void setGlobalByPass999(boolean isGlobal999ByPass){
		this.isGlobal999ByPass = isGlobal999ByPass;
	}
	
	public void setAutomationTestHash(Hashtable<String,Integer> testHash){
		this.testHash = testHash;
	}
	
	
	public Hashtable<String,Integer> getAutomationTestHash(){
		return testHash;
	}
	public boolean isProgressDialogVisible() {
		return isProgressDialogVisible;
	}
	public void setProgressDialogVisible(boolean isProgressDialogVisible) {
		this.isProgressDialogVisible = isProgressDialogVisible;
	}
	public Hashtable<String, String> getLoginCredentialHash() {
		return loginCredentialHash;
	}
	public void setLoginCredentialHash(Hashtable<String, String> loginCredentialHash) {
		this.loginCredentialHash = loginCredentialHash;
	}
	public String getLoginUserColumnName() {
		return loginUserColumnName;
	}
	public void setLoginUserColumnName(String loginUserColumnName) {
		this.loginUserColumnName = loginUserColumnName;
	}
	public Hashtable<String, Object> getGlobalBLHash() {
		return globalBLHash;
	}
	public void setGlobalBLHash(Hashtable<String, Object> globalBLHash) {
		this.globalBLHash = globalBLHash;
	}
	public boolean isAcceleroMeterSupported() {
		return isAcceleroMeterSupported;
	}
	public void setAcceleroMeterSupported(boolean isAcceleroMeterSupported) {
		this.isAcceleroMeterSupported = isAcceleroMeterSupported;
	}
	public int getPreviouslyClickedSlidingMenuItemPosition() {
		return previouslyClickedSlidingMenuItemPosition;
	}
	public void setPreviouslyClickedSlidingMenuItemPosition(
			int previouslyClickedSlidingMenuItemPosition) {
		this.previouslyClickedSlidingMenuItemPosition = previouslyClickedSlidingMenuItemPosition;
	}
	public boolean isInViewPager() {
		return isInViewPager;
	}
	public void setInViewPager(boolean isInViewPager) {
		this.isInViewPager = isInViewPager;
	}
	
	//For not Setting ActionBar Title if From Scroller Template to another template   
	//true in loadscreen for scroller template
	//false else where in any template and Sliding menu click
	public boolean isScrollerTemplate() {
		return isScrollerTemplate;
	}
	public void setScrollerTemplate(boolean isScrollerTemplate) {
		this.isScrollerTemplate = isScrollerTemplate;
	}
	public String getAppLaunchTimeUsid() {
		return appLaunchTimeUsid;
	}
	public void setAppLaunchTimeUsid(String appLaunchTimeUsid) {
		this.appLaunchTimeUsid = appLaunchTimeUsid;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getServerProcessDuration() {
		return serverProcessDuration;
	}
	public void setServerProcessDuration(String serverProcessDuration) {
		this.serverProcessDuration = serverProcessDuration;
	}
	public String getDatabaseProcessDuration() {
		return databaseProcessDuration;
	}
	public void setDatabaseProcessDuration(String databaseProcessDuration) {
		this.databaseProcessDuration = databaseProcessDuration;
	}
	private boolean isNegativeAppSlidingMenuClicked = false;
	public boolean isNegativeAppSlidingMenuClicked() {
		return isNegativeAppSlidingMenuClicked;
	}
	public void setNegativeAppSlidingMenuClicked(boolean isNegativeAppSlidingMenuClicked) {
		this.isNegativeAppSlidingMenuClicked = isNegativeAppSlidingMenuClicked;
	}
	public String getUserSessionId() {
		return userSessionId;
	}
	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
	public String getScreensessionId() {
		return screensessionId;
	}
	public void setScreensessionId(String screensessionId) {
		this.screensessionId = screensessionId;
	}
	public String getTraceType() {
		return traceType;
	}
	public void setTraceType(String traceType) {
		this.traceType = traceType;
	}

    public int getWidgetID() {
        return widgetID;
    }

    public void setWidgetID(int widgetID) {
        this.widgetID = widgetID;
    }

    // This changes are for Oasis Project. # Start
//	private String userKey;
//	private String oasisProductSearchScope;
//	
//	public String getUserKey() {
//		return userKey;
//	}
//	public void setUserKey(String userKey) {
//		this.userKey = userKey;
//	}
//	public String getOasisProductSearchScope() {
//		return oasisProductSearchScope;
//	}
//	public void setOasisProductSearchScope(String oasisProductSearchScope) {
//		this.oasisProductSearchScope = oasisProductSearchScope;
//	}
	// This changes are for Oasis Project. # End
}

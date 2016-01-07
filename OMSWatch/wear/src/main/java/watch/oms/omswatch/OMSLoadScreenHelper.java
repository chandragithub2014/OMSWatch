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
package watch.oms.omswatch;

import java.util.Hashtable;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.OMSDTO.NavigationItems;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.fragments.MultiFormFragment;
import watch.oms.omswatch.helpers.NavigationHelper;


/**
 * @author 280779 LoadScreenHelper - This class is used to load the templates
 *         based on the screen type field in navigation table.
 */

public class OMSLoadScreenHelper {

	private final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private NavigationHelper navHelper;

	public OMSLoadScreenHelper(Activity c, int containerId) {
		this.context = c;
		this.mContainerId = containerId;
		navHelper = new NavigationHelper();
	}

	/**
	 * this method is used to load the template in the screen based on the
	 * screen type comes from navigation table
	 * 
	 * @param screenType
	 * @param navId
	 * @param navUsid
	 * @param screenOrder
	 * @param isFullScreen
	 * @param isMain
	 * @param filterColumnName
	 * @param filterColumnVal
	 * @param headerTitle
	 */
	public void loadTargetScreen(String screenType, int navId, String navUsid,
			int screenOrder, boolean isFullScreen, boolean isMain,
			String filterColumnName, String filterColumnVal,
			String headerTitle, int customScreenContainerId, int appId) {
		OMSApplication.getInstance().setNavUsid(navUsid);
		OMSApplication.getInstance().setScrollerTemplate(false);
		Log.d(TAG, "method loadTargetScreen, ScreenType:" + screenType
				+ " navId:" + navId + " screenOrder:" + screenOrder
				+ " isFullScreen: " + isFullScreen + " isMain: " + isMain
				+ " appId: " + appId + "headerTitle :: " + headerTitle);

		boolean enableLogout = navHelper.isParentLogin(appId, navUsid);
		Bundle bundle = null;
		FragmentManager fm = context.getFragmentManager();
		//OMSApplication.getInstance().setInViewPager(false);
		/*if(!screenType.equalsIgnoreCase(OMSConstants.SLIDING_MENU_SCREEN_TYPE)){
			OMSFactory.enableNavigationDrawer(context, screenType, appId, usid, mContainerId, customScreenContainerId);	
		}*/
		/*// navigation drawer
		if (!screenType.equalsIgnoreCase(OMSConstants.TAB_SCREEN_TYPE)){
			OMSFactory.enableNavigationDrawer(context, screenType, appId, usid, mContainerId, customScreenContainerId);		
		}*/
		// navigation drawer ends here

		// Load List Screen Template
		if (screenType.equalsIgnoreCase(OMSConstants.LIST_SCREEN_TYPE)) {
           /*    NavigationHelper navigationHelper = new NavigationHelper();

            // Get SplitView Value from DB
         boolean showSplitView = navigationHelper.isSplitViewEnable(usid,
                    appId);
            Log.d(TAG, "SplitView from DB - Fragment Tabs" + showSplitView);*/
			Log.d(TAG,"mcontainerId:::"+mContainerId);
            bundle = new Bundle();
            bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
            bundle.putString(OMSMessages.UNIQUE_ID.getValue(), navUsid);
            bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
            bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
            bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
            bundle.putBoolean(OMSMessages.SHOW_LOGOUT.getValue(), enableLogout);
			bundle.putBoolean(OMSMessages.IS_BACK.getValue(),isMain);
/*			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(),
					((OMSApplication.getInstance().isSplitView())));*/
            Fragment listFragment = OMSFactory.getInstance(OMSFactory.TemplateType.List,
					navUsid);
            listFragment.setArguments(bundle);

            //
            if (isMain) {
                fm.beginTransaction().replace(mContainerId, listFragment)
                        .commit();
            } else {
                bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), false);
                fm.beginTransaction().replace(mContainerId, listFragment)
                        .addToBackStack(OMSMessages.NULL_STRING.getValue())
                        .commit();
            }
        }else if (screenType
				.equalsIgnoreCase(OMSConstants.MULTI_FORM_SCREEN_TYPE) || screenType.equalsIgnoreCase("Form")) {
            Fragment multiFormScreenFragment = OMSFactory.getInstance(
                    OMSFactory.TemplateType.MultiForm, navUsid);
            bundle = new Bundle();
            bundle.putString(OMSMessages.UNIQUE_ID.getValue(), navUsid);
            bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
            bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
            bundle.putBoolean(OMSMessages.IS_LOADSCREEN.getValue(), true);

            multiFormScreenFragment.setArguments(bundle);
            fm.beginTransaction().replace(mContainerId, multiFormScreenFragment)
                    .addToBackStack(OMSMessages.NULL_STRING.getValue())
                    .commit();
		}
			//
			/*if (isMain) {
				if (!(customScreenContainerId != OMSDefaultValues.NONE_DEF_CNT
						.getValue())
						&& OMSApplication.getInstance().isSplitView()
						&& OMSApplication.getInstance().isTablet() && showSplitView) {
					bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), true);
					bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(),
							(false));
					fm.beginTransaction().replace(R.id.rootpanel, listFragment)
					.commit();
				} else {
					bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), false);
					fm.beginTransaction().replace(R.id.detailpanel, listFragment)
					.commit();
				}
			} else {
				if (!(customScreenContainerId != OMSDefaultValues.NONE_DEF_CNT
						.getValue())
						&& OMSApplication.getInstance().isSplitView()
						&& OMSApplication.getInstance().isTablet() && showSplitView) {
					bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), true);
					fm.beginTransaction().replace(R.id.rootpanel, listFragment)
					.addToBackStack(OMSMessages.NULL_STRING.getValue())
					.commit();
				} else {
					bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), false);
					fm.beginTransaction().replace(R.id.detailpanel, listFragment)
					.addToBackStack(OMSMessages.NULL_STRING.getValue())
					.commit();
				}
			}*/
			/*if (isMain) {

				fm.beginTransaction().replace(mContainerId, listFragment)
				.commit();

			} else {

				fm.beginTransaction().replace(mContainerId, listFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();

			}*/
			// Load Spring Board Screen Template
		 /*else if (screenType
				.equalsIgnoreCase(OMSConstants.SPRINGBOARD_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SHOW_LOGOUT.getValue(), enableLogout);

			Fragment springBoradFragment = OMSFactory.getInstance(
					TemplateType.Springboard, usid);
			springBoradFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction()
						.replace(mContainerId, springBoradFragment).commit();
			} else {
				fm.beginTransaction()
						.replace(mContainerId, springBoradFragment)
						.addToBackStack(OMSMessages.NULL_STRING.getValue())
						.commit();
			}

			// Load Tab Screen Template
		}*//* else if (screenType.equalsIgnoreCase(OMSConstants.TAB_SCREEN_TYPE)) {
			boolean isLogout = navHelper.isParentLogin(
					Integer.parseInt(OMSConstants.APP_ID), usid);
			String listUsId = navHelper.getTabBarUsid(appId, usid);
			OMSFactory.startTabBarWithScreenOrder(screenOrder, usid,listUsId, context,
					customScreenContainerId, appId, isLogout);
		}*/// load map screen template
		/*else if (screenType.equalsIgnoreCase(OMSConstants.MAP_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putBoolean(OMSMessages.SHOW_LOGOUT.getValue(), enableLogout);
			Fragment mapScreenFragment = OMSFactory.getInstance(
					TemplateType.MapView, usid);
			mapScreenFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, mapScreenFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, mapScreenFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		}*/ else if (screenType.equalsIgnoreCase(OMSConstants.BAR_CHART_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putString(OMSMessages.SCREENID.getValue(), navUsid);
			bundle.putInt(OMSMessages.CONTAINER_ID.getValue(),
                    customScreenContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			Fragment barChart = OMSFactory.getInstance(OMSFactory.TemplateType.BarChart,
					navUsid);
			barChart.setArguments(bundle);
			fm.beginTransaction().replace(mContainerId, barChart)
			.addToBackStack(null).commit();
		} else if (screenType.equalsIgnoreCase(OMSConstants.LINE_CHART_TYPE)) {

			bundle = new Bundle();
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putString(OMSMessages.SCREENID.getValue(), navUsid);
			bundle.putInt(OMSMessages.CONTAINER_ID.getValue(),
                    customScreenContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			Fragment lineChart = OMSFactory.getInstance(OMSFactory.TemplateType.LineChart,
					navUsid);
			lineChart.setArguments(bundle);
			fm.beginTransaction().replace(mContainerId, lineChart)
			.addToBackStack(null).commit();

		} else if (screenType.equalsIgnoreCase(OMSConstants.PIE_CHART_TYPE)) {

			bundle = new Bundle();
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putString(OMSMessages.SCREENID.getValue(), navUsid);
			bundle.putInt(OMSMessages.CONTAINER_ID.getValue(),
					customScreenContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			Fragment pieChart = OMSFactory.getInstance(OMSFactory.TemplateType.PieChart,
					navUsid);
			pieChart.setArguments(bundle);
			fm.beginTransaction().replace(mContainerId, pieChart)
			.addToBackStack(null).commit();

		}/*else if (screenType.equalsIgnoreCase(OMSConstants.MEDIA_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			Fragment mediaControllerFragment = OMSFactory.getInstance(TemplateType.Media,
					usid);
			mediaControllerFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, mediaControllerFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, mediaControllerFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		
		} else if (screenType.equalsIgnoreCase(OMSConstants.SCROLLER_TEMPLATE_TYPE)) {
			OMSApplication.getInstance().setInViewPager(true);
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			Fragment viewPagerFragment = OMSFactory.getInstance(TemplateType.Scroller,
					usid);
			viewPagerFragment.setArguments(bundle);
			
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, viewPagerFragment)
				.commit();
			} else {
				bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), false);
				fm.beginTransaction().replace(mContainerId, viewPagerFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		} else if (screenType.equalsIgnoreCase(OMSConstants.GRID_VIEW_SCREEN_TYPE)) {
			OMSApplication.getInstance().setInViewPager(true);
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			Fragment gridViewFragment = OMSFactory.getInstance(TemplateType.GridView,
					usid);
			gridViewFragment.setArguments(bundle);
			
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, gridViewFragment)
				.commit();
			} else {
				bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), false);
				fm.beginTransaction().replace(mContainerId, gridViewFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		} *//*else if (screenType.equalsIgnoreCase(OMSConstants.LOGIN_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			Fragment loginFragment = OMSFactory.getInstance(TemplateType.Login,
					usid);
			loginFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, loginFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, loginFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
			// Load Gallery Template
		} else if (screenType
				.equalsIgnoreCase(OMSConstants.GALLERY_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SHOW_LOGOUT.getValue(), enableLogout);
			Fragment galleryFragment = OMSFactory.getInstance(
					TemplateType.Gallery, usid);
			galleryFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, galleryFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, galleryFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();

			}
			// load table template
		} else if (screenType.equalsIgnoreCase(OMSConstants.TABLE_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			Fragment tableScreenFragment = OMSFactory.getInstance(
					TemplateType.TableLayout, usid);
			tableScreenFragment.setArguments(bundle);

			if (isMain) {
				fm.beginTransaction()
				.replace(mContainerId, tableScreenFragment).commit();
			} else {
				fm.beginTransaction()
				.replace(mContainerId, tableScreenFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
			// Load Calendar Template
		} else if (screenType
				.equalsIgnoreCase(OMSConstants.CALENDAR_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    mContainerId);
			Fragment calendarFragment = OMSFactory.getInstance(
					TemplateType.Calendar, usid);
			calendarFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, calendarFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, calendarFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		}// load multi tab - horizontal scrolling template
		else if (screenType
				.equalsIgnoreCase(OMSConstants.MULTI_TAB_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);

			Fragment multiTabFragment = OMSFactory.getInstance(
					TemplateType.MultiTab, usid);
			multiTabFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, multiTabFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, multiTabFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		} else if (screenType
				.equalsIgnoreCase(OMSConstants.MULTI_FORM_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putBoolean(OMSMessages.IS_PREPOPULATED.getValue(), false);
			bundle.putInt(OMSMessages.ACTION_BUTTON_ID.getValue(),
                    OMSConstants.FORM_ACTION_BUTTON_ID);
			bundle.putString(OMSMessages.TRANS_USID.getValue(), "");
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.IS_LOADSCREEN.getValue(), true);
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putBoolean(OMSMessages.SHOW_LOGOUT.getValue(), enableLogout);
			
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this.context);
			boolean isAlreadyAuthenticated = sharedPreferences.getBoolean(
					"OMS_IS_AUTHENTICATED", false);
			
			 NavigationItems navigationItemItem = new NavigationHelper()
				.getNavigationDetailsByUsId(appId, usid);
				Log.d(TAG, "Shared Pref Authenticated?:" + isAlreadyAuthenticated + ", " +
                        "AppID:" + appId + ", ScreenName:" + navigationItemItem.screenName);
			if((isAlreadyAuthenticated && appId != -100)
					&& (!TextUtils.isEmpty(navigationItemItem.screenName) 
							&&
							( (navigationItemItem.screenName.
							equalsIgnoreCase(OMSConstants.LOGIN_SCREEN_TYPE)
							|| navigationItemItem.screenName.equalsIgnoreCase("Log In"))
							    || (navigationItemItem.screenName.
								equalsIgnoreCase("SignIn")
								|| navigationItemItem.screenName.
								    equalsIgnoreCase("Sign In"))))){
				 NavigationItems  childScreenItem = new NavigationHelper()
				.getChildForLoginScreen(appId,
						screenOrder);
				 OMSApplication.getInstance().setLoginUserName(sharedPreferences.getString("USER_ID",""));
				 
				Hashtable<String,Object> globalBLHash ; 
			    if(OMSApplication.getInstance().getGlobalBLHash()!=null)
			    {
			    	globalBLHash = OMSApplication.getInstance().getGlobalBLHash();
			    }else{
			    	globalBLHash = new Hashtable<String,Object>();
			    }
			    
			    OMSApplication.getInstance().setLoginUserColumnName(sharedPreferences.getString("USER_ID_COLUMN",""));
				globalBLHash.put(sharedPreferences.getString("USER_ID_COLUMN",""),sharedPreferences.getString("USER_ID",""));
				OMSApplication.getInstance().setGlobalBLHash(globalBLHash);
					
		      if(childScreenItem != null){
					new OMSLoadScreenHelper(this.context, mContainerId)
					.loadTargetScreen(childScreenItem.screentype,
							childScreenItem.parent_id,
							childScreenItem.uniqueId,
							childScreenItem.screenorder, isMain,
							null, null, "",
							OMSDefaultValues.NONE_DEF_CNT.getValue(),
							childScreenItem.appId);
		       }
			} else{
				Fragment multiFormScreenFragment = OMSFactory.getInstance(
						TemplateType.MultiForm, usid);
				multiFormScreenFragment.setArguments(bundle);
				if (isMain) {
					fm.beginTransaction()
					.replace(mContainerId, multiFormScreenFragment)
					.commit();
				} else {
					fm.beginTransaction()
					.replace(mContainerId, multiFormScreenFragment)
					.addToBackStack(OMSMessages.NULL_STRING.getValue())
					.commit();	
				}
			
			}
		} else if (screenType
				.equalsIgnoreCase(OMSConstants.PRE_POPULATED_MULTI_FORM_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putBoolean(OMSMessages.IS_PREPOPULATED.getValue(), true);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putBoolean(OMSMessages.IS_FROM_NAVIGATION.getValue(), true);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putString(OMSDatabaseConstants.UNIQUE_ROW_ID, "");
			Fragment multiFormScreenFragment = OMSFactory.getInstance(
                    TemplateType.MultiForm, usid);
			multiFormScreenFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction()
				.replace(mContainerId, multiFormScreenFragment)
				.commit();
			} else {
				fm.beginTransaction()
				.replace(mContainerId, multiFormScreenFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();

			}
		} else if (screenType.equalsIgnoreCase(OMSConstants.TILES_TYPE)) {

			bundle = new Bundle();
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			Fragment tileScreenFragment = OMSFactory.getInstance(
                    TemplateType.Tiles, usid);
			tileScreenFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, tileScreenFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, tileScreenFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}

		} else if (screenType.equalsIgnoreCase(OMSConstants.CUSTOM_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			Fragment customScreenFragment = OMSFactory.getInstance(
					TemplateType.CustomScreen, usid);
			customScreenFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction()
				.replace(mContainerId, customScreenFragment).commit();
			} else {
				fm.beginTransaction()
				.replace(mContainerId, customScreenFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
			// Load Coverflow screen Template
		} else if (screenType
				.equalsIgnoreCase(OMSConstants.COVERFLOW_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			Fragment coverFlowFragment = OMSFactory.getInstance(
					TemplateType.Coverflow, usid);
			coverFlowFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, coverFlowFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, coverFlowFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		} else if (screenType
				.equalsIgnoreCase(OMSConstants.WEBVIEW_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			Fragment webViewFragment = OMSFactory.getInstance(
					TemplateType.WebView, usid);
			webViewFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction().replace(mContainerId, webViewFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, webViewFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		}// load SectionList template
		else if (screenType
				.equalsIgnoreCase(OMSConstants.SECTIONLIST_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);

			Fragment sectionListFragment = OMSFactory.getInstance(
					TemplateType.SectionList, usid);
			sectionListFragment.setArguments(bundle);

			if (isMain) {
				fm.beginTransaction()
				.replace(mContainerId, sectionListFragment).commit();
			} else {
				fm.beginTransaction()
				.replace(mContainerId, sectionListFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		}
		// load media screen template
		else if (screenType
				.equalsIgnoreCase(OMSConstants.MEDIA_VIEWER_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);


			Fragment mediaViewerScreenFragment = OMSFactory.getInstance(
					TemplateType.MediaViewer, usid);
			mediaViewerScreenFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction()
				.replace(mContainerId, mediaViewerScreenFragment)
				.commit();
			} else {
				fm.beginTransaction()
				.replace(mContainerId, mediaViewerScreenFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		}
		// load dialog template
		else if (screenType.equalsIgnoreCase(OMSConstants.DIALOG_SCREEN_TYPE)) {
			OMSFactory.launchDialog(context, fm, mContainerId, screenOrder,
					isMain, usid, headerTitle, isFullScreen,
					customScreenContainerId, appId);
			// END
		}// load Frame by frame Animation template
		else if (screenType
				.equalsIgnoreCase(OMSConstants.ANIMATION_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			Fragment animFragment = OMSFactory.getInstance(
					.Animation, usid);
			animFragment.setArguments(bundle);

			if (isMain) {
				fm.beginTransaction().replace(mContainerId, animFragment)
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, animFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}

		}*//* else if (screenType.equalsIgnoreCase(OMSConstants.LIST_NAVIGATION_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);

			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SHOW_LOGOUT.getValue(), enableLogout);
			Fragment listFragment = OMSFactory.getInstance(TemplateType.ListNavigation,
					usid);
			listFragment.setArguments(bundle);
			if (isMain) {
				if (!(customScreenContainerId != OMSDefaultValues.NONE_DEF_CNT
						.getValue())
						&& OMSApplication.getInstance().isSplitView()
						&& OMSApplication.getInstance().isTablet()) {
					bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), true);
					fm.beginTransaction().replace(R.id.rootpanel, listFragment)
					.commit();
				} else {
					bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), false);
					fm.beginTransaction().replace(mContainerId, listFragment)
					.commit();
				}
			} else {
				bundle.putBoolean(OMSMessages.SPLIT_VIEW.getValue(), false);
				fm.beginTransaction().replace(mContainerId, listFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}
		} */else if (screenType
				.equalsIgnoreCase(OMSConstants.SPRINGBOARD_SCREEN_TYPE)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), navUsid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    mContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putBoolean(OMSMessages.SHOW_LOGOUT.getValue(), enableLogout);

			Fragment springBoradFragment = OMSFactory.getInstance(
					OMSFactory.TemplateType.Springboard, navUsid);
			springBoradFragment.setArguments(bundle);
			if (isMain) {
				fm.beginTransaction()
				.replace(mContainerId, springBoradFragment).commit();
			} else {
				fm.beginTransaction()
				.replace(mContainerId, springBoradFragment)
				.addToBackStack(OMSMessages.NULL_STRING.getValue())
				.commit();
			}

			// Load Launch Screen Template
		} else if (screenType.equalsIgnoreCase(OMSConstants.SPLASH_SCREEN)) {
			bundle = new Bundle();
			bundle.putInt(OMSMessages.SCREEN_ORDER.getValue(), screenOrder);
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), navUsid);
			bundle.putString(OMSMessages.TITLE.getValue(), headerTitle);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
                    customScreenContainerId);
			bundle.putBoolean(OMSMessages.SCREEN_MODE.getValue(), isFullScreen);
			bundle.putBoolean(OMSMessages.SHOW_LOGOUT.getValue(), enableLogout);
			Fragment splashScreenFragment = OMSFactory.getInstance(
					OMSFactory.TemplateType.SplashScreen, navUsid);
			splashScreenFragment.setArguments(bundle);

			if (isMain) {
				fm.beginTransaction().replace(mContainerId, splashScreenFragment, "launchscreen")
				.commit();
			} else {
				fm.beginTransaction().replace(mContainerId, splashScreenFragment, "launchscreen")
					.addToBackStack(OMSMessages.NULL_STRING.getValue())
			    .commit();
			}
		}
	}

	/**
	 * this method is used to load the template in the screen based on the
	 * screen type comes from navigation table
	 * 
	 * @param screenType
	 * @param navId
	 * @param usid
	 * @param screenOrder
	 * @param isMain
	 * @param filterColumnName
	 * @param filterColumnVal
	 * @param uiHeadingTitle
	 */
	public void loadTargetScreen(String screenType, int navId, String usid,
			int screenOrder, boolean isMain, String filterColumnName,
			String filterColumnVal, String uiHeadingTitle,
			int customScreenContainerId, int appId) {
		loadTargetScreen(screenType, navId, usid, screenOrder, true, isMain,
				filterColumnName, filterColumnVal, uiHeadingTitle,
				customScreenContainerId, appId);
	}

}

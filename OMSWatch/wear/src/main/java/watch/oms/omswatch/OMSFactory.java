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



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.fragments.ListScreenFragment;
import watch.oms.omswatch.fragments.MultiFormFragment;


public class OMSFactory {
	private static final String TAG = "OMSFactory";
	public static enum TemplateType {
		Animation, AppVision, BusinessLogic, ListConfigurator,ServerMapper,Calendar,CalendarDayView, Coverflow, CustomScreen, DatabaseDesign, Gallery, List, 
		ListNavigation,SplashScreen,Login, MapView, MediaViewer, MultiForm, MultiTab, SlidingMenu, Reports, ScreeDesignMultiForm, SectionList, Springboard, 
		TableLayout, Tiles, WebView, BarChart, LineChart, PieChart, ListItemConfigurator, SlidingmenuConfiguration, MultiFormConfigurator,ActionSheetItemConfiguration,
		Media,Scroller,GridView
	}

	public static Fragment getInstance(TemplateType screenType, String usid) {
		Fragment frag = null;

		switch (screenType) {
			//OMS_CORE_CASE_START

			case List:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = ListScreenFragment.newInstance();
				/*else
					frag = com.cognizant.oms.automatedtest.TestListScreen.newInstance();*/
				break;


			case ListNavigation:
				/*if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.listnavigation.ListNavigationScreenFragment.newInstance();
				else
					frag = com.cognizant.oms.automatedtest.TestNavigationListScreen.newInstance();*/
				break;

			case MultiForm:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = MultiFormFragment.newInstance("", "");
				/*else
					frag = com.cognizant.oms.automatedtest.TestMultiForm.newInstance();*/
				break;

		/*	case Animation:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
				   frag = com.cognizant.oms.uitemplate.animation.AnimationScreen.newInstance();
				else
				   frag = com.cognizant.oms.automatedtest.TestAnimation.newInstance();
				break;
			case Calendar:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.calendar.CalendarFragment.newInstance();
				else
					frag = com.cognizant.oms.automatedtest.TestCalendar.newInstance();
				break;
			case CalendarDayView:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.calendar.DayViewFragment.newInstance();
				else
					frag = com.cognizant.oms.automatedtest.TestDayViewCalendar.newInstance();
				break;
			case Coverflow:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.coverflow.CoverFlowFragment.newInstance();
				else
					frag = com.cognizant.oms.automatedtest.TestCoverFlow.newInstance();
				break;
			case CustomScreen:
				frag = com.cognizant.oms.uitemplate.customscreens.CustomScreenFragment1.newInstance();
				break;	
			case Gallery:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.gallery.GalleryScreenFragment.newInstance();
				else
					frag = com.cognizant.oms.automatedtest.TestGallery.newInstance();
				break;
			case List:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
				   frag = com.cognizant.oms.uitemplate.list.ListScreenFragment.newInstance();
			    else
				   frag = com.cognizant.oms.automatedtest.TestListScreen.newInstance();
				break;				
			case ListNavigation:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					 frag = com.cognizant.oms.uitemplate.listnavigation.ListNavigationScreenFragment.newInstance();
				 else
				     frag = com.cognizant.oms.automatedtest.TestNavigationListScreen.newInstance();
				break;
			case SplashScreen:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					 frag = com.cognizant.oms.uitemplate.launchscreen.LaunchScreen.newInstance();
				 else
				     frag = com.cognizant.oms.automatedtest.TestNavigationListScreen.newInstance();
				break;				
			case MapView:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.map.MapScreenFragment.newInstance(usid);
			     else
				   frag = com.cognizant.oms.automatedtest.TestMap.newInstance();
				break;
			case MediaViewer:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.mediaviewer.MediaViewerScreenFragment.newInstance();
				else
				   frag = com.cognizant.oms.automatedtest.TestMediaViewer.newInstance();
				break;
			case MultiForm:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
				   frag = com.cognizant.oms.uitemplate.multiform.MultiFormScreenFragment.newInstance();
				else
				   frag = com.cognizant.oms.automatedtest.TestMultiForm.newInstance();
				break;
			case Media:
				frag =  com.cognizant.oms.uitemplate.mediacontroller.MediaControllerFragment.newInstance();
				break;
			case Scroller:
				frag =  com.cognizant.oms.uitemplate.viewpager.ViewPagerFragment.newInstance();
				break;
			case GridView:
				frag =  com.cognizant.oms.uitemplate.gridview.GridViewFragment.newInstance();
				break;
			case SectionList:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
				   frag = com.cognizant.oms.configurator.uitemplate.sectionlist.SectionListScreenFragment.newInstance();
				else
				   frag = com.cognizant.oms.automatedtest.TestSectionList.newInstance();
				break;	
			case SlidingMenu:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED) {
					 frag = (Fragment)com.cognizant.oms.uitemplate.slidingmenu.NavigationDrawer.newInstance();
				}
				break;
			case Springboard:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.springboard.SpringBoardFragment.newInstance();
				else
				    frag = com.cognizant.oms.automatedtest.TestSpringBoard.newInstance();
				break;
			case TableLayout:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
				  frag = com.cognizant.oms.uitemplate.tablescreen.TableScreenFragment.newInstance();
				else
				  frag = com.cognizant.oms.automatedtest.TestTableView.newInstance();
				break;			
			case MultiTab:
			  if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
				  frag = com.cognizant.oms.uitemplate.multitab.ScrollableMultiTabFragment.newInstance();
			  else
				  frag = com.cognizant.oms.automatedtest.TestMultiTab.newInstance(); 
				break;
			case WebView:
			   if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
				 frag = com.cognizant.oms.uitemplate.webviewscreen.WebViewFragment.newInstance();
			   else
				  frag = com.cognizant.oms.automatedtest.TestWebView.newInstance(); 
				break;			
			case PieChart:
			    if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
			      frag = com.cognizant.oms.uitemplate.reports.PieChartFragment.newInstance();
			    else
				  frag = new com.cognizant.oms.automatedtest.TestPieChart(); 
				break;	
			case LineChart:
				 if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					 frag = com.cognizant.oms.uitemplate.reports.LineChartFragment.newInstance();
				 else
				    ; //frag = new com.cognizant.oms.automatedtest.TestLineChartWithTable(); 
				break;
			case BarChart:
				if(!OMSConstants.IS_AUTO_DEBUG_ENABLED)
					frag = com.cognizant.oms.uitemplate.reports.BarChartFragment.newInstance();
				else
					  ;
				    	//frag = new com.cognizant.oms.automatedtest.TestTabletBarChart(); 
				break;
			//OMS_CORE_CASE_END	
			//OMS_CONFIG_CASE_START
			case ScreeDesignMultiForm:
				frag = com.cognizant.oms.configurator.screendesign.ScreenDesignMultiFormItemFragment.newInstance();
				break;	
			case Tiles:
			   if(!OMSConstants.IS_AUTO_DEBUG_ENABLED){
			  //    frag = com.cognizant.oms.configurator.uitemplate.tiles.TilesScreenFragment.newInstance();
				   frag = com.cognizant.oms.configurator.uitemplate.sectionlist.SectionListScreenFragment.newInstance();

			   }  else
				  frag = com.cognizant.oms.automatedtest.TestTiles.newInstance();
				break;
			case AppVision:
				java.util.Hashtable<String, String> appsHashTable = new com.cognizant.oms.configurator.appvision.helper.AppvisionDBHelper()
				.getDataFromTable(
						com.cognizant.oms.core.helpers.OMSDatabaseConstants.APPS_TABLE_SCREEN_TABLE_NAME,
						OMSApplication.getInstance()
						.getGlobalFilterColumnVal(),
						com.cognizant.oms.core.helpers.OMSDatabaseConstants.UNIQUE_ROW_ID);
				com.cognizant.oms.core.appguard.utils.Log.i("OMSFactory", "Selected AppId:" + appsHashTable);
	
				OMSApplication.getInstance().setCurrentAppvisionAppId(Integer
						.valueOf(appsHashTable.get("appid")));
				com.cognizant.oms.configurator.appvision.AppVisionParentChildHolder
				.setCurrentAppvisionAppId(OMSApplication.getInstance().getCurrentAppvisionAppId());
				com.cognizant.oms.configurator.appvision.AppVisionDefaultStyles defaultStyling = null;
	
				//Style Guide 
				defaultStyling = new com.cognizant.oms.configurator.appvision.AppVisionDefaultStyles();
				defaultStyling.deleteStyleGuideSubList();
				defaultStyling.populateDefaultStyling(Integer
						.valueOf(appsHashTable.get("appid")));
				defaultStyling.populateStyleGuideSubList(Integer
						.valueOf(appsHashTable.get("appid")));
				frag = new com.cognizant.oms.configurator.appvision.AppVisionParentChildHolder();
				break;				
			case DatabaseDesign:
				frag = com.cognizant.oms.configurator.dbbrowser.DBBrowserFragment.newInstance(usid);
				break;	
			case BusinessLogic:
				frag = com.cognizant.oms.configurator.blgenerator.BLGeneratorFragment.newInstance(OMSApplication.getInstance().getCurrentAppvisionAppId());
				break;	
			case ServerMapper:
				frag = com.cognizant.oms.configurator.servermapper.ServerMapperFragment.newInstance();
				break;
			case ListConfigurator:
				frag = com.cognizant.oms.configurator.uitemplate.configuratorlist.ConfiguratorListScreenFragment.newInstance();
				break;

			case ListItemConfigurator:
				frag = com.cognizant.oms.configurator.screendesign.ScreenDesignListItemFragment.newInstance();
				break;
			case SlidingmenuConfiguration:
				frag = com.cognizant.oms.configurator.screendesign.ScreenDesignSlidingMenuConfigFragment.newInstance();
				break;
			case ActionSheetItemConfiguration:
				frag = com.cognizant.oms.configurator.screendesign.ScreenDesignActionSheetConfigFragment.newInstance();
				break;

			case MultiFormConfigurator:
				frag = com.cognizant.oms.configurator.screendesign.AppVisionMultiformFragment.newInstance();
				break;*/
			//OMS_CONFIG_CASE_END	
			default:
				Log.e(TAG, "Instance not found for template:"+screenType);
				frag = null;
				break;
		}
		return frag;
	}
/*
	public static void startTabBarWithScreenOrder(int screenOrder,
			String navusId,String listUsId, Activity context, int containerId, int appId,
			boolean enableLogin) {
		//OMS_TAB_BAR_START
		Object activity =null;
		
			//OMS_TAB_TESTING_START
			if (OMSConstants.IS_AUTO_DEBUG_ENABLED) 
			activity = com.cognizant.oms.automatedtest.TestTab.class;
			else
			//OMS_TAB_TESTING_END
			activity = com.cognizant.oms.uitemplate.tabbar.TabBarActivity.class;
			
		com.cognizant.oms.uitemplate.tabbar.helpers.TabConfigHelper.startTabBarWithScreenOrder(screenOrder, navusId,listUsId,
				context, containerId, appId, enableLogin,activity);
		//OMS_TAB_BAR_END
	}


	public static void launchDialog(Activity context, FragmentManager fm,
			int mContainerId,
			int screenOrder, boolean isMain, String usid,
			String headerTitle, boolean isFullScreen, int customScreenContainerId, int appId){
		//OMS_DIALOG_START
		com.cognizant.oms.uitemplate.dialog.dto.CustomDialogInputDTO inputData = new com.cognizant.oms.uitemplate.dialog.dto.CustomDialogInputDTO();
		inputData.screenOrder = screenOrder;
		inputData.ismain = isMain;
		inputData.uniqueid = usid;
		inputData.title = headerTitle;
		inputData.screenmode = isFullScreen;
		inputData.customScreenContainerId = customScreenContainerId;
		inputData.configAppId = appId;
		 com.cognizant.oms.uitemplate.dialog.CustomDialog customDialog;
		
		//OMS_DIALOG_TESTING_START
		if(OMSConstants.IS_AUTO_DEBUG_ENABLED)
		customDialog = new com.cognizant.oms.automatedtest.TestDialog(context, fm, mContainerId, inputData);		
		else
		//OMS_DIALOG_TESTING_END
		customDialog = new com.cognizant.oms.uitemplate.dialog.CustomDialog(context, fm, mContainerId, inputData);

		customDialog.launch();
		//OMS_DIALOG_END
	}
	*/
	/**
	 * @param context
	 * @param screenType
	 * @param appId
	 * @param usid
	 * @param mContainerId
	 * @param customScreenContainerId
	 */
/*	public static void enableNavigationDrawer(Activity context, String screenType, int appId, String usid, int mContainerId, int customScreenContainerId){
		//OMS_DRAWER_START

		boolean navigation = new NavigationHelper()
		.isNavigationDrawerAvailable(appId,usid);
		
		if (navigation){// || OMSApplication.getInstance().isUserLoggedIn()) {
			Bundle bundle = new Bundle();
			bundle.putString(OMSMessages.UNIQUE_ID.getValue(), usid);
			bundle.putInt(OMSMessages.CUSTOM_CONTAINERID.getValue(),
					customScreenContainerId);
			bundle.putInt(OMSMessages.CONFIGAPP_ID.getValue(), appId);
			
			com.cognizant.oms.uitemplate.slidingmenu.NavigationDrawer navigationDrawerScreenFragment = (com.cognizant.oms.uitemplate.slidingmenu.NavigationDrawer) OMSFactory.getInstance(
					TemplateType.SlidingMenu, usid);
			
			navigationDrawerScreenFragment.enableNavigationDrawer(context, bundle,
					mContainerId);
		}
		//OMS_DRAWER_END
	}
	*/
}
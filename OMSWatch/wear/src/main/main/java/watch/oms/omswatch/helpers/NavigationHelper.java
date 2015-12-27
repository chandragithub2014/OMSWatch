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
package watch.oms.omswatch.helpers;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

import watch.oms.omswatch.OMSDTO.NavigationItems;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;


/**
 * NavigationHelper - to retrieve application navigation detail
 * 
 * @author 245742
 * 
 */
public class NavigationHelper {

	private final String TAG = this.getClass().getSimpleName();
	//private final String NAVIGATION_DRAWER = "Navigation Drawer";
	
	public NavigationItems getLoginScreenForApp ()
	{
		NavigationItems navigationItems = null;
		String appId = OMSApplication.getInstance()
				.getAppId();
		Log.d(TAG, "method getLoginScreenForApp, appId:"
				+ appId);
		Cursor navigationScreenCursor = null;
		try {
			navigationScreenCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
					null,
					OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE + "= '"
							+ OMSConstants.MULTIFORM_SCREEN_TYPE
							+ "' and "
							+ OMSDatabaseConstants.NAVIGATION_SCREEN_NAME
							+ " like ('%" + OMSConstants.LOGIN_SCREEN_TYPE
							+ "%') and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + OMSApplication.getInstance().getAppId()
							+ "'" + " AND "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " = '0'", null, null, null, null);

			if (navigationScreenCursor != null && navigationScreenCursor.moveToFirst()) {
				do {
					navigationItems = new NavigationItems();
					navigationItems.screentype = navigationScreenCursor
							.getString(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenorder = navigationScreenCursor
							.getInt(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationScreenCursor
							.getString(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationScreenCursor
							.getInt(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationScreenCursor
							.getInt(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
				} while (navigationScreenCursor.moveToNext());
				navigationScreenCursor.close();
			}
			
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getLoginScreenForApp. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		finally{
			if(navigationScreenCursor!=null){
				navigationScreenCursor.close();
			}
		}
		return navigationItems;
	}

	/**
	 * Get screen details by passing parent screen id
	 * 
	 * @param parentScreenOrder
	 * @return ArrayList<NavigationItems>
	 */
	public ArrayList<NavigationItems> getScreenInfoByParentId(int parentScreenOrder, String appId) {

		Log.d(TAG, "method getScreenInfoByParentId, parentScreenOrder:"
				+ parentScreenOrder);

		NavigationItems navigationItems = null;
		ArrayList<NavigationItems> navigationdata = new ArrayList<NavigationItems>();
		Cursor navigationScreenCursor = null;
		try {
			navigationScreenCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
					null,
					OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER
							+ "="
							+ parentScreenOrder
							+ " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '"+appId+ "'" + "AND "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null, null);

			if (navigationScreenCursor.moveToFirst()) {
				do {
					navigationItems = new NavigationItems();
					navigationItems.screentype = navigationScreenCursor
							.getString(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenorder = navigationScreenCursor
							.getInt(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationScreenCursor
							.getString(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationScreenCursor
							.getInt(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationScreenCursor
							.getInt(navigationScreenCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
					navigationdata.add(navigationItems);
				} while (navigationScreenCursor.moveToNext());
			}
			navigationScreenCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getScreenInfoByParentId for input parameter parentScreenId["
							+ parentScreenOrder + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}finally{
			if(navigationScreenCursor!=null){
				navigationScreenCursor.close();
			}
		}
		return navigationdata;
	}

	/**
	 * Get child screen details by passing screen order
	 * 
	 * @param screenOrder
	 * @return ArrayList<NavigationItems>
	 */
	public ArrayList<NavigationItems> getChild(int screenOrder, int appId) {
		Cursor navigationCursor = null;
		ArrayList<NavigationItems> listScreendata = new ArrayList<NavigationItems>();
		try {
		String[] selectionArgs = new String[] {
				OMSConstants.MIN_CHILD_POSITION_FOR_LIST,
				OMSConstants.MAX_CHILD_POSITION_FOR_LIST };
		String selection = "  and position between ? and ?";
		String deleteSelection = " and isdelete = " + 0;
	
		if(OMSApplication.getInstance().getRoleID() >= 0){
			if(OMSApplication.getInstance().getRoleID() == 0)
				deleteSelection += " and  ( roleid in(0,'')  or roleid is null ) ";
			else
				deleteSelection += " and  ( roleid in(0,"+OMSApplication.getInstance().getRoleID()+")  or roleid is null ) ";

		}
		
		try {

			if (appId == -1) {
				navigationCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER + "="
								+ screenOrder + selection + deleteSelection,
						selectionArgs, null, null, null);
			} else {
				navigationCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER + "="
								+ screenOrder + selection + deleteSelection
								+ " and " + OMSDatabaseConstants.CONFIGDB_APPID
								+ " = '" + appId + "'", selectionArgs, null,
						null, null);
			}
			if (navigationCursor.moveToFirst()) {
				NavigationItems navigationItems = null;
				do {
					navigationItems = new NavigationItems();
					navigationItems.screentype = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenorder = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
					if(navigationItems.position > 100 && navigationItems.position < 105 )
						continue;
					listScreendata.add(navigationItems);
				} while (navigationCursor.moveToNext());
			}
			navigationCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChild() method   from Navigation table of ConfigDB."
							+ e.getMessage());
			e.printStackTrace();
		}
		}catch(Exception e){
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChild() method   from Navigation table of ConfigDB."
							+ e.getMessage());
			e.printStackTrace();
		}
		finally{
			if(navigationCursor!=null){
				navigationCursor.close();
			}
		}
		return listScreendata;
	}
	
	
	public ArrayList<NavigationItems> getAppBoxChild(int screenOrder, int appId,int position) {
		Cursor navigationCursor = null;
		String[] selectionArgs = new String[] {
				OMSConstants.MIN_CHILD_POSITION_FOR_LIST,
				OMSConstants.MAX_CHILD_POSITION_FOR_LIST };
		String selection = "  and position between ? and ?";
		String deleteSelection = " and isdelete = " + 0;
		ArrayList<NavigationItems> listScreendata = new ArrayList<NavigationItems>();
		
		
		if(OMSApplication.getInstance().getRoleID() >= 0){
			if(OMSApplication.getInstance().getRoleID() == 0)
				deleteSelection += " and  ( roleid in(0,'')  or roleid is null ) ";
			else
				deleteSelection += " and  ( roleid in(0,"+OMSApplication.getInstance().getRoleID()+")  or roleid is null ) ";

		}
		
		try {

			if (appId == -1) {
				navigationCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						"position ="
								+ position + deleteSelection,
						null, null, null, null);
			} else {
				navigationCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER + "="
								+ screenOrder + selection + deleteSelection
								+ " and " + OMSDatabaseConstants.CONFIGDB_APPID
								+ " = '" + appId + "'", selectionArgs, null,
						null, null);
			}
			if (navigationCursor.moveToFirst()) {
				NavigationItems navigationItems = null;
				do {
					navigationItems = new NavigationItems();
					navigationItems.screentype = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenorder = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
					if(navigationItems.position > 100 && navigationItems.position < 105 )
						continue;
					listScreendata.add(navigationItems);
				} while (navigationCursor.moveToNext());
			}
			navigationCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChild() method   from Navigation table of ConfigDB."
							+ e.getMessage());
			e.printStackTrace();
		}
		finally{
			if(navigationCursor!=null){
			navigationCursor.close();
			}
		}
		return listScreendata;
	}
	
	/**
	 * Get child screen details by passing usid order
	 * 
	 * @param
	 * @return ArrayList<NavigationItems>
	 */
	public ArrayList<NavigationItems> getChild(String childNavUsid, int appId) {
		Cursor navigationCursor = null;
		ArrayList<NavigationItems> listScreendata = new ArrayList<NavigationItems>();
		try {

			
				navigationCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID + " = '" + childNavUsid + "'",
						null, null, null, null);
			//}
			if (navigationCursor.moveToFirst()) {
				NavigationItems navigationItems = null;
				do {
					navigationItems = new NavigationItems();
					navigationItems.screentype = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenorder = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
					if(navigationItems.position > 100 && navigationItems.position < 105 )
						continue;
					listScreendata.add(navigationItems);
				} while (navigationCursor.moveToNext());
			}
			navigationCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChild() method   from Navigation table of ConfigDB."
							+ e.getMessage());
			e.printStackTrace();
		}finally{
			if(navigationCursor!=null){
			navigationCursor.close();
			}
		}
		return listScreendata;
	}

	/**
	 * get child screen detail of toolbar by passing navusid
	 * 
	 * @param navUsid
	 * @return ArrayList<NavigationItems>
	 */
	public ArrayList<NavigationItems> getChildForToolBar(String navUsid,
			int appId) {
		ArrayList<NavigationItems> listScreendata = new ArrayList<NavigationItems>();

		Cursor navigationCursor = null;
		Log.d(TAG, "getChildForToolBarr - navUsid::::" + navUsid);
		try {
			if (navUsid != null && !navUsid.trim().equals("")) {
				navigationCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID + "="
								+ "'" + navUsid + "'" + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
								+ appId + "'", null, null, null, null);

				if (navigationCursor.moveToFirst()) {
					do {
						NavigationItems tempList = new NavigationItems();
						tempList.screentype = navigationCursor
								.getString(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
						tempList.screenorder = navigationCursor
								.getInt(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
						tempList.uniqueId = navigationCursor
								.getString(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
						tempList.appId = navigationCursor
								.getInt(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
						tempList.position = navigationCursor
								.getInt(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
						listScreendata.add(tempList);
					} while (navigationCursor.moveToNext());
				}
				navigationCursor.close();
			}
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChildForToolBar() method   from Navigation table of ConfigDB."
							+ e.getMessage());
			e.printStackTrace();
		}
		return listScreendata;
	}
	

	/**
	 * get child screen detail of toolbar by passing navusid
	 * 
	 * @param navUsid
	 * @return ArrayList<NavigationItems>
	 */
	public ArrayList<NavigationItems> getChildForActionItem(String navUsid,
			int appId,int position) {
		ArrayList<NavigationItems> listScreendata = new ArrayList<NavigationItems>();

		Cursor navigationCursor = null;
		Log.d(TAG, "getChildForToolBarr - navUsid:::: " + navUsid+"   and Position ::"+position);
		try {
			if (navUsid != null && !navUsid.trim().equals("")) {
				navigationCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID + "="
								+ "'" + navUsid + "'" + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
								+ appId + "'" + " and " + OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION + " = '"+position+"' and isdelete <> 1" , null, null, null, null);

				if (navigationCursor.moveToFirst()) {
					do {
						NavigationItems tempList = new NavigationItems();
						tempList.screentype = navigationCursor
								.getString(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
						tempList.screenorder = navigationCursor
								.getInt(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
						tempList.uniqueId = navigationCursor
								.getString(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
						tempList.appId = navigationCursor
								.getInt(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
						tempList.position = navigationCursor
								.getInt(navigationCursor
										.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
						listScreendata.add(tempList);
					} while (navigationCursor.moveToNext());
				}
				navigationCursor.close();
			}
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChildForToolBar() method   from Navigation table of ConfigDB."
							+ e.getMessage());
			e.printStackTrace();
		}
		return listScreendata;
	}

	/**
	 * Get child screen details by passing screen order
	 * 
	 * @param screenOrder
	 * @return ArrayList<NavigationItems>
	 */
	public ArrayList<NavigationItems> getChildForCustomScreen(int screenOrder,
			int appId) {

		String deleteSelection = " and isdelete = " + 0;
		ArrayList<NavigationItems> customScreenChilddata = new ArrayList<NavigationItems>();
		try {

			Cursor navigationCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
					null,
					OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER + "="
							+ screenOrder + deleteSelection + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'", null, null, null,
					OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION + " ASC");

			if (navigationCursor.moveToFirst()) {
				NavigationItems navigationItems = null;
				do {
					navigationItems = new NavigationItems();
					navigationItems.screentype = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenorder = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
					customScreenChilddata.add(navigationItems);
				} while (navigationCursor.moveToNext());
			}
			navigationCursor.close();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChildForCustomScreen() method   from Navigation table of ConfigDB."
							+ e.getMessage());
			e.printStackTrace();
		}
		return customScreenChilddata;
	}

	/**
	 * Get child screen details by passing buttonId
	 * 
	 * @param buttonId
	 * @param appId
	 * @return ArrayList<NavigationItems>
	 */
	public ArrayList<NavigationItems> getChildForButton(int buttonId,
			int appId, int screenorder) {
		Cursor navigationCursor = null;
		String deleteSelection = " and isdelete = " + 0;
		ArrayList<NavigationItems> buttonLaunchScreendata = new ArrayList<NavigationItems>();

		try {
			navigationCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
					null,
					OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION + "="
							+ buttonId + deleteSelection + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "' and "
							+ OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER
							+ " = '" + screenorder + "'", null, null, null,
					null);
			if (navigationCursor.moveToFirst()) {
				NavigationItems navigationItems = null;
				do {
					navigationItems = new NavigationItems();
					navigationItems.screentype = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenorder = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
					buttonLaunchScreendata.add(navigationItems);
				} while (navigationCursor.moveToNext());
			}
			navigationCursor.close();

		} catch (Exception e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChildForButton() method   from Navigation table of ConfigDB."
							+ e.getMessage());
		}
		return buttonLaunchScreendata;
	}

	
	/**
	 * Get child screen details for Login Screen
	 * 
	 * @param appId
	 * @return ArrayList<NavigationItems>
	 */
	public NavigationItems getChildForLoginScreen(
			int appId, int screenorder) {
		Cursor navigationCursor = null;
		String deleteSelection = " and isdelete = " + 0;
		NavigationItems navigationItems = null;
		try {
			navigationCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
					null,
				      OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "' and "
							+ OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER
							+ " = '" + screenorder + "'"+deleteSelection, null, null, null,
					null);
			if (navigationCursor.moveToFirst()) {
				     navigationItems = new NavigationItems();
					navigationItems.screentype = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenorder = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
			}
			navigationCursor.close();

		} catch (Exception e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChildForButton() method   from Navigation table of ConfigDB."
							+ e.getMessage());
		}
		return navigationItems;
	}
	
	/**
	 * Get get Navigation Details By UsId
	 * 
	 * @param appId
	 * @return ArrayList<NavigationItems>
	 */
	public NavigationItems getNavigationDetailsByUsId(
			int appId,String usId) {
		Cursor navigationCursor = null;
		String deleteSelection = " and isdelete = " + 0;
		NavigationItems navigationItems = null;
		try {
			navigationCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
					null,
				      OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "' and "
							+ OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID
							+ " = '" + usId + "'"+deleteSelection, null, null, null,
					null);
			if (navigationCursor.moveToFirst()) {
				    navigationItems = new NavigationItems();
					navigationItems.screentype = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenName = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_NAME));
					navigationItems.screenorder = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
			}
			navigationCursor.close();

		} catch (Exception e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChildForButton() method   from Navigation table of ConfigDB."
							+ e.getMessage());
		}
		return navigationItems;
	}
	
	
	/**
	 * Get get Navigation Details By Screen Name
	 * 
	 * @param appId
	 * @return ArrayList<NavigationItems>
	 */
	public NavigationItems getNavigationDetailsByScreenName(
			String appId,String screenName) {
		Cursor navigationCursor = null;
		String deleteSelection = " and isdelete = " + 0;
		NavigationItems navigationItems = null;
		try {
			navigationCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
					null,
				      OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "' and "
							+ OMSDatabaseConstants.NAVIGATION_SCREEN_NAME
							+ " = '"+ screenName +"'"+deleteSelection, null, null, null,
					null);
			if (navigationCursor.moveToFirst()) {
				    navigationItems = new NavigationItems();
					navigationItems.screentype = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
					navigationItems.screenName = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_NAME));
					navigationItems.screenorder = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					navigationItems.uniqueId = navigationCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
					navigationItems.position = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));
					navigationItems.appId = navigationCursor
							.getInt(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.CONFIGDB_APPID));
			}
			navigationCursor.close();

		} catch (Exception e) {
			Log.e(TAG,
					"Exception occured while fetching  data for button in getChildForButton() method   from Navigation table of ConfigDB."
							+ e.getMessage());
		}
		return navigationItems;
	}
	/**
	 * @param appid
	 * @param usid
	 * @return boolean Checks whether the Parent for a Child screen is login or
	 *         not
	 */
	public boolean isParentLogin(int appid, String usid) {

		boolean isFromLogin = false;
		int parentScreenOrder = -1;
		String parentScreenType = null;
		try {

			Cursor navigationCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
					null,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "=" + "'" + usid + "'"
							+ " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appid + "'" + "and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ "='0'", null, null, null, null);
			if (navigationCursor.moveToFirst()) {
				parentScreenOrder = navigationCursor
						.getInt(navigationCursor
								.getColumnIndex(OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER));
			}
			navigationCursor.close();
			if (parentScreenOrder != -1) {

				Cursor navigationParentCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.CONFIGDB_APPID
								+ " = '"
								+ OMSApplication.getInstance()
										.getAppId() + "' and "
								+ OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER
								+ " = '" + parentScreenOrder + "'" + "and "
								+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
								+ "='0'", null, null, null, null);
				if (navigationParentCursor.moveToFirst()) {
					parentScreenType = navigationParentCursor
							.getString(navigationCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));
				}
				navigationParentCursor.close();
			}

			if (parentScreenType != null
					&& parentScreenType
							.equalsIgnoreCase(OMSConstants.LOGIN_SCREEN_TYPE)) {
				isFromLogin = true;
			} else {
				isFromLogin = false;
			}

		} catch (Exception e) {
			Log.e(TAG, "Error Occured in isParentLogin" + e.getMessage());
			e.printStackTrace();
		}
		return isFromLogin;

	}
	
	/**
	 * get List screen template detail
	 * 
	 * @param usId
	 * @param appId
	 * @return Cursor
	 */
	public boolean isSplitViewEnable(String usId, int appId) {
		Log.d(TAG, " method isSplitViewEnable, usid:" + usId);
		Cursor listCursor = null;
		boolean showSplitView = false;
		try {
			listCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.LIST_SCREEN_TABLE_NAME,
					null,
					OMSDatabaseConstants.LIST_SCREEN_NAVIGATION_UNIQUE_ID + "="
							+ "'" + usId + "'" + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'" + "and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ "='0'", null, null, null, null);
			
			if(listCursor.moveToNext())
				showSplitView = listCursor
				.getInt(listCursor
						.getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_SPLIT_VIEW)) != OMSDefaultValues.MIN_INDEX_INT
				.getValue();
			
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method isSplitViewEnable for input parameter usId["
							+ usId + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		finally{
			if(listCursor!=null){
				listCursor.close();
			}
		}
		return showSplitView;
	}
	
	
	/**
	 * get List screen template detail
	 * 
	 * @param usId
	 * @param appId
	 * @return Cursor
	 */
	public boolean isDetailAvailable(String usId, int appId) {
		Log.d(TAG, " method isDetailAvailable, usid:" + usId);
		Cursor listCursor = null;
		boolean showDetail = false;
		try {
			listCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.LIST_SCREEN_TABLE_NAME,
					null,
					OMSDatabaseConstants.LIST_SCREEN_NAVIGATION_UNIQUE_ID + "="
							+ "'" + usId + "'" + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'" + "and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ "='0'", null, null, null, null);
			
			if(listCursor.moveToNext())
				showDetail = listCursor
				.getInt(listCursor
						.getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_DETAIL)) != OMSDefaultValues.MIN_INDEX_INT
				.getValue();
			
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method isDetailAvailable for input parameter usId["
							+ usId + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return showDetail;
	}
	
	/**
	 * get navigation drawer screen template 
	 * @param appId
	 * @return boolean showNavigationDrawer
	 */
	public boolean isNavigationDrawerAvailable(int appId) {
		Log.d(TAG, " method isNavigationDrawerAvailable, appid:" + appId);
		Cursor navCursor = null;
		Cursor navDrawerCursor = null;
		boolean showNavigationDrawer = false;
		try {
			navCursor = OMSDBManager
					.getConfigDB()
					.query(OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
							null,
							OMSDatabaseConstants.CONFIGDB_APPID
									+ " = '" + appId + "' and "
									+ OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER
									+ " = '" + 3 + "' and "
									+ OMSDatabaseConstants.NAVIGATION_PARENT_SCREEN_ORDER
									+ " = '" + 2 + "' and "
									+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ "='0'", null, null, null, null);

			if (navCursor != null && navCursor.moveToNext())
				navDrawerCursor = OMSDBManager
						.getConfigDB()
						.query(OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
								null,
								OMSDatabaseConstants.CONFIGDB_APPID
										+ " = '" + appId + "' and "
										+ OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE
										+ " = '" + OMSConstants.SLIDING_MENU_SCREEN_TYPE + "' and "
										+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
										+ "='0'", null, null, null, null);
			if (navDrawerCursor != null && navDrawerCursor.getCount() > 0) {
				showNavigationDrawer = true;
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method isNavigationDrawerAvailable for input parameter appid["
							+ appId + "]. Error is:" + e.getMessage());
		}
		return showNavigationDrawer;
	}
	
	
	public String getTabBarUsid( int appId,String usId) {
		Log.d(TAG, " method getTabBarUsid, usid:" + usId);
		Cursor listCursor = null;
		String listUsId  = null;
		try {
			listCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.NAVIGATION_LIST_SCREEN_TABLE_NAME,
					null,
					OMSDatabaseConstants.LIST_SCREEN_NAVIGATION_UNIQUE_ID + "="
							+ "'" + usId + "'" + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'" + "and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ "='0'", null, null, null, null);
			
			if(listCursor != null && listCursor.moveToNext())
				listUsId = listCursor
				.getString(listCursor
						.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));
			
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method isSplitViewEnable for input parameter usId["
							+ usId + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return listUsId;
	}
	
	/**
	 * get navigation drawer screen template 
	 * @param appId
	 * @return boolean showNavigationDrawer
	 */
	public boolean isNavigationDrawerAvailable(int appId,String usId) {
		boolean isSlidingMenuEnabled = false;
		Cursor navDrawerCursor = null;
		try{
		
		
		navDrawerCursor = OMSDBManager
				.getConfigDB()
				.query(OMSDatabaseConstants.NAVIGATION_DRAWER_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.CONFIGDB_APPID
								+ " = '" + appId + "' and "
								+ OMSDatabaseConstants.NAVIGATION_DRAWER_SCREEN_CHILD_NAVIGATION_UNIQUE_ID
								+ "="
								+ "'" + usId + "'" + " and "
								+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
								+ "='0'", null, null, null, null);
		if (navDrawerCursor != null && navDrawerCursor.getCount() > 0) {
			isSlidingMenuEnabled = true;
		}
		}
		catch(Exception e){
			Log.d(TAG, "Exception in isNavigationDrawerAvailable"+e.getMessage());
		}
		finally{
			if(navDrawerCursor!=null){
				navDrawerCursor.close();
			}
		}
		return isSlidingMenuEnabled;
	}
	
}

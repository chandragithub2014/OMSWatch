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
package watch.oms.omswatch.toolbar;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.toolbar.dto.ToolBarConfigDTO;
import watch.oms.omswatch.toolbar.dto.ToolBarDTO;


/**
 * @author 280779 ToolBarHelper - this helper class is used to retrieve data for
 *         tool bar template from configDB schema
 */
public class ToolBarHelper {
	private final String TAG = this.getClass().getSimpleName();

	/**
	 * get tool bar data from Toolbar table
	 * 
	 * @param parentUniqueId
	 * @return ArrayList <ToolBarConfig>
	 */
	public ArrayList<ToolBarConfigDTO> getToolBarData(String parentUniqueId,
			int appId) {
		Cursor toolBarCursor = null;
		String buttonName, tabIcon;
		ToolBarConfigDTO tempConfig;
		ArrayList<ToolBarConfigDTO> toolConfigDetails = new ArrayList<ToolBarConfigDTO>();

		try {
			toolBarCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.TOOLBAR_TABLE_NAME,
					null,
					OMSDatabaseConstants.TOOLBAR_PARENT_USID + "=" + "'"
							+ parentUniqueId + "'" + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'" + "and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null, null);
			if (toolBarCursor.moveToFirst()) {
				do {
					buttonName = toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_ITEM_TITLE));
					tabIcon = toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_ITEM_ICON));

					tempConfig = new ToolBarConfigDTO();
					tempConfig.buttonTitle = buttonName;
					tempConfig.buttonicon = tabIcon;
					tempConfig.nav_usid = toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_LAUNCHER_NAVIGATION_UNIQUE_ID));
					tempConfig.parent_usid = toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_PARENT_USID));
					tempConfig.styleguidename = toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_STYLEGUIDE_NAME));

					// For SpringBoard, buttonStyleName is Toolbar Style Name
					tempConfig.buttonStyleName = tempConfig.styleguidename;

					tempConfig.position = toolBarCursor
							.getInt(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_ITEM_POSITION));
					tempConfig.blName=toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SAVE_EDIT_BL_NAME));
					
					tempConfig.showBadge = toolBarCursor
							.getInt(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_SHOW_BADGE)) != OMSDefaultValues.MIN_INDEX_INT
							.getValue();
					tempConfig.badgeTableName=toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_BADGE_DATA_TABLE));
					tempConfig.badgeColumn=toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_BADGE_DATA_COLUMN));
					
					tempConfig.badgeUseWhere = toolBarCursor
							.getInt(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_BADGE_USE_WHERE)) != OMSDefaultValues.MIN_INDEX_INT
							.getValue();
					tempConfig.badgeWhereColumn=toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_BADGE_WHERE_COLUMN));
					tempConfig.badgeWhereConst=toolBarCursor
							.getString(toolBarCursor
									.getColumnIndex(OMSDatabaseConstants.TOOLBAR_BADGE_WHERE_CONSTANT));
					
					toolConfigDetails.add(tempConfig);

				} while (toolBarCursor.moveToNext());
			}
			toolBarCursor.close();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getToolBarData for input parameter parentUniqueId["
							+ parentUniqueId + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getToolBarData for input parameter parentUniqueId["
							+ parentUniqueId + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return toolConfigDetails;
	}

	/**
	 * Get tool bar items detail from navigation table
	 * 
	 * @param usID
	 * @return ArrayList <ToolBarData>
	 */
	public ArrayList<ToolBarDTO> getToolBarItemsDetails(List<String> usID,
			int appId) {
		ArrayList<ToolBarDTO> toolBarDetails = new ArrayList<ToolBarDTO>();
		Cursor tabCursor = null;
		int screenOrder = 0;
		String screenType = null;
		ToolBarDTO tempToolData = null;
		try {
			for (int i = 0; i < usID.size(); i++) {
				tabCursor = OMSDBManager.getConfigDB().query(
						OMSDatabaseConstants.NAVIGATION_SCREEN_TABLE_NAME,
						null,
						OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID + " = "
								+ "'" + usID.get(i) + "' and  isdelete <> "
								+ "1" + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
								+ appId + "'", null, null, null, null);
				for (boolean hasItem = tabCursor.moveToFirst(); hasItem; hasItem = tabCursor
						.moveToNext()) {
					screenOrder = tabCursor
							.getInt(tabCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_ORDER));
					screenType = tabCursor
							.getString(tabCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_TYPE));

					tempToolData = new ToolBarDTO();
					tempToolData.screenOrder = screenOrder;
					tempToolData.screenType = screenType;
					tempToolData.nav_Id = tabCursor.getInt(tabCursor
							.getColumnIndex(OMSDatabaseConstants.KEY_ROWID));
					tempToolData.unique_Id = tabCursor
							.getString(tabCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_UNIQUE_ID));

					tempToolData.position = tabCursor
							.getInt(tabCursor
									.getColumnIndex(OMSDatabaseConstants.NAVIGATION_SCREEN_POSITION));

					toolBarDetails.add(tempToolData);
				}
				tabCursor.close();
			}
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getToolBarItemsDetails. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getToolBarItemsDetails. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		return toolBarDetails;
	}
	
	public int getBadgeCountFormTransDb(String tableName,
			String coulmnName,
			String whereClauseColumnName, String whereClauseConstant) {
		Cursor transDBDataCursor = null;
		String selection = null;
		int badgeCount = 0;
		try {
			if (whereClauseColumnName != null
					&& whereClauseColumnName.length() > 0) {
				if (whereClauseConstant != null
						&& whereClauseConstant.length() > 0) {
					selection = whereClauseColumnName + " = " + "'"
							+ whereClauseConstant + "'";
				} else {
					selection = whereClauseColumnName
							+ " = "
							+ "'"
							+ OMSApplication.getInstance()
									.getGlobalFilterColumnVal() + "'";
				}
			}

			if (selection != null) {
				selection = selection + " AND "
						+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
						+ " <> '1'";
			} else {
				selection = OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
						+ " <> '1'";
			}


			if(tableName != null){
				if(!TextUtils.isEmpty(coulmnName)){
					transDBDataCursor = TransDatabaseUtil.query(tableName,
							new String[] {"sum("+coulmnName+") as badgecount"}, selection, null, null, null, null);
				} else{
					transDBDataCursor = TransDatabaseUtil.query(tableName,
							new String[]{"count(*) as badgecount"}, selection, null, null, null, null);
				}
			}

		
			if (transDBDataCursor != null && transDBDataCursor.moveToFirst()) {
				do {
					badgeCount = transDBDataCursor
							.getInt(transDBDataCursor
									.getColumnIndex("badgecount"));

				} while (transDBDataCursor.moveToNext());
			}
			transDBDataCursor.close();
		}
		
		catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getBadgeCountFormTransDb  for input parameter tableName["
							+ tableName
							+ "], tempQuery["
							+ selection
							+ "]. Error is:" + e.getMessage());
		}

		return badgeCount;
	}
}
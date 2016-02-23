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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import watch.oms.omswatch.OMSDTO.NavigationItems;
import watch.oms.omswatch.OMSLoadScreenHelper;
import watch.oms.omswatch.R;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorActionCenter;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.helpers.NavigationHelper;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.toolbar.dto.ToolBarConfigDTO;
import watch.oms.omswatch.toolbar.dto.ToolBarDTO;


/**
 * Generates ToolBar dynamically and displays as footer in List or Form.
 * 
 * @author 280779
 * 
 */
@SuppressLint("UseSparseArrays")
public class ToolBarGenerator implements OMSReceiveListener {

	private final String TAG = this.getClass().getSimpleName();
	private boolean isMain = false;
	private Activity context = null;
	private List<NavigationItems> navScreenlist = null;
	private NavigationHelper navigationHelper = null;
	private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
	private OMSReceiveListener rListener = null;
	private List<String> transUsidList = null;
	// Styling
	//private OMSStyleGuideHelper styleGuideHelper;
	private Cursor titleBarStyleCursor;
	private int configAppId;
	private String filterColumnName;
	private String filterColumnValue;
	private ToolBarHelper tBarHelper;
    private TextView  toolBarItem;

	public ToolBarGenerator(Activity c, Object O, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList,
			int appId,TextView toolBarItem) {
		context = c;
		mContainerId = containerId;
		rListener = receiveListener;
		navScreenlist = new ArrayList<NavigationItems>();
		navigationHelper = new NavigationHelper();
		this.transUsidList = transUsidList;
	//	styleGuideHelper = new OMSStyleGuideHelper(context);
		configAppId = appId;
        this.toolBarItem = toolBarItem;
	}

	public ToolBarGenerator(Activity c, Object O, int containerId,
			OMSReceiveListener receiveListener,
			ArrayList<String> transUsidList, int appId,
			String filterColumnName, String filterColumnValue,TextView toolBarItem) {
		context = c;
		mContainerId = containerId;
		rListener = receiveListener;
		navScreenlist = new ArrayList<NavigationItems>();
		navigationHelper = new NavigationHelper();
		this.transUsidList = transUsidList;
//		styleGuideHelper = new OMSStyleGuideHelper(context);
		configAppId = appId;
		this.filterColumnName = filterColumnName;
		this.filterColumnValue = filterColumnValue;
		tBarHelper = new ToolBarHelper();
        this.toolBarItem = toolBarItem;
	}

	// /**
	// * Returns ToolBar View for given data List and UsId etc.,
	// *
	// * @param toolBarDataList
	// * @param vGroup
	// * @param toolBarTargerScreenlist
	// * @return viewGroup.
	// */
	// public ViewGroup getFooterLayout(final List<ToolBarConfig>
	// toolBarDataList,
	// ViewGroup vGroup, final List<ToolBarData> toolBarTargerScreenlist,
	// final String parentnavusid) {
	// List<Integer> toolBarButtonIds = new ArrayList<Integer>();
	// Log.d(TAG, "Parent Nav UsId: " + parentnavusid);
	// if (vGroup != null) {
	// vGroup.setVisibility(View.VISIBLE);
	// }
	// LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	// for (int i = 0; i < toolBarDataList.size(); i++) {
	// // Styling
	// String backgroundColor = Constants.EMPTY_STRING;
	// String buttonStyleName = Constants.EMPTY_STRING;
	// toolbarStyleCursor = styleGuideHelper
	// .getToolbarStyle(toolBarDataList.get(i).styleguidename);
	// if (toolbarStyleCursor.moveToFirst()) {
	// backgroundColor = toolbarStyleCursor
	// .getString(toolbarStyleCursor
	// .getColumnIndex(DatabaseConstants.TOOLBAR_STYLE_BACKGROUND_COLOR));
	// buttonStyleName = toolbarStyleCursor
	// .getString(toolbarStyleCursor
	// .getColumnIndex(DatabaseConstants.TOOLBAR_STYLE_BUTTON_STYLE_NAME));
	// }
	// toolbarStyleCursor.close();
	// if (!(Constants.EMPTY_STRING.equals(backgroundColor)))
	// vGroup.setBackgroundColor(Color.parseColor(backgroundColor));
	// toolBarBtn = styleGuideHelper.getStyledButton(new Button(context),
	// buttonStyleName);
	// // Styling ends
	// toolBarButtonIds.add(i + INTIAL_BUTTONID);
	// toolBarBtn.setId(i + INTIAL_BUTTONID);
	// toolBarBtn.setText(toolBarDataList.get(i).buttonTitle);
	// toolBarBtn.setCompoundDrawablesWithIntrinsicBounds(
	// android.R.drawable.ic_delete, 0, 0, 0);
	// toolBarBtn.setLayoutParams(lprams);
	// final int index = i;
	// if (vGroup != null)
	// vGroup.addView(toolBarBtn);
	// toolBarBtn.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (toolBarTargerScreenlist.size() > 0) {
	// if (!(index > toolBarTargerScreenlist.size())) {
	// navScreenlist = navigationHelper.getChildForToolBar(
	// toolBarTargerScreenlist.get(index).unique_Id,
	// configAppId);
	// }
	// }
	// new ActionCenter(context, mContainerId,
	// ToolBarGenerator.this, configAppId,
	// filterColumnName, filterColumnValue).doAction(
	// parentnavusid, toolBarBtn.getId(), transUsidList);
	// }
	// });
	// }
	// return vGroup;
	// }


	public void performToolBarAction(final List<ToolBarConfigDTO> toolBarDataList,
									  final List<ToolBarDTO> toolBarTargerScreenlist,
									 final String parentnavusid){
        final Hashtable<Integer, Integer> toolBarButtonIdList = new Hashtable<Integer, Integer>();

		if (toolBarDataList != null && toolBarDataList.size() > 0) {
			for (int i = 0; i < toolBarDataList.size(); i++) {
				String menuTitle = toolBarDataList.get(i).buttonTitle;
                toolBarItem.setText(menuTitle);
                final int index = i;
                toolBarButtonIdList.put(index, i + 101);

                if (!(index >= toolBarTargerScreenlist.size())) {
                    navScreenlist = navigationHelper
                            .getChildForToolBar(
                                    toolBarTargerScreenlist
                                            .get(index).unique_Id,
                                    configAppId);
                }
                String blName ="";
                blName = toolBarDataList.get(index).blName;
                if(!TextUtils.isEmpty(blName)){
                    new BLExecutorActionCenter(context, mContainerId,
                            ToolBarGenerator.this, configAppId, transUsidList).doBLAction(blName);

                }else{
                    receiveResult("");
                }

			}

		}

	}

	/*MenuItem menuItem;

	public Menu getActionMenu(final List<ToolBarConfigDTO> toolBarDataList,
			Menu menu, final List<ToolBarDTO> toolBarTargerScreenlist,
			final String parentnavusid) {
		final Hashtable<Integer, Integer> toolBarButtonIdList = new Hashtable<Integer, Integer>();
		ActionBar.LayoutParams menuParams = new ActionBar.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		String buttonStyleName = null;

		if (toolBarDataList != null && toolBarDataList.size() > 0) {


			// Iterate and populate Action Menu
			for (int i = 0; i < toolBarDataList.size(); i++) {

				// Get Menu Item
				String menuTitle = toolBarDataList.get(i).buttonTitle;
				menuItem = menu.add(0, toolBarDataList.get(i).position, toolBarDataList.size() - i,
						menuTitle);
				menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

				final int index = i;
				toolBarButtonIdList.put(index, i + 101);

				// Get Styled Menu Button
				if (buttonStyleName != null
						&& buttonStyleName.trim().length() > 0) {
					Button menuButton = null;
					try {
						menuButton = styleGuideHelper.getStyledButton(
								new Button(context), buttonStyleName);
						menuButton.setBackgroundColor(color.transparent);
					} catch (Exception e) {
						Log.e(TAG,
								"Exception occurred while retreiving button styling Details from ButtonStyle Table :"
										+ e.getMessage());
					}

					
					Cursor titleBarStyleCursor = styleGuideHelper.getTitleBarStyleDetails();
					String toolBarButtonFontStyle;
					if (titleBarStyleCursor != null && titleBarStyleCursor.moveToFirst()) {
						toolBarButtonFontStyle = titleBarStyleCursor
								.getString(titleBarStyleCursor
										.getColumnIndex("actionbuttonsstyle"));
						if(!TextUtils.isEmpty(toolBarButtonFontStyle)){
								try {
									if(toolBarButtonFontStyle.equalsIgnoreCase("dark halo")){
										menuButton.setTextColor(Color.parseColor("#0279fb"));
									} else{
										menuButton.setTextColor(Color.parseColor("#ffffff"));
									}
								} catch (Exception e) {
									Log.e(TAG, "Exception occurred while getting font data"
											+ e.getMessage());
								}
						}
						titleBarStyleCursor.close();
					} 
					
					menuButton.setLayoutParams(menuParams);

					// Set Title to Menu Button
					menuButton.setText(menuTitle);

					// Set Icon to Menu Button
					if (toolBarDataList.get(i).buttonicon != null
							&& !(toolBarDataList.get(i).buttonicon.trim()
									.equals(""))) {
						String tabIcon = toolBarDataList.get(i).buttonicon;
						if (tabIcon != null
								&& tabIcon.contains(OMSConstants.PERIOD)) {
							String result = tabIcon.substring(0,
									tabIcon.indexOf("."));
							tabIcon = result.toLowerCase(Locale.US);
							int imageResource = context.getResources()
									.getIdentifier(tabIcon, "drawable",
											context.getPackageName());
							if (imageResource != 0) {
								Drawable image = context.getResources()
										.getDrawable(imageResource);
								menuButton
										.setCompoundDrawablesWithIntrinsicBounds(
												image, null, null, null);
								menuButton.setText("");
							}
						}
					}

					menuButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Log.d(TAG, "Styled Menu Button Clicked");
							if (!(index >= toolBarTargerScreenlist.size())) {
								navScreenlist = navigationHelper
										.getChildForToolBar(
												toolBarTargerScreenlist
														.get(index).unique_Id,
												configAppId);
							}
							String blName ="";
							blName = toolBarDataList.get(index).blName;
							if(!TextUtils.isEmpty(blName)){
								new BLExecutorActionCenter(context, mContainerId,
										ToolBarGenerator.this, configAppId, transUsidList).doBLAction(blName);
									
							}else{
								receiveResult("");
							}

*//*							new ActionCenter(context, mContainerId,
									ToolBarGenerator.this, configAppId,
									filterColumnName, filterColumnValue)
									.doAction(parentnavusid,
											toolBarButtonIdList.get(index),
											transUsidList);*//*

						}
					});
					
					LinearLayout ly = new LinearLayout(context);
					ly.addView(menuButton);
					
					if(toolBarDataList.get(i).showBadge){
						BadgeView badge = new BadgeView(context, menuButton);
						int count = tBarHelper.getBadgeCountFormTransDb(toolBarDataList.get(i).badgeTableName,
								toolBarDataList.get(i).badgeColumn, 
								toolBarDataList.get(i).badgeWhereColumn,
								toolBarDataList.get(i).badgeWhereConst);
					
						badge.setText(String.valueOf(count));
						if(count > 0){
						  badge.show();
						}else{
						  badge.hide();
						}
					}
					menuItem.setActionView(ly);
				} else {
					// Set Action Menu Icon
					if (toolBarDataList.get(i).buttonicon != null
							&& !(toolBarDataList.get(i).buttonicon.trim()
									.equals(""))) {
						String tabIcon = toolBarDataList.get(i).buttonicon;
						if (tabIcon != null
								&& tabIcon.contains(OMSConstants.PERIOD)) {
							String result = tabIcon.substring(0,
									tabIcon.indexOf("."));
							tabIcon = result.toLowerCase(Locale.US);
							int imageResource = context.getResources()
									.getIdentifier(tabIcon, "drawable",
											context.getPackageName());
							if (imageResource != 0) {
								Drawable image = context.getResources()
										.getDrawable(imageResource);
								menuItem.setIcon(image);
								menuItem.setTitle("");
							}
						}
					}
				}

				menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Log.d(TAG,
								"ButtonId:::" + toolBarButtonIdList.get(index));
						Log.d(TAG, "unique_id::footerlayout"
								+ toolBarTargerScreenlist.size());
						if (toolBarTargerScreenlist.size() > 0) {
							Log.d(TAG, "Index::::" + index
									+ "  toolBarTrageSize::::"
									+ toolBarTargerScreenlist.size());

							for (int i = 0; i < toolBarTargerScreenlist.size(); i++) {
								if (toolBarTargerScreenlist.get(i).position == item
										.getItemId()) {
									navScreenlist = navigationHelper
											.getChildForToolBar(
													toolBarTargerScreenlist
															.get(i).unique_Id,
													configAppId);
									toolBarChilds.put(item.getItemId(),
											navScreenlist);
								}
							}
						}

						
						String blName ="";
						blName = toolBarDataList.get(index).blName;
						if(!TextUtils.isEmpty(blName)){
							new BLExecutorActionCenter(context, mContainerId,
									ToolBarGenerator.this, configAppId, transUsidList).doBLAction(blName);
								
						}else{
							receiveResult("");
						}
						
						return false;
					}
				});

			}
		}

		return menu;

	}*/

	private Map<Integer, List<NavigationItems>> toolBarChilds = new HashMap<Integer, List<NavigationItems>>();

	public Map<Integer, List<NavigationItems>> getToolBarChilds() {
		return toolBarChilds;
	}

	public void setToolBarChilds(
			Map<Integer, List<NavigationItems>> toolBarChilds) {
		this.toolBarChilds = toolBarChilds;
	}

	private void loadTargetScreenForToolBarItem(
			List<NavigationItems> navScreenlist) {
		if (!navScreenlist.isEmpty()) {
				new OMSLoadScreenHelper(context, mContainerId)
						.loadTargetScreen(navScreenlist.get(0).screentype,
								navScreenlist.get(0).parent_id,
								navScreenlist.get(0).uniqueId,
								navScreenlist.get(0).screenorder, isMain, null,
								null, OMSConstants.EMPTY_STRING,
								OMSDefaultValues.NONE_DEF_CNT.getValue(),
								navScreenlist.get(0).appId,false);
		}
	}

	@Override
	public void receiveResult(String result) {
	    loadTargetScreenForToolBarItem(navScreenlist);
		rListener.receiveResult(OMSMessages.TOOLBAR_REFRESH.getValue());
	}
}

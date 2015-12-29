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
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import android.content.ContentValues;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.graphics.Color;
    import android.graphics.Paint.Style;
    import android.graphics.drawable.ColorDrawable;
    import android.graphics.drawable.Drawable;
    import android.graphics.drawable.LayerDrawable;
    import android.graphics.drawable.ShapeDrawable;
    import android.graphics.drawable.StateListDrawable;
    import android.graphics.drawable.shapes.RectShape;
    import android.text.TextUtils;
    import android.util.Log;

    import watch.oms.omswatch.OMSDTO.ListScreenItemsDTO;
    import watch.oms.omswatch.WatchDB.OMSDBManager;
    import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
    import watch.oms.omswatch.constants.OMSConstants;
    import watch.oms.omswatch.constants.OMSDatabaseConstants;
    import watch.oms.omswatch.constants.OMSDefaultValues;
    import watch.oms.omswatch.constants.OMSMessages;


    /**
     * @author 280779 ListHelper to fetch data for List screen/Spring board
     *         templates from config db schema
     */

    public class ListHelper {

        private static final String IS_DELETE = "isdelete";
        private static final String SPACE = " ";
        private final String TAG = this.getClass().getSimpleName();

        /**
         * get List screen template detail
         *
         * @param usId
         * @return Cursor
         */
        public Cursor getListScreenDetails(String usId, int appId) {
            Log.d(TAG, " method getListScreenDetails, usid:" + usId);
            Cursor listCursor = null;
            try {
                listCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.LIST_SCREEN_TABLE_NAME,
                        null,
                        OMSDatabaseConstants.LIST_SCREEN_NAVIGATION_UNIQUE_ID + "="
                                + "'" + usId + "'" + SPACE
                                + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                                + appId + "'" + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + "='0'", null, null, null, null);
            } catch (Exception e) {
                Log.e(TAG,
                        "Error occurred in method getListScreenDetails for input parameter usId["
                                + usId + "]. Error is:" + e.getMessage());

            }
            return listCursor;
        }

        /*public List<ListItemDTO> getListScreenItemsHeader(String listUsid,
                int configDBAppId) {
            List<ListItemDTO> listItems = new ArrayList<ListItemDTO>();
            Cursor listScreenItemsCursor = null;
            ListItemDTO itemDTO;
            try {
                listScreenItemsCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.LIST_SCREEN_ITEMS_TABLE,
                        null,
                        OMSDatabaseConstants.SORTING_DATA_LIST_UNIQUE_ID + "="
                                + "'" + listUsid + "'" + SPACE
                                + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                                + configDBAppId + "'" + OMSMessages.AND.getValue()
                                + SPACE
                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + "<> '1' and  itemrowid = -5 ", null, null, null, "itemrowid");
                if (listScreenItemsCursor.moveToFirst()) {
                    do {
                        for (int i = 0; i < 4; i++) {
                            itemDTO = new ListItemDTO();
                            if (!TextUtils
                                    .isEmpty(listScreenItemsCursor.getString(listScreenItemsCursor
                                            .getColumnIndex(OMSMessages.COLUMN
                                                    .getValue()
                                                    + (i + 1)
                                                    + "content")))) {
                                itemDTO.setItemType(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + (i + 1)
                                                        + OMSMessages.TYPE
                                                        .getValue())));
                                itemDTO.setItemContent(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "content")));
                                itemDTO.setItemAlignment(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "alignment")));
                                itemDTO.setItemStyle(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "style")));
                                itemDTO.setItemID(Integer.parseInt(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "id"))));
                                itemDTO.setColumnID(i);
                                itemDTO.setRowID(listScreenItemsCursor
                                        .getPosition());
                                itemDTO.setItemColumnSpan(Integer.parseInt(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "width")).replace('%', ' ').trim()));
                                itemDTO.setItemRowSpan(Integer.parseInt(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "height"))));
                                itemDTO.setItemButtonBL(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex("column" + +(i + 1)
                                                        + "blname")));
                                Log.d(TAG+" List Header ",itemDTO.toString());
                                listItems.add(itemDTO);
                            }
                        }
                    } while (listScreenItemsCursor.moveToNext());
                }
                listScreenItemsCursor.close();
            } catch (IllegalArgumentException e) {
                Log.e(TAG,
                        "Exception occured while fetching  data  from ListScreenItems table of ConfigDB."
                                + e.getMessage());

            }

            return listItems;
        }

        public List<ListItemDTO> getListScreenItemsList(String listUsid,
                int configDBAppId) {
            List<ListItemDTO> listItems = new ArrayList<ListItemDTO>();
            Cursor listScreenItemsCursor = null;
            ListItemDTO itemDTO;
            try {
                listScreenItemsCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.LIST_SCREEN_ITEMS_TABLE,
                        null,
                        OMSDatabaseConstants.SORTING_DATA_LIST_UNIQUE_ID + "="
                                + "'" + listUsid + "'" + SPACE
                                + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                                + configDBAppId + "'" + OMSMessages.AND.getValue()
                                + SPACE
                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + "<> '1' and  itemrowid <> -5 ", null, null, null, "itemrowid");
                if (listScreenItemsCursor.moveToFirst()) {
                    do {
                        for (int i = 0; i < 4; i++) {
                            itemDTO = new ListItemDTO();
                            if (!TextUtils
                                    .isEmpty(listScreenItemsCursor.getString(listScreenItemsCursor
                                            .getColumnIndex(OMSMessages.COLUMN
                                                    .getValue()
                                                    + (i + 1)
                                                    + OMSMessages.TYPE.getValue())))) {
                                itemDTO.setItemType(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + (i + 1)
                                                        + OMSMessages.TYPE
                                                        .getValue())));
                                itemDTO.setItemContent(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "content")));
                                itemDTO.setItemAlignment(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "alignment")));
                                itemDTO.setItemStyle(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "style")));
                                itemDTO.setItemID(Integer.parseInt(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "id"))));
                                itemDTO.setColumnID(i);
                                itemDTO.setRowID(listScreenItemsCursor
                                        .getPosition());
                                itemDTO.setItemColumnSpan(Integer.parseInt(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "width"))));
                                itemDTO.setItemRowSpan(Integer.parseInt(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + +(i + 1)
                                                        + "height"))));
                                itemDTO.setItemButtonBL(listScreenItemsCursor
                                        .getString(listScreenItemsCursor
                                                .getColumnIndex("column" + +(i + 1)
                                                        + "blname")));
                                Log.d(TAG,itemDTO.toString());
                                listItems.add(itemDTO);
                            }
                        }
                    } while (listScreenItemsCursor.moveToNext());
                }
                listScreenItemsCursor.close();
            } catch (IllegalArgumentException e) {
                Log.e(TAG,
                        "Exception occured while fetching  data  from ListScreenItems table of ConfigDB."
                                + e.getMessage());

            }

            return listItems;
        }*/

        /**
         * Method to state a layered drawable for List
         *
         * @param backgroundColor
         * @param selectedColor
         * @param pressedColor
         * @return StateListDrawable
         */
        public StateListDrawable getStateListDrawableForList(
                String backgroundColor, String selectedColor, String pressedColor) {
            StateListDrawable stateList = new StateListDrawable();
            Drawable state_activated = null;
            Drawable backgroundDrawable = getBackgroundDrawableForList(backgroundColor);
            Drawable selectedDrawable = getSelectedBackgroundDrawableForList(
                    backgroundColor, selectedColor);
            Drawable pressedDrawable = getPressedDrawableForList(pressedColor);
            Drawable pressedAndSelectedDrawable = getSelectedBackgroundDrawableForPressedStateList(
                    pressedColor, selectedColor);
            if(pressedColor != null)
            state_activated = new ColorDrawable(Color.parseColor(pressedColor));//0xff357EC7
            else
                state_activated = new ColorDrawable(0xff357EC7);
            ///////////////
            // Activated State
            stateList.addState(new int[] { -android.R.attr.state_focused,
                    android.R.attr.state_activated, -android.R.attr.state_pressed },
                    state_activated);
            // Activated State
            stateList.addState(new int[] { android.R.attr.state_focused,
                    android.R.attr.state_activated, -android.R.attr.state_pressed },
                    state_activated);

            // Button Not Pressed && Non-Focussed States
            // Enabled
            stateList.addState(
                    new int[] { -android.R.attr.state_focused,
                            -android.R.attr.state_selected,
                            -android.R.attr.state_pressed }, backgroundDrawable);

            // Selected
            stateList.addState(new int[] { -android.R.attr.state_focused,
                    android.R.attr.state_selected, -android.R.attr.state_pressed },
                    selectedDrawable);

            // Button Not Pressed && Focussed States
            stateList.addState(
                    new int[] { android.R.attr.state_focused,
                            -android.R.attr.state_selected,
                            -android.R.attr.state_pressed }, backgroundDrawable);

            // Selected
            stateList.addState(new int[] { android.R.attr.state_focused,
                    android.R.attr.state_selected, -android.R.attr.state_pressed },
                    selectedDrawable);

            // When Button is Pressed and Not Focussed
            stateList.addState(new int[] { -android.R.attr.state_focused,
                    -android.R.attr.state_selected, android.R.attr.state_pressed },
                    pressedDrawable);
            stateList.addState(new int[] { -android.R.attr.state_focused,
                    android.R.attr.state_selected, android.R.attr.state_pressed },
                    pressedAndSelectedDrawable);

            // When Button is Pressed and Focussed
            stateList.addState(new int[] { android.R.attr.state_focused,
                    -android.R.attr.state_selected, android.R.attr.state_pressed },
                    pressedDrawable);
            stateList.addState(new int[] { android.R.attr.state_focused,
                    android.R.attr.state_selected, android.R.attr.state_pressed },
                    pressedAndSelectedDrawable);

            return stateList;
        }

        /**
         * Method creates a layered drawable for selected List
         *
         * @param backgroundColor
         * @param selectedColor
         *            - List indicator color
         * @return Drawable - Selected Tab List
         */
        private Drawable getSelectedBackgroundDrawableForList(
                String backgroundColor, String selectedColor) {

            if (TextUtils.isEmpty(backgroundColor)) {
                backgroundColor = OMSConstants.BACKGROUND_TAB_COLOR;
            }
            if (TextUtils.isEmpty(selectedColor)) {
                selectedColor = OMSConstants.SELECTED_TAB_COLOR;
            }

            ShapeDrawable shape1 = new ShapeDrawable(new RectShape());
            shape1.getPaint().setColor(Color.parseColor(selectedColor));
            shape1.getPaint().setStyle(Style.FILL);

            ShapeDrawable shape2 = new ShapeDrawable(new RectShape());
            shape2.getPaint().setColor(Color.parseColor(backgroundColor));
            shape2.getPaint().setStyle(Style.FILL);

            Drawable[] background = new Drawable[2];
            background[0] = shape1;
            background[1] = shape2;

            LayerDrawable selectedTabBackground = new LayerDrawable(background);
            selectedTabBackground.setLayerInset(0, 0, 0, 0, 0);
            selectedTabBackground.setLayerInset(1, 0, 0, 0, 10);

            return selectedTabBackground;
        }

        /**
         * Method creates a shape drawable for List Pressed State
         *
         * @param pressedColor
         * @return Drawable
         */
        private Drawable getPressedDrawableForList(String pressedColor) {
            ShapeDrawable tabPressedDrawalbe = new ShapeDrawable(new RectShape());
            if (TextUtils.isEmpty(pressedColor)) {
                pressedColor = OMSConstants.PRESSED_TAB_COLOR;
            }
            tabPressedDrawalbe.getPaint().setColor(Color.parseColor(pressedColor));
            tabPressedDrawalbe.getPaint().setStyle(Style.FILL);
            return tabPressedDrawalbe;
        }

        /**
         * Method creates a layered drawable for pressed & selected List
         *
         * @param pressedColor
         * @param selectedColor
         *            - List indicator color
         * @return Drawable - Selected Tab Drawable
         */
        private Drawable getSelectedBackgroundDrawableForPressedStateList(
                String pressedColor, String selectedColor) {

            if (TextUtils.isEmpty(selectedColor)) {
                selectedColor = OMSConstants.SELECTED_TAB_COLOR;
            }
            if (TextUtils.isEmpty(pressedColor)) {
                pressedColor = OMSConstants.PRESSED_TAB_COLOR;
            }

            ShapeDrawable shape1 = new ShapeDrawable(new RectShape());
            shape1.getPaint().setColor(Color.parseColor(selectedColor));
            shape1.getPaint().setStyle(Style.FILL);

            ShapeDrawable shape2 = new ShapeDrawable(new RectShape());
            shape2.getPaint().setColor(Color.parseColor(pressedColor));
            shape2.getPaint().setStyle(Style.FILL);

            Drawable[] background = new Drawable[2];
            background[0] = shape1;
            background[1] = shape2;

            LayerDrawable selectedTabBackground = new LayerDrawable(background);
            selectedTabBackground.setLayerInset(0, 0, 0, 0, 0);
            selectedTabBackground.setLayerInset(1, 0, 0, 0, 10);

            return selectedTabBackground;
        }

        /**
         * Method creates a shape drawable for List Background. This drawable can be
         * used to set the background for ActionBar Stacked List Bar
         *
         * @param backgroundColor
         * @return Drawable
         */
        public Drawable getBackgroundDrawableForList(String backgroundColor) {
            ShapeDrawable tabBackgroundDrawalbe = new ShapeDrawable(new RectShape());
            if (TextUtils.isEmpty(backgroundColor)) {
                backgroundColor = OMSConstants.BACKGROUND_TAB_COLOR;
            }
            tabBackgroundDrawalbe.getPaint().setColor(
                    Color.parseColor(backgroundColor));
            tabBackgroundDrawalbe.getPaint().setStyle(Style.FILL);

            return tabBackgroundDrawalbe;
        }

        /**
         * get sorting detail
         *
         * @param listusId
         * @param appId
         * @return Cursor
         */
        public Cursor getSortingDetails(String listusId, int appId) {
            Log.d(TAG, " method getSortingDetails, listusId:" + listusId);
            Cursor sortingDataCursor = null;
            try {
                sortingDataCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.SORTING_CONFIG_TABLE_NAME,
                        null,
                        OMSDatabaseConstants.SORTING_DATA_LIST_UNIQUE_ID + "="
                                + "'" + listusId + "'" + SPACE
                                + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                                + appId + "'" + SPACE + OMSMessages.AND.getValue()
                                + SPACE
                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + "='0'", null, null, null, null);
            } catch (Exception e) {
                Log.e(TAG,
                        "Error occurred in method getSortingDetails for input parameter listusId["
                                + listusId + "]. Error is:" + e.getMessage());

            }
            return sortingDataCursor;
        }

        /**
         * get Springboard header detail
         *
         * @param appId
         * @return Cursor
         */
        public Cursor getSpringBoardHeaderDetail(int appId) {
            Log.d(TAG, " method getListScreenDetails, usid:" + appId);
            Cursor sbCursor = null;
            try {
                sbCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.APPS_TABLE_SCREEN_TABLE_NAME, null,
                        OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId + "'",
                        null, null, null, null);
            } catch (Exception e) {
                Log.e(TAG,
                        "Error occurred in method getListScreenDetails for input parameter appId["
                                + appId + "]. Error is:" + e.getMessage());

            }
            return sbCursor;
        }

        /**
         * Delete list item from database
         *
         * @return int
         */
        public int deleteSelectedListItem(String transTableName, String rowId) {
            long updateRow = OMSDefaultValues.NONE_DEF_CNT.getValue();

            ContentValues updatedValues = new ContentValues();
            updatedValues.put(IS_DELETE, 1);
            try {
                updateRow = TransDatabaseUtil.update(transTableName,
                        updatedValues, OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
                        new String[]{rowId});
            } catch (Exception e) {
                Log.e(TAG,
                        "Error occurred in method updateTransData for input parameter rowId["
                                + rowId + "], tableName[" + transTableName
                                + "]. Error is:" + e.getMessage());

            }
            return (int) updateRow;
        }

        /**
         * Update list item status from database
         *
         * @return int
         */
        public int updateSelectedListItem(String transTableName, String rowId, String readColumn) {
            long updateRow = OMSDefaultValues.NONE_DEF_CNT.getValue();

            ContentValues updatedValues = new ContentValues();
            updatedValues.put(readColumn, 1);
            try {
                updateRow = TransDatabaseUtil.update(transTableName,
                        updatedValues, OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
                        new String[] { rowId });
            } catch (Exception e) {
                Log.e(TAG,
                        "Error occurred in method updateTransData for input parameter rowId["
                                + rowId + "], tableName[" + transTableName
                                + "]. Error is:" + e.getMessage());

            }
            return (int) updateRow;
        }

        /**
         * Update list item value
         * @param transTableName
         * @param rowId
         * @param columnName
         * @param columnValue
         * @return
         */
        public int updateSelectedListItemValue(String transTableName, String rowId, String columnName, String columnValue) {
            long updateRow = OMSDefaultValues.NONE_DEF_CNT.getValue();

            ContentValues updatedValues = new ContentValues();
            updatedValues.put(columnName, columnValue);
            try {
                updateRow = TransDatabaseUtil.update(transTableName,
                        updatedValues, OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
                        new String[] { rowId });
            } catch (Exception e) {
                Log.e(TAG,
                        "Error occurred in method updateSelectedListItemValue for input parameter rowId["
                                + rowId + "], tableName[" + transTableName
                                + "]. Error is:" + e.getMessage());

            }
            return (int) updateRow;
        }

        /** To get the title for child screen.
         * @param usID
         * @param appId of currentusing
         * @return title
         */
        public String getChildScreenTitle(String usID, int appId) throws Exception{
            Log.d(TAG, " method getChildScreenTitle, usID:" + usID);
            Cursor listCursor = null;
            String headerTitle = "";
            try {
                listCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.NAVIGATION_LIST_SCREEN_ITEMS, null,
                        buildSelectionCondition(OMSDatabaseConstants.NAVIGATION_LIST_CHILD_NAVUSID, usID, appId),
                        null, null, null, null);
                if (listCursor.moveToFirst()) {
                    headerTitle = listCursor
                            .getString(listCursor
                                    .getColumnIndex(OMSDatabaseConstants.NAVIGATION_LIST_SCREEN_ITEMS_SECONDARY_TEXT));
                }
            } catch (SQLException e) {
                Log.e(TAG, "Exception occurred while retreiving "
                        + "ListNavigationScreen child screen title "
                        + "from ListNavigationScreenItems Table :" + e.getMessage());
                throw new Exception(e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Exception occurred while retreiving "
                        + "ListNavigationScreen child screen title  "
                        + "from ListNavigationScreenItems Table :" + e.getMessage());
                throw new Exception(e.getMessage());
            }
            finally{
                if(listCursor!=null){
                    listCursor.close();
                }
            }
            return headerTitle;
        }


        private String buildSelectionCondition(String columnName, String usId, int appId) {
            StringBuffer selection = new StringBuffer();
            selection.append(columnName);
            selection.append("= '" + usId + "' and ");
            selection.append(OMSDatabaseConstants.CONFIGDB_APPID);
            selection.append(" = '" + appId + "' and ");
            selection.append(OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE);
            selection.append("= '" + OMSDatabaseConstants.IS_DELETE_CONSTANT + "'");

            return selection.toString();
        }

        /** To get child screen position.
         * @param usID of listnavigation
         * @param appId of currentusing
         * @return position of child
         */
        public int getChildPosition(String usID, int appId) throws Exception{
            Log.d(TAG, " method getChildPosition, usid:" + usID);
            Cursor listCursor = null;

            int childPosition = OMSDefaultValues.NONE_DEF_CNT.getValue();
            try {
                listCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.NAVIGATION_LIST_SCREEN_ITEMS, null,
                        buildSelectionCondition(OMSDatabaseConstants.NAVIGATION_LIST_CHILD_NAVUSID, usID, appId)
                         , null, null, null, null);
                if (listCursor.moveToFirst()) {
                    childPosition = listCursor.getInt(listCursor
                                    .getColumnIndex(OMSDatabaseConstants.NAVIGATION_LIST_SCREEN_ITEMS_POSITION));
                }
            } catch (SQLException e) {
                Log.e(TAG,
                        "Exception occurred while retreiving ListNavigationScreen "
                                + "child screen position from "
                                + "ListNavigationScreenItems Table :"
                                + e.getMessage());
                throw new Exception(e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Exception occurred while retreiving "
                        + "ListNavigationScreen child screen position "
                        + "from ListNavigationScreenItems Table :" + e.getMessage());
                throw new Exception(e.getMessage());
            }
            return childPosition;
        }





    /*
        public AdvSearchDTO getAdvSearchDetails(String navUsId, int appId) {
            Log.d(TAG, " method getAdvSearchDetails,navUsId:" + navUsId);
            Cursor searchColCursor = null;
            Cursor searchColValuesCursor = null;
            AdvSearchDTO advSearchDTO = new AdvSearchDTO();
            try {
                searchColCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.LIST_ADV_SEARCH_OPTIONS_CONFIG_TABLE_NAME,
                        null,
                        OMSDatabaseConstants.LIST_SCREEN_NAVIGATION_UNIQUE_ID + "="
                                + "'" + navUsId + "'" + SPACE
                                + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                                + appId + "'" + SPACE + OMSMessages.AND.getValue()
                                + SPACE
                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + "='0'", null, null, null, null);

                if (searchColCursor.moveToFirst()) {
                            if (!TextUtils
                                    .isEmpty(searchColCursor.getString(searchColCursor
                                            .getColumnIndex("searchcolname")))) {
                                advSearchDTO.optionSearchColName = searchColCursor.getString(searchColCursor
                                        .getColumnIndex("searchcolname"));
                                advSearchDTO.colDisplayName = searchColCursor.getString(searchColCursor
                                        .getColumnIndex("searchcollabel"));
                                advSearchDTO.optionsUsId = searchColCursor.getString(searchColCursor
                                        .getColumnIndex("usid"));
                                advSearchDTO.optionDataTableName = searchColCursor.getString(searchColCursor
                                        .getColumnIndex("datatablename"));

                                searchColValuesCursor = OMSDBManager.getConfigDB().query(
                                        OMSDatabaseConstants.LIST_ADV_SEARCH_VALUES_CONFIG_TABLE_NAME,
                                        null,
                                        "parentusid="
                                                + "'" + advSearchDTO.optionsUsId + "'" + SPACE
                                                + OMSMessages.AND.getValue() + SPACE
                                                + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                                                + appId + "'" + SPACE + OMSMessages.AND.getValue()
                                                + SPACE
                                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                                + "='0'"
                                                 , null, null, null, null);
                                if (searchColValuesCursor.moveToFirst()) {
                                    if (!TextUtils
                                            .isEmpty(searchColValuesCursor.getString(searchColValuesCursor
                                                    .getColumnIndex("searchcolname")))) {
                                        advSearchDTO.valuesSearchColName = searchColValuesCursor.getString(searchColValuesCursor
                                                .getColumnIndex("searchcolname"));
                                        advSearchDTO.valuesColName = searchColValuesCursor.getString(searchColValuesCursor
                                                .getColumnIndex("searchcolvalue"));
                                        advSearchDTO.valuesDataTableName = searchColValuesCursor.getString(searchColValuesCursor
                                                .getColumnIndex("datatablename"));
                                    }
                                }
                            }
                }

            } catch (Exception e) {
                searchColCursor.close();
                Log.e(TAG,
                        "Error occurred in method getAdvSearchDetails for input parameter navUsId["
                                + navUsId + "]. Error is:" + e.getMessage());

            }
            finally{
                if(searchColCursor!=null){
                    searchColCursor.close();
                }
            }
            return advSearchDTO;
        }
    */


        public Map<String,String>  getAdvSearchColumnsTransDBData(String dataTableName,
                String optionsColumnName,String optionsDisplayColName) {
            Map<String,String> optionsColumnMap = null;
            String displayName;
            String colName;
            try {
                Cursor transCursor;
                 transCursor = TransDatabaseUtil.query(true,dataTableName,
                            new String[] { optionsColumnName , optionsDisplayColName}, OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                            + " <> '1'",null ,null, null, null, null);

                if (transCursor.moveToFirst()) {
                    optionsColumnMap = new HashMap<String, String>();
                 do {
                    displayName = transCursor.getString(transCursor
                            .getColumnIndex(optionsDisplayColName));
                    colName = transCursor.getString(transCursor
                            .getColumnIndex(optionsColumnName));
                    optionsColumnMap.put(colName , displayName);
                 } while (transCursor.moveToNext());
                }
                transCursor.close();
            } catch (SQLException e) {
                Log.e(TAG,
                        "Error occurred in method getTransData for input parameter tableName["
                                + dataTableName + "], columnName["
                                + optionsColumnName + "]. Error is:"
                                + e.getMessage());
            }
            return optionsColumnMap;
        }


        public 	List<String>  getAdvSearchValuesTransDBData(String dataTableName,
                String optionsColumnName,String optionsValueColName, String searchBy) {
            List<String> listValues = null;
            String values;
            String colName;
            try {
            Cursor transCursor;
              transCursor = TransDatabaseUtil.query(false,dataTableName,
                        new String[] { optionsColumnName , optionsValueColName}, OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                        + " <> '1' and "+optionsColumnName+"='"+searchBy+"'",null ,null, null, null, null);

                if (transCursor.moveToFirst()) {
                    listValues = new ArrayList<String>();
                    do {
                    values = transCursor.getString(transCursor
                            .getColumnIndex(optionsValueColName));

                    colName = transCursor.getString(transCursor
                            .getColumnIndex(optionsColumnName));

                    listValues.add(values);
                    } while (transCursor.moveToNext());
                }
                transCursor.close();
            } catch (SQLException e) {
                Log.e(TAG,
                        "Error occurred in method getTransData for input parameter tableName["
                                + dataTableName + "], columnName["
                                + optionsColumnName + "]. Error is:"
                                + e.getMessage());
            }
            return listValues;

        }


        /**
         * get List screen template detail
         *
         * @param usId
         * @return Cursor
         */
        public HashMap<String,String> getListScreenDetailsMap(String usId, int appId) {
            Log.d(TAG, " method getListScreenDetails, usid:" + usId);
            HashMap<String,String> listDetailMap = new HashMap<String,String>();
            Cursor listScreenCursor = null;
            try {
                listScreenCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.LIST_SCREEN_TABLE_NAME,
                        null,
                        OMSDatabaseConstants.LIST_SCREEN_NAVIGATION_UNIQUE_ID + "="
                                + "'" + usId + "'" + SPACE
                                + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                                + appId + "'" + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + "='0'", null, null, null, null);
                Log.d(TAG,"listScreenCursor size"+listScreenCursor.getCount());

                if(listScreenCursor!=null){
                    if(listScreenCursor.moveToFirst()){
                        /*if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex("backgroundImage")) != null) {
                                listDetailMap.put("backgroundImage", listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex("backgroundImage")));
                        }
                        */

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_FILTER_COLUMN_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_FILTER_COLUMN_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_FILTER_COLUMN_NAME)));
                        }


                        if(listScreenCursor
                            .getString(listScreenCursor
                                    .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_TITLE))!=null){
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_TITLE, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_TITLE)));
                        }


                    /*	if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_COLUMNNAME_FOR_TITLE)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_COLUMNNAME_FOR_TITLE, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_COLUMNNAME_FOR_TITLE)));
                        }

                        {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHOW_SEARCH, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_SEARCH))));
                                    }

                    *
                        {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_USE_PAGINATION, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_USE_PAGINATION))));

                        }



                    {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_USE_SERVER_SEARCH, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_USE_SERVER_SEARCH))));

                        }

    {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHOW_HEADER, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_HEADER))));

                        }

                    */
                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_DATA_TABLE_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_DATA_TABLE_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_DATA_TABLE_NAME)));
                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_TYPE)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_TYPE, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_TYPE)));
                        }
                        /*if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.BL_COMPLETE_BL_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.BL_COMPLETE_BL_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.BL_COMPLETE_BL_NAME)));
                        }
                        */

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_UNIQUE_ID)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_UNIQUE_ID, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_UNIQUE_ID)));
                        }


                    /*	if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SEARCH_COLUMN_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SEARCH_COLUMN_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SEARCH_COLUMN_NAME)));
                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME1)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME1, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME1)));
                        }



                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME2)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME2, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME2)));
                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME3)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME3, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SORT_COLUMN_NAME3)));
                        }


                        {
                            listDetailMap.put(OMSDatabaseConstants.USE_WHERE, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.USE_WHERE))));

                        }



                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.WHERE_COLUMN_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.WHERE_COLUMN_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.WHERE_COLUMN_NAME)));
                        }



                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.WHERE_CONSTANT)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.WHERE_CONSTANT, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.WHERE_CONSTANT)));
                        }



                        {
                            listDetailMap.put(OMSDatabaseConstants.RETAIN_WHERE_CLAUSE, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.RETAIN_WHERE_CLAUSE))));

                        }

                    {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHOW_LOCATION_SEARCH, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_LOCATION_SEARCH))));

                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_TABLE_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_TABLE_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_TABLE_NAME)));
                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_COLUMN_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_COLUMN_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_COLUMN_NAME)));
                        }



                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_LATITUDE)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_LATITUDE, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_LATITUDE)));
                        }



                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_LONGITUDE)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_LONGITUDE, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_LONGITUDE)));
                        }

                     {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_RADIUS, Float.toString(listScreenCursor
                                    .getFloat(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_RADIUS))));
                        }


                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_BL_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_BL_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_LOCATION_SEARCH_BL_NAME)));
                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_IS_READ_ITEM_COLUMN_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_IS_READ_ITEM_COLUMN_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_IS_READ_ITEM_COLUMN_NAME)));
                        }

                        {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_IS_READ_ITEM, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_IS_READ_ITEM))));

                        }

                    {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHOW_BADGE, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_BADGE))));

                        }

                        if (listScreenCursor
                                .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_STYLEGUIDE_NAME) > OMSDefaultValues.MIN_INDEX_INT
                                .getValue()) {
                            listDetailMap
                            .put(OMSDatabaseConstants.LIST_SCREEN_STYLEGUIDE_NAME,listScreenCursor.getString(listScreenCursor
                                    .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_STYLEGUIDE_NAME)));
                        }


                        {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHOW_SORTING, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_SORTING))));

                        }



                    {
                            listDetailMap.put(OMSDatabaseConstants.SHOW_INDEX_LIST, Integer.toString( listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.SHOW_INDEX_COLUMN))));
                        }
                        */
                    {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHOW_TOOL_BAR, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_TOOL_BAR))));

                        }

                        /*{
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHOW_ACTION_SHEET, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_ACTION_SHEET))));

                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_REFRESH_BL)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_REFRESH_BL, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_REFRESH_BL)));
                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_PAGINATION_BL)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_PAGINATION_BL, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_PAGINATION_BL)));
                        }


                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SERVER_SEARCH_BL)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SERVER_SEARCH_BL, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SERVER_SEARCH_BL)));
                        }

                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHAKE_TO_REFRESH_BABEL)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHAKE_TO_REFRESH_BABEL, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHAKE_TO_REFRESH_BABEL)));
                        }


                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHAKE_TO_REFRESH_STYLE_NAME)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHAKE_TO_REFRESH_STYLE_NAME, listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHAKE_TO_REFRESH_STYLE_NAME)));
                        }


                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_ADV_SEARCH)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_SHOW_ADV_SEARCH, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_SHOW_ADV_SEARCH))));
                        }


                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_ADV_SEARCH_GRUOP_BY)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LIST_SCREEN_ADV_SEARCH_GRUOP_BY, Integer.toString(listScreenCursor
                                    .getInt(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LIST_SCREEN_ADV_SEARCH_GRUOP_BY))));
                        }
                    */
                        if (listScreenCursor
                                .getString(listScreenCursor
                                        .getColumnIndex(OMSDatabaseConstants.LAUNCH_BL)) != null) {
                            listDetailMap.put(OMSDatabaseConstants.LAUNCH_BL,listScreenCursor
                                    .getString(listScreenCursor
                                            .getColumnIndex(OMSDatabaseConstants.LAUNCH_BL)));
                        }



                    }
                }
            } catch (Exception e) {
                Log.e(TAG,
                        "Error occurred in method getListScreenDetails for input parameter usId["
                                + usId + "]. Error is:" + e.getMessage());

            }
            finally{
                if(listScreenCursor!=null){
                    listScreenCursor.close();
                }
            }
            return listDetailMap;
        }


        public List<ListScreenItemsDTO> getListScreenItemsList(String listUsid,
                                                        int configDBAppId) {
            List<ListScreenItemsDTO> listItems = new ArrayList<ListScreenItemsDTO>();
            try{
                Cursor listScreenItemsCursor = null;
                ListScreenItemsDTO itemDTO;
                listScreenItemsCursor = OMSDBManager.getConfigDB().query(
                        OMSDatabaseConstants.LIST_SCREEN_ITEMS_TABLE,
                        null,
                        OMSDatabaseConstants.SORTING_DATA_LIST_UNIQUE_ID + "="
                                + "'" + listUsid + "'" + SPACE
                                + OMSMessages.AND.getValue() + SPACE
                                + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                                + configDBAppId + "'" + OMSMessages.AND.getValue()
                                + SPACE
                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + "<> '1' ", null, null, null, "position");
                if (listScreenItemsCursor.moveToFirst()) {
                    do{
                        itemDTO = new ListScreenItemsDTO();
                        itemDTO.setPosition(listScreenItemsCursor
                                .getInt(listScreenItemsCursor
                                        .getColumnIndex("position")));
                        itemDTO.setChildNavUsid(listScreenItemsCursor
                                .getString(listScreenItemsCursor
                                        .getColumnIndex("childnavusid")));
                        itemDTO.setPrimaryText(listScreenItemsCursor
                                .getString(listScreenItemsCursor
                                        .getColumnIndex("primarytext")));
                        itemDTO.setSecondaryText(listScreenItemsCursor
                                .getString(listScreenItemsCursor
                                        .getColumnIndex("secondarytext")));
                        itemDTO.setImageURL(listScreenItemsCursor
                                .getString(listScreenItemsCursor
                                        .getColumnIndex("thumbnailimage")));
                        listItems.add(itemDTO);

                    }while(listScreenItemsCursor.moveToNext());
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return listItems;
        }

    public HashMap<Integer,String> getListScreenChildMap(String listUsid,
                                                       int configDBAppId){
        HashMap<Integer,String> listChilds = new HashMap<Integer,String>();
        try{
            Cursor listScreenItemsCursor = null;
            ListScreenItemsDTO itemDTO;
            listScreenItemsCursor = OMSDBManager.getConfigDB().query(
                    OMSDatabaseConstants.LIST_SCREEN_ITEMS_TABLE,
                    null,
                    OMSDatabaseConstants.SORTING_DATA_LIST_UNIQUE_ID + "="
                            + "'" + listUsid + "'" + SPACE
                            + OMSMessages.AND.getValue() + SPACE
                            + OMSDatabaseConstants.CONFIGDB_APPID + " = '"
                            + configDBAppId + "'" + OMSMessages.AND.getValue()
                            + SPACE
                            + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                            + "<> '1' ", null, null, null, "position");
            if (listScreenItemsCursor.moveToFirst()) {
                do{
                   int position = listScreenItemsCursor
                           .getInt(listScreenItemsCursor
                                   .getColumnIndex("position"));
                    String childNavUsid = listScreenItemsCursor
                            .getString(listScreenItemsCursor
                                    .getColumnIndex("childnavusid"));
                    listChilds.put(position,childNavUsid);
                }while(listScreenItemsCursor.moveToNext());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listChilds;

    }


    }

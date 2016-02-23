package watch.oms.omswatch.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import watch.oms.omswatch.OMSDTO.PickerItems;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;

/**
 * Created by 245742 on 1/5/2016.
 */
public class MultiFormScreenHelper {
    private static final String IS_DELETE = "isdelete";
    private static final String SPACE = " ";
    private final String TAG = this.getClass().getSimpleName();



    public Context context;

    //login buttonid and guestlogin
    public int loginButtonId;
    public int guestButtonId;

    public MultiFormScreenHelper(Context c) {
        context = c;
    }

    /**
     * Fetched Multi Form screen data from Multi Form Screen table of Config
     * Database
     *
     * @param navusid
     * @return
     */
    public HashMap<String,String> getMultiFormScreenData(String navusid, int appId)
			/*throws Exception*/ {
        Cursor multiFormScreenDataCursor = null;
        HashMap<String,String> formDetailMap = new HashMap<String,String>();
        try {

            multiFormScreenDataCursor = OMSDBManager
                        .getConfigDB()
                        .query(OMSDatabaseConstants.MULTI_FORM_SCREEN_GRID_TABLE,
                                null,
                                OMSDatabaseConstants.MULTI_FORM_SCREEN_NAVIGATION_SCREEN_UNIQUE_ID
                                        + "="
                                        + "'"
                                        + navusid
                                        + "'"
                                        + " and "
                                        + OMSDatabaseConstants.CONFIGDB_APPID
                                        + " = '"
                                        + appId
                                        + "'"
                                        + " and "
                                        + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                        + " <> '1'", null, null, null, null);
            if(multiFormScreenDataCursor.moveToFirst()){

                if(multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_DATA_TABLE_NAME))!=null) {

                    formDetailMap.put(OMSDatabaseConstants.MULTI_FORM_SCREEN_DATA_TABLE_NAME, multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_DATA_TABLE_NAME)));

                }

                if(multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID)) != null) {
                    formDetailMap.put(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID,multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID)));

                }

                if(multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_TITLE)) !=null){
                    formDetailMap.put(OMSDatabaseConstants.FORM_SCREEN_TITLE, multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_TITLE)));
                }


                if (multiFormScreenDataCursor
                        .getInt(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED)) != 0) {
                    int isPrepopulated = multiFormScreenDataCursor
                            .getInt(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED));
                    formDetailMap.put(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED,""+isPrepopulated);


                }else{
                    formDetailMap.put(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED,""+0);
                }



                if(multiFormScreenDataCursor
                        .getInt(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_SHOW_EDIT)) != 0){
                    int showEdit = multiFormScreenDataCursor
                            .getInt(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_SHOW_EDIT));
                    formDetailMap.put(OMSDatabaseConstants.FORM_SCREEN_SHOW_EDIT,""+showEdit);

                }


                if(multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.WHERE_COLUMN_NAME))!=null){
                    formDetailMap.put(OMSDatabaseConstants.WHERE_COLUMN_NAME, multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.WHERE_COLUMN_NAME)));

                }

                if( multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.WHERE_CONSTANT))!=null){
                    formDetailMap.put(OMSDatabaseConstants.WHERE_CONSTANT, multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.WHERE_CONSTANT)));
                }


                formDetailMap.put(OMSDatabaseConstants.USE_WHERE,""+multiFormScreenDataCursor
                        .getInt(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.USE_WHERE)));

                if( multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex("childnavusid"))!=null){

                    formDetailMap.put("childnavusid",multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex("childnavusid")));
                }

            }

        } catch (SQLException e) {
            Log.e(TAG,
                    "Exception occured while fetching  data  from MultiFormScreen table of ConfigDB."
                            + e.getMessage());
            //throw new Exception(e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG,
                    "Exception occured while fetching  data  from MultiFormScreen table of ConfigDB."
                            + e.getMessage());
            //	throw new Exception(e.getMessage());
        }
        catch (Exception e){
            Log.e(TAG,
                    "Exception occured while fetching  data  from MultiFormScreen table of ConfigDB."
                            + e.getMessage());
        }
        return formDetailMap;
    }


    /**
     * Fetched Multi Form screen data from Multi Form Screen table of Config
     * Database
     *
     * @param navusid
     * @return
     */
    public HashMap<String,String> getMultiFormScreenData(String navusid, boolean isPrepopulated,
                                         int appId) {
        int prepopulated = OMSDefaultValues.MIN_INDEX_INT.getValue();
        if (isPrepopulated) {
            prepopulated = OMSDefaultValues.DEF_CNT.getValue();
        }
        Cursor multiFormScreenDataCursor = null;
        HashMap<String,String> formDetailMap = new HashMap<String,String>();
        try {
            multiFormScreenDataCursor = OMSDBManager
                    .getConfigDB()
                    .query(OMSDatabaseConstants.MULTI_FORM_SCREEN_GRID_TABLE,
                            null,
                            OMSDatabaseConstants.MULTI_FORM_SCREEN_NAVIGATION_SCREEN_UNIQUE_ID
                                    + "="
                                    + "'"
                                    + navusid
                                    + "'"
                                    + "AND "
                                    + OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED
                                    + "= "
                                    + prepopulated
                                    + " and "
                                    + OMSDatabaseConstants.CONFIGDB_APPID
                                    + " = '"
                                    + appId
                                    + "'"
                                    + "and "
                                    + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                    + " <> '1'", null, null, null, null);


            if(multiFormScreenDataCursor.moveToFirst()){

                if(multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_DATA_TABLE_NAME))!=null) {

                    formDetailMap.put(OMSDatabaseConstants.MULTI_FORM_SCREEN_DATA_TABLE_NAME, multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_DATA_TABLE_NAME)));

                }

                if(multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID)) != null) {
                    formDetailMap.put(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID,multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID)));

                }

                if(multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_TITLE)) !=null){
                    formDetailMap.put(OMSDatabaseConstants.FORM_SCREEN_TITLE, multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_TITLE)));
                }


                if (multiFormScreenDataCursor
                        .getInt(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED)) != 0) {


                    formDetailMap.put(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED,""+multiFormScreenDataCursor
                            .getInt(multiFormScreenDataCursor.getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED)));


                }else{
                    formDetailMap.put(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED,""+0);
                }


                if(multiFormScreenDataCursor
                        .getInt(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_SHOW_EDIT)) != 0){
                    int showEdit = multiFormScreenDataCursor
                            .getInt(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.FORM_SCREEN_SHOW_EDIT));
                    formDetailMap.put(OMSDatabaseConstants.FORM_SCREEN_SHOW_EDIT,""+showEdit);

                }


                if(multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.WHERE_COLUMN_NAME))!=null){
                    formDetailMap.put(OMSDatabaseConstants.WHERE_COLUMN_NAME, multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.WHERE_COLUMN_NAME)));

                }

                if( multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.WHERE_CONSTANT))!=null){
                    formDetailMap.put(OMSDatabaseConstants.WHERE_CONSTANT, multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.WHERE_CONSTANT)));
                }


                formDetailMap.put(OMSDatabaseConstants.USE_WHERE,""+multiFormScreenDataCursor
                        .getInt(multiFormScreenDataCursor
                                .getColumnIndex(OMSDatabaseConstants.USE_WHERE)));

                if( multiFormScreenDataCursor
                        .getString(multiFormScreenDataCursor
                                .getColumnIndex("childnavusid"))!=null){

                    formDetailMap.put("childnavusid",multiFormScreenDataCursor
                            .getString(multiFormScreenDataCursor
                                    .getColumnIndex("childnavusid")));
                }

            }
        } catch (SQLException e) {
            Log.e(TAG,
                    "Exception occured while fetching  data  from MultiFormScreen table of ConfigDB."
                            + e.getMessage());
            e.printStackTrace();

        }
        return formDetailMap;
    }


    @SuppressWarnings("unused")
    public Hashtable<Integer, Hashtable<String, Object>> getMultiFormScreenGridItems(
            String fUsid, int appId) {
        Hashtable<Integer, Hashtable<String, Object>> rowIndexHashTable = new Hashtable<Integer, Hashtable<String, Object>>();
        List<Hashtable<Integer, Hashtable<String, Object>>> tempList = new ArrayList<Hashtable<Integer, Hashtable<String, Object>>>();
        int columnCount = 4;
        String MULTIFORM_GRID_ROW_INDEX = "rowindex";
        Cursor multiFormScreenItemsCursor=null;
        try {
            for (int rowIndex = 0; rowIndex < OMSConstants.GRID_FORM_ROW_COUNT; rowIndex++) {

                multiFormScreenItemsCursor = OMSDBManager
                        .getConfigDB()
                        .query(OMSDatabaseConstants.MULTI_FORM_SCREEN_GRID_ITEMS_TABLE,
                                null,
                                OMSDatabaseConstants.MULTI_FORM_SCREEN_ITEMS_FORM_USID
                                        + " = "
                                        + "'"
                                        + fUsid
                                        + "'"
                                        + " and "
                                        + OMSDatabaseConstants.CONFIGDB_APPID
                                        + " = '"
                                        + appId
                                        + "'"
                                        + "and "
                                        + MULTIFORM_GRID_ROW_INDEX
                                        + " = '"
                                        + rowIndex
                                        + "'"
                                        + "and "
                                        + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                        + " <> '1'", null, null, null, null);
                if(multiFormScreenItemsCursor!=null){
                    if (multiFormScreenItemsCursor.moveToFirst()) {
                        do {

                            Hashtable<String, Object> tempmultiFormHashTable = new Hashtable<String, Object>();

                            tempmultiFormHashTable
                                    .put(OMSDatabaseConstants.UNIQUE_ROW_ID,
                                            multiFormScreenItemsCursor
                                                    .getString(multiFormScreenItemsCursor
                                                            .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_ITEMS_UNIQUE_ID)));
                            String formItemUsid = multiFormScreenItemsCursor
                                    .getString(multiFormScreenItemsCursor
                                            .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_ITEMS_UNIQUE_ID));
                       //     for (int i = 1; i <= columnCount; i++) {

                                if (multiFormScreenItemsCursor
                                        .getString(multiFormScreenItemsCursor
                                                .getColumnIndex("columnname")) != null) {
                                    tempmultiFormHashTable
                                            .put("columnname",
                                                    multiFormScreenItemsCursor
                                                            .getString(multiFormScreenItemsCursor
                                                                    .getColumnIndex("columnname")));
                                } else {
                                    tempmultiFormHashTable.put("columnname","");
                                }
                                if (multiFormScreenItemsCursor
                                        .getString(multiFormScreenItemsCursor
                                                .getColumnIndex("columncontent")) != null) {
                                    tempmultiFormHashTable
                                            .put("columncontent",
                                                    multiFormScreenItemsCursor
                                                            .getString(multiFormScreenItemsCursor
                                                                    .getColumnIndex("columncontent")));
                                } else {
                                    tempmultiFormHashTable.put(
                                            "columncontent", "");
                                }
                                if (multiFormScreenItemsCursor
                                        .getString(multiFormScreenItemsCursor.getColumnIndex("columnid")) != null) {
                                    tempmultiFormHashTable
                                            .put("columnid",
                                                    multiFormScreenItemsCursor
                                                            .getString(multiFormScreenItemsCursor
                                                                    .getColumnIndex("columnid")));
                                } else {
                                    tempmultiFormHashTable
                                            .put("columnid", "");
                                }

                           if (multiFormScreenItemsCursor
                                    .getString(multiFormScreenItemsCursor.getColumnIndex("blname")) != null) {
                                tempmultiFormHashTable
                                        .put("blname",
                                                multiFormScreenItemsCursor
                                                        .getString(multiFormScreenItemsCursor
                                                                .getColumnIndex("blname")));
                            } else {
                                tempmultiFormHashTable
                                        .put("blname", "");
                            }

                            if (multiFormScreenItemsCursor
                                    .getString(multiFormScreenItemsCursor.getColumnIndex("type")) != null) {
                                tempmultiFormHashTable
                                        .put("type",
                                                multiFormScreenItemsCursor
                                                        .getString(multiFormScreenItemsCursor
                                                                .getColumnIndex("type")));
                            } else {
                                tempmultiFormHashTable
                                        .put("type", "");
                            }


                                tempmultiFormHashTable
                                        .put("columntype",
                                                multiFormScreenItemsCursor
                                                        .getString(multiFormScreenItemsCursor.getColumnIndex("columntype")));


                             /*   if (!multiFormScreenItemsCursor.getString(
                                        multiFormScreenItemsCursor
                                                .getColumnIndex(OMSMessages.COLUMN
                                                        .getValue()
                                                        + i
                                                        + OMSMessages.TYPE
                                                        .getValue()))
                                        .equalsIgnoreCase("none")) {
                                    Hashtable<String, Object> widgetAlignmentHash = getWidgetAlignment(
                                            multiFormScreenItemsCursor
                                                    .getInt(multiFormScreenItemsCursor.getColumnIndex(OMSMessages.COLUMN
                                                            .getValue()
                                                            + i
                                                            + OMSMessages.INDEX
                                                            .getValue())),
                                            multiFormScreenItemsCursor
                                                    .getString(multiFormScreenItemsCursor.getColumnIndex(OMSMessages.COLUMN
                                                            .getValue()
                                                            + i
                                                            + OMSMessages.TYPE
                                                            .getValue())),
                                            formItemUsid, appId);
                                    if (widgetAlignmentHash.size() > 0
                                            && widgetAlignmentHash != null) {

                                        tempmultiFormHashTable.put(
                                                OMSMessages.COLUMN.getValue()
                                                        + i
                                                        + OMSMessages.WIDTH
                                                        .getValue(),
                                                widgetAlignmentHash
                                                        .get("columnwidth"));
                                        tempmultiFormHashTable.put(
                                                OMSMessages.COLUMN.getValue()
                                                        + i
                                                        + OMSMessages.HEIGHT
                                                        .getValue(),
                                                widgetAlignmentHash
                                                        .get("columnheight"));
                                        tempmultiFormHashTable.put(
                                                OMSMessages.COLUMN.getValue()
                                                        + i
                                                        + OMSMessages.STYLE
                                                        .getValue(),
                                                widgetAlignmentHash
                                                        .get("widgetstyle"));
                                        tempmultiFormHashTable.put(
                                                OMSMessages.COLUMN.getValue()
                                                        + i
                                                        + OMSMessages.ALIGNMENT
                                                        .getValue(),
                                                widgetAlignmentHash
                                                        .get("widgetalignment"));
                                        tempmultiFormHashTable.put(
                                                OMSMessages.COLUMN.getValue()
                                                        + i
                                                        + OMSMessages.BUTTONTYPE
                                                        .getValue(),
                                                widgetAlignmentHash
                                                        .get("buttontype"));
                                        tempmultiFormHashTable.put(
                                                OMSMessages.COLUMN.getValue()
                                                        + i
                                                        + OMSMessages.BLNAME
                                                        .getValue(),
                                                widgetAlignmentHash.get("blname"));
                                        tempmultiFormHashTable.put(
                                                OMSMessages.COLUMN.getValue()
                                                        + i
                                                        + OMSMessages.BUTTONURL
                                                        .getValue(),
                                                widgetAlignmentHash.get("url"));
                                    }
                                }

                                tempmultiFormHashTable
                                        .put(OMSMessages.COLUMN.getValue() + i
                                                        + OMSMessages.INDEX.getValue(),
                                                multiFormScreenItemsCursor
                                                        .getInt(multiFormScreenItemsCursor.getColumnIndex(OMSMessages.COLUMN
                                                                .getValue()
                                                                + i
                                                                + OMSMessages.INDEX
                                                                .getValue())));
                                                                */

                          //  }
                            tempmultiFormHashTable
                                    .put(OMSDatabaseConstants.FORM_SCREEN_ITEMS_FORM_USID,
                                            multiFormScreenItemsCursor
                                                    .getString(multiFormScreenItemsCursor
                                                            .getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_ITEMS_FORM_USID)));

                            rowIndexHashTable.put(rowIndex, tempmultiFormHashTable);

                        } while (multiFormScreenItemsCursor.moveToNext());

                    }
                }
                multiFormScreenItemsCursor.close();
            }
        } catch (Exception e) {
            multiFormScreenItemsCursor.close();
            e.printStackTrace();
        }
        finally{
            if(multiFormScreenItemsCursor!=null){
                multiFormScreenItemsCursor.close();
            }
        }
        return rowIndexHashTable;

    }


    /**
     * Returns the picker data list that includes picker type,content ,data
     * table name
     *
     * @param formUsid
     * @param multiFormScreenItemsusid
     * @param position
     * @return
     */
    public List<PickerItems> getPickerData(String formUsid,
                                           String multiFormScreenItemsusid, String position, int appId) {
        List<PickerItems> pickerData = new ArrayList<PickerItems>();
        Cursor pickerCursor = null;
        try {
            pickerCursor = OMSDBManager.getConfigDB().query(
                    OMSDatabaseConstants.PICKER_CONFIG_TABLE_NAME,
                    null,
                    "formusid ='" + formUsid + "' and parentusid ='"
                            + multiFormScreenItemsusid
                            + "' and pickerposition='" + position + "'"
                            + " and " + OMSDatabaseConstants.CONFIGDB_APPID
                            + " = '" + appId + "'" + "and "
                            + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                            + " <> '1'", null, null, null, null);
            for (boolean hasItem = pickerCursor.moveToFirst(); hasItem; hasItem = pickerCursor
                    .moveToNext()) {
                PickerItems tempPickerItems = new PickerItems();
                tempPickerItems.pickerdatatablename = pickerCursor
                        .getString(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_PICKER_DATA_TABLE_NAME));
                tempPickerItems.pickertype = pickerCursor
                        .getString(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_PICKER_TYPE));
                tempPickerItems.pickercontent = pickerCursor
                        .getString(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_PICKER_CONTENT));
                tempPickerItems.useWhere = pickerCursor.getInt(pickerCursor
                        .getColumnIndex(OMSDatabaseConstants.USE_WHERE));
                tempPickerItems.whereColumn = pickerCursor
                        .getString(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.WHERE_COLUMN_NAME));
                tempPickerItems.whereConstant = pickerCursor
                        .getString(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.WHERE_CONSTANT));

                // Picker Mapping
                tempPickerItems.pickermapping = pickerCursor
                        .getInt(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_PICKER_MAPPING));

                // Picker Key
                tempPickerItems.pickerkey = pickerCursor
                        .getString(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_PICKER_CONTENT_KEY));

                tempPickerItems.usId = pickerCursor
                        .getString(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));


                tempPickerItems.hasdependent = pickerCursor
                        .getInt(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_HAS_DEPENDENT));
                tempPickerItems.isdependent = pickerCursor
                        .getInt(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_IS_DEPENDENT));
                tempPickerItems.dependentpickerusid = pickerCursor
                        .getString(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_DEPENDENT_PICKER_USID));
                tempPickerItems.isMandatory = pickerCursor
                        .getInt(pickerCursor
                                .getColumnIndex(OMSDatabaseConstants.PICKER_CONFIG_IS_MANDATORY));
                pickerData.add(tempPickerItems);

            }
            pickerCursor.close();
        } catch (IllegalArgumentException e) {
            Log.e(TAG,
                    "Exception occured while fetching picker data  from PickerConfig table of ConfigDB."
                            + e.getMessage());
            e.printStackTrace();
        }

        return pickerData;
    }


}

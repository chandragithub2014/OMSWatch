package watch.oms.omswatch.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.utils.OMSAlertDialog;

/**
 * Created by 245742 on 12/23/2015.
 */
public class OMSTransHelper {

    private final String TAG = this.getClass().getSimpleName();
    public static String lastInsertedUSID = "";
    public Context ctx;
    public OMSTransHelper(Context c) {
        ctx = c;
    }


    public String fetchFilterColumnValFromTransDBData(List<String> colsList,String dataTableName,  HashMap<String,String> columnNameMap ,String filterColumn,int position){
        String filterColumnVal="";
        Cursor transCursor = null;
        /*String[] colArr = new String[colsList.size()];
        colArr = colsList.toArray(colArr);*/


        //

        String[] colArr = null;//new String[colsList.size()];

        List<String> updatedColList = checkForColumnExistence(colsList, dataTableName);
        if(updatedColList!=null && updatedColList.size()>0){
            Log.d(TAG,"11111111111111");
            colArr = new String[updatedColList.size()];
            colArr = updatedColList.toArray(colArr);
            colsList = new ArrayList<String>();
            for(int i=0;i<updatedColList.size();i++){
                colsList.add(updatedColList.get(i));
            }
        }else {
            Log.d(TAG,"222222222222");
            colArr = new String[colsList.size()];
            colArr = colsList.toArray(colArr);
        }

        //



        try{
            transCursor = TransDatabaseUtil.query(dataTableName,colArr,null,null,null,null,null);
            if(transCursor.moveToPosition(position))
                if (transCursor.getColumnIndex(filterColumn) > -1) {
                    filterColumnVal = transCursor.getString(transCursor
                            .getColumnIndex(filterColumn));
                }

        }catch (Exception e){

        }
        transCursor.close();
        return filterColumnVal;
    }
    public List<HashMap<String,String>> fetchTransDBData(List<String> colsList,String dataTableName,  HashMap<String,String> columnNameMap ){
        Cursor transCursor = null;
        String[] colArr = new String[colsList.size()];
        colArr = colsList.toArray(colArr);
  //      HashMap<String,String> transDataHash = new HashMap<String,String>();
        List<HashMap<String,String>> transHashList = new ArrayList<>();
        try{
            transCursor = TransDatabaseUtil.query(dataTableName,colArr,null,null,null,null,null);
            if(transCursor.moveToFirst()){
                do{
                    HashMap<String,String> transDataHash = new HashMap<String,String>();
                    for (int i = 0; i < colsList.size(); i++) {
                        transDataHash.put(columnNameMap.get(colsList.get(i)), transCursor.getString(transCursor
                                .getColumnIndex(colsList.get(i))));
                      }
                    transHashList.add(transDataHash);
                }while(transCursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        transCursor.close();
        return transHashList;
    }

    public List<HashMap<String,String>> fetchTransDBDataforHomogeneousList(List<String> colsList,String dataTableName,  HashMap<String,String> columnNameMap ,String retainWhereClause,String whereClauseColumnName, String whereClauseConstant){
        colsList.add("usid");
        columnNameMap.put("usid","uniqueid");
        String selection = null;
        Cursor transCursor = null;
        String[] colArr = null;//new String[colsList.size()];

        List<String> updatedColList = checkForColumnExistence(colsList, dataTableName);
        if(updatedColList!=null && updatedColList.size()>0){
            Log.d(TAG,"11111111111111");
            colArr = new String[updatedColList.size()];
            colArr = updatedColList.toArray(colArr);
            colsList = new ArrayList<String>();
            for(int i=0;i<updatedColList.size();i++){
                colsList.add(updatedColList.get(i));
            }
        }else {
            Log.d(TAG,"222222222222");
            colArr = new String[colsList.size()];
            colArr = colsList.toArray(colArr);
        }
        //      HashMap<String,String> transDataHash = new HashMap<String,String>();
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
        if (retainWhereClause != null) {
            selection = retainWhereClause + " AND " + selection;
        }

        List<HashMap<String,String>> transHashList = new ArrayList<>();
        try{
            transCursor = TransDatabaseUtil.query(dataTableName,colArr,selection,null,null,null,null);
            if(transCursor.moveToFirst()){
                do{
                    HashMap<String,String> transDataHash = new HashMap<String,String>();
                    for (int i = 0; i < colsList.size(); i++) {
                        transDataHash.put(columnNameMap.get(colsList.get(i)), transCursor.getString(transCursor
                                .getColumnIndex(colsList.get(i))));

                    }
                    transHashList.add(transDataHash);
                }while(transCursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(transCursor!=null) {
            transCursor.close();
        }
        return transHashList;
    }

    public List<String> checkForColumnExistence(List<String> colsList,String dataTableName){
        List<String> colList = new ArrayList<String>();
        List<String> updatedColList = new ArrayList<String>();
        Cursor transCursor = null;
        String selection = null;

        if (selection != null) {
            selection = selection + " AND "
                    + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                    + " <> '1'";
        } else {
            selection = OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                    + " <> '1'";
        }

        try{
            transCursor = TransDatabaseUtil.rawQuery("PRAGMA table_info(" + dataTableName + ")", null);//TransDatabaseUtil.query(dataTableName,null,selection,null,null,null,null);
            if(transCursor.moveToFirst()){
                do{
                   colList.add(transCursor.getString(transCursor
                           .getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)));
                }while(transCursor.moveToNext());
               /* for(int i=0;i<colsList.size();i++){
                    if(transCursor.getColumnIndex(colsList.get(i))!=-1){
                        colList.add(colsList.get(i));
                    }
                }*/
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        transCursor.close();
if(colList.size()>0) {
    for (int i = 0; i < colsList.size(); i++) {
        for (int j = 0; j < colList.size(); j++) {
           if(colsList.get(i).equalsIgnoreCase(colList.get(j))){
               updatedColList.add(colList.get(j));
           }
        }
    }
}
        return  updatedColList;




    }
    /**
     * fetch picker table data from trans db for given column
     *
     * @param tableName
     * @param columnName
     * @return List<String>
     */
    public List<String> getPickerDataWithDependency(String tableName,
                                                    String columnName, String whereClauseColumnName,
                                                    String whereClauseConstant,
                                                    String dependentPickerWhereCondition) throws Exception {
        Log.d(TAG, "getPickerDataWithDependency - tableName: "
                + tableName + " - ColumnName: " + columnName);
        Cursor transCursor = null;
        List<String> pickerList = new ArrayList<String>();
        columnName = columnName.trim();
        String pickerData="";
        // pickerList.add("");// for no option
        String selection = null;
        try {
            //if (OMSApplication.getInstance().getGlobalFilterColumnVal() != null) {
            if (whereClauseColumnName != null
                    && whereClauseColumnName.length() > 0) {
                if (whereClauseConstant != null
                        && whereClauseConstant.length() > 0) {
                    selection = whereClauseColumnName + " = " + "'"
                            + whereClauseConstant + "'";
                } else {
                    if (whereClauseColumnName.equalsIgnoreCase("appid")
                            && OMSApplication.getInstance().getAppId()
                            .equalsIgnoreCase("10")) {

                        selection = whereClauseColumnName
                                + " = "
                                + "'"
                                + OMSApplication.getInstance()
                                .getCurrentAppvisionAppId() + "'";
                    } else {
                        selection = whereClauseColumnName
                                + " = "
                                + "'"
                                + OMSApplication.getInstance()
                                .getGlobalFilterColumnVal() + "'";
                    }

                }
            }
            //	}


            if (selection != null) {
                if (!TextUtils.isEmpty(dependentPickerWhereCondition)) {
                    selection = selection + " AND " + dependentPickerWhereCondition;
                }
            } else {
                if (!TextUtils.isEmpty(dependentPickerWhereCondition)) {
                    selection = dependentPickerWhereCondition;
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

            transCursor = TransDatabaseUtil.query(tableName,

                    new String[] { "distinct " + columnName }, selection, null, null,
                    null, null);

            if (transCursor.moveToFirst()) {
                do {
                    pickerData = transCursor.getString(transCursor
                            .getColumnIndex(columnName));
                    if(!TextUtils.isEmpty(pickerData)) {
                        pickerList.add(pickerData);
                    }
                } while (transCursor.moveToNext());
            }
            transCursor.close();
        } catch (SQLException e) {
            Log.e(TAG,
                    "Error occurred in method getTransData for input parameter tableName["
                            + tableName + "], columnName[" + columnName
                            + "]. Error is:" + e.getMessage());
            throw new Exception(e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG,
                    "Error occurred in method getTransData for input parameter tableName["
                            + tableName + "], columnName[" + columnName
                            + "]. Error is:" + e.getMessage());
            throw new Exception(e.getMessage());
        }
        return pickerList;
    }



    /**
     * inserts new entry into trans db from add screen and form screen
     *
     * @param tableName
     * @param newrowvalues
     * @param
     * @return Integer
     */
   /* public Integer insertTransData(String tableName, List<String> columnname,
                                   List<String> columnval) throws Exception {*/
        public Integer insertTransData(String tableName, ContentValues newrowvalues) throws Exception {
        Log.i(TAG, "In insertTransData");
        long rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
        Calendar cal;

        try {
            rowId = TransDatabaseUtil.insert(tableName, null,
                    newrowvalues);
        } catch (SQLException e) {
            Log.e(TAG, "Error occurred in method insertTransData. Error is:"
                    + e.getMessage());
            throw new Exception(e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error occurred in method insertTransData. Error is:"
                    + e.getMessage());
            throw new Exception(e.getMessage());
        }

        return (int) rowId;
    }


    public String getUsidFromTransTable(String tableName, String id) {
        String usid = null;
        try {
            Cursor usidCursor = TransDatabaseUtil.query(tableName,
                    new String[] { OMSDatabaseConstants.UNIQUE_ROW_ID },
                    OMSDatabaseConstants.KEY_ROWID + "=" + id, null, null,
                    null, null);
            if (usidCursor.moveToFirst()) {
                usid = usidCursor.getString(usidCursor
                        .getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID));
            }
            usidCursor.close();
        } catch (SQLException e) {
            Log.e(TAG,
                    "Error occurred in method getUsidFromTransTable for input parameter tableName["
                            + tableName + "], id[" + id + "]. Error is:"
                            + e.getMessage());
        }
        return usid;
    }



    public Integer updateTransData(String rowId, String tableName,
                                   ContentValues updatedValues) throws Exception {
        long updateRow = OMSDefaultValues.NONE_DEF_CNT.getValue();



        try {
            updateRow = TransDatabaseUtil.update(tableName,
                    updatedValues, OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
                    new String[] { rowId });
        } catch (SQLException e) {
            Log.e(TAG,
                    "Error occurred in method updateTransData for input parameter rowId["
                            + rowId + "], tableName[" + tableName
                            + "]. Error is:" + e.getMessage());
            throw new Exception(e.getMessage());

        } catch (IllegalArgumentException e) {
            Log.e(TAG,
                    "Error occurred in method updateTransData for input parameter rowId["
                            + rowId + "], tableName[" + tableName
                            + "]. Error is:" + e.getMessage());
            throw new Exception(e.getMessage());

        }
        return (int) updateRow;
    }


    /**
     * Fetches TransDB data for prepopulated multi form screen
     *
     * @param tableName
     * @return Hashtable<String,Object>
     */
	/*
	 * public Hashtable<String, Object> getTransDataForPrepopulatedMultiForm(
	 * String tableName, String prePopulatedTransUsid, String
	 * transFilterColName, String transFilterColValue) {
	 */
    public Hashtable<String, Object> getTransDataForPrepopulatedMultiForm(
            String tableName, String prePopulatedTransUsid,
            String whereClauseColumnName, String whereClauseConstant,String retainWhereClause)
            throws Exception {

        Cursor transCursor = null;
        Cursor pragmaCursor = null;
        Hashtable<String, Object> transHashTable = new Hashtable<String, Object>();
        List<String> columnList = new ArrayList<String>();
        try {
            pragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
                    "pragma table_info(" + tableName + ");", null);
            if (pragmaCursor.getCount() > 0) {
                if (pragmaCursor.moveToFirst()) {
                    do {
                        columnList
                                .add(pragmaCursor.getString(pragmaCursor
                                        .getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)));
                    } while (pragmaCursor.moveToNext());
                }
            }
            pragmaCursor.close();
            if (columnList.size() > 0) {
                if (prePopulatedTransUsid != null
                        && prePopulatedTransUsid.trim().length() > 0) {

                    transCursor = TransDatabaseUtil.query(
                            tableName,
                            null,
                            OMSDatabaseConstants.UNIQUE_ROW_ID + "=" + "'"
                                    + prePopulatedTransUsid + "' AND " +OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                    + " <> '1'", null, null,
                            null, null);
                } else {

                    String selection = null;

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
                        selection = selection
                                + " AND "
                                + OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + " <> '1'";
                    } else {
                        selection = OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + " <> '1'";
                    }

                    if (retainWhereClause != null) {
                        selection = retainWhereClause + " AND " + selection;
                    }
//Log.d(TAG,"Selection For Prepopulated Form::::"+selection);
                    transCursor = TransDatabaseUtil.query(tableName,
                            null, selection, null, null, null, null);

                }
                if (transCursor.moveToFirst()) {
                    for (int i = 0; i < columnList.size(); i++) {
                      /*  Log.d(TAG,"Colmn Name & Col Val:::"+" "+columnList.get(i)+"  "+transCursor.getString(transCursor
                                .getColumnIndex(columnList.get(i))));*/
                        if (transCursor.getString(transCursor
                                .getColumnIndex(columnList.get(i))) != null) {
                            transHashTable
                                    .put(columnList.get(i), transCursor
                                            .getString(transCursor
                                                    .getColumnIndex(columnList
                                                            .get(i))));
                        } else {
                            transHashTable.put(columnList.get(i), "");
                        }

                    }
                }
                transCursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG,
                    "Error occurred in method getTransDataForPrepopulatedMultiForm for input parameter tableName["
                            + tableName + "]. Error is:" + e.getMessage());
            throw new Exception(e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG,
                    "Error occurred in method getTransDataForPrepopulatedMultiForm for input parameter tableName["
                            + tableName + "]. Error is:" + e.getMessage());
            throw new Exception(e.getMessage());
        }
        return transHashTable;
    }


    /**
     * Method retrieves Picker Key from the given table name for Picker Content
     *
     * @param
     * @param
     * @return List<String>
     */
    public String getPickerKeyForValue(String pickerTableName,
                                       String pickerValColumnName, String pickerKeyColumnName,
                                       String pickerValue) {
        Cursor transCursor = null;
        String selection = null;
        String pickerKey = OMSConstants.EMPTY_STRING;
        String pickerValueFromDb = null;
        try {

            Log.d(TAG, "Retrieving Picker Key from Table: " + pickerTableName
                    + " for PickerValue - " + pickerValue);
            selection = OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                    + " <> '1'";

            transCursor = TransDatabaseUtil.query(pickerTableName,
                    null, selection, null, null, null, null);

            if (transCursor.moveToFirst()) {
                do {
                    pickerValueFromDb = transCursor.getString(transCursor
                            .getColumnIndex(pickerValColumnName));
                    if (pickerValueFromDb.equals(pickerValue)) {
                        pickerKey = transCursor.getString(transCursor
                                .getColumnIndex(pickerKeyColumnName));
                        Log.d(TAG, "Found Picker Key :: " + pickerKey);
                        break;
                    }
                } while (transCursor.moveToNext());
            }
            transCursor.close();
        } catch (SQLException e) {
            Log.e(TAG,
                    "Error occurred in method getTransData for input parameter tableName["
                            + pickerTableName + "], columnName["
                            + pickerValColumnName + "]. Error is:"
                            + e.getMessage());
        }
        return pickerKey;
    }


    /**
     * update status column of trans database table when data is posted to
     * server through web service e.g. Status: TRIED - When webservice is failed
     * , COMPLETED - When data posted successfully
     *
     * @param tableName
     * @param contentValues
     * @param uniqueID
     * @return Integer
     */
    public Integer updateTransDataStatus(String tableName,
                                         ContentValues contentValues, String uniqueID) {
        long updateRow = OMSDefaultValues.NONE_DEF_CNT.getValue();
        Cursor currentActionCursor = null;
        try {
            currentActionCursor = TransDatabaseUtil.query(tableName,
                    null, OMSDatabaseConstants.UNIQUE_ROW_ID + "=" + uniqueID,
                    null, null, null, null);
            if (currentActionCursor.moveToFirst()) {
                updateRow = TransDatabaseUtil.update(tableName,
                        contentValues,
                        OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? ",
                        new String[] { uniqueID });
            }
            currentActionCursor.close();
        } catch (SQLException e) {
            Log.e(TAG,
                    "Error occurred in method updateTransDataStatus for input parameter tableName["
                            + tableName + "], uniqueID[" + uniqueID
                            + "]. Error is:" + e.getMessage());
        }
        return (int) updateRow;
    }


    public String getURLforTransColumn(String tableName, String columnName) {
        String imageurl = null;
        Cursor transDBDataCursor = null;
        try {
            if (tableName != null && columnName != null) {
                transDBDataCursor = TransDatabaseUtil.query(
                        tableName,
                        new String[] { OMSDatabaseConstants.UNIQUE_ROW_ID,
                                columnName },
                        OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
                                + " <> '1'", null, null, null, null);
                if (transDBDataCursor.moveToFirst()) {
                    imageurl = transDBDataCursor.getString(transDBDataCursor
                            .getColumnIndex(columnName));
                }
                transDBDataCursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG,
                    "Error occurred in method getURLforTransColumn  for input parameter tableName["
                            + tableName + "], columnName[" + columnName
                            + "]. Error is:" + e.getMessage());

        }
        if (imageurl != null)
            if (imageurl.contains("http"))
                imageurl = imageurl.replace(" ", "%20");
        return imageurl;
    }


    /**
     * Retrieves the list of trans usids to be used for BL
     *
     * @param tableName
     * @return ArrayList<String>
     */
    public ArrayList<String> getTransUsids(String tableName, String tempQuery,
                                           String filterCoulmnName, String whereClauseColumnName,
                                           String whereClauseConstant) throws Exception {
        ArrayList<String> transList = new ArrayList<String>();
        Cursor transDBDataCursor = null;
        String selection = null;
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
            if (tempQuery != null) {
                transDBDataCursor = TransDatabaseUtil.query(
                        tableName,
                        new String[] { OMSDatabaseConstants.UNIQUE_ROW_ID,
                                tempQuery }, selection, null, null, null, null);
            } else {
                transDBDataCursor = TransDatabaseUtil.query(tableName,
                        new String[] { OMSDatabaseConstants.UNIQUE_ROW_ID },
                        selection, null, null, null, null);
            }
            if (transDBDataCursor.moveToFirst()) {
                do {
                    transList
                            .add(transDBDataCursor.getString(transDBDataCursor
                                    .getColumnIndex(OMSDatabaseConstants.UNIQUE_ROW_ID)));
                } while (transDBDataCursor.moveToNext());
            }
            transDBDataCursor.close();

        } catch (SQLException e) {
            Log.e(TAG,
                    "Error occurred in method getTransUsids  for input parameter tableName["
                            + tableName + "], tempQuery[" + tempQuery
                            + "], filterCoulmnName[" + filterCoulmnName
                            + "]. Error is:" + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG,
                    "Error occurred in method getTransUsids  for input parameter tableName["
                            + tableName + "], tempQuery[" + tempQuery
                            + "], filterCoulmnName[" + filterCoulmnName
                            + "]. Error is:" + e.getMessage());
        }
        return transList;
    }


}

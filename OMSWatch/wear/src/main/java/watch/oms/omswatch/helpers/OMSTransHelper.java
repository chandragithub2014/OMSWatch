package watch.oms.omswatch.helpers;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;

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
        String[] colArr = new String[colsList.size()];
        colArr = colsList.toArray(colArr);
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
        String selection = null;
        Cursor transCursor = null;
        String[] colArr = new String[colsList.size()];
        colArr = colsList.toArray(colArr);
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
        transCursor.close();
        return transHashList;
    }
}

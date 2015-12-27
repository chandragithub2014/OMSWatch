package watch.oms.omswatch.helpers;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import watch.oms.omswatch.WatchDB.TransDatabaseUtil;

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
        return transHashList;
    }
}

/**
 * 
 */
package watch.oms.omswatch.actioncenter.blexecutor.helper;

import java.util.Hashtable;

import android.text.TextUtils;

import watch.oms.omswatch.application.OMSApplication;


/**
 * @author 245742
 *
 */
public class BLGlobalValueGenerator {

	@SuppressWarnings("unchecked")
	String getGlobalBLValue(String columnName){
		String globalBLVal="0.0";
		Hashtable<String,Object> tempGlobalHash= OMSApplication
				.getInstance().getGlobalBLHash();
		 if(tempGlobalHash != null && tempGlobalHash.get(columnName)!=null){
		 if(!TextUtils.isEmpty((String)""+tempGlobalHash.get(columnName))){
			 globalBLVal = (String)""+tempGlobalHash.get(columnName);
		 }
		 }
		return globalBLVal;
	}
}

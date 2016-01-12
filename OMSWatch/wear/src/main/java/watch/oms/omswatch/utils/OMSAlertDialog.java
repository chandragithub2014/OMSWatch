package watch.oms.omswatch.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class OMSAlertDialog {

	 public static  void displayAlertDialog(Context ctx,String message,String postiveButtonName){
		   
		   AlertDialog alertDialog = null ;
		      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
		      alertDialogBuilder.setMessage(message);
		      alertDialogBuilder.setPositiveButton(postiveButtonName, 
		      new DialogInterface.OnClickListener() {
				
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
		        	 arg0.dismiss();
					
		         }
		      });

		      alertDialog = alertDialogBuilder.create();
		      alertDialog.show();
	   }
	 
	 public static String getReadableDate(Long systemDate){
		 String readableDate="";
		 SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm");
        Date resultdate = new Date(systemDate);
        readableDate = sdf.format(resultdate);
        return readableDate;
	 }
	 
	 public static String getDataBaseName(String path){
		 String dbName="";
		 int lastIndex= path.lastIndexOf("/");
		 dbName =  path.substring(lastIndex+1, path.length());
		 return dbName;
	 }
}

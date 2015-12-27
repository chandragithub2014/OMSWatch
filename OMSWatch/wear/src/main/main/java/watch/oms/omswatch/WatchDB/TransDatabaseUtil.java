package watch.oms.omswatch.WatchDB;

import java.util.HashMap;
import java.util.Map;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CancellationSignal;
import android.util.Log;

import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSMessages;

public class TransDatabaseUtil {
	private static final String TAG = "TransDatabaseUtil";
	private static Context mContext = null;
	private static String appId = null;
	private static SQLiteDatabase transDB;
	private static int notifCounter = 1 ;
	private static Map<String,Boolean> mapNotificaiotns = new HashMap<String, Boolean>();
	
	private TransDatabaseUtil() {
	}

	public static SQLiteDatabase getTransDB() {
		if(transDB==null)
			openTransDBConnection();
		else if(!appId.equalsIgnoreCase(OMSApplication.getInstance().getAppId()))
		{
			appId = OMSApplication.getInstance().getAppId();
			transDB = null;
			openTransDBConnection();
		}
		return transDB;
	}
	
	public static void setTransDB(SQLiteDatabase db, Context context) {
		transDB  = db;
		mContext = context;
		appId = OMSApplication.getInstance().getAppId();
	}

	private static void openTransDBConnection() {
		String transDatabaseName = OMSApplication.getInstance()
				.getAppId() + OMSMessages.DB_EXT.getValue();
		Log.i(TAG, "Opening Trans DB Connection for " + transDatabaseName);
		try{
			if (transDB == null || !transDB.isOpen()) {
				transDB = SQLiteDatabase.openDatabase(
						mContext.getDatabasePath(transDatabaseName).toString(),
						null, SQLiteDatabase.OPEN_READWRITE);
			}
		} catch(Exception exception){

		}
	}
	
	public static long insert(String tableName,String nullColumnHack, ContentValues values) {
		long id = getTransDB().insert(tableName, null, values);
		/*if(id > 0){
			if(!mapNotificaiotns.containsKey(tableName) || mapNotificaiotns.get(tableName)) {
				createLocalNotification(tableName, values);
		   } 
		}*/
		return id;
	}
	
	public static int update(String table, ContentValues values,String whereClause, String[] whereArgs) {
		int count = getTransDB().update(table, values, whereClause, whereArgs);
		/*if(count > 0 ){
			if(!mapNotificaiotns.containsKey(table) || mapNotificaiotns.get(table)) {
				createLocalNotification(table, values);
		   } 
		}*/
		return count;
	}
	
	public static int deleteAllRecord(String tableName) {
		return getTransDB().delete(tableName, null, null);
	}

	public static int delete(String table, String whereClause, String[] whereArgs) {
		return   getTransDB().delete(table, whereClause, whereArgs);
	}

	public static Cursor query(boolean distinct, String table, 
			String[] columns, String selection, String[] selectionArgs, String groupBy, 
			String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
	      return getTransDB().query(distinct, table, columns, selection, selectionArgs,
	    		  groupBy, having, orderBy, limit, cancellationSignal);
	}
	
	
	public static Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having, String orderBy) {
	      return getTransDB().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, 
			String groupBy, String having, String orderBy, String limit) {
	      return getTransDB().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
	
	public static Cursor query(boolean distinct, String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
	      return getTransDB().query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
	      return getTransDB().rawQuery(sql, selectionArgs);
	}


	/*private static void createLocalNotification(String table, ContentValues values) {
		String contentText = "";
		boolean isLocalNotification = false;
		String colName;
		
		Cursor notifDetailsCursor = OMSDBManager
				.getConfigDB()
				.query("notificationqueue",
						null,
						"datatable"
								+ "="
								+ "'"
								+ table
								+ "'"
								+ " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID
								+ " = '"
								+ appId
								+ "'"
								+ " and "
								+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
								+ " <> '1'", null, null, null, null);
		
		
		if (notifDetailsCursor.moveToFirst()) {
			contentText = notifDetailsCursor.getString(notifDetailsCursor
					     .getColumnIndex("message"));		
			isLocalNotification = notifDetailsCursor.getInt(notifDetailsCursor
				     .getColumnIndex("islocal")) != OMSDefaultValues.MIN_INDEX_INT
								.getValue();
			
			if(isLocalNotification){
				Pattern pattern = Pattern.compile("\\{(.*?)\\}");
				Matcher matcher = pattern.matcher(contentText);
				
				while (matcher.find()) {
					colName = matcher.group(1);
					
					if(values.get(colName) != null)
						contentText = contentText.replace("{"+colName+"}", 
								values.get(colName).toString());
				}

				Log.i(TAG,"Notification Message:"+contentText);
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
						mContext);
				Intent intent = new Intent(mContext, OMSMainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		                Intent.FLAG_ACTIVITY_SINGLE_TOP);
				PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentTitle("One Mobile Studio");
				mBuilder.setContentText(contentText);
				mBuilder.setTicker("Message From OMS!");
				mBuilder.setSmallIcon(R.drawable.ic_core_logo);
				mBuilder.setContentIntent(pIntent);
				mBuilder.setAutoCancel(true);
		
				NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
				mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
		
				Notification notif = mBuilder.build();
				notif.defaults |= Notification.DEFAULT_SOUND;;
				notif.defaults |= Notification.DEFAULT_VIBRATE;
				notificationManager.notify(notifCounter++, notif);
			}
		}
		
		mapNotificaiotns.put(table, isLocalNotification);
	}*/
}

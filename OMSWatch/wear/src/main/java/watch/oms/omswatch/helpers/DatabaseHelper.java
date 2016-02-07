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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


public class DatabaseHelper {

	public static SQLiteDatabase launcherDatabase;
	public Context context;
	String databaseName = null;
	int databaseVersion;
	public OMSDBHelper dbhelper;
	public Cursor cursor = null;

	public DatabaseHelper(Context c, String dbName, int dbVersion) {
		context = c;
		databaseName = dbName;
		databaseVersion = dbVersion;
		dbhelper = new OMSDBHelper(context, databaseName, null, databaseVersion);

	}

	public void close() {
		launcherDatabase.close();
	}

	public SQLiteDatabase open() throws SQLiteException {
		try {
			launcherDatabase = dbhelper.getWritableDatabase();
			return launcherDatabase;
		} catch (SQLiteException ex) {
			Log.v("Open database exception caught in Database helper",
					ex.getMessage());
			ex.printStackTrace();
			launcherDatabase = dbhelper.getReadableDatabase();
		}
		return null;
	}

	/*public Cursor GetTableData(String tblName, String[] columns) {
		String query = "SELECT _id,";
		String prefix = "";
		for (String col : columns) {
			if (!col.equals(OMSDatabaseConstants.KEY_ROWID)) {
				query += prefix + col;
				prefix = OMSMessages.COMMA.getValue();
			}
		}
		query += " from " + tblName;
		Log.i("", "Query--" + query);
		Cursor cur = launcherDatabase.rawQuery(query, null);
		return cur;
	}

	public void AddNewEntryInDatabase(String itemName, String colName,
			String tableName) {
		ContentValues values = new ContentValues();
		values.put(colName, itemName);
		launcherDatabase.insert(tableName, null, values);
	}

	public void AddNewDetailInDatabase(String paramName, String value) {
		ContentValues values = new ContentValues();
		values.put(OMSMessages.PARAM_NAME.getValue(), paramName);
		values.put(OMSMessages.VALUE_NAME.getValue(), value);
		launcherDatabase.insert(DETAILS, null, values);
	}

	public int UpdateEntryInDatabase(String itemName, String colName,
			String tableName, int id) {
		ContentValues values = new ContentValues();
		values.put(colName, itemName);
		int rowCount = launcherDatabase.update(tableName, values, "_id=" + id,
				null);
		return rowCount;
	}

	public Cursor SearchInDatabase(String itemName, String colName,
			String tableName) {
		String query = "SELECT * from " + tableName + " WHERE " + colName
				+ "='" + itemName + "';";
		Cursor cur = launcherDatabase.rawQuery(query, null);
		return cur;
	}

	public Cursor getItemDetailFromDB(String colName, String tblName, int id) {
		String query = "SELECT _id," + colName + " from " + tblName
				+ " WHERE _id=" + id;
		Log.i(TAG, "getItemDetailFromDB---query--" + query);
		Cursor cur = launcherDatabase.rawQuery(query, null);
		cur.moveToFirst();
		return cur;

	}*/
}

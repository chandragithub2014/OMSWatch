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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class OMSDBHelper extends SQLiteOpenHelper {

	private final String TAG = this.getClass().getSimpleName();

	public OMSDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		Log.v(TAG, "name-------" + name + "---version---" + version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(TAG, "Creating all the tables");
		try {
			Log.i(TAG, "Creating all the tables"
					+ OMSTransDBGenerator.tableQueryArray.length);
			for (String query : OMSTransDBGenerator.tableQueryArray) {
				db.execSQL(query);
			}
		} catch (SQLiteException ex) {
			Log.v(TAG,
					"Error Occured in onCreate(),Error is " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int i = 0; i < OMSTransDBGenerator.tableList.size(); i++) {
			if (OMSTransDBGenerator.tableList.get(i).version > oldVersion) {
				db.execSQL("drop table if exists "
						+ OMSTransDBGenerator.tableList.get(i).tbName);
				db.execSQL(OMSTransDBGenerator.tableQueryArray[i]);
			}
		}
	}
}
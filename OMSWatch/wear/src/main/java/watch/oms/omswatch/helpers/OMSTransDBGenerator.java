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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.OMSDTO.ColumnDTO;
import watch.oms.omswatch.OMSDTO.DatabaseDTO;
import watch.oms.omswatch.OMSDTO.TableDTO;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.parser.OMSDBParserHelper;


public class OMSTransDBGenerator {
	private Context context;
	private String xmlResponse = null;
	private ArrayList<DatabaseDTO> getList = new ArrayList<DatabaseDTO>();
	private String databaseName = null;
	private int databaseVersion;
	private String tableCreateQuery = null;
	public static String[] tableQueryArray;
	public static ArrayList<TableDTO> tableList = null;
	private ArrayList<ColumnDTO> colList = null;
	private DatabaseHelper dbHelper;
	private final String TAG = "OMSTransDBGenerator";

	public OMSTransDBGenerator(Context c) {
		context = c;

	}


	private SharedPreferences sp =null;

	public void createTransDataBase(String tansDBResponse,int appID) {

		sp =context.getSharedPreferences("TRANS_DB_COLS", 0);
		SharedPreferences.Editor editor= sp.edit();

		OMSDBParserHelper configTransDBHelper = new OMSDBParserHelper();
		int versionNumber = configTransDBHelper.getVersionNumber(appID);
		OMSTransXmlParser dbXmlParser = new OMSTransXmlParser(context);
		xmlResponse = tansDBResponse;

		List<String> colums = null;		
		try {
			getList = dbXmlParser.parseTransDBXML(xmlResponse);

		} catch (XmlPullParserException e) {
			Log.e(TAG,
					"Error occurred in method createTransDataBase . Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG,
					"IOException  occurred in method createTransDataBase . Error is:"
							+ e.getMessage());
			e.printStackTrace();
		}
		if(getList == null || getList.isEmpty()){
			SQLiteDatabase.openOrCreateDatabase(OMSApplication.getInstance()
					.getDatabasePath(appID + ".db").toString(), null);
			Log.d(TAG, "There no tables schema available in the list, So created Empty database");
			return;
		}


		databaseName = getList.get(0).dbName + OMSMessages.DB_EXT.getValue();
		databaseVersion = Integer.parseInt(getList.get(0).dbVersion);
		TableDTO tableDTO = null;
		String tblName = null;
		Log.d(TAG, "Trans DB XML[ DataBase Server Version: "+databaseVersion+", Local Version: "+versionNumber+"]");		
		if (versionNumber < databaseVersion) {
			tableList = new ArrayList<TableDTO>();
			tableList = getList.get(0).tableDTOList;
			tableQueryArray = new String[tableList.size()];
			for (int i = 0; i < tableList.size(); i++) {
				String prefix = "";
				boolean isUsidExist = false;
				tableDTO = tableList.get(i);
				tblName = tableDTO.tbName;
				Log.d(TAG, "Trans DB XML[ Table Name: "+tblName+", Version: "+tableDTO.version+"]");	

				if(tableDTO.version > 0){

					isUsidExist = false;
					colList = new ArrayList<ColumnDTO>();
					colList = tableDTO.columnDTOList;
					tableCreateQuery = "create table IF NOT EXISTS " + tblName
							+ OMSMessages.OPEN_BRACES.getValue();

					colums = new ArrayList<String>();
					for (int j = 0; j < colList.size(); j++) {

						if(colList.get(j).name.equalsIgnoreCase("usid"))
							isUsidExist = true;

						tableCreateQuery += prefix + " " + colList.get(j).name
								+ " " + colList.get(j).type;
						if (colList.get(j).Constraint != null) {
							tableCreateQuery += " " + colList.get(j).Constraint;
						}

						//Log.d(TAG, "Default Value :: "+colList.get(j).defaultVal);

						if(TextUtils.isEmpty(colList.get(j).defaultVal)){
							if(colList.get(j).type.equalsIgnoreCase("REAL")){
								tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
										+ " " + " 0.0";
							}else if(colList.get(j).type.equalsIgnoreCase("INTEGER")){
								tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
										+ " " + " 0";
							}else if(colList.get(j).type.equalsIgnoreCase("DATE")){
								tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
										+ " " + "(strftime('%Y-%m-%d',date('now','localtime')))";
							}else if(colList.get(j).type.equalsIgnoreCase("TIME")){
								tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
										+ " " + "(strftime('%H:%M:%S',time('now','localtime')))";
							} else{
								tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
										+ " " + "''";
							}
						}else{
							if(colList.get(j).type.equalsIgnoreCase("DATE")){
								tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
										+ " " + "(strftime('%Y-%m-%d',date('now','localtime')))";
							}else if(colList.get(j).type.equalsIgnoreCase("TIME")){
								tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
										+ " " + "(strftime('%H:%M:%S',time('now','localtime')))";
							}else if(colList.get(j).type.equalsIgnoreCase("TEXT")){
								if(!(colList.get(j).defaultVal.indexOf('\'') > -1))
								{
									tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
											+ " '" +colList.get(j).defaultVal.trim()+"'";
								}
								else
								{
									tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
											+ " " +colList.get(j).defaultVal.trim()+"";
								}
							} else{
								tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
										+ " " +colList.get(j).defaultVal.trim()+"";
							}
						}

					/*	if (colList.get(j).defaultVal != null
							&& !colList.get(j).defaultVal.trim().equals("")) {
						tableCreateQuery += " " + OMSMessages.DEFAULT.getValue()
								+ " " + OMSMessages.MIN_INDEX_STR.getValue();
					}*/

						prefix = OMSMessages.COMMA.getValue();

						colums.add(colList.get(j).name);
					}


					if(!isUsidExist){
						tableCreateQuery += OMSMessages.COMMA.getValue() + " "
								+ OMSDatabaseConstants.UNIQUE_ROW_ID + " " + "BIGINT" + " ";
					}


					tableCreateQuery += OMSMessages.COMMA.getValue() + " "
							+ OMSDatabaseConstants.KEY_ROWID + " " + "INTEGER" + " "
							+ "PRIMARY" + " " + "KEY" + " " + "AUTOINCREMENT";


					tableCreateQuery += ");";

					Log.d(TAG, "isUsidExist :: "+isUsidExist);

				}else{
					tableCreateQuery ="DROP TABLE IF EXISTS '" +tblName+ "'";
					colums = new ArrayList<String>();
				}


				Log.d(TAG, "Table Query :: "+tableCreateQuery);

				tableQueryArray[i] = tableCreateQuery;

				editor.putStringSet(tblName, new HashSet<String>(colums));			
			}
			editor.commit();

			dbHelper = new DatabaseHelper(context, databaseName,
					databaseVersion);
			dbHelper.open();
			dbHelper.close();
			int insertedId = configTransDBHelper.insertOrUpdateVersionNumber(
					databaseVersion, appID);
			Log.d(TAG, "insertOrUpdateVersionNumber-" + insertedId);

		}

	}
}

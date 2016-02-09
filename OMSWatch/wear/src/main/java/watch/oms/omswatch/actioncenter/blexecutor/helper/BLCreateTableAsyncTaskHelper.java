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
package watch.oms.omswatch.actioncenter.blexecutor.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import watch.oms.omswatch.OMSDTO.ColumnDTO;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLCreateTableDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLTableColumnDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLTableCreateTableDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLTableDBDTO;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;
import watch.oms.omswatch.R;


/**
 * @author 245742 BLCreateTableAsyncTaskHelper : OMS BL for dynamic Table
 *         Creation
 */

public class BLCreateTableAsyncTaskHelper extends
		AsyncTask<String, Void, String> {

	private final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private OMSReceiveListener rListener;
	String uniqueId = null;
	ProgressDialog pDialog;
	List<BLCreateTableDTO> createTableList;
	private List<String> transUsidList;

	public BLCreateTableAsyncTaskHelper(Activity FragmentContext,
			int containerId, OMSReceiveListener receiveListener,
			List<String> transUsidList) {
		context = FragmentContext;
		rListener = receiveListener;
		this.transUsidList = transUsidList;
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getResources().getString(
				R.string.bl_table_create));
		pDialog.show();

	}

	@Override
	protected String doInBackground(String... args) {
		boolean result = false;
		String xml_file = args[0];
		result = processCreateTableBL(xml_file);
		
		return Boolean.toString(result);
	}

	/**
	 * @param xmlFile
	 * @return boolean This method processes the parses and processes the
	 *         BLCreate xml
	 */
	private boolean processCreateTableBL(String xmlFile) {
		boolean result = false;
		try {
			String response = xmlFile;
			BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
			createTableList = xmlParser.parseBLCreateTable(response);
			if (createTableList != null) {
				result = processBLCreateTableList(createTableList);
			}
		} catch (XmlPullParserException e) {
			Log.e(TAG, "Error Occured in processCreateTableBL" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "Error Occured in processCreateTableBL" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param blTableList
	 * @return booelan Process the parsed BLCreate XML file and create or alter
	 *         new table if required
	 */
	private boolean processBLCreateTableList(List<BLCreateTableDTO> blTableList) {
		boolean result = false;
		boolean tableExists = false;
		String tableCreateQuery = null;
		String prefix = "";
		BLTableDBDTO tempBLCreateDBData = blTableList.get(0).dbDto;
		BLTableCreateTableDTO tempBLCreateTableData = blTableList.get(0).dbtableDTO;
		List<BLTableColumnDTO> tempBLCreateColumnDataList = blTableList.get(0).columnDTOList;

		String schemaName = processDBTagData(tempBLCreateDBData);
		String tableName = processTableTagData(tempBLCreateTableData);
		if (schemaName != null && tableName != null && schemaName.length() > 0
				&& tableName.length() > 0) {
			tableExists = checkForTableExistence(schemaName, tableName);
			if (!tableExists) {
				// create Table
				List<ColumnDTO> columnDTOList = processColumnTagList(
						tempBLCreateColumnDataList, schemaName);
				tableCreateQuery = "create table " + tableName + "(";
				for (int j = 0; j < columnDTOList.size(); j++) {

					tableCreateQuery += prefix + " "
							+ columnDTOList.get(j).name + " "
							+ columnDTOList.get(j).type;
					if (columnDTOList.get(j).Constraint != null) {
						tableCreateQuery += " "
								+ columnDTOList.get(j).Constraint;
					}
					if (columnDTOList.get(j).defaultVal != null
							&& !columnDTOList.get(j).defaultVal.trim().equals(
									"")) {
						tableCreateQuery += " " + "default" + " "
								+ OMSMessages.MIN_INDEX_STR.getValue();

					}
					prefix = OMSMessages.COMMA.getValue();
				}
				tableCreateQuery += OMSMessages.COMMA.getValue() + " "
						+ OMSDatabaseConstants.KEY_ROWID + " " + "INTEGER"
						+ " " + "PRIMARY" + " " + "KEY" + " " + "AUTOINCREMENT"
						+ " " + OMSMessages.COMMA.getValue() + " "
						+ OMSDatabaseConstants.UNIQUE_ROW_ID + " " + "TEXT"
						+ " " + OMSMessages.COMMA.getValue() + " "
						+ OMSDatabaseConstants.IS_DELETE + " " + "INTEGER"
						+ " " + "DEFAULT 0" + " "
						+ OMSMessages.COMMA.getValue() + " "
						+ OMSDatabaseConstants.MODIFIED_DATE + " " + "REAL"
						+ " " + OMSMessages.COMMA.getValue() + " "
						+ OMSDatabaseConstants.TRANS_TABLE_STATUS_COLUMN + " "
						+ "TEXT" + " ";

				tableCreateQuery += ");";
				if (tableCreateQuery != null) {
					OMSDBManager.getSpecificDB(schemaName).execSQL(
							tableCreateQuery);
				}
				result = true;
			} else {
				List<ColumnDTO> columnDTOList = processColumnTagList(
						tempBLCreateColumnDataList, schemaName);
				if (columnDTOList != null && columnDTOList.size() > 0) {
					List<ColumnDTO> targetColumnDTO = getUnMatchedNewColumnList(
							schemaName, tableName, columnDTOList);
					if (targetColumnDTO != null && targetColumnDTO.size() > 0) {
						alterTableWithNewColumns(schemaName, tableName,
								targetColumnDTO);
					}

				}
				result = true;
			}
		}
		return result;
	}

	/**
	 * @param schemaName
	 * @param tableName
	 * @param targetColumnDTO
	 *            Alters the table with new columns if table exists
	 */
	private void alterTableWithNewColumns(String schemaName, String tableName,
			List<ColumnDTO> targetColumnDTO) {
		String alterTableQuery = null;
		// alterTableQuery = "ALTER TABLE " + tableName + "  ADD COLUMN";
		for (int j = 0; j < targetColumnDTO.size(); j++) {
			alterTableQuery = "ALTER TABLE " + tableName + "  ADD COLUMN";
			String tempAlterQuery = targetColumnDTO.get(j).name + " "
					+ targetColumnDTO.get(j).type;
			alterTableQuery = alterTableQuery + " " + tempAlterQuery;
			OMSDBManager.getSpecificDB(schemaName).execSQL(alterTableQuery);
			alterTableQuery = "";

		}
	}

	/**
	 * @param schemaName
	 * @param tableName
	 * @param columnDTOList
	 * @return List Forms the list of unMatchedColumn details to alter the table
	 * 
	 */
	private List<ColumnDTO> getUnMatchedNewColumnList(String schemaName,
			String tableName, List<ColumnDTO> columnDTOList) {
		// Remove duplicates
		/*
		 * List <ColumnDTO> tempDTOList = columnDTOList; List<ColumnDTO>
		 * nonDupList = new ArrayList<ColumnDTO>(); Iterator<ColumnDTO> dupIter
		 * = tempDTOList.iterator(); while(dupIter.hasNext()) { ColumnDTO
		 * dupWord = dupIter.next(); if(nonDupList.contains(dupWord)) {
		 * dupIter.remove(); }else { nonDupList.add(dupWord); } }
		 */

		// columnDTOList = nonDupList;

		boolean isMatched = false;
		Cursor sourcePragmaCursor = null;
		List<ColumnDTO> targetColumnDTO = new ArrayList<ColumnDTO>();
		List<String> pragmaColumnList = new ArrayList<String>();
		sourcePragmaCursor = OMSDBManager.getSpecificDB(schemaName).rawQuery(
				"pragma table_info(" + tableName + ");", null);
		if (sourcePragmaCursor.moveToFirst()) {
			do {
				pragmaColumnList
						.add(sourcePragmaCursor.getString(sourcePragmaCursor
								.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)));
			} while (sourcePragmaCursor.moveToNext());

			for (int i = 0; i < columnDTOList.size(); i++) {
				for (int j = 0; j < pragmaColumnList.size(); j++) {
					if (columnDTOList.get(i).name.trim().equalsIgnoreCase(
							pragmaColumnList.get(j).trim())) {
						isMatched = true;
						break;
					} else {
						isMatched = false;
					}
				}
				if (!isMatched) {
					targetColumnDTO.add(columnDTOList.get(i));
				}
			}
		}

		/*
		 * if (sourcePragmaCursor.moveToFirst()) { do { for (int i = 0; i <
		 * columnDTOList.size(); i++) { if (sourcePragmaCursor .getString(
		 * sourcePragmaCursor
		 * .getColumnIndex(DatabaseConstants.PRAGMA_COLUMN_NAME1))
		 * .equalsIgnoreCase(columnDTOList.get(i).name)) {
		 * 
		 * } else { targetColumnDTO.add(columnDTOList.get(i)); } } } while
		 * (sourcePragmaCursor.moveToNext()); }
		 */
		return targetColumnDTO;
	}

	/**
	 * @param schemaName
	 * @param tableName
	 * @return boolean Check whether the table exists or not to creat new table
	 *         or alter
	 */
	private boolean checkForTableExistence(String schemaName, String tableName) {
		boolean tableExists = false;
		Cursor tableExistsCursor = null;
		try {
			tableExistsCursor = OMSDBManager.getSpecificDB(schemaName).query(
					OMSDatabaseConstants.SQLITE_MASTER_TABLE_NAME,
					null,
					OMSDatabaseConstants.SQLITE_MASTER_TABLE_COLUMN_NAME + "='"
							+ tableName + "'", null, null, null, null);
			if (tableExistsCursor.moveToFirst()) {
				tableExists = true;
			}
		} catch (SQLException e) {
			Log.e(TAG,
					"Error Occured in checkForTableExistence" + e.getMessage());
			e.printStackTrace();
		}
		tableExistsCursor.close();
		return tableExists;
	}

	/**
	 * @param tempBLCreateDBData
	 * @return String Processes the DB Tag of BLCreate XML and returns the
	 *         schema name
	 */
	private String processDBTagData(BLTableDBDTO tempBLCreateDBData) {
		Object schemaName = null;
		String dbTagDataColumnName = tempBLCreateDBData.columnName;
		String dbTagDataColumnValue = tempBLCreateDBData.columnvalue;
		String dbTagDataSchemaName = tempBLCreateDBData.schemaname;
		String dbTagDatatablename = tempBLCreateDBData.tablename;
		String columnType = tempBLCreateDBData.columnType;
		if (dbTagDataColumnName.equalsIgnoreCase(OMSMessages.CONSTANT
				.getValue())) {
			dbTagDataColumnName = dbTagDataColumnValue;
		}
		for (int k = 0; k < transUsidList.size(); k++) {
			uniqueId = transUsidList.get(k);
			Cursor schemaCursor = null;
			try {
				schemaCursor = OMSDBManager
						.getSpecificDB(dbTagDataSchemaName)
						.query(dbTagDatatablename,
								null,
								OMSDatabaseConstants.UNIQUE_ROW_ID
										+ "='"
										+ uniqueId
										+ "'"
										+ "AND "
										+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
										+ " <> '1'", null, null, null, null);
				if (schemaCursor.moveToFirst()) {
					if (columnType.equalsIgnoreCase(OMSMessages.INTEGER
							.getValue())) {
						schemaName = schemaCursor.getInt(schemaCursor
								.getColumnIndex(dbTagDataColumnName));
					} else if (columnType.equalsIgnoreCase(OMSMessages.TEXT
							.getValue()) || columnType.equalsIgnoreCase(OMSMessages.BIGINT
									.getValue())) {
						schemaName = schemaCursor.getString(schemaCursor
								.getColumnIndex(dbTagDataColumnName));
					}
				}
			} catch (SQLException e) {
				Log.e(TAG, "Error Occured in processDBTagData" + e.getMessage());
				e.printStackTrace();

			}
			schemaCursor.close();
		}
		return schemaName.toString();
	}

	/**
	 * @param tempTableTagData
	 * @return String Process the Table tag of BLCreate XML and returns the
	 *         table name
	 */
	private String processTableTagData(BLTableCreateTableDTO tempTableTagData) {
		Object tableName = null;
		String dbTableDataColumnName = tempTableTagData.columnName;
		String dbTableDataColumnValue = tempTableTagData.columnvalue;
		String dbTableDataSchemaName = tempTableTagData.schemaname;
		String dbTableDatatablename = tempTableTagData.tablename;
		String columnType = tempTableTagData.columnType;
		if (dbTableDataColumnName.equalsIgnoreCase(OMSMessages.CONSTANT
				.getValue())) {
			dbTableDataColumnName = dbTableDataColumnValue;
		}
		for (int k = 0; k < transUsidList.size(); k++) {
			uniqueId = transUsidList.get(k);
			Cursor tableCursor = null;
			try {
				tableCursor = OMSDBManager
						.getSpecificDB(dbTableDataSchemaName)
						.query(dbTableDatatablename,
								null,
								OMSDatabaseConstants.UNIQUE_ROW_ID
										+ "='"
										+ uniqueId
										+ "'"
										+ "AND "
										+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
										+ " <> '1'", null, null, null, null);
				if (tableCursor.moveToFirst()) {
					if (columnType.equalsIgnoreCase(OMSMessages.INTEGER
							.getValue())) {
						tableName = tableCursor.getInt(tableCursor
								.getColumnIndex(dbTableDataColumnName));
					} else if (columnType.equalsIgnoreCase(OMSMessages.TEXT
							.getValue()) || columnType.equalsIgnoreCase(OMSMessages.BIGINT
									.getValue())) {
						tableName = tableCursor.getString(tableCursor
								.getColumnIndex(dbTableDataColumnName));
					}
				}
			} catch (SQLException e) {
				Log.e(TAG,
						"Error Occured in processTableTagData" + e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				Log.e(TAG,
						"Error Occured in processTableTagData" + e.getMessage());
				e.printStackTrace();
			}
			tableCursor.close();
		}
		return tableName.toString();
	}

	/**
	 * @param tempBLCreateColumnDataList
	 * @param schemaName
	 * @param
	 * @return List Process the column tag to generate column list
	 */
	private List<ColumnDTO> processColumnTagList(
			List<BLTableColumnDTO> tempBLCreateColumnDataList, String schemaName) {
		List<ColumnDTO> columnDTOList = new ArrayList<ColumnDTO>();
		Object columnName = null;
		for (int k = 0; k < transUsidList.size(); k++) {
			uniqueId = transUsidList.get(k);
			for (int i = 0; i < tempBLCreateColumnDataList.size(); i++) {
				Cursor columnCursor = null;
				String tableName = tempBLCreateColumnDataList.get(i).tablename;
				schemaName = tempBLCreateColumnDataList.get(i).schemaname;
				columnCursor = OMSDBManager
						.getSpecificDB(schemaName)
						.query(tableName,
								null,
								OMSDatabaseConstants.UNIQUE_ROW_ID
										+ "='"
										+ uniqueId
										+ "'"
										+ "AND "
										+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
										+ " <> '1'", null, null, null, null);
				if (columnCursor.moveToFirst()) {
					ColumnDTO tempColumnDTO = new ColumnDTO();
					if (tempBLCreateColumnDataList.get(i).columnType
							.equalsIgnoreCase(OMSMessages.INTEGER.getValue())) {
						columnName = columnCursor.getInt(columnCursor
								.getColumnIndex(tempBLCreateColumnDataList
										.get(i).columnName));
					} else if (tempBLCreateColumnDataList.get(i).columnType
							.equalsIgnoreCase(OMSMessages.TEXT.getValue()) || 
							tempBLCreateColumnDataList.get(i).columnType
							.equalsIgnoreCase(OMSMessages.BIGINT.getValue())) {
						columnName = columnCursor.getString(columnCursor
								.getColumnIndex(tempBLCreateColumnDataList
										.get(i).columnName));
					}
					tempColumnDTO.name = columnName.toString();
					tempColumnDTO.type = OMSMessages.TEXT.getValue();
					tempColumnDTO.Constraint = null;
					tempColumnDTO.defaultVal = null;
					if (columnName.toString().length() > 0
							&& columnName.toString() != null) {
						columnDTOList.add(tempColumnDTO);
					}
				}
				columnCursor.close();
			}
		}
		return columnDTOList;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result.equalsIgnoreCase(OMSMessages.TRUE.getValue())) {
			if (rListener != null) {
				pDialog.dismiss();
				rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
			}
		} else {
			if (rListener != null) {
				pDialog.dismiss();
				rListener.receiveResult(OMSMessages.BL_FAILURE.getValue());
			}
		}

		Log.d("TAG", "Result BLCreateTableAsyncTaskHelper:::" + result);
		super.onPostExecute(result);
	}
}

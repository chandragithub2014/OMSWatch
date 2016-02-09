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
package watch.oms.omswatch.actioncenter.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;


/**
 * @author 245742 This helper class is used to performs actions fired from UI
 *         component like Buttons, List items etc
 */
public class ActionCenterHelper {
	private final String TAG = this.getClass().getSimpleName();
	public Context context;
	// This changes are for Oasis Project. # Start
	//String oasisSchemaName = "127645";
	// This changes are for Oasis Project. # End

	public ActionCenterHelper(Context appContext) {
		context = appContext;
	}

	/**
	 * Populates the action queue table with the data obtained from Action Table
	 * 
	 * @param usid
	 * @param buttonId
	 */
	public void getFromActionTableAndUpdateActionQueue(String usid,
			int buttonId, int appId) {
		Cursor actionCursor = null;
		Calendar calendar = Calendar.getInstance();
		List<DoAction> actionList  = new ArrayList<DoAction>();

		try {
			actionCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.ACTION_TABLE_NAME,
					null,
					OMSDatabaseConstants.ACTION_TABLE_NAVIGATION_UNIQUE_ID + "='"
							+ usid + "' and "
							+ OMSDatabaseConstants.ACTION_TABLE_BUTTON_ID + "= "
							+ buttonId + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"
							+ " and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null,
					OMSDatabaseConstants.ACTION_TABLE_ACTION_ORDER);
			ContentValues contentValues = new ContentValues();
			if (actionCursor.getCount() > 0) {
				if (actionCursor.moveToFirst()) {
					do {
						contentValues
								.put(OMSDatabaseConstants.ACTION_QUEUE_TYPE,
										actionCursor.getString(actionCursor
												.getColumnIndex(OMSDatabaseConstants.ACTION_TYPE)));
						contentValues
								.put(OMSDatabaseConstants.ACTION_UNIQUE_ID,
										actionCursor.getString(actionCursor
												.getColumnIndex(OMSDatabaseConstants.ACTION_TABLE_ACTION_ID)));
						contentValues.put(
								OMSDatabaseConstants.ACTION_QUEUE_STATUS,
								OMSDatabaseConstants.ACTION_STATUS_NEW);
						contentValues
								.put(OMSDatabaseConstants.ACTION_QUEUE_PRIORITY,
										actionCursor.getInt(actionCursor
												.getColumnIndex(OMSDatabaseConstants.ACTION_TABLE_ACTION_ORDER)));
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(calendar.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.CONFIGDB_APPID,
								appId);
						insertOrUpdateActionQueue(contentValues,
								OMSMessages.MIN_INDEX_STR.getValue(), appId);
						
						DoAction tempAction = new DoAction();
						tempAction.actionType = actionCursor.getString(actionCursor
								.getColumnIndex(OMSDatabaseConstants.ACTION_TYPE));
						tempAction.actionUsid = actionCursor.getString(actionCursor
								.getColumnIndex(OMSDatabaseConstants.ACTION_TABLE_ACTION_ID));
						tempAction.uniqueRowId = actionCursor.getString(actionCursor
								.getColumnIndex("usid"));
						actionList.add(tempAction);
					} while (actionCursor.moveToNext());
				}
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error in method getFromActionTableAndUpdateActionQueue. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			actionCursor.close();
		}
	}

	/**
	 * insert a new entry or update status in ActionQueue table
	 * 
	 * @param contentValues
	 * @param uniqueID
	 * @return
	 */
	public int insertOrUpdateActionQueue(ContentValues contentValues,
			String uniqueID, int appId) {
		int rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Cursor currentActionCursor = null;
		try {
			currentActionCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.ACTION_QUEUE_TABLE_NAME,
					null,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "=" + "'" + uniqueID
							+ "'" + " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'", null, null, null, null);
			if (currentActionCursor.moveToFirst()) {
				rowId = OMSDBManager.getConfigDB().update(
						OMSDatabaseConstants.ACTION_QUEUE_TABLE_NAME,
						contentValues,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "= ? " + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
								+ appId + "'", new String[] { uniqueID });
			} else {
				rowId = (int) OMSDBManager.getConfigDB().insert(
						OMSDatabaseConstants.ACTION_QUEUE_TABLE_NAME, null,
						contentValues);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateActionQueue for input parameter uniqueID["
							+ uniqueID + "]. Error is:" + e.getMessage());
			e.printStackTrace();

		} finally {
			currentActionCursor.close();
		}
		return rowId;
	}

	/**
	 * insert a new entry or update status in ActionQueue table using actionusid
	 * 
	 * @param contentValues
	 * @param actionUsid
	 * @return
	 */
	public int insertOrUpdateActionQueueByActionusid(
			ContentValues contentValues, String actionUsid, int appId) {
		int rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Cursor currentActionCursor = null;
		try {
			currentActionCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.ACTION_QUEUE_TABLE_NAME,
					null,
					OMSDatabaseConstants.ACTION_UNIQUE_ID + "=" + "'" + actionUsid
							+ "'" + " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'", null, null, null, null);
			if (currentActionCursor.moveToFirst()) {
				rowId = OMSDBManager.getConfigDB().update(
						OMSDatabaseConstants.ACTION_QUEUE_TABLE_NAME,
						contentValues,
						OMSDatabaseConstants.ACTION_UNIQUE_ID + "= ? " + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
								+ appId + "'", new String[] { actionUsid });
			} else {
				rowId = (int) OMSDBManager.getConfigDB().insert(
						OMSDatabaseConstants.ACTION_QUEUE_TABLE_NAME, null,
						contentValues);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateActionQueue for input parameter uniqueID["
							+ actionUsid + "]. Error is:" + e.getMessage());
			e.printStackTrace();

		} finally {
			currentActionCursor.close();
		}
		return rowId;
	}

	/**
	 * return cursor that contains rows with status "fresh"
	 * 
	 * @param status
	 * @return Cursor
	 */
	public Cursor getActionQueueCursorForStatus(String status) {
		Cursor actionQueueCursor = null;
		String selection=null;
		try {
			selection =OMSDatabaseConstants.ACTION_QUEUE_STATUS + " = " + "'" + status + "'"
					+ "or "
					+ OMSDatabaseConstants.ACTION_QUEUE_STATUS + " = " + "'" + OMSDatabaseConstants.ACTION_STATUS_TRIED + "'";
			actionQueueCursor = OMSDBManager.getConfigDB()
					.query(OMSDatabaseConstants.ACTION_QUEUE_TABLE_NAME,
							null,
							selection , null, null, null, null);
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getActionQueueCursorForStatus for input parameter status["
							+ status + "]. Error is:" + e.getMessage());
			e.printStackTrace();

		}
		return actionQueueCursor;
	}

	/**
	 * retrieve address to be sent
	 * 
	 * @param actionusId
	 * @return String[]
	 */
	public String[] getToAddressesForMail(String actionusId, int appId) {
		String toAddrs[] = null;
		Cursor toAddrCursor = null;
		int cursorCount = OMSDefaultValues.NONE_DEF_CNT.getValue();
		;
		int i = OMSDefaultValues.MIN_INDEX_INT.getValue();
		try {
			toAddrCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.MAIL_TABLE_NAME,
					null,
					OMSDatabaseConstants.MAIL_PARENT_USID + "=" + "'" + actionusId
							+ "'" + " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'"+ " and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null, null);
			cursorCount = toAddrCursor.getCount();
			if (cursorCount > 0) {
				toAddrs = new String[cursorCount];
				if (toAddrCursor.moveToFirst()) {
					do {
						toAddrs[i] = toAddrCursor
								.getString(toAddrCursor
										.getColumnIndex(OMSDatabaseConstants.MAIL_TO_ADDRESS));
						i++;
					} while (toAddrCursor.moveToNext() && i < cursorCount);
				}
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getToAddressesForMail for input parameter actionusId["
							+ actionusId + "]. Error is:" + e.getMessage());
			e.printStackTrace();

		} finally {
			toAddrCursor.close();
		}
		return toAddrs;
	}

	/**
	 * retrieve email subject and body for given actionusid
	 * 
	 * @param actionusId
	 * @return List<String>
	 */
	public List<String> getSubjectAndBodyForMail(String actionusId, int appId) {
		List<String> mailDataList = new ArrayList<String>();
		Cursor mailDataCursor = null;
		String subject = null;
		String messagebody = null;
		try {
			mailDataCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.MAIL_TABLE_NAME,
					null,
					OMSDatabaseConstants.MAIL_PARENT_USID + "=" + "'" + actionusId
							+ "'" + " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'"+ " and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null, null);
			if (mailDataCursor.moveToFirst()) {
				subject = mailDataCursor.getString(mailDataCursor
						.getColumnIndex(OMSDatabaseConstants.MAIL_SUBJECT));
				messagebody = mailDataCursor.getString(mailDataCursor
						.getColumnIndex(OMSDatabaseConstants.MAIL_MESSAGE_BODY));
				mailDataList.add(subject);
				mailDataList.add(messagebody);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getSubjectAndBodyForMail for input parameter actionusId["
							+ actionusId + "]. Error is:" + e.getMessage());
			e.printStackTrace();

		} finally {
			mailDataCursor.close();
		}
		return mailDataList;
	}

	/**
	 * fetch contact number
	 * 
	 * @param actionusId
	 * @return String
	 */
	public String getContactNumber(String actionusId, int appId) {
		String contactNum = null;
		Cursor contactCursor = null;
		try {
			contactCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.CONTACTS_TABLE_NAME,
					null,
					OMSDatabaseConstants.CONTACTS_PARENT_USID + "=" + "'"
							+ actionusId + "'" + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"+ " and "
									+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			if (contactCursor.moveToFirst()) {
				contactNum = contactCursor.getString(contactCursor
						.getColumnIndex(OMSDatabaseConstants.CONTACTS_TEL_NUMBER));
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getContactNumber for input parameter actionusId["
							+ actionusId + "]. Error is:" + e.getMessage());
			e.printStackTrace();

		} finally {
			contactCursor.close();
		}
		return contactNum;
	}

	/**
	 * Returns the list from Post table for parentActionUsid
	 * 
	 * @param parentActionUsid
	 * @return List<String>
	 */
	public List<String> getPostData(String parentActionUsid, int appId) {
		Cursor postCursor = null;
		List<String> postDataList = new ArrayList<String>();
		try {
			postCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.POST_TABLE_NAME,
					null,
					OMSDatabaseConstants.POST_PARENT_USID + "=" + "'"
							+ parentActionUsid + "'" + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"+ " and "
									+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			if (postCursor.moveToFirst()) {
				postDataList.add(postCursor.getString(postCursor
						.getColumnIndex(OMSDatabaseConstants.POST_DB_NAME)));
				postDataList.add(postCursor.getString(postCursor
						.getColumnIndex(OMSDatabaseConstants.POST_TO_TABLE_NAME)));
				postDataList.add(postCursor.getString(postCursor
						.getColumnIndex(OMSDatabaseConstants.POST_URL)));

			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getPostData for input parameter parentActionUsid["
							+ parentActionUsid + "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			postCursor.close();
		}
		return postDataList;

	}

	/**
	 * Returns the list that contains the data obtained from tableName(table) of
	 * dbName(database) received from action queue to be posted to server
	 * 
	 * @param dbName
	 * @param tableName
	 * @return JSONObject
	 */
	public JSONObject formJSONPayLoad(String dbName, String tableName) {
		String targetTableName = null;
		if (tableName != null) {
			targetTableName = Character.toLowerCase(tableName.charAt(0))
					+ tableName.substring(1);
			// This changes are for Oasis Project. # Start					
			if(tableName.equalsIgnoreCase("Products")) 
				targetTableName = tableName;
			// This changes are for Oasis Project. # END
		}
		
		JSONObject manJson = new JSONObject();
		Cursor pragmaCursor = null;
		Cursor postToCursor = null;

		List<String> colNamesList = new ArrayList<String>();
		List<String> colTypeList = new ArrayList<String>();
		try {
			//pragmaCursor = TransDatabaseUtil.rawQuery(
					pragmaCursor = OMSDBManager.getSpecificDB(dbName).rawQuery(
					"pragma table_info(" + tableName + ");", null);
			for (boolean hasItem = pragmaCursor.moveToFirst(); hasItem; hasItem = pragmaCursor
					.moveToNext()) {
				colNamesList
						.add(pragmaCursor.getString(pragmaCursor
								.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)));
				colTypeList
						.add(pragmaCursor.getString(pragmaCursor
								.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2)));

			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getActionQueueCursorForStatus for input parameter dbName["
							+ dbName
							+ "] and tableName["
							+ tableName
							+ "]. Error is:" + e.getMessage());
			e.printStackTrace();

		} finally {
			pragmaCursor.close();
		}

		try {
			//postToCursor = TransDatabaseUtil.query(
					postToCursor = OMSDBManager.getSpecificDB(dbName).query(
					tableName,
					null,
					OMSDatabaseConstants.TRANS_TABLE_STATUS_COLUMN + "='"
							+ OMSDatabaseConstants.STATUS_TYPE_TRIED + "'"
							+ " OR "
							+ OMSDatabaseConstants.TRANS_TABLE_STATUS_COLUMN
							+ "='" + OMSDatabaseConstants.STATUS_TYPE_NEW + "'",/*+ " and "
									+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'",*/
					null, null, null, null);
			if(postToCursor!=null)
			if (postToCursor.getCount() > 0) {
				if (postToCursor.moveToFirst()) {
					JSONArray jArray = new JSONArray();
					do {
						JSONObject rawJsonObject = new JSONObject();

						for (int i = 0; i < colNamesList.size(); i++) {
							String value = null;
							if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.STRING_TYPE))
								value = postToCursor.getString(postToCursor
										.getColumnIndex(colNamesList.get(i)));
							else if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.INTEGER_TYPE))
								value = ""
										+ postToCursor.getInt(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));
							else if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.LONG_TYPE))
								value = ""
										+ postToCursor.getLong(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));
							else if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.DOUBLE_TYPE))
								value = ""
										+ postToCursor.getDouble(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));
							else if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.REAL_TYPE))
								value = ""
										+ postToCursor.getDouble(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));
							else
								value = ""
										+ postToCursor.getString(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));
							try {
								rawJsonObject.put(colNamesList.get(i), value);
							} catch (JSONException e) {
								Log.e(TAG,
										"Error occurred in method formJSONPayLoad . Error is:"
												+ e.getMessage());
								e.printStackTrace();

							}
						}
						// This changes are for Oasis Project. # Start
//						if(targetTableName.equalsIgnoreCase("Products"))
//						{
//							if(rawJsonObject.getString("sysselcategory").equalsIgnoreCase("ISBN"))
//								rawJsonObject.put("ISBN", rawJsonObject.getString("sysselvalue"));
//							else if(rawJsonObject.getString("sysselcategory").equalsIgnoreCase("Title"))
//								rawJsonObject.put("Keywords", rawJsonObject.getString("sysselvalue"));
//							
//							rawJsonObject.put("Scope", OMSApplication.getInstance().getOasisProductSearchScope());
//							
//							rawJsonObject.put("BillTo", "09390559000");
//							rawJsonObject.put("ShipTo","09390559000");
////							rawJsonObject.put("Keywords", "Chemistry");
////							rawJsonObject.put("Author", "");
////							rawJsonObject.put("Edition", "");"ISBN": "0023030852"
////							rawJsonObject.put("ProductType","");
////							rawJsonObject.put("Publisher", "");
////							rawJsonObject.put("Discipline", "");
////							rawJsonObject.put("Program", "");
////							rawJsonObject.put("GradeLevel", "");
////							rawJsonObject.put("CopyRight", "");
//						}
						// This changes are for Oasis Project. # End
						jArray.put(rawJsonObject);
						Log.d("TAG", "jsonArray:::::::" + jArray.toString());
					} while (postToCursor.moveToNext());

					manJson.put(targetTableName, jArray);
					Log.d("TAG", "jsonObject::::" + manJson.toString());
				}
			}
			postToCursor.close();
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getActionQueueCursorForStatus for input parameter dbName["
							+ dbName
							+ "] and tableName["
							+ tableName
							+ "]. Error is:" + e.getMessage());
			e.printStackTrace();

		} finally {
			if(postToCursor!=null)
			postToCursor.close();
		}
		return manJson;
	}

	/**
	 * Return IU data as list
	 * 
	 * @param parentActionUsid
	 * @return List<InsertUpdateActionCenter>
	 *//*
	public List<InsertUpdateActionCenter> getIUData(String parentActionUsid,
			int appId) {
		Cursor iuCursor = null;
		List<InsertUpdateActionCenter> IUDataList = new ArrayList<InsertUpdateActionCenter>();
		try {
			iuCursor = DBManager.getConfigDB().query(
					DatabaseConstants.IU_TABLE_NAME,
					null,
					DatabaseConstants.POST_PARENT_USID + "=" + "'"
							+ parentActionUsid + "'" + " and "
							+ DatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"+ " and "
									+ DatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			if (iuCursor.moveToFirst()) {
				InsertUpdateActionCenter tempIUCenter = new InsertUpdateActionCenter();
				tempIUCenter.sourceTable = iuCursor.getString(iuCursor
						.getColumnIndex(DatabaseConstants.SOURCE_TABLE_NAME));
				tempIUCenter.destinationTable = iuCursor
						.getString(iuCursor
								.getColumnIndex(DatabaseConstants.DESTINATION_TABLE_NAME));
				tempIUCenter.primaryKeyColumnName = iuCursor
						.getString(iuCursor
								.getColumnIndex(DatabaseConstants.IU_TABLE_PRIMARY_COLUMN_NAME));
				IUDataList.add(tempIUCenter);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getIUData for input parameter parentActionUsid["
							+ parentActionUsid + "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			iuCursor.close();
		}
		return IUDataList;
	}

	*//**
	 * Returns the list from Delete Table for actionusid
	 * 
	 * @param parentActionUsid
	 * @return List<DeleteActionCenter>
	 *//*
	public List<DeleteActionCenter> getDeleteData(String parentActionUsid,
			int appId) {
		Cursor dCursor = null;
		List<DeleteActionCenter> deleteActionList = new ArrayList<DeleteActionCenter>();
		try {
			dCursor = DBManager.getConfigDB().query(
					DatabaseConstants.DELETE_TABLE_NAME,
					null,
					DatabaseConstants.POST_PARENT_USID + "=" + "'"
							+ parentActionUsid + "'" + " and "
							+ DatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"+ " and "
									+ DatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			if (dCursor.moveToFirst()) {
				DeleteActionCenter tempdeleteactionCenter = new DeleteActionCenter();
				tempdeleteactionCenter.destinationTable = dCursor
						.getString(dCursor
								.getColumnIndex(DatabaseConstants.DELETE_TARGET_TABLE_NAME));
				tempdeleteactionCenter.primaryKeyColumnName = dCursor
						.getString(dCursor
								.getColumnIndex(DatabaseConstants.IU_TABLE_PRIMARY_COLUMN_NAME));
				deleteActionList.add(tempdeleteactionCenter);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getDeleteData for input parameter parentActionUsid["
							+ parentActionUsid + "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			dCursor.close();
		}
		return deleteActionList;
	}

	*//**
	 * Returns the list for One2Many Table for actionusid
	 * 
	 * @param parentActionUsid
	 * @return List<OneToManyActionCenter>
	 *//*
	public List<OneToManyActionCenter> getOneToManyData(
			String parentActionUsid, int appId) {
		Cursor one2ManyCursor = null;
		List<OneToManyActionCenter> one2ManyActionList = new ArrayList<OneToManyActionCenter>();
		try {
			one2ManyCursor = DBManager.getConfigDB().query(
					DatabaseConstants.ONETOMANY_TABLE_NAME,
					null,
					DatabaseConstants.POST_PARENT_USID + "=" + "'"
							+ parentActionUsid + "'" + " and "
							+ DatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"+ " and "
									+ DatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			if (one2ManyCursor.moveToFirst()) {
				OneToManyActionCenter tempOne2Many = new OneToManyActionCenter();
				tempOne2Many.parentTable = one2ManyCursor
						.getString(one2ManyCursor
								.getColumnIndex(DatabaseConstants.ONETOMANY_PARENT_TABLE));
				tempOne2Many.childTable = one2ManyCursor
						.getString(one2ManyCursor
								.getColumnIndex(DatabaseConstants.ONETOMANY_CHILD_TABLE));
				tempOne2Many.pkeyfkey = one2ManyCursor
						.getString(one2ManyCursor
								.getColumnIndex(DatabaseConstants.ONETOMANY_PRIMARY_FOREIGN_KEY_COLUMN_NAME));
				tempOne2Many.countTable = one2ManyCursor
						.getString(one2ManyCursor
								.getColumnIndex(DatabaseConstants.ONETOMANY_COUNT_TABLE));
				one2ManyActionList.add(tempOne2Many);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getOneToManyData for input parameter parentActionUsid["
							+ parentActionUsid
							+ "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			one2ManyCursor.close();
		}
		return one2ManyActionList;
	}*/

	/**
	 * Returns the list from Get Table for actionusid
	 * 
	 * @param parentActionUsid
	 * @return
	 */
	public List<String> getGetData(String parentActionUsid, int appId) {
		Cursor getCursor = null;
		List<String> getDataList = new ArrayList<String>();
		try {
			getCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.GET_TABLE_NAME,
					null,
					OMSDatabaseConstants.POST_PARENT_USID + "=" + "'"
							+ parentActionUsid + "'" + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"+ " and "
									+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			if (getCursor.moveToFirst()) {
				getDataList.add(getCursor.getString(getCursor
						.getColumnIndex(OMSDatabaseConstants.POST_DB_NAME)));
				getDataList.add(getCursor.getString(getCursor
						.getColumnIndex(OMSDatabaseConstants.POST_TO_TABLE_NAME)));
				getDataList.add(getCursor.getString(getCursor
						.getColumnIndex(OMSDatabaseConstants.POST_URL)));
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getGetData for input parameter parentActionUsid["
							+ parentActionUsid + "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			getCursor.close();
		}
		return getDataList;
	}

	/**
	 * Retrieves Column Names for table in Get Table
	 * 
	 * @param getDataList
	 * @return List<String>
	 */
	public List<String> getColumnNamesForTableInGetTable(
			List<String> getDataList) {
		List<String> columnNameList = new ArrayList<String>();
		Cursor pragmaCursor = null;
		try {
			pragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
					"pragma table_info(" + getDataList.get(1) + ");", null);

			for (boolean hasItem = pragmaCursor.moveToFirst(); hasItem; hasItem = pragmaCursor
					.moveToNext()) {
				columnNameList.add(pragmaCursor.getString(pragmaCursor
						.getColumnIndex(OMSMessages.NAME.getValue())));
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getColumnNamesForTableInGetTable. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			pragmaCursor.close();
		}
		return columnNameList;
	}

	/**
	 * insert or update data into Get table
	 * 
	 * @param getDataList
	 * @param contentvals
	 * @param PrimaryKeyColumnName
	 * @param primaryKeyVal
	 * @return int
	 */
	public int insertOrUpdateDataIntoTableOfGet(List<String> getDataList,
			ContentValues contentvals, String PrimaryKeyColumnName,
			String primaryKeyVal) {
		int insertedOrUpdatedId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Cursor getTableCursor = null;
		try {
			getTableCursor = TransDatabaseUtil.query(getDataList.get(1),
					null, PrimaryKeyColumnName + " = " + primaryKeyVal, null,
					null, null, null);
			if (getTableCursor.moveToFirst()) {
				TransDatabaseUtil.update(getDataList.get(1), contentvals,
						PrimaryKeyColumnName + " = ?",
						new String[] { primaryKeyVal });
			} else {
				TransDatabaseUtil.insert(getDataList.get(1), null,
						contentvals);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateDataIntoTableOfGet. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			getTableCursor.close();
		}
		return insertedOrUpdatedId;
	}

	/**
	 * get Business Logic detail
	 * 
	 * @param actionusId
	 * @return Cursor
	 */
	public Cursor getBLDetails(String actionusId, int appId) {
		Cursor blCursor = null;
		try {
			blCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.BL_TABLE_NAME,
					null,
					OMSDatabaseConstants.BL_PARENT_USID + "=" + "'" + actionusId
							+ "'" + " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'"+ " and "
									+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getBLDetails for input parameter actionusId["
							+ actionusId + "]. Error is:" + e.getMessage());
			e.printStackTrace();

		}
		return blCursor;
	}

	/**
	 * update Business Logic response data to destination table
	 * 
	 * @param result
	 * @param destinationTableName
	 * @param destinationRowId
	 * @return boolean
	 */
	public boolean updateBLResponseToDestinationTable(String result,
			String destinationTableName, String destinationRowId) {
		int inserted = OMSDefaultValues.NONE_DEF_CNT.getValue();
		String usid = null;
		JSONObject rawJsonObject = null;
		ContentValues contentValues = null;
		Cursor pragmaCursor = null;
		Calendar cal = Calendar.getInstance();
		try {
			Log.d("BL Result : ", result);
			rawJsonObject = new JSONObject(result);

			JSONArray blResultJsonArray = rawJsonObject
					.getJSONArray(OMSMessages.RESULT_ARRAY.getValue());
			Log.d("blResultJsonArray : ", blResultJsonArray.toString());

			pragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
					"pragma table_info(" + destinationTableName + ");", null);
			if (blResultJsonArray.length() > 0) {
				for (int i = 0; i < blResultJsonArray.length(); i++) {
					JSONObject tempJsonObject = blResultJsonArray
							.getJSONObject(i);
					contentValues = new ContentValues();
					for (boolean hasItem = pragmaCursor.moveToFirst(); hasItem; hasItem = pragmaCursor
							.moveToNext()) {
						String colName = pragmaCursor
								.getString(pragmaCursor
										.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
						if (!colName
								.equalsIgnoreCase(OMSDatabaseConstants.KEY_ROWID)
								&& !tempJsonObject.isNull(colName))
							contentValues.put(colName,
									tempJsonObject.getString(colName));

					}

					if (tempJsonObject.isNull(OMSDatabaseConstants.UNIQUE_ROW_ID)) {
						usid = Long.toString(cal.getTimeInMillis());
					} else {
						usid = tempJsonObject.getString(OMSDatabaseConstants.UNIQUE_ROW_ID);
					}
					inserted = insertOrUpdateBLData(contentValues,
							destinationTableName, usid);
					Log.d(TAG, "Inserted BL Data Insert resutlt-" + inserted);
				}
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method updateBLResponseToDestinationTable for input parameter result["
							+ result
							+ "], destinationTableName["
							+ destinationTableName
							+ "], destinationRowId["
							+ destinationRowId
							+ "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} finally {
			pragmaCursor.close();
		}
		return true;
	}

	/**
	 * insert or update BL data into trans table
	 * 
	 * @param contentValues
	 * @param tablename
	 * @param rowID
	 * @return int
	 */
	private int insertOrUpdateBLData(ContentValues contentValues,
			String tablename, String rowID) {
		int rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Cursor currentActionCursor = null;
		try {
			currentActionCursor = TransDatabaseUtil.query(tablename, null,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "=" + rowID+ " and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null,
					null, null);
			if (currentActionCursor.moveToFirst()) {
				rowId = OMSDBManager.getConfigDB().update(
						tablename,
						contentValues,
						OMSDatabaseConstants.UNIQUE_ROW_ID
								+ "= ? "
								+ " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID
								+ " = '"
								+ OMSApplication.getInstance()
										.getAppId() + "'",
						new String[] { rowID });
			} else {
				rowId = (int) TransDatabaseUtil.insert(tablename, null,
						contentValues);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdate for input parameter tablename["
							+ tablename + "], rowID[" + rowID + "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();
		} finally {
			currentActionCursor.close();
		}
		return rowId;
	}

	/**
	 * create JSON payload for posting data to server
	 * 
	 * @param tableName
	 * @param transUsidList
	 * @return JSONObject
	 */
	public JSONObject createJSONPayLoad(String tableName,
			ArrayList<String> transUsidList) {
		JSONObject rawJsonObject = null;
		JSONObject finalJsonObject = new JSONObject();
		JSONArray jsonArray = null;
		Cursor pragmaCursor = null;
		Cursor postToCursor = null;

		List<String> colNamesList = new ArrayList<String>();
		List<String> colTypeList = new ArrayList<String>();
		try {
			pragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
					"pragma table_info(" + tableName + ");", null);
			for (boolean hasItem = pragmaCursor.moveToFirst(); hasItem; hasItem = pragmaCursor
					.moveToNext()) {
				colNamesList
						.add(pragmaCursor.getString(pragmaCursor
								.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)));
				colTypeList
						.add(pragmaCursor.getString(pragmaCursor
								.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2)));

			}
		} catch (Exception e) {
			Log.e(TAG, "Error occurred in method createJSONPayLoad . Error is:"
					+ e.getMessage());
			e.printStackTrace();

		} finally {
			pragmaCursor.close();
		}
		try {
			jsonArray = new JSONArray();
			for (int k = 0; k < transUsidList.size(); k++) {
				postToCursor = TransDatabaseUtil.query(
						tableName,
						null,
						OMSDatabaseConstants.UNIQUE_ROW_ID + "='"
								+ transUsidList.get(k) + "'", null, null, null,
						null, null);
				if (postToCursor.getCount() > OMSDefaultValues.MIN_INDEX_INT
						.getValue() && postToCursor.moveToFirst()) {
					rawJsonObject = new JSONObject();
					for (int i = 0; i < colNamesList.size(); i++) {
						String value = null;
						if (colTypeList.get(i).equalsIgnoreCase(
								OMSDatabaseConstants.STRING_TYPE))
							value = postToCursor.getString(postToCursor
									.getColumnIndex(colNamesList.get(i)));
						else if (colTypeList.get(i).equalsIgnoreCase(
								OMSDatabaseConstants.INTEGER_TYPE))
							value = ""
									+ postToCursor
											.getInt(postToCursor
													.getColumnIndex(colNamesList
															.get(i)));
						else if (colTypeList.get(i).equalsIgnoreCase(
								OMSDatabaseConstants.LONG_TYPE))
							value = ""
									+ postToCursor
											.getLong(postToCursor
													.getColumnIndex(colNamesList
															.get(i)));
						else if (colTypeList.get(i).equalsIgnoreCase(
								OMSDatabaseConstants.DOUBLE_TYPE))
							value = ""
									+ postToCursor
											.getDouble(postToCursor
													.getColumnIndex(colNamesList
															.get(i)));
						else if (colTypeList.get(i).equalsIgnoreCase(
								OMSDatabaseConstants.REAL_TYPE))
							value = ""
									+ postToCursor
											.getDouble(postToCursor
													.getColumnIndex(colNamesList
															.get(i)));
						rawJsonObject.put(colNamesList.get(i), value);
					}
					jsonArray.put(rawJsonObject);
				}
			}

			finalJsonObject.put(OMSMessages.SOURCE_JS.getValue(), jsonArray);
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method createJSONPayLoad for input parameter tableName["
							+ tableName + "]. Error is:" + e.getMessage());
			e.printStackTrace();
		} finally {
			postToCursor.close();
		}
		return finalJsonObject;
	}

	/**
	 * Returns the list from MobileStudioUniverseGet Table for actionusid
	 * 
	 * @param parentActionUsid
	 * @return
	 */
	public List<String> getMobileStudioUniverseGetData(int parentActionUsid,
			int appId) {
		Cursor mobileStudioUniversegetCursor = null;
		List<String> mobileuniversegetDataList = new ArrayList<String>();
		try {
			mobileStudioUniversegetCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.MOBILESTUDIOUNIVERSEGET_TABLE_NAME,
					null,
					OMSDatabaseConstants.POST_PARENT_USID + "=" + parentActionUsid
							+ " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'"+ " and "
									+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			if (mobileStudioUniversegetCursor.moveToFirst()) {
				mobileuniversegetDataList.add(mobileStudioUniversegetCursor
						.getString(mobileStudioUniversegetCursor
								.getColumnIndex(OMSDatabaseConstants.POST_URL)));
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method mobileuniverseget data  for input parameter parentActionUsid["
							+ parentActionUsid
							+ "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			mobileStudioUniversegetCursor.close();
		}
		return mobileuniversegetDataList;
	}

	/**
	 * Returns the list from getCustomActionData Table for actionusid
	 * 
	 * @param parentActionUsid
	 * @param appId
	 * @return
	 */
	public List<CustomActionData> getCustomActionData(String parentActionUsid,
			int appId) {
		Cursor customActionCursor = null;
		List<CustomActionData> customActionList = new ArrayList<CustomActionData>();
		CustomActionData customActionData = new CustomActionData();
		try {
			customActionCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.CUSTOM_ACTION_TABLE_NAME,
					null,
					OMSDatabaseConstants.POST_PARENT_USID + " = '"
							+ parentActionUsid + "' and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"+ " and "
									+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
									+ " <> '1'", null, null, null, null);
			if (customActionCursor.moveToFirst()) {
				customActionData.methodNumber = customActionCursor
						.getInt(customActionCursor
								.getColumnIndex(OMSDatabaseConstants.CUSTOM_ACTION_METHOD_NUMBER));
				customActionData.sourceTable = customActionCursor
						.getString(customActionCursor
								.getColumnIndex(OMSDatabaseConstants.CUSTOM_ACTION_SOURCE_DATA_TABLE));
				customActionList.add(customActionData);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method CustomActionData data  for input parameter parentActionUsid["
							+ parentActionUsid
							+ "]. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			customActionCursor.close();
		}
		return customActionList;
	}
	
	
	
	public List<String> getActionTypesFromActionTable(String usid,
			int buttonId, int appId)
	{
		
		/*selection =DatabaseConstants.ACTION_QUEUE_STATUS + " = " + "'" + status + "'"
				+ "or "
				+ DatabaseConstants.ACTION_QUEUE_STATUS + " = " + "'" + DatabaseConstants.ACTION_STATUS_TRIED + "'";
				*/
		List<String> actionTypeList = new ArrayList<String>();
       	Cursor actionCursor = null;

		try {
			actionCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.ACTION_TABLE_NAME,
					null,
					OMSDatabaseConstants.ACTION_TABLE_NAVIGATION_UNIQUE_ID + "='"
							+ usid + "' and "
							+ OMSDatabaseConstants.ACTION_TABLE_BUTTON_ID + "= "
							+ buttonId + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"
							+ " and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null,
					OMSDatabaseConstants.ACTION_TABLE_ACTION_ORDER);
			if (actionCursor.getCount() > 0) {
				if (actionCursor.moveToFirst()) {
					do {
						actionTypeList.add(actionCursor.getString(actionCursor
												.getColumnIndex(OMSDatabaseConstants.ACTION_TYPE)));
						} while (actionCursor.moveToNext());
				}
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error in method getFromActionTableAndUpdateActionQueue. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			actionCursor.close();
		}
	 return actionTypeList;
	}
	
	/**
	 * Populates the action queue table with the data obtained from Action Table
	 * 
	 * @param usid
	 * @param buttonId
	 */
	public List<DoAction> getActionListFromActionTable(String usid,
			int buttonId, int appId) {
		Cursor actionCursor = null;
		Calendar calendar = Calendar.getInstance();
		List<DoAction> actionList  = new ArrayList<DoAction>();

		try {
			actionCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.ACTION_TABLE_NAME,
					null,
					OMSDatabaseConstants.ACTION_TABLE_NAVIGATION_UNIQUE_ID + "='"
							+ usid + "' and "
							+ OMSDatabaseConstants.ACTION_TABLE_BUTTON_ID + "= "
							+ buttonId + " and "
							+ OMSDatabaseConstants.CONFIGDB_APPID + " = '" + appId
							+ "'"
							+ " and "
							+ OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE
							+ " <> '1'", null, null, null,
					OMSDatabaseConstants.ACTION_TABLE_ACTION_ORDER);
			ContentValues contentValues = new ContentValues();
			if (actionCursor.getCount() > 0) {
				if (actionCursor.moveToFirst()) {
					do {
						/*	contentValues
								.put(OMSDatabaseConstants.ACTION_QUEUE_TYPE,
										actionCursor.getString(actionCursor
												.getColumnIndex(OMSDatabaseConstants.ACTION_TYPE)));
						contentValues
								.put(OMSDatabaseConstants.ACTION_UNIQUE_ID,
										actionCursor.getString(actionCursor
												.getColumnIndex(OMSDatabaseConstants.ACTION_TABLE_ACTION_ID)));
						contentValues.put(
								OMSDatabaseConstants.ACTION_QUEUE_STATUS,
								OMSDatabaseConstants.ACTION_STATUS_NEW);
						contentValues
								.put(OMSDatabaseConstants.ACTION_QUEUE_PRIORITY,
										actionCursor.getInt(actionCursor
												.getColumnIndex(OMSDatabaseConstants.ACTION_TABLE_ACTION_ORDER)));
						contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
								Long.toString(calendar.getTimeInMillis()));
						contentValues.put(OMSDatabaseConstants.CONFIGDB_APPID,
								appId);
						insertOrUpdateActionQueue(contentValues,
								OMSMessages.MIN_INDEX_STR.getValue(), appId);*/
						
						DoAction tempAction = new DoAction();
						tempAction.actionType = actionCursor.getString(actionCursor
								.getColumnIndex(OMSDatabaseConstants.ACTION_TYPE));
						tempAction.actionUsid = actionCursor.getString(actionCursor
								.getColumnIndex(OMSDatabaseConstants.ACTION_TABLE_ACTION_ID));
						tempAction.uniqueRowId = actionCursor.getString(actionCursor
								.getColumnIndex("usid"));
						actionList.add(tempAction);
					} while (actionCursor.moveToNext());
				}
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error in method getFromActionTableAndUpdateActionQueue. Error is:"
							+ e.getMessage());
			e.printStackTrace();

		} finally {
			actionCursor.close();
		}
		return actionList;
	}

	
	
	/**
	 * insert a new entry or update status in GetQueue table
	 * 
	 * @param contentValues
	 * @param actionusid
	 * @return
	 */
	public int insertOrUpdateTransactionFailQueue(ContentValues contentValues,
			String actionusid, int appId) {
		int rowId = OMSDefaultValues.NONE_DEF_CNT.getValue();
		Cursor currentActionCursor = null;
		try {
			currentActionCursor = OMSDBManager.getConfigDB().query(
					OMSDatabaseConstants.TRANSACTION_QUEUE_TABLE_NAME,
					null,
					OMSDatabaseConstants.TRANSACTION_QUEUE_ACTION_UNIQUE_ID + "=" + "'" + actionusid
							+ "'" + " and " + OMSDatabaseConstants.CONFIGDB_APPID
							+ " = '" + appId + "'", null, null, null, null);
			if (currentActionCursor.moveToFirst()) {
				rowId = OMSDBManager.getConfigDB().update(
						OMSDatabaseConstants.TRANSACTION_QUEUE_TABLE_NAME,
						contentValues,
						OMSDatabaseConstants.TRANSACTION_QUEUE_ACTION_UNIQUE_ID + "= ? " + " and "
								+ OMSDatabaseConstants.CONFIGDB_APPID + " = '"
								+ appId + "'", new String[] { actionusid });
			} else {
				Calendar calendar = Calendar.getInstance();
				contentValues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
						Long.toString(calendar.getTimeInMillis()));
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_IS_DELETE,
						0);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_ACTION_UNIQUE_ID,
						actionusid);
				contentValues.put(OMSDatabaseConstants.TRANSACTION_QUEUE_MODIFIED_DATE,
						Long.toString(calendar.getTimeInMillis()));
				contentValues.put(OMSDatabaseConstants.CONFIGDB_APPID,
						appId);
				rowId = (int) OMSDBManager.getConfigDB().insert(
						OMSDatabaseConstants.TRANSACTION_QUEUE_TABLE_NAME, null,
						contentValues);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method insertOrUpdateActionQueue for input parameter uniqueID["
							+ actionusid + "]. Error is:" + e.getMessage());
			e.printStackTrace();

		} finally {
			currentActionCursor.close();
		}
		return rowId;
	}
	
	/*
	 *  Get All Tables data
	 */
	
	public JSONObject getJsonPayload(String schemaName) {
		JSONObject finalAppJson = new JSONObject();
		JSONObject appJson = null ;
		Cursor dbCursor = null;
		List<String> tableNamesList = new ArrayList<String>();
		try {
			appJson = new JSONObject();
			dbCursor = OMSDBManager
					.getSpecificDB(schemaName)
					.rawQuery(
							"SELECT name FROM sqlite_master where type='table' and name != 'android_metadata' and name != 'sqlite_sequence';",
							null);
			for (boolean hasItem = dbCursor.moveToFirst(); hasItem; hasItem = dbCursor
					.moveToNext()) {
				tableNamesList
				.add(dbCursor.getString(dbCursor
						.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)));
			}

			JSONArray tableDataArray = null;
			String tableName = null;
			for (int i = 0; i < tableNamesList.size(); i++) {
				tableName = tableNamesList.get(i);
				if (tableName != null
						) {
					tableDataArray = getTabledata(tableNamesList.get(i),schemaName);

					if (tableDataArray != null && tableDataArray.length() > 0) {

						appJson = appJson.put(tableNamesList.get(i),
								tableDataArray);
					}
				}
			}

			if(!OMSConstants.USE_GENERIC_URL){	
				finalAppJson.put(schemaName, appJson);
			}
		} catch (Exception e) {

		}
		if(OMSConstants.USE_GENERIC_URL){
			return appJson;
		}else{

			return finalAppJson;
		}
	}
	
	
	
	/**
	 * Returns the list that contains the data obtained from tableName(table) of
	 * dbName(database) received from action queue to be posted to server
	 * 
	 * @param schema
	 * @param tableName
	 * @return JSONObject
	 */
	public JSONArray getTabledata(String tableName,String schema) {
		Cursor pragmaCursor = null;
		Cursor postToCursor = null;
		JSONArray jArray = null;
		List<String> colNamesList = new ArrayList<String>();
		List<String> colTypeList = new ArrayList<String>();
		try {
			pragmaCursor = OMSDBManager.getSpecificDB(schema).rawQuery(
					"pragma table_info(" + tableName + ");", null);
			for (boolean hasItem = pragmaCursor.moveToFirst(); hasItem; hasItem = pragmaCursor
					.moveToNext()) {
				if(!pragmaCursor.getString(pragmaCursor
						.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)).equalsIgnoreCase("isdirty")){
					colNamesList
					.add(pragmaCursor.getString(pragmaCursor
							.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1)));
					colTypeList
					.add(pragmaCursor.getString(pragmaCursor
							.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2)));
				}

			}
		} catch (Exception e) {
			Log.e(TAG,
					"Error occurred in method getTabledata for input parameter dbName[10]"
							+ "] and tableName[" + tableName + "]. Error is:"
							+ e.getMessage());

		} finally {
			pragmaCursor.close();
		}

		try {
			if (tableName != null) {

				// Post only status =fresh
				postToCursor = OMSDBManager.getSpecificDB(schema).query(tableName,
						null, OMSDatabaseConstants.COMMON_COLUMN_NAME_STATUS + "=" + "'" + OMSDatabaseConstants.ACTION_STATUS_NEW
						+ "'" ,
						null, null, null, null);
				// }
			}

			if (postToCursor.getCount() > 0) {
				if (postToCursor.moveToFirst()) {
					jArray = new JSONArray();
					do {
						JSONObject rawJsonObject = new JSONObject();

						for (int i = 0; i < colNamesList.size(); i++) {
							String value = null;
							value = postToCursor.getString(postToCursor
									.getColumnIndex(colNamesList.get(i)));
							
							/*if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.STRING_TYPE)
									|| colTypeList.get(i).equalsIgnoreCase(
											"varchar(45)"))
								value = postToCursor.getString(postToCursor
										.getColumnIndex(colNamesList.get(i)));
							else if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.INTEGER_TYPE)
									|| colTypeList.get(i).equalsIgnoreCase(
											"int(11)")
									|| colTypeList.get(i).equalsIgnoreCase(
											"BIGINT"))
								value = ""
										+ postToCursor.getInt(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));
							else if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.LONG_TYPE))
								value = ""
										+ postToCursor.getLong(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));
							else if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.DOUBLE_TYPE))
								value = ""
										+ postToCursor.getDouble(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));
							else if (colTypeList.get(i).equalsIgnoreCase(
									OMSDatabaseConstants.REAL_TYPE))
								value = ""
										+ postToCursor.getDouble(postToCursor
												.getColumnIndex(colNamesList
														.get(i)));*/
							try {
								rawJsonObject.put(colNamesList.get(i), value);
							} catch (JSONException e) {
								Log.e(TAG,
										"Error occurred in method formJSONPayLoad . Error is:"
												+ e.getMessage());

							}
						}
						jArray.put(rawJsonObject);
					} while (postToCursor.moveToNext());

				}
			}
		} catch (SQLException e) {
			Log.e(TAG,
					"Error occurred in method getActionQueueCursorForStatus for input parameter dbName[10]"
							+ "] and tableName["
							+ tableName
							+ "]. Error is:"
							+ e.getMessage());

		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"Error occurred in method getActionQueueCursorForStatus for input parameter dbName[10]"
							+ "] and tableName["
							+ tableName
							+ "]. Error is:"
							+ e.getMessage());

		} finally {
			postToCursor.close();
		}
		return jArray;
	}
	// This changes are for Oasis Project. # Start
//	public OasisDetails getOasisData(String schemaName) {
//		OasisDetails oasisDetails = new OasisDetails();
//		Cursor dbCursor = null;
//		try {
//			dbCursor = OMSDBManager
//					.getSpecificDB(oasisSchemaName)
//					.rawQuery(
//							"SELECT criterianname, criteriavalue, criteriacode FROM OrderSearch where isdelete <> 1;",
//							null);
//			if (dbCursor.getCount() > 0) {
//				if (dbCursor.moveToFirst()) {
//					//do 
//					{
//												
//						oasisDetails.setCriteriaName(dbCursor.getString(dbCursor
//								.getColumnIndex("criterianname")));
//						oasisDetails.setCriteriaValue(dbCursor.getString(dbCursor
//								.getColumnIndex("criteriavalue")));
//						oasisDetails.setCriteriacode(dbCursor.getString(dbCursor
//								.getColumnIndex("criteriacode")));
//						
//					} //while (dbCursor.moveToNext());
//				}
//			}
//
//		} catch (Exception e) {
//			Log.e(TAG,
//					"Error occurred in method getOasisData"
//							+ "tableName["
//							+ "OrderSearch"
//							+ "]. Error is:"
//							+ e.getMessage());
//		}
//		return oasisDetails;
//	}
//
//	public OasisDetails getOasisOrderDetails(String orderUsid) {
//		OasisDetails oasisDetails = new OasisDetails();
//		Cursor dbCursor = null;
//		try {
//			dbCursor = OMSDBManager
//					.getSpecificDB(oasisSchemaName)
//					.rawQuery(
//							"SELECT Order_Number,System_Indicator FROM Orders where usid='"+orderUsid+"';",
//							null);
//			if (dbCursor.getCount() > 0) {
//				if (dbCursor.moveToFirst()) {
//					do {
//						oasisDetails.setOrderNumber(dbCursor.getString(dbCursor
//								.getColumnIndex("Order_Number")));	
//						oasisDetails.setOrderSystemIndicator(dbCursor.getString(dbCursor
//								.getColumnIndex("System_Indicator")));
//					} while (dbCursor.moveToNext());
//				}
//			}
//
//		} catch (Exception e) {
//			Log.e(TAG,
//					"Error occurred in method getOasisData"
//							+ "tableName["
//							+ "OrderSearch"
//							+ "]. Error is:"
//							+ e.getMessage());
//		}
//		return oasisDetails;
//	}
//	public OasisDetails getOasisShippmentDetails(String invoiceUsid) {
//		OasisDetails oasisDetails = new OasisDetails();
//		Cursor dbCursor = null;
//		try {
//			dbCursor = OMSDBManager
//					.getSpecificDB(oasisSchemaName)
//					.rawQuery(
//							"SELECT Order_Number,InvoiceNumber,System_Indicator FROM ShipmentDetails where usid='"+invoiceUsid+"';",
//							null);
//			if (dbCursor.getCount() > 0) {
//				if (dbCursor.moveToFirst()) {
//					do {
//						oasisDetails.setOrderNumber(dbCursor.getString(dbCursor
//								.getColumnIndex("Order_Number")));	
//						oasisDetails.setOrderSystemIndicator(dbCursor.getString(dbCursor
//								.getColumnIndex("System_Indicator")));
//						oasisDetails.setInvoiceNumber(dbCursor.getString(dbCursor
//								.getColumnIndex("InvoiceNumber")));
//					} while (dbCursor.moveToNext());
//				}
//			}
//
//		} catch (Exception e) {
//			Log.e(TAG,
//					"Error occurred in method getOasisData"
//							+ "tableName["
//							+ "OrderSearch"
//							+ "]. Error is:"
//							+ e.getMessage());
//		}
//		return oasisDetails;
//	}
//	public OasisDetails getOasisDeliveryDetails(String invoiceUsid) {
//		OasisDetails oasisDetails = new OasisDetails();
//		Cursor dbCursor = null;
//		try {
//			dbCursor = OMSDBManager
//					.getSpecificDB(oasisSchemaName)
//					.rawQuery(
//							"SELECT Order_Number,InvoiceNumber,ParcelID,System_Indicator FROM DeliveryInfo where usid='"+invoiceUsid+"';",
//							null);
//			if (dbCursor.getCount() > 0) {
//				if (dbCursor.moveToFirst()) {
//					do {
//						oasisDetails.setOrderNumber(dbCursor.getString(dbCursor
//								.getColumnIndex("Order_Number")));	
//						oasisDetails.setOrderSystemIndicator(dbCursor.getString(dbCursor
//								.getColumnIndex("System_Indicator")));
//						oasisDetails.setParcelID(dbCursor.getString(dbCursor
//								.getColumnIndex("ParcelID")));
//						oasisDetails.setInvoiceNumber(dbCursor.getString(dbCursor
//								.getColumnIndex("InvoiceNumber")));
//					} while (dbCursor.moveToNext());
//				}
//			}
//
//		} catch (Exception e) {
//			Log.e(TAG,
//					"Error occurred in method getOasisData"
//							+ "tableName["
//							+ "OrderSearch"
//							+ "]. Error is:"
//							+ e.getMessage());
//		}
//		return oasisDetails;
//	}
	// This changes are for Oasis Project. # End
}

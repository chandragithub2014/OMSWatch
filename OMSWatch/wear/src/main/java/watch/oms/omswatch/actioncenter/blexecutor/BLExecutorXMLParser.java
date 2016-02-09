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
package watch.oms.omswatch.actioncenter.blexecutor;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.database.Cursor;

import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLBreakDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLColumnDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLConnectivityOperatorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLCreateTableDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLDeleteDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLDestinationDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLFalseDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLFileDownloadDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLFileUploadDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLForDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLGetDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLGlobalDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLIUDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLNextDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOneToManyDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperandDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLOperatorDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLStartDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLStringModifierDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLStringModifierOperandDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLTableColumnDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLTableCreateTableDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLTableDBDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLTrueDTO;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSMessages;


/**
 * TransDBXMLParser: Parses TransDB XML String and returns list of DataBase,
 * Table and column data.
 * 
 * @author 280779
 * 
 */
public class BLExecutorXMLParser {
	private static final String COLUMN_TYPE = "columnType";
	private static final String COLUMN = "Column";
	private static final String START = OMSMessages.BL_START.getValue();
	private BLDestinationDTO destination;
	public BLOperandDTO operand;
	public BLOperatorDTO operator;
	public BLConnectivityOperatorDTO conOperator;
	public boolean isStart = false;
	public boolean hasAttributes = false;
	public BLStartDTO start;
	public List<BLDestinationDTO> destList;
	public List<BLTrueDTO> trueList;
	public List<BLFalseDTO> falseList;
	public BLTrueDTO trueData;
	public BLFalseDTO falseData;
	public BLNextDTO expressionNextData;
	public List<BLNextDTO> nextExpressionDataList;
    public boolean isForceUpdate; 

	public BLExecutorXMLParser(Context context) {
	}

	public List<Object> parseAddBL(String xmlResponse,String blType)
			throws XmlPullParserException, IOException {
		List<Object> expressionsList = new ArrayList<Object>();
		List<BLStartDTO> expressionList;
		List<BLDestinationDTO> destinationDataList = new ArrayList<BLDestinationDTO>();
		List<BLOperandDTO> operandList = new ArrayList<BLOperandDTO>();
		List<BLTrueDTO> trueList = new ArrayList<BLTrueDTO>();
		List<BLFalseDTO> falseList = new ArrayList<BLFalseDTO>();
		List<BLNextDTO> nextDataList = new ArrayList<BLNextDTO>();
		String tagName = null;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlResponse));
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.RESULT.getValue())) {
					destination = new BLDestinationDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							destination.destinataionTableName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.PRIMARY_KEY.getValue())) {
							destination.destinationPrimaryKey = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_TYPE.getValue())) {
							destination.destinationType = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_NAME.getValue())) {
							destination.destinationColumnName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.IS_GLOBAL.getValue())) {
							destination.isGlobal = Boolean.parseBoolean(xmlPullParser
									.getAttributeValue(i));
						}
						if( i == xmlPullParser.getAttributeCount()-1)
							destination.destinationType = getColumnType(destination.destinataionTableName, destination.destinationColumnName);
					}

				} else if (tagName.equalsIgnoreCase(START)) {
					isStart = true;
					start = new BLStartDTO();
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.OPERAND
						.getValue())) {
					if (isStart) {
						if (xmlPullParser.getAttributeCount() > 0) {
							operand = new BLOperandDTO();
							hasAttributes = true;
						}
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.DB_TABLENAME.getValue())) {
								operand.operandTableName = xmlPullParser
										.getAttributeValue(i);
							} else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.PRIMARY_KEY.getValue())) {
								operand.operandPrimaryKey = xmlPullParser
										.getAttributeValue(i);
							} else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.COLUMN_TYPE.getValue())) {
								operand.operandType = xmlPullParser
										.getAttributeValue(i);
							} else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.COLUMN_NAME.getValue())) {
								operand.operandColumnName = xmlPullParser
										.getAttributeValue(i);
							} else if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.COLUMN_VALUE.getValue())) {
								operand.constantValue = xmlPullParser
										.getAttributeValue(i);
							} else if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.IS_GLOBAL.getValue())) {
								operand.isGlobal = Boolean.parseBoolean(xmlPullParser
										.getAttributeValue(i));
							}
							if( i == xmlPullParser.getAttributeCount()-1 && !operand.operandColumnName.equals("constant"))
								operand.operandType = getColumnType(operand.operandTableName, operand.operandColumnName);
							
						}
					}
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.OPERATOR
						.getValue())) {
					if (isStart) {
						operator = new BLOperatorDTO();
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++)
							operator.operatorName = xmlPullParser
									.getAttributeValue(i);
					} else {
						if(blType.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_EXPRESSION)){
						operator = new BLOperatorDTO();
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++)
							operator.operatorName = xmlPullParser
									.getAttributeValue(i);
						 }else{
						conOperator = new BLConnectivityOperatorDTO();
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++)
							conOperator.connectivityoperatorName = xmlPullParser
									.getAttributeValue(i);
						 }
					}
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.TRUE.getValue())) {
					trueData = new BLTrueDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							trueData.tableName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_NAME.getValue())) {
							trueData.colName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							trueData.colVal = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(COLUMN_TYPE)) {
							trueData.colType = xmlPullParser
									.getAttributeValue(i);
						}
						if( i == xmlPullParser.getAttributeCount()-1)
							trueData.colType = getColumnType(trueData.tableName, trueData.colName);
					}
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.FALSE.getValue())) {
					falseData = new BLFalseDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							falseData.tableName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_NAME.getValue())) {
							falseData.colName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							falseData.colVal = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(COLUMN_TYPE)) {
							falseData.colType = xmlPullParser
									.getAttributeValue(i);
						}
						if( i == xmlPullParser.getAttributeCount()-1)
							falseData.colType = getColumnType(falseData.tableName, falseData.colName);
					}
				}

				else if (tagName
						.equalsIgnoreCase(OMSMessages.CONNECTIVITY_OPERATOR
								.getValue())) {
					conOperator = new BLConnectivityOperatorDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++)
						conOperator.connectivityoperatorName = xmlPullParser
								.getAttributeValue(i);
				} else if (tagName
						.equalsIgnoreCase(OMSMessages.NEXT.getValue())) {
					expressionNextData = new BLNextDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							expressionNextData.tableName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_NAME.getValue())) {
							expressionNextData.colName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							expressionNextData.colVal = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(COLUMN_TYPE)) {
							expressionNextData.colType = xmlPullParser
									.getAttributeValue(i);
						}
						if( i == xmlPullParser.getAttributeCount()-1)
							expressionNextData.colType = getColumnType(expressionNextData.tableName, expressionNextData.colName);
					}
				} else if (tagName
						.equalsIgnoreCase(OMSDatabaseConstants.BL_TYPE_EXPRESSION)){
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase("isforceupdate")) {
							String isForceUpdateAttr =  xmlPullParser
									.getAttributeValue(i);
							if(isForceUpdateAttr.equalsIgnoreCase("true")||isForceUpdateAttr.equalsIgnoreCase("false")){
								setForceUpdate(Boolean.parseBoolean(isForceUpdateAttr));
							}
						}
					}
					
				}

				break;

			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.RESULT.getValue())) {
					destinationDataList.add(destination);
					setDestinationDataList(destinationDataList);
				} else if (tagName.equalsIgnoreCase(OMSMessages.OPERAND
						.getValue())) {
					if (hasAttributes) {
						operandList.add(operand);

					}
				} else if (tagName.equalsIgnoreCase(OMSMessages.OPERATOR
						.getValue())) {
					if (isStart) {
						start.operator = operator;
					} else {
						
						expressionsList.add(conOperator);
						expressionsList.add(operator);
					}
				} else if (tagName.equalsIgnoreCase(START)) {
					if (isStart && hasAttributes) {
						hasAttributes = false;
						start.operandList = operandList;
						expressionList = new ArrayList<BLStartDTO>();
						expressionList.add(start);
						isStart = false;
						expressionsList.add(expressionList);
						operandList = new ArrayList<BLOperandDTO>();
					}
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.TRUE.getValue())) {
					trueList.add(trueData);
					setTrueDataList(trueList);
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.FALSE.getValue())) {
					falseList.add(falseData);
					setFalseDataList(falseList);
				} else if (tagName
						.equalsIgnoreCase(OMSMessages.CONNECTIVITY_OPERATOR
								.getValue())) {
					expressionsList.add(conOperator);
				} else if (tagName
						.equalsIgnoreCase(OMSMessages.NEXT.getValue())) {
					nextDataList.add(expressionNextData);
					setExpressionNextList(nextDataList);
				}

				break;
			}
			eventType = xmlPullParser.next();
		}
		return expressionsList;
	}
	public boolean isForceUpdate() {
		return isForceUpdate;
	}

	public void setForceUpdate(boolean isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}

	public void setDestinationDataList(List<BLDestinationDTO> destList) {
		this.destList = destList;
	}

	public void setTrueDataList(List<BLTrueDTO> trueList) {
		this.trueList = trueList;
	}

	public void setFalseDataList(List<BLFalseDTO> falseList) {
		this.falseList = falseList;
	}

	public List<BLDestinationDTO> getDestList() {
		return destList;
	}

	public List<BLTrueDTO> getTrueList() {
		return trueList;
	}

	public List<BLFalseDTO> getFalseList() {
		return falseList;
	}

	public void setExpressionNextList(List<BLNextDTO> nextList) {
		this.nextExpressionDataList = nextList;
	}

	public List<BLNextDTO> getExpressionNextList() {
		return nextExpressionDataList;
	}

	public List<String> parseBLXML(String xmlResponse)
			throws XmlPullParserException, IOException {
		List<String> conditionList = new ArrayList<String>();
		String tagName = null;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlResponse));

		int eventType = xmlPullParser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();

			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.END_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.IF.getValue())
						&& !xmlPullParser.isEmptyElementTag()) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.PARAMETER.getValue())) {
							conditionList.add(xmlPullParser
									.getAttributeValue(i));
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(OMSMessages.TYPE.getValue())) {
							conditionList.add(xmlPullParser
									.getAttributeValue(i));
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(OMSMessages.VALUE.getValue())) {
							conditionList.add(xmlPullParser
									.getAttributeValue(i));
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.CONDITION.getValue())) {
							conditionList.add(xmlPullParser
									.getAttributeValue(i));
						}
					}
				} else if (tagName
						.equalsIgnoreCase(OMSMessages.TRUE.getValue())) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						conditionList.add(xmlPullParser.getAttributeValue(i));
					}

				}
				break;
			}
			eventType = xmlPullParser.next();
		}
		return conditionList;
	}

	public List<BLOneToManyDTO> parseOneToManyXML(String xmlRespone)
			throws XmlPullParserException, IOException {
		BLColumnDTO column = null;
		List<BLColumnDTO> destinationColList = null;
		List<BLColumnDTO> sourceColList = null;
		List<BLOneToManyDTO> oneToManyList = new ArrayList<BLOneToManyDTO>();
		BLOneToManyDTO tempOneToMany = new BLOneToManyDTO();
		boolean isDestination = false;
		boolean isSource = false;
		String tagName = null;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlRespone));
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.END_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if (tagName
						.equalsIgnoreCase(OMSMessages.DESTINATION.getValue())
						&& !xmlPullParser.isEmptyElementTag()) {
					isDestination = true;
					destinationColList = new ArrayList<BLColumnDTO>();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							tempOneToMany.destinationTableName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.PK_FK_KEY.getValue())) {
							tempOneToMany.destinationPrimaryForeignKey = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_TYPE.getValue())) {
							tempOneToMany.destinationPrimaryForeignKeyType = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.PARENT_TABLE.getValue())) {
							tempOneToMany.parentTableName = xmlPullParser
									.getAttributeValue(i);
						}
					}
				} else if (tagName.equalsIgnoreCase(COLUMN)) {
					column = new BLColumnDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.COLUMN_NAME.getValue())) {
							column.columnName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_TYPE.getValue())) {
							column.columnType = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							column.columnValue = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_INDEX.getValue())) {
							column.columnIndex = xmlPullParser
									.getAttributeValue(i);
						}else if(isSource){
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.IS_GLOBAL.getValue())){
								column.isGlobal = Boolean.parseBoolean(xmlPullParser
										.getAttributeValue(i));
							}
						}
					}

				}

				else if (tagName
						.equalsIgnoreCase(OMSMessages.SOURCE.getValue())) {
					isSource = true;
					sourceColList = new ArrayList<BLColumnDTO>();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							tempOneToMany.sourceTableName = xmlPullParser
									.getAttributeValue(i);
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.ONE_MANY.getValue())) {
					oneToManyList.add(tempOneToMany);
				} else if (tagName.equalsIgnoreCase(COLUMN)) {
					if (isDestination) {
						destinationColList.add(column);

					}
					if (isSource) {
						sourceColList.add(column);
					}
				} else if (tagName.equalsIgnoreCase(OMSMessages.DESTINATION
						.getValue())) {
					isDestination = false;
					tempOneToMany.destinationColList = destinationColList;
				} else if (tagName.equalsIgnoreCase(OMSMessages.SOURCE
						.getValue())) {
					isSource = false;
					tempOneToMany.sourceColList = sourceColList;
				}
				break;
			}
			eventType = xmlPullParser.next();
		}
		return oneToManyList;
	}

	public List<BLIUDTO> parseIUXML(String xmlResponse)
			throws XmlPullParserException, IOException {
		BLColumnDTO column = null;
		List<BLColumnDTO> destinationColList = null;
		List<BLColumnDTO> sourceColList = null;
		List<BLIUDTO> IUList = new ArrayList<BLIUDTO>();
		BLIUDTO iu = new BLIUDTO();
		boolean isDestination = false;
		boolean isSource = false;
		String tagName = null;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlResponse));
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.END_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if (tagName
						.equalsIgnoreCase(OMSMessages.DESTINATION.getValue())
						&& !xmlPullParser.isEmptyElementTag()) {
					isDestination = true;
					destinationColList = new ArrayList<BLColumnDTO>();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							iu.destinationTableName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.PRIMARY_KEY.getValue())) {
							iu.destinationPrimaryKey = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(OMSMessages.TYPE.getValue())) {
							iu.destinationPrimaryKeyType = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.SCHEMA_NAME.getValue())) {
							iu.destinationSchemaName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.DEST_WHERE.getValue())){
							if(xmlPullParser
									.getAttributeValue(i)!=null){
							iu.destinationWhere = xmlPullParser
									.getAttributeValue(i); }else{
										iu.destinationWhere="";
									}
							
						}

					}
				} else if (tagName.equalsIgnoreCase(COLUMN)) {
					column = new BLColumnDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.COLUMN_NAME.getValue())) {
							column.columnName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_TYPE.getValue())) {
							column.columnType = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							column.columnValue = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_INDEX.getValue())) {
							column.columnIndex = xmlPullParser
									.getAttributeValue(i);
						}else if(isSource){
							if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.IS_GLOBAL.getValue())){
								column.isGlobal = Boolean.parseBoolean(xmlPullParser
										.getAttributeValue(i));
							}
						}
					}
				} else if (tagName.equalsIgnoreCase(OMSMessages.SOURCE
						.getValue())) {
					isSource = true;
					sourceColList = new ArrayList<BLColumnDTO>();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							iu.sourceTableName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.SCHEMA_NAME.getValue())) {
							iu.sourceSchemaName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.PRIMARY_KEY.getValue())) {
							iu.sourcePrimaryKey = xmlPullParser
									.getAttributeValue(i);
						}else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.SOURCE_WHERE.getValue())){
							if(xmlPullParser
									.getAttributeValue(i)!=null){
							iu.sourceWhere = xmlPullParser
									.getAttributeValue(i);
							}else{
								iu.sourceWhere="";
							}
							
						}

					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.IU.getValue())) {
					IUList.add(iu);
				}

				else if (tagName.equalsIgnoreCase(COLUMN)) {
					if (isDestination) {
						destinationColList.add(column);

					}
					if (isSource) {
						sourceColList.add(column);
					}
				} else if (tagName.equalsIgnoreCase(OMSMessages.DESTINATION
						.getValue())) {
					isDestination = false;
					iu.destinationColList = destinationColList;
				} else if (tagName.equalsIgnoreCase(OMSMessages.SOURCE
						.getValue())) {
					isSource = false;
					iu.sourceColList = sourceColList;
				}
				break;
			}

			eventType = xmlPullParser.next();

		}
		return IUList;
	}

	public List<BLDeleteDTO> parseBLDelete(String xmlResponse)
			throws XmlPullParserException, IOException {
		List<BLDeleteDTO> blDelList = new ArrayList<BLDeleteDTO>();
		BLDeleteDTO blDel = new BLDeleteDTO();
		String tagName = null;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlResponse));
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.END_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if (tagName
						.equalsIgnoreCase(OMSMessages.DESTINATION.getValue())) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							blDel.destinationTable = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.PRIMARY_KEY.getValue())) {
							blDel.primaryKeyColum = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(OMSMessages.BL_DELETE_HARD_DELETE.getValue())) {
							blDel.isHardDelete = xmlPullParser
									.getAttributeValue(i);
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (tagName
						.equalsIgnoreCase(OMSMessages.DESTINATION.getValue())) {
					blDelList.add(blDel);
				}
				break;
			}
			eventType = xmlPullParser.next();
		}
		return blDelList;
	}

	public List<BLForDTO> parseBLFor(String xmlResponse)
			throws XmlPullParserException, IOException {
		List<BLForDTO> forList = new ArrayList<BLForDTO>();
		BLForDTO blFor = new BLForDTO();
		String tagName = null;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlResponse));
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.END_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.INIT.getValue())) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.INITIAL_VALUE.getValue())) {
							blFor.initialVal = Integer.parseInt(xmlPullParser
									.getAttributeValue(i));
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_NAME.getValue())) {

							blFor.initcolumnName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.DB_TABLENAME.getValue())) {

							blFor.initTableName = xmlPullParser
									.getAttributeValue(i);
						}
					}
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.DO.getValue())) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							blFor.doTableName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							blFor.doColVal = xmlPullParser.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_NAME.getValue())) {
							blFor.doColName = xmlPullParser
									.getAttributeValue(i);
						}

					}
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.BREAK.getValue())) {
					List<BLBreakDTO> breakList = new ArrayList<BLBreakDTO>();
					BLBreakDTO breakdata = new BLBreakDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.COLUMN_NAME.getValue())) {
							breakdata.breakColumnName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							breakdata.breakColVal = xmlPullParser
									.getAttributeValue(i);
						}
						
					}
					breakList.add(breakdata);
					blFor.breakList = breakList;
				}
				break;
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.FOR.getValue())) {
					forList.add(blFor);
				}
				break;
			}
			eventType = xmlPullParser.next();
		}
		return forList;

	}

	public List<BLCreateTableDTO> parseBLCreateTable(String xmlResponse)
			throws XmlPullParserException, IOException {
		List<BLCreateTableDTO> blCreateTableList = new ArrayList<BLCreateTableDTO>();
		BLCreateTableDTO tempBLCreateTable = new BLCreateTableDTO();
		BLTableDBDTO tempBLDB = new BLTableDBDTO();
		BLTableCreateTableDTO tempTableDTO = new BLTableCreateTableDTO();
		BLTableColumnDTO tempColumnDTO = null;
		List<BLTableColumnDTO> tempColList = new ArrayList<BLTableColumnDTO>();
		String tagName = null;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlResponse));
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.END_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.DB.getValue())
						&& !xmlPullParser.isEmptyElementTag()) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.COLUMN_NAME.getValue())) {
							tempBLDB.columnName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							tempBLDB.columnvalue = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.SCHEMA_NAME.getValue())) {
							tempBLDB.schemaname = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.DB_TABLENAME.getValue())) {
							tempBLDB.tablename = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.PRIMARY_KEY.getValue())) {
							tempBLDB.primarykey = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_TYPE.getValue())) {
							tempBLDB.columnType = xmlPullParser
									.getAttributeValue(i);
						}
					}
				}

				else if (tagName.equalsIgnoreCase(OMSMessages.TABLE.getValue())
						&& !xmlPullParser.isEmptyElementTag()) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.COLUMN_NAME.getValue())) {
							tempTableDTO.columnName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							tempTableDTO.columnvalue = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.SCHEMA_NAME.getValue())) {
							tempTableDTO.schemaname = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.DB_TABLENAME.getValue())) {
							tempTableDTO.tablename = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_TYPE.getValue())) {
							tempTableDTO.columnType = xmlPullParser
									.getAttributeValue(i);
						}

					}
				} else if (tagName.equalsIgnoreCase(OMSMessages.COLUMN
						.getValue()) && !xmlPullParser.isEmptyElementTag()) {
					tempColumnDTO = new BLTableColumnDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.COLUMN_NAME.getValue())) {
							tempColumnDTO.columnName = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_VALUE.getValue())) {
							tempColumnDTO.columnvalue = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.SCHEMA_NAME.getValue())) {
							tempColumnDTO.schemaname = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.DB_TABLENAME.getValue())) {
							tempColumnDTO.tablename = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.CONSTRAINT.getValue())) {
							tempColumnDTO.constraint = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.DEFAULT_VALUE_STRING
												.getValue())) {
							tempColumnDTO.defaultVal = xmlPullParser
									.getAttributeValue(i);
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.COLUMN_TYPE.getValue())) {
							tempColumnDTO.columnType = xmlPullParser
									.getAttributeValue(i);
						}

					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.DB.getValue())) {
					tempBLCreateTable.dbDto = tempBLDB;
					blCreateTableList.add(tempBLCreateTable);
				} else if (tagName.equalsIgnoreCase(OMSMessages.TABLE
						.getValue())) {
					tempBLCreateTable.dbtableDTO = tempTableDTO;
				} else if (tagName.equalsIgnoreCase(OMSMessages.COLUMN
						.getValue())) {
					tempColList.add(tempColumnDTO);
					tempBLCreateTable.columnDTOList = tempColList;
				}
				break;
			}
			eventType = xmlPullParser.next();
		}
		return blCreateTableList;
	}

	public Hashtable<String,String> processPostBL(String xmlResponse, String methodType) {
	//	List<String> result = new ArrayList<String>();
		  String tagText;
		boolean isLoadingMessage=false;
		boolean isSuccessMessageColumn=false;
		boolean isSuccessMessage=false;
		
		Hashtable<String, String> result = new Hashtable<String,String>();
		String tagName = null;
		try {
			XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
					.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlResponse));
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				tagName = xmlPullParser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;

				case XmlPullParser.END_DOCUMENT:
					break;

				case XmlPullParser.START_TAG:
					if (tagName.equalsIgnoreCase(methodType)) {
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.DB_TABLENAME.getValue())) {
							/*	result.add(0,
										xmlPullParser.getAttributeValue(i));*/
								result.put("datatablename", xmlPullParser.getAttributeValue(i));
							} else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.URL.getValue())) {
								result.put("url", xmlPullParser.getAttributeValue(i));
								/*result.add(1,
										xmlPullParser.getAttributeValue(i));*/
							}else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.HASUSERID.getValue())) {
/*								result.add(2,
										xmlPullParser.getAttributeValue(i));*/
								result.put("hasuserid",xmlPullParser.getAttributeValue(i));
								
							}else if (xmlPullParser.getAttributeName(i)
									.equalsIgnoreCase(
											OMSMessages.SCHEMA_MESSAGE.getValue())) {
								result.put("schema",xmlPullParser.getAttributeValue(i));
								
							}
						}
					}else if(tagName
							.equalsIgnoreCase("loadingmessage")
							&& !xmlPullParser.isEmptyElementTag()){
						isLoadingMessage=true;
					} else if (tagName.equalsIgnoreCase("successmessagecolumn")&& !xmlPullParser.isEmptyElementTag()){
						isSuccessMessageColumn=true;
					}else if (tagName.equalsIgnoreCase("successmessagetext")&& !xmlPullParser.isEmptyElementTag()){
						isSuccessMessage=true;
					}
					break;
				case XmlPullParser.END_TAG:
					if(tagName
							.equalsIgnoreCase("loadingmessage")){
						isLoadingMessage=false;
					} else if (tagName.equalsIgnoreCase("successmessagecolumn")){
						 isSuccessMessageColumn=false;
						
					}else if (tagName.equalsIgnoreCase("successmessagetext")){
						 isSuccessMessage=false;
					}
						
					break;
					
				case XmlPullParser.TEXT:
					if(isLoadingMessage){
						tagText=xmlPullParser.getText();
						result.put("loadingmessage",tagText);
					}else if(isSuccessMessageColumn){
						tagText=xmlPullParser.getText();
						result.put("successmessagecolumn",tagText);
					}else if(isSuccessMessage){
						tagText=xmlPullParser.getText();
						result.put("successmessagetext",tagText);
					}
					break;
				}

				eventType = xmlPullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}
	
	/**
	 * parses XML string of Bl type Dialog and returns a map of element names
	 * and values.
	 * 
	 * @param xmlResponse
	 *            String
	 * @return map
	 */
	public Map<String,String> processDialog(String xmlResponse) {
		Map<String,String> blDialogMap = new HashMap<String, String>();
		String tagName = null;
		try{
				XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
						.newInstance();
				xmlPullParserFactory.setNamespaceAware(true);
				XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
				xmlPullParser.setInput(new StringReader(xmlResponse));
				int eventType = xmlPullParser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					tagName = xmlPullParser.getName();
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
		
					case XmlPullParser.END_DOCUMENT:
						break;
		
					case XmlPullParser.START_TAG:
						if (tagName
								.equalsIgnoreCase(OMSDatabaseConstants.BL_DIALOG)) {
							for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
								if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
										OMSMessages.MESSAGE.getValue())) {
									blDialogMap.put(OMSMessages.MESSAGE.getValue(),xmlPullParser
											.getAttributeValue(i));
								} else if (xmlPullParser.getAttributeName(i)
										.equalsIgnoreCase(OMSMessages.BUTTON1.getValue())) {
									blDialogMap.put(OMSMessages.BUTTON1.getValue(),xmlPullParser
											.getAttributeValue(i));
								} else if (xmlPullParser.getAttributeName(i)
										.equalsIgnoreCase(OMSMessages.BUTTON2.getValue())) {
									blDialogMap.put(OMSMessages.BUTTON2.getValue(),xmlPullParser
											.getAttributeValue(i));
								}
							}
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = xmlPullParser.next();
					
				}
		
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return blDialogMap;
	}
	
	
	/**
	 * parses XML string of Bl type Toast and returns a map of element names
	 * and values.
	 * 
	 * @param xmlResponse
	 *            String
	 * @return map
	 */
	public Map<String,String> processToast(String xmlResponse) {
		Map<String,String> blDialogMap = new HashMap<String, String>();
		String tagName = null;
		try{
				XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
						.newInstance();
				xmlPullParserFactory.setNamespaceAware(true);
				XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
				xmlPullParser.setInput(new StringReader(xmlResponse));
				int eventType = xmlPullParser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					tagName = xmlPullParser.getName();
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
		
					case XmlPullParser.END_DOCUMENT:
						break;
		
					case XmlPullParser.START_TAG:
						if (tagName
								.equalsIgnoreCase(OMSDatabaseConstants.BL_TOAST)) {
							for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
								if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(OMSMessages.MESSAGE1.getValue())) {
									blDialogMap.put(OMSMessages.MESSAGE1.getValue(),xmlPullParser
											.getAttributeValue(i));
								} else if (xmlPullParser.getAttributeName(i)
										.equalsIgnoreCase(OMSMessages.MESSAGE2.getValue())) {
									blDialogMap.put(OMSMessages.MESSAGE2.getValue(),xmlPullParser
											.getAttributeValue(i));
								} 
							}
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = xmlPullParser.next();
					
				}
		
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return blDialogMap;
	}

	private String getColumnType(String tableName, String columnName)
	{
		
		String columnType = "TEXT";
		Cursor PragmaCursor = null;
		try {
		if(tableName!= null && !tableName.equalsIgnoreCase(""))
		{
			PragmaCursor = TransDatabaseUtil.getTransDB().rawQuery(
					"pragma table_info(" + tableName + ");", null);
			if (PragmaCursor.getCount() > 0) {
				if (PragmaCursor.moveToFirst()) {
					do {
						String columnname = PragmaCursor
								.getString(PragmaCursor
										.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME1));
						if (columnname.equalsIgnoreCase(columnName.trim())) {
							columnType = PragmaCursor
									.getString(PragmaCursor
											.getColumnIndex(OMSDatabaseConstants.PRAGMA_COLUMN_NAME2));
						}
					} while (PragmaCursor.moveToNext());
				}
	
			}
		}
		}
		finally{
			if(PragmaCursor!=null){
			PragmaCursor.close();}
		}
		return columnType;
	
	}
	
	
	
	public List<BLGlobalDTO> parseGlobalBLXML(String xmlResponse)
			throws XmlPullParserException, IOException {
		BLColumnDTO column = null;
		List<BLColumnDTO> destinationColList = null;
		List<BLColumnDTO> sourceColList = null;
		List<BLGlobalDTO> IUList = new ArrayList<BLGlobalDTO>();
		BLGlobalDTO iu = new BLGlobalDTO();
		boolean isDestination = false;
		boolean isSource = false;
		String tagName = null;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlResponse));
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.END_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if (tagName.equalsIgnoreCase("glbl")) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("isforceupdate")) {
							iu.isForceUpdate = Boolean.parseBoolean(xmlPullParser
									.getAttributeValue(i));
							
						}
				}
				}
				else  if (tagName.equalsIgnoreCase(COLUMN)) {
					column = new BLColumnDTO();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.COLUMN_NAME.getValue())) {
							column.columnName = xmlPullParser
									.getAttributeValue(i);
						} 
					}
				} else if (tagName.equalsIgnoreCase(OMSMessages.SOURCE
						.getValue())) {
					isSource = true;
					sourceColList = new ArrayList<BLColumnDTO>();
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSMessages.DB_TABLENAME.getValue())) {
							iu.sourceTableName = xmlPullParser
									.getAttributeValue(i);
						} 

					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.GLBL.getValue())) {
					IUList.add(iu);
				}

				else if (tagName.equalsIgnoreCase(COLUMN)) {
					if (isSource) {
						sourceColList.add(column);
					}
				} else if (tagName.equalsIgnoreCase(OMSMessages.SOURCE
						.getValue())) {
					isSource = false;
					iu.sourceColList = sourceColList;
				}
				break;
			}

			eventType = xmlPullParser.next();

		}
		return IUList;
	}

	/**
	 * parses XML string of Bl type Get and returns a list of  getBL data
	 * and values.
	 * 
	 * @param xmlResponse ,methodtype
	 *            String
	 * @return list
	 */
	
	public List<BLGetDTO> processGetBL(String xmlResponse, String methodType) throws XmlPullParserException, IOException{
		boolean isLoadingMessage=false;
		boolean isPaginationParam=false;

		BLColumnDTO column = null;
		List<BLColumnDTO> getBLColList = null;
		String tagName = null;
	    String tagText;
	    List<BLGetDTO> getBLList = new ArrayList<BLGetDTO>();
	    BLGetDTO blGet = new BLGetDTO();
	    boolean isColumn=false;
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
				.newInstance();
		xmlPullParserFactory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(new StringReader(xmlResponse));
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = xmlPullParser.getName();
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if (tagName.equalsIgnoreCase(methodType)) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.DB_TABLENAME.getValue())) {
							blGet.getDataTable = xmlPullParser
									.getAttributeValue(i);
							
						}else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.URL.getValue())) {
							blGet.getURL=xmlPullParser
 									.getAttributeValue(i);
							
						}else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.HASUSERID.getValue())) {
							blGet.hasUserId= xmlPullParser
									.getAttributeValue(i);
						}else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.HAS_MODIFIED_DATE.getValue())) {
							blGet.hasModifiedDate= xmlPullParser
									.getAttributeValue(i);
						}else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(
										OMSMessages.HAS_LOCATION.getValue())) {
							blGet.hasLocation= xmlPullParser
									.getAttributeValue(i);
						}else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase("haspagination")) { //Pagination Code
							blGet.hasPagination= xmlPullParser
									.getAttributeValue(i);
						}
					}
				}else if (tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_COLUMN.getValue())
						&& !xmlPullParser.isEmptyElementTag()) {
					isColumn=true;
					column = new BLColumnDTO();
				}else if(tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_QUERY_PARAMS.getValue())
						&& !xmlPullParser.isEmptyElementTag()){
					getBLColList = new ArrayList<BLColumnDTO>();
				}else if(tagName
						.equalsIgnoreCase("loadingmessage")
						&& !xmlPullParser.isEmptyElementTag()){
					isLoadingMessage=true;
				}else if(tagName
						.equalsIgnoreCase("paginationparam")
						&& !xmlPullParser.isEmptyElementTag()){ //Pagination Code
					isPaginationParam=true;
				}
				break;

			case XmlPullParser.END_DOCUMENT:
				break;
			case XmlPullParser.START_DOCUMENT:
				break;
				
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase(OMSMessages.BL_GET_COLUMN.getValue())) {
					getBLColList.add(column);
					isColumn=false;
				}else if (tagName.equalsIgnoreCase(methodType)) {
					getBLList.add(blGet);
				}else if(tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_QUERY_PARAMS.getValue())){
					blGet.getColList=getBLColList;
					
				}else if(tagName
						.equalsIgnoreCase("loadingmessage")){
					isLoadingMessage=false;
				}else if(tagName
						.equalsIgnoreCase("paginationparam")){ //Pagination Code
					isPaginationParam=false;
				}
				break;
				
			case XmlPullParser.TEXT:
			//	if (tagName.equalsIgnoreCase(OMSMessages.BL_GET_COLUMN.getValue())) {
				if(isColumn){
				    tagText=xmlPullParser.getText();
					column.columnName=tagText;
				} else if(isLoadingMessage){
					tagText=xmlPullParser.getText();
					blGet.blGetLoadingMessage=tagText;
				} else if(isPaginationParam){  //Pagination Code
						tagText=xmlPullParser.getText();
						blGet.paginationParam=tagText;
					}
				//}
				
				break;
			}
			eventType = xmlPullParser.next();
		}
		return getBLList;
	}
	
	
	   public List<BLFileDownloadDTO> parseFileDownLoadURL(String xmlResponse,String usid)  throws XmlPullParserException, IOException {
		   List<BLFileDownloadDTO> getFileDownloadList = new ArrayList<BLFileDownloadDTO>();
		   BLColumnDTO column = null;
		    List<BLColumnDTO> getBLColList = null;
		   String tagText;
		   boolean isLoadingMessage=false;
		   boolean isColumn = false;
		   BLFileDownloadDTO blfileDownload = new BLFileDownloadDTO();
		   String tagName = null;
		   XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
					.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlResponse));
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				tagName = xmlPullParser.getName();
				switch (eventType) {
			case XmlPullParser.START_TAG:
				if (tagName.equalsIgnoreCase("filedownload")) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						 if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("tablename")) {
							blfileDownload.tableName=xmlPullParser
									.getAttributeValue(i);
							
						} else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("urlcolumnname")) {
							blfileDownload.urlColName=xmlPullParser
									.getAttributeValue(i);
							
						} else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("filecolname")) {
							blfileDownload.fileColName=xmlPullParser
									.getAttributeValue(i);
							
						} else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("columnname")) {
							blfileDownload.updateColumn=xmlPullParser
									.getAttributeValue(i);
						}else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("filetype")) {
							blfileDownload.fileType=xmlPullParser
									.getAttributeValue(i);
						}
					}
				} else if (tagName.equalsIgnoreCase("loadingmessage")) {
					isLoadingMessage=true;
				} else if (tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_COLUMN.getValue())
						&& !xmlPullParser.isEmptyElementTag()) {
					isColumn=true;
					column = new BLColumnDTO();
				}else if(tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_QUERY_PARAMS.getValue())
						&& !xmlPullParser.isEmptyElementTag()){
					getBLColList = new ArrayList<BLColumnDTO>();
				}
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			case XmlPullParser.START_DOCUMENT:
				break;
				
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase("filedownload")) {
					getFileDownloadList.add(blfileDownload);
				}else if (tagName.equalsIgnoreCase(OMSMessages.BL_GET_COLUMN.getValue())) {
					getBLColList.add(column);
					isColumn=false;
				}else if(tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_QUERY_PARAMS.getValue())){
					blfileDownload.getColList = getBLColList;
					
				}
	          	break;
				
			case XmlPullParser.TEXT:
				if(isLoadingMessage){
					 tagText=xmlPullParser.getText();
					 blfileDownload.loadingText=tagText;
					 isLoadingMessage=false;
				}else if(isColumn){
				    tagText=xmlPullParser.getText();
					column.columnName=tagText;
				}
				break;
			}
				eventType = xmlPullParser.next();
		 }
			return getFileDownloadList;
	   }
	   
	   
	   public List<BLFileUploadDTO> parseFileUpLoadURL(String xmlResponse,String usid)  throws XmlPullParserException, IOException {
		   List<BLFileUploadDTO> getFileUploadList = new ArrayList<BLFileUploadDTO>();
		   BLColumnDTO column = null;
		    List<BLColumnDTO> getBLColList = null;
		   String tagText;
		   boolean isColumn = false;
		   boolean isLoadingMessage=false;
		   BLFileUploadDTO blfileUpload = new BLFileUploadDTO();
		   String tagName = null;
		   XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
					.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlResponse));
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				tagName = xmlPullParser.getName();
				switch (eventType) {
			case XmlPullParser.START_TAG:
				if (tagName.equalsIgnoreCase("fileupload")) {
					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("serverurl")) {
							blfileUpload.fileUploadURL = xmlPullParser
									.getAttributeValue(i);
							
						} else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("tablename")) {
							blfileUpload.tableName=xmlPullParser
									.getAttributeValue(i);
							
						}else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("columnname")) {
							blfileUpload.updateColumn=xmlPullParser
									.getAttributeValue(i);
						}else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("filetype")) {
							blfileUpload.fileType=xmlPullParser
									.getAttributeValue(i);
						}else if (xmlPullParser
								.getAttributeName(i)
								.equalsIgnoreCase("filecolname")) {
							blfileUpload.fileColName=xmlPullParser
									.getAttributeValue(i);
						}
					}
				} else if (tagName.equalsIgnoreCase("loadingmessage")) {
					isLoadingMessage=true;
				} else if (tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_COLUMN.getValue())
						&& !xmlPullParser.isEmptyElementTag()) {
					isColumn=true;
					column = new BLColumnDTO();
				}else if(tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_QUERY_PARAMS.getValue())
						&& !xmlPullParser.isEmptyElementTag()){
					getBLColList = new ArrayList<BLColumnDTO>();
				}
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			case XmlPullParser.START_DOCUMENT:
				break;
				
			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase("fileupload")) {
					getFileUploadList.add(blfileUpload);
				}else if (tagName.equalsIgnoreCase(OMSMessages.BL_GET_COLUMN.getValue())) {
					getBLColList.add(column);
					isColumn=false;
				}else if(tagName
						.equalsIgnoreCase(OMSMessages.BL_GET_QUERY_PARAMS.getValue())){
					blfileUpload.getColList = getBLColList;
					
				}
	          	break;
				
			case XmlPullParser.TEXT:
				if(isLoadingMessage){
					 tagText=xmlPullParser.getText();
					 blfileUpload.loadingText=tagText;
					 isLoadingMessage=false;
				}else if(isColumn){
				    tagText=xmlPullParser.getText();
					column.columnName=tagText;
				}
				break;
			}
				eventType = xmlPullParser.next();
		 }
			return getFileUploadList;
	   }

	   
	   
	   public BLStringModifierDTO parseStringModifier(String xmlResponse) throws XmlPullParserException, IOException {
		   int operandCount=0; 
		   boolean isOperandTag=false;
		   BLStringModifierOperandDTO operatorDTO = null;
		   BLStringModifierDTO blStringModifierDTO = new BLStringModifierDTO();
		   HashMap<String,String> stringModifierHash = new  HashMap<String,String>();
		   HashMap<Integer,BLStringModifierOperandDTO> stringModifierOperandHash = new HashMap<Integer,BLStringModifierOperandDTO>();
		   HashMap<Integer,String> stringModifierOperatorHash = new HashMap<Integer,String>();
		   String tagName = null;
		   XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
					.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlResponse));
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				tagName = xmlPullParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (tagName.equalsIgnoreCase("StringModifier")) {
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase("tablename")) {
								stringModifierHash.put("table", xmlPullParser
										.getAttributeValue(i));
								
							}if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase("database")) {
								stringModifierHash.put("database", xmlPullParser
										.getAttributeValue(i));
								
							}
						}
					}else if (tagName.equalsIgnoreCase("result")) {
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase("columnname")) {
								stringModifierHash.put("messageColumnName", xmlPullParser
										.getAttributeValue(i));
								
							}
						}
					} else if (tagName.equalsIgnoreCase("input")){
						isOperandTag = true;
					}else if(tagName.equalsIgnoreCase("operand")){
						operatorDTO = new BLStringModifierOperandDTO();
						operandCount=operandCount+1;
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase("type")) {
								operatorDTO.operandType=  xmlPullParser.getAttributeValue(i);
							}else if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase("columnname")) {
								operatorDTO.columnName = xmlPullParser
										.getAttributeValue(i);
							}
						}
						stringModifierOperandHash.put(operandCount, operatorDTO);
					}else if(tagName.equalsIgnoreCase("operator")){
						for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
							if (xmlPullParser
									.getAttributeName(i)
									.equalsIgnoreCase("name")) {
								stringModifierOperatorHash.put(operandCount, xmlPullParser
										.getAttributeValue(i));
							}
						}
					}
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_TAG:
					if (tagName.equalsIgnoreCase("input")){
						isOperandTag = false;
						blStringModifierDTO.stringModifierOperandHash = stringModifierOperandHash;
						blStringModifierDTO.stringModifierOperatorHash = stringModifierOperatorHash;
					}else if (tagName.equalsIgnoreCase("StringModifier")){
						blStringModifierDTO.stringModifierHash = stringModifierHash;
					}
					break;
				case XmlPullParser.TEXT:	
					break;
				}
				eventType = xmlPullParser.next();
			}
			return blStringModifierDTO;
	   }
	   
}

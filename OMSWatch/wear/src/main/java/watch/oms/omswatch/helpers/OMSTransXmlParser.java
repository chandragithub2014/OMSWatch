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
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;


import watch.oms.omswatch.OMSDTO.ColumnDTO;
import watch.oms.omswatch.OMSDTO.DatabaseDTO;
import watch.oms.omswatch.OMSDTO.TableDTO;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;

/**
 * TransDBXMLParser: Parses TransDB XML String and returns list of DataBase,
 * Table and column data.
 * 
 * @author 280779
 * 
 */
public class OMSTransXmlParser {

	private final String TAG = this.getClass().getSimpleName();

	public OMSTransXmlParser(Context context) {
	}

	/**
	 * parses TransDB XML String and returns list of DataBase, Table and column
	 * data.
	 * 
	 * @param xmlResponse
	 * @return List of DatabaseDTO.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public ArrayList<DatabaseDTO> parseTransDBXML(String xmlResponse)
			throws XmlPullParserException, IOException {

		String tagName = null;
		ColumnDTO colDTO = null;
		TableDTO tblDTO = null;
		DatabaseDTO dbDTO = null;
		String dbVersion = null;
		String dbName = null;
		String tableName = null;
		int version = OMSDefaultValues.NONE_DEF_CNT.getValue();
		String name = null;
		String type = null;
		String constraint = null;
		String defaultVal = null;
		ArrayList<DatabaseDTO> dbDTOList = null;
		ArrayList<ColumnDTO> columnDTOList = null;
		ArrayList<TableDTO> tableDTOList = null;

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
				if (tagName.equalsIgnoreCase(OMSConstants.DATABASE)
						&& !xmlPullParser.isEmptyElementTag()) {
					tableDTOList = new ArrayList<TableDTO>();
					dbDTOList = new ArrayList<DatabaseDTO>();
					dbDTO = new DatabaseDTO();

					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSConstants.VERSION)) {
							dbVersion = xmlPullParser.getAttributeValue(i);
							Log.d(TAG, "DB VERSION: [" + dbVersion + "]");
							dbDTO.dbVersion = dbVersion;
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(OMSConstants.NAME)) {
							dbName = xmlPullParser.getAttributeValue(i);
							Log.d(TAG, "DB NAME: [" + dbName + "]");
							dbDTO.dbName = dbName;
						}
					}
				} else if (tagName.equalsIgnoreCase(OMSConstants.TABLE)) {
					columnDTOList = new ArrayList<ColumnDTO>();
					tblDTO = new TableDTO();

					for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
						if (xmlPullParser.getAttributeName(i).equalsIgnoreCase(
								OMSConstants.NAME)) {
							tableName = xmlPullParser.getAttributeValue(i);
							tblDTO.tbName = tableName;
						} else if (xmlPullParser.getAttributeName(i)
								.equalsIgnoreCase(OMSConstants.VERSION)) {
							version = Integer.parseInt(xmlPullParser
									.getAttributeValue(i));
							tblDTO.version = version;
						}
					}

				} else if (tagName.equalsIgnoreCase(OMSConstants.COLUMN)) {
					colDTO = new ColumnDTO();
				} else if (tagName.equalsIgnoreCase(OMSConstants.NAME)) {
					name = xmlPullParser.nextText().trim();
					colDTO.name = name;
				} else if (tagName.equalsIgnoreCase(OMSConstants.TYPE)) {
					type = xmlPullParser.nextText().trim();
					colDTO.type = type;
				} else if (tagName.equalsIgnoreCase(OMSConstants.CONSTRAINT)) {
					constraint = xmlPullParser.nextText().trim();
					colDTO.Constraint = constraint;
				} else if (tagName.equalsIgnoreCase(OMSConstants.DEFAULT_VALUE)) {
					defaultVal = xmlPullParser.nextText().trim();
					colDTO.defaultVal = defaultVal;
				}
				break;

			case XmlPullParser.END_TAG:
				if (tagName.equalsIgnoreCase(OMSConstants.DATABASE)) {
					if(dbDTO != null){
						dbDTO.tableDTOList = tableDTOList;
						dbDTOList.add(dbDTO);
					}
				} else if (tagName.equalsIgnoreCase(OMSConstants.TABLE)) {
					tblDTO.columnDTOList = columnDTOList;
					tableDTOList.add(tblDTO);
				} else if (tagName.equalsIgnoreCase(OMSConstants.COLUMN)) {
					if(!colDTO.name.equalsIgnoreCase("_id"))
						columnDTOList.add(colDTO);
				}
				break;

			}

			eventType = xmlPullParser.next();
		}
		return dbDTOList;
	}

}

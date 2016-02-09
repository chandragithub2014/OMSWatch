/*
 * Copyright (C) 2013 - Cognizant Technology Solutions.
 * This file inputStream a part of OneMobileStudio
 * Licensed under the OneMobileStudio, Cognizant Technology Solutions, 
 * Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.cognizant.com/
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License inputStream distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package watch.oms.omswatch.constants;

/**
 * Contains all the string/character constants used in the AIM container
 * 
 * @author 265838
 * 
 */
public enum OMSMessages {
	/**
	 * Success and Failure Strings
	 */
	TRANS_DATABASE_SUCCESS("TransDataBaseSuccess"), TRANS_DATABASE_FAILURE(
			"TransDataBaseFailure"), POLICY_DATABASE_SUCCESS(
			"PolicyDataBaseSuccess"), POLICY_DATABASE_FAILURE(
			"PolicyDataBaseFailure"), CONFIG_DATABASE_PARSE_SUCCESS(
			"ConfigDBParseSuccess"), ACTION_CENTER_SUCCESS(
			"ActionCenterSuccess"), GET_SUCCESS("GetSuccess"), ACTION_CENTER_FAILURE(
			"ActionCenterFailure"), TRANS_DB_ERROR("TransDBError"), CONFIG_DB_ERROR(
			"ConfigDBError"), FAILED("Failed"), GET_FAILURE("GetFailure"), ERROR(
			"error"), ERROR_DESCRIPTION("error_description"), TOOLBAR_REFRESH(
			"ToolBarRefresh"), SUCCESS("Success"), FILE_UPLOAD_FAILED(
			"fileUploadFailed"), FAIL("Fail"), FAILURE("failure"), CONFIGDB_PARSE_FAILURE(
			"ConfigDBParseFailure"), BL_SUCCESS("BLSuccess"),TABLE_BL_SUCCESS("TableBLSuccess"), BL_FAILURE(
			"BLFailure"),TABLE_BL_FAILURE("TableBLFailure"), NETWORK_RESPONSE_ERROR("NetworkResponseError"),
	
	/**
	 * General strings
	 */
	PATH_SEP_WIN("/"), NEWLINE_CHAR("\n"), MIN_INDEX_STR("0"), ALL("ALL"), DEFAULT_JSON_CODE(
			"00"), DEFAULT_VALUE("1"), SERVER_URL("SERVER_URL"), SENDER_ID(
			"SENDER_ID"), LESSER_THAN("<"), PERCENTAGE_CHAR("%"), GREATER_THAN(
			">"), OPEN_BRACES("("), CLOSE_BRACES(")"), COMMA(","), HYPEN_CHAR(
			"-"), CARRIAGE_RETURN_CHAR("\r"), PLUS_CHAR("+"), COLON(":"), AM(
			"am"), PM("pm"), DOT_CHAR("."), HASH("#"), CLOSE_SQUARE_BRACES("]"), OPEN_SQUARE_BRACES(
			"["), SEMICOLON(";"), ICON_NAME("ic_core_small_no_image"), DRAWABLE_STRING(
			"drawable"), ON("ON"), OFF("OFF"), ALL_SMALL("all"), OK("Ok"), EXCEPTION(
			"Exception"), EQUAL_CHAR(" = "), SECONDS(" seconds"), NO_FILE(
			"No File"), REFRESH("Refresh"), TEL_PREFIX("tel:"), EMAIL_PREFIX(
			"Email:"), DEFAULT("default"), SELECT("Select"), CANCEL("Cancel"), UPLOAD(
			"Upload"), DB_TABLENAME("tablename"), DB_SCHEMA("schema"),
	/**
	 * HTTP Strings
	 */
	USERNAME(""), PASSWORD(""), PROXY_URL("proxy.cognizant.com"), ACCEPT(
			"Accept"), APP_JSON("application/json"), CONTENT_TYPE(
			"Content-type"), GET("get"), POST("POST"),FILE_DOWNLOAD("filedownload"), FILE_UPLOAD("fileupload"), USERNAME_STRING(
			"username"), PASSWORD_STRING("password"), CONNECTION_PREFIX("CON"), HTTP_PREFIX(
			"http://"), HTTPS_PREFIX("https://"), HTTP_HOST("http.proxyHost"), HTTP_PORT(
			"http.proxyPort"), PROXY_PORT("6050"), PARAM_NAME("param_name"), VALUE_NAME(
			"value_name"),LOGIN_SUCCESS("success"),LOGIN_FAILURE_INVALID_USER("Invalid User Id/Password"),
			LOGIN_FAILURE_SERVICE_INCORRECT("Invalid service url"),

	/**
	 * Bundle Constant
	 */
	UNIQUE_ID("uniqueid"), UI_HEADING("title"), SCREEN_ORDER("screenOrder"), CUSTOM_CONTAINERID(
			"customContainerId"), CONFIGAPP_ID("configAppId"), SCREEN_MODE(
			"screenmode"), SCREEN_TYPE("screentype"), FILTER_COLUMN_NAME("filterColumnName"), FILTER_COLUMN_VALUE(
			"filterColumnVal"), TRANS_USID("transusid"), IS_PREPOPULATED(
			"isPrepopulated"), ACTION_BUTTON_ID("actionbuttonid"), EVENT_DATE(
			"eventDate"), CONTAINER_ID("containerId"), TITLE("title"), NULL_STRING(
			"null"), NULL_CAPS_STRING("NULL"), SHOW_LOGOUT("showLogout"), SCREEN_ID(
			"SCREEN_ID"), CONTAINERID_CAPS("CONTAINER_ID"), LIST_USID("listusid"),SPLIT_VIEW(
			"splitview"), NAV_ID("nav_Id"), SCREENID("screenID"), IS_FROM_NAVIGATION(
			"isFromNavigation"), IS_LOADSCREEN("isLoadScreen"), SIGNATURE(
			"signature"), IMAGE_PATH("ALL_IMAGES_PATH"), IMAGE_INDEX(
			"SELECTED_IMAGE_INDEX"), SEARCH_WHERE_CLAUSE("searchWhereClause"), TABLENAME(
			"tableName"), SELECTIONARG("selectionarg"), SELECTARG2("selectarg2"), LIST_DTO(
			"listdto"), FROM_SECTIONLIST("fromSectionList"), SHOW_ADD("showAdd"),SHOW_ADV_SEARCH("showAdvSearch"), COLUMN(
			"column"), TYPE("type"), VIDEO_PATH("VIDEO_PATH"), COLUMN_COUNT(
			"columncount"), ALIGNMENT("alignment"),BUTTONTYPE("buttontype"),BLNAME("blname"),BUTTONURL("url"),INDEX("index"),WIDTH("width"),HEIGHT("height"), NAV_USID("nav_usID"), SHOW_TABLE(
			"showTable"), SHOW_EDIT("showEdit"), FILE_PATH("filePath"), ADD_TITLE(
			"Add Data"),STYLE("style"), ADD_EVENT("addEvent"), IMAGE_NAMES("ALL_IMAGES_NAMES"),IMAGE_LOCAL("IMAGE_LOCAL"), DB_SCHEMA_NAME(
			"schemaname"), SECTION_ITEM_NAME("sectionItemName"),OMS_TITLE("One Mobile Studio"),QUERY_PARAMETERS(
					"queryparameters"),IS_BACK("isback"),
	/**
	 * Json/XML Strings
	 */
	ACTIONCENTER_JSON_FILENAME("actionCenter.json"), RESULT_ARRAY("resultArray"), SOURCE_JS(
			"sourceJS"), DB_EXT(".db"), ROWID_DEF("-999"), RESPONSE_STRING(
			"response"), MESSAGE("message"), CODE("code"), LOGIN("login"), AUTHENTICATION_STATUS(
			"AuthenticationStatus"), PROCESSED_DATE("processedDate"), DUMMY_JSON_FILENAME(
			"dummyjson.json"), ID("id"), SHOW_COLOR("showcolor"), NAME("name"), CONTENT(
			"content"), RESULTS("results"), YES("Yes"), NO("No"), STOP("Stop"), START(
			"Start"), FILE("file"), JPG_EXTN(".jpg"), VISITED_DATE(
			"visiteddate"), RESULT("result"), PRIMARY_KEY("primarykey"), COLUMN_TYPE(
			"columntype"), COLUMN_NAME("columnname"), URL_COLUMN_NAME("urlcolumnname"),FILE_TYPE("filetype"),
			FILE_COLUMN_NAME("filecolname"),COLUMN_VALUE("columnvalue"), TABLE("table"), DB("db"), DESTINATION("Destination"), SOURCE(
			"Source"), SCHEMA_NAME("schemaname"), DEFAULT_VALUE_STRING(
			"defaultvalue"), CONSTRAINT("constraint"), OPERAND("operand"), OPERATOR(
			"operator"), FALSE("false"), OPERAND1("operand"), OPERAND2(
			"operand"), FINAL_VAL("finalval"), COL_VAL("colval"), BL_SOURCE(
			"source"),SOURCE_WHERE("wherecolumn"),DEST_WHERE("wherecolumn"),IS_GLOBAL("isglobal"),
	/**
	 * AppMonitor
	 */
	FEATURE1("feature1"), FEATURE2("feature2"), FEATURE3("feature3"), FEATURE4(
			"feature4"), FEATURE5("feature5"), ERROR_LOG_FILENAME("error.log"), LOG_FILE_EXTENSION(
			".log"), STATUS_UNKNOW("UnKnown"), STATUS_CHARGING("Charging"), STATUS_DISCHARGING(
			"Discharging"), STATUS_NOCHARGE("No Charge"), STATUS_RECHARGE(
			"Recharge the Battery"), BATTERY_DISCHARGING(
			"Battery is Discharging"), BATTERY_FULL("Battery is Full"), BATTERY_NOSTATE(
			"No State"), TRANSACTION1("transaction1"), TRANSACTION2(
			"transaction2"), TRANSACTION3("transaction3"), TRANSACTION4(
			"transaction4"), TRANSACTION5("transaction5"), POLLING_VALUE(
			"pollingvalue"),

	/**
	 * MultiFormScreen Items Default Values
	 */
	DEFAULT_LEFT_MARGIN("60.0"), DEFAULT_RIGHT_MARGIN("10"), DEFAULT_WIDGET_WIDTH(
			"100"), DEFAULT_WIDGET_HEIGHT("100"), DEFAULT_TOP_MARGIN("5.0"), DEFAULT_BOTTOM_MARGIN(
			"5.0"),
	/**
	 * Maps
	 */
	LOCATION_TITLE("locationtitle"), LOCATION_DESCRIPTION("locationdescription"),

	/**
	 * BL
	 */
	INTEGER("integer"), TEXT("TEXT"), BIGINT("BIGINT"), CONSTANT("constant"), TRUE("true"), IS_DELETE(
			"isdelete"), ADD("add"), SIGMA("sigma"),MULTIPLY("multiply"), DIVISION("divide"), SUBTRACT(
			"subtract"), REAL("real"), BL_TYPE("bltype"), XML("xml"), TEXT_SMALL(
			"text"), GREATER("greater than"), LESSER("less than"), EQUALS(
			"equal to"),  AND("and"), OR("or"), REAL_CAPS(
			"REAL"), FOR("for"), DO("do"), INIT("init"), BREAK("break"), INITIAL_VALUE(
			"initialvalue"), VALUE("value"), IF("if"), PARENT_TABLE(
			"parenttable"), CONDITION("condition"), PARAMETER("parameter"), PK_FK_KEY(
			"pkfkkey"), ONE_MANY("OneToMany"), CONNECTIVITY_OPERATOR(
			"connectivityoperator"), NEXT("next"), COLUMN_INDEX("columnindex"), IU(
			OMSDatabaseConstants.BL_TYPE_IU), SAVE("Save"), EDIT("edit"), BL_ADD(
			"add"), BL_MULTIPLY("multiply"), BL_DIVIDE("divide"), BL_SUBTRACT(
			"subtract"), GREATER_THAN_STR("greater than"), LESS_THAN(
			"less than"), EQUAL_TO("equals"), NOT_EQUAL_TO("not equal to"), WHILE(
			"While"), DELETE("Delete"), BL_START("start"), PNG_EXTN(".png"),URL("url"),SERVERURL("serverurl"),
			MESSAGE1("message1"),MESSAGE2("message1"),BUTTON1("button1"),BUTTON2("button2"),
			BL_CANCEL("cancel"), ERROR_ADDITIONAL_MESSAGE("additionMessage"),HASUSERID("hasuserid"),
			GLBL(OMSDatabaseConstants.BL_GLOBAL_BL),SCHEMA_MESSAGE("schema"),HAS_MODIFIED_DATE("hasmodifieddate"),
			HAS_LOCATION("HAS_LOCATION"),BL_GET_COLUMN("column"),BL_GET_QUERY_PARAMS("queryparameters"),
			BL_DELETE_HARD_DELETE("harddelete"),
			

	/**
	 * Fonts Typeface
	 */
	SANS_SERIF("SANS_SERIF");
	/**
	 * A variable which hold the value of the message for <code>String</code>
	 */
	private String stringValue;

	/**
	 * An <code>String</code> argument constructor to assign the
	 * <code>String</code> value to the message
	 * 
	 * @param stringValue
	 */
	OMSMessages(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * A method to return the <code>String</code> value of the message
	 * 
	 * @return <code>String</code> value of the message
	 */
	public String getValue() {
		return stringValue;
	}

}

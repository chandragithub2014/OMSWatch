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
package watch.oms.omswatch.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OMSConstants {
	// General DB and URL Constants
	public static final String TRANSPARENT_COLOR_CODE = "#00000000";
	public static final String NULL_STRING = "null";
	public static final String DEFAULT_MODIFIED_TIME = "1.00";
	public static final String DEFAULT_WHERE_CLAUSE_FOR_DELETE = "1";
	public static final String IS_DELETE_ONE = "1";
	public static final String EMPTY_STRING = "";
	public static final String EQUALS = "=";
	public static final String HYPHEN = "-";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final long CHART_REFRESH_TIMER = 30000l;
	public static final String DATABASE_PATH = "/data/data/com.cognizant.oms/databases/";
	public static final String CONFIG_DB_NAME = "10.db";
	public static final String trans_xml_url = "http://www.twitter.com/statuses/public_timeline.json";
	public static final String SCHEMA_NAME = "oms";
	public static final String APP_ID = "444444";
	public static final int CONFIG_APP_ID = 10;	
	//	Production server url  "http://gtomobilecoe.cognizant.com
	//  TestServer URL 	 "http://shoponmobile.cognizant.com"

	public static final String SERVER_NAME = "http://gtomobilecoe.cognizant.com";

	public static final String CONFIG_GET = "/AiM/service/config/get/";
	public static final String TRANS_GET = "/AiM/service/trans/get/";
	public static final String TRANS_POST = "/AiM/service/trans/post/";
	public static final String PERIOD = ".";
	public static final String FILE_UPLOAD = "/AiM/file/upload/";
	public static final String DEVICE_REGISTER = "/omsnotification/register";
	
	public static final String POLICY_URL = SERVER_NAME
			+ "/AiM/service/trans/get/console/all";
	public static final String TILES_TYPE = "Tiles";
	public static final String CUSTOM_SCREEN_TYPE = "Custom Screen";
	public static final String PRE_POPULATED_MULTI_FORM_SCREEN_TYPE = "Prepopulatedmultiform";
	public static final String MULTI_FORM_SCREEN_TYPE = "Multi Form";
	public static final String LIST_SCREEN_TYPE = "List";
	public static final String GALLERY_SCREEN_TYPE = "Gallery";
	public static final String TAB_SCREEN_TYPE = "Tab Bar";
	public static final String MAP_SCREEN_TYPE = "Map View";
	public static final String FORM_SCREEN_SINGLE_COLUMN_TYPE = "Form Single Column";
	public static final String TABLE_SCREEN_TYPE = "Table Layout";
	public static final String SPRINGBOARD_SCREEN_TYPE = "Springboard";
	public static final String GRID_VIEW_SCREEN_TYPE = "GridView";
	public static final String CALENDAR_SCREEN_TYPE = "Calendar";
	public static final String MULTI_TAB_SCREEN_TYPE = "Multi Tab";
	public static final String COVERFLOW_SCREEN_TYPE = "Coverflow";
	public static final String WEBVIEW_SCREEN_TYPE = "WebView";
	public static final String MEDIA_VIEWER_SCREEN_TYPE = "MediaViewer";
	public static final String DIALOG_SCREEN_TYPE = "Dialog Box";
	public static final String CONGIGURATOR_LIST_TYPE = "List Configurator";
	public static final String LIST_NAVIGATION_TYPE = "List Navigation";
	public static final String SPLASH_SCREEN = "Splash Screen";
	public static final String LOG_OUT_SCREEN = "Log Out";
	
	public static final String MEDIA_TYPE = "Media";

	public static final String SLIDING_MENU_SCREEN_TYPE = "Sliding Menu";
	
	public static final String SCROLLER_TEMPLATE_TYPE = "Scroller";
	
	public static final String MIN_CHILD_POSITION_FOR_LIST = "-5";
	public static final String MAX_CHILD_POSITION_FOR_LIST = "1000000";

	// Graph Related Constants
	public static final String MOBILE_BI_TABLE_NAME_IN_CONFIG = "Chart";
	public static final String MOBILE_BI_DATA_TABLE_NAME = "datatablename";
	public static final String MOBILE_BI_DATE_SCALE = "datescale";
	public static final String MOBILE_BI_YAXISLABEL1 = "yaxislabel1";
	public static final String MOBILE_BI_YAXISLABEL2 = "yaxislabel2";
	public static final String MOBILE_BI_YAXISLABEL3 = "yaxislabel3";
	public static final String MOBILE_BI_YAXISLABEL4 = "yaxislabel4";
	public static final String MOBILE_BI_YAXISLABEL5 = "yaxislabel5";
	public static final String MOBILE_BI_YCOLUMN1 = "ycolumn1";
	public static final String MOBILE_BI_YCOLUMN2 = "ycolumn2";
	public static final String MOBILE_BI_YCOLUMN3 = "ycolumn3";
	public static final String MOBILE_BI_YCOLUMN4 = "ycolumn4";
	public static final String MOBILE_BI_YCOLUMN5 = "ycolumn5";
	public static final String MOBILE_BI_XAXISLABEL = "xaxislabel";
	public static final String MOBILE_BI_XAXIS_COLUMN_NAME = "xcolumnname";
	public static final String MOBILE_BI_IS_DATE = "isdate";
	public static final String MOBILE_BI_CHART_TYPE = "charttype";
	
	public static final String MOBILE_BI_TABLE_HEADER_LABEL_1 = "tablelabel1";
	public static final String MOBILE_BI_TABLE_HEADER_LABEL_2 = "tablelabel2";
	public static final String MOBILE_BI_TABLE_HEADER_LABEL_3 = "tablelabel3";
	public static final String MOBILE_BI_TABLE_HEADER_LABEL_4 = "tablelabel4";
	public static final String MOBILE_BI_TABLE_HEADER_LABEL_5 = "tablelabel5";
	
	public static final String MOBILE_BI_TABLE_COLUMN_1 = "tablecol1";
	public static final String MOBILE_BI_TABLE_COLUMN_2 = "tablecol2";
	public static final String MOBILE_BI_TABLE_COLUMN_3 = "tablecol3";
	public static final String MOBILE_BI_TABLE_COLUMN_4 = "tablecol4";
	public static final String MOBILE_BI_TABLE_COLUMN_5 = "tablecol5";
	
	public static final String BAR_CHART_TAG = "DisplayBarChartFragment";
	public static final int START_DATE = 2455927;
	public static final int END_DATE = 2456293;
	public static final String BAR_CHART_TYPE = "Bar Chart";
	public static final String LINE_CHART_TYPE = "Line Chart";
	public static final String PIE_CHART_TYPE = "Pie Chart";
	/*public static final String TABLET_BAR_CHART = "Tablet Bar Chart";
	public static final String TABLET_LINE_CHART = "Tablet Line Chart";
	public static final String TABLET_PIE_CHART = "Tablet Pie Chart";
	public static final String TABLET_BAR_WITH_TABLE = "Tablet Bar With Table";
	public static final String TABLET_LINE_WITH_TABLE = "Tablet Line With Table";
	public static final String TABLET_PIE_WITH_TABLE = "Tablet Pie With Table";*/
	public static final String SECTIONLIST_SCREEN_TYPE = "Section List";
	public static final String ANIMATION_SCREEN_TYPE = "animation";
	public static final String APP_VISION_SCREEN_TYPE = "App Vision";
	public static final String DATABASEBROWSER_SCREEN_TYPE = "DatabaseDesign";
	public static final String LOGIN_SCREEN_TYPE = "Login";
	public static final String MULTIFORM_SCREEN_TYPE = "Multi Form";
	public static final String IS_DATE = "1";
	public static final String CHART_TITLE = "title";

	// Action Types
	public static final String ACTION_TYPE_POST = "POST";
	public static final String ACTION_TYPE_GET = "GET";
	public static final String ACTION_TYPE_CALL = "CALL";
	public static final String ACTION_TYPE_SMS = "SMS";
	public static final String ACTION_TYPE_MAIL = "MAIL";
	public static final String ACTION_TYPE_BL = "BL";
	public static final String ACTION_TYPE_INSERT_OR_UPDATE = "IU";
	public static final String ACTION_TYPE_DELETE = "DELETE";
	public static final String ACTION_TYPE_ONE_TO_MANY = "OneToMany";
	public static final String ACTION_TYPE_MOBILE_STUDIO_UNIVERSAL_GET = "MobileStudioUniverseGet";
	public static final String ACTION_TYPE_CUSTOM_ACTION = "Custom Action";
	public static final String ACTION_TYPE_SOCIAL_INTEGRATION = "SocialIntegration";

	// RequestCode
	public static final int REQUEST_CODE = 1;
	public static final int TIMEOUT_CONNECTION = 5000;
	public static final int TIMEOUT_SOCKET = 5000;
	public static final int STATUSCODE_OK = 200;
	public static final int STATUSCODE_METHOD_NOT_ALLOWED = 405;
	public static final int STATUSCODE_METHOD_UNAUTHORIZED =401;

	public static final String FORM_FIELD_INPUT_TYPE_ALPHA_NUMERIC = "alphanumeric";
	public static final String FORM_FIELD_INPUT_TYPE_NUMERIC = "numeric";
	public static final String FORM_FIELD_INPUT_TYPE_PHONE = "phonenumber";
	public static final String FORM_FIELD_INPUT_TYPE_PASSWORD = "password";
	public static final boolean USE_PROXY = false;
	public static final boolean USE_DUMMY_JSON = false;

	// Post Related Constants
	public static final int FORM_ACTION_BUTTON_ID = 7;
	public static final int ADD_ACTION_BUTTON_ID = 5;
	public static final int DETAIL_ACTION_BUTTON_ID = 6;
	public static final int HOMOGENIOUS_ACTION_BUTTON_ID = -10;
	public static final int TABLE_SCREEN_CUSTOM_BUTTON_ID = 601;
	
	public static final int ADD_BUTTON_POSITION = -3;
	public static final int DETAIL_BUTTON_POSITION = -2;
	public static final int ADV_SEARCH_POSITION = -4;

	// Splash Related Constants
	public static final boolean USE_SPLASH = true;

	// Load MultiForm
	public static final boolean LOAD_MULTI_FORM_SCREEEN_AS_INITIAL = false;

	// Trans XML Constants
	public static final String DATABASE = "Database";
	public static final String VERSION = "Version";
	public static final String TABLE = "Table";
	public static final String NAME = "Name";
	public static final String COLUMN = "Column";
	public static final String TYPE = "Type";
	public static final String DEFAULT_VALUE = "Defaultvalue";
	public static final String CONSTRAINT = "Constraint";
	public static final String DEFAULT_FONT_COLOR_WHITE = "#000000";
	public static final String DEFAULT_FONT_SIZE = "12";
	public static final String DEFAULT_FONT_TYPE_FACE = "0";
	public static final String DEFAULT_FONT_NAME = "SANS_SERIF";
	public static final String DEFAULT_TAB_STYLE_NAME = "default";
	public static final String DEFAULT_TAB_BACKGROUND = "#000000";
	public static final String DEFAULT_ACTIONBAR_BACKGROUND = "#f3f3f3";
	public static final String DEFAULT_TAB_STATE_NORMAL = "#f3f3f3";
	public static final String DEFAULT_TAB_STATE_SELECTED = "#33b5e5";
	public static final String DEFAULT_TAB_FONT_STYLE = "default";
	public static final String HTTP_URI = "http://";
	public static final String HTTPS_URI = "https://";
	public static final String DEFAULT_FONT_STYLE_NAME = "default1";

	// ByPass Webservice call for ConfigDB
	public static final boolean BYPASS_WEBSERVICE_CALL_CONFIGDB = true;

	// Action Menu items related Constants
	public static final int SEARCH_BUTTON_ID = -0;
	public static final int DELETE_BUTTON_ID = -1;
	public static final int CANCLE_BUTTON_ID = -2;
	public static final int EDIT_BUTTON_ID = -3;
	public static final int SAVE_BUTTON_ID = -4;
	public static final int ADD_BUTTON_ID = -5;
	public static final int ADD_EVENT_BUTTON_ID = -6;
	public static final int PUBLISH_BUTTON_ID = -7;
	public static final int DESIGNER_BUTTON_ID = -7;
	public static final int ADV_SEARCH_BUTTON_ID = -8;
	public static final int LIST_SORT_BY_ID= -9;


	public static final String SEARCH_BUTTON_TEXT = "SEARCH";
	public static final String DELETE_BUTTON_TEXT = "DELETE";
	public static final String CANCLE_BUTTON_TEXT = "CANCLE";
	public static final String EDIT_BUTTON_TEXT = "EDIT";
	public static final String SAVE_BUTTON_TEXT = "SAVE";
	public static final String ADD_BUTTON_TEXT = "ADD";
	public static final String ADV_SEARCH_BUTTON_TEXT = "Filter By";
	public static final String DESIGNER_BUTTON_TEXT = "Designer";

	public static final String ADD_EVENT_BUTTON_TEXT = "ADD EVENT";
	public static final String PUBLISH_BUTTON_TEXT = "PUBLISH";

	// Push Notifications
	public static final boolean isEnableNotifications = false;
	// 1 for positive AppID, 0 for negative AppID, 2 for -200
	public static final int LAUNCH_SCREEN_ORDER_CONSTANT =0;
	public static final String[] TEMPLATES_LIST = { LOGIN_SCREEN_TYPE,
		TAB_SCREEN_TYPE, SPRINGBOARD_SCREEN_TYPE, LIST_SCREEN_TYPE,
		MULTI_FORM_SCREEN_TYPE, TILES_TYPE, CUSTOM_SCREEN_TYPE,
		GALLERY_SCREEN_TYPE, TABLE_SCREEN_TYPE, CALENDAR_SCREEN_TYPE,
		MULTI_TAB_SCREEN_TYPE, COVERFLOW_SCREEN_TYPE, WEBVIEW_SCREEN_TYPE,
		MAP_SCREEN_TYPE, BAR_CHART_TYPE, LINE_CHART_TYPE, PIE_CHART_TYPE,
		SECTIONLIST_SCREEN_TYPE, DIALOG_SCREEN_TYPE,
		MEDIA_VIEWER_SCREEN_TYPE, ANIMATION_SCREEN_TYPE };
	public static final Map<String, String> TABLE_MAP;
	static {
		HashMap<String, String> aMap = new HashMap<String, String>();
		aMap.put(LOGIN_SCREEN_TYPE, OMSDatabaseConstants.LOGIN_SCREEN_TABLE_NAME);
		aMap.put(TAB_SCREEN_TYPE, OMSDatabaseConstants.TAB_SCREEN_TABLE_NAME);
		aMap.put(SPRINGBOARD_SCREEN_TYPE,
				OMSDatabaseConstants.LIST_SCREEN_TABLE_NAME);
		aMap.put(LIST_SCREEN_TYPE, OMSDatabaseConstants.LIST_SCREEN_TABLE_NAME);

		aMap.put(MULTI_FORM_SCREEN_TYPE,
				OMSDatabaseConstants.MULTI_FORM_SCREEN_TABLE);
		aMap.put(TILES_TYPE, OMSDatabaseConstants.CUSTOM_SCREEN_TABLE);
		aMap.put(GALLERY_SCREEN_TYPE,
				OMSDatabaseConstants.GALLERY_SCREEN_TABLE_NAME);
		aMap.put(TABLE_SCREEN_TYPE, OMSDatabaseConstants.TABLE_SCREEN_TABLE_NAME);
		aMap.put(CALENDAR_SCREEN_TYPE,
				OMSDatabaseConstants.CALENDAR_SCREEN_TABLE_NAME);
		aMap.put(MULTI_TAB_SCREEN_TYPE,
				OMSDatabaseConstants.MULTI_TAB_SCREEN_TABLE_NAME);
		aMap.put(COVERFLOW_SCREEN_TYPE,
				OMSDatabaseConstants.LIST_SCREEN_TABLE_NAME);
		aMap.put(WEBVIEW_SCREEN_TYPE,
				OMSDatabaseConstants.WEBVIEW_SCREEN_TABLE_NAME);
		aMap.put(MAP_SCREEN_TYPE, OMSDatabaseConstants.MAP_SCREEN_TABLE_NAME);
		aMap.put(BAR_CHART_TYPE, "Dimension");
		aMap.put(LINE_CHART_TYPE, "Dimension");
		aMap.put(PIE_CHART_TYPE, "Dimension");
		aMap.put(SECTIONLIST_SCREEN_TYPE, OMSDatabaseConstants.GALLERY_SCREEN_TABLE_NAME);
		aMap.put(DIALOG_SCREEN_TYPE, "dialog");
		aMap.put(ANIMATION_SCREEN_TYPE,
				OMSDatabaseConstants.ANIMATION_SCREEN_TABLE_NAME);
		aMap.put(MEDIA_VIEWER_SCREEN_TYPE,
				OMSDatabaseConstants.MEDIA_VIEWER_SCREEN_TABLE_NAME);
		TABLE_MAP = Collections.unmodifiableMap(aMap);
	}

	// Style Guide NavUSID related to style guide application
	public static final String STYLE_GUIDE_APP_ID = "10";
	public static final int SERVERMAPPER_APP_ID = 10;

	public static final String ADDSCREEN_STYLE_NAVUSID = "1368006250647076";
	public static final String DETAILESCREEN_STYLE_NAVUSID = "1368005772367363";
	public static final String BUTTON_STYLE_NAVUSID = "1397793487743587";
	public static final String CHART_STYLE_NAVUSID = "1398311872448529";
	public static final String COVERFLOW_STYLE_NAVUSID = "1368001089406715";
	public static final String FONT_STYLE_NAVUSID = "1398249343041026";
	public static final String GALLERY_STYLE_NAVUSID = "1367910153664802";
	public static final String GRID_STYLE_NAVUSID = "1398148126839712";//"1401355271.504463";//
	public static final String MULTIFORM_STYLE_NAVUSID = "1398158138496136";
	public static final String MULTITAB_STYLE_NAVUSID = "1367990928871488";
	public static final String SEARCHBAR_STYLE_NAVUSID = "1367305722879905";
	public static final String SECTIONLIST_STYLE_NAVUSID = "1368012238976652";
	//public static final String TABCHART_STYLE_NAVUSID = "1368160521.165974";
	//public static final String TABCHARTWITHTABLE_STYLE_NAVUSID = "1368107797.054324";
	public static final String TABLE_STYLE_NAVUSID = "1398233806071623";
	public static final String TITLEBAR_STYLE_NAVUSID = "1398228968863788";
	public static final String TOOLBAR_STYLE_NAVUSID = "1398233053344089";
	public static final String TABBARSTYLE_NAVUSID = "1398160623319647";
	public static final String LIST_STYLE_NAVUSID = "1398073443336068";
	public static final String WIDGET_STYLE_NAVUSID="1373895620489038";
	
	public static final String ADD_ADDSCREEN_STYLE_NAVUSID = "1368006250647076";
	public static final String ADD_DETAILESCREEN_STYLE_NAVUSID = "1368005772367363";
	public static final String ADD_BUTTON_STYLE_NAVUSID = "1397649925584130";
	public static final String ADD_CHART_STYLE_NAVUSID = "1398311841351388";
	public static final String ADD_COVERFLOW_STYLE_NAVUSID = "1368595735207027";
	public static final String ADD_FONT_STYLE_NAVUSID = "1398249362260532";
	public static final String ADD_GALLERY_STYLE_NAVUSID = "1368595136449065";
	public static final String ADD_GRID_STYLE_NAVUSID = "1398151580839543";//"1401355296.958167";
	public static final String ADD_MULTIFORM_STYLE_NAVUSID = "1398158120160914";
	public static final String ADD_MULTITAB_STYLE_NAVUSID = "1368596098102492";
	public static final String ADD_SEARCHBAR_STYLE_NAVUSID = "1368596249175356";
	public static final String ADD_SECTIONLIST_STYLE_NAVUSID = "1368596398238826";
	//public static final String ADD_TABCHART_STYLE_NAVUSID = "1368595381.831003";
	//public static final String ADD_TABCHARTWITHTABLE_STYLE_NAVUSID = "1368595491.783092";
	public static final String ADD_TABLE_STYLE_NAVUSID = "1398233762352407";
	public static final String ADD_TITLEBAR_STYLE_NAVUSID = "1398229010304152";
	public static final String ADD_TOOLBAR_STYLE_NAVUSID = "1398233100601255";
	public static final String ADD_TABBARSTYLE_NAVUSID ="1398161344623206";
	public static final String ADD_LIST_STYLE_NAVUSID = "1398081642601856";
	public static final String ADD_WIDGET_STYLE_NAVUSID="1373900179128365";
	
	public static final String ADD_LIST_NAVIGATION_STYLE_NAVUSID= "1398245295022936";
	public static final String LIST_NAVIGATION_STYLE_NAVUSID= "1398245261702987";
	
	public static final String SWITCH_STYLE_NAVUSID="1398246803136596"; 
	public static final String ADD_SWITCH_STYLE_NAVUSID="1398246843800587";
	
	public static final String RATING_STYLE_NAVUSID="1398247600695835";
	public static final String ADD_RATING_STYLE_NAVUSID="1398247610360004";
	
	public static final String MF_LINE_SEPARATOR_STYLE_NAVUSID="1407308060995938";
	public static final String ADD_MF_LINE_SEPARATOR_NAVUSID="1407308037947377";

	//Badge Count styling
	public static final String ADD_BADGE_COUNT_STYLE_NAVUSID = "1424175158729";
	public static final String DETAIL_BADGE_COUNT_STYLE_NAVUSID = "1424175161980";
	
	//Checkbox styling
	public static final String ADD_CHECKBOX_STYLE_NAVUSID = "1424679306094";
	public static final String DETAIL_CHECKBOX_STYLE_NAVUSID = "1424679311662";
	
	public static final long CONFIGURATOR_SECTION_LIST_SCREEN_DESIGN_NAVUSID	=1372837882637268l;
	public static final long CONFIGURATOR_SECTION_LIST_STYLING_NAVUSID		=1367499068043996l;
	
	
	//Calendar Add/Edit screen Navigation Usid's
	
	public static final String CALENDAR_ADD_SCREEN_NAVUSID = "1401355313223274";
	public static final String CALENDAR_EDIT_SCREEN_NAVUSID = "1401355337653730";

	// Images for section header related to style guide application
	public static final String BUTTON_HEADER_IMG = "icon_styling_button";
	public static final String CHART_HEADER_IMG = "icon_styling_chart";
	public static final String COVERFLOW_HEADER_IMG = "icon_styling_coverflow";
	public static final String DETAILSCREEN_HEADER_IMG = "";
	public static final String FONT_HEADER_IMG = "icon_styling_font";
	public static final String GALLERY_HEADER_IMG = "icon_styling_gallery";
	public static final String GRID_HEADER_IMG = "icon_styling_grid";
	public static final String LIST_HEADER_IMG = "icon_styling_list";
	public static final String MULTIFORM_HEADER_IMG = "icon_styling_multiform";
	public static final String MULTITAB_HEADER_IMG = "icon_styling_multitab";
	public static final String SECTION_HEADER_IMG = "icon_styling_sectionlist";
	public static final String TABBAR_HEADER_IMG = "icon_styling_tabbar";
	public static final String TABLETCHART_HEADER_IMG = "icon_styling_tabletchart";
	public static final String TABLETCHARTWITHTABLE_HEADER_IMG = "icon_styling_tabletchartwithtable";
	public static final String TABLE_HEADER_IMG = "icon_styling_table";
	public static final String TITLE_HEADER_IMG = "icon_styling_titlebar";
	public static final String TOOLBAR_HEADER_IMG = "icon_styling_toolbar";

	public static final int DEFAULT_LEFT_MARGIN = 40;
	public static final String LOGIN_USER_NAME = "admin";
	public static final String FORM_FIELD_INPUT_TYPE_DECIMAL = "decimal";

	// Barcode Module Constants
	public static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";
	public static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";
	public static final String[] ZXING_URLS = {
		"http://zxing.appspot.com/scan", "zxing://scan/" };
	public static final String RETURN_URL_PARAM = "ret";
	public static final int HISTORY_REQUEST_CODE = 0x0000bacc;
	public static final boolean ENABLE_MULTI_FORM_LABEL_PREPOPULATE = true;
	public static final boolean BACK_STACK_BUG_FIX = true;

	// Reports
	public static final int PIE_CHART_FONT_SIZE = 11;

	public static final String CUSTOM_TAB_TEXTVIEW_TAG = "aimtabtext";
	public static final String CUSTOM_TAB_SMARTIMAGEVIEW_TAG = "aimtabicon";

	// Default Tab Indicator Color - Holo Light Theme
	public static final String SELECTED_TAB_COLOR = "#357EC7";

	// Default Stacked Actionbar Background Color - Holo Light Theme
	public static final String BACKGROUND_TAB_COLOR = "#ffffff";

	// Holo Blue Light
	public static final String PRESSED_TAB_COLOR = "#357EC7";


	public static final boolean USE_SERVER_MAPPER_GET_POST_URLS=true;
	public static final boolean IS_AUTO_DEBUG_ENABLED = false;

	//BL types
	public static final String BL_START = "start";
	public static final String BL_CONNECTOR = "connector";
	public static final String BL_EXPRESSION = "expression";
	public static final String BL_IF = "if";
	public static final String BL_FOR = "for";
	public static final String BL_WHILE = "while";
	public static final String BL_ONETOMANY = "onetomany";
	public static final String BL_INSERTUPDATE = "insertupdate";
	public static final String BL_DELETE = "delete";
	public static final String BL_CREATE = "create";
	
	//
	public static final boolean USE_FOOTER_LOAD_TEXT = false;
	
	public static final boolean USE_TABS_BELOW_ACTIONBAR=true;

	public static final int DEFAULT_LIST_SEPERATOR_WIDTH = 1;
	public static final String DRAWABLE = "drawable";
	public static final String IMAGE_PNG_EXTENSION = ".png";
	public static final String S_BACKSPACE = "\\s";
	
	public static final boolean IS_GRID_FORM_ENABLED=true;
	public static int GRID_FORM_ROW_COUNT=16;
	
	public static final String LOGIN_SCREEN_SUCCESS = "loginsuccess";
	public static final String LOGIN_SCREEN_FAILURE = "loginfailure";
	public static final String LOGIN_SCREEN_SKIP = "skiplogin";
	
	public static String BYPASS_APPID = "999999";
	public static final boolean USE_GENERIC_URL=true;
	public static final String GENERIC_GET = "/AiM/";
	public static final String URL_PUBLISH_ACTION_TABLE=SERVER_NAME+"/AiM/10/action";
	public static final String URL_PUBLISH_MINUS_HUNDRED_APPSLIST_TABLE=SERVER_NAME+"/AiM/oms/minushundredappslist";
	public static final String URL_PUBLISH_INDIVIDUAL_APP = SERVER_NAME+"/AiM/";
	public static final String LOGIN_USER_ID_DATABASE_COLUMN="userid";
	
	public static final String SECTIONLIST_SCREENDESIGN_TAB_USID = "1367499068043996";
	public static final String SECTIONLIST_STYLE_TAB_USID= "1372837882637268";
	

	
	public static final boolean USE_GENERIC_WEBSERVICE_ERROR_RESPONSE = false;
	
	
	public static final String SHARED_PREF_IS_LOGGED_IN= "OMS_IS_AUTHENTICATED";
	public static final String MF_BUTTON_LOG_OUT= "Logout";
	public static final Map<String,String> tableKeyMap= new HashMap<String,String>();
	static {
		//tabelKey.put("patientsList", "seqEdpId");
	}
	
	public static boolean IS_LDAP_AUTHENTICATION = false;
	
	public static boolean IS_STRICT_MODE_ENABLED = false;
	
	public static boolean IS_DEBUG_ENABLED = true;
	
	public static String TRACE_TYPE_BL = "BusinessLogic";
	public static String TRACETYPE_DB = "Local Database";
	public static String TRACE_TYPE_GET_LAUNCH = "LaunchGet";
	public static String TRACE_TYPE_SERVER_DATA_FETCH="serverGet";
	public static String TRACE_TYPE_SERVER_DATA_POST="serverPost";
	public static String TRACE_TYPE_SERVER = "Server";
	public static String TRACE_TYPE_SCREEN_LAUNCH = "ScreenLaunch";
			
	
	
	

}

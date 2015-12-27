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

/**
 * @author 245742
 * 
 */
public class OMSDatabaseConstants {

	public static final String DATABASE_PATH = "/data/data/com.cognizant.aim/databases/";
	public static final String CONFIG_DB_NAME = OMSConstants.CONFIG_DB_NAME;
	public static final String TRANS_DB_NAME = OMSConstants.APP_ID + ".db";
	public static final String KEY_ROWID = "_id";
	public static final String UNIQUE_ROW_ID = "usid";
	// Table Column Types
	public static final String INTEGER_TYPE = "INTEGER";
	public static final String STRING_TYPE = "TEXT";
	public static final String LONG_TYPE = "Long";
	public static final String DOUBLE_TYPE = "Double";
	public static final String REAL_TYPE = "REAL";
	public static final String BIGINT_TYPE = "BIGINT";
	
	public static final int IS_DELETE_CONSTANT = 0;

	// Server Constants
	public static final String SERVERURL = "serverurl";

	public static final String CONFIGDB_APPID = "appid";
	
	// common constants
	public static final String IS_DELETE = "isdelete";
	public static final String MODIFIED_DATE = "modifieddate";
	public static final String COMMON_COLUMN_NAME_STATUS = "status";
	public static final String COMMON_COLUMN_NAME_MODIFIED_DATE = "modifieddate";
	public static final String COMMON_COLUMN_NAME_IS_DELETE = "isdelete";
	//public static final String COMMON_TRANS_COLUMN_NAME_READABLE_DATE="datetimestamp";
	public static final String COMMON_TRANS_COLUMN_NAME_TRANSACTION_DATE="transactiondate";
	public static final String COMMON_TRANS_COLUMN_NAME_TRANSACTION_TIME="transactiontime";
	public static final String CURRENT_PAGE = "currentpage";

	
	// Login Screen related Constants
	public static final String LOGIN_SCREEN_TABLE_NAME = "Login";
	public static final String LOGIN_SCREEN_TITLE = "title";
	public static final String LOGIN_SCREEN_IMAGE = "loginimage";
	public static final String LOGIN_SCREEN_LABEL_NAME2 = "labelname2";
	public static final String LOGIN_SCREEN_LABEL_NAME1 = "labelname1";
	public static final String LOGIN_SCREEN_UNIQUE_ID = "usid";
	public static final String LOGIN_SCREEN_URL = "loginurl";
	public static final String LOGIN_NAV_USID = "navusid";
	// Navigation screen related constants
	public static final String NAVIGATION_SCREEN_TABLE_NAME = "Navigation";
	public static final String NAVIGATION_SCREEN_ORDER = "screenorder";
	public static final String NAVIGATION_SCREEN_TYPE = "screentype";
	public static final String NAVIGATION_PARENT_SCREEN_ORDER = "parentscreenorder";
	public static final String NAVIGATION_SCREEN_POSITION = "position";
	public static final String NAVIGATION_SCREEN_UNIQUE_ID = "usid";
	public static final String NAVIGATION_SCREEN_ISDELETE = "isdelete";
	public static final String NAVIGATION_SCREEN_APPID = "appid";
	public static final String NAVIGATION_SCREEN_NAME = "screenname";
	public static final String NAVIGATION_SCREEN_X_ORIGIN = "xorigin";
	public static final String NAVIGATION_SCREEN_Y_ORIGIN = "yorigin";
	public static final String NAVIGATION_SCREEN_MIN_POS = "1";
	public static final String NAVIGATION_SCREEN_MAX_POS = "30";

	// AddScreen related constants
	public static final String ADD_SCREEN_TABLE_NAME = "AddScreen";
	public static final String ADD_SCREEN_TITLE = "title";
	public static final String ADD_SCREEN_DATA_TABLE_NAME = "datatablename";
	public static final String ADD_SCREEN_LIST_SCREEN_UNIQUE_ID = "listusid";

	// AddScreenItems related constants
	public static final String ADD_SCREEN_ITEMS_TABLE_NAME = "AddScreenItems";
	public static final String ADD_SCREEN_ITEMS_LABEL_NAME = "labelname";
	public static final String ADD_SCREEN_ITEMS_LABEL_CONTENT = "labelcontent";
	public static final String ADD_SCREEN_ITEMS_LIST_SCREEN_UNIQUE_ID = "listusid";
	public static final String ADD_SCREEN_ITEMS_SHOW_CAMERA = "showcamera";

	// DetailScreen Table related constants
	public static final String DETAIL_SCREEN_TABLE = "DetailScreen";
	public static final String DETAIL_SCREEN_TITLE = "title";
	public static final String DETAIL_SCREEN_SHOW_EDIT = "showedit";
	public static final String DETAIL_SCREEN_DATA_TABLE_NAME = "datatablename";
	public static final String DETAIL_SCREEN_LIST_SCREEN_UNIQUE_ID = "listusid";

	// DetailScreenItems Table related constants
	public static final String DETAIL_SCREEN_ITEMS_TABLE = "DetailScreenItems";
	public static final String DETAIL_SCREEN_ITEMS_LABEL_NAME = "labelname";
	public static final String DETAIL_SCREEN_ITEMS_LABEL_CONTENT = "labelcontent";
	public static final String DETAIL_SCREEN_ITEMS_LIST_SCREEN_UNIQUE_ID = "listusid";

	// FormScreen Table related Constants
	public static final String FORM_SCREEN_TABLE = "FormScreen";
	public static final String FORM_SCREEN_TITLE = "title";
	public static final String FORM_SCREEN_SHOW_EDIT = "showedit";
	public static final String FORM_SCREEN_DATA_TABLE_NAME = "datatablename";
	public static final String FORM_SCREEN_SHOW_TOOL_BAR = "showtoolbar";
	public static final String FORM_SCREEN_DATA_TABLE_ROW_ID = "datatablenamerowid";
	public static final String FORM_SCREEN_NAVIGATION_SCREEN_UNIQUE_ID = "navusid";
	public static final String FORM_SCREEN_UNIQUE_ID = "usid";
	public static final String FORM_SCREEN_IS_PREPOPULATED = "isprepopulated";

	// FormScreenItems Table related Constants
	public static final String FORM_SCREEN_ITEMS_TABLE = "FormScreenItems";
	public static final String FORM_SCREEN_ITEMS_LABEL_NAME = "labelname";
	public static final String FORM_SCREEN_ITEMS_LABEL_CONTENT = "labelcontent";
	public static final String FORM_SCREEN_ITEMS_PICKER_TABLE_NAME = "pickerdatatablename";
	public static final String FORM_SCREEN_ITEMS_PICKER_CONTENT = "pickercontent";
	public static final String FORM_SCREEN_ITEMS_SHOW_CHOICE_PICKER = "showchoicepicker";
	public static final String FORM_SCREEN_ITEMS_IS_MANDATORY = "ismandatory";
	public static final String FORM_SCREEN_ITEMS_INPUT_TYPE = "inputtype";
	public static final String FORM_SCREEN_ITEMS_IS_MULTILINE = "ismultiline";
	public static final String FORM_SCREEN_ITEMS_FORM_USID = "formusid";
	public static final String FORM_SCREEN_ITEMS_SHOW_DATE_PICKER = "showdatepicker";
	public static final String FORM_SCREEN_ITEMS_SHOW_TIME_PICKER = "showtimepicker";
	public static final String FORM_SCREEN_ITEMS_READ_ONLY = "readonly";
	public static final String FORM_SCREEN_ITEMS_SHOW_RATING_BAR = "showratingbar";
	public static final String FORM_SCREEN_ITEMS_SPECIALFUNCTIONALITY = "specialfunctionality";
	public static final String FORM_SCREEN_ITEMS_UNIQUE_ID = "usid";

	// FormScreenSingleColumn Table related Constants
	public static final String FORM_SCREEN_SINGLE_COLUMN_TABLE = "FormScreenSingleColumn";
	public static final String FORM_SCREEN_SINGLE_COLUMN_TITLE = "title";
	public static final String FORM_SCREEN_SINGLE_COLUMN_SHOW_EDIT = "showedit";
	public static final String FORM_SCREEN_SINGLE_COLUMN_DATA_TABLE_NAME = "datatablename";
	public static final String FORM_SCREEN_SINGLE_COLUMN_SHOW_TOOL_BAR = "showtoolbar";
	public static final String FORM_SCREEN_SINGLE_COLUMN_DATA_TABLE_ROW_ID = "datatablenamerowid";
	public static final String FORM_SCREEN_SINGLE_COLUMN_NAVIGATION_SCREEN_UNIQUE_ID = "navusid";
	public static final String FORM_SCREEN_SINGLE_COLUMN_UNIQUE_ID = "usid";

	// FormScreenItemsSingleColumn Table related Constants
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_TABLE = "FormScreenItemsSingleColumn";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_LABEL_CONTENT = "labelcontent";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_PICKER_TABLE_NAME = "pickerdatatablename";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_PICKER_CONTENT = "pickercontent";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_SHOW_CHOICE_PICKER = "showchoicepicker";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_IS_MANDATORY = "ismandatory";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_INPUT_TYPE = "inputtype";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_IS_MULTILINE = "ismultiline";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_FORM_USID = "formusid";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_SHOW_DATE_PICKER = "showdatepicker";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_SHOW_TIME_PICKER = "showtimepicker";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_READ_ONLY = "readonly";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_SHOW_RATING_BAR = "showratingbar";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_SPECIALFUNCTIONALITY = "specialfunctionality";
	public static final String FORM_SCREEN_ITEMS_SINGLE_COLUMN_UNIQUE_ID = "usid";

	// ListScreen related constants
	public static final String LIST_SCREEN_TABLE_NAME = "watchlistscreen";
	
	public static final String GRID_VIEW_SCREEN_TABLE_NAME = "GridViewScreen";
	public static final String GRID_VIEW_ITEM_IMG_URL = "gridimageurl";
	public static final String GRID_VIEW_ITEM_TEXT = "gridtext";
	
	
	public static final String  LIST_SCREEN_ITEMS_TABLE = "watchlistscreenitems";
	public static final String  LIST_SCREEN_ITEM_IMAGE_VIEW = "Image";
	public static final String  LIST_SCREEN_ITEM_RATING_CONTROL = "Rating Control";
	public static final String  LIST_SCREEN_ITEM_DYNAMIC_LABEL = "Dynamic Text";
	public static final String  LIST_SCREEN_ITEM_LABEL = "Label";

	public static final String  LIST_SCREEN_ITEM_BUTTON = "Button";
	public static final String  LIST_SCREEN_ITEM_DETAIL = "Detail Button";
	public static final String  LIST_SCREEN_ITEM_CALL = "Call";
	public static final String  LIST_SCREEN_ITEM_SMS = "SMS";
	public static final String  LIST_SCREEN_ITEM_BADGE_COUNT_LABEL 	= "Badge Count";
	public static final String  LIST_SCREEN_ITEM_CHECKBOX_LABEL 	= "Check Box";
	
	public static final String  LIST_SCREEN_ITEM_SPACE = "Empty";
	
	public static final String  LIST_SCREEN_ITEM_BUTTON_TYPE_NORMAL = "Normal";
	public static final String  LIST_SCREEN_ITEM_BUTTON_TYPE_DETAIL = "Detail";
	public static final String  LIST_SCREEN_ITEM_BUTTON_TYPE_CALL = "Call";
	public static final String  LIST_SCREEN_ITEM_BUTTON_TYPE_SMS = "SMS";




	
	
	public static final String LIST_SCREEN_TITLE = "title";
	public static final String LIST_SCREEN_SHOW_SEARCH = "showsearch";
	public static final String LIST_SCREEN_SHOW_HEADER = "useheader";
	
	public static final String LIST_SCREEN_USE_PAGINATION = "ispagination";
	public static final String LIST_SCREEN_USE_SERVER_SEARCH = "isserversearch";


	
	public static final String LIST_SCREEN_SHOW_SORTING= "showsorting";
	public static final String LIST_SCREEN_SORTING_DEFAULT_LABEL= "defaultsortinglabel";
	public static final String LIST_SCREEN_REFRESH_BL= "refreshblname";
	public static final String LIST_SCREEN_PAGINATION_BL= "paginationbl";
	public static final String LIST_SCREEN_SERVER_SEARCH_BL= "serversearchbl";
	public static final String LIST_SCREEN_SHAKE_TO_REFRESH_BABEL= "refreshlabel";
	public static final String LIST_SCREEN_SHAKE_TO_REFRESH_STYLE_NAME= "refreshstylename";
	
	// Navigation ListScreen related constants
	public static final String NAVIGATION_LIST_SCREEN_TABLE_NAME = "listnavigationscreen";
	public static final String NAVIGATION_LIST_SCREEN_ITEMS = "listnavigationscreenitems";
	public static final String NAVIGATION_LIST_SCREEN_STYLE_NAME = "styleguidename";
	public static final String NAVIGATION_LIST_SCREEN_STYLE_TABLE = "listnavigationstyle";
	public static final String NAVIGATION_LIST_USID = "listusid";
	public static final String NAVIGATION_LIST_CHILD_NAVUSID = "childnavusid";
	public static final String NAVIGATION_LIST_SCREEN_ITEMS_SECONDARY_TEXT = "secondarytext";
	public static final String NAVIGATION_LIST_SCREEN_ITEMS_POSITION = "position";
	public static final String NAVIGATION_LIST_SCREEN_ITEMS_PRIMARY_TEXT = "primarytext";
	public static final String NAVIGATION_LIST_SCREEN_ITEMS_THUMBMAIL_IMAGE = "thumbnailimage";
	public static final String NAVIGATION_LIST_ROLE_NAME = "roleid";
	public static final String NAVIGATION_LIST_SHOW_BADGE = "showbadge";
	public static final String NAVIGATION_LIST_BADGE_DATA_TABLE = "badgedatatable";
	public static final String NAVIGATION_LIST_BADGE_USE_WHERE = "badgeusewhere";
	public static final String NAVIGATION_LIST_BADGE_WHERE_COL = "badgewherecolumn";
	public static final String NAVIGATION_LIST_BADGE_WHERE_CONST = "badgewhereconstant";


	
	public static final String LIST_SCREEN_LIST_ITEM1 = "listitem1";
	public static final String LIST_SCREEN_LIST_ITEM2 = "listitem2";
	public static final String LIST_SCREEN_LIST_ITEM3 = "listitem3";
	public static final String LIST_SCREEN_DATA_TABLE_NAME = "datatablename";
	public static final String LIST_SCREEN_SHOW_TOOL_BAR = "showtoolbar";
	public static final String LIST_SCREEN_SHOW_ADD = "showadd";
	public static final String LIST_SCREEN_SHOW_DETAIL = "showdetail";
	public static final String LIST_SCREEN_SHOW_BADGE = "showbadge";
	public static final String LIST_SCREEN_BADGE_COUNT_COLUMN = "badgecountcolumn";
	public static final String LIST_SCREEN_SHOW_ACTION_SHEET = "showactionsheet";
	public static final String LIST_SCREEN_SHOW_ADV_SEARCH = "advancedsearch";
	public static final String LIST_SCREEN_ADV_SEARCH_GRUOP_BY = "groupcondition";
	public static final String LIST_TYPE = "listtype";


	
	public static final String LIST_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String LIST_SCREEN_UNIQUE_ID = "usid";
	public static final String LIST_SCREEN_FILTER_COLUMN_NAME = "filtercolumn";
	public static final String LIST_SCREEN_SEARCH_COLUMN_NAME = "searchcolumnname";
	public static final String LIST_SCREEN_SORT_COLUMN_NAME1 = "sortcolumnname1";
	public static final String LIST_SCREEN_SORT_COLUMN_NAME2 = "sortcolumnname2";
	public static final String LIST_SCREEN_SORT_COLUMN_NAME3 = "sortcolumnname3";
//	public static final String LIST_SCREEN_LIST_ITEM_TYPE = "listitemtype";
	public static final String LIST_SCREEN_COLUMNNAME_FOR_TITLE = "transcolumnnamefortitle";
	public static final String LIST_SCREEN_IS_HOMOGENEOUS = "ishomogeneouslist";
	public static final String LIST_SCREEN_STYLEGUIDE_NAME = "styleguidename";
	public static final String LIST_SCREEN_LIST_POSITION = "listposition";
	public static final String LIST_SCREEN_IS_UNIVERSAL_MOBILE_STUDIO = "isuniversalmobilestudio";
	public static final String LIST_SCREEN_SHOW_DELETE = "showdelete";
	public static final String LIST_SCREEN_IS_HEADER_FLAG = "isheaderflag";
	public static final String LIST_SCREEN_SHOW_SPLIT_VIEW = "showsplitview";
	public static final String LIST_SCREEN_USE_HEADER = "useheader";
	
	
	public static final String LIST_SCREEN_SHOW_LOCATION_SEARCH = "locationsearchenabled";
	public static final String LIST_SCREEN_LOCATION_SEARCH_TABLE_NAME = "locationinfotablename";
	public static final String LIST_SCREEN_LOCATION_SEARCH_COLUMN_NAME = "locationcolumnname";
	public static final String LIST_SCREEN_LOCATION_SEARCH_LATITUDE = "latitudecolumnname";
	public static final String LIST_SCREEN_LOCATION_SEARCH_LONGITUDE = "longitudecolumnname";
	public static final String LIST_SCREEN_LOCATION_SEARCH_RADIUS = "locationsearchradius";
	public static final String LIST_SCREEN_LOCATION_SEARCH_BL_NAME = "locationsearchblname";
	
	public static final String LIST_SCREEN_IS_READ_ITEM = "markasprocessed";
	public static final String LIST_SCREEN_IS_READ_ITEM_COLUMN_NAME = "processedvaluecolumn";


	//MEDIA
	public static final String MEDIA_TABLE= "mediacontroller";
	public static final String MEDIA_DATA_TABLE_NAME = "datatablename";
	public static final String MEDIA_COLUMN_CONTENT_NAME = "columncontent";
	public static final String MEDIA_TYPE = "mediatype";
	public static final String MEDIA_URL = "url";
	public static final String MEDIA_THUMBNAIL_IMG = "thumbnailimg";
	public static final String MEDIA_USE_WHERE = "usewhere";
	public static final String MEDIA_WHERE_CNST = "whereconstant";
	public static final String MEDIA_WHERE_COLUMN_NAME = "wherecolumn";
	public static final String MEDIA_FILTER_COLUMN_NAME = "filtercolumn";
	public static final String MEDIA_RETAIN_WHERE = "retainwhereclause";
	public static final String MEDIA_FILE_UPLOAD_BL = "fileuploadbl";
	public static final String MEDIA_FILE_DOWNLOAD_BL = "filedownloadbl";
	public static final String MEDIA_FILE_DELETE_BL = "filedeletebl";	
	public static final String MEDIA_IS_VIDEO_STREAM = "isvideostreamingneeded";
	public static final String MEDIA_VIDEO_STREAM_URL = "videostreamingurl";
	public static final String MEDIA_IS_SAVE_IN_GALLERY = "issavetophotosneeded";
	
	//PAGE SCROLLER
		public static final String PAGE_SCROLLER_TABLE= "scroller";
		
	// Sorting List Screen related constants
	//public static final String SORTING_DATA_TABLE_NAME 		= "Sorting";
	public static final String SORTING_CONFIG_TABLE_NAME 		= "sortingconfig";
	
	public static final String LIST_ADV_SEARCH_OPTIONS_CONFIG_TABLE_NAME 		= "listadvancedsearchoptions";
	public static final String LIST_ADV_SEARCH_VALUES_CONFIG_TABLE_NAME 		= "listadvancedsearchoptionvalues";
	
	public static final String SORTING_DATA_LIST_UNIQUE_ID 	= "listusid";
	public static final String SORTING_DATA_TABLE_UNIQUE_ID = "usid";
	
	// Calendar related constants
	public static final String CALENDAR_SCREEN_TABLE_NAME = "CustomCalendar";
	public static final String CALENDAR_SCREEN_DATA_TABLE_NAME = "datatablename";
	public static final String CALENDAR_SCREEN_UNIQUE_ID = "usid";
	public static final String CALENDAR_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String CALENDAR_SCREEN_SHOW_TOOL_BAR = "showtoolbar";
	public static final String CALENDAR_SCREEN_EVENT_NAME = "eventname";
	public static final String CALENDAR_SCREEN_EVENT_DESC = "eventdesc";
	public static final String CALENDAR_SCREEN_START_DATE = "startdate";
	public static final String CALENDAR_SCREEN_END_DATE = "enddate";
	public static final String CALENDAR_SCREEN_START_TIME = "starttime";
	public static final String CALENDAR_SCREEN_END_TIME = "endtime";
	public static final String CALENDAR_SCREEN_CALENDAR_NAME = "calendarname";
	public static final String CALENDAR_SCREEN_EVENT_LOCATION = "eventlocation";
	//public static final String CALENDAR_EVENTS_TABLE_NAME = "calendarevents";
	// GalleryScreen related constants

	public static final String GALLERY_SCREEN_TABLE_NAME = "GalleryScreen";
	public static final String GALLERY_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String GALLERY_SCREEN_UNIQUE_ID = "usid";
	public static final String GALLERY_SCREEN_FILTER_COLUMN_NAME = "filtercolumn";
	public static final String GALLERY_SCREEN_TITLE = "title";
	public static final String GALLERY_SCREEN_TRANSCOLUMNNAME = "transcolumnnamefortitle";

	public static final String GALLERY_SCREEN_IS_HOMOGENEOUS = "ishomogeneousgallery";
	public static final String GALLERY_SCREEN_POSITION = "galleryposition";

	public static final String PRIMARY_GALLERY_SCREEN_ITEM = "primarygalleryitem";
	public static final String PRIMARY_GALLERY_SCREEN_ITEM_IMAGEURL = "primarygalleryitemimageurl";

	public static final String SECONDARY_GALLERY_SCREEN_ITEM1 = "secondarygalleryitem1";
	public static final String SECONDARY_GALLERY_SCREEN_ITEM2 = "secondarygalleryitem2";
	public static final String GALLERY_SCREEN_PRIMARY_DATA_TABLE_NAME = "primarydatatablename";
	public static final String GALLERY_SCREEN_SECONDARY_DATA_TABLE_NAME = "secondarydatatablename";
	public static final String GALLERY_SCREEN_STYLE_GUIDE_NAME = "styleguidename";
	public static final String SECONDARY_GALLERY_SCREEN_ITEM3 = "secondarygalleryitem3";
	public static final String SECONDARY_GALLERY_SHOW_DETAIL = "showdetail";

	// Map Screen Related Constants
	public static final String MAP_SCREEN_TABLE_NAME = "Map";
	public static final String MAP_SCREEN_TITLE = "title";
	public static final String MAP_SCREEN_SHOW_TOOLBAR = "showtoolbar";
	public static final String MAP_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String MAP_SCREEN_DATA_TABLE_NAME = "datatablename";

	// TabScreen Related Constants
	public static final String TAB_SCREEN_TABLE_NAME = "Tab";
	public static final String TAB_SCREEN_TABTITLE = "tabtitle";
	public static final String TAB_SCREEN_TAB_ICON = "tabicon";
	public static final String TAB_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String TAB_SCREEN_POSITION = "position";
	public static final String TAB_SCREEN_STYLEGUIDE_NAME = "styleguidename";

	// ToolBar Related Constants
	public static final String TOOLBAR_TABLE_NAME = "ToolBar";
	public static final String TOOLBAR_ITEM_TITLE = "toolbaritemtitle";
	public static final String TOOLBAR_ITEM_ICON = "toolbaritemicon";
	public static final String TOOLBAR_LAUNCHER_NAVIGATION_UNIQUE_ID = "launchernavusid";
	public static final String TOOLBAR_ITEM_POSITION = "position";
	public static final String TOOLBAR_PARENT_USID = "parentusid";
	public static final String TOOLBAR_STYLEGUIDE_NAME = "styleguidename";
	public static final String TOOLBAR_SHOW_BADGE = "showbadge";
	public static final String TOOLBAR_BADGE_DATA_TABLE = "badgedatatable";
	public static final String TOOLBAR_BADGE_DATA_COLUMN = "badgedatacolumn";
	public static final String TOOLBAR_BADGE_USE_WHERE = "badgeusewhere";
	public static final String TOOLBAR_BADGE_WHERE_COLUMN = "badgewherecolumn";
	public static final String TOOLBAR_BADGE_WHERE_CONSTANT = "badgewhereconstant";
	
	// Action Sheet Related Constants
	public static final String ACTION_SHEET_TABLE_NAME = "actionsheetitems";
	public static final String ACTION_SHEET_TITLE = "actionbuttontext";
	public static final String ACTION_SHEET_ICON = "actionbuttonicon";
	public static final String ACTION_SHEET_ITEM_TITLE = "menuitemtitle";
	public static final String ACTION_SHEET_ITEM_ICON = "menuitemicon";
	public static final String ACTION_SHEET_ITEM_LAUNCHER_NAVIGATION_UNIQUE_ID = "menuitemlaunchnavusid";
	public static final String ACTION_SHEET_ITEM_POSITION = "position";
	public static final String ACTION_SHEET_PARENT_USID = "parentusid";
	public static final String ACTION_SHEET_STYLEGUIDE_NAME = "menuitemstyle";
	public static final String ACTION_SHEET_ITEM_BL_NAME = "blname";
	public static final String ACTION_SHEET_ID = "actionsheetid";



	// Action Center related constants
	public static final String ACTION_TABLE_NAME = "Action";
	public static final String ACTION_TABLE_BUTTON_ID = "buttonid";
	public static final String ACTION_TABLE_UNIQUE_ID = "usid";
	public static final String ACTION_TYPE = "actiontype";
	public static final String ACTION_TABLE_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String ACTION_TABLE_ACTION_ORDER = "actionorder";
	public static final String ACTION_TABLE_CONDITIONAL_VALUE = "conditionalvalue";
	public static final String ACTION_TABLE_IS_DELETE = "isdelete";
	public static final String ACTION_TABLE_MODIFIED_DATE = "modifieddate";
	public static final String ACTION_TABLE_ACTION_ID = "actionid";

	// Action queue table related constants
	public static final String ACTION_QUEUE_TABLE_NAME = "ActionQueue";
	public static final String ACTION_QUEUE_TYPE = "actiontype";
	public static final String ACTION_UNIQUE_ID = "actionusid";
	public static final String ACTION_QUEUE_STATUS = "status";
	public static final String ACTION_QUEUE_PRIORITY = "priority";
	public static final String ACTION_QUEUE_IS_DELETE = "isdelete";
	public static final String ACTION_QUEUE_MODIFIED_DATE = "modifieddate";

	public static final String ACTION_STATUS_NEW = "fresh";
	public static final String ACTION_STATUS_FINISHED = "done";
	public static final String ACTION_STATUS_TRIED = "tried";

	// Mail Table related constants
	public static final String MAIL_TABLE_NAME = "Mail";
	public static final String MAIL_PARENT_USID = "usid";
	public static final String MAIL_TO_ADDRESS = "toaddress";
	public static final String MAIL_SUBJECT = "subject";
	public static final String MAIL_MESSAGE_BODY = "messagebody";

	// Contacts Table related constants
	public static final String CONTACTS_TABLE_NAME = "Contacts";
	public static final String CONTACTS_PARENT_USID = "usid";
	public static final String CONTACTS_TEL_NUMBER = "contactno";

	// IU Table related Constants
	public static final String IU_TABLE_NAME = "IU";
	public static final String SOURCE_TABLE_NAME = "sourcetable";
	public static final String DESTINATION_TABLE_NAME = "destinationtable";
	public static final String IU_TABLE_PRIMARY_COLUMN_NAME = "pkcolumn";

	// Delete Table
	public static final String DELETE_TABLE_NAME = "DeleteActionTable";
	public static final String DELETE_TARGET_TABLE_NAME = "tablename";
	// one2Many Table related Constants
	public static final String ONETOMANY_TABLE_NAME = "OneToManyTable";
	public static final String ONETOMANY_PARENT_TABLE = "parenttable";
	public static final String ONETOMANY_CHILD_TABLE = "childtable";
	public static final String ONETOMANY_PRIMARY_FOREIGN_KEY_COLUMN_NAME = "pkeyfkey";
	public static final String ONETOMANY_COUNT_TABLE = "counttable";

	// Post Table related constants
	public static final String GET_TABLE_NAME = "Get";
	public static final String POST_TABLE_NAME = "Post";
	public static final String MOBILESTUDIOUNIVERSEGET_TABLE_NAME = "MobileStudioUniverseGet";
	public static final String POST_PARENT_USID = "usid";
	public static final String POST_TO_TABLE_NAME = "datatablename";
	public static final String POST_DB_NAME = "dbname";
	public static final String POST_URL = "url";

	// Pragma Table_Info Constants
	public static final String PRAGMA_COLUMN_NAME1 = "name";
	public static final String PRAGMA_COLUMN_NAME2 = "type";

	public static final String TRANS_TABLE_STATUS_COLUMN = "status";
	public static final String STATUS_TYPE_TRIED = "tried";
	public static final String STATUS_TYPE_NEW = "fresh";
	public static final String STATUS_TYPE_FINISHED = "done";

	// Trans Modified Date Constants
	public static final String TRANS_MODIFIED_DATE = "modifieddate";

	// Server Mapper Related Constants.
	public static final String SERVER_MAPPER_TABLE_NAME = "ServerMapper";
	public static final String SERVER_MAPPER_CLIENTTABLE_NAME = "clienttablename";
	public static final String SERVER_MAPPER_SERVERTABLENAME = "servertablename";
	public static final String SERVER_MAPPER_URL = "url";
	public static final String SERVER_MAPPER_SCHEMA = "appschema";
	public static final String VISITED_DATE_MAPPER_TABLE_NAME = "visiteddatemapper";
    public static final String SERVER_MAPPER_GET_URL="geturl";
    public static final String SERVER_MAPPER_POST_URL="posturl";
    public static final String SERVER_MAPPER_ERROR_OBJECT = "errorobject";
	public static final String SERVER_MAPPER_CODE_KEY = "codekey";
    public static final String SERVER_MAPPER_MSG_KEY="messagekey";
    public static final String SERVER_MAPPER_SUCCESS_CODE="successcode";

	// Contacts Table related to BL
	public static final String BL_TABLE_NAME = "BL";
	//public static final String BL_PARENT_USID = "usid";
	public static final String BL_PARENT_USID = "parentusid";
	public static final String BL_SCRIPT_CONTENT = "jsfile";
	public static final String BL_SOURCE_TABLE_NAME = "sourcedatatablename";
	public static final String BL_DATA_TABLE_ROW_ID = "datatablerowid";
	public static final String BL_DESTINATION_TABLE_NAME = "destinationdatatablename";
	public static final String BL_DESTINATION_ROW_ID = "destinationrowid";
	public static final String BL_ACTION_ORDER = "blorder";
	public static final String BL_ACTION_UNIQUE_NAME = "unifiedkey";
    public static final String BL_COMPLETE_TABLE_NAME="BlComplete";
    public static final String BL_COMPLETE_USID = "usid";
	// PickerConfig Table related constants
	public static final String PICKER_CONFIG_TABLE_NAME = "PickerConfig";
	public static final String PICKER_CONFIG_PARENT_USID = "parentusid";
	public static final String PICKER_CONFIG_PICKER_CONTENT = "pickercontent";
	public static final String PICKER_CONFIG_PICKER_DATA_TABLE_NAME = "pickerdatatablename";
	public static final String PICKER_CONFIG_PICKER_TYPE = "pickertype";
	public static final String PICKER_CONFIG_FORM_USID = "formusid";
	public static final String PICKER_CONFIG_PICKER_MAPPING = "pickermapping";
	public static final String PICKER_CONFIG_PICKER_CONTENT_KEY = "pickerkey";
	
	public static final String PICKER_CONFIG_HAS_DEPENDENT = "hasdependent";
	public static final String PICKER_CONFIG_IS_DEPENDENT = "isdependent";
	public static final String PICKER_CONFIG_DEPENDENT_PICKER_USID = "dependentpickerusid";
	public static final String PICKER_CONFIG_IS_MANDATORY = "ismandatory";
	

	// Multi TabScreen Related Constants
	public static final String MULTI_TAB_SCREEN_TABLE_NAME = "MultiTab";
	public static final String MULTI_TAB_SCREEN_UNIQUE_ID = "usid";
	public static final String MULTI_TAB_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String MULTI_TAB_SCREEN_POSITION = "position";
	public static final String MULTI_TAB_SCREEN_TABTITLE = "tabtitle";
	public static final String MULTI_TAB_SCREEN_TAB_ICON = "tabicon";
	public static final String MULTI_TAB_SCREEN_STYLEGUIDE_NAME = "styleguidename";

	// ListStyle related constants
	public static final String LIST_SCREEN_STYLE_TABLE_NAME = "ListStyle";

	public static final String LIST_SCREEN_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	public static final String LIST_SCREEN_STYLE_BACKGROUND_IMAGE = "backgroundimage";
	public static final String LIST_SCREEN_STYLE_ALTERNATIVE_COLOR1 = "alternatecolor1";
	public static final String LIST_SCREEN_STYLE_ALTERNATIVE_COLOR2 = "alternatecolor2";
	public static final String LIST_SCREEN_STYLE_CELL_SEPARATOR_COLOR = "cellseparatorcolor";
	public static final String LIST_SCREEN_STYLE_ITEM1_FONTSTYLE_USID = "item1fontstylename";
	public static final String LIST_SCREEN_STYLE_ITEM2_FONTSTYLE_USID = "item2fontstylename";
	public static final String LIST_SCREEN_STYLE_DETAILED_BUTTON_STYLE_NAME = "detailedbuttonstylename";
	public static final String LIST_SCREEN_STYLE_SELECTION_COLOR = "selectioncolor";
	public static final String LIST_SCREEN_STYLE_STYLE_NAME = "stylename";
	public static final String LIST_SCREEN_STYLE_HEADER_COLOR = "listheadercolor";


	// SpringboardStyle related constants
	public static final String SPRINGBOARD_SCREEN_STYLE_TABLE_NAME = "GridStyle";
	public static final String SPRINGBOARD_SCREEN_STYLE_HEADER_BACKGROUND_COLOR = "titleheaderbackgroundcolor";
	public static final String SPRINGBOARD_SCREEN_STYLE_HEADER_BACKGROUND_IMAGE = "titleheaderbackgroundimage";
	public static final String SPRINGBOARD_SCREEN_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	public static final String SPRINGBOARD_SCREEN_STYLE_BACKGROUND_IMAGE = "backgroundimage";
	public static final String SPRINGBOARD_SCREEN_STYLE_HEADER_SEPARATOR_COLOR = "headerseparatorcolor";
	public static final String SPRINGBOARD_SCREEN_STYLE_HEADER_SEPARATOR_IMAGE = "headerseparatorimage";
	public static final String SPRINGBOARD_SCREEN_STYLE_LABEL_FONT_STYLE = "labelfontstyle";
	public static final String SPRINGBOARD_SCREEN_STYLE_HEADER_LABEL_FONT_STYLE = "headerlabelfontstyle";
	public static final String SPRINGBOARD_SCREEN_STYLE_UNIQUE_ID = "usid";
	public static final String SPRINGBOARD_SCREEN_STYLE_SPRINGBOARD_UNIQUE_ID = "gridusid";
	public static final String SPRINGBOARD_SCREEN_STYLE_HEADER_FONT_STYLE = "headerfontstyle";
	public static final String SPRINGBOARD_SCREEN_STYLE_FONT_STYLE = "fontstyle";
	public static final String SPRINGBOARD_SCREEN_STYLE_NAME = "stylename";
	public static final String SPRINGBOARD_SCREEN_STYLE_TOOLBAR_STYLE = "toolbarstyle";

	// FontStyle related constants
	public static final String FONT_STYLE_TABLE_NAME = "FontStyle";
	public static final String FONT_STYLE_FONT_NAME = "fontname";
	public static final String FONT_STYLE_FONT_SIZE = "fontsize";
	public static final String FONT_STYLE_FONT_TYPE_FACE = "fonttypeface";
	public static final String FONT_STYLE_FONT_COLOR = "fontcolor";
	public static final String FONT_STYLE_UNIQUE_ID = "usid";
	public static final String FONT_STYLE_STYLE_NAME = "stylename";

	// ButtonStyle related constants
	public static final String BUTTON_STYLE_TABLE_NAME = "ButtonStyle";
	public static final String BUTTON_STYLE_BG_COLOR_NORMAL_STATE = "backgroundcolornormalstate";
	public static final String BUTTON_STYLE_BG_COLOR_SELECTED_STATE = "backgroundcolorselectedstate";
	public static final String BUTTON_STYLE_BG_IMAGE_NORMAL_STATE = "backgroundimagenormalstate";
	public static final String BUTTON_STYLE_BG_IMAGE_SELECTED_STATE = "backgroundimageselectedstate";
	public static final String BUTTON_STYLE_UNIQUE_ID = "usid";
	public static final String BUTTON_STYLE_FONTSTYLE_NAME = "fontstylename";
	public static final String BUTTON_STYLE_STYLE_NAME = "stylename";

	// GalleryStyle related constants
	public static final String GALLERY_STYLE_TABLE_NAME = "gallerystyle";
	public static final String GALLERY_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	public static final String GALLERY_STYLE_BACKGROUND_IMAGE = "backgroundimage";
	public static final String GALLERY_STYLE_PRIMARY_GALLERY_FONT_STYLE = "primarygalleryfontstyle";
	public static final String GALLERY_STYLE_PRIMARY_GALLERY_BACKGROUND_COLOR = "primarygallerybackgroundcolor";
	public static final String GALLERY_STYLE_PRIMARY_GALLERY_BACKGROUND_IMAGE = "primarygallerybackgroundimage";
	public static final String GALLERY_STYLE_SECONDARY_GALLERY_FONT_STYLE = "secondarygalleryfontstyle";
	public static final String GALLERY_STYLE_UNIQUE_ID = "usid";
	public static final String GALLERY_STYLE_STYLE_NAME = "stylename";

	// MultiFormStyle related constants
	public static final String MULTIFORM_STYLE_TABLE_NAME = "MultiFormStyle";
	public static final String MULTIFORM_STYLE_STYLE_NAME = "stylename";
	public static final String MULTIFORM_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	public static final String MULTIFORM_STYLE_BACKGROUND_IMAGE = "backgroundimage";
	public static final String MULTIFORM_STYLE_LABEL_FONT_STYLE = "labelfontstyle";
	public static final String MULTIFORM_STYLE_BUTTON_STYLE_NAME = "buttonstylename";
	public static final String MULTIFORM_STYLE_UNIQUE_ID = "usid";

	// MultiFormTextConfig Table related constants
	public static final String MULTIFORM_EDIT_TEXT_CONFIG_TABLE_NAME = "MultiFormTextConfig";
	public static final String MULTIFORM_EDIT_TEXT_CONFIG_PARENT_USID = "parentusid";
	public static final String MULTIFORM_EDIT_TEXT_CONFIG_FORM_USID = "formusid";
	public static final String MULTIFORM_EDIT_TEXT_CONFIG_TEXTFIELD_POSITION = "textfieldposition";
	public static final String MULTIFORM_EDIT_TEXT_IS_MANDATORY = "ismandatory";
	public static final String MULTIFORM_EDIT_TEXT_IS_READONLY = "isreadonly";
	public static final String MULTIFORM_EDIT_TEXT_INPUTTYPE = "inputtype";
	public static final String MULTIFORM_EDIT_TEXT_HIDDENTYPE = "ishidden";

	public static final String MULTIFORM_EDIT_TEXT_IS_NUMBER_VALID = "isNumberValid";
	public static final String MULTIFORM_EDIT_TEXT_NUMBER_ALLOWED = "allowedNumber";
	public static final String MULTIFORM_EDIT_TEXT_EMAIL_VALIDATE = "validateEmail";
	public static final String MULTIFORM_EDIT_TEXT_IS_CARD_VALID = "isCardNumberValid";
	public static final String MULTIFORM_EDIT_TEXT_IS_RANGE_CHECK = "shouldCheckRange";
	public static final String MULTIFORM_EDIT_TEXT_RANGE_START = "startRange";
	public static final String MULTIFORM_EDIT_TEXT_RANGE_END = "endRange";
	public static final String MULTIFORM_EDIT_TEXT_PASSWORD = "ispassword";
	public static final String MULTIFORM_EDIT_TEXT_GLOBAL="isglobal";

	// MultiFormButtonConfig Table related constants
	public static final String MULTIFORM_BUTTON_CONFIG_TABLE_NAME 		= "ButtonConfig";
	public static final String MULTIFORM_BUTTON_CONFIG_PARENT_USID 		= "parentusid";
	public static final String MULTIFORM_BUTTON_CONFIG_FORM_USID 		= "formusid";
	public static final String MULTIFORM_BUTTON_CONFIG_POSITION 		= "position";
	public static final String MULTIFORM_BUTTON_CONFIG_TRANS_COL_NAME	= "transcolumn";
	public static final String MULTIFORM_BUTTON_CONFIG_TRANS_VALUE 		= "transdata";
	
	// MultiFormScreen widget type constants
	public static final String MULTI_FORM_SCREEN_LABEL_TYPE = "Label";
	public static final String MULTI_FORM_SCREEN_BUTTON_TYPE = "Button";
	public static final String MULTI_FORM_SCREEN_EDIT_TEXT_TYPE = "TextField";
	public static final String MULTI_FORM_SCREEN_PICKER_TYPE = "Picker";
	public static final String MULTI_FORM_SCREEN_SIGNATURE_CAPTURE_TYPE = "Signature Capture";
	public static final String MULTI_FORM_SCREEN_CAMERA = "Camera";
	public static final String MULTI_FORM_SCREEN_RATING_CONTROL = "Rating Control";
	public static final String MULTI_FORM_SCREEN_BAR_CODE = "Bar Code";
	public static final String MULTI_FORM_SCREEN_VIDEO_PLAY = "Video Play";
	public static final String MULTI_FORM_SCREEN_IMAGE_VIEW = "Image";
	public static final String MULTI_FORM_SCREEN_DOWNLOAD = "File Download";
	public static final String MULTI_FORM_SCREEN_COLORPICKER_TYPE = "Color Picker";
	public static final String MULTI_FORM_SCREEN_TOGGLE_BUTTON = "Switch";
	public static final String MULTI_FORM_SCREEN_DISPLAY_TEXT = "Display Text";
	public static final String MULTI_FORM_SCREEN_UPLOAD = "File Upload";
	public static final String MULTI_FORM_SCREEN_QR_CODE = "QR Code";
	public static final String MULTI_FORM_SCREEN_AR_CODE = "AR";
	public static final String MULTI_FORM_CHECK_BUTTON = "Check box";
	public static final String MULTI_FORM_RADIO_BUTTON = "Radio button";
	public static final String MULTI_FORM_SCREEN_DATE_PICKER_TYPE = "Date Picker";
	public static final String MULTI_FORM_SCREEN_TIME_PICKER_TYPE = "Time Picker";
	// MultiFormScreen Table related Constants
	public static final String MULTI_FORM_SCREEN_TABLE = "MultiFormScreen";
	public static final String MULTI_FORM_SCREEN_TITLE = "title";
	public static final String MULTI_FORM_SCREEN_SHOW_EDIT = "showedit";
	public static final String MULTI_FORM_SCREEN_DATA_TABLE_NAME = "datatablename";
	public static final String MULTI_FORM_SCREEN_SHOW_TOOL_BAR = "showtoolbar";
	public static final String MULTI_FORM_SCREEN_DATA_TABLE_ROW_ID = "datatablenamerowid";
	public static final String MULTI_FORM_SCREEN_NAVIGATION_SCREEN_UNIQUE_ID = "navusid";
	public static final String MULTI_FORM_SCREEN_UNIQUE_ID = "usid";
	public static final String MULTI_FORM_SCREEN_STYLE_GUIDE_NAME = "styleguidename";
	public static final String MULTI_FORM_SAVE_EDIT_BL_NAME="blname";
	public static final String MULTI_FORM_SCREEN_REFRESH_BL= "refreshblname";
	public static final String MULTI_FORM_SCREEN_SHAKE_TO_REFRESH_LABEL_NAME= "refreshlabelname";
	public static final String MULTI_FORM_SCREEN_SHAKE_TO_REFRESH_STYLE_NAME= "refreshstylename";

	// MultiFormScreenItems Table related Constants
	public static final String MULTI_FORM_SCREEN_ITEMS_TABLE = "MultiFormScreenItems";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN1_TYPE = "column1type";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN1_CONTENT = "column1content";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN1_NAME = "column1name";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN1_ID = "column1id";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN2_TYPE = "column2type";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN2_CONTENT = "column2content";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN2_NAME = "column2name";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN2_ID = "column2id";

	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN3_TYPE = "column3type";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN3_CONTENT = "column3content";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN3_NAME = "column3name";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN3_ID = "column3id";
	public static final String MULTI_FORM_SCREEN_ITEMS_IS_DELETE = "isdelete";
	public static final String MULTI_FORM_SCREEN_ITEMS_MODIFIED_DATE = "modifieddate";

	public static final String MULTI_FORM_SCREEN_COLUMN4_TYPE = "column4type";
	public static final String MULTI_FORM_SCREEN_COLUMN4_CONTENT = "column4content";
	public static final String MULTI_FORM_SCREEN_COLUMN4_NAME = "column4name";
	public static final String MULTI_FORM_SCREEN_COLUMN4_ID = "column4id";

	public static final String MULTI_FORM_SCREEN_COLUMN_COUNT = "columncount";
	public static final String MULTI_FORM_SCREEN_ITEMS_UNIQUE_ID = "usid";
	public static final String MULTI_FORM_SCREEN_ITEMS_FORM_USID = "formusid";

	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN1_IS_DELETE = "column1isdelete";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN2_IS_DELETE = "column2isdelete";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN3_IS_DELETE = "column3isdelete";
	public static final String MULTI_FORM_SCREEN_ITEMS_COLUMN4_IS_DELETE = "column4isdelete";
	
	public static final String MULTI_FORM_SCREEN_ITEMS_ROW_INDEX = "rowindex";
	public static final String MULTI_FORM_SCREEN_GRID_ITEMS_TABLE = "multiformgridscreenitems";
	public static final String MULTI_FORM_SCREEN_GRID_TABLE ="multiformgridscreen";
	// TableScreen related constants
	public static final String TABLE_SCREEN_TABLE_NAME = "TableScreen";
	public static final String TABLE_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String TABLE_SCREEN_UNIQUE_ID = "usid";
	public static final String TABLE_SCREEN_FILTER_COLUMN_NAME = "filtercolumn";
	public static final String TABLE_SCREEN_TITLE = "title";
	public static final String TABLE_SCREEN_ROWID = "rowid";
	public static final String TABLE_SCREEN_ITEM = "tableitem";
	public static final String TABLE_SCREEN_DATA_TABLE_NAME = "primarydatatable";
	public static final String TABLE_SCREEN_COLUMN_TABLE_NAME = "TableColumn";
	public static final String TABLE_SCREEN_COLUMN_DATA = "columndata";
	public static final String TABLE_SCREEN_COLUMN_NAME = "columnname";
	public static final String TABLE_SCREEN_COLUMN_TABLE = "combotable";
	public static final String TABLE_SCREEN_COLUMN_UNIQUE_ID = "usid";
	public static final String TABLE_SCREEN_COLUMN_ROWID = "rowid";
	public static final String TABLE_SCREEN_COLUMN_TYPE = "columntype";
	public static final String TABLE_SCREEN_CONTROL_ID = "controlid";
	public static final String TABLE_SCREEN_STYLE_GUIDE_NAME = "styleguidename";
	public static final String TABLE_SCREEN_COLUMN_PRIMARY_LABLE = "primarylabelcontent";
	public static final String TABLE_SCREEN_COLUMN_SECONDARY_LABLE = "secondarylabelcontent";
	public static final String TABLE_SCREEN_COLUMN_TERTIARY_LABLE = "tertiarylabelcontent";
	public static final String TABLE_SCREEN_COLUMN_WIDTH = "columnwidth";
	public static final String TABLE_SCREEN_COLUMN_XML = "columnxml";
	public static final String TABLE_SCREEN_COLUMN_SUMMARY_HEADER_NAME = "summaryheadername";
	public static final String TABLE_SCREEN_COLUMN_SUMMARY_HEADER_XML = "summaryheaderxml";
	public static final String TABLE_SCREEN_HEADER_TABLE_NAME = "TableHeader";
	public static final String TABLE_SCREEN_HEADER_NAME="tableheadername";
	public static final String TABLE_SCREEN_HEADER_DATA="tableheaderdata";
	public static final String TABLE_SCREEN_HEADER_XML="tableheaderxml";
	public static final String TABLE_SCREEN_SUMMARY_HEADER = "showheader";
	public static final String TABLE_SCREEN_SHOW_CUSTOM_BUTTON = "showcustombutton";
	public static final String TABLE_SCREEN_CUSTOM_BUTTON_NAME = "custombuttonname";
	public static final String TABLE_SCREEN_COMBO_COLUMN = "combodata";
	public static final String TABLE_SCREEN_COMBO_FILTERCOLUMN = "combofilter";
	public static final String TABLE_SCREEN_COMBO_WHERECOLUMN = "combowhere";
	public static final String TABLE_SCREEN_COLUMN_PRIMARY_PREFIX = "primaryprefix";
	public static final String TABLE_SCREEN_COLUMN_SECONDARY_PREFIX = "secondaryprefix";
	public static final String TABLE_SCREEN_COLUMN_TERTIARY_PREFIX = "tertiaryprefix";


	// CustomScreen related constants
	public static final String CUSTOM_SCREEN_TABLE = "CustomScreen";
	public static final String CUSTOM_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String CUSTOM_SCREEN_UNIQUE_ID = "usid";
	public static final String CUSTOM_SCREEN_TITLE = "title";
	public static final String CUSTOM_SCREEN_ORIENTATION = "orientation";

	// CustomScreenItems table related constants
	public static final String CUSTOM_SCREEN_ITEMS_TABLE = "CustomScreenItems";
	public static final String CUSTOM_SCREEN_ITEMS_NAVIGATION_UNIQUE_ID = "childnavusid";
	public static final String CUSTOM_SCREEN_ITEMS_CUSTOM_SCREEN_UNIQUE_ID = "customscreenusid";
	public static final String CUSTOM_SCREEN_ITEMS_CUSTOM_SCREEN_HEIGHT = "childheight";

	
	public static final String STYLE_STYLE_NAME = "stylename";
	
	// ToolBarStyle related constants
//	public static final String TOOLBAR_STYLE_TABLE_NAME = "ToolBarStyle";
//	public static final String TOOLBAR_STYLE_STYLE_NAME = "stylename";
//	public static final String TOOLBAR_STYLE_BACKGROUND_COLOR = "backgroundcolor";
//	public static final String TOOLBAR_STYLE_BACKGROUND_IMAGE = "backgroundimage";
//	public static final String TOOLBAR_STYLE_BUTTON_STYLE_NAME = "buttonstylename";

	// TabBarStyle related constants
	public static final String TABBAR_STYLE_TABLE_NAME = "TabBarStyle";
	public static final String TABBAR_STYLE_STYLE_NAME = "stylename";
	public static final String TABBAR_STYLE_SELECTED_STATE_COLOR = "selectedstatecolor";
	public static final String TABBAR_STYLE_NORMAL_STATE_COLOR = "normalstatecolor";
	public static final String TABBAR_STYLE_UNIQUE_ID = "usid";
	public static final String TABBAR_STYLE_LABLE_FONTSTYLE_NAME = "labelfontstyle";
	public static final String TABBAR_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	

	// MultiTabStyle related constants
	public static final String MULTITAB_STYLE_TABLE_NAME = "MultiTabStyle";
	public static final String MULTITAB_STYLE_STYLE_NAME = "stylename";
	public static final String MULTITAB_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	public static final String MULTITAB_STYLE_BACKGROUND_IMAGE = "backgroundimage";
	public static final String MULTITAB_STYLE_SELECTED_STATE_COLOR = "selectedstatecolor";
	public static final String MULTITAB_STYLE_NORMAL_STATE_COLOR = "normalstatecolor";
	public static final String MULTITAB_STYLE_LEFT_ARROW_IMAGE = "leftarrowimage";
	public static final String MULTITAB_STYLE_RIGHT_ARROW_IMAGE = "rightarrowimage";
	public static final String MULTITAB_STYLE_LABLE_FONTSTYLE_NAME = "labelfontstyle";
	public static final String MULTITAB_STYLE_UNIQUE_ID = "usid";

	// TableStyle related constants
	public static final String TABLE_STYLE_TABLE_NAME = "TableStyle";
	public static final String TABLE_STYLE_STYLE_NAME = "stylename";
	public static final String TABLE_STYLE_COLUMN_BACKGROUND_COLOR = "columnbackgroundcolor";
	public static final String TABLE_STYLE_COLUMN_LABEL_FONT_STYLE = "columnlabelfontstyle";
	public static final String TABLE_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	public static final String TABLE_STYLE_BACKGROUND_IMAGE = "backgroundimage";
	public static final String TABLE_STYLE_ALTERNATE_COLOR1 = "alternatecolor1";
	public static final String TABLE_STYLE_ALTERNATE_COLOR2 = "alternatecolor2";
	public static final String TABLE_STYLE_ROW_LABEL_FONT_STYLE = "rowlabelfontstyle";
	public static final String TABLE_STYLE_BUTTON_STYLE_NAME = "buttonstylename";
	public static final String TABLE_STYLE_UNIQUE_ID = "usid";
	public static final String TABLE_STYLE_TABLE_LINE_COLOR = "tablelinecolor";	
	public static final String TABLE_STYLE_SUMMARY_HEADER_BG_COLOR = "summaryheaderbackgroundcolor";
	public static final String TABLE_STYLE_SUMMARY_HEADER_FONT = "summaryheaderfont";
	public static final String TABLE_STYLE_PRIMARY_LABEL_FONT = "primarylabelfont";
	public static final String TABLE_STYLE_SECONDARY_LABEL_FONT = "secondarylabelfont";
	public static final String TABLE_STYLE_TERTIARY_LABEL_FONT = "tertiarylabelfont";
	public static final String TABLE_STYLE_SUMMARY_RESULT_FONT = "summaryresultfont";
	public static final String TABLE_STYLE_SUMMARY_RESULT_BG_COLOR = "summaryresultbackgroundcolor";

	// TitleBarStyle related constants
	public static final String TITLE_BAR_STYLE_TABLE_NAME = "TitleBarStyle";
	public static final String TITLE_BAR_STYLE_STYLE_NAME = "stylename";
	public static final String TITLE_BAR_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	public static final String TITLE_BAR_STYLE_BACKGROUND_IMAGE = "backgroundimage";
	public static final String TITLE_BAR_STYLE_LABEL_FONT_STYLE = "labelfontstyle";
	public static final String TITLE_BAR_STYLE_BUTTON_STYLE_NAME = "buttonstylename";
	public static final String TITLE_BAR_STYLE_UNIQUE_ID = "usid";
	public static final String TITLE_BAR_STYLE_APP_ID = "appid";

	// Reports Styles Constants
	public static final String REPORT_BACKGROUND_COLOR = "bgcolor";
	public static final String REPORT_BACKGROUND_IMAGE = "bgimage";
	public static final String REPORT_HIGHLIGHTED_BAR_COLOR = "highlightedbarcolor";
	public static final String REPORT_HIGHLIGHTED_BAR_STYLE = "highlightedbarlabelstyle";
	public static final String REPORT_AXIS_LABELS_FONT_STYLE = "axislabelsfontstyle";
	public static final String REPORT_AXIS_COLNAME_FONT_STYLE = "axiscolnamefontstyle";
	public static final String REPORT_TOOLBAR_BACKGROUND_COLOR = "toolbarbgcolor";
	public static final String REPORT_BUTTON_STYLE = "buttonfontstyle";
	public static final String REPORT_STYLE_NAME = "styleguidename";
	public static final String REPORT_STYLE_GUIDE_NAME = "stylename";
	public static final String CHART_BACKGROUND_COLOR1 = "chartbgcolor1";
	public static final String CHART_BACKGROUND_COLOR2 = "chartbgcolor2";
	public static final String CHART_BACKGROUND_COLOR3 = "chartbgcolor3";
	public static final String CHART_BACKGROUND_COLOR4 = "chartbgcolor4";
	public static final String CHART_BACKGROUND_COLOR5 = "chartbgcolor5";
	public static final String CHART_BAR_COLOR1 = "barcolor1";
	public static final String CHART_BAR_COLOR2 = "barcolor2";
	public static final String CHART_BAR_COLOR3 = "barcolor3";
	public static final String CHART_BAR_COLOR4 = "barcolor4";
	public static final String CHART_BAR_COLOR5 = "barcolor5";
	public static final String REPORT_BACKGROUND_IMAGE1 = "bgimage1";
	public static final String REPORT_BACKGROUND_IMAGE2 = "bgimage2";
	public static final String REPORT_BACKGROUND_IMAGE3 = "bgimage3";
	public static final String REPORT_BACKGROUND_IMAGE4 = "bgimage4";
	public static final String REPORT_BACKGROUND_IMAGE5 = "bgimage5";

	public static final String REPORT_TITLES_STYLE 					= "charttitlesstyle";
	public static final String REPORT_TABLE_HIGHLIGHTED_COLUMN_COLOR= "selectedcolcolor";
	public static final String REPORT_TABLE_STYLE 					= "tablestyle";
	
	public static final String REPORT_SCREEN_TABLE_NAME 	= "chart";
	public static final String REPORT_TABLE_DIMENSIONTYPE 	= "dimensiontype";
	public static final String REPORT_TABLE_DATATABLENAME 	= "datatablename";

	// Coverflow style related constants
	public static final String COVERFLOW_STYLE_TABLE_NAME 			= "CoverflowStyle";
	public static final String COVERFLOW_STYLE_STYLE_NAME 			= "stylename";
	public static final String COVERFLOW_STYLE_BACKGROUND_COLOR	 	= "backgroundcolor";
	public static final String COVERFLOW_STYLE_BACKGROUND_IMAGE 	= "backgroundimage";
	public static final String COVERFLOW_STYLE_LABLE_FONTSTYLE_NAME = "labelfontstyle";
	public static final String COVERFLOW_STYLE_UNIQUE_ID 			= "usid";
	// Reports Styles Constants
	public static final String REPORT_STYLE_TABLE_NAME = "chartstyle";

	// WebViewScreen style related constants
	public static final String WEBVIEW_SCREEN_TABLE_NAME 			= "WebViewScreen";
	public static final String WEBVIEW_SCREEN_POSITION 				= "position";
	public static final String WEBVIEW_SCREEN_DATA_TABLE_NAME 		= "datatablename";
	public static final String WEBVIEW_SCREEN_COLUMN_CONTENT 		= "columncontent";
	public static final String WEBVIEW_SCREEN_DEFAULT_URL 			= "defaulturl";
	public static final String WEBVIEW_SCREEN_STYLEGUIDE_NAME 		= "styleguidename";
	public static final String WEBVIEW_SCREEN_SHOW_NAVIGATION_BAR 	= "shownavigationbar";
	public static final String WEBVIEW_SCREEN_NAVIGATION_UNIQUE_ID 	= "navusid";
	public static final String WEBVIEW_SCREEN_TITLE = "title";

	// SplashSCreen constants
	public static final String SPLASH_SCREEN_TABLE_NAME = "splashscreen";
	
	// MediaViewerStyle related constants
	public static final String MEDIAVIEWER_SCREEN_STYLE_TABLE_NAME 			= "MediaViewerStyle";
	public static final String MEDIAVIEWER_SCREEN_STYLE_BACKGROUND_COLOR 	= "backgroundcolor";
	public static final String MEDIAVIEWER_SCREEN_STYLE_BACKGROUND_IMAGE 	= "backgroundimage";
	public static final String MEDIAVIEWER_SCREEN_STYLE_LABEL_FONT_STYLE 	= "labelfontstyle";
	
	// LineSeparator Style related constants
	public static final String MF_LINE_SEPARATOR_STYLE_TABLE	= "lineseparator";
	public static final String MF_LINE_SEPARATOR_THICKNESS 		= "thickness";
	public static final String MF_LINE_SEPARATOR_LINE_COLOR 	= "linecolor";

	// Badge Count Style related constants
	public static final String BADGE_COUNT_STYLE_TABLE_NAME 			= "badgecountstyle";
	public static final String BADGE_COUNT_STYLE_STYLE_NAME 			= "stylename";
	public static final String BADGE_COUNT_STYLE_BACKGROUND_COLOR 		= "backgroundcolor";
	public static final String BADGE_COUNT_STYLE_LABEL_FONT_STYLE_NAME 	= "fontstylename";
	
	// Check Box Style related constants
	public static final String CHECK_BOX_STYLE_TABLE_NAME 		= "checkboxstyle";
	public static final String CHECK_BOX_STYLE_NAME 			= "stylename";
	public static final String CHECK_BOX_STYLE_IMAGE_NAME_ON 	= "checkboxonimage";
	public static final String CHECK_BOX_STYLE_IMAGE_NAME_OFF 	= "checkboxoffimage";

	// AppsTable related constants
	public static final String APPS_TABLE_SCREEN_TABLE_NAME 				= "AppsTable";
	public static final String APPS_TABLE_SCREEN_COLUMN_APP_NAME 			= "appname";
	public static final String APPS_TABLE_SCREEN_COLUMN_APP_ICON 			= "appicon2";
	public static final String APPS_TABLE_SCREEN_COLUMN_TITLEBARSTYLENAME 	= "titlebarstylename";
	public static final String APPS_TABLE_SCREEN_COLUMN_APP_ICON_ONE 		= "appicon";
	public static final String APPS_TABLE_SCREEN_COLUMN_PUBLISH_OPTION 		= "publishoption";
	
	// TransDBVersionMapper table related constants
	public static final String TRANSDBVERSIONMAPPER_TABLE_NAME 	= "TransDBVersionMapper";
	public static final String TRANSDBVERSIONMAPPER_VERSION 	= "version";

	// CustomAction table related Constants
	public static final String CUSTOM_ACTION_TABLE_NAME 		= "CustomAction";
	public static final String CUSTOM_ACTION_SOURCE_DATA_TABLE 	= "sourcetable";
	public static final String CUSTOM_ACTION_METHOD_NUMBER 		= "custommethodnumber";

	public static final String CONFIG_TRANS_DB_IS_DELETE	=	"isdelete";
	public static final String SWITCH_ON_INT_VALUE 			=	"1";
	public static final String SWITCH_OFF_INT_VALUE 		=	"0";

	public static final String CONFIG_LAST_MODIFIED_DATE	=	"configlastmodifieddate";

	// CustomActions transdb constants
	public static final String TRANS_IMAGE_URL = "imageurl";
	public static final String TRANS_PERSONA_ID = "personaid";
	public static final String TRANS_DEVICE_ID = "deviceid";
	public static final String DEFAULT_PERSONA_ID = "0";
	// policymaster related constants
	public static final String POLICY_MASTER_POLICYID = "policyid";

	// policyfeatures1 related constants
	public static final String POLICY_FEATURES1_TABLE_NAME = "policymaster";
	public static final String POLICY_FEATURES1_APPUSAGE_MONITOR = "appusagemonitoring";
	public static final String POLICY_FEATURES1_CONNECTION_MONITOR = "connectionmonitoring";
	public static final String POLICY_FEATURES1_APPMETADATA_MONITOR = "appmetamonitoring";
	public static final String POLICY_FEATURES1_DEVICEMETADATA_MONITOR = "devicemetadata";
	public static final String POLICY_FEATURES1_POLICYID = "policyid";

	// policyfeatures2 related constants
	public static final String POLICY_FEATURES2_TABLE_NAME = "policymaster";
	public static final String POLICY_FEATURES2_APPLAUNCH_TIME_MONITOR = "applaunchmonitoring";
	public static final String POLICY_FEATURES2_ERROR_MONITOR = "errormonitoring";
	public static final String POLICY_FEATURES2_EXCEPTION_MONITOR = "exceptionmonitoring";
	public static final String POLICY_FEATURES2_UI_RENDER_TIME_MONITOR = "uirenderingtimemonitoring";
	public static final String POLICY_FEATURES2_NETWORK_LATENCY_MONITOR = "networklatencymonitoring";
	public static final String POLICY_FEATURES2_WARNING_MONITOR = "warningmonitoring";
	public static final String POLICY_FEATURES2_INFO_MONITOR = "infomonitoring";
	public static final String POLICY_FEATURES2_POLICYID = "policyid";

	// policyfeatures3 related constants
	public static final String POLICY_FEATURES3_TABLE_NAME = "policymaster";
	public static final String POLICY_FEATURES3_APPLOGGING = "applogging";
	public static final String POLICY_FEATURES3_AUTHENTICATION = "authentication";
	public static final String POLICY_FEATURES3_DATA_AT_REST_ENCRYPTION = "dataatrestencryption";
	public static final String POLICY_FEATURES3_DATA_ENCRYPTION_KEY = "dataencryptionkey";
	public static final String POLICY_FEATURES3_DB_DATA_ENCRYPTION = "dbdataencryption";
	public static final String POLICY_FEATURES3_DOWNLOAD_LIMIT = "downloadlimit";
	public static final String POLICY_FEATURES3_FILE_SHARING_ACCESS = "filesharingaccess";
	public static final String POLICY_FEATURES3_HIDE_SCREEN = "hidescreen";
	public static final String POLICY_FEATURES3_SECURES_PASTE_BOARD = "securespasteboard";
	public static final String POLICY_FEATURES3_SQL_INJECTION_CHECK = "sqlinjectioncheck";
	public static final String POLICY_FEATURES3_VALIDATE_SQL_INJECTION = "validatesqlinjection";
	public static final String POLICY_FEATURES3_POLICYID = "policyid";

	// policyfeatures4 related constants
	public static final String POLICY_FEATURES4_TABLE_NAME = "policymaster";
	public static final String POLICY_FEATURES4_BATTERY_MONITOR = "batterymonitoring";
	public static final String POLICY_FEATURES4_CPU_MONITOR = "cpumonitoring";
	public static final String POLICY_FEATURES4_DISK_MONITOR = "diskmonitoring";
	public static final String POLICY_FEATURES4_MEMORY_MONITOR = "memorymonitoring";
	public static final String POLICY_FEATURES4_NETWORK_MONITOR = "networkmonitoring";
	public static final String POLICY_FEATURES4_POLICYID = "policyid";

	// geofencing related constants
	public static final String GEO_FENCING_TABLE_NAME = "policymaster";
	public static final String GEO_FENCING_LATITUDE = "latitude";
	public static final String GEO_FENCING_LONGITUDE = "longitude";
	public static final String GEO_FENCING_RADIUS = "radius";
	public static final String GEO_FENCING_GEOFENCING_STATUS = "geofencingstatus";
	public static final String GEO_FENCING_POLICYID = "policyid";

	// networkprotect related constants
	public static final String NETWORK_PROTECT_TABLE_NAME = "policymaster";
	public static final String NETWORK_PROTECT_CONNECTION_TIMEOUT = "connectiontimeout";
	public static final String NETWORK_PROTECT_MULTITHREAD_CLIENT = "multithreadclient";
	public static final String NETWORK_PROTECT_SECURITY_TYPE = "securitytype";
	public static final String NETWORK_PROTECT_WAIT_TIMEOUT = "waittimeout";
	public static final String NETWORK_PROTECT_NETWORK_PROTECT_STATUS = "networkprotectstatus";
	public static final String NETWORK_PROTECT_POLICYID = "policyid";

	// timefencing related constants
	public static final String TIME_FENCING_TABLE_NAME = "policymaster";
	public static final String TIME_FENCING_APP_IDLE_TIME = "appidletime";
	public static final String TIME_FENCING_APP_USAGE_TIME = "appusagetime";
	public static final String TIME_FENCING_END_TIME = "endtime";
	public static final String TIME_FENCING_IDLE_STATUS = "idlestatus";
	public static final String TIME_FENCING_LOCATION_ID = "locationid";
	public static final String TIME_FENCING_START_TIME = "starttime";
	public static final String TIME_FENCING_TIME_FENCING_STATUS = "timefencingstatus";
	public static final String TIME_FENCING_USAGE_STATUS = "usagestatus";
	public static final String TIME_FENCING_TIME_FRAME_STATUS = "timeframestatus";
	public static final String TIME_FENCING_POLICYID = "policyid";

	// Section List Style related constants
	public static final String SECTION_LIST_SCREEN_STYLE_TABLE_NAME = "SectionListStyle";

	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_BACKGROUND_COLOR = "backgroundcolor";
	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_BACKGROUND_IMAGE = "backgroundimage";
	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_ALTERNATIVE_COLOR1 = "alternatecolor1";
	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_ALTERNATIVE_COLOR2 = "alternatecolor2";
	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_CELL_SEPARATOR_COLOR = "cellseparatorcolor";
	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_ITEM1_FONTSTYLE_USID = "item1fontstylename";
	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_ITEM2_FONTSTYLE_USID = "item2fontstylename";
	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_DETAILED_BUTTON_STYLE_NAME = "detailedbuttonstylename";
	public static final String SECTION_LIST_SCREEN_ITEM_STYLE_SELECTION_COLOR = "selectioncolor";
	public static final String SECTION_LIST_SCREEN_STYLE_STYLE_NAME = "stylename";
	public static final String LIST_SCREEN_STYLE_LIST_ITEM_COLOR = "listitemcolor";
	
	public static final String SECTION_LIST_SCREEN_STYLE_SECTION_HEADER_BACKGROUND_IMAGE = "sectionheaderbackgroundimage";
	public static final String SECTION_LIST_SCREEN_STYLE_SECTION_HEADER_BACKGROUND_COLOR = "sectionheaderbackgroundcolor";
	public static final String SECTION_LIST_SCREEN_STYLE_SECTION_HEADER_FONTSTYLE = "sectionheaderfontstylename";

	// Media Viewer Screen related constants
	public static final String MEDIA_VIEWER_SCREEN_TABLE_NAME = "MediaViewer";
	public static final String MEDIA_VIEWER_SCREEN_TITLE = "title";
	public static final String MEDIA_VIEWER_MEDIA_NAME = "medianame";
	public static final String MEDIA_VIEWER_MEDIA_PATH = "mediapath";
	public static final String MEDIA_VIEWER_MEDIA_THUMBNAIL_PATH = "mediathumbnailpath";
	public static final String MEDIA_VIEWER_MEDIA_TYPE = "mediatype";
	public static final String MEDIA_VIEWER_FILE_IS_LOCAL = "islocal";
	public static final String MEDIA_VIEWER_SCREEN_MEDIA_POSITION = "mediaposition";
	public static final String MEDIA_VIEWER_SCREEN_DATA_TABLE_NAME = "datatablename";

	public static final String MEDIA_VIEWER_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String MEDIA_VIEWER_SCREEN_UNIQUE_ID = "usid";
	public static final String MEDIA_VIEWER_SCREEN_STYLEGUIDE_NAME = "styleguidename";
	public static final String MEDIA_VIEWER_MEDIA_TYPE_VIDEO = "V";
	public static final String MEDIA_VIEWER_MEDIA_TYPE_IMAGE = "I";

	// Dialog box related constants
	public static final String CUSTOM_DIALOG_SCREEN_TABLE_NAME = "CustomDialog";
	public static final String CUSTOM_DIALOG_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_TYPE = "contenttype";
	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_TEXT1 = "contenttext1";
	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_TEXT2 = "contenttext2";
	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_TEXT3 = "contenttext3";
	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_TEXT_FLAG = "contenttextflag";
	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_SOURCE_TABLE_NAME = "contentsourcetablename";
	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_SOURCE_COLUMN_NAME = "contentsourcecolumnname";

	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_TARGET_TABLE_NAME = "contenttargettablename";
	public static final String CUSTOM_DIALOG_SCREEN_CONTENT_TARGET_COLUMN_NAME = "contenttargetcolumnname";

	public static final String CUSTOM_DIALOG_SCREEN_NEG_BTN_LABEL = "negativebtnlabel";
	public static final String CUSTOM_DIALOG_SCREEN_NEU_BTN_LABEL = "neutralbtnlabel";
	public static final String CUSTOM_DIALOG_SCREEN_POS_BTN_LABEL = "positivebtnlabel";
	public static final String CUSTOM_DIALOG_SCREEN_TITLE = "title";

	public static final String CUSTOM_DIALOG_SCREEN_FIELD_TYPE1 = "fieldtype1";
	public static final String CUSTOM_DIALOG_SCREEN_FIELD_TYPE2 = "fieldtype2";
	public static final String CUSTOM_DIALOG_SCREEN_FIELD_HINT1 = "fieldhint1";
	public static final String CUSTOM_DIALOG_SCREEN_FIELD_HINT2 = "fieldhint2";
	public static final String CUSTOM_DIALOG_SCREEN_SHOW_DIALOG = "showdialog";
	public static final String CUSTOM_DIALOG_SCREEN_IS_TOAST = "istoast";

	// Animation template related constants
	public static final String ANIMATION_SCREEN_TABLE_NAME = "Animation";
	public static final String ANIMATION_SCREEN_DATA_TABLE_NAME = "animationtablename";
	public static final String ANIMATION_DURATION = "duration";
	public static final String ANIMATION_ISLOOPING = "islooping";
	public static final String ANIMATION_SUFIX = "suffix";
	public static final String ANIMATION_FRAMES_COLUMN = "framescolumnname";
	public static final String ANIMATION_FRAME_POSOTION = "frameposition";
	public static final String ANIMATION_START_VALUE = "startvalue";
	// BL Types Related Constants
	public static final String BL_TYPE_FOR = "for";
	public static final String BL_TYPE_IF = "if";
	public static final String BL_TYPE_WHILE = "while";
	public static final String BL_TYPE_EXPRESSION = "expression";
	public static final String BL_TYPE_IU = "iu";
	public static final String BL_TYPE_DELETE = "delete";
	public static final String BL_TYPE_ONE_TO_MANY = "onetomany";
	public static final String BL_TYPE_CREATE_TABLE = "tablecreator";
	public static final String GET_TYPE_REQUEST = "get";
	public static final String POST_TYPE_REQUEST = "post";
	public static final String BL_DIALOG = "dialog";
	public static final String BL_TOAST = "toast";
	public static final String BL_GLOBAL_BL = "glbl";
	public static final String BL_FILE_DOWNLOAD__BL = "filedownload";
	public static final String BL_FILE_UPLOAD__BL = "fileupload";
	public static final String BL_NOTIFICATION = "notification";
    public static final String BL_SCHEMA_NAME= "schemaname";
    public static final String BL_STRING_MODIFIER="StringModifier";
   // public static final String BL_ADV_SEARCH_BL = "advsearchbl";
	

	// 60.db authentication Table related Constants
	public static final String AUTHENTICATION_TABLE_NAME = "authentication";
	public static final String AUTHENTICATION_TABLE_LOGINCOUNT = "logincount";
	public static final String AUTHENTICATION_TABLE_LOGOFFCOUNT = "logoffcount";
	public static final String AUTHENTICATION_TABLE_NOOFSEQWRONGATTEMPTS = "noofseqwrongattempts";
	public static final String AUTHENTICATION_TABLE_TIMESTAMP = "timestamp";
	public static final String AUTHENTICATION_TABLE_LOGINSUCCESS = "loginsuccess";
	public static final String AUTHENTICATION_TABLE_LOGINFAILURE = "loginfailure";
	public static final String AUTHENTICATION_TABLE_STATUS = "status";
	public static final String AUTHENTICATION_TABLE_MODIFIEDDATE = "modifieddate";
	public static final String AUTHENTICATION_TABLE_USID = "usid";
	public static final String AUTHENTICATION_TABLE_APPID = "appid";
	public static final String AUTHENTICATION_TABLE_DEVICEID = "deviceid";
	public static final String AUTHENTICATION_TABLE_USERID = "userid";

	// WHERE Related Constants for All Templates
	public static final String USE_WHERE = "usewhere";
	public static final String WHERE_COLUMN_NAME = "wherecolumn";
	public static final String WHERE_CONSTANT = "whereconstant";
	public static final String RETAIN_WHERE_CLAUSE = "retainwhereclause";
	
	public static final String SHOW_INDEX_COLUMN = "indexedcolumn";
	public static final String SHOW_INDEX_LIST = "showindexedtable";


	// MultiFormTextConfig Table related constants
	public static final String MULTIFORM_WIDGET_STYLE_TABLE_NAME = "MultiFormWidgetStyle";
	public static final String MULTIFORM_WIDGET_STYLE_PARENT_USID = "parentusid";
	public static final String MULTIFORM_WIDGET_STYLE_FORM_USID = "formusid";
	public static final String MULTIFORM_WIDGET_STYLE_POSITION = "widgetposition";
	public static final String MULTIFORM_WIDGET_LEFT_MARGIN = "leftmargin";
	public static final String MULTIFORM_WIDGET_RIGHT_MARGIN = "rightmargin";
	public static final String MULTIFORM_WIDGET_WIDTH = "width";
	public static final String MULTIFORM_WIDGET_TOP_MARGIN = "topmargin";
	public static final String MULTIFORM_WIDGET_BOTTOM_MARGIN = "bottommargin";
	public static final String MULTIFORM_WIDGET_HEIGHT = "height";
	public static final String MULTIFORM_WIDGET_STYLENAME = "stylename";

	// sqlite master Table related Constants
	public static final String SQLITE_MASTER_TABLE_NAME = "sqlite_master";
	public static final String SQLITE_MASTER_TABLE_COLUMN_NAME = "name";

	// Custom Table
	public static final String CUSTOM_TABLE_NAME = "customtablename";
	public static final String CUSTOM_COLUMN_NAME = "customcolumnname";
	public static final String SCREEN_USID = "screenusid";

	// StyleGuide Config Table
	public static final String STYLEGUIDE_SUBLIST_TABLE_NAME = "styleguidesublist";
	public static final String STYLEGUIDE_STYLE_NAME = "stylename";
	public static final String STYLEGUIDE_STYLE_SUBLIST_TYPEID = "sublisttypeid";
	public static final String STYLEGUIDE_STYLE_STYLEID = "styleid";
	public static final String STYLEGUIDE_STYLE_DETAILS_TYPEID = "detailstypeid";
	
	
	//ScreenDesign Table
	public static final String TABLE_SCREEN_DESIGN_LIST  		= "screendesignlist";
	public static final String TABLE_SCREEN_DESIGN_SUB_LIST  	= "screendesignsublist";
	public static final String COLUMN_SCREEN_DESIGN_ID			= "screendesignid";
	
	
	//ScreenDesignAction Table
	public static final String SCREEN_DESIGN_ACTION  = "screendesignaction";
	public static final String SCREE_DESIGN_BL_NAME = "blname";
	public static final String SCREE_DESIGN_BL_ACTION_USID = "blactionusid";
	
	public static final String SCREE_DESIGN_GET_ACTION_USID = "getactionusid";
	public static final String SCREE_DESIGN_POST_ACTION_USID = "postactionusid";
	public static final String SCREE_DESIGN_SOCIAL_INTEGRATION_ACTION_USID = "socialintegrationactionusid";
	public static final String SCREE_DESIGN_MAIL_ACTION_USID = "mailactionusid";
	public static final String SCREE_DESIGN_CALL_ACTION_USID = "callactionusid";
	public static final String SCREE_DESIGN_SMS_ACTION_USID = "smsactionusid";
	public static final String SCREE_DESIGN_CUSTOM_ACTION_USID = "customactionusid";
	
	public static final String SCREE_DESIGN_GET_IS_DELETE = "isgetdelete";
	public static final String SCREE_DESIGN_POST_IS_DELETE = "ispostdelete";
	public static final String SCREE_DESIGN_BL_IS_DELETE = "isbldelete";
	public static final String SCREE_DESIGN_MAIL_IS_DELETE = "ismaildelete";
	public static final String SCREE_DESIGN_CALL_IS_DELETE = "iscalldelete";
	public static final String SCREE_DESIGN_SMS_IS_DELETE = "issmsdelete";
	public static final String SCREE_DESIGN_CUSTOM_IS_DELETE = "iscustommethoddelete";
	public static final String SCREE_DESIGN_SOCIAL_INTEGRATION_IS_DELETE = "issocialintegrationdelete";
	public static final String SCREE_DESIGN_BUTTON_ID = "buttonid";
	public static final String SCREE_DESIGN_NAVUSID = "navusid";
	
	
	
	
	//BLComplete Table 
	public static final String BL_COMPLETE = "blcomplete";
	public static final String BL_COMPLETE_BL_NAME = "blname";
	
	//sdMultiformItems
	public static final String SD_MULTIFORM_ITEMS_TABLENAME = "sdMultiformScreenitems";
	public static final String SD_MULTIFORM_ITEMS_COLUMNNAME = "columnname";
	public static final String SD_MULTIFORM_ITEMS_COLUMN_TYPE = "columntype";
	public static final String SD_MULTIFORM_ITEMS_COLUMNCONTENT = "columncontent";
	public static final String SD_MULTIFORM_ITEMS_COLUMNID = "columnid";
	public static final String SD_MULTIFORM_ITEMS_ISHIDDEN = "ishidden";
	public static final String SD_MULTIFORM_ITEMS_ISREADONLY = "isreadonly";
	public static final String SD_MULTIFORM_ITEMS_ISMANDATORY = "ismandatory";
	public static final String SD_MULTIFORM_ITEMS_PICKERTYPE = "pickertype";
	public static final String SD_MULTIFORM_ITEMS_PICKERCONTENT = "pickercontent";
	public static final String SD_MULTIFORM_ITEMS_PICKERDATATABLENAME = "pickerdatatablename";
	public static final String SD_MULTIFORM_ITEMS_INPUTTYPE = "inputtype";
	public static final String SD_MULTIFORM_ITEMS_PICKERPOSITION = "pickerposition";
	public static final String SD_MULTIFORM_ITEMS_USEWHERE = "usewhere";
	public static final String SD_MULTIFORM_ITEMS_WHERECONSTANT = "whereconstant";
	public static final String SD_MULTIFORM_ITEMS_WHERECOLUMN = "wherecolumn";
	public static final String SD_MULTIFORM_ITEMS_PARENTUSID = "parentusid";
	public static final String SD_MULTIFORM_ITEMS_FORMUSID = "formusid";
	public static final String SD_MULTIFORM_ITEMS_FORMITEMUSID = "formitemsusid";
	public static final String SD_MULTIFORM_ITEMS_COLUMNINDEX = "columnindex";
	public static final String SD_MULTIFORM_ITEMS_COLUMNALIGNMENT = "columnalignment";
	public static final String SD_MULTIFORM_ITEMS_COLUMNISDELETE = "columnisdelete";
	public static final String SD_MULTIFORM_ITEMS_USID = "usid";
	
	//Navigationdrawer

    public static final String NAVIGATION_DRAWER_SCREEN_TABLE_NAME = "slidingmenu";
    public static final String NAVIGATION_DRAWER_SCREEN_NAVIGATION_UNIQUE_ID = "navusid";
    public static final String NAVIGATION_DRAWER_SCREEN_UNIQUE_ID = "usid";
    public static final String NAVIGATION_DRAWER_SCREEN_CHILD_NAVIGATION_UNIQUE_ID = "childnavusid";
    public static final String NAVIGATION_DRAWER_SCREEN_SECTION_ITEM = "sectionitem";
    public static final String NAVIGATION_DRAWER_SCREEN_MENU_ITEM = "menuitems";
    public static final String NAVIGATION_DRAWER_SCREEN_MENU_ICON = "menuicon";
    public static final String NAVIGATION_DRAWER_SCREEN_TAILING_COUNT = "tailingcount";
    public static final String NAVIGATION_DRAWER_SCREEN_STYLEGUIDE_NAME = "styleguidename";
    public static final String NAVIGATION_DRAWER_SCREEN_MENU_ITEM_TYPE = "menuitemtype";
    public static final String NAVIGATION_DRAWER_SCREEN_SHOW_SEARCH = "showsearch";
    public static final String NAVIGATION_DRAWER_SCREEN_SHOW_TAILING = "showtailingcounter";
    public static final String NAVIGATION_DRAWER_SCREEN_STATUS = "status";
    public static final String NAVIGATION_DRAWER_SCREEN_SECTION_ITEM_TEXT = "sectionitemtext";
    
    public static final String NAVIGATION_DRAWER_SCREEN_SHOW_TAILING_TABLE_NAME = "tailingcounttable";
    public static final String NAVIGATION_DRAWER_SCREEN_SHOW_TAILING_COLUMN_NAME = "tailingcountcolumn";
    
	// MultiFormGridScreen Table related Constants
	public static final String MULTI_FORM_GRID_SCREEN_TABLE = "multiformgridscreen";
	public static final String MULTI_FORM_GRID_SCREEN_ITEMS = "multiformgridscreenitems";
	public static final String MULTI_FORM_GRID_ALIGNMENT = "multiformgridalignment";
	public static final String MULTI_FORM_GRID_PARENT_USID ="parentusid";
	
	public static final String MULTI_FORM_GRID_TEXT_FIELD ="Textfield";
	public static final String MULTI_FORM_GRID_PICKER ="Picker";
	public static final String MULTI_FORM_GRID_RADIO_BUTTON ="Radio Button";
	public static final String MULTI_FORM_GRID_SWITCH ="Switch";
	public static final String MULTI_FORM_GRID_CHECK_BOX ="Check Box";
	public static final String MULTI_FORM_GRID_DATE_PICKER ="Date Picker";
	public static final String MULTI_FORM_GRID_COLOR_PICKER ="Color Picker";
	public static final String MULTI_FORM_GRID_TIME_PICKER ="Time Picker";
	public static final String MULTI_FORM_GRID_CAMERA ="Camera";
	public static final String MULTI_FORM_GRID_NONE ="none";
	public static final String MULTI_FORM_GRID_EMPTY ="Empty";
	public static final String MULTI_FORM_LINE_SEPARATOR ="Line Separator";
	public static final String MULTI_FORM_GRID_RATING_CONTROL ="Rating Control";
	public static final String MULTI_FORM_GRID_IMAGE ="Image";	
	public static final String MULTI_FORM_AUDIO_VIDEO ="Audio/Video";
	public static final String MULTI_FORM_PHONE_CONTACT_PICKER ="Contact Picker";
	
	public static final String MULTI_FORM_GRID_SWITCH_STYLE ="switchstyle";
	public static final String MULTI_FORM_GRID_RATING_STYLE ="ratingcontrolstyle";
    
	//loginitems table related constants
	public static final String LOGIN_ITEMS_TABLE = "loginitems";
	public static final String LOGIN_ITEMS_LOGIN_URL = "loginurl";
	public static final String LOGIN_ITEMS_GUEST_BUTTON_ID = "guestbuttonid";
	
	
    //Font style constants
    
    public static final  String BOLD_VALUE = "1";
    public static final  String ITALIC_VALUE = "2";
    public static final  String BOLD_ITALIC_VALUE = "3";

    public static final  String BOLD_STRING = "BOLD";
    public static final  String ITALIC_STRING = "ITALIC";
    public static final  String BOLD_ITALIC_STRING = "BOLD_ITALIC";
    
    
 // Transaction fail  queue table related constants
 	public static final String TRANSACTION_QUEUE_TABLE_NAME = "failedtransactionqueue";
 	public static final String TRANSACTION_QUEUE_TYPE = "actiontype";
 	public static final String TRANSACTION_QUEUE_ACTION_UNIQUE_ID = "actionusid";
 	public static final String TRANSACTION_QUEUE_STATUS = "status";
 	public static final String TRANSACTION_QUEUE_IS_DELETE = "isdelete";
 	public static final String TRANSACTION_QUEUE_MODIFIED_DATE = "modifieddate";
 	public static final String TRANSACTION_QUEUE_SERVER_URL="url";
 	public static final String TRANSACTION_QUEUE_DATA_TABLE_NAME="datatablename";
 	public static final String TRANSACTION_QUEUE_ERROR_MESSAGE="errormessage";
 	
 	
 	//BL Constant IU
	public static final String BL_CONSTANT_VALUE_IU_TYPE = "constantiu";
	
	public static final String NEGATIVE_APPS_LIST = "appList";
	
	
	//Launch BL Constant
	public static final String LAUNCH_BL = "launchbl";
}

package watch.oms.omswatch.fragments;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import watch.oms.omswatch.OMSDTO.PickerItems;
import watch.oms.omswatch.R;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.helpers.MultiFormScreenHelper;
import watch.oms.omswatch.helpers.OMSTransHelper;
import watch.oms.omswatch.interfaces.SendDataDialogListener;
import watch.oms.omswatch.utils.CustomToast;
import watch.oms.omswatch.utils.ReadableDateGenerator;
import watch.oms.omswatch.widgets.DatePickerDialogFragment;
import watch.oms.omswatch.widgets.TimePickerDialogFragment;
import watch.oms.omswatch.widgets.WatchButton;
import watch.oms.omswatch.widgets.WatchEditText;
import watch.oms.omswatch.widgets.WatchSpinner;
import watch.oms.omswatch.widgets.WatchTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MultiFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultiFormFragment extends Fragment implements SendDataDialogListener ,View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String TAG = this.getClass().getSimpleName();

    private String navusid = "";
    private String uiHeadingTitle = "";
    private boolean isPrepopulated = false;
    private String transUniqueId = null;
    private int customContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
    private int configDBAppId;
    private boolean isFromLoadScreen = false;
    private MultiFormScreenHelper multiFormScreenHelper;
    View view = null;
    private Activity activityContext = null;
    HashMap<String, String> formDataMap;

    private Hashtable<Integer, Hashtable<String, Object>> formGridItemsHash = null;
    private String multiFormScreenuniqueId = "";


    LinearLayout formParentLayout;

    int widgetIds[] = {
            R.id.widget1,
            R.id.widget2,
            R.id.widget3,
            R.id.widget4,
            R.id.widget5,
            R.id.widget6,
            R.id.widget7,
            R.id.widget8,
            R.id.widget9,
            R.id.widget10
    };
    SimpleDateFormat formatter;
    int widgetId = -1;
    int timePickerWidgetId = -1;
    private OMSTransHelper tdbHelper;
    private List<String> pickerList;
    TextView saveForm;
    HashMap<String, String> transColumnValHash = new HashMap<String, String>();
    int savedInsertedId = -1;
    private String multiFormScreenDataTableName = "";
    private ArrayList<String> transUsidList;

    public MultiFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MultiFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MultiFormFragment newInstance(String param1, String param2) {
        MultiFormFragment fragment = new MultiFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        activityContext = getActivity();
        if (args != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/


            navusid = args.getString(OMSMessages.UNIQUE_ID.getValue());
            uiHeadingTitle = args.getString(OMSMessages.TITLE.getValue());
            isPrepopulated = args.getBoolean(OMSMessages.IS_PREPOPULATED
                    .getValue());
            transUniqueId = args.getString(OMSMessages.TRANS_USID.getValue());
            customContainerId = args.getInt(OMSMessages.CUSTOM_CONTAINERID
                    .getValue());
            configDBAppId = args.getInt(OMSMessages.CONFIGAPP_ID.getValue());
            isFromLoadScreen = args.getBoolean(OMSMessages.IS_LOADSCREEN
                    .getValue());
            /*screenMode = args.getBoolean(OMSMessages.SCREEN_MODE.getValue(),
                    true);
            signature = args.getString(OMSMessages.SIGNATURE.getValue(), null);
            screenOrder = args.getInt(OMSMessages.SCREEN_ORDER.getValue());
            fromsectionList = args.getBoolean(OMSMessages.FROM_SECTIONLIST
                    .getValue());
            showadd = args.getBoolean(OMSMessages.SHOW_ADD.getValue());
            isAddFromSplitView = args.getBoolean(
                    OMSMessages.SPLIT_VIEW.getValue(), false);

            showLogOut = args.getBoolean(OMSMessages.SHOW_LOGOUT.getValue(),
                    false);
            addEvent = args.getBoolean("addEvent", false);
            sectionName = args.getString(
                    OMSMessages.SECTION_ITEM_NAME.getValue(), "");
            isFromScroller = args.getBoolean("FROM_SCROLLER", false);*/


        }
        tdbHelper = new OMSTransHelper(activityContext);
        pickerList = new ArrayList<String>();
        multiFormScreenHelper = new MultiFormScreenHelper(activityContext);
        transUsidList = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_multi_form, container, false);
        Toolbar mToolBar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ImageView back_img = (ImageView) mToolBar.findViewById(R.id.back);
        saveForm = (TextView) mToolBar.findViewById(R.id.save_form);
        saveForm.setVisibility(View.VISIBLE);
        saveForm.setOnClickListener(this);
        formParentLayout = (LinearLayout) view.findViewById(R.id.formParent);
        if (isFromLoadScreen) {
            try {
                formDataMap = multiFormScreenHelper
                        .getMultiFormScreenData(navusid, configDBAppId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            formDataMap = multiFormScreenHelper
                    .getMultiFormScreenData(navusid, isPrepopulated,
                            configDBAppId);
        }

        if (formDataMap != null && formDataMap.size() > 0) {
            multiFormScreenuniqueId = formDataMap.get(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID);
            multiFormScreenDataTableName =  formDataMap.get(OMSDatabaseConstants.MULTI_FORM_SCREEN_DATA_TABLE_NAME);
            if(Integer.parseInt(formDataMap.get(OMSDatabaseConstants.FORM_SCREEN_IS_PREPOPULATED))!=0){
             isPrepopulated = true;
            }
        }
//multiFormScreenuniqueId = multiFormScreenDataCursor .getString(multiFormScreenDataCursor.getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID));
        if (multiFormScreenuniqueId != null) {
            formGridItemsHash = multiFormScreenHelper
                    .getMultiFormScreenGridItems(multiFormScreenuniqueId,
                            configDBAppId);
        }
        Log.d(TAG, "FormItems Hash Size:::" + formGridItemsHash.size());
        if (formGridItemsHash != null && formGridItemsHash.size() > 0) {
            createFormUserInterface(getActivity(), false);
        }

        return view;
    }


    private void createFormUserInterface(Context context, boolean isSave) {
        Log.d(TAG, "In createFormUserInterface()");
        Hashtable<String, Object> transHashTable = null;
        String columnContent = "";

        if (isPrepopulated) {
            try {
                transHashTable = tdbHelper
                        .getTransDataForPrepopulatedMultiForm(
                                multiFormScreenDataTableName,
                                transUniqueId, "", "");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (transHashTable != null) {
            Enumeration<String> keys = transHashTable.keys();
            if (keys.hasMoreElements()) {
                transUniqueId = (String) transHashTable
                        .get(OMSDatabaseConstants.UNIQUE_ROW_ID);
                if (transUsidList != null) {
                    transUsidList.clear();
                    transUsidList.add(transUniqueId);
                } else {
                    transUsidList = new ArrayList<String>();
                    transUsidList.add(transUniqueId);
                }
            }

        }


        int rowCount = formGridItemsHash.size();
        Log.d(TAG, "rowCount::::" + rowCount);
        Hashtable<String, Object> tempHashTable = null;
        for (int i = 0; i < rowCount; i++) {
            Log.d(TAG, "I val initial" + i);
            tempHashTable = formGridItemsHash.get(i);
            if (tempHashTable != null) {
                Enumeration<String> keys = tempHashTable.keys();
                if (keys.hasMoreElements()) {
                    String widgetType = tempHashTable.get("columntype").toString();
                    String widgetName = "";
                    if (!TextUtils.isEmpty(tempHashTable.get("columnname").toString().trim())) {
                        widgetName = tempHashTable.get("columnname").toString().trim();
                    }
                    if (!TextUtils.isEmpty(tempHashTable.get("columncontent").toString().trim())) {
                        columnContent = tempHashTable.get("columncontent").toString().trim();
                    }

                    if (!(TextUtils.isEmpty(widgetType))
                            && (widgetType.length() > 0)) {
                        if (widgetType.equalsIgnoreCase("Label")) {

                            if (!isSave) {
                                TextView label = WatchTextView.getInstance().fetchTextView(getActivity(), widgetName, widgetIds[i], null);
                                //   Log.d(TAG,"I val ..."+i);
                                formParentLayout.addView(label);

                                        if(isPrepopulated){

                                            if (columnContent != null
                                                    && columnContent.length() > 0) {
                                                Object colVal = transHashTable
                                                        .get(columnContent);
                                                if (colVal != null) {
                                                    if(columnContent.equalsIgnoreCase("transactiondate")){
                                                        /*String columnContent = (String)""+colVal;*/
                                                        String readableDate =  ReadableDateGenerator.getInstance().getReadableDate(""+columnContent,"yy-MM-dd","dd MMM, yyyy");
                                                        label.setText(readableDate);// format output
                                                    }else{
                                                            label.setText((CharSequence) colVal);
                                                        }
                                                    }

                                            } else {
                                                label.setText(widgetName);
                                            }

                                        }


                            }
                        } else if (widgetType.equalsIgnoreCase(OMSDatabaseConstants.MULTI_FORM_SCREEN_EDIT_TEXT_TYPE)) {

                            if (!isSave) {
                                EditText editText = WatchEditText.getInstance().fetchEditText(getActivity(), widgetName, widgetIds[i], null);
                                Log.d(TAG, "I val ..." + i);
                                formParentLayout.addView(editText);
                                if(isPrepopulated){
                                    if(columnContent.equalsIgnoreCase("transactiondate")){
                                        String columnContent_val = (String)""+(CharSequence) transHashTable
                                                .get(columnContent
                                                        .trim());
                                        String readableDate = ReadableDateGenerator.getInstance().getReadableDate(columnContent_val, "yy-MM-dd", "dd MMM, yyyy");
                                        editText.setText(readableDate);// format output
                                    }else{
                                        editText
                                                .setText((CharSequence) transHashTable
                                                        .get(columnContent
                                                                .trim()));
                                    }
                                }
                            } else {

                                EditText editTextVal = (EditText) view.findViewById(widgetIds[i]);
                                String val = editTextVal.getText().toString();
                                if (!TextUtils.isEmpty(columnContent)) {
                                    transColumnValHash.put(columnContent, val);
                                }

                            }
                        } else if (widgetType.equalsIgnoreCase(OMSDatabaseConstants.MULTI_FORM_SCREEN_BUTTON_TYPE)) {

                            if (!isSave) {
                                Button watchButton = WatchButton.getInstance().fetchButton(getActivity(), widgetName, widgetIds[i], null);
                                Log.d(TAG, "I val ..." + i);
                                formParentLayout.addView(watchButton);
                            }
                        } else if (widgetType.equalsIgnoreCase(OMSDatabaseConstants.MULTI_FORM_SCREEN_DATE_PICKER_TYPE)) {

                            widgetId = widgetIds[i];
                            TextView watchDatePickerLabel = null;
                            if (!isSave) {
                                watchDatePickerLabel = WatchTextView.getInstance().fetchTextView(getActivity(), widgetName, widgetIds[i], null);
                                Log.d(TAG, "I val ..." + i);
                                formParentLayout.addView(watchDatePickerLabel);
                                setDate(widgetId, "label");

                                watchDatePickerLabel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        launchDatePicker(widgetId, "label");
                                    }
                                });

                                if(isPrepopulated){
                                   if(columnContent!=null){
                                       String columnVal = (String) transHashTable
                                               .get(columnContent);
                                       if (!columnVal.isEmpty()
                                               && columnVal.length() > 0
                                               && columnVal != null) {
                                           String datePickerColumnContent = (String)""+columnVal;
                                           String readableDate="";
                                           if(datePickerColumnContent.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")){
                                               readableDate = ReadableDateGenerator.getInstance().getReadableDate(datePickerColumnContent, "yyyy-MM-dd", "dd MMM, yyyy");
                                           }else{
                                               readableDate = ReadableDateGenerator.getInstance().getReadableDate(datePickerColumnContent, "dd MMM, yyyy", "dd MMM, yyyy");
                                           }
                                           watchDatePickerLabel
                                                   .setText(readableDate);
                                       }else{
                                           watchDatePickerLabel
                                                   .setText(widgetName);
                                       }
                                   }else{
                                       watchDatePickerLabel
                                               .setText(widgetName);
                                   }
                                }
                            } else {
                                TextView dateView = (TextView) view.findViewById(widgetIds[i]);
                                String val = dateView.getText().toString();
                                if (!TextUtils.isEmpty(columnContent)) {
                                    if (columnContent.equalsIgnoreCase("transactiondate")) {

                                        String readableDate = ReadableDateGenerator.getInstance().getReadableDate(val, "dd MMM, yyyy", "yy-MM-dd");
                                        transColumnValHash.put(columnContent, readableDate);
                                    } else {

                                        String readableDate = ReadableDateGenerator.getInstance().getReadableDate(val, "dd MMM, yyyy", "yyyy-MM-dd");
                                        transColumnValHash.put(columnContent, readableDate);
                                        //	transDBValuesList.add(valueviews.getText().toString());
                                    }

                                }
                            }

                        } else if (widgetType.equalsIgnoreCase(OMSDatabaseConstants.MULTI_FORM_GRID_TIME_PICKER)) {

                            timePickerWidgetId = widgetIds[i];
                            TextView watchDatePickerLabel = null;
                            if (!isSave) {
                                watchDatePickerLabel = WatchTextView.getInstance().fetchTextView(getActivity(), widgetName, widgetIds[i], null);
                                Log.d(TAG, "I val ..." + i);
                                formParentLayout.addView(watchDatePickerLabel);
                                setTime(timePickerWidgetId, "label");
                                watchDatePickerLabel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        launchTimePicker(timePickerWidgetId, "label");
                                    }
                                });
                                if(isPrepopulated){
                                    if(columnContent!=null){
                                        String columnVal = (String) transHashTable
                                                .get(columnContent);
                                        String result = ReadableDateGenerator.getInstance().parseTime(columnVal);
                                        columnVal=result;
                                        if (!columnVal.isEmpty()
                                                && columnVal.length() > 0
                                                && columnVal != null) {
                                            watchDatePickerLabel
                                                    .setText(columnContent);
                                            //timeInString = columnContent;
                                        } else {
                                            watchDatePickerLabel
                                                    .setText(widgetName);
                                            //timeInString = widgetName;
                                        }
                                    }else{
                                        watchDatePickerLabel
                                                .setText(widgetName);
                                    }
                                }
                            } else {
                                TextView timeView = (TextView) view.findViewById(widgetIds[i]);
                                String val = timeView.getText().toString();
                                if (!TextUtils.isEmpty(columnContent)) {
                                    transColumnValHash.put(columnContent, val);
                                }
                            }

                        } else if (widgetType
                                .equalsIgnoreCase(OMSDatabaseConstants.MULTI_FORM_SCREEN_PICKER_TYPE)) {
                            Spinner choicePicker = null;
                            if (!isSave) {
                                List<PickerItems> pickerData = new ArrayList<PickerItems>();
                                Log.d(TAG, "I Val Before" + i);
                                final int position = i + 1;
                                final String pickerUsid = (String) tempHashTable
                                        .get(OMSDatabaseConstants.UNIQUE_ROW_ID);
                                final String formUsid = (String) tempHashTable
                                        .get(OMSDatabaseConstants.FORM_SCREEN_ITEMS_FORM_USID);
                                pickerData = multiFormScreenHelper
                                        .getPickerData(formUsid,
                                                pickerUsid,
                                                Integer.toString(position),
                                                configDBAppId);
                                if (pickerData != null && pickerData.size() > 0) {
                                    try {
                                        final String pickerTransDBColumnContent = pickerData
                                                .get(0).pickercontent;
                                        final String pickerTransDataTable = pickerData
                                                .get(0).pickerdatatablename;
                                        final String usId = pickerData
                                                .get(0).usId;
                                        pickerList = tdbHelper
                                                .getPickerDataWithDependency(
                                                        pickerTransDataTable,
                                                        pickerTransDBColumnContent,
                                                        null,
                                                        null, null);
                                        if (pickerList != null && pickerList.size() > 0) {

                                             choicePicker = WatchSpinner.getInstance().fetchSpinnerView(getActivity(), widgetName, widgetIds[i], null);

                                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                                    activityContext,
                                                    R.layout.spinner_item,
                                                    pickerList);
                                     /*  dataAdapter
                                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
                                            choicePicker
                                                    .setAdapter(dataAdapter);
                                            Log.d(TAG, "I val ..." + i);
                                            formParentLayout.addView(choicePicker);


                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                     choicePicker = WatchSpinner.getInstance().fetchSpinnerView(getActivity(), widgetName, widgetIds[i], null);
                                    Log.d(TAG, "I val ...Spinner else " + i);
                                    formParentLayout.addView(choicePicker);

                                }

                                if(isPrepopulated){
                                    if(columnContent!=null){
                                        String spinnerItemText = (String) transHashTable
                                                .get(columnContent);
                                        if (pickerList
                                                .size() > 0) {
                                            if (spinnerItemText != null) {
                                                if (pickerData
                                                        .get(0).pickermapping == 1) {
                                                    spinnerItemText = tdbHelper
                                                            .getPickerKeyForValue(
                                                                    pickerData
                                                                            .get(0).pickerdatatablename,
                                                                    pickerData
                                                                            .get(0).pickerkey,
                                                                    pickerData
                                                                            .get(0).pickercontent,
                                                                    spinnerItemText);
                                                }
                                                int pos = pickerList
                                                        .indexOf(spinnerItemText);
                                                choicePicker
                                                        .setSelection(pos);
                                            }
                                        }
                                    }
                                }

                            } else {
                                Spinner mySpinner = (Spinner) view.findViewById(widgetIds[i]);
                                if (!TextUtils.isEmpty(mySpinner.getSelectedItem().toString())) {
                                    String val = mySpinner.getSelectedItem().toString();
                                    if (!TextUtils.isEmpty(columnContent)) {
                                        transColumnValHash.put(columnContent, val);
                                    }
                                }
                            }
                        } else if (widgetType
                                .equalsIgnoreCase("none")) {
                            if (!isSave) {
                                TextView label = WatchTextView.getInstance().fetchTextView(getActivity(), "Empty", widgetIds[i], null);
                                //   Log.d(TAG,"I val ..."+i);
                                label.setVisibility(View.INVISIBLE);
                                formParentLayout.addView(label);
                            }
                        }

                    }
                }
            }
        }
    }


    private void launchDatePicker(int widgetID, String widgetType) {
        if (widgetType.equalsIgnoreCase("Label")) {
            TextView dateLabel = (TextView) view.findViewById(widgetID);
            FragmentManager fm = getFragmentManager();
            DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment();
            Bundle args = new Bundle();
            args.putString("date", dateLabel.getText().toString());
            args.putInt("widgetId", widgetID);
            args.putSerializable("type", widgetType);
            dialogFragment.setArguments(args);
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(fm, "Date Dialog Fragment");
        }
    }

    private void launchTimePicker(int widgetID, String widgetType) {
        if (widgetType.equalsIgnoreCase("Label")) {
            TextView dateLabel = (TextView) view.findViewById(widgetID);
            FragmentManager fm = getFragmentManager();
            TimePickerDialogFragment dialogFragment = new TimePickerDialogFragment();
            Bundle args = new Bundle();
            args.putString("time", dateLabel.getText().toString());
            args.putInt("widgetId", widgetID);
            args.putSerializable("type", widgetType);
            dialogFragment.setArguments(args);
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(fm, "Time  Dialog Fragment");
        }
    }

    private void setDate(int widgetId, String widgetType) {
        if (widgetType.equalsIgnoreCase("Label")) {
            TextView dateLabel = (TextView) view.findViewById(widgetId);
            formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            Date dt = new Date();
            dateLabel.setText(formatter.format(dt));
            dateLabel.setTextColor(Color.parseColor("#999999"));

        }
    }

    private void setTime(int widgetId, String widgetType) {
        if (widgetType.equalsIgnoreCase("Label")) {
            TextView dateLabel = (TextView) view.findViewById(widgetId);
            formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date dt = new Date();
            dateLabel.setText(formatter.format(dt));
            dateLabel.setTextColor(Color.parseColor("#999999"));

        }
    }

    @Override
    public void onFinishDialog(String inputText, String type, int widgetId) {
        if (!TextUtils.isEmpty(inputText)) {
            if (type.equalsIgnoreCase("Label")) {
                TextView dateLabel = (TextView) view.findViewById(widgetId);
                try {
                    if (!TextUtils.isEmpty(inputText)) {
                        //Oct 1,2015
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "dd MMM, yyyy", Locale.ENGLISH);
                        // Date myDate;
                        Date myDate = dateFormat.parse(inputText);
                        dateLabel.setText(dateFormat.format(myDate));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFinishTimeDialog(String inputText, String type, int widgetId) {
        Log.d(TAG, "onFinishTimeDialog:::" + inputText);
        if (!TextUtils.isEmpty(inputText)) {
            if (type.equalsIgnoreCase("Label")) {
                try {
                    TextView TimeLabel = (TextView) view.findViewById(widgetId);
                    //Oct 1,2015
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "HH:mm", Locale.ENGLISH);
                    // Date myDate;
                    Date myTime = dateFormat.parse(inputText);
                    TimeLabel.setText(dateFormat.format(myTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_form:
                CustomToast.getInstance().displayToast("Clicked on Save Form", getActivity(), getActivity());

                //   Toast.makeText(getActivity(),"Clicked on Form Save",Toast.LENGTH_LONG).show();
                createFormUserInterface(getActivity(), true);
                saveFormData();
                break;
        }
    }

    private void saveFormData() {
        ContentValues newrowvalues = new ContentValues();
        if (transColumnValHash != null && transColumnValHash.size() > 0) {
            Set<Map.Entry<String, String>> entrySet1 = transColumnValHash.entrySet();
            Iterator<Map.Entry<String, String>> entrySetIterator = entrySet1.iterator();
            while (entrySetIterator.hasNext()) {
                System.out.println("------------------------------------------------");
                System.out.println("Iterating HashMap in Java using EntrySet and Java iterator");
                Map.Entry entry = entrySetIterator.next();
                System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
                newrowvalues.put(entry.getKey().toString(), entry.getValue().toString());
            }
            Calendar cal = Calendar.getInstance();
            String insertedUSID = Long.toString(cal.getTimeInMillis());
            newrowvalues.put(OMSDatabaseConstants.UNIQUE_ROW_ID,
                    insertedUSID);
            newrowvalues.put(
                    OMSDatabaseConstants.TRANS_TABLE_STATUS_COLUMN,
                    OMSDatabaseConstants.STATUS_TYPE_NEW);
            newrowvalues.put(
                    OMSDatabaseConstants.CONFIG_TRANS_DB_IS_DELETE, 0);
            Calendar juliancal = Calendar.getInstance();
            Long timeinmillis = juliancal.getTimeInMillis();
            newrowvalues.put(
                    OMSDatabaseConstants.COMMON_COLUMN_NAME_MODIFIED_DATE,
                    Long.toString(timeinmillis));
            try {
                if (savedInsertedId == -1) {
                    savedInsertedId = tdbHelper
                            .insertTransData(
                                    multiFormScreenDataTableName,
                                    newrowvalues);
                    String usid = tdbHelper
                            .getUsidFromTransTable(
                                    multiFormScreenDataTableName,
                                    ""
                                            + savedInsertedId);


                    Log.d(TAG, "Save Sucess");
                }else if (savedInsertedId != OMSDefaultValues.NONE_DEF_CNT
                        .getValue()){

                    String usid = tdbHelper.getUsidFromTransTable(
                            multiFormScreenDataTableName, ""
                                    + savedInsertedId);
                    int updatedRows = tdbHelper.updateTransData(usid,
                            multiFormScreenDataTableName,
                            newrowvalues);
                    if(updatedRows < 1 )
                        savedInsertedId =  -1;


                    Log.d(TAG, "Updated " + savedInsertedId + "Rows");




                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
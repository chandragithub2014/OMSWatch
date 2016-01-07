package watch.oms.omswatch.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import watch.oms.omswatch.R;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.helpers.MultiFormScreenHelper;
import watch.oms.omswatch.widgets.WatchButton;
import watch.oms.omswatch.widgets.WatchEditText;
import watch.oms.omswatch.widgets.WatchTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MultiFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultiFormFragment extends Fragment {
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
    HashMap<String,String> formDataMap;

    private Hashtable<Integer, Hashtable<String, Object>> formGridItemsHash = null;
    private String multiFormScreenuniqueId = "";


    LinearLayout formParentLayout ;

    int widgetIds[] ={
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
        multiFormScreenHelper = new MultiFormScreenHelper(activityContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_multi_form, container, false);
        formParentLayout = (LinearLayout)view.findViewById(R.id.formParent);
        if (isFromLoadScreen) {
            try {
                formDataMap   = multiFormScreenHelper
                        .getMultiFormScreenData(navusid, configDBAppId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            formDataMap = multiFormScreenHelper
                    .getMultiFormScreenData(navusid, isPrepopulated,
                            configDBAppId);
        }

        if(formDataMap!=null && formDataMap.size()>0){
            multiFormScreenuniqueId = formDataMap.get(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID);
        }
//multiFormScreenuniqueId = multiFormScreenDataCursor .getString(multiFormScreenDataCursor.getColumnIndex(OMSDatabaseConstants.MULTI_FORM_SCREEN_UNIQUE_ID));
      if(multiFormScreenuniqueId!=null) {
          formGridItemsHash = multiFormScreenHelper
                  .getMultiFormScreenGridItems(multiFormScreenuniqueId,
                          configDBAppId);
      }
Log.d(TAG,"FormItems Hash Size:::"+formGridItemsHash.size());
        if (formGridItemsHash != null && formGridItemsHash.size() > 0) {
            createFormUserInterface(getActivity());
        }

        return view;
    }


    private void createFormUserInterface(Context context){
        Log.d(TAG, "In createFormUserInterface()");
           int rowCount = formGridItemsHash.size();
        Log.d(TAG, "rowCount::::"+rowCount);
        Hashtable<String, Object> tempHashTable = null;
        for(int i=0;i<rowCount;i++){
            tempHashTable = formGridItemsHash.get(i);
            if (tempHashTable != null) {
                Enumeration<String> keys = tempHashTable.keys();
                if (keys.hasMoreElements()) {
                    String widgetType = tempHashTable.get("columntype").toString();
                    String widgetName = "";
                    if (!TextUtils.isEmpty(tempHashTable.get("columnname").toString().trim())) {
                        widgetName = tempHashTable.get("columnname").toString().trim();
                    }

                    if (!(TextUtils.isEmpty(widgetType))
                            && (widgetType.length() > 0)) {
                        if (widgetType.equalsIgnoreCase("Label")) {
                            TextView label = WatchTextView.getInstance().fetchTextView(getActivity(),widgetName,widgetIds[i],null);
                            formParentLayout.addView(label,i);
                        }else if(widgetType.equalsIgnoreCase(OMSDatabaseConstants.MULTI_FORM_SCREEN_EDIT_TEXT_TYPE)){
                           EditText editText = WatchEditText.getInstance().fetchEditText(getActivity(),widgetName,widgetIds[i],null);
                           formParentLayout.addView(editText,i);
                        }
                        else if(widgetType.equalsIgnoreCase(OMSDatabaseConstants.MULTI_FORM_SCREEN_BUTTON_TYPE)){
                            Button watchButton = WatchButton.getInstance().fetchButton(getActivity(), widgetName, widgetIds[i], null);
                            formParentLayout.addView(watchButton,i);
                        }

                    }
                }
            }
        }
    }


}

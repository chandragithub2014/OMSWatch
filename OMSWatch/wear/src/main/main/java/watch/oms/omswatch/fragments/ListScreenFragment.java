package watch.oms.omswatch.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import watch.oms.omswatch.OMSDTO.ListScreenItemsDTO;
import watch.oms.omswatch.R;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.helpers.ListHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListScreenFragment#} factory method to
 * create an instance of this fragment.
 */
public class ListScreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private final String TAG = this.getClass().getSimpleName();
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int customContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
    private String navUsid;
    private int screenOrder = OMSDefaultValues.NONE_DEF_CNT.getValue();

    private int mContainerId = OMSDefaultValues.NONE_DEF_CNT.getValue();
    HashMap<String,String> listScreenMap = new HashMap<String,String>();
    private int configDBAppId;
    private ListHelper listDBHelper = null;
    List<ListScreenItemsDTO> listItems;

    public ListScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ListScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
      public static ListScreenFragment newInstance() {
        ListScreenFragment fragment = new ListScreenFragment();
        //Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          /*  mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
            navUsid = getArguments().getString(OMSMessages.UNIQUE_ID.getValue());
            screenOrder = getArguments().getInt(OMSMessages.SCREEN_ORDER.getValue());
            customContainerId = getArguments().getInt(OMSMessages.CUSTOM_CONTAINERID
                    .getValue());
            configDBAppId = getArguments().getInt(OMSMessages.CONFIGAPP_ID
                    .getValue());


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listDBHelper = new ListHelper();
        if (container != null) {
            mContainerId = container.getId();
        }
        if (customContainerId != OMSDefaultValues.NONE_DEF_CNT.getValue()) {
            mContainerId = customContainerId;
        }

        OMSApplication.getInstance().setAppId(Integer.toString(configDBAppId));

        try{
            listScreenMap = listDBHelper.getListScreenDetailsMap(navUsid,
                    configDBAppId);
        }
        catch(Exception e){
            Log.d(TAG, "Exception while fetching List details::" + e.getMessage());
        }

        if(listScreenMap!=null && listScreenMap.size()>0){
            listItems = listDBHelper.getListScreenItemsList(listScreenMap.get(OMSDatabaseConstants.LIST_SCREEN_UNIQUE_ID),configDBAppId);
            if(listItems!= null && listItems.size()>0){
                for(int i=0;i<listItems.size();i++){
                    Log.d(TAG,"ChildNavUsid::"+listItems.get(i).getChildNavUsid());
                    Log.d(TAG,"ImageURL::"+listItems.get(i).getImageURL());
                    Log.d(TAG,"PrimaryText::"+listItems.get(i).getPrimaryText());
                    Log.d(TAG,"SecondaryText::"+listItems.get(i).getSecondaryText());
                    Log.d(TAG,"Position::"+listItems.get(i).getPosition());
                }
            }
        }


        if(listScreenMap!=null && listScreenMap.size()>0){
            HashMap<Integer,String> listScreenChilds = listDBHelper.getListScreenChilds(listScreenMap.get(OMSDatabaseConstants.LIST_SCREEN_UNIQUE_ID),configDBAppId);


        }
        return inflater.inflate(R.layout.fragment_list_screen, container, false);
    }

}

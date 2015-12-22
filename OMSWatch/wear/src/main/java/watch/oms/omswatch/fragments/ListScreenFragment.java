    package watch.oms.omswatch.fragments;


    import android.app.Activity;
    import android.os.Bundle;
    import android.app.Fragment;
    import android.support.wearable.view.WearableListView;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import java.util.HashMap;
    import java.util.List;

    import watch.oms.omswatch.OMSDTO.ListScreenItemsDTO;
    import watch.oms.omswatch.OMSDTO.NavigationItems;
    import watch.oms.omswatch.OMSLoadScreenHelper;
    import watch.oms.omswatch.R;
    import watch.oms.omswatch.adapters.OMSListAdapter;
    import watch.oms.omswatch.adapters.WearableListItemLayout;
    import watch.oms.omswatch.application.OMSApplication;
    import watch.oms.omswatch.constants.OMSConstants;
    import watch.oms.omswatch.constants.OMSDatabaseConstants;
    import watch.oms.omswatch.constants.OMSDefaultValues;
    import watch.oms.omswatch.constants.OMSMessages;
    import watch.oms.omswatch.helpers.ListHelper;
    import watch.oms.omswatch.helpers.NavigationHelper;

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
        HashMap<String, String> listScreenMap = new HashMap<String, String>();
        private int configDBAppId;
        private ListHelper listDBHelper = null;
        private NavigationHelper navigationHelper = null;

        View listView = null;
        WearableListView wearableListView;

        HashMap<Integer, String> listChilds;
        int clickedListPosition = -1;
        private Activity activityContext = null;
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
            activityContext = getActivity();
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
            Log.d(TAG, "OnCreateView");
            initializeHelpers();

            if (container != null) {
                mContainerId = container.getId();
            }
            if (customContainerId != OMSDefaultValues.NONE_DEF_CNT.getValue()) {
                mContainerId = customContainerId;
            }

            OMSApplication.getInstance().setAppId(Integer.toString(configDBAppId));
            listView = inflater.inflate(R.layout.fragment_list_screen, container, false);

            try {
                listScreenMap = listDBHelper.getListScreenDetailsMap(navUsid,
                        configDBAppId);
            } catch (Exception e) {
                Log.d(TAG, "Exception while fetching List details::" + e.getMessage());
            }
            Log.d(TAG, "List Type:::" + listScreenMap.get(OMSDatabaseConstants.LIST_TYPE));
            wearableListView =
                    (WearableListView) listView.findViewById(R.id.watch_List);
            wearableListView.setClickListener(mClickListener);

            if (listScreenMap.get(OMSDatabaseConstants.LIST_TYPE).equalsIgnoreCase(OMSConstants.LIST_HETEROGENEOUS_TYPE)) {
                listChilds = fetchChildsForList();
                prepareDataForHeterogeneousList();

            } else if (listScreenMap.get(OMSDatabaseConstants.LIST_TYPE).equalsIgnoreCase(OMSConstants.LIST_HOMOGENEOUS_TYPE)) {

                prepareDataforHomogeneousList();
            }

            WearableListView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
            wearableListView.addItemDecoration(itemDecoration);
            return listView;
        }


        private void initializeHelpers() {
            listDBHelper = new ListHelper();
            navigationHelper = new NavigationHelper();
        }





        private void prepareDataForHeterogeneousList(){
            List<ListScreenItemsDTO>  heterogeneousData = fetchListItemsFromDB();
            if(heterogeneousData!=null && heterogeneousData.size()>0) {
                setAdapter(heterogeneousData);
            }
        }
       private  HashMap<Integer,String>  fetchChildsForList(){
        HashMap<Integer,String> listChilds = null;
        try {
            if (listScreenMap != null && listScreenMap.size() > 0) {
                listChilds = listDBHelper.getListScreenChildMap(listScreenMap.get(OMSDatabaseConstants.LIST_SCREEN_UNIQUE_ID), configDBAppId);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  listChilds;
    }

        private void prepareDataforHomogeneousList(){

        }

        private void setAdapter(List<ListScreenItemsDTO> heterogeneousData){
            wearableListView.setAdapter(new OMSListAdapter(getActivity(), heterogeneousData));
        }

        private  List<ListScreenItemsDTO>  fetchListItemsFromDB(){
            List<ListScreenItemsDTO> listItems = null;
            if(listScreenMap!=null && listScreenMap.size()>0){
             listItems  = listDBHelper.getListScreenItemsList(listScreenMap.get(OMSDatabaseConstants.LIST_SCREEN_UNIQUE_ID),configDBAppId);
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
            return listItems;
        }



        // Handle our Wearable List's click events
        private WearableListView.ClickListener mClickListener =
                new WearableListView.ClickListener() {
                    @Override
                    public void onClick(WearableListView.ViewHolder viewHolder) {
                       /* Toast.makeText(getActivity(),
                                String.format("You selected item #%s",
                                        viewHolder.getLayoutPosition()+1),
                                Toast.LENGTH_SHORT).show();*/
                        WearableListItemLayout listViewRowView = (WearableListItemLayout) viewHolder.itemView;
                        ListScreenItemsDTO temp = (ListScreenItemsDTO) listViewRowView.getTag();
                        Log.d(TAG,"Position of selected List:::"+temp.getPosition());
                        clickedListPosition = temp.getPosition();
                        lauchChildForList(clickedListPosition);

                    }

                    @Override
                    public void onTopEmptyRegionClick() {
                       /* Toast.makeText(getActivity(),
                                "Top empty area tapped", Toast.LENGTH_SHORT).show();*/
                    }
                };


        private void lauchChildForList(int position){
            if(listChilds!=null && listChilds.size()>0){
                String childNavUsid = listChilds.get(position);
                NavigationItems navigationItems =  navigationHelper.getNavigationDetailsByUsId(configDBAppId,childNavUsid);

                Log.d("TAG","Child SCREEN screentype:::"+navigationItems.screentype);
                Log.d("TAG","Child SCREEN parent_id:::"+ navigationItems.parent_id);
                Log.d("TAG","Child SCREEN uniqueId:::"+ navigationItems.uniqueId);
                Log.d("TAG","Child SCREEN screenorder:::"+  navigationItems.screenorder);
                Log.d("TAG","Child SCREEN appId:::"+  navigationItems.appId);

               /* if(navigationItems!=null){
                    new OMSLoadScreenHelper(activityContext, mContainerId)
                            .loadTargetScreen(navigationItems.screentype,
                                    navigationItems.parent_id,
                                    navigationItems.uniqueId,
                                    navigationItems.screenorder, false,
                                    null, null, "",
                                    OMSDefaultValues.NONE_DEF_CNT.getValue(),
                                    navigationItems.appId);
                }*/
            }
        }

    }

package watch.oms.omswatch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import watch.oms.omswatch.OMSDTO.NavigationItems;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.WebServiceHelpers.MedDataPostAsyncTaskHelper;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.helpers.NavigationHelper;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

public class MainActivity extends AppCompatActivity implements OMSReceiveListener{

    private final String TAG = this.getClass().getSimpleName();

    private boolean isMain = true;
    private ArrayList<NavigationItems> loadScreenData = new ArrayList<NavigationItems>();
    private Context context = null;
    private NavigationHelper navigationHelper = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = ( android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setTitle("OMS Watch");
        setSupportActionBar(toolbar);

        LayoutInflater mInflater= LayoutInflater.from(getApplicationContext());
        View mCustomView = mInflater.inflate(R.layout.toolbar_custom_view, null);
        toolbar.addView(mCustomView);

      //  new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this, "").execute("0");
        context = MainActivity.this;
        OMSDBManager manager = new OMSDBManager(MainActivity.this,
                MainActivity.this);
        try {

            OMSApplication.getInstance().setActivity(MainActivity.this);
            OMSApplication.getInstance().setAppId(OMSConstants.APP_ID);
            OMSApplication.getInstance().setAppSchema(OMSConstants.SCHEMA_NAME);
            OMSApplication.getInstance().setServerUrl(OMSConstants.SERVER_NAME);
            if(OMSConstants.USE_GENERIC_URL){
                OMSApplication.getInstance().setConfigURL(OMSApplication.getInstance().getServerURL()+OMSConstants.GENERIC_GET);
            }else{
                OMSApplication.getInstance().setConfigURL(
                        OMSApplication.getInstance().getServerURL()
                                + OMSConstants.CONFIG_GET
                                + OMSApplication.getInstance().getAppId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        manager.load();

    }

    @Override
    public void receiveResult(String result) {
        Log.d(TAG,"receiveResult MainActivity");
        if (result.contains(OMSMessages.TRANS_DATABASE_SUCCESS.getValue())) {
				/*if(OMSConstants.USE_FOOTER_LOAD_TEXT){
				loadingText.setText(R.string.app_load_policyDB);
				}
				if(OMSApplication.getInstance().isProgressDialogVisible()) {
				//pd.setTitle(R.string.app_logo_bottom);
				if(pd!=null) pd.setTitle(Html.fromHtml("<b><H1>"+this.getResources().getString(R.string.app_logo_bottom)+"</H1></b>"));
				}*/
            Log.d(TAG,"OMSDBManager.checkNetworkConnectivity()"+OMSDBManager.checkNetworkConnectivity());
            if (OMSDBManager.checkNetworkConnectivity()) {
                loadIntialScreen();
                // AppMonitor START
                Log.d(TAG, "******************startPerformanceMonitoring");

            }else{
                loadIntialScreen();
            }
        }
    }


    private void loadIntialScreen() {

        {
           /* if (OMSDBManager.checkNetworkConnectivity()) {
                if (OMSApplication.getInstance().isProgressDialogVisible()) {
                    if (pd != null)
                        pd.dismiss();
                    OMSApplication.getInstance().setProgressDialogVisible(false);
                }
            }*/
            navigationHelper = new NavigationHelper();

            int launchScreenOrder = OMSConstants.LAUNCH_SCREEN_ORDER_CONSTANT;
            if(	OMSApplication.getInstance().getLogOutScreenOrder() != -1){

            //    launchScreenOrder = OMSApplication.getInstance().getLogOutScreenOrder();
            }
            //	}else{
            loadScreenData = navigationHelper
                    .getScreenInfoByParentId(launchScreenOrder,
                            OMSApplication.getInstance()
                                    .getAppId() );
            if (loadScreenData.size() > 0) {
                String filterCoulmnName = null;
                String filterColumnVal = null;
                for (int i = 0; i < loadScreenData.size(); i++) {
                    Log.d(TAG, "Launching Main Activity with ParentScreenOrder:"+launchScreenOrder);
                    new OMSLoadScreenHelper((Activity) context, R.id.watchparentLayout)
                            .loadTargetScreen(loadScreenData.get(i).screentype,
                                    loadScreenData.get(i).parent_id,
                                    loadScreenData.get(i).uniqueId,
                                    loadScreenData.get(i).screenorder, isMain,
                                    filterCoulmnName, filterColumnVal, "", -1,
                                    loadScreenData.get(i).appId,false);
                }
            }
        }
    }
}

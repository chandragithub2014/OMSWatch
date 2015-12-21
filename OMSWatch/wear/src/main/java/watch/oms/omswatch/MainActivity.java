package watch.oms.omswatch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import watch.oms.omswatch.OMSDTO.NavigationItems;
import watch.oms.omswatch.WatchDB.OMSDBManager;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSConstants;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.helpers.NavigationHelper;
import watch.oms.omswatch.interfaces.OMSReceiveListener;

public class MainActivity extends Activity implements OMSReceiveListener{

    private final String TAG = this.getClass().getSimpleName();

    private boolean isMain = true;
    private ArrayList<NavigationItems> loadScreenData = new ArrayList<NavigationItems>();
    private Context context = null;
    private NavigationHelper navigationHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OMSDBManager manager = new OMSDBManager(MainActivity.this,
                MainActivity.this);
        manager.load();

    }

    @Override
    public void receiveResult(String result) {
        if (result.contains(OMSMessages.TRANS_DATABASE_SUCCESS.getValue())) {
				/*if(OMSConstants.USE_FOOTER_LOAD_TEXT){
				loadingText.setText(R.string.app_load_policyDB);
				}
				if(OMSApplication.getInstance().isProgressDialogVisible()) {
				//pd.setTitle(R.string.app_logo_bottom);
				if(pd!=null) pd.setTitle(Html.fromHtml("<b><H1>"+this.getResources().getString(R.string.app_logo_bottom)+"</H1></b>"));
				}*/
            if (OMSDBManager.checkNetworkConnectivity()) {
                loadIntialScreen();
                // AppMonitor START
                Log.d(TAG, "******************startPerformanceMonitoring");

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
                                    loadScreenData.get(i).appId);
                }
            }
        }
    }
}

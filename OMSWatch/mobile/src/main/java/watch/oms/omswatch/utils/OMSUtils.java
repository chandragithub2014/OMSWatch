package watch.oms.omswatch.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by 245742 on 2/8/2016.
 */
public class OMSUtils {

    Context ctx;
    Activity act;

    private static OMSUtils instance;
    private  ConnectivityManager connectionManager;

    private OMSUtils(){

    }

    public static OMSUtils getInstance(){
        if(instance == null){
            instance = new OMSUtils();
        }
        return instance;
    }

    // Verify Network Availability.
    public  boolean checkNetworkConnectivity(Context ctx) {
        Log.d("TAG", "checkNetworkConnectivity() OMSDBMANAGER");
        connectionManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectionManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}

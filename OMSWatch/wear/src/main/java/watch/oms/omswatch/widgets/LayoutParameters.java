package watch.oms.omswatch.widgets;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by CHANDRASAIMOHAN on 1/8/2016.
 */
public class LayoutParameters {

    private static LayoutParameters instance;
    Context ctx;

    private LayoutParameters(){

    }

    public static LayoutParameters getInstance(){
        if(instance == null){
            instance = new LayoutParameters();
        }
        return instance;
    }

    LinearLayout.LayoutParams fetchParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(20,-5,10,-5);
        return params;
    }
}

package watch.oms.omswatch.widgets;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by 245742 on 1/11/2016.
 */
public class WatchSpinner {
    private static WatchSpinner instance;
    Context ctx;

    private WatchSpinner(){

    }

    public static WatchSpinner getInstance(){
        if(instance == null){
            instance = new WatchSpinner();
        }
        return instance;
    }


    public Spinner fetchSpinnerView(Context ctx,String lableName,int id,HashMap<String,String> props){
        Spinner watchSpinner = null;
        try{
            watchSpinner = new Spinner(ctx);
            watchSpinner.setId(id);
           /// watchSpinner.setText(lableName);
            LinearLayout.LayoutParams params =   LayoutParameters.getInstance().fetchParams();
            watchSpinner.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return watchSpinner;
    }

    LinearLayout.LayoutParams fetchParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(20,5,10,5);
        return params;
    }
}

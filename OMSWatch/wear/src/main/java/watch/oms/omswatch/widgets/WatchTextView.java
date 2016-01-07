package watch.oms.omswatch.widgets;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by 245742 on 1/5/2016.
 */
public class WatchTextView {


    private static WatchTextView instance;
    Context ctx;

    private WatchTextView(){

    }

    public static WatchTextView getInstance(){
        if(instance == null){
            instance = new WatchTextView();
        }
        return instance;
    }


     public  TextView fetchTextView(Context ctx,String lableName,int id,HashMap<String,String> props){
        TextView label = null;
        try{
              label = new TextView(ctx);
              label.setId(id);
              label.setText(lableName);
              LinearLayout.LayoutParams params =   LayoutParameters.getInstance().fetchParams();
              label.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }
       return label;
    }

    LinearLayout.LayoutParams fetchParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(20,5,10,5);
        return params;
    }
}

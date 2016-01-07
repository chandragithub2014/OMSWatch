package watch.oms.omswatch.widgets;

import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;

/**
 * Created by 245742 on 1/7/2016.
 */
public class WatchButton {
    private static WatchButton instance;
    Context ctx;

    private WatchButton(){

    }

    public static WatchButton getInstance(){
        if(instance == null){
            instance = new WatchButton();
        }
        return instance;
    }

    public Button fetchButton(Context ctx,String lableName,int id,HashMap<String,String> props){
        Button watchButton = null;
        try{
            watchButton = new Button(ctx);
            watchButton.setId(id);
            watchButton.setHint(lableName);
            watchButton.getBackground().setColorFilter(0xFFBBAA00, PorterDuff.Mode.MULTIPLY);

            //Set the color of the text displayed inside the button
            watchButton.setTextColor(0xFF0000FF);
            watchButton.setTextSize(10);
            watchButton.setMinHeight(0);

            //Render this Button again
            watchButton.invalidate();
            LinearLayout.LayoutParams params =   LayoutParameters.getInstance().fetchParams();
            watchButton.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return watchButton;
    }

    LinearLayout.LayoutParams fetchParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(20,5,10,5);
        return params;
    }
}

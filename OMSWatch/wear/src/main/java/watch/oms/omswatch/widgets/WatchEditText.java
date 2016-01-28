package watch.oms.omswatch.widgets;

import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by 245742 on 1/7/2016.
 */
public class WatchEditText {

    private static WatchEditText instance;
    Context ctx;

    private WatchEditText(){

    }

    public static WatchEditText getInstance(){
        if(instance == null){
            instance = new WatchEditText();
        }
        return instance;
    }

    public EditText fetchEditText(Context ctx,String lableName,int id,HashMap<String,String> props){
        EditText editText = null;
        try{
            editText = new EditText(ctx);
            editText.setId(id);
            editText.setHint(lableName);
            editText.setHintTextColor(Color.GRAY);
          //  editText.setTextSize(10);
            LinearLayout.LayoutParams params =   LayoutParameters.getInstance().fetchParams();
            editText.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return editText;
    }

    LinearLayout.LayoutParams fetchParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(20,5,10,5);
        return params;
    }
}

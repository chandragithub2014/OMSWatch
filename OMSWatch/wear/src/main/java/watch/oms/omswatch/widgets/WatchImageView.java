package watch.oms.omswatch.widgets;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.HashMap;

/**
 * Created by 245742 on 2/16/2016.
 */
public class WatchImageView {
    private static WatchImageView instance;
    Context ctx;

    private WatchImageView(){

    }

    public static WatchImageView getInstance(){
        if(instance == null){
            instance = new WatchImageView();
        }
        return instance;
    }


    public ImageView fetchImageView(Context ctx,String lableName,int id,HashMap<String,String> props){
        ImageView watchImage = null;
        try{
            watchImage = new ImageView(ctx);
            watchImage.setId(id);
            /// watchSpinner.setText(lableName);
            LinearLayout.LayoutParams params = LayoutParameters.getInstance().fetchParams();
            params.gravity = Gravity.CENTER_HORIZONTAL;
            watchImage.setLayoutParams(params);

        }catch (Exception e){
            e.printStackTrace();
        }
        return watchImage;
    }



    LinearLayout.LayoutParams fetchParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(20,5,10,5);
        return params;
    }
}

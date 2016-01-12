package watch.oms.omswatch.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import watch.oms.omswatch.R;


/**
 * Created by 245742 on 12/3/2015.
 */
public class CustomToast {
    Context ctx;
    Activity act;

    private static CustomToast instance;


    private CustomToast(){

    }

    public static CustomToast getInstance(){
        if(instance == null){
            instance = new CustomToast();
        }
        return instance;
    }




   /* public CustomToast(Context ctx, Activity act){
        this.ctx = ctx;
        this.act = act;

    }
*/
    public void displayToast(String message,Context ctx, Activity act){
        this.ctx = ctx;
        this.act = act;

        //Creating the LayoutInflater instance
        LayoutInflater li = act.getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.toast_layout,
                (ViewGroup)act.findViewById(R.id.custom_toast_layout));
        TextView toastMessage   = (TextView)layout.findViewById(R.id.toast_label);
        toastMessage.setText(message);
        //Creating the Toast object
        Toast toast = new Toast(ctx);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();
    }
}

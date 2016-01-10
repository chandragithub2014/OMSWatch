package watch.oms.omswatch.interfaces;

/**
 * Created by CHANDRASAIMOHAN on 11/9/2015.
 */
public interface SendDataDialogListener {
    void onFinishDialog(String inputText, String type,int widgetId);
    void onFinishTimeDialog(String inputText, String type,int widgetId);
}
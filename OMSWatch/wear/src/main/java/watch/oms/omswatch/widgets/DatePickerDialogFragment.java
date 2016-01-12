package watch.oms.omswatch.widgets;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import watch.oms.omswatch.R;
import watch.oms.omswatch.interfaces.SendDataDialogListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatePickerDialogFragment#} factory method to
 * create an instance of this fragment.
 */
public class DatePickerDialogFragment extends DialogFragment
       /* implements DatePickerDialog.OnDateSetListener */{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View mRootView;
    String receivedDate;

    NumberPicker mYearPicker,mDayPicker, mMonthPicker;
    Button mCancelButton,mSetButton;
    SimpleDateFormat formatter;
    TextView numPickerTitle;
    int  numPickerYear = 1;
    int  numPickerDay = 1;
    int numPickerMonth = 1;
    TextView set_btn,cancel_btn;
    HashMap<Integer,String> monthlyHash;
    String dateType="label";
    int widgetID  = -1;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            Bundle mArgs = getArguments();
            receivedDate = mArgs.getString("date");
            dateType =  mArgs.getString("type");
            widgetID = mArgs.getInt("widgetId");
            if (!TextUtils.isEmpty(receivedDate) && !receivedDate.equalsIgnoreCase("value")) {
                parseDate(receivedDate);
            }
        }
        populateMonthlyHash();
    }

    private void parseDate(String dateReceived){
        formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

       try{
           Date date =  formatter.parse(dateReceived);
           String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
           String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
           String day = (String) android.text.format.DateFormat.format("dd", date); //20
             numPickerYear =  (Integer.parseInt(year))%100;
             numPickerDay = Integer.parseInt(day);
            numPickerMonth  = Integer.parseInt(intMonth);
           Log.d("Tag", "Picker Year::" + numPickerYear);
           Log.d("Tag", "Picker numPickerDay::" + numPickerDay);
           Log.d("Tag", "Picker numPickerMonth::" + numPickerMonth);

           /*mYearPicker.setValue(numPickerYear);
           mMonthPicker.setValue(numPickerMonth);
           mDayPicker.setValue(numPickerDay);*/

       }catch (ParseException e){
           e.printStackTrace();
       }
    }
    public DatePickerDialogFragment() {
        // Required empty public constructor
    }
private void populateMonthlyHash(){
    monthlyHash = new HashMap<Integer,String>();
    monthlyHash.put(1,"Jan");
    monthlyHash.put(2,"Feb");
    monthlyHash.put(3,"Mar");
    monthlyHash.put(4,"Apr");
    monthlyHash.put(5,"May");
    monthlyHash.put(6,"Jun");
    monthlyHash.put(7,"Jul");
    monthlyHash.put(8,"Aug");
    monthlyHash.put(9,"Sep");
    monthlyHash.put(10,"Oct");
    monthlyHash.put(11,"Nov");
    monthlyHash.put(12,"Dec");

}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_custom_date_picker, container, false);

        mYearPicker = (NumberPicker) mRootView.findViewById(R.id.year);
        mYearPicker.setMaxValue(99);
        mYearPicker.setMinValue(0);

        mYearPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        mDayPicker = (NumberPicker) mRootView.findViewById(R.id.day);
        mDayPicker.setMaxValue(31);
        mDayPicker.setMinValue(01);
        mDayPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });


        mMonthPicker = (NumberPicker) mRootView.findViewById(R.id.month);
        mMonthPicker.setMaxValue(12);
        mMonthPicker.setMinValue(01);

        mMonthPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        getDialog().setTitle("Select Date");
        numPickerTitle = (TextView)mRootView.findViewById(R.id.numpicker_title);
        if(!TextUtils.isEmpty(receivedDate)) {
            numPickerTitle.setText(receivedDate);
            mYearPicker.setValue(numPickerYear);
            mMonthPicker.setValue(numPickerMonth);
            mDayPicker.setValue(numPickerDay);
        }

        mYearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numPickerYear = newVal;
            }
        });

        mMonthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numPickerMonth = newVal;
            }
        });

        mDayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numPickerDay = newVal;
            }
        });
        set_btn = (TextView)mRootView.findViewById(R.id.set_dialog);
        cancel_btn = (TextView)mRootView.findViewById(R.id.cancel_dialog);
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numPickerYear>=0 &&numPickerYear<=37 ){
                    numPickerYear = 2000+numPickerYear;
                }else if(numPickerYear>=38 && numPickerYear<=99){
                    numPickerYear = 1900+numPickerYear;
                }
                Log.d("TAG", "Final Date::" + monthlyHash.get(numPickerMonth) + " " + numPickerDay + "," + numPickerYear);
              /*  SendDataDialogListener activity = (SendDataDialogListener) getActivity();
                activity.onFinishDialog("monthlyHash.get(numPickerMonth)+\" \"+numPickerDay+\",\"+numPickerYear");*/
                SendDataDialogListener mHost = (SendDataDialogListener)getTargetFragment();
              //  String parsedDate = monthlyHash.get(numPickerMonth)+" "+numPickerDay+","+" "+numPickerYear;
                String parsedDate = numPickerDay+" "+monthlyHash.get(numPickerMonth)+","+" "+numPickerYear;
                mHost.onFinishDialog(parsedDate,dateType,widgetID);
                dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return  mRootView;
    }

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
       DatePickerDialog dateDialog =  new DatePickerDialog(getActivity(), this, year, month, day);
        dateDialog.getDatePicker().setCalendarViewShown(false);
       // dateDialog.getDatePicker().setSpinnersShown(true);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }*/

}

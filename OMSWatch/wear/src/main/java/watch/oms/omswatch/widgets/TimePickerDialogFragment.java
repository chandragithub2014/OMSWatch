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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import watch.oms.omswatch.R;
import watch.oms.omswatch.interfaces.SendDataDialogListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimePickerDialogFragment#} factory method to
 * create an instance of this fragment.
 */
public class TimePickerDialogFragment extends DialogFragment
       /* implements DatePickerDialog.OnDateSetListener */{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View mRootView;
    String receivedTime;

    NumberPicker mHourPicker,mMinutePicker;
    Button mCancelButton,mSetButton;
    SimpleDateFormat formatter;
    TextView numPickerTitle;
    int  numPickerHour = 1;
    int  numPickerMinute = 00;

    TextView set_btn,cancel_btn;
    HashMap<Integer,String> monthlyHash;
    String dateType="label";
    int widgetID  = -1;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            Bundle mArgs = getArguments();
            receivedTime = mArgs.getString("time");
            dateType =  mArgs.getString("type");
            widgetID = mArgs.getInt("widgetId");
            if (!TextUtils.isEmpty(receivedTime) && !receivedTime.equalsIgnoreCase("value")) {
                parseDate(receivedTime);
            }
        }
        populateMonthlyHash();
    }

    private void parseDate(String timeReceived){
        formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar c = Calendar.getInstance();

       try{
           Date date =  formatter.parse(timeReceived);
           c.setTime(date);
           numPickerHour = c.get(Calendar.HOUR_OF_DAY);
           numPickerMinute = c.get(Calendar.MINUTE);
          /* String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
           String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
           String day = (String) android.text.format.DateFormat.format("dd", date); //20
             numPickerYear =  (Integer.parseInt(year))%100;
             numPickerDay = Integer.parseInt(day);
            numPickerMonth  = Integer.parseInt(intMonth);*/
           Log.d("Tag", "Picker Hour::" + numPickerHour);
           Log.d("Tag", "Picker Minute::" + numPickerMinute);


           /*mYearPicker.setValue(numPickerYear);
           mMonthPicker.setValue(numPickerMonth);
           mDayPicker.setValue(numPickerDay);*/

       }catch (ParseException e){
           e.printStackTrace();
       }
    }
    public TimePickerDialogFragment() {
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
        mRootView = inflater.inflate(R.layout.fragment_custom_time_picker, container, false);

        mHourPicker = (NumberPicker) mRootView.findViewById(R.id.hour);
        mHourPicker.setMaxValue(23);
        mHourPicker.setMinValue(0);

        mHourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        mMinutePicker = (NumberPicker) mRootView.findViewById(R.id.minute);
        mMinutePicker.setMaxValue(59);
        mMinutePicker.setMinValue(00);
        mMinutePicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });



        getDialog().setTitle("Select Time");
        numPickerTitle = (TextView)mRootView.findViewById(R.id.numpicker_title);
        if(!TextUtils.isEmpty(receivedTime)) {
            numPickerTitle.setText(receivedTime);
            mHourPicker.setValue(numPickerHour);
            mMinutePicker.setValue(numPickerMinute);
        }

        mHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numPickerHour = newVal;
            }
        });



        mMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numPickerMinute = newVal;
            }
        });
        set_btn = (Button)mRootView.findViewById(R.id.set_dialog);
        cancel_btn = (Button)mRootView.findViewById(R.id.cancel_dialog);
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(numPickerYear>=0 &&numPickerYear<=37 ){
                    numPickerYear = 2000+numPickerYear;
                }else if(numPickerYear>=38 && numPickerYear<=99){
                    numPickerYear = 1900+numPickerYear;
                }
                Log.d("TAG", "Final Date::" + monthlyHash.get(numPickerMonth) + " " + numPickerDay + "," + numPickerYear);
              *//*  SendDataDialogListener activity = (SendDataDialogListener) getActivity();
                activity.onFinishDialog("monthlyHash.get(numPickerMonth)+\" \"+numPickerDay+\",\"+numPickerYear");*//*
                SendDataDialogListener mHost = (SendDataDialogListener)getTargetFragment();
                String parsedDate = monthlyHash.get(numPickerMonth)+" "+numPickerDay+","+" "+numPickerYear;*/
                SendDataDialogListener mHost = (SendDataDialogListener)getTargetFragment();
                String parsedTime = numPickerHour+":"+numPickerMinute;
                Log.d("TimePickerDialog","Time Picker:::"+parsedTime);
                mHost.onFinishTimeDialog(parsedTime,dateType,widgetID);
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

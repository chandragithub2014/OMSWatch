package watch.oms.omswatch.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 245742 on 1/12/2016.
 */
public class ReadableDateGenerator {

    Context ctx;
    Activity act;

    private static ReadableDateGenerator instance;


    private ReadableDateGenerator(){

    }

    public static ReadableDateGenerator getInstance(){
        if(instance == null){
            instance = new ReadableDateGenerator();
        }
        return instance;
    }

    public String getReadableDate(String originalDate,String inputFormat,String outputFormat){
        String readableDate="";
        SimpleDateFormat input = new SimpleDateFormat(inputFormat);
        SimpleDateFormat output = new SimpleDateFormat(outputFormat);
        try {
            Date oneWayTripDate = input.parse(originalDate); // parse input
            readableDate = (output.format(oneWayTripDate));
        } catch (ParseException e) {
            Log.e("ReadableDateGenerator",
                    "ParseException. Error is:" + e.getMessage() + ". Method[getReadableDate] paramaters are:originalDate["
                            + originalDate + "], inputFormat[" + inputFormat
                            + "],outputFormat[" + outputFormat + "] Error is:" + e.getMessage());
            e.printStackTrace();
        }
        return readableDate;
    }
}

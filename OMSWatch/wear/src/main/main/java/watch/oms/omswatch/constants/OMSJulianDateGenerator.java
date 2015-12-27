/**
 * 
 */
package watch.oms.omswatch.constants;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * @author 245742
 * Returns Current generated Julian date.
 */
public class OMSJulianDateGenerator {

	/**
	   *  Returns Current generated Julian date.
	   * @return Julian date
	   */
	         public static double getJulDate(){            
	                     Calendar cal = Calendar.getInstance();          
	                     int year = cal.get(Calendar.YEAR);
	               int month = cal.get(Calendar.MONTH)+1;
	               int day = cal.get(Calendar.DAY_OF_MONTH);
	               int hour = cal.get(Calendar.HOUR_OF_DAY);
	               int minute = cal.get(Calendar.MINUTE);
	               int second = cal.get(Calendar.SECOND);
	               double extra = (100.0 * year) + month - 190002.5;
	               double julianDay = (367.0 * year) -
	                (Math.floor(7.0 * (year + Math.floor((month + 9.0) / 12.0)) / 4.0)) + 
	                 Math.floor((275.0 * month) / 9.0) +  
	                 day + ((hour + ((minute + (second / 60.0)) / 60.0)) / 24.0) +
	                 1721013.5 - ((0.5 * extra) / Math.abs(extra)) + 0.5;
	               
	               DecimalFormat sixDigitFormat = new DecimalFormat("#.######");
	               return Double.valueOf(sixDigitFormat.format(julianDay));           
	              }
	        
	        public static String getCurrentTimeUsid()
	     	{
	     		    Random randomGenerator = new Random();
	     		    int randomInt = randomGenerator.nextInt(50);
	     			Calendar juliancal = Calendar.getInstance();
	     			long  timeinmillis = (juliancal.getTimeInMillis())+randomInt;
	     			String usid = Long.toString(timeinmillis);
	     			return usid;
	     	}
	        
	        /**
	    	 * Returns Date from Julian Date.
	    	 * 
	    	 * @param Julian
	    	 *            Date
	    	 * @return String date
	    	 */
	    	public static String getDateFromJulian(double injulian) {
	    		int JGREG = 15 + 31 * (10 + 12 * 1582);
	    		int jalpha, ja, jb, jc, jd, je, year, month, day;
	    		double julian = injulian + .5 / 86400.0;
	    		ja = (int) julian;
	    		if (ja >= JGREG) {

	    			jalpha = (int) (((ja - 1867216) - 0.25) / 36524.25);
	    			ja = ja + 1 + jalpha - jalpha / 4;
	    		}
	    		jb = ja + 1524;
	    		jc = (int) (6680.0 + ((jb - 2439870) - 122.1) / 365.25);
	    		jd = 365 * jc + jc / 4;
	    		je = (int) ((jb - jd) / 30.6001);
	    		day = jb - jd - (int) (30.6001 * je);
	    		month = je - 1;
	    		if (month > 12)
	    			month = month - 12;
	    		year = jc - 4715;
	    		if (month > 2)
	    			year--;
	    		if (year <= 0)
	    			year--;
	    		StringBuffer date = new StringBuffer();
	    		date.append(year);
	    		date.append(OMSConstants.HYPHEN);
	    		date.append(month);
	    		date.append(OMSConstants.HYPHEN);
	    		date.append(day);
	    		return date.toString();
	    	} 
	        

}

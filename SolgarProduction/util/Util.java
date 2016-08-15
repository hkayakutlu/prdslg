package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {

	public static String getCurrentDateTime() {  	   
		String currDate = "";		
			try {			

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				currDate = dateFormat.format(cal.getTime()); 
				
			} catch (Exception e) {
	           e.printStackTrace();
	       }			
			return currDate;
	  }	
	
	
}

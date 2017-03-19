package testappstudyblue.juliakorolko.com.testappforstudyblue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by juliakorolko on 3/18/17.
 */

public class Utils {

    public static String getDate(String timestamp){

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = inputFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
        return outputFormat.format(date);
    }
}

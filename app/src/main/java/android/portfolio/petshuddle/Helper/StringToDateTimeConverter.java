package android.portfolio.petshuddle.Helper;

import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StringToDateTimeConverter {
    public static Calendar stringToCalendar(String givenDateTime) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date theDate = sdf.parse(givenDateTime.trim());
            cal.setTime(theDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

}

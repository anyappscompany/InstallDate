package ua.com.anyapps.installdate;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Functions {
    public static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(date);
    }
}

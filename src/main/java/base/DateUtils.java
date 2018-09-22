package base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String format(Date date,String fmt){
        SimpleDateFormat sdf=new SimpleDateFormat(fmt);
        return sdf.format(date);
    }
}

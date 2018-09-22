package base;

import java.util.Date;

public class LogUtils {

    public static void log(String log){
        System.out.println(String.format("%s %s",DateUtils.format(new Date(),"HH:mm:ss.SSS"),log));
    }
}

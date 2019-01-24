package jvm.hotswap;

import sun.security.action.GetPropertyAction;

import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by guzy on 16/12/29.
 */
public class Test {

    public void test() {
        System.out.println("ddsds");
    }

    public static void main(String[] args) {

        System.out.println(System.getProperties().toString());
        System.out.println(System.currentTimeMillis());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1548275836601l)));
//        new Test().test();
        System.out.println(getTimeZone());

        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        System.out.println(timeZone.getID());
        System.out.println(timeZone.getDisplayName());

        System.out.println(AccessController.doPrivileged(
                new GetPropertyAction("user.timezone")));

        System.out.println("after");
        System.setProperty("user.timezone","Asia/Shanghai");

        System.out.println(AccessController.doPrivileged(
                new GetPropertyAction("user.timezone")));

         cal = Calendar.getInstance();
         timeZone = cal.getTimeZone();
        System.out.println(timeZone.getID());
        System.out.println(timeZone.getDisplayName());
    }

    public static String getTimeZone() {
        Calendar cal = Calendar.getInstance();
        int offset = cal.get(Calendar.ZONE_OFFSET);
        cal.add(Calendar.MILLISECOND, -offset);
        Long timeStampUTC = cal.getTimeInMillis();
        Long timeStamp = System.currentTimeMillis();
        Long timeZone = (timeStamp - timeStampUTC) / (1000 * 3600);
        System.out.println(timeZone.intValue());
        return String.valueOf(timeZone);

    }
}

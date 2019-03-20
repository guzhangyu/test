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

//        System.out.println(System.getProperties().toString());
//        System.out.println(System.currentTimeMillis());
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1548275836601l)));
////        new Test().test();
//        System.out.println(getTimeZone());
//
//        Calendar cal = Calendar.getInstance();
//        TimeZone timeZone = cal.getTimeZone();
//        System.out.println(timeZone.getID());
//        System.out.println(timeZone.getDisplayName());
//
//        System.out.println(AccessController.doPrivileged(
//                new GetPropertyAction("user.timezone")));
//
//        System.out.println("after");
//        System.setProperty("user.timezone","Asia/Shanghai");
//
//        System.out.println(AccessController.doPrivileged(
//                new GetPropertyAction("user.timezone")));
//
//         cal = Calendar.getInstance();
//         timeZone = cal.getTimeZone();
//        System.out.println(timeZone.getID());
//        System.out.println(timeZone.getDisplayName());

        System.out.println("{\"data\":[{\"account_id\":\"1693422410734111\",\"account_name\":\"\\u871c\\u67da\\u7f51\\u7edc\\u79d1\\u6280\\uff08\\u4e0a\\u6d77\\uff09\\u6709\\u9650\\u516c\\u53f8-0408-1\",\"actions\":[{\"action_carousel_card_id\":\"298572484083650\",\"action_carousel_card_name\":\"1: \\ud83d\\ude41+\\ud83c\\udfb5=\\ud83d\\ude04\",\"action_type\":\"link_click\",\"value\":\"56\"},{\"action_carousel_card_id\":\"298572490750316\",\"action_carousel_card_name\":\"2: Let's create&share. \\ud83d\\ude04\",\"action_type\":\"link_click\",\"value\":\"12\"},{\"action_carousel_card_id\":\"298572504083648\",\"action_carousel_card_name\":\"3: Download to watch more \\ud83d\\udc49\",\"action_type\":\"link_click\",\"value\":\"16\"},{\"action_carousel_card_id\":\"298572514083647\",\"action_carousel_card_name\":\"Click to discover&share \\ud83d\\udc49\",\"action_type\":\"link_click\",\"value\":\"5\"}],\"adset_id\":\"23843023590060650\",\"clicks\":\"88\",\"impressions\":\"43042\",\"reach\":\"28208\",\"spend\":\"214.45\",\"date_start\":\"2018-10-29\",\"date_stop\":\"2018-10-29\",\"age\":\"13-17\",\"gender\":\"female\"},{\"account_id\":\"1693422410734111\",\"account_name\":\"\\u871c\\u67da\\u7f51\\u7edc\\u79d1\\u6280\\uff08\\u4e0a\\u6d77\\uff09\\u6709\\u9650\\u516c\\u53f8-0408-1\",\"actions\":[{\"action_carousel_card_id\":\"298572484083650\",\"action_carousel_card_name\":\"1: \\ud83d\\ude41+\\ud83c\\udfb5=\\ud83d\\ude04\",\"action_type\":\"link_click\",\"value\":\"60\"},{\"action_carousel_card_id\":\"298572490750316\",\"action_carousel_card_name\":\"2: Let's create&share. \\ud83d\\ude04\",\"action_type\":\"link_click\",\"value\":\"13\"},{\"action_carousel_card_id\":\"298572504083648\",\"action_carousel_card_name\":\"3: Download to watch more \\ud83d\\udc49\",\"action_type\":\"link_click\",\"value\":\"14\"},{\"action_carousel_card_id\":\"298572514083647\",\"action_carousel_card_name\":\"Click to discover&share \\ud83d\\udc49\",\"action_type\":\"link_click\",\"value\":\"7\"}],\"adset_id\":\"23843023590060650\",\"clicks\":\"95\",\"impressions\":\"36354\",\"reach\":\"23968\",\"spend\":\"219.59\",\"date_start\":\"2018-10-29\",\"date_stop\":\"2018-10-29\",\"age\":\"13-17\",\"gender\":\"male\"},{\"account_id\":\"1693422410734111\",\"account_name\":\"\\u871c\\u67da\\u7f51\\u7edc\\u79d1\\u6280\\uff08\\u4e0a\\u6d77\\uff09\\u6709\\u9650\\u516c\\u53f8-0408-1\",\"actions\":[{\"action_carousel_card_id\":\"298572484083650\",\"action_carousel_card_name\":\"1: \\ud83d\\ude41+\\ud83c\\udfb5=\\ud83d\\ude04\",\"action_type\":\"link_click\",\"value\":\"1\"}],\"adset_id\":\"23843023590060650\",\"clicks\":\"1\",\"impressions\":\"423\",\"reach\":\"400\",\"spend\":\"2.04\",\"date_start\":\"2018-10-29\",\"date_stop\":\"2018-10-29\",\"age\":\"13-17\",\"gender\":\"unknown\"}],\"paging\":{\"cursors\":{\"before\":\"MAZDZD\",\"after\":\"MgZDZD\"}}}");
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

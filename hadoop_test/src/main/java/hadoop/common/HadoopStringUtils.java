package hadoop.common;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public class HadoopStringUtils {

    public static String sortAndJoin(String...s){
        Arrays.sort(s);
        return StringUtils.join(s,",");
    }

    public static String join(String...s){
        return StringUtils.join(s,",");
    }
}

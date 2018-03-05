package interview;

import java.io.UnsupportedEncodingException;

public class CutStrByByte {

    public static String subStr(String pre,int len,String encode) throws UnsupportedEncodingException {
        String str=new String(pre.getBytes(encode),0,len);
        int newLen=str.length();
        if(pre.substring(0,newLen).getBytes(encode).length!=len || !pre.substring(0,newLen).equals(str)){
            return pre.substring(0,newLen-1);
        }
        return str;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(subStr("我",3,"utf-8"));
    }
}

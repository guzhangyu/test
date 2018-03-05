package interview;

import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.util.regex.Pattern;

public class DT1 {

    public DT1() {
    }

    static Pattern emoji=Pattern.compile("[\\\\ud800\\\\udc00-\\\\udbff\\\\udfff\\\\ud800-\\\\udfff]");

    public String sub2(String pre,int len){
        int i=0,j=0;
        while(i<len){
            int curLen=pre.substring(j++,j).length();
            if(i+curLen>len){
                break;
            }
            i+=curLen;
        }
        return new String(pre.getBytes(),0,i);
    }

    public String sub(String pre,int len){
        byte[] bytes=pre.getBytes();
        while(len>0 && (bytes[len-1]<0 || bytes[len-1]>127)){
            len--;
        }
        return new String(bytes,0,len);
    }

    public String sub1(String pre,int len){
        char[] cs=pre.toCharArray();

        int i=0;
        int j=0;
        while(i<len){
            int curLen=0;
            if(j<pre.length()-1 && emoji.matcher(pre.substring(j,j+2)).matches()){
                curLen=4;
                j+=2;
            }else{
                char c=cs[j++];
                //if(c >= 0x4E00 &&  c <= 0x9FA5){
                //if(c>0x80){
//                i+=2;
//            }else{
//                i+=1;
//            }
                curLen=Character.valueOf(c).toString().getBytes().length;
            }

            if(i+curLen>len){
                break;
            }
            i+=curLen;

        }
//        if(i==len+1){
//            i-=2;
//        }
        return new String(pre.getBytes(),0,i);
    }

    public static void main(String[] args) {

        System.out.println("😅".replaceAll("[\\\\ud800\\\\udc00-\\\\udbff\\\\udfff\\\\ud800-\\\\udfff]",""));
        System.out.println(emoji.matcher("😅").matches());

       // new DT().t();
        String pre=">[,,..#%d&&⸘‽₱😅我";
        System.out.println(pre.substring(4,6));
        System.out.println("⸘‽₱😅我".length());
        System.out.println(pre.length());
        System.out.println("😅".length());
        System.out.println("😅".getBytes().length);
        System.out.println(new String(pre.getBytes(),0,14));
        System.out.println(new DT1().sub1("😅",4));
        //System.out.println(new DT1().sub2(pre,24));
    }
}

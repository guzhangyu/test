package interview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ADT extends DT{

    public ADT(int a) {
        System.out.println(this.getClass());
        System.out.println(super.getClass());
    }

    static class A{
        public void t(){
            System.out.println(a);
        }
    }

    public static int reverse(int x) {
        int result=0;
        while(x!=0){
            if(result>Integer.MAX_VALUE/10 || result<-Integer.MAX_VALUE/10){
                return 0;
            }
            result=result*10+x%10;
            x/=10;
        }
        return result;
    }

    public boolean isPalindrome(int x) {
        char[]cs =(x+"").toCharArray();
        for(int i=0;i<cs.length/2;i++){
            if(cs[i]!=cs[cs.length-i]){
                return false;
            }
        }
        return true;
    }


    //2147483647
    public static int myAtoi(String str) {
        Matcher matcher= Pattern.compile("^\\s*([+-])?0*([0-9]+)").matcher(str);
        if(!matcher.find()){
            return 0;
        }

        String num=matcher.group(2);
        String preffix=matcher.group(1);
        if(preffix==null){
            preffix="";
        }

        if(num.length()<10 || (num.length()==10 && num.compareTo(Integer.MAX_VALUE+"")<=0) ){
            return Integer.parseInt(preffix+num);
        }
        return preffix.equals("-")?Integer.MIN_VALUE:Integer.MAX_VALUE;
    }

    public static void main(String[] args) {

        System.out.println(myAtoi("hh 4999999999944 fda"));
        //System.out.println(reverse(-2147483412));

//        int l=99999;
//        byte[][] arr=new byte[20][l];
//        for(int i=0;i<arr.length;i++){
//            arr[i]=new byte[l];
//        }
//        System.out.println(arr);
//        Object a;
//        class A{
//
//        }
//        System.out.println("fda");
//        new ADT(4).t();
//
//        new ADT(4){
//
//        }.t();
    }

    public void t(){
        new A().t();
    }

    private static int a;

    //abstract void t();


}

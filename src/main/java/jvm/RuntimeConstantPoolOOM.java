package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guzy on 16/10/8.
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
//        List<String> list=new ArrayList<String>();
//        int i=0;
//        while(true){
//            list.add(String.valueOf(i++).intern());
//        }

        new RuntimeConstantPoolOOM().test1();
    }

    public void test1(){
        String str1=new StringBuffer("计算机").append("软件").toString();
        System.out.println(str1.intern()==str1);

        String str2=new StringBuffer("ja").append("va").toString();
        System.out.println(str2.intern()==str2);
    }


}

package jvm.hotcompile;

/**
 * Created by guzy on 16/12/29.
 */
public class CompileTest {

    public static final int NUM=15000;

    public static int doubleValue(int k){
        for(int i=0;i<100000;i++);
        return k*2;
    }

    public static long calcSum(){
        long sum=0;
        for(int i=1;i<100;i++){
            sum+=doubleValue(i);
        }
        return sum;
    }

    public static void main(String[] args) {
//        for(int i=0;i<NUM;i++){
//            System.out.println(calcSum());;
//        }
        int x=3;
        int x1=4;
        int x2=5;
        int b=x+x1*x2;
        System.out.println("result:"+b);
    }
}

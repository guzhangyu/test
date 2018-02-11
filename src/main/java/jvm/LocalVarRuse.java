package jvm;

/**
 * Created by guzy on 16/12/28.
 */
public class LocalVarRuse {

    public static void main(String[] args) {
        {
            byte[] placeholder=new byte[64*1024];
        }
        int a;
        //System.out.println(a);
        System.gc();
    }


}

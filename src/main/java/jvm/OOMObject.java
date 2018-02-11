package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guzy on 16/10/20.
 */
public class OOMObject {

    static class OOMObject1{
        public byte[] placeholder = new byte[64*1024];
    }


    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject1> list=new ArrayList<OOMObject1>();
        for(int i=0;i<num;i++){
            Thread.sleep(50);
            list.add(new OOMObject1());
        }
        System.gc();
    }

    public static void main(String[] args) throws InterruptedException {
        fillHeap(1000);
    }

}


package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆溢出测试
 * Created by guzy on 16/10/8.
 */
public class HeapOOM {

    static class OOMObject{

    }

    public static void main(String[] args) {
        List<OOMObject> list=new ArrayList<OOMObject>();
        while (true){
            list.add(new OOMObject());
        }
    }
}

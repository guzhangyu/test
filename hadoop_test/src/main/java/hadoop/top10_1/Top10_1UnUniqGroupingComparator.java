package hadoop.top10_1;

import hadoop.common.Tuple;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class Top10_1UnUniqGroupingComparator extends WritableComparator {

    public Top10_1UnUniqGroupingComparator(){
        super(Tuple.class,true);
    }


    @Override
    public int compare(WritableComparable a, WritableComparable b) {
//        return super.compare(a, b);
        Tuple t1=(Tuple)a;
        Tuple t2=(Tuple)b;
        int c=t1.compareTo(t2);
        if(c!=0){
            return c;
        }
        return t1.getT1().compareTo(t2.getT1());
    }
}

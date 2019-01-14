package hadoop.cochran_armitage;

import hadoop.common.TextTriple;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CochranArmitage2_1GroupingComparator extends WritableComparator {

    public CochranArmitage2_1GroupingComparator(){
        super(TextTriple.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        TextTriple t1=(TextTriple)a;
        TextTriple t2=(TextTriple)b;
        return t1.getT2().toString().compareTo(t2.getT2().toString());
    }
}

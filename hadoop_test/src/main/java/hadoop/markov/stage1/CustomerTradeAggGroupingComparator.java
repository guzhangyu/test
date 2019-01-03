package hadoop.markov.stage1;

import hadoop.common.TextPair2;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CustomerTradeAggGroupingComparator extends WritableComparator{

    public CustomerTradeAggGroupingComparator() {
        super(TextPair2.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        TextPair2 t1=(TextPair2)a;
        TextPair2 t2=(TextPair2)b;

        return t1.getT1().toString().compareTo(t2.getT1().toString());
    }
}

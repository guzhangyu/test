package hadoop.top10.array;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class Top10ArrayGroupingComparator extends WritableComparator {

    public Top10ArrayGroupingComparator(){
        super(Text.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
//        return super.compare(a, b);
        return 0;
    }
}

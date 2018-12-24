package hadoop.top10.array;

import hadoop.top10.Tuple;
import org.apache.hadoop.io.ArrayWritable;

public class TupleArray extends ArrayWritable {
    public TupleArray() {
        super(Tuple.class);
    }
}

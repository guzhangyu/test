package hadoop.common;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.ArrayWritable;

public class TupleArray extends ArrayWritable {
    public TupleArray() {
        super(Tuple.class);
    }

    public TupleArray( Tuple[] values) {
        super(Tuple.class, values);
    }

    @Override
    public String toString() {
        return "["+StringUtils.join(super.toStrings(),",")+"]";
    }
}

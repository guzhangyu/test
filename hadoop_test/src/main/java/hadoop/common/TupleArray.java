package hadoop.common;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.ArrayWritable;

public class TupleArray extends ArrayWritable {
    public TupleArray() {
        super(Tuple.class);
    }

    @Override
    public String toString() {
        return "["+StringUtils.join(super.toStrings(),",")+"]";
    }
}

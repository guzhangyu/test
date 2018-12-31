package hadoop.common;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;

import java.util.Arrays;

public class UseableArrayWritable extends ArrayWritable {
    public UseableArrayWritable(Class<? extends Writable> valueClass, Writable[] values) {
        super(valueClass, values);
    }

    @Override
    public String toString() {
        return Arrays.asList(this.get()).toString();
    }
}

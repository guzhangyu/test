package hadoop.top10;

import hadoop.common.Tuple;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class Top10Partitioner extends Partitioner<Text, Tuple> {

    @Override
    public int getPartition(Text text, Tuple tuple, int i) {
        return i-1-(text.hashCode()%i);
    }
}

package hadoop.bloom;

import hadoop.common.TextPair;
import hadoop.common.Tuple;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class BloomReducer extends Reducer<TextPair, IntWritable, Text, Tuple> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(TextPair key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        for(IntWritable v:values){
            context.write(key.getT1(),new Tuple(key.getT2(),v));
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

package hadoop.cochran_armitage;

import hadoop.common.TextPair2;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CochranArmitage2Reducer extends Reducer<TextPair2, IntWritable,TextPair2,IntWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(TextPair2 key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        int s=0;
        for(IntWritable t:values){
            s+=t.get();
        }
        context.write(key,new IntWritable(s));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

package hadoop.markov.stage2;

import hadoop.common.TextPair2;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class StatusTransferReducer extends Reducer<TextPair2, IntWritable,TextPair2,IntWritable> {

    @Override
    protected void reduce(TextPair2 key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int s=0;
        for(IntWritable v:values){
            s+=v.get();
        }
        context.write(key,new IntWritable(s));
    }
}

package hadoop.allele_freq;

import hadoop.common.TextTriple;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AlleleFreqReducer extends Reducer<TextTriple, IntWritable,TextTriple,IntWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(TextTriple key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        int s=0;
        for(IntWritable a:values){
            s++;
        }
        context.write(key,new IntWritable(s));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

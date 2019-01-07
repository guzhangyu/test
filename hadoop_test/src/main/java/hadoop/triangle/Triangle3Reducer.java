package hadoop.triangle;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Triangle3Reducer extends Reducer<Text, IntWritable,Text,IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int s=0;
        for(IntWritable value:values) {
            s += value.get();
        }
        context.write(key,new IntWritable(s));
    }
}

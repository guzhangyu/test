package hadoop.naive_bayes_category;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class NaiveBayesCategoryReducer extends Reducer<Text,IntWritable,Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int s=0;
        for(IntWritable v:values){
            s+=v.get();
        }
        context.write(key,new IntWritable(s));
    }
}

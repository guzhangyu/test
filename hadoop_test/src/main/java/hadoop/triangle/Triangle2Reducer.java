package hadoop.triangle;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Triangle2Reducer extends Reducer<Text,Text,Text, IntWritable> {

    IntWritable ONE=new IntWritable(1);

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Set<String> strs=new HashSet<>();
        for(Text value:values){
            strs.add(value.toString());
        }

        if(!strs.contains("")){
            return;
        }

        strs.remove("");
        for(String s:strs){
            context.write(new Text(s),ONE);
        }
    }
}

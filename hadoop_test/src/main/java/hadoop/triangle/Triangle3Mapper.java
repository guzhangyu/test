package hadoop.triangle;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Triangle3Mapper extends Mapper<LongWritable,Text,Text,IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] vs=value.toString().split("\\t");
        if(vs.length<2){
            return;
        }
        context.write(new Text(vs[0]),new IntWritable(Integer.parseInt(vs[1])));
    }
}

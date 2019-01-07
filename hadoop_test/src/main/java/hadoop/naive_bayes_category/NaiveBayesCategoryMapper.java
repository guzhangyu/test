package hadoop.naive_bayes_category;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class NaiveBayesCategoryMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    final static IntWritable ONE=new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String v=value.toString();
        String[] vs=v.split("\\s+");
        String[] v1s=vs[0].split(",");

        int i=0;
        for(String v1:v1s){
            context.write(new Text((i++)+"-"+v1+"-"+vs[1]),ONE);
        }
        context.write(new Text(vs[1]),ONE);
    }
}

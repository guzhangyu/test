package hadoop.allele_freq;

import hadoop.common.TextTriple;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AlleleFreqMapper extends Mapper<LongWritable, Text, TextTriple,IntWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String v=value.toString();
        String[] vs=v.split(";");
        String[] vs1=vs[1].split(":");

        context.write(new TextTriple(vs[0],vs1[0],vs1[1]),new IntWritable(1));
        context.write(new TextTriple(vs[0],vs1[0],vs1[2]),new IntWritable(1));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

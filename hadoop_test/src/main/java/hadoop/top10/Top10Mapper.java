package hadoop.top10;

import hadoop.common.Tuple;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Top10Mapper extends Mapper<LongWritable, Text,Text, Tuple> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String [] values=value.toString().split(",");
        context.write(new Text(values[0]),new Tuple(values[0],Integer.parseInt(values[1])));
    }
}

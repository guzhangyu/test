package hadoop.markov.stage1;

import hadoop.common.TextPair2;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CustomerTradeAggMapper extends Mapper<LongWritable, Text, TextPair2, IntWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String v=value.toString();
        if(StringUtils.isBlank(v)){
            return;
        }
        String[] vs=v.split(",");
//        context.write(new Text(vs[1]),new Tuple1(vs[2],Integer.parseInt(vs[3])));
        context.write(new TextPair2(vs[0],vs[2]),new IntWritable(Integer.parseInt(vs[3])));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

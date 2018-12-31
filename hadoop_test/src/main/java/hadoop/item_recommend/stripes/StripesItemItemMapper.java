package hadoop.item_recommend.stripes;

import hadoop.common.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class StripesItemItemMapper extends Mapper<LongWritable, Text,Text, Tuple> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        if(StringUtils.isBlank(value.toString())){
            return;
        }
        String[] values=value.toString().split("\\s");
        String[] tuples=values[1].substring(1,values[1].length()-1).split(",");
        context.write(new Text(values[0]),new Tuple(tuples[0],Integer.parseInt(tuples[1])));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

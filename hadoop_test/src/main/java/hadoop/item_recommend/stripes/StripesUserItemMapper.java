package hadoop.item_recommend.stripes;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class StripesUserItemMapper extends Mapper<LongWritable, Text,Text,Text> {

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
        context.write(new Text(vs[0]),new Text(vs[1]));
//        super.map(key, value, context);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

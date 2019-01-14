package hadoop.cochran_armitage;

import hadoop.common.TextTriple;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CochranArmitage2_1Mapper extends Mapper<LongWritable, Text, TextTriple, IntWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String v=value.toString();
        if(StringUtils.isBlank(v)){
            return;
        }
        String[] vs=v.split("\t");
        String[] v1s=vs[0].substring(1,vs[0].length()-1).split(",");
        context.write(new TextTriple(v1s[0],v1s[1],v1s[2]),new IntWritable(Integer.parseInt(vs[1])));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

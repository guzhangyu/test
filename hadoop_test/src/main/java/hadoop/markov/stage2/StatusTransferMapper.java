package hadoop.markov.stage2;

import hadoop.common.TextPair2;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class StatusTransferMapper extends Mapper<LongWritable, Text, TextPair2, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String v=value.toString();
        if(StringUtils.isBlank(v)){
            return;
        }

        String []vs=v.split("\\t");
        String ks[]=vs[0].substring(1,vs[0].length()-1).split(",");
        context.write(new TextPair2(ks[0],ks[1]),new IntWritable(Integer.parseInt(vs[1])));
    }
}

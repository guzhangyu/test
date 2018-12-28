package hadoop.common_friends;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;

public class CommonFriendsMapper extends Mapper<LongWritable,Text,Text, Text> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String[] values=value.toString().split(",");
        if(values.length<2){
            return;
        }
        String[] friends=values[1].split(" ");
        if(friends.length<2){
            return;
        }

        for(String friend:friends){
            String[] pair=new String[]{values[0],friend};
            Arrays.sort(pair);
            context.write(new Text(StringUtils.join(pair,",")),new Text(values[1]));
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

package hadoop.item_recommend.user_recommend;

import hadoop.common.TupleArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CommonFriends1Mapper extends Mapper<LongWritable, Text, Text,Text> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String v=value.toString();
        if (StringUtils.isBlank(v)) {
            return;
        }

        String[] vs=v.split("\\s+");
        String v1=vs[0];

        context.write(new Text(v1),new Text(vs[1]));
        String[] friends=vs[1].split(",");
        for(int i=0;i<friends.length-1;i++){
            for(int j=i+1;j<friends.length;j++){
                String[] r=friends[i].compareTo(friends[j])>=0?new String[]{friends[i],friends[j]}:new String[]{friends[j],friends[i]};
                context.write(new Text(StringUtils.join(r,",")),new Text(v1));
//                context.write(new Text(friends[i]+","+friends[j]),new Text(v1));
            }
        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

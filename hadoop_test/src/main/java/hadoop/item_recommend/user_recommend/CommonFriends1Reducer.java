package hadoop.item_recommend.user_recommend;

import hadoop.common.TextPair;
import hadoop.common.Tuple;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonFriends1Reducer extends Reducer<Text, Text, TextPair, Tuple> {
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        String keyStr=key.toString();
        if(keyStr.indexOf(",")<0){
            for(Text value:values){
                context.write(new TextPair(keyStr,""),new Tuple("["+value.toString()+"]",0));
            }
            return;
        }

        List<String> friends=new ArrayList<>();
        for(Text v:values){
            friends.add(v.toString());
        }
        Collections.sort(friends);

        Tuple tuple=new Tuple(friends.toString(),friends.size());

        String[] keys=keyStr.split(",");
        context.write(new TextPair(keys[1],keys[0]),tuple);
//        context.write(new TextPair(keys[0],keys[1]),tuple);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

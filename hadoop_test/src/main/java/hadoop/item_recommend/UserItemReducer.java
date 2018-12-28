package hadoop.item_recommend;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserItemReducer extends Reducer<Text,Text,Text,Text> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        List<String> list=new ArrayList<>();
        for(Text value:values){
            list.add(value.toString());
        }
//        Text v=new Text(StringUtils.join(list,","));
//        for(String i:list){
//            context.write(new Text(i),key);
//        }
        for(String s:list){
            for(String s1:list){
                if(!s.equals(s1)){
                    context.write(new Text(s),new Text(s1));
                    context.write(new Text(s1),new Text(s));
                }
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

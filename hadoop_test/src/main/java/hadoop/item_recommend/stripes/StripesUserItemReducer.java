package hadoop.item_recommend.stripes;

import hadoop.common.Tuple;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StripesUserItemReducer extends Reducer<Text,Text,Text,Tuple> {
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

        Map<String,Integer> map=new HashMap<>();
        for(String s:list){
            Integer v=map.get(s);
            if(v==null){
                v=0;
            }
            map.put(s,v+1);
        }

        for(String s:list){
            for(Map.Entry<String,Integer> entry:map.entrySet()){
                if(entry.getKey().equals(s)){
                    continue;
                }
                context.write(new Text(s),new Tuple(entry.getKey(),entry.getValue()));
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

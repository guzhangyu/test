package hadoop.item_recommend;

import hadoop.common.DataTuple;
import hadoop.common.Tuple;
import hadoop.common.TupleArray;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class ItemItemReducer extends Reducer<Text,Text, Text, TupleArray> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        Map<String,Integer> map=new HashMap<>();
        for(Text value:values){
            String v=value.toString();
            Integer i=map.get(v);
            if(i==null){
                i=0;
            }
            map.put(v,i+1);
        }

        List<Map.Entry<String,Integer>> list=new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int c= o1.getValue().compareTo(o2.getValue());
                if(c!=0){
                    return c;
                }
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        if(list.size()>4){
           list=list.subList(0,4);
        }
        Tuple[] tuples=new Tuple[list.size()];
        int i=tuples.length-1;
        for(Map.Entry<String,Integer> entry:list){
            tuples[i--]=new Tuple(entry.getKey(),entry.getValue());
        }
        TupleArray tupleArray=new TupleArray();
        tupleArray.set(tuples);
        context.write(key,tupleArray);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

package hadoop.top10_1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Top10_1Reducer extends Reducer<IntWritable, Text,IntWritable,Text> {

    SortedMap<Integer,String> map=new TreeMap<>();

    @Override
    protected void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        for(Text value:values){
            map.put(key.get(),value.toString());
            if(map.size()>10){
                map.remove(map.firstKey());
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);

        for(Map.Entry<Integer,String> entry: map.entrySet()){
            context.write(null,new Text(entry.getValue()));
        }
    }
}

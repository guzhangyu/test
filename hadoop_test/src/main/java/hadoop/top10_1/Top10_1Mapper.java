package hadoop.top10_1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Top10_1Mapper extends Mapper<LongWritable, Text, IntWritable,Text> {


    SortedMap<Integer,String> map=new TreeMap<>();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String[] values=value.toString().split(",");
        map.put(Integer.parseInt(values[1]),value.toString());
        if(map.size()>10){
            map.remove(map.firstKey());
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
        for(Map.Entry<Integer,String> entry: map.entrySet()){
            context.write(new IntWritable(entry.getKey()),new Text(entry.getValue()));
        }
    }
}

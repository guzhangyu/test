package hadoop.bloom;

import hadoop.common.TextPair;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloomMapper extends Mapper<LongWritable, Text, TextPair, IntWritable> {

    List<Map<String,Integer>> hashMaps=new ArrayList<>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        for(int i=0;i<4;i++){
            hashMaps.add(new HashMap<>());
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String v=value.toString();
        String[] vs=v.split(";");
        Integer k=Integer.parseInt(vs[0]);

        for(int i=0;i<4;i++){
            Map<String,Integer> hashMap=hashMaps.get(i);
            String keyToInput=(k%(i+2))+"";

            Integer n=hashMap.get(keyToInput);
            if(n==null){
                n=0;
            }
            hashMap.put(keyToInput,n+1);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);

        int i=0;
        for(Map<String,Integer> map:hashMaps){
            for(Map.Entry<String,Integer> entry:map.entrySet()){
                context.write(new TextPair(i+"",entry.getKey()),new IntWritable(entry.getValue()));
            }
            i++;
        }
    }
}

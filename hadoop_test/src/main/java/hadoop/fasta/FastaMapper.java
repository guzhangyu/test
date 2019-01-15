package hadoop.fasta;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FastaMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    Map<Character,Integer> map=new HashMap<>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String vPre=value.toString();
        String[] vPres=vPre.split("\n");
        if(vPres.length<4){
            return;
        }

        String v=vPres[1];
        char[] cs=v.toCharArray();
        for(int i=0;i<v.length();i++){
            char c=cs[i];
            Integer n=map.get(c);
            if(n==null){
                n=0;
            }

            map.put(c,n+1);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
        for(Map.Entry<Character,Integer> entry:map.entrySet()){
            context.write(new Text(entry.getKey().toString()),new IntWritable(entry.getValue()));
        }
    }
}

package hadoop.bloom;

import hadoop.common.TextPair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloomUseMapper extends Mapper<LongWritable, Text, TextPair,Text> {

    List<Map<String,Integer>> hashMaps=new ArrayList<>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);

        Configuration conf=context.getConfiguration();
        FileSystem fileSystem=FileSystem.get(conf);

        FSDataInputStream inputStream=fileSystem.open(new Path(conf.get("bloom_path")));
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String line=null;
        while((line=br.readLine())!=null){
            String lines[]=line.split("\t");

            Integer index=Integer.parseInt(lines[0]);
            if(hashMaps.size()<=index){
                hashMaps.add(new HashMap<>());
            }

            Map<String,Integer> map=hashMaps.get(index);
            String[] vs=lines[1].substring(1,lines[1].length()-1).split(",");
            map.put(vs[0],Integer.parseInt(vs[1]));
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String v=value.toString();
        String[] vs=v.split(";");

        String v2s[]=vs[1].split(",");
        if(v2s[0].equals("a")){
            Integer k=Integer.parseInt(vs[0]);
            for(int i=0;i<hashMaps.size();i++){
                String mapedK=(k%(i+2))+"";
                if(!hashMaps.get(i).containsKey(mapedK)){
                    return;
                }
            }
        }

        context.write(new TextPair(vs[0],v2s[0]),new Text(v2s[1]));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

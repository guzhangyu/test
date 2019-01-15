package hadoop.ttest;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TTestReducer extends Reducer<Text,Text,Text, DoubleWritable> {

    Map<String,Double> map=new HashMap<>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        File file=new File("/usr/local/Cellar/hadoop/test/bioset_time.txt");
        System.out.println(file.exists());

        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line=null;
        while((line=br.readLine())!=null){
            String[] lines=line.split(",");
            map.put(lines[0],Double.parseDouble(lines[1]));
        }
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);

        Set<String> exists=new HashSet<>();
        for(Text value:values){
            exists.add(value.toString());
        }

        double []v1=new double[exists.size()];
        int i1=0;
        double []v2=new double[map.size()-exists.size()];
        int i2=0;
        for(Map.Entry<String,Double> entry:map.entrySet()){
            if(exists.contains(entry.getKey())){
                v1[i1++]=entry.getValue();
            }else{
                v2[i2++]=entry.getValue();
            }
        }
        context.write(key,new DoubleWritable(MathUtil.ttest(v1,v2)));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

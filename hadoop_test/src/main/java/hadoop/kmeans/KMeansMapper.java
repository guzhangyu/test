package hadoop.kmeans;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class KMeansMapper extends Mapper<LongWritable, Text, IntWritable,Text> {

    List<Integer[]> centers=new ArrayList<>();

    PrintWriter pw=null;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        Configuration conf=context.getConfiguration();
        String[] cts=conf.get("centers").split("\t");
        for(String ct:cts){
            Integer[] ctI = str2Ints(ct);
            centers.add(ctI);
        }

        FileSystem fileSystem=FileSystem.get(conf);
        FSDataOutputStream fos=fileSystem.create(new Path(conf.get("categoried_data")),true);
        pw=new PrintWriter(new OutputStreamWriter(fos));
    }

    static Integer[] str2Ints(String ct) {
        String[] ctArr=ct.split(",");
        Integer[] ctI=new Integer[ctArr.length];
        for(int i=0;i<ctArr.length;i++){
            ctI[i]=Integer.parseInt(ctArr[i]);
        }
        return ctI;
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        if(StringUtils.isBlank(value.toString())){
            return;
        }
        Integer[] v=str2Ints(value.toString());

        int min=-1,minIndex=0;
        for(int j=0;j<centers.size();j++){
            Integer[] center=centers.get(j);
            int a = calcDistance(v, center);
            if(min==-1 || min>a){
                min=a;
                minIndex=j;
            }
        }
        context.write(new IntWritable(minIndex),value);
        pw.println(String.format("%s\t%s",minIndex,value.toString()));
    }

    static int calcDistance(Integer[] v, Integer[] center) {
        int a=0;
        for(int i=0;i<center.length;i++){
            a+=(center[i]-v[i])*(center[i]-v[i]);
        }
        return a;
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
        pw.close();
    }
}

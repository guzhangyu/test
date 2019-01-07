package hadoop.naive_bayes_category;

import com.google.common.collect.Sets;
import hadoop.purchase.PurchaseCombineDriver;
import hadoop.purchase.PurchaseCombineMapper;
import hadoop.purchase.PurchaseCombineReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class NaiveBayesCategoryDriver extends Configured implements Tool{

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new NaiveBayesCategoryDriver(), args);
        System.exit(res);
    }

    static String preffix = "/usr/local/Cellar/hadoop/test/";

    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        conf.set("mapred.job.tracker", "local");
        conf.set("fs.default.name", "local");

        Job job = new Job(conf);

        FileInputFormat.setInputPaths(job, new Path(preffix + args[0]));
        FileOutputFormat.setOutputPath(job, new Path(preffix + args[1]));

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setMapperClass(NaiveBayesCategoryMapper.class);
        job.setReducerClass(NaiveBayesCategoryReducer.class);
//        job.setNumReduceTasks(4);
        int res=job.waitForCompletion(true) ? 1 : 0;

        predictModel(getLinesByPath(preffix + args[1]+"/part-r-00000", conf));

        return res;
    }

    private void predictModel(List<String> linesByPath) throws IOException {
        List<String> lines= linesByPath;
        Map<String,Integer> map=new HashMap<>();
        for(String line:lines){
            String ss[]=line.split("\\s+");
            map.put(ss[0],Integer.parseInt(ss[1]));
        }

        Set<String> y= Sets.newHashSet("I","II");
        Map<String,Double> probabilityMap=new HashMap<>();
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            String k=entry.getKey();
            if(y.contains(k)){
                probabilityMap.put(k,entry.getValue().doubleValue());
            }else{
                String[] ks=k.split("-");
                probabilityMap.put(k,entry.getValue()/map.get(ks[ks.length-1]).doubleValue());
            }
        }

        String x[]=new String[]{"1","3"};
        double[] rs=new double[y.size()];
        int i=0;
        for(String yi:y){
            double r=probabilityMap.get(yi);
            int k=0;
            for(String xi:x){
                Double d=probabilityMap.get((k++)+"-"+xi+"-"+yi);
                if(d==null){
                    d=0d;
                }
                r*=d;
            }
            rs[i++]=r;
            System.out.println(r);
        }
    }

    private static List<String> getLinesByPath(String arg, Configuration conf) throws IOException {
        FileSystem fileSystem=FileSystem.get(conf);
        FSDataInputStream inputStream=fileSystem.open(new Path(arg));
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String line=null;
        List<String> centers=new ArrayList<>();
        while((line=br.readLine())!=null){
            centers.add(line);
        }
        return centers;
    }



}

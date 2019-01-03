package hadoop.kmeans;

import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class KMeansDriver extends Configured implements Tool {

    static String preffix="/usr/local/Cellar/hadoop/test/";

    public static void main(String[] args) throws Exception {
        Configuration conf=new Configuration();
        conf.set("mapred.job.tracker", "local");
        conf.set("fs.default.name", "local");
        List<String> lines=new ArrayList<>(new HashSet<>(getLinesByPath(preffix+args[0],conf)));
        int n=2;
        lines=lines.subList(0,n);
        conf.set("centers", StringUtils.join(lines,"\t"));
        conf.set("categoried_data",preffix+"/knn_categoried_data.txt");

        List<Double> distances=new ArrayList<>();
        do{
            FileSystem fileSystem=FileSystem.get(conf);
            if(fileSystem.exists(new Path(preffix+args[1]))){
                fileSystem.delete(new Path(preffix+args[1]),true);
            }

            ToolRunner.run(conf,new KMeansDriver(),args);

            lines = getLinesByPath(preffix+args[1]+"/part-r-00000", conf);
            List<String> centers=new ArrayList<>(lines.size());
            for(String line:lines){
                String[] lineArr=line.split("--");
                distances.add(Double.parseDouble(lineArr[1]));
                centers.add(lineArr[0]);
            }
            conf.set("centers", StringUtils.join(centers,"\t"));
        }while(totalDistance(distances)>80);

    }

    private static double totalDistance(List<Double> distances){
        double totalDistance=0d;
        for(double distance:distances){
            totalDistance+=distance*distance;
        }
        System.out.println(String.format("distance:%s",totalDistance));
        return totalDistance;
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

    public int run(String[] args) throws Exception {
        Configuration conf=getConf();
        Job job=new Job(conf);

        FileInputFormat.setInputPaths(job,new Path(preffix+args[0]));
        FileOutputFormat.setOutputPath(job,new Path(preffix+args[1]));

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setMapperClass(KMeansMapper.class);
        job.setReducerClass(KMeansReducer.class);
        return job.waitForCompletion(true)?1:0;
    }
}

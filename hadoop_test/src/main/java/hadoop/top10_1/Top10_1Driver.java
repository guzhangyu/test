package hadoop.top10_1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Top10_1Driver extends Configured implements Tool {

    static String preffix="/usr/local/Cellar/hadoop/test/";
    //"hdfs://master:8020/";

    public static void main(String[] args) throws Exception {
        int res= ToolRunner.run(new Configuration(),new Top10_1Driver(),args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        Configuration conf=getConf();
        conf.set("mapred.job.tracker", "local");
        conf.set("fs.default.name", "local");
        conf.set("n","10");

        Job job=new Job(conf);

        FileInputFormat.setInputPaths(job,preffix+args[0]);
        FileOutputFormat.setOutputPath(job,new Path(preffix+args[1]));

        job.setMapOutputKeyClass(IntWritable.class);

        job.setMapperClass(Top10_1Mapper.class);
        job.setReducerClass(Top10_1Reducer.class);
        job.setNumReduceTasks(Integer.parseInt(args[2]));
        return job.waitForCompletion(true)?1:0;
    }
}

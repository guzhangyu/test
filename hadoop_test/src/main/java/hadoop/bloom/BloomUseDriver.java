package hadoop.bloom;

import hadoop.common.TextPair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class BloomUseDriver extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res= ToolRunner.run(new Configuration(),new BloomUseDriver(),args);
        System.exit(res);
    }

    static String preffix="/usr/local/Cellar/hadoop/test/";

    public int run(String[] args) throws Exception {
        Configuration conf=getConf();
        conf.set("mapred.job.tracker", "local");
        conf.set("fs.default.name", "local");
        conf.set("bloom_path",preffix+"bloom_output/part-r-00000");

        Job job=new Job(conf);

        FileInputFormat.setInputPaths(job,new Path(preffix+args[0]));
        FileOutputFormat.setOutputPath(job,new Path(preffix+args[1]));


        job.setMapOutputKeyClass(TextPair.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(BloomUseMapper.class);
        job.setReducerClass(BloomUseReducer.class);
//        job.setNumReduceTasks(4);
        return job.waitForCompletion(true)?1:0;
    }

}

package hadoop.triangle;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Triangle3Driver extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new Triangle3Driver(), args);
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
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Triangle3Mapper.class);
        job.setReducerClass(Triangle3Reducer.class);
//        job.setNumReduceTasks(4);
        return job.waitForCompletion(true) ? 1 : 0;
    }
}

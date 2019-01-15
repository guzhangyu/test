package hadoop.small_files;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SmallFilesFormatDriver extends Configured implements Tool {

    static String preffix="/usr/local/Cellar/hadoop/test/";
    //"hdfs://master:8020/";

    public static void main(String[] args) throws Exception {
        int res= ToolRunner.run(new Configuration(),new SmallFilesFormatDriver(),args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        Configuration conf=getConf();

        Job job=new Job(conf);

        FileInputFormat.setInputPaths(job,preffix+args[0]);
        FileOutputFormat.setOutputPath(job,new Path(preffix+args[1]));
        job.setInputFormatClass(SmallFilesInputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setMapperClass(SmallFilesFormatMapper.class);
        job.setReducerClass(SmallFilesReducer.class);
        return job.waitForCompletion(true)?1:0;
    }

}

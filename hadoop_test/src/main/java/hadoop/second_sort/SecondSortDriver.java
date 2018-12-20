package hadoop.second_sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SecondSortDriver extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res= ToolRunner.run(new Configuration(),new SecondSortDriver(),args);
        System.exit(res);
    }

    static String preffix="/usr/local/Cellar/hadoop/test/";
            //"hdfs://master:8020/";

    public int run(String[] args) throws Exception {
        Configuration conf=getConf();
        conf.set("mapred.job.tracker", "local");
        conf.set("fs.default.name", "local");

        Job job=new Job(getConf());

        FileInputFormat.setInputPaths(job,preffix+args[0]);
        FileOutputFormat.setOutputPath(job,new Path(preffix+args[1]));

        job.setOutputKeyClass(DateTemperaturePair.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(SecondSortMapper.class);
        job.setReducerClass(SecondSortReducer.class);
        job.setGroupingComparatorClass(DateTemperatureGroupingComparator.class);
        job.setPartitionerClass(DateTemperaturePartitioner.class);
        job.setNumReduceTasks(4);

        return job.waitForCompletion(true)?1:0;
    }
}

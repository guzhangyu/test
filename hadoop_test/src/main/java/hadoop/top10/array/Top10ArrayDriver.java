package hadoop.top10.array;

import hadoop.common.TupleArray;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Top10ArrayDriver extends Configured implements Tool {

    static String preffix="/usr/local/Cellar/hadoop/test/";
    //"hdfs://master:8020/";

    public static void main(String[] args) throws Exception {
        int res= ToolRunner.run(new Configuration(),new Top10ArrayDriver(),args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        Configuration conf=getConf();
        conf.set("mapred.job.tracker", "local");
        conf.set("fs.default.name", "local");

        Job job=new Job(conf);

        FileInputFormat.setInputPaths(job,preffix+args[0]);
        FileOutputFormat.setOutputPath(job,new Path(preffix+args[1]));

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TupleArray.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(ArrayWritable.class);
////        job.setOutputFormatClass(new FileOutputFormat<Text, ArrayWritable>() {
//            @Override
//            public RecordWriter<Text, ArrayWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
//                return null;
//            }
//        });

        job.setMapperClass(Top10ArrayMapper.class);
        job.setReducerClass(Top10ArrayReducer.class);
        job.setGroupingComparatorClass(Top10ArrayGroupingComparator.class);

//        job.setSortComparatorClass(IntWritable.Comparator.class);
//        job.setMaxReduceAttempts(3);
//        job.setNumReduceTasks(10);
        return job.waitForCompletion(true)?1:0;
    }
}

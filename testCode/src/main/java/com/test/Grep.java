package com.test;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.InverseMapper;
import org.apache.hadoop.mapred.lib.LongSumReducer;
import org.apache.hadoop.mapred.lib.RegexMapper;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Random;

public class Grep extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res= ToolRunner.run(new Configuration(),new Grep(),args);
        System.exit(res);
    }

    static String preffix="hdfs://master:8020/";

    @Override
    public int run(String[] args) throws Exception {
        String tempDirStr="/tmp.db/grep-temp-"+Integer.toString(new Random().nextInt());
        Path tempDir=new Path(preffix+tempDirStr);
        JobConf grepJob=new JobConf(getConf(),Grep.class);
        grepJob.setJobName("grep-search");
        FileInputFormat.setInputPaths(grepJob,preffix+args[0]);
        grepJob.setMapperClass(RegexMapper.class);

        grepJob.set("mapred.mapper.regex",args[2]);
        if(args.length==4){
            grepJob.set("mapred.mapper.regex.group",args[3]);
        }
        grepJob.setCombinerClass(LongSumReducer.class);
        grepJob.setReducerClass(LongSumReducer.class);

        FileOutputFormat.setOutputPath(grepJob,tempDir);
        grepJob.setOutputFormat(SequenceFileOutputFormat.class);
        grepJob.setOutputKeyClass(Text.class);
        grepJob.setOutputValueClass(LongWritable.class);

        JobClient.runJob(grepJob);

        JobConf sortJob=new JobConf(getConf(),Grep.class);
        sortJob.setJobName("grep-sort");
        FileInputFormat.setInputPaths(sortJob,tempDir);
        sortJob.setInputFormat(SequenceFileInputFormat.class);
        sortJob.setMapperClass(InverseMapper.class);
        sortJob.setNumReduceTasks(1);
        FileOutputFormat.setOutputPath(sortJob,new Path(preffix+"/tmp.db/"+args[1]));
        sortJob.setOutputKeyComparatorClass(LongWritable.DecreasingComparator.class);
        JobClient.runJob(sortJob);
        System.out.println(tempDir.toString());

        FileSystem.get(grepJob).delete(tempDir,true);
        return 0;
    }
}

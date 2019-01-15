package fasta;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;

public class FastaSparkDriver {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().set("spark.master", "local")//spark://192.168.1.103:7077
                .set("spark.app.name", "test");
        JavaSparkContext sc = new JavaSparkContext(conf);

        Configuration hadoopConf=new Configuration();
        hadoopConf.set("mapred.job.tracker", "local");
        hadoopConf.set("fs.default.name", "local");
        hadoopConf.set(NLineInputFormat.LINES_PER_MAP,"4");

        String preffix = "file:///usr/local/Cellar/hadoop/test";
        JavaPairRDD<LongWritable,Text> input = sc.newAPIHadoopFile(preffix+"/fasta_input2.txt",FastaInputFormat.class, LongWritable.class, Text.class,hadoopConf);
        List list=input.collect();
        System.out.println(list);
    }
}

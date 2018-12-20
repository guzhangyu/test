package second_sort;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SecondSortSparkDriver {

    public static void main(String[] args) {
        SparkConf conf=new SparkConf().set("spark.master","spark://master:7077")
                .set("spark.app.name","test");
        JavaSparkContext sc=new JavaSparkContext(conf);
        String path="/usr/local/Cellar/hadoop/3.1.1/libexec//tests/sample_input.txt";
        JavaRDD<String> input=sc.textFile(path,4);
        input.saveAsTextFile(path+1);
    }
}

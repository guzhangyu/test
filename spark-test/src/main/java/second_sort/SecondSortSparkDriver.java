package second_sort;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondSortSparkDriver {

    public static void main(String[] args) {
        SparkConf conf=new SparkConf()//.set("spark.master","spark://master:7077")
                .set("spark.app.name","test");
        JavaSparkContext sc=new JavaSparkContext(conf);
        //file:///usr/local/Cellar/hadoop/3.1.1/libexec//tests/sample_input.txt
//        String path="/sample_input.txt";
        JavaRDD<String> input=sc.textFile("/secondary_sort/input/sample_input.txt",4);
//        input.saveAsTextFile(path+1);

        JavaPairRDD<String,Integer> pairs=input.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                String[] lines=s.split(",");
                return new Tuple2<>(lines[0]+"-"+lines[1],Integer.parseInt(lines[3]));
            }
        });
        JavaRDD<Tuple2<String,String>> result=pairs.groupByKey().map(new Function<Tuple2<String, Iterable<Integer>>,Tuple2<String,String>>() {
            @Override
            public Tuple2<String,String> call(Tuple2<String, Iterable<Integer>> tuple) throws Exception {
                List<Integer> list=new ArrayList<>();
                for(Integer i:tuple._2){
                    list.add(i);
                }
                Collections.sort(list);
                return new Tuple2<String,String>(tuple._1,StringUtils.join(list.toArray(new Integer[list.size()]),','));
            }
        });
        result.saveAsTextFile("/secondary_sort/output1");
    }
}

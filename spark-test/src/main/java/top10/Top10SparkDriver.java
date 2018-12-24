package top10;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Top10SparkDriver {

    public static void main(String[] args) {
        SparkConf conf=new SparkConf().set("spark.master","spark://192.168.1.103:7077")
                .set("spark.app.name","test");
        JavaSparkContext sc=new JavaSparkContext(conf);
        JavaRDD<String> input=sc.textFile("file:///usr/local/Cellar/hadoop/test/top10_input.txt",1);
//        input.saveAsTextFile(path+1);

        JavaPairRDD<Integer,String> pairRDD=input.mapToPair(new PairFunction<String, Integer, String>() {
            @Override
            public Tuple2<Integer, String> call(String s) throws Exception {
                String[] ss=s.split(",");
                return new Tuple2<>(Integer.parseInt(ss[1]),ss[0]);
            }
        });



        final Comparator<Tuple2<Integer,?>> comparator=new SerializableComparator<Tuple2<Integer,?>>() {
            @Override
            public int compare(Tuple2<Integer, ?> o1, Tuple2<Integer, ?> o2) {
                return o1._1.compareTo(o2._1);
            }
        };
        List list=pairRDD.aggregate(new ArrayList(), new Function2<List, Tuple2<Integer, String>, List>() {
            @Override
            public List call(List v1, Tuple2<Integer, String> v2) throws Exception {
                v1.add(v2);
                if (v1.size() > 10) {
                    Collections.sort(v1, comparator);
                    v1.remove(10);
                }
                return v1;
            }
        }, new Function2<List, List, List>() {
            @Override
            public List call(List v1, List v2) throws Exception {
                v1.addAll(v2);
                Collections.sort(v1,comparator);
                if(v1.size()>10){
                    v1=v1.subList(0,10);
                }
                return v1;
            }
        });

//        List list=pairRDD.sortByKey().collect();
//        if(list.size()>10){
//            list=list.subList(0,10);
//        }
        System.out.println("result:");
        System.out.println(list);

    }
}

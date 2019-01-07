package triangle;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.*;

public class TriangleSparkDriver {

    public static void main(String[] args) {
        SparkConf conf=new SparkConf().set("spark.master","local")
                .set("spark.app.name","test");
        JavaSparkContext sc=new JavaSparkContext(conf);
        JavaRDD<String> input=sc.textFile("file:///usr/local/Cellar/hadoop/test/triangle_input.txt",1);

        List result=input.flatMapToPair((PairFlatMapFunction<String, String, String>) s -> {
            String ss[]=s.split(",");
            if(ss.length<2){
                return Collections.EMPTY_LIST.iterator();
            }
            return Arrays.asList(new Tuple2<>(ss[0],ss[1]),new Tuple2<>(ss[1],ss[0])).iterator();
        }).groupByKey() //每个点的边列表
                .flatMapToPair((PairFlatMapFunction<Tuple2<String, Iterable<String>>, Tuple2<String, String>, String>) stringIterableTuple2 -> {
                    List<Tuple2<Tuple2<String, String>, String>> list=new ArrayList<>();
                    String k=stringIterableTuple2._1;

                    List<String> values=new ArrayList<>();
                    for(String s:stringIterableTuple2._2){
                        for(String v:values){
                            list.add(new Tuple2<>(new Tuple2<>(v,s),k));
                        }
                        list.add(new Tuple2<>(new Tuple2<>(s,k),""));
                        values.add(s);
                    }
                    return list.iterator();
                })
                .groupByKey() //每个边的关联点列表
                .flatMapToPair((PairFlatMapFunction<Tuple2<Tuple2<String, String>, Iterable<String>>, String, String>) tuple2IterableTuple2 -> {
                    Set<String> set=new HashSet<>();
                    for(String s:tuple2IterableTuple2._2){
                        set.add(s);
                    }
                    List<Tuple2<String,String>> list=new ArrayList<>();
                    if(!set.contains("")){
                        return list.iterator();
                    }

                    set.remove("");
                    for(String s:set){
                        list.add(new Tuple2<>(s,String.format("(%s,%s)",tuple2IterableTuple2._1._1,tuple2IterableTuple2._1._2)));
                    }
                    return list.iterator();
                }) //每个点对应的三角点对
                .aggregateByKey(new ArrayList<>(), (Function2<List<String>, String, List<String>>) (v1, v2) -> {
                     v1.add(v2);
                     return v1;
                 },(Function2<List<String>, List<String>, List<String>>) (v1, v2) -> {
                    v1.addAll(v2);
                    return v1;
                }).collect();

        System.out.println(result);
    }
}

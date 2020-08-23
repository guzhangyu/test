package leftjoin;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.*;

public class LeftJoinSparkDriver{

    final static Logger logger= LoggerFactory.getLogger(LeftJoinSparkDriver.class);

    public static void main(String[] args) {
        SparkConf conf=new SparkConf().set("spark.master","spark://192.168.1.103:7077")//
                .set("spark.app.name","test");
        JavaSparkContext sc=new JavaSparkContext(conf); //file:///usr/local/Cellar/hadoop
        JavaRDD<String> input=sc.textFile("/test/leftjoin_input1.txt",1);

        JavaPairRDD<String,Tuple2<Integer,String>> pairRDD1=input.mapToPair((PairFunction<String, String, Tuple2<Integer,String>>) s -> {
            String[] ss=s.split(",");
            return new Tuple2<>(ss[1],new Tuple2<>(1,s));
        });

        input=sc.textFile("/test/leftjoin_input2.txt",1);

        JavaPairRDD<String,Tuple2<Integer,String>> pairRDD2=input.mapToPair((PairFunction<String, String, Tuple2<Integer,String>>) s -> {
            String[] ss=s.split(",");
            return new Tuple2<>(ss[0],new Tuple2<>(2,s));
        });

        //1
//        JavaPairRDD pair=pairRDD1.union(pairRDD2).groupByKey().flatMapToPair(new PairFlatMapFunction<Tuple2<String, Iterable<Tuple2<Integer, String>>>, Object, Object>() {
//            @Override
//            public Iterator<Tuple2<Object, Object>> call(Tuple2<String, Iterable<Tuple2<Integer, String>>> stringIterableTuple2) throws Exception {
//                List<String> list1=new ArrayList<>();
//                List<String> list2=new ArrayList<>();
//                for(Tuple2<Integer,String> itr:stringIterableTuple2._2()){
//                    if(itr._1==1){
//                        list1.add(itr._2);
//                    }else{
//                        list2.add(itr._2);
//                    }
//                }
//
//                List<Tuple2<Object,Object>> list=new ArrayList<>();
//                logger.debug("result:");
//                for(String s1:list1){
//                    if(list2.size()>0){
//                        for(String s2:list2){
//                            logger.debug(String.format("%s %s",s1,s2));
//                            list.add(new Tuple2(s1,s2));
//                        }
//                    }else{
//                        logger.debug(s1);
//                        list.add(new Tuple2(s1,null));
//                    }
//                }
//                return list.iterator();
//            }
//        });

//2
//        //cogroup 笛卡尔积
//        System.out.println(pair.collect());


        //3
//        JavaPairRDD pairs=pairRDD1.union(pairRDD2).groupByKey().mapValues(new Function<Iterable<Tuple2<Integer, String>>,List< Tuple2<String,Integer>>>() {
//            @Override
//            public List<Tuple2<String,Integer>> call(Iterable<Tuple2<Integer, String>> v1) throws Exception {
//                List<String> list1=new ArrayList<>();
//                List<String> list2=new ArrayList<>();
//                for(Tuple2<Integer,String> itr:v1){
//                    if(itr._1==1){
//                        list1.add(itr._2);
//                    }else{
//                        list2.add(itr._2);
//                    }
//                }
//                List<Tuple2<String,Integer>> list=new ArrayList<>();
//                for(String s:list1){
//                    list.add(new Tuple2<>(s,list2.size()));
//                }
//                return list;
//            }
//        });


        //4
        JavaPairRDD pair3=pairRDD1.union(pairRDD2).combineByKey(new Function<Tuple2<Integer, String>, Tuple2<Set<String>, Set<String>>>() {

            @Override
            public Tuple2<Set<String>, Set<String>> call(Tuple2<Integer, String> v1) throws Exception {
                Set<String> set = new HashSet<>();
                set.add(v1._2);
                return new Tuple2<Set<String>, Set<String>>(v1._1 == 1 ? set : new HashSet<>(), v1._1 == 2 ? set : new HashSet<>());
            }
        }, new Function2<Tuple2<Set<String>, Set<String>>, Tuple2<Integer, String>, Tuple2<Set<String>, Set<String>>>() {
            @Override
            public Tuple2<Set<String>, Set<String>> call(Tuple2<Set<String>, Set<String>> v1, Tuple2<Integer, String> v2) throws Exception {
                if (v2._1 == 1) {
                    v1._1.add(v2._2);
                } else {
                    v1._2.add(v2._2);
                }
                return v1;
            }
        }, new Function2<Tuple2<Set<String>, Set<String>>, Tuple2<Set<String>, Set<String>>, Tuple2<Set<String>, Set<String>>>() {
            @Override
            public Tuple2<Set<String>, Set<String>> call(Tuple2<Set<String>, Set<String>> v1, Tuple2<Set<String>, Set<String>> v2) throws Exception {
                v1._1.addAll(v2._1);
                v1._2.addAll(v2._2);
                return v1;
            }
        });


        System.out.println(pair3.collect());


        //5
//        pairRDD1.union(pairRDD2).groupByKey().foreach(new VoidFunction<Tuple2<String, Iterable<Tuple2<Integer, String>>>>() {
//            @Override
//            public void call(Tuple2<String, Iterable<Tuple2<Integer, String>>> stringIterableTuple2) throws Exception {
//                List<String> list1=new ArrayList<>();
//                List<String> list2=new ArrayList<>();
//                for(Tuple2<Integer,String> itr:stringIterableTuple2._2()){
//                    if(itr._1==1){
//                        list1.add(itr._2);
//                    }else{
//                        list2.add(itr._2);
//                    }
//                }
//
//               logger.debug("result:");
//                for(String s1:list1){
//                    if(list2.size()>0){
//                        for(String s2:list2){
//                            logger.debug(String.format("%s %s",s1,s2));
//                        }
//                    }else{
//                        logger.debug(s1);
//                    }
//                }
//            }
//        });

//        JavaPairRDD<String,Tuple2<String, Optional<String>>> result=pairRDD1.leftOuterJoin(pairRDD2);
//        List<Tuple2<String,Tuple2<String, Optional<String>>>> list=result.collect();
//        System.out.println(list);
    }
}

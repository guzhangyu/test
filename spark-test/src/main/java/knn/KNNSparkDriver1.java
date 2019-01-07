package knn;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;

import java.io.IOException;
import java.util.*;

public class KNNSparkDriver1 {

    final static Comparator<Tuple4<String, int[], Double,Integer>> comparator= (o1, o2) -> {
        double r=o1._3()-o2._3();
        if(r>0){
            return 1;
        }else if(r<0){
            return -1;
        }
        return 0;
    };

    public static void main(String[] args) throws IOException {
        SparkConf conf=new SparkConf().set("spark.master","local")//
                .set("spark.app.name","test");
        JavaSparkContext sc=new JavaSparkContext(conf); //file://
        String preffix="/usr/local/Cellar/hadoop";
        JavaRDD<String> input1=sc.textFile(preffix+"/test/knn_input1.txt",1);

        final Integer N=2;

        JavaPairRDD<String,int[]> pair1=input1.mapToPair((PairFunction<String, String, int[]>) s -> {
            String[] ss=s.split("\\s+");
            return new Tuple2<>(ss[0],str2Ints(ss[1]));
        });
        JavaRDD<Tuple3<String,int[], Integer>> pair2=KMeansSparkDriver.categoryData(sc,preffix+"/test/knn_input2.txt");

        JavaPairRDD<Tuple2<String, int[]>,Iterable<Tuple4<String, int[],Double,Integer>>> mid=pair1
                .cartesian(pair2)
                .mapToPair((PairFunction<Tuple2<Tuple2<String, int[]>, Tuple3<String, int[],Integer>>, Tuple2<String, int[]>, Tuple4<String, int[], Double,Integer>>) tuple2Tuple2Tuple2
                        -> new Tuple2<>(tuple2Tuple2Tuple2._1,
                            new Tuple4<>(tuple2Tuple2Tuple2._2._1(),tuple2Tuple2Tuple2._2._2(),calcDistance(tuple2Tuple2Tuple2._1._2,tuple2Tuple2Tuple2._2._2()),tuple2Tuple2Tuple2._2._3())))
                .groupByKey();

        List<Tuple2<Tuple2<String, int[]>,List<Tuple4<String, int[], Double,Integer>>>> list=mid.aggregateByKey(new ArrayList<>(), (Function2<List<Tuple4<String, int[], Double,Integer>>, Iterable<Tuple4<String, int[], Double,Integer>>, List<Tuple4<String, int[], Double,Integer>>>) (v1, v2) -> {
                    for (Tuple4<String, int[], Double,Integer> v : v2) {
                        v1.add(v);
                    }
                    if (v1.size() > N) {
                        Collections.sort(v1, comparator);
                        v1 = new ArrayList<>(v1.subList(0, N));
                    }
                    return v1;
                }, (Function2<List<Tuple4<String, int[], Double,Integer>>, List<Tuple4<String, int[], Double,Integer>>, List<Tuple4<String, int[], Double,Integer>>>) (v1, v2) -> {
                    v1.addAll(v2);
                    if (v1.size() > N) {
                        Collections.sort(v1, comparator);
                        v1 = new ArrayList<>(v1.subList(0, N));
                    }
                    return v1;
                }).mapValues(new Function<List<Tuple4<String,int[], Double, Integer>>, List<Tuple4<String,int[], Double, Integer>>>() {
                    @Override
                    public List<Tuple4<String,int[], Double, Integer>> call(List<Tuple4<String, int[], Double, Integer>> v1) throws Exception {
                        Map<Integer,Integer> categoryCount=new HashMap<>();
                        for(Tuple4<String,int[], Double, Integer> t:v1){
                            Integer v=categoryCount.get(t._4());
                            if(v==null){
                                v=0;
                            }
                            categoryCount.put(t._4(),v+1);
                        }

                        Integer mostCategory=0;
                        Integer mostCategoryCount=0;
                        for(Map.Entry<Integer,Integer> entry:categoryCount.entrySet()){
                            if(entry.getValue()>mostCategoryCount){
                                mostCategoryCount=entry.getValue();
                                mostCategory=entry.getKey();
                            }
                        }

                        List<Tuple4<String,int[], Double, Integer>> newList=new ArrayList<>();
                        for(Tuple4<String,int[], Double, Integer> t:v1){
                            newList.add(new Tuple4<>(t._1(),t._2(),t._3(),mostCategory));
                        }
                        return newList;
                    }
                })
                .collect();
        System.out.println(list);
    }

    static double calcDistance(int[] v, int[] center) {
        int a=0;
        for(int i=0;i<center.length;i++){
            a+=(center[i]-v[i])*(center[i]-v[i]);
        }
        return a;
    }

    static double calcDistance(int[] v, double[] center) {
        int a=0;
        for(int i=0;i<center.length;i++){
            a+=(center[i]-v[i])*(center[i]-v[i]);
        }
        return a;
    }

    static double calcDistance(double[] v, double[] center) {
        int a=0;
        for(int i=0;i<center.length;i++){
            a+=(center[i]-v[i])*(center[i]-v[i]);
        }
        return a;
    }

    private static int[] str2Ints(String v1) {
        String[] vs=v1.split(",");
        int[] r=new int[vs.length];
        for(int i=0;i<vs.length;i++){
            r[i]=Integer.parseInt(vs[i]);
        }
        return r;
    }
}

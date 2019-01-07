package knn;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import scala.Tuple3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KNNSparkDriver {

    final static Comparator<Tuple3<String, int[], Double>> comparator= (o1, o2) -> {
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
        JavaRDD<String> input2=sc.textFile(preffix+"/test/knn_input2.txt",1);
        final Integer N=2;

        JavaPairRDD<String,int[]> pair1=input1.mapToPair((PairFunction<String, String, int[]>) s -> {
            String[] ss=s.split("\\s+");
            return new Tuple2<>(ss[0],str2Ints(ss[1]));
        });
        JavaPairRDD<String,int[]> pair2=input2.mapToPair((PairFunction<String, String, int[]>) s -> {
            String[] ss=s.split("\\s+");
            return new Tuple2<>(ss[0],str2Ints(ss[1]));
        });

        JavaPairRDD<Tuple2<String, int[]>,Iterable<Tuple3<String, int[],Double>>> mid=pair1
                .cartesian(pair2)
                .mapToPair((PairFunction<Tuple2<Tuple2<String, int[]>, Tuple2<String, int[]>>, Tuple2<String, int[]>, Tuple3<String, int[], Double>>) tuple2Tuple2Tuple2
                        -> new Tuple2<>(tuple2Tuple2Tuple2._1,new Tuple3<>(tuple2Tuple2Tuple2._2._1,tuple2Tuple2Tuple2._2._2,calcDistance(tuple2Tuple2Tuple2._1._2,tuple2Tuple2Tuple2._2._2))))
                .groupByKey();

//        List<Tuple2<Tuple2<String, int[]>,List<Tuple3<String, int[], Double>>>> list=.collect();
//        System.out.println(list);

        mid.aggregateByKey(new ArrayList<>(), (Function2<List<Tuple3<String, int[], Double>>, Iterable<Tuple3<String, int[], Double>>, List<Tuple3<String, int[], Double>>>) (v1, v2) -> {
                    for (Tuple3<String, int[], Double> v : v2) {
                        v1.add(v);
                    }
                    if (v1.size() > N) {
                        Collections.sort(v1, comparator);
                        v1 = new ArrayList<>(v1.subList(0, N));
                    }
                    return v1;
                }, (Function2<List<Tuple3<String, int[], Double>>, List<Tuple3<String, int[], Double>>, List<Tuple3<String, int[], Double>>>) (v1, v2) -> {
                    v1.addAll(v2);
                    if (v1.size() > N) {
                        Collections.sort(v1, comparator);
                        v1 = new ArrayList<>(v1.subList(0, N));
                    }
                    return v1;
                });
        //.join(KMeansSparkDriver.categoryData(sc,preffix+"/test/knn_input2.txt"));
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

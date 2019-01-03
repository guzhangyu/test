package kmeans;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class KMeansSparkDriver {

    static List<double[]> centers=new ArrayList<>();

    public static void main(String[] args) throws IOException {
        SparkConf conf=new SparkConf().set("spark.master","local")//
                .set("spark.app.name","test");
        JavaSparkContext sc=new JavaSparkContext(conf); //file://
        String preffix="/usr/local/Cellar/hadoop";
        JavaRDD<String> input=sc.textFile(preffix+"/test/kmeans_input.txt",1);
        JavaRDD<int[]> data=input.map((Function<String, int[]>) v1 -> str2Ints(v1));

        Configuration hadoopConf=new Configuration();
        hadoopConf.set("mapred.job.tracker", "local");
        hadoopConf.set("fs.default.name", "local");
        List<String> lines=new ArrayList<>(new HashSet<>(getLinesByPath(preffix+"/test/kmeans_input.txt",hadoopConf)));
        int n=2;
        lines=lines.subList(0,n);
        for(String line:lines){
            int arr[]=str2Ints(line);
            double darr[]=new double[arr.length];
            for(int i=0;i<arr.length;i++){
                darr[i]=arr[i];
            }
            centers.add(darr);
        }

        double totalDistance=-1;
        do{
            List<Tuple2<Integer,Tuple2<int[],Integer>>> list=data.mapToPair(new PairFunction<int[], Integer,Tuple2<int[],Integer>>() {
                @Override
                public Tuple2<Integer, Tuple2<int[],Integer>> call(int[] ints) throws Exception {
                    double min=-1;
                    int minIndex=0;
                    for(int j=0;j<centers.size();j++){
                        double[] center=centers.get(j);
                        double a = calcDistance(ints, center);
                        if(min==-1 || min>a){
                            min=a;
                            minIndex=j;
                        }
                    }
                    return new Tuple2<>(minIndex,new Tuple2<>(ints,1));
                }
            }).reduceByKey((Function2<Tuple2<int[], Integer>, Tuple2<int[], Integer>, Tuple2<int[], Integer>>) (v1, v2) -> {
                int[]r=new int[v1._1.length];
                for(int i=0;i<r.length;i++){
                    r[i]=v1._1[i]+v2._1[i];
                }
                return new Tuple2<>(r,v1._2+v2._2);
            }).collect();

            List<double[]> newCenters=new ArrayList<>();
            List<Double> distances=new ArrayList<>();
            for(Tuple2<Integer,Tuple2<int[],Integer>> t:list){
                int r[]=t._2._1;
                double rd[]=new double[r.length];
                for(int i=0;i<r.length;i++){
                    rd[i]=r[i]/t._2._2.doubleValue();
                }

                newCenters.add(rd);
                distances.add(calcDistance(rd,centers.get(t._1)));
            }
            totalDistance=totalDistance(distances);

            centers=newCenters;
        }while (totalDistance>80);
        System.out.println(totalDistance);
    }

    private static double totalDistance(List<Double> distances){
        double totalDistance=0d;
        for(double distance:distances){
            totalDistance+=distance*distance;
        }
        System.out.println(String.format("distance:%s",totalDistance));
        return totalDistance;
    }

    private static List<String> getLinesByPath(String arg, Configuration conf) throws IOException {
        FileSystem fileSystem=FileSystem.get(conf);
        FSDataInputStream inputStream=fileSystem.open(new Path(arg));
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String line=null;
        List<String> centers=new ArrayList<>();
        while((line=br.readLine())!=null){
            centers.add(line);
        }
        return centers;
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

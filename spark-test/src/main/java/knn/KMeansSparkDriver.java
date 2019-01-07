package knn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import scala.Tuple3;

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
        categoryData(sc, preffix+"/test/knn_input1.txt");
    }

    /**
     * 对数据进行分组
     * @param sc
     * @throws IOException
     */
    public static JavaRDD<Tuple3<String,int[], Integer>> categoryData(JavaSparkContext sc, String path) throws IOException {
        JavaRDD<String> input=sc.textFile(path,1);
        JavaRDD<Tuple2<String,int[]>> data=input.map((Function<String, Tuple2<String,int[]>>) v1 -> {
            String[] s=v1.split("\\s+");
            return new Tuple2<>(s[0],str2Ints(s[1]));
        });

        Configuration hadoopConf=new Configuration();
        hadoopConf.set("mapred.job.tracker", "local");
        hadoopConf.set("fs.default.name", "local");
        List<String> lines=new ArrayList<>(new HashSet<>(getLinesByPath(path,hadoopConf)));
        int n=2;
        lines=lines.subList(0,n);
        for(String line:lines){
            String[] s=line.split("\\s+");
            int arr[]=str2Ints(s[1]);
            double darr[]=new double[arr.length];
            for(int i=0;i<arr.length;i++){
                darr[i]=arr[i];
            }
            centers.add(darr);
        }

        double totalDistance=-1;
        JavaPairRDD<Integer,Tuple3<String,int[],Integer>> categoriedData=null;
        do{
            categoriedData=data.mapToPair((PairFunction<Tuple2<String,int[]>, Integer, Tuple3<String,int[], Integer>>) ints -> {
                double min=-1;
                int minIndex=0;
                for(int j=0;j<centers.size();j++){
                    double[] center=centers.get(j);
                    double a = calcDistance(ints._2, center);
                    if(min==-1 || min>a){
                        min=a;
                        minIndex=j;
                    }
                }
                return new Tuple2<>(minIndex,new Tuple3<>(ints._1,ints._2,1));
            });

            //center下标 值之和 以及 值个数
            List<Tuple2<Integer,Tuple3<String,int[],Integer>>> list=categoriedData.reduceByKey((Function2<Tuple3<String,int[], Integer>, Tuple3<String,int[], Integer>, Tuple3<String,int[], Integer>>) (v1, v2) -> {
                int[]r=new int[v1._2().length];
                for(int i=0;i<r.length;i++){
                    r[i]=v1._2()[i]+v2._2()[i];
                }
                return new Tuple3<>(v1._1(),r,v1._3()+v2._3());
            }).collect();

            List<double[]> newCenters=new ArrayList<>();
            List<Double> distances=new ArrayList<>();
            for(Tuple2<Integer,Tuple3<String,int[],Integer>> t:list){
                int r[]=t._2._2();
                double rd[]=new double[r.length];
                for(int i=0;i<r.length;i++){
                    rd[i]=r[i]/t._2._3().doubleValue();
                }

                newCenters.add(rd);
                distances.add(calcDistance(rd,centers.get(t._1)));
            }
            totalDistance=totalDistance(distances);

            centers=newCenters;
        }while (totalDistance>80);
        System.out.println(totalDistance);
        return categoriedData.map(new Function<Tuple2<Integer, Tuple3<String, int[], Integer>>, Tuple3<String, int[], Integer>>() {
            @Override
            public Tuple3<String, int[], Integer> call(Tuple2<Integer, Tuple3<String, int[], Integer>> v1) throws Exception {
                return new Tuple3<>(v1._2._1(),v1._2._2(),v1._1());
            }
        });
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

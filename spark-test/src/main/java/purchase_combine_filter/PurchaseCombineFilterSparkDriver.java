package purchase_combine_filter;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import scala.Tuple4;

import java.util.*;

public class PurchaseCombineFilterSparkDriver  {

    final static Logger logger= LoggerFactory.getLogger(PurchaseCombineFilterSparkDriver.class);

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().set("spark.master", "local")//spark://192.168.1.103:7077
                .set("spark.app.name", "test");
        JavaSparkContext sc = new JavaSparkContext(conf); //
        String preffix = "file:///usr/local/Cellar/hadoop";
        JavaRDD<String> input = sc.textFile(preffix + "/test/purchase_combine_input.txt", 1);
        final int support = 3;
        final double confidence=0.7;

        class MapFunction implements Function<String, Set<DataTuples>>{
            int n;

            public MapFunction(int n){
                this.n=n;
            }

            @Override
            public Set<DataTuples> call(String v1) throws Exception {
                String s = v1.toString();
                Set<DataTuples> list = new HashSet<>();
                if (org.apache.commons.lang.StringUtils.isEmpty(s)) {
                    return list;
                }
                String[] keys = s.split(",");
                if(keys.length<n){
                    return list;
                }

                list.add(new DataTuples(Arrays.copyOfRange(keys, 0, n)));
                for (int i = n; i < keys.length; i++) {
                    Set<DataTuples> newSet = new HashSet<>();
                    for (DataTuples aa : list) {
                        String[] a = aa.getArr();
                        for (int j = 0; j < a.length; j++) {
                            String[] tmp = a.clone();
                            tmp[j] = keys[i];
                            newSet.add(new DataTuples(tmp));
                        }
                    }
                    list.addAll(newSet);
                }

                return list;
            }
        }
        int n = 1;
        JavaPairRDD<DataTuples,Integer> preMap=null;
        do {
            JavaRDD<Set<DataTuples>> pairs = input.map(new MapFunction(n));

            JavaPairRDD<DataTuples,Integer> map=pairs.flatMapToPair(new PairFlatMapFunction<Set<DataTuples>, DataTuples, Integer>() {
                @Override
                public Iterator<Tuple2<DataTuples, Integer>> call(Set<DataTuples> dataTuples) throws Exception {
                    Set<Tuple2<DataTuples,Integer>> set=new HashSet<>(dataTuples.size());
                    for(DataTuples dt:dataTuples){
                        set.add(new Tuple2<>(dt,1));
                    }
                    return set.iterator();
                }
            })
            .reduceByKey(new Function2<Integer, Integer, Integer>() {
                @Override
                public Integer call(Integer v1, Integer v2) throws Exception {
                    return v1+v2;
                }
            }).filter(new Function<Tuple2<DataTuples, Integer>, Boolean>() {
                @Override
                public Boolean call(Tuple2<DataTuples, Integer> v1) throws Exception {
                    return v1._2()>=support;
                }
            });

            if(n>1){
                JavaRDD<Tuple4<DataTuples, String, Double, Integer>> results=map.flatMapToPair((PairFlatMapFunction<Tuple2<DataTuples, Integer>, DataTuples, Tuple2<String, Integer>>) dataTuplesIntegerTuple2 -> {
                    Set<Tuple2<DataTuples, Tuple2<String, Integer>>> mids=new HashSet<>();

                    String[] arr=dataTuplesIntegerTuple2._1.getArr();
                    for(int i=0;i<arr.length;i++){
                        DataTuples midDt = getDataTuples(arr, i);
                        mids.add(new Tuple2<>(midDt,new Tuple2<>(arr[i],dataTuplesIntegerTuple2._2)));
                    }
                    return mids.iterator();
                }).groupWith(preMap).flatMap(new FlatMapFunction<Tuple2<DataTuples, Tuple2<Iterable<Tuple2<String, Integer>>, Iterable<Integer>>>, Tuple4<DataTuples, String, Double, Integer>>() {
                    @Override
                    public Iterator<Tuple4<DataTuples, String, Double, Integer>> call(Tuple2<DataTuples, Tuple2<Iterable<Tuple2<String, Integer>>, Iterable<Integer>>> dataTuplesTuple2Tuple2) throws Exception {
                        double s=0;
                        for(Integer si:dataTuplesTuple2Tuple2._2._2){
                            s=si;
                        }
                        Set<Tuple4<DataTuples, String, Double, Integer>> result=new HashSet<>();
                        if(s>0){
                            Iterator<Tuple2<String,Integer>> itr=dataTuplesTuple2Tuple2._2._1.iterator();
                            while(itr.hasNext()){
                                Tuple2<String,Integer> a=itr.next();
                                double c=a._2/s;
                                if(c>=confidence){
                                    result.add(new Tuple4<DataTuples, String, Double, Integer>(dataTuplesTuple2Tuple2._1,a._1,c,a._2));
                                }
                            }
                        }

                        return result.iterator();
                    }
                });

                List<Tuple4<DataTuples, String, Double, Integer>> tmpResult=results.collect();
                System.out.println(String.format("第%d层",n));
                for(Tuple4<DataTuples, String, Double, Integer> tmp:tmpResult){
                    System.out.print(String.format("(%s->%s,%s,%s) ",tmp._1(),tmp._2(),tmp._3(),tmp._4()));
                }
            }
            preMap=map;
            n++;
        }while(preMap.count()>0);

    }

    private static DataTuples getDataTuples(String[] arr, int i) {
        String[] mid=new String[arr.length-1];
        for(int j=0;j<i;j++){
            mid[j]=arr[j];
        }
        for(int j=i+1;j<arr.length;j++){
            mid[j-1]=arr[j];
        }
        return new DataTuples(mid);
    }


}

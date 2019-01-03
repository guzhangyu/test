package markov;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;
import scala.Tuple3;

import java.text.SimpleDateFormat;
import java.util.*;

public class MarkovSparkDriver {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().set("spark.master", "local")//spark://192.168.1.103:7077
                .set("spark.app.name", "test");
        JavaSparkContext sc = new JavaSparkContext(conf); //
        String preffix = "file:///usr/local/Cellar/hadoop";
        JavaRDD<String> input = sc.textFile(preffix + "/test/markov_input.txt", 1);

        List<Tuple2<String,Integer>> results=input.map((Function<String, Tuple3<String, Date, Integer>>) s -> {
            String ss[]=s.split(",");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return new Tuple3<String, Date, Integer>(ss[0],sdf.parse(ss[2]),Integer.parseInt(ss[3]));
        })//数据解析
        //.sortBy((Function<Tuple3<String, Date, Integer>, Date>) v1 -> v1._2(),true,1) //根据时间排序
        .groupBy((Function<Tuple3<String, Date, Integer>, String>) v1 -> v1._1()) //根据顾客分组
        .flatMapToPair((PairFlatMapFunction<Tuple2<String, Iterable<Tuple3<String, Date, Integer>>>, String, Integer>) stringIterableTuple2 -> {
            Integer preV=null;
            Date preDate=null;
            String preK=null;
            Map<String,Integer> result=new HashMap<>();
            List<Tuple2<Date, Integer>> list=new ArrayList<>();
            for(Tuple3<String, Date, Integer> value:stringIterableTuple2._2){
                list.add(new Tuple2<>(value._2(),value._3()));
            }
            Collections.sort(list, Comparator.comparing(Tuple2::_1));
            for(Tuple2<Date, Integer> value:list){
                Date newDate=value._1();
                int newV=value._2();
                if(preV!=null){
                    long days=(newDate.getTime()-preDate.getTime())/1000/3600/24;
                    String k=String.format("%s%s",days<30?"S":(days<60?"M":"L"),preV<0.9*newV?"L":(preV<1.1*newV?"E":"G"));
                    if(preK!=null){
                        String key=preK+","+k;
                        Integer v=result.get(key);
                        if(v==null){
                            v=0;
                        }
                        result.put(key,v+1);
                    }

                    preK=k;
                }

                preV=newV;
                preDate=newDate;
            }
            List<Tuple2<String,Integer>> resultt= new ArrayList<>();
            for(Map.Entry<String,Integer> entry:result.entrySet()){
                resultt.add(new Tuple2<>(entry.getKey(),entry.getValue()));
            }
            return resultt.iterator();
        })//计算每一个顾客的状态转移次数
        .reduceByKey((Function2<Integer, Integer, Integer>) (v1, v2) -> v1+v2).collect();
        System.out.println("result:"+results);

    }
}

package item_recommend.user_recommend;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.util.*;

public class UserRecommendSparkDriver {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().set("spark.master", "local")//spark://192.168.1.103:7077
                .set("spark.app.name", "test");
        JavaSparkContext sc = new JavaSparkContext(conf); //
        String preffix = "file:///usr/local/Cellar/hadoop";
        JavaRDD<String> input = sc.textFile(preffix + "/test/ir_user_recommend_input.txt", 1);

        JavaPairRDD<String, Tuple2<String, String>> pairRDD=input.flatMapToPair(new PairFlatMapFunction<String, String, Tuple2<String,String>>() {
            @Override
            public Iterator<Tuple2<String, Tuple2<String, String>>> call(String s) throws Exception {
                List<Tuple2<String, Tuple2<String, String>>> result=new ArrayList<>();
                if(StringUtils.isBlank(s)){
                    return result.iterator();
                }
                String vs[]=s.split("\\s+");
                String v1s[]=vs[1].split(",");


                for(int i=0;i<v1s.length;i++){
                    result.add(new Tuple2<>(vs[0],new Tuple2<>(v1s[i],"-1")));
                    result.add(new Tuple2<>(v1s[i],new Tuple2<>(v1s[0],"-1")));
                    for(int j=i+1;j<v1s.length;j++){
                        result.add(new Tuple2<>(v1s[i],new Tuple2<>(v1s[j],vs[0])));
                        result.add(new Tuple2<>(v1s[j],new Tuple2<>(v1s[i],vs[0])));
                    }
                }
                return result.iterator();
            }
        });

        JavaPairRDD<String, List<Tuple2<String, List<String>>>> result= pairRDD.groupByKey().flatMapToPair(new PairFlatMapFunction<Tuple2<String, Iterable<Tuple2<String, String>>>,  String, List<Tuple2<String,List<String>>>>() {
            @Override
            public Iterator<Tuple2<String, List<Tuple2<String, List<String>>>>> call(Tuple2<String, Iterable<Tuple2<String, String>>> stringIterableTuple2) throws Exception {
                Map<String,List<String>> commonFriendMap=new HashMap<>();
                for(Tuple2<String,String> rec:stringIterableTuple2._2){
                    String rec1=rec._1;
//                    if(rec._2.equals("-1")){
//                        commonFriendMap.put(rec1,null);
//                    }else if(commonFriendMap.containsKey(rec1)){
//                        List<String> commonFriends=commonFriendMap.get(rec1);
//                        if(commonFriends!=null){
//                            commonFriends.add(rec._2);
//                        }
//                    }else{
//                        List<String> commonFriends=new ArrayList<>();
//                        commonFriendMap.put(rec1,commonFriends);
//                        commonFriends.add(rec._2);
//                    }
                    List<String> commonFriends=commonFriendMap.get(rec1);
                    if(commonFriends==null){
                        commonFriends=new ArrayList<>();
                        commonFriendMap.put(rec1,commonFriends);
                    }
                    commonFriends.add(rec._2);
                }

                List<Tuple2<String, List<String>>> result=new ArrayList<>();
                for(Map.Entry<String,List<String>> entry:commonFriendMap.entrySet()){
                    if(entry.getValue().contains("-1")){
                        continue;
                    }
                    result.add(new Tuple2<>(entry.getKey(),entry.getValue()));
                }
                result.sort(new Comparator<Tuple2<String, List<String>>>() {
                    @Override
                    public int compare(Tuple2<String, List<String>> o1, Tuple2<String, List<String>> o2) {
                        return o1._2.size()-o2._2.size();
                    }
                });
                return Arrays.asList(new Tuple2<>(stringIterableTuple2._1,result)).iterator();
            }
        });

        System.out.println(result.collect());
    }
}

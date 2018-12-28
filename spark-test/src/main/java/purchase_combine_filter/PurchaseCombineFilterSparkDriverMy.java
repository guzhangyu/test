package purchase_combine_filter;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PurchaseCombineFilterSparkDriverMy  {

    static Map<DataTuples, Integer> result=null;

    final static Logger logger= LoggerFactory.getLogger(PurchaseCombineFilterSparkDriver.class);

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().set("spark.master", "spark://192.168.1.103:7077")//spark://192.168.1.103:7077
                .set("spark.app.name", "test");
        JavaSparkContext sc = new JavaSparkContext(conf); //
        String preffix = "file:///usr/local/Cellar/hadoop";
        JavaRDD<String> input = sc.textFile(preffix + "/test/purchase_combine_input.txt", 1);
        final int support = 3;
        final double confidence=0.7;
        //1
//        JavaRDD<List<Tuple2<String,Integer>>> pairRDD1=input.map(new Function<String, List<Tuple2<String, Integer>>>() {
//            @Override
//            public List<Tuple2<String, Integer>> call(String v1) throws Exception {
//                List<Tuple2<String, Integer>> list=new ArrayList<>();
//                if(StringUtils.isBlank(v1)){
//                    return list;
//                }
//                for(String s:v1.split(",")){
//                    list.add(new Tuple2<>(s,1));
//                }
//                return list;
//            }
//        });
//
//        Map<String, Integer> result=pairRDD1.aggregate(new HashMap<String, Integer>(), new Function2<HashMap<String, Integer>, List<Tuple2<String, Integer>>, HashMap<String, Integer>>() {
//            @Override
//            public HashMap<String, Integer> call(HashMap<String, Integer> v1, List<Tuple2<String, Integer>> v2) throws Exception {
//                for (Tuple2<String, Integer> v2i : v2) {
//                    String k = v2i._1;
//                    if (v1.containsKey(k)) {
//                        v1.put(k, v1.get(k) + v2i._2);
//                    } else {
//                        v1.put(k, 1);
//                    }
//                }
//                return v1;
//            }
//        }, new Function2<HashMap<String, Integer>, HashMap<String, Integer>, HashMap<String, Integer>>() {
//            @Override
//            public HashMap<String, Integer> call(HashMap<String, Integer> v1, HashMap<String, Integer> v2) throws Exception {
//                for(Map.Entry<String,Integer> entry: v2.entrySet()){
//                    String k=entry.getKey();
//                    if(v1.containsKey(k)){
//                        v1.put(k,v1.get(k)+entry.getValue());
//                    }else{
//                        v1.put(k,entry.getValue());
//                    }
//                }
//                return v1;
//            }
//        });
//        System.out.println(result);



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
//                logger.debug(String.format("---n:%d",n));
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

                if(result!=null){
                    filter(result.keySet(),list);
                }
                return list;
            }
        }
        int n = 1;
        List<Map<DataTuples,Integer>> results=new ArrayList<>();

        do {
            JavaRDD<Set<DataTuples>> pairs = input.map(new MapFunction(n));

            result= pairs.aggregate(new HashMap<DataTuples, Integer>(), new Function2<HashMap<DataTuples, Integer>, Set<DataTuples>, HashMap<DataTuples, Integer>>() {
                @Override
                public HashMap<DataTuples, Integer> call(HashMap<DataTuples, Integer> v1, Set<DataTuples> v2) throws Exception {
                    for (DataTuples dataTuples : v2) {
                        if (v1.containsKey(dataTuples)) {
                            v1.put(dataTuples, v1.get(dataTuples) + 1);
                        } else {
                            v1.put(dataTuples, 1);
                        }
                    }
                    return v1;
                }
            }, new Function2<HashMap<DataTuples, Integer>, HashMap<DataTuples, Integer>, HashMap<DataTuples, Integer>>() {
                @Override
                public HashMap<DataTuples, Integer> call(HashMap<DataTuples, Integer> v1, HashMap<DataTuples, Integer> v2) throws Exception {
                    for (Map.Entry<DataTuples, Integer> entry : v2.entrySet()) {
                        if (v1.containsKey(entry.getKey())) {
                            v1.put(entry.getKey(), v1.get(entry.getKey()) + entry.getValue());
                        } else {
                            v1.put(entry.getKey(), entry.getValue());
                        }
                    }
                    return v1;
                }
            });


            Iterator<Map.Entry<DataTuples, Integer>> itr = result.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<DataTuples, Integer> entry = itr.next();
                if (entry.getValue() < support) {
                    itr.remove();
                }
            }
            System.out.println(String.format("n:%s",n));
            System.out.println(result);
            n++;
            results.add(result);
        }while(result.size()>0);

        System.out.println(results);

        for(int i=1;i<results.size();i++){
            System.out.println(String.format("第%d层",i));
            for(Map.Entry<DataTuples,Integer> entry:results.get(i).entrySet()){
                String[] arr=entry.getKey().getArr();
                for(int j=0;j<arr.length;j++){
                    DataTuples midDt = getDataTuples(arr, j);
                    double v=entry.getValue().doubleValue()/results.get(i-1).get(midDt);
                    if(v>=confidence){
                        System.out.print(String.format("(%s->%s,%s) ",midDt,arr[j],v));
                    }
                }
            }
            System.out.println();
        }
    }

    public static void filter(Set<DataTuples> ins,Set<DataTuples> data){
        Iterator<DataTuples> itr=data.iterator();
        out:while(itr.hasNext()){
            DataTuples dataTuples=itr.next();

            Set<DataTuples> mids=new HashSet<>();
            String[] arr=dataTuples.getArr();
            for(int i=0;i<arr.length;i++){
                DataTuples midDt = getDataTuples(arr, i);
                mids.add(midDt);
            }

            for(DataTuples dt:ins){
                if(mids.contains(dt)){
                    continue out;
                }
            }

            itr.remove();
        }
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

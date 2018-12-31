package taobao_item_recommend;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.python.PythonFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.PythonUDF;
import org.apache.spark.streaming.StreamingContext;
import scala.Function0;
import scala.Function1;
import scala.Tuple3;

import java.util.stream.Stream;

public class TaobaoItemRecommendDriver {

    public static void main(String[] args) {
//        StreamingContext a= StreamingContext.getOrCreate("", new Function0<StreamingContext>() {
//            @Override
//            public StreamingContext apply() {
//                SparkConf conf=new SparkConf().set("spark.master","spark://master:7077")
//                        .set("spark.app.name","taobaoItemRecommend");
//                SparkContext sc=new SparkContext(conf);
//                StreamingContext streamingContext=new StreamingContext("",sc);
//
//                return null;
//            }
//        },new Configuration(),false);

        SparkConf conf = new SparkConf()//.set("spark.master", "spark://172.31.7.198:7077")//spark://192.168.1.103:7077
                .set("spark.app.name", "test");
//        JavaSparkContext sc = new JavaSparkContext(conf); //
        SparkContext sparkContext=new SparkContext(conf);
        String preffix = "hdfs://master:8020/tmp.db/item_recommend/";

        SparkSession sparkSession=new SparkSession(sparkContext);
        Dataset<Row> b=sparkSession.read().parquet(preffix+"b");
//        b.groupBy(b.col("user_id"),b.col("user_geohash"),b.col("item_category"))
//                .flatMapGroupsInPandas(new PythonUDF("b",new PythonFunction()));


//        JavaSparkContext sparkContext=new JavaSparkContext(conf);
//        b.rdd().groupBy(new Function1<Row, Object>() {
//            @Override
//            public Object apply(Row v1) {
//                return null;
//            }
//
//            @Override
//            public <A> Function1<Row, A> andThen(Function1<Object, A> g) {
//                return super.andThen(g);
//            }
//
//            @Override
//            public <A> Function1<A, Object> compose(Function1<A, Row> g) {
//                return super.compose(g);
//            }
//        })

    }
}

package taobao_item_recommend;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.StreamingContext;
import scala.Function0;

import java.util.stream.Stream;

public class TaobaoItemRecommendDriver {

    public static void main(String[] args) {
        StreamingContext a= StreamingContext.getOrCreate("", new Function0<StreamingContext>() {
            @Override
            public StreamingContext apply() {
                SparkConf conf=new SparkConf().set("spark.master","spark://master:7077")
                        .set("spark.app.name","taobaoItemRecommend");
                SparkContext sc=new SparkContext(conf);
                StreamingContext streamingContext=new StreamingContext("",sc);

                return null;
            }
        },new Configuration(),false);

    }
}

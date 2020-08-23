package live.item_recommend1;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SparkSession;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;

public class NeualNetworkLearningJava {

    public static void main(String[] args) {
        SparkConf conf=new SparkConf().setMaster("spark://master:7077")
                .set("spark.ui.enabled","false")
                .set("spark.cleaner.referenceTracking","false")
                .setAppName("ItemRecommend1");
        SparkContext sc=new SparkContext(conf);
        SparkSession spark=new SparkSession(sc);

//        DataFrame user_behaviors_test_fullFeatured=spark.read().parquet("/item_recommend1_totalB/user_behaviors_test_fullFeatured.parquet");

//        MultiLayerConfiguration conf=new Neural

    }
}

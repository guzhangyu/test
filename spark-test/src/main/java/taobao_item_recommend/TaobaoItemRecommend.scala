package taobao_item_recommend

import java.util

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

object TaobaoItemRecommend {
  def main(args:Array[String]): Unit ={
    val conf = new SparkConf().set("spark.app.name", "test")
    val sparkContext = new SparkContext(conf)
    val preffix = "hdfs://master:8020/tmp.db/item_recommend/"

    val spark = new SparkSession(sparkContext)
    val b = spark.read.parquet(preffix + "b")
    val left_items=spark.read.parquet(preffix + "left_items")
//    b.groupBy(b.col("user_id"),b.col("user_geohash"),b.col("item_category"))
    //        b.groupBy(b.col("user_id"),b.col("user_geohash"),b.col("item_category"))
    //                .flatMapGroupsInPandas(new PythonUDF("b",new PythonFunction()));

    val c=b.rdd.keyBy[Tuple3[Int,String,String]](r=>(r.getAs[Int]("user_id"),r.getAs[String]("user_geohash"),r.getAs[String]("item_category")))
    import scala.collection.JavaConverters._

    val d=c.groupByKey().flatMap((a:Tuple2[Tuple3[Int,String,String],Iterable[Row]]) => {
      val (t,itr)=a
      val (_,user_geohash,item_category)=t
      val items=left_items.filter(String.format("user_geohash='%s' and item_category='%s' ",user_geohash,item_category)).collect()
      val result=new util.ArrayList[Row]()
      for(row <- itr){
        val item_id=items(0).getAs[Long]("item_id")
        if(items.length>1){
          items.drop(1)
        }
        result.add(Row.fromTuple((row.getAs[Long]("user_id"),item_id,row.getAs[Double]("rating"),user_geohash,item_category)))
      }
      result.asScala
    });
  }
}

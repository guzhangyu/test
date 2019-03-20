import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

object ALSTest {

  def main(args: Array[String]): Unit = {
    val spark=SparkSession.builder().master("local").appName("predict").getOrCreate()
    import spark.implicits._

    val ratingsData1=spark.read.textFile("file:///Users/zhangyugu/Downloads/ml-latest-small/ratings.csv")
    val ratingsData=ratingsData1.map(a=>{
        val arr=a.split(",")
        Rating(arr(0).toInt,arr(1).toInt,arr(3).toDouble)
      })

    val tempPartitions=ratingsData.randomSplit(Array(0.7,0.3))
    val model:MatrixFactorizationModel=ALS.train(tempPartitions(0).cache().rdd,20,10,0.01)
    model.predict(1,1)

  }


}

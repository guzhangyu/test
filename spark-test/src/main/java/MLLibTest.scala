import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.linalg.Vectors

object MLLibTest {

  def main(args: Array[String]): Unit = {

    val conf=new SparkConf().setMaster("local")
      .set("spark.ui.enabled","false").set("spark.cleaner.referenceTracking","false").setAppName("t")
    val sc=new SparkContext(conf)

    val data=sc.textFile("/Users/zhangyugu/IdeaProjects/spark-master/data/mllib/ridge-data/lpsa.data")
    val parsedData =data.map{
      line=>;val parts=line.split(",")
        LabeledPoint(parts(0).toDouble,Vectors.dense(parts(1).split(" ").map(_.toDouble)) )
    }.cache()

    //训练模型
    val numIterations=100
    val model=LinearRegressionWithSGD.train(parsedData,numIterations)

    //使用模型进行预估，并评估模型
    val valuesAndPreds = parsedData.map{
      point=>;
        val prediction=model.predict(point.features)
        (point.label,prediction)
    }
    val MSE=valuesAndPreds.map{case(v,p) => ;math.pow(v-p,2)}.mean()
    println("training Mean Squared Error = "+MSE)
  }
}

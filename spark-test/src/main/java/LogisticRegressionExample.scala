import java.text.SimpleDateFormat
import java.util.Date

import org.apache.avro.SchemaBuilder.ArrayBuilder
import org.apache.spark.mllib.classification.{LogisticRegressionWithLBFGS, LogisticRegressionWithSGD}
import org.apache.spark.mllib.evaluation.{BinaryClassificationMetrics, RegressionMetrics}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.optimization.{L1Updater, SquaredL2Updater}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object LogisticRegressionExample {

  def main(args:Array[String]): Unit ={
    val conf=new SparkConf().setMaster("local").setAppName("TESTT")
    val sc=new SparkContext(conf)

    var num_iterations=50
    var train_algorithm ="SGD"
    var train_file_path="file:///usr/local/Cellar/hadoop/test/LogisticRegressionExample/"
    var training_file_name=train_file_path+"training_test.txt"
//    var clear_threshold_test = false
    var reg_type="L1"

    val training_data_raw=sc.textFile(training_file_name)
    val training_data_expanded=training_data_raw.map(line=>{
      val lines=line.split(" ");
      (lines(2),(lines(0),lines(1),lines(3)))
    }).join[Array[String]](
      sc.textFile(train_file_path+"queryid_tokensid.txt").map(
        line=> {
          val lines = line.split(" ");
          val tokens = lines(1).split("\\|");
          (lines(0),tokens)
        }
      )
    ).map(a=>(a._2._1._3,(a._1,a._2._1,a._2._2)))
      .join[Tuple2[String,String]](
      sc.textFile(train_file_path+"userid_profile1_test.txt").map(
        line=> {
          val lines = line.split("\\s");
          (lines(0),(lines(1),lines(2)))
        }
      )
    ).flatMap[(String,(String,((String,(String,String,String),Array[String]),(String,String))))](p=>{
      val a=new Array[(String,(String,((String,(String,String,String),Array[String]),(String,String))))](p._2._1._3.length)
      var i=0;
      for(pi <- p._2._1._3){
        a(i)=(pi,p)
        i=i+1
      }
      a
    })

    val maxSize=training_data_expanded.count()
    val train_instance_flat=training_data_expanded.zipWithIndex()
      . //token,(user_id,
      // (query_id,
      //    ( (lines(0),lines(1),lines(3)),tokens ) ,(gender,age)
      // )
      // )
      map(
      a=> {
        val data=a._1._2._2._2
        ((a._1._1, a._1._2._1, a._1._2._2._1._1), (data._1, data._2, a._2))
      }
    )//(token,user_id,query_id) (点击 曝光 index)
      .filter(a=>a._2._1.toInt<=a._2._2.toInt && a._2._2.toInt>0)
        .groupByKey()
      .flatMap(a=>{
        var s=""
        var click=0
        var impression=0
        val feature_index_arr=mutable.ArrayBuffer[Int]()
        val feature_value_arr = mutable.ArrayBuffer[Double]()
        for(i<-a._2){
          s=s+" "+i._3+" 1"
          click=i._1.toInt
          impression=i._2.toInt
          feature_index_arr += i._3.intValue()
          feature_value_arr+=1
        }

        val arr=new Array[LabeledPoint](impression)
        for(i<- click until impression ){
          arr(i-click)=LabeledPoint(0,
            Vectors.sparse(maxSize.intValue(),feature_index_arr.toArray[Int],feature_value_arr.toArray[Double]))
        }
        for(i<- 0 until click ){
          arr(i+impression-click)=LabeledPoint(1,
            Vectors.sparse(maxSize.intValue(),feature_index_arr.toArray[Int],feature_value_arr.toArray[Double]))
        }
        arr
      })
//        .map(a=>{
//          var s=""
//          var click=""
//          var impression=""
//          for(i<-a._2){
//            s=s+" "+i._3+" 1"
//            click=i._1
//            impression=i._2
//          }
//          (a._1,(click,impression,s))
//        }).filter(a=>a._2._1.toInt<=a._2._2.toInt && a._2._2.toInt>0)
//        .flatMap(arrWithIndex=>{
//          val feature_index_arr=arrWithIndex
//          Vectors.sparse(maxSize,)
//        })

    val examples = train_instance_flat.randomSplit(Array(0.8,0.2),seed=1L)
    val training_set=examples(0).cache()
    val test_set=examples(1).cache()

    val num_training=training_set.count()
    val num_test=test_set.count()
    println(s"Training: $num_training, Testing: $num_test.")

    val updater=reg_type match{
      case "L1" => new L1Updater()
      case "L2" => new SquaredL2Updater()
    }

    val model = train_algorithm match{
      case "LBFGS" =>
        val algorithm = new LogisticRegressionWithLBFGS()
        algorithm.optimizer
        .setNumIterations(num_iterations)
        .setUpdater(updater)
        algorithm.run(training_set)

      case "SGD" =>
        val algorithm = new LogisticRegressionWithSGD()
        algorithm.optimizer
          .setNumIterations(num_iterations)
          .setUpdater(updater)
        algorithm.run(training_set)
    }

    model.clearThreshold()

    val weight_count = model.weights.size
    println(s"\nModel weights count: ${weight_count}")

    val prediction = model.predict(test_set.map(_.features))
    val score_and_labels = prediction.zip(test_set.map(_.label))
    val metrics=new BinaryClassificationMetrics(score_and_labels)
    println(s"\nTest areaUnderROC = ${metrics.areaUnderROC()}")

    val metrics2=new RegressionMetrics(score_and_labels)
    println(s"\nTest meanAbsoluteError = ${metrics2.meanAbsoluteError}")

    val date_format = new SimpleDateFormat("yyyyMMddHHmm")
    val model_path = train_file_path+"model_"+date_format.format(new Date())
    model.save(sc,model_path)
//    sc.stop()

    //    val feature_index_rdd=training_data_expanded.reduceByKey((a1,a2)=>a1).zipWithIndex()
//    val train_instance=training_data_expanded.join(feature_index_rdd)
//        .map(a=>{
//
//        })
  }

}

//training_test
//点击 曝光 查询词id(queryid) 用户id 其他属性
//
//queryid_tokensid
//queryid token1 | token2
//
//userid_profile1_test
//userid gender age

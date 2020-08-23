package live.item_recommend1

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.{ConvolutionLayer, DenseLayer, SubsamplingLayer}
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.spark.impl.multilayer.SparkDl4jMultiLayer
import org.deeplearning4j.spark.impl.paramavg.ParameterAveragingTrainingMaster
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Nesterovs

object NeualNetworkLearning {

  def main(args: Array[String]): Unit = {
//    val conf=new SparkConf().setMaster("spark://master:7077")
//      .set("spark.ui.enabled","false")
//      .set("spark.cleaner.referenceTracking","false")
//      .setAppName("ItemRecommend1")
//    val sc=new SparkContext(conf)
//    val spark=new SparkSession(sc)
//
//    val user_behaviors_test_fullFeatured=spark.read.parquet("/item_recommend1_totalB/user_behaviors_test_fullFeatured.parquet")

    val tm = new ParameterAveragingTrainingMaster.Builder(1)
      .workerPrefetchNumBatches(2)
      .averagingFrequency(3)
      .batchSizePerWorker(4)
      .build()

    val neuralNetConfiguration=new NeuralNetConfiguration.Builder()
      .seed(42)
      .l2(0.005)
      .activation(Activation.RELU)
      .weightInit(WeightInit.XAVIER)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .updater(new Nesterovs(0.0001,0.9))
      .list()
      .layer(0,new DenseLayer.Builder().nIn(22).nOut(44).build())
      .layer(1,new DenseLayer.Builder().nIn(44).nOut(11).build())
      .layer(2,new DenseLayer.Builder().nIn(11).nOut(1).build())
      .build()

//    val sparkNetwork = new SparkDl4jMultiLayer(sc,neuralNetConfiguration,tm)
//    val trainData=user_behaviors_test_fullFeatured.rdd.map
//    for( i<- 0 to 10){
//      sparkNetwork.fit(trainData)
//    }
//
//    sparkNetwork.doEvaluation(trainData,32,new Evaluation(4))[0]

//      .layer(0,new ConvolutionLayer.Builder(Array(5,5),Array(1,1),Array(0,0)).name("cnn1")
//          .nIn(4).nOut(50).biasInit(0).build())
//      .layer(1,new SubsamplingLayer.Builder(Array(2,2),Array(2,2)).name("maxpool1").build())
//      .layer(2,new ConvolutionLayer.Builder(Array(5,5),Array(5,5),Array(1,1)).name("cnn2")
//          .nOut(100).biasInit(0).build())

  }
}

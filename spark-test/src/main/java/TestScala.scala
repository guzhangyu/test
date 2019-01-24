import java.util

import com.google.common.base.Charsets
import org.apache.hadoop.io.{LongWritable, Text}

import org.apache.hadoop.mapreduce.lib.input.{LineRecordReader, TextInputFormat}
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
//import mlib.

abstract class Tree

object TestScala {

  def main(args: Array[String]): Unit = {
//    print("a")
    val conf=new SparkConf().setMaster("local")
      .set("spark.ui.enabled","false").set("spark.cleaner.referenceTracking","false").setAppName("t")
    val sc=new SparkContext(conf)
//    println(sc)
//    val sqlContext=SQLContext(sc)
//    sqlContext
//    val spark=SparkSession.builder().config(conf).getOrCreate()
//    val a1c:org.apache.spark.sql.Dataset[String]=spark.read.textFile("a1.txt")
//    val a2c=spark.read.csv(a1c)
//    a2c.show()
//    print("test----")
//    print(a2c.collect())
//    println(a2c)

      import org.apache.spark.streaming.{Seconds, StreamingContext}
//      val ssc=StreamingContext.getOrCreate("hdfs://master:8020/user/zhangyugu/ssc",()=>{
//          val ssc=new StreamingContext(sc,Seconds(5))
//          ssc.checkpoint("hdfs://master:8020/user/zhangyugu/ssc")
//
//          val lines=ssc.socketTextStream("localhost",9999)
//          val words=lines.flatMap(_.split(" "))
//          val pairs=words.map(word=>(word,1))
//          val joinedDStream =pairs.transform(rdd=>{rdd.join(rdd)})
//          //      val joinedDStream =pairs.transform({_:org.apache.spark.rdd.RDD[Tuple2[String,Int]].join(_)})
//
//          //      def updateFunction(newValues:Seq[Int],runningCount:Option[Int]):Option[Int]= {
//          //          val preCount = runningCount.getOrElse(0)
//          //          val newCount = newValues.sum
//          //          Some(newCount+preCount)
//          //      }
//          //
//          //      val runningCounts=pairs.updateStateByKey[Int](updateFunction _)
//
////          pairs.print(10)
//          pairs.countByWindow(Seconds(10),Seconds(10)).foreachRDD((r,t)=>{
//              println("window:"+t+":"+r.take(10))
//          })
//          pairs.reduceByKeyAndWindow((x:Int,y:Int)=>x+y,Seconds(10),Seconds(10)).foreachRDD((r,t)=>{
//              println("reduceByKeyAndWindow:"+t+":"+r.take(10))
//          })
//          ssc
//      })

      import org.apache.hadoop.io.{LongWritable, Text}
      import org.apache.hadoop.mapreduce.lib.input.{LineRecordReader, TextInputFormat}
      import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}
      import com.google.common.base.Charsets

//      class WebIpInputFormat extends TextInputFormat{
//         override def createRecordReader(split: InputSplit, context: TaskAttemptContext): RecordReader[LongWritable, Text] = {
//             val delimiter = context.getConfiguration.get("textinputformat.record.delimiter")
//             return new LineRecordReader(if(null != delimiter) delimiter.getBytes(Charsets.UTF_8) else null){
//                 override def getCurrentValue: Text = {
//                     val v=super.getCurrentValue
//                     if(v!=null){
//                         v.set(v.toString().split("-")(0))
//                     }
//                     return v
//                 }
//             }
//         }
//      }
//      val ssc=StreamingContext.getOrCreate("hdfs://master:8020/user/zhangyugu/ssc1",()=> {
//          val ssc = new StreamingContext(sc, Seconds(5))
//          ssc.checkpoint("hdfs://master:8020/user/zhangyugu/ssc1")
//          ssc.fileStream[LongWritable,Text,WebIpInputFormat]("hdfs://master:8020/user/zhangyugu/weblog")
//              .map((_._2.toString)).countByValue().print()
//          ssc
//      })


          val ssc=StreamingContext.getOrCreate("hdfs://master:8020/user/zhangyugu/ssc4",()=> {
              val ssc = new StreamingContext(sc, Seconds(5))
              ssc.checkpoint("hdfs://master:8020/user/zhangyugu/ssc4")
              ssc.textFileStream("hdfs://master:8020/user/zhangyugu/weblog")
                  .map(_.split("-")(0)).countByValue()
                .transform(rdd=>rdd.sortBy(_._2,true)).print()
              ssc
          })



      import org.apache.hadoop.mapred
      import org.apache.hadoop.mapred._
      class WebIpInputFormat1 extends org.apache.hadoop.mapred.TextInputFormat{
          override def getRecordReader(genericSplit: mapred.InputSplit, job: JobConf, reporter: Reporter): mapred.RecordReader[LongWritable, Text] = {
              reporter.setStatus(genericSplit.toString)
              val delimiter = job.get("textinputformat.record.delimiter")
              return new  org.apache.hadoop.mapred.LineRecordReader(job, genericSplit.asInstanceOf[FileSplit], if(null != delimiter) delimiter.getBytes(Charsets.UTF_8) else null){
                  override def next(key:LongWritable, value:Text)={
                      val r=super.next(key,value)
                      if(value!=null) value.set(value.toString().split("-")(0))
                      r
                  }
              }
          }
      }

      sc.hadoopFile[LongWritable,Text,WebIpInputFormat1]("hdfs://master:8020/user/zhangyugu/weblog").map((_._2.toString)).countByValue()

      ssc.start()
      ssc.awaitTermination()
      ssc.stop(false,true)
  }
}

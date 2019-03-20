import org.apache.spark.sql.SparkSession

object SimpleApp {

  def main(args:Array[String]): Unit ={
    val logFile=""
    val spark=SparkSession.builder.appName("Simple Application").getOrCreate()
    val logData=spark.read.textFile(logFile).cache()
    val numAs=logData.filter(line=>line.contains("a")).count()
    println(s"Lines with a:$numAs")
    spark.stop()
  }

}

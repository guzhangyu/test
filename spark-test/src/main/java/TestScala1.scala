import com.google.common.base.Charsets
import org.apache.hadoop.io.{LongWritable, Text}

object TestScala1 {



  def main(args: Array[String]): Unit = {
    org.apache.hadoop.util.ReflectionUtils.newInstance(classOf[WebIpInputFormat1],null);

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
    val cls=classOf[WebIpInputFormat1].getDeclaredConstructor();
    println(cls);

    class WebIpInputFormat2(val a:Int){
      def this(){
        this(1)
      }
    }
  }
}

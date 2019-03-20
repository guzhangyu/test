import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class SimpleA {

    public static void main(String[] args) {
        String logFile="";
        SparkSession spark=SparkSession.builder().appName("Simple Application")
                .getOrCreate();
        Dataset<String> logData=spark.read().textFile(logFile).cache();

        long numAs=logData.filter((FilterFunction<String>) s->s.contains("a")).count();
        spark.stop();
    }
}

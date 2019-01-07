package hadoop.triangle;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TriangleMapper extends Mapper<LongWritable, Text,Text,Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String vs[]=value.toString().split(",");
        if(vs.length<2){
            return;
        }
        Text t1=new Text(vs[0]);
        Text t2=new Text(vs[1]);
        context.write(t1,t2);
        context.write(t2,t1);
    }
}

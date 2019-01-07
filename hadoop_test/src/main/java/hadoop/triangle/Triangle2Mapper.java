package hadoop.triangle;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Triangle2Mapper extends Mapper<LongWritable,Text,Text,Text> {

    Text BLANK=new Text("");

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String vs[]=value.toString().split("\\t");
        if(vs.length==1){
            context.write(new Text(vs[0]),BLANK);
            return;
        }

        context.write(new Text(vs[0]),new Text(vs[1]));
    }
}

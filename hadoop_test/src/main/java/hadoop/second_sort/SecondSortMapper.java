package hadoop.second_sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SecondSortMapper extends Mapper<LongWritable, Text,DateTemperaturePair,Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String lines[]=value.toString().split(",");
        DateTemperaturePair pair=new DateTemperaturePair();
        pair.yearMonth.set(lines[0]+"-"+lines[1]);
        pair.temperature.set(Integer.parseInt(lines[3]));
//        pair.day.set(lines[2]);
        context.write(pair,new Text(lines[3]));
    }
}

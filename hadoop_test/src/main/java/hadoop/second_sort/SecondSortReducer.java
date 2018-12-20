package hadoop.second_sort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SecondSortReducer extends Reducer<DateTemperaturePair, Text,Text,Text> {

    @Override
    protected void reduce(DateTemperaturePair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);;
        StringBuffer sb=new StringBuffer();
        for(Text value:values){
            sb.append(",").append(value.toString());
        }
        context.write(key.getYearMonth(),new Text(sb.substring(1)));
    }
}

package hadoop.top10_1;

import hadoop.top10.Tuple;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Top10_1UnUniqMapper extends Mapper<LongWritable, Text, Tuple,Text> {

    String sep=",";

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        sep=context.getConfiguration().get("sep");
    }

    List<DataTuple> list=new ArrayList<>(10);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String[] values=value.toString().split(sep);
        list.add(new DataTuple(Integer.parseInt(values[1]),values[0]));
        if(list.size()>10){
            Collections.sort(list);
            list.remove(0);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
        for(DataTuple dataTuple:list){
            context.write(new Tuple(dataTuple.getT2(),dataTuple.getT1()),new Text(dataTuple.getT2()));
        }
    }
}

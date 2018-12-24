package hadoop.top10_1;

import hadoop.top10.Tuple;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Top10_1UnUniqReducer extends Reducer<Tuple, Text,Text, IntWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    List<DataTuple> tuples=new ArrayList<>(11);

    @Override
    protected void reduce(Tuple key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);

        tuples.add(new DataTuple(key.getT2().get(),key.getT1().toString()));
        if(tuples.size()>10){
            Collections.sort(tuples);
            tuples.remove(0);
        }
    }


    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
        Collections.sort(tuples);
        for(DataTuple tuple:tuples){
            context.write(new Text(tuple.getT2()),new IntWritable(tuple.getT1()));
        }
    }
}

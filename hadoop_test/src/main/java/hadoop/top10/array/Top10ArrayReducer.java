package hadoop.top10.array;

import hadoop.common.TupleArray;
import hadoop.common.Tuple;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Top10ArrayReducer extends Reducer<Text, TupleArray,Text, Text> {

    Logger logger= LoggerFactory.getLogger(Top10ArrayReducer.class);

    @Override
    protected void reduce(Text key, Iterable<TupleArray> values, Context context) throws IOException, InterruptedException {
        List<Tuple> tuples=new ArrayList<>();
        for(ArrayWritable value:values){
            for(Writable writable:value.get()){
                tuples.add((Tuple) writable);
            }
        }
        Collections.sort(tuples);
        if(tuples.size()>10){
            tuples=tuples.subList(0,10);
        }

        context.write(null,new Text(tuples.toString()));
    }
}

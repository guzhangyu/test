package hadoop.top10;

import hadoop.common.Tuple;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Top10Reducer extends Reducer<Text, Tuple,Text, Text> {

    Logger logger= LoggerFactory.getLogger(Top10Reducer.class);

    int n;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        n=context.getConfiguration().getInt("n",0);
    }

    @Override
    protected void reduce(Text key, Iterable<Tuple> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        List<Tuple> tuples=new ArrayList<>();
        for(Tuple value:values){
            tuples.add(new Tuple(value.t1.toString(),value.t2.get()));
        }
        Collections.sort(tuples);
        if(tuples.size()>n){
            tuples=tuples.subList(0,n);
        }

        //job.setMapOutputKeyClass();
        context.write(key,new Text(tuples.toString()));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
//        context.get
    }
}

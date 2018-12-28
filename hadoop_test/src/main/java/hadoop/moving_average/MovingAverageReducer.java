package hadoop.moving_average;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MovingAverageReducer extends Reducer<Text, DoubleWritable,Text, DoubleWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        String k=key.toString();
        double a=0d;
        int c=0;
        for(DoubleWritable v:values){
            a+=v.get();
            c++;
        }
        context.write(new Text(k),new DoubleWritable(a/c));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

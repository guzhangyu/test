package hadoop.moving_average;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class MovingAverageQueueReducer extends Reducer<Text, DoubleWritable,Text, DoubleWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    Queue<Double> queue=new ArrayBlockingQueue<Double>(2);

    Queue<String> keyQueue=new ArrayBlockingQueue<String>(2);

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        for(DoubleWritable dw:values){
            queue.add(dw.get());
            keyQueue.add(key.toString());

            if(queue.size()>=2){
                String k=keyQueue.poll();
                Double v=queue.poll();

                double d=v;
                for(Double di:queue){
                    d+=di;
                }
                context.write(new Text(k),new DoubleWritable(d/2));
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}


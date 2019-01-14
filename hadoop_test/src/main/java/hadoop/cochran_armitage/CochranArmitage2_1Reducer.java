package hadoop.cochran_armitage;

import hadoop.common.TextTriple;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CochranArmitage2_1Reducer extends Reducer<TextTriple, IntWritable, Text, DoubleWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(TextTriple key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int[][] vs=new int[2][3];
        for(IntWritable t:values){
            vs[key.getT1().toString().equals("a")?0:1][Integer.parseInt(key.getT3().toString())]=t.get();
        }

        CochranArmitage instance=new CochranArmitage();
        instance.calc(vs);

        context.write(key.getT2(),new DoubleWritable(instance.getpValue()));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

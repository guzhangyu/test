package hadoop.cochran_armitage;

import hadoop.common.TextTriple;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CochranArmitageMapper extends Mapper<LongWritable, Text, TextTriple, IntWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    static String[] pairs=new String[]{"A","C","G","T","AC"};

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String v=value.toString();
        String[] vs=v.split(";");
        String[] vs1=vs[1].split(":");

        int size[]=new int[pairs.length];
        for(int i=1;i<3;i++){
            for(int j=0;j<pairs.length;j++){
                if(pairs[j].equals(vs1[i])){
                    size[j]++;
                }
            }
        }

        for(int i=0;i<size.length;i++){
            context.write(new TextTriple(vs1[0],pairs[i],size[i]+""),new IntWritable(1));
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

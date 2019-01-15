package hadoop.local_mem;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LocalMemMapper extends Mapper<LongWritable, Text,Text,Text> {

    LocalMemLRUMap localMemLRUMap=new LocalMemLRUMap();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String v=value.toString();
        String vs[]=v.split(";");
        String mapedV=(String) localMemLRUMap.get(vs[0],context.getConfiguration());

        context.write(new Text(mapedV),new Text(vs[1]));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

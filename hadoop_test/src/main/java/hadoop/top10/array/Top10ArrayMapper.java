package hadoop.top10.array;

import hadoop.top10.Tuple;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Top10ArrayMapper extends Mapper<LongWritable, Text,Text, TupleArray> {

    Pattern pattern=Pattern.compile("\\((\\w+),(\\d+)\\)");

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String [] values=value.toString().split("\t");

        TupleArray arrayWritable=new TupleArray();
        Matcher matcher=pattern.matcher(values[1]);
        List<Tuple> tuples=new ArrayList<>();
        while(matcher.find()){
            tuples.add(new Tuple(matcher.group(1),Integer.parseInt(matcher.group(2))));
        }

        arrayWritable.set(tuples.toArray(new Tuple[tuples.size()]));
        context.write(new Text(values[0]),arrayWritable);
    }
}

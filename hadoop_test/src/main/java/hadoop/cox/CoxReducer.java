package hadoop.cox;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CoxReducer extends Reducer<Text,Text,Text, Text> {

    List<String> bioset= Arrays.asList("8800");

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        String[] vs=new String[bioset.size()];
        for(Text value:values){
            String valueArr[]=value.toString().split(",");
            int index=bioset.indexOf(valueArr[0]);
            vs[index]=valueArr[1];
        }
        context.write(key,new Text(StringUtils.join(vs,";")));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

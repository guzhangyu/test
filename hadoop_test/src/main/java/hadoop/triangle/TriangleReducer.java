package hadoop.triangle;

import hadoop.common.HadoopStringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TriangleReducer extends Reducer<Text,Text,Text, Text> {

    Text BLANK=new Text("");

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        String k=key.toString();
        List<String> strs=new ArrayList<>();
        for(Text value:values){
            strs.add(value.toString());
            context.write(new Text(HadoopStringUtils.join(new String[]{k,value.toString()})),BLANK);
        }

        for(int i=0;i<strs.size()-1;i++){
            for(int j=i+1;j< strs.size();j++){
                context.write(new Text(HadoopStringUtils.join(strs.get(i),strs.get(j))),new Text(k));
            }
        }
    }
}

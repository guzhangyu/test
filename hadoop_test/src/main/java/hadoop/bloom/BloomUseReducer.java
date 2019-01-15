package hadoop.bloom;

import hadoop.common.TextPair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BloomUseReducer extends Reducer<TextPair, Text,Text,Text> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(TextPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        List<String> a=new ArrayList<>();
        List<String> b=new ArrayList<>();

        for(Text value:values){
            String v=value.toString();
            if(key.getT2().toString().equals("a")){
                a.add(v);
            }else{
                b.add(v);
            }
        }

        if(a.size()==0 || b.size()==0){
            return;
        }

        for(String ai:a){
            for(String bi:b){
                context.write(new Text(ai),new Text(bi));
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

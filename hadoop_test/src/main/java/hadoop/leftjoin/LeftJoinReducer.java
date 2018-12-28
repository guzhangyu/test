package hadoop.leftjoin;

import hadoop.common.Tuple;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeftJoinReducer extends Reducer<Text,Tuple, Text,Text> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Tuple> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        List<String> text1=new ArrayList<>();
        List<String> text2=new ArrayList<>();

        for(Tuple value:values){
            int type=value.getT2().get();
            if(type==1){
                text1.add(value.getT1().toString());
            }else{
                text2.add(value.getT1().toString());
            }
        }

        for(String a:text1){
            if(text2.size()>0){
                for(String b:text2){
                    context.write(new Text(a),new Text(b));
                }
            }else{
                context.write(new Text(a),null);
            }
        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

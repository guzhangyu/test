package hadoop.item_recommend.user_recommend;

import hadoop.common.TextPair;
import hadoop.common.Tuple;
import hadoop.common.TupleArray;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class Friends4UserReducer extends Reducer<TextPair, Tuple,Text, TupleArray> {


    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(TextPair key, Iterable<Tuple> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        String resultKey=key.getT1().toString();

        Map<String,Tuple> writables=new HashMap<>();
        String[] friends=null;
        for(Tuple tuple:values){
            if(key.getT2().toString().equals("")){
                String v=tuple.t1.toString();
                friends= v.substring(1,v.length()-1).split(",");
                continue;
            }
            String another=key.getT2().toString();
//            if(Arrays.binarySearch(friends,another)>=0){
//                continue;
//            }
            writables.put(another,new Tuple(String.format("%s: %s",another,tuple.t1.toString())
                    ,tuple.t2.get()));
        }

        if(friends!=null){
            for(String friend:friends){
                writables.remove(friend);
            }

            if(writables.size()>0){
                List<Tuple> list=new ArrayList<>(writables.values());
                Collections.sort(list);
                context.write(new Text(resultKey),new TupleArray(list.toArray(new Tuple[list.size()])));
            }
        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

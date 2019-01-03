package hadoop.markov.stage1;

import hadoop.common.TextPair2;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerTradeAggReducer extends Reducer<TextPair2, IntWritable,TextPair2, IntWritable> {

    IntWritable ONE=new IntWritable(1);

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(TextPair2 key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        Integer preV=null;
        Date preDate=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String preK=null;
        try {
            for(IntWritable value:values){
                Date newDate=sdf.parse(key.getT2().toString());
                int newV=value.get();
                if(preV!=null){
                    long days=(newDate.getTime()-preDate.getTime())/1000/3600/24;
                    String k=String.format("%s%s",days<30?"S":(days<60?"M":"L"),preV<0.9*newV?"L":(preV<1.1*newV?"E":"G"));
                    if(preK!=null){
                        context.write(new TextPair2(preK,k),ONE);
                    }

                    preK=k;
                }

                preV=newV;
                preDate=newDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

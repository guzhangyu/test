package hadoop.item_recommend.user_recommend;

import hadoop.common.TextPair;
import hadoop.common.Tuple;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Friends4UserMapper extends Mapper<LongWritable, Text, TextPair, Tuple> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    static Pattern pattern=Pattern.compile("\\((\\[[\\w ,]+\\]),(\\d+)\\)");

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String v=value.toString();
        String[] vs=v.split("\\t");
        Matcher matcher=pattern.matcher(vs[1]);
        if(matcher.find()){
            Tuple resultV=new Tuple(matcher.group(1),Integer.parseInt(matcher.group(2)));

            String[] v1s=vs[0].substring(1,vs[0].length()-1).split(",");
            if(v1s.length==1){
                context.write(new TextPair(v1s[0],""),resultV);
            }else{
                context.write(new TextPair(v1s[0],v1s[1]),resultV);
                context.write(new TextPair(v1s[1],v1s[0]),resultV);
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }

    public static void main(String[] args) {
        Matcher matcher=pattern.matcher( "([3, 4, 5, 7],4)");
        System.out.println(matcher.find());
//        String[] rs="(1,2)\t([3, 4, 5, 7],4)".split("\\t");
//        for(String s:rs){
//            System.out.println(s);
//        }
    }
}

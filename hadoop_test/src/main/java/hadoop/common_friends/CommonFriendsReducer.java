package hadoop.common_friends;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class CommonFriendsReducer extends Reducer<Text,Text, Text,Text> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        String[] ps=key.toString().split(",");
        Set<String>[] sets=new Set[2];
        int i=0;
        for(Text text:values){
            Set<String> s= Sets.newHashSet(text.toString().split(" "));
            s.remove(ps[0]);
            s.remove(ps[1]);
            sets[i++]=s;
        }
        if(sets[1]==null || sets[0]==null){
            return ;
        }
        sets[0].retainAll(sets[1]);

        String[] friends=sets[0].toArray(new String[sets[0].size()]);
        Arrays.sort(friends);
        context.write(key,new Text(StringUtils.join(friends,",")));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

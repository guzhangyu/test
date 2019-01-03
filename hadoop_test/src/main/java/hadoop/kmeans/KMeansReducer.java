package hadoop.kmeans;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KMeansReducer extends Reducer<IntWritable, Text,IntWritable, Text> {

    List<Integer[]> centers=new ArrayList<>();
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        String[] cts=context.getConfiguration().get("centers").split("\t");
        for(String ct:cts){
            Integer[] ctI = KMeansMapper.str2Ints(ct);
            centers.add(ctI);
        }
    }

    @Override
    protected void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        Integer[] a=null;
        double ct=0;
        for(Text v:values){
            Integer[] arr=KMeansMapper.str2Ints(v.toString());
            if(a==null){
                a=arr;
            }else{
                for(int i=0;i<arr.length;i++){
                    a[i]+=arr[i];
                }
            }
            ct++;
        }
        double [] b=new double[a.length];
        for(int i=0;i<a.length;i++){
            b[i]=a[i]/ct;
        }

        double distance=calcDistance(centers.get(key.get()),b);
        context.write(key,new Text(StringUtils.join(b,',')+"--"+distance));
    }

    static double calcDistance(Integer[] v, double[] center) {
        double a=0;
        for(int i=0;i<center.length;i++){
            a+=(center[i]-v[i])*(center[i]-v[i]);
        }
        return a;
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

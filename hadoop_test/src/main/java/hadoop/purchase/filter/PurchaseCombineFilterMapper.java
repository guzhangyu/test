//package hadoop.purchase.filter;
//
//import hadoop.common.DataTuples;
//import org.apache.commons.lang.StringUtils;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Mapper;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
//public class PurchaseCombineFilterMapper extends Mapper<LongWritable, Text,Text, IntWritable> {
//
//    int support;
//
//    @Override
//    protected void setup(Context context) throws IOException, InterruptedException {
//        super.setup(context);
//        support=context.getConfiguration().getInt("support",0);
//    }
//
//    @Override
//    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        String s=value.toString();
//        if(StringUtils.isEmpty(s)){
//            return;
//        }
//        String[] keys=s.split(",");
//
//        Set<DataTuples> list=new HashSet<>();
//        list.add(new DataTuples(Arrays.copyOfRange(keys,0,n)));
//        for(int i=n;i<keys.length;i++){
//            Set<DataTuples> newSet=new HashSet<>();
//            for(DataTuples aa:list){
//                String[] a=aa.getArr();
//                for(int j=0;j<a.length;j++){
//                    String[] tmp=a.clone();
//                    tmp[j]=keys[i];
//                    newSet.add(new DataTuples(tmp));
//                }
//            }
//            list.addAll(newSet);
//        }
//
//        for(DataTuples dt:list){
//            String[] ar=dt.getArr();
//            Arrays.sort(ar);
//            context.write(new Text(StringUtils.join(ar,",")),new IntWritable(1));
//        }
//    }
//
//    @Override
//    protected void cleanup(Context context) throws IOException, InterruptedException {
//        super.cleanup(context);
//    }
//}

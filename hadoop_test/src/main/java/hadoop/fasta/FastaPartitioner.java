package hadoop.fasta;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FastaPartitioner extends Partitioner<Text, IntWritable> {

    final static String[] vs=new String[]{"A","G","C","T"};

    @Override
    public int getPartition(Text text, IntWritable intWritable, int numPartitions) {
        String v= text.toString();
        for(int i=0;i<vs.length;i++){
            String v1=vs[i];
            if(v.equalsIgnoreCase(v1)){
                return i;
            }
        }

        return vs.length;
    }
}

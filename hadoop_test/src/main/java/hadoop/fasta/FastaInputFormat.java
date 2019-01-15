package hadoop.fasta;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import java.io.IOException;

public class FastaInputFormat extends FileInputFormat {
    @Override
    public RecordReader createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {

        return new LineRecordReader(){
            LongWritable newKey;

            Text newValue;


            public boolean nextKeyValue() throws IOException {
                long k=-1;
                StringBuffer v=new StringBuffer();
                while(super.nextKeyValue()){
                    if(k==-1){
                        k=super.getCurrentKey().get();
                    }
                    v.append(super.getCurrentValue()).append("\n");
                }

                if(k!=-1){
                    newKey=new LongWritable(k);
                    newValue=new Text(v.toString());
                }else{
                    newKey=getCurrentKey();
                    newValue=getCurrentValue();
                }
                return k!=-1;
            }

            public LongWritable getCurrentKey() {
                return newKey;
            }

            public Text getCurrentValue() {
                return newValue;
            }
        };


    }


}

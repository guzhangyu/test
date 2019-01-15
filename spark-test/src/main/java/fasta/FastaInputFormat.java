package fasta;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;

import java.io.IOException;

public class FastaInputFormat extends NLineInputFormat {
    @Override
    public RecordReader createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException {
        context.setStatus(split.toString());
        return new FastaLineRecordReader();
    }

}

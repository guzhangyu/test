package hadoop.small_files;

import hadoop.common.StringLongPair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SmallFilesFormatMapper extends Mapper<StringLongPair, Text,Text,Text> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(StringLongPair key, Text value, Context context) throws IOException, InterruptedException {
        context.write(key.getS(), value);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}

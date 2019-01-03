package hadoop.markov.stage2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

public class HadoopFileReader extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res= ToolRunner.run(new Configuration(),new HadoopFileReader(),args);
        System.exit(res);
    }

    @Override
    public int run(String[] strings) throws Exception {
        String preffix="/usr/local/Cellar/hadoop/test/";
        Path path=new Path(preffix+"markov_output");

        Configuration conf=getConf();
        conf.set("mapred.job.tracker", "local");
        conf.set("fs.default.name", "local");
        FileSystem fileSystem=path.getFileSystem(conf);

//        FSDataInputStream inputStream=fileSystem.open(path);
        RemoteIterator<LocatedFileStatus> iterator=fileSystem.listFiles(path,false);
        while(iterator.hasNext()){
            LocatedFileStatus status=iterator.next();
            System.out.println(status);
            System.out.println(status.getPath());

            RawLocalFileSystem rawLocalFileSystem=new RawLocalFileSystem();
            rawLocalFileSystem.initialize(new URI("file:///"+preffix),conf);
            FSDataInputStream fis=rawLocalFileSystem.open(status.getPath(),1024);
            byte[] bytes=new byte[1024];
            fis.read(bytes);
            System.out.println(new String(bytes));
        }
        System.out.println(((LocalFileSystem) fileSystem).pathToFile(path));

        return 0;
    }
}

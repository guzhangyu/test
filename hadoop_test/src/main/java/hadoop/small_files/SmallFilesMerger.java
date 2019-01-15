package hadoop.small_files;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmallFilesMerger extends Configured implements Tool {

    static String preffix="/usr/local/Cellar/hadoop/test/";
    //"hdfs://master:8020/";

    public static void main(String[] args) throws Exception {
        int res= ToolRunner.run(new Configuration(),new SmallFilesMerger(),args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        Configuration conf=getConf();
        conf.set("mapred.job.tracker", "local");
        conf.set("fs.default.name", "local");
        new SmallFilesMerger().split(preffix+"sfs",preffix+"sf_target/",conf);
        return 0;
    }

    ExecutorService executors= Executors.newFixedThreadPool(20);



    public void split(String prePath,String targetPath,Configuration conf) throws IOException, InterruptedException {
        Path path=new Path(prePath);
        FileSystem fileSystem=path.getFileSystem(conf);

//        FSDataInputStream inputStream=fileSystem.open(path);
        RemoteIterator<LocatedFileStatus> iterator=fileSystem.listFiles(path,false);
        List<String> files=new ArrayList<>();
        long size=0;
        String target=targetPath;
        int c=0;
        while(iterator.hasNext()){
            LocatedFileStatus status=iterator.next();
            files.add(status.getPath().toString());
            size+=status.getLen();

            if(size>=conf.getLong("file.blocksize",0)){
                List<String> files4Merge=new ArrayList<>(files);
                String targetName=target+(c++);
                executors.submit(() -> {
                    try {
                        merge(files4Merge,targetName,conf);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                files.clear();
                size=0;
            }
        }

        if(size>0){
            String targetName=target+(c++);
            executors.submit(() -> {
                try {
                    merge(files,targetName,conf);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(1000l);
        executors.shutdown();
//        while (true);
    }

    public void merge(List<String> files, String target,Configuration conf) throws IOException {
        FileSystem fileSystem=FileSystem.get(conf);
        FSDataOutputStream fos=fileSystem.create(new Path(target));
        for(String file:files){
            IOUtils.copy(fileSystem.open(new Path(file)),fos);
        }
        fos.close();
    }
}

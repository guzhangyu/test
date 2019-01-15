package hadoop.local_mem;

import org.apache.commons.collections.map.LRUMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocalMemLRUMap {

    private LRUMap vMap =new LRUMap(100);

    private LRUMap indexMap=new LRUMap(10);

    static String preffix="/usr/local/Cellar/hadoop/test/";

    public Object get(String key,Configuration conf) throws IOException {
        if(!vMap.containsKey(key)){
            String[] keys=key.split(",");
            fillVMapOut(keys[0],keys[1],conf);
        }
        return vMap.get(key);
    }

    public void fillVMapOut(String k1, String k2, Configuration conf) throws IOException {
        if(!indexMap.containsKey(k1)){
            FileSystem fileSystem=FileSystem.get(conf);
            FSDataInputStream inputStream=fileSystem.open(new Path(preffix+"local_mem/"+k1+"/index.txt"));
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));

            List<FilePos> list=new ArrayList<>();

            String line=null;
            while((line=br.readLine())!=null){
                String[] lines=line.split(":");

                String[] pos=lines[1].split(",");
                list.add(new FilePos(lines[0],Integer.parseInt(pos[0]),Integer.parseInt(pos[1])));
            }
            inputStream.close();

            indexMap.put(k1,list);
        }

        List<FilePos> list=(List<FilePos>)indexMap.get(k1);

        Integer k2V=Integer.parseInt(k2);
        int start=0,end=list.size()-1;
        while(start<=end){
            int mid=(start+end)/2;
            FilePos midPos=list.get(mid);
            if(midPos.getStart()>k2V){
                end=mid-1;
                continue;
            }

            //start <= k2V
            if(midPos.getStop()<k2V){
                start=mid+1;
                continue;
            }

            fillVMap(midPos.getFileName(),k1,conf);
            return;
        }
        throw new IllegalArgumentException("找不到该索引");
    }

    public void fillVMap(String fileName, String key, Configuration conf) throws IOException {
        FileSystem fileSystem=FileSystem.get(conf);
        FSDataInputStream inputStream=fileSystem.open(new Path(preffix+"local_mem/"+key+"/"+fileName));
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));

        String line=null;
        List<FilePos> list=new ArrayList<>();
        while((line=br.readLine())!=null){
            String[] lines=line.split(";");
            vMap.put(key+","+lines[0],lines[1]);
        }
        inputStream.close();
    }
}

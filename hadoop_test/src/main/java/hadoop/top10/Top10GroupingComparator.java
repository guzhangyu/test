package hadoop.top10;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Top10GroupingComparator extends WritableComparator {

    Logger logger= LoggerFactory.getLogger(Top10GroupingComparator.class);

    int N;

    public Top10GroupingComparator() {
        super(Text.class,true);

    }



    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        if(N==0){
            N=getConf().getInt("n",0);
        }
//        return super.compare(a, b);
        if(a==null || b==null){
            return -1;
        }
//        Text t1=(Text)a;
//        Text t2=(Text)b;
        int c= (a.hashCode()-b.hashCode())%N;
//        logger.debug(String.format("v1:%s,v2:%s,cp:%d",t1.toString(),t2.toString(),c));
        return c;
    }
}

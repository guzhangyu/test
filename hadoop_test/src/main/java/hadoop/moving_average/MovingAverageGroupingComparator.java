package hadoop.moving_average;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovingAverageGroupingComparator extends WritableComparator{


    private int ct=0;

    public MovingAverageGroupingComparator() {
        super(Text.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
//        return super.compare(a, b);
//        ct++;
//        ct%=2;

        Text t1=(Text)a;
        Text t2=(Text)b;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1=sdf.parse(t1.toString());
            Date d2=sdf.parse(t2.toString());
            long d=d1.getTime()-d2.getTime();
//            if(Math.abs(d)<=1000*3600*24*1){
//                if(ct==1){
//                    return 0;
//                }
//            }
            return d>0?1:-1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

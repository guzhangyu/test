package hadoop.second_sort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class DateTemperatureGroupingComparator extends WritableComparator {

    public DateTemperatureGroupingComparator() {
        super(DateTemperaturePair.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        DateTemperaturePair p1=(DateTemperaturePair)a;
        DateTemperaturePair p2=(DateTemperaturePair)b;
//        int com=p1.getYearMonth().compareTo(p2.getYearMonth());
//        if(com!=0){
//            return com;
//        }
//        com=p1.getTemperature().compareTo(p2.getTemperature());
//        return com;

        return DateTemperaturePair.compare(p1,p2);
    }
}

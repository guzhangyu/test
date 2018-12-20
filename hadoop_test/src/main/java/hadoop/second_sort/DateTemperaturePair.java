package hadoop.second_sort;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DateTemperaturePair implements Writable, WritableComparable<DateTemperaturePair> {


     Logger logger= LoggerFactory.getLogger(DateTemperaturePair.class);
     Text yearMonth=new Text();
//     Text day=new Text();
     IntWritable temperature=new IntWritable();

    @Override
    public int compareTo(DateTemperaturePair o) {
        int com=this.getYearMonth().compareTo(o.getYearMonth());
        if(com!=0){
            return com;
        }
        com=temperature.compareTo(o.getTemperature());
        return com;
    }

    public static int compare(DateTemperaturePair c,DateTemperaturePair o){
        int com=c.getYearMonth().compareTo(o.getYearMonth());
        if(com!=0){
            return com;
        }
        return com;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
//        dataOutput.write(yearMonth.getBytes());
//        dataOutput.writeChar(',');
//        dataOutput.write(day.getBytes());
//        dataOutput.writeChar(',');
//        dataOutput.write(temperature.get());
        logger.error("enter write");
        yearMonth.write(dataOutput);
       // day.write(dataOutput);
        temperature.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
//        String line=dataInput.readLine();
//        String[] lines=line.split(",");
//        yearMonth.set(lines[0]);
//        temperature.set(Integer.parseInt(lines[2]));
//        day.set(lines[1]);
        logger.error("enter readFields");
        yearMonth.readFields(dataInput);
        //day.readFields(dataInput);
        temperature.readFields(dataInput);
    }

    public Text getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(Text yearMonth) {
        this.yearMonth = yearMonth;
    }

//    public Text getDay() {
//        return day;
//    }
//
//    public void setDay(Text day) {
//        this.day = day;
//    }

    public IntWritable getTemperature() {
        return temperature;
    }

    public void setTemperature(IntWritable temperature) {
        this.temperature = temperature;
    }
}

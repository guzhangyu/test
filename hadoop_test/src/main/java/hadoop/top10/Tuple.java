package hadoop.top10;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Tuple implements Writable, WritableComparable<Tuple> {

    Text t1=new Text();
    IntWritable t2=new IntWritable();

    public Tuple(){

    }

    public Tuple(Text t1, IntWritable t2) {
        this.t1 = t1;
        this.t2 = t2;
    }


    public Tuple(String text,int i){
        t1.set(text);
        t2.set(i);
    }

    @Override
    public int compareTo(Tuple o) {
        int a=t2==null?0: t2.get();
        int b=o.t2==null?0:o.t2.get();
        return b-a;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        t1.write(dataOutput);
        t2.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        t1.readFields(dataInput);
        t2.readFields(dataInput);
    }

    @Override
    public String toString() {
        return String.format("(%s,%d)",t1.toString(),t2.get());
    }

    public Text getT1() {
        return t1;
    }

    public void setT1(Text t1) {
        this.t1 = t1;
    }

    public IntWritable getT2() {
        return t2;
    }

    public void setT2(IntWritable t2) {
        this.t2 = t2;
    }
}
